package com.quoc.identity.service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.quoc.identity.service.dto.request.AuthenticationRequest;
import com.quoc.identity.service.dto.request.IntrospectRequest;
import com.quoc.identity.service.dto.request.LogoutRequest;
import com.quoc.identity.service.dto.request.RefreshRequest;
import com.quoc.identity.service.dto.response.AuthenticationResponse;
import com.quoc.identity.service.dto.response.IntrospectResponse;
import com.quoc.identity.service.entity.User;
import com.quoc.identity.service.exception.AppException;
import com.quoc.identity.service.exception.ErrorCode;
import com.quoc.identity.service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    RedisService redisService;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected  String SIGNAL_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected  long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected  long REFRESHABLE_DURATION;

    public IntrospectResponse introspect(
            IntrospectRequest request
    ) throws JOSEException, ParseException {

        var token = request.getToken();

        try {

            verifyToken(token);

        } catch (AppException e) {

            return IntrospectResponse.builder()
                    .valid(false)
                    .build();
        }

        return IntrospectResponse.builder()
                .valid(true)
                .build();
    }

    public AuthenticationResponse authenticate(
            AuthenticationRequest request
    ) {

        var user = userRepository.findByUsername(
                request.getUsername()
        ).orElseThrow(
                () -> new AppException(
                        ErrorCode.USER_NOT_EXISTED
                )
        );

        PasswordEncoder passwordEncoder =
                new BCryptPasswordEncoder(10);

        boolean authenticated =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!authenticated) {
            throw new AppException(
                    ErrorCode.UNAUTHENTICATED
            );
        }

        // Access Token
        var accessToken =
                generateToken(
                        user,
                        VALID_DURATION,
                        "ACCESS"
                );

        // Refresh Token
        var refreshToken =
                generateToken(
                        user,
                        REFRESHABLE_DURATION,
                        "REFRESH"
                );

        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    public void logout(
            LogoutRequest request
    ) throws ParseException, JOSEException {

        try {

            var signedToken =
                    verifyRefreshToken(request.getToken());

            String jti =
                    signedToken
                            .getJWTClaimsSet()
                            .getJWTID();

            Date expiryTime =
                    signedToken
                            .getJWTClaimsSet()
                            .getExpirationTime();

            long ttl =
                    expiryTime.getTime()
                            - System.currentTimeMillis();

            if (ttl > 0) {

                redisService.invalidateToken(
                        jti,
                        Duration.ofMillis(ttl)
                );
            }

        } catch (AppException exception) {

            log.info(
                    "Token already expired or invalid"
            );
        }
    }

    private SignedJWT verifyToken(
            String token
    ) throws ParseException, JOSEException {

        JWSVerifier verifier =
                new MACVerifier(
                        SIGNAL_KEY.getBytes()
                );

        SignedJWT signedJWT =
                SignedJWT.parse(token);

        var verified =
                signedJWT.verify(verifier);

        Date expiryTime =
                signedJWT
                        .getJWTClaimsSet()
                        .getExpirationTime();

        if (!(verified && expiryTime.after(new Date()))) {

            throw new AppException(
                    ErrorCode.UNAUTHENTICATED
            );
        }

        String jti =
                signedJWT
                        .getJWTClaimsSet()
                        .getJWTID();

        if (redisService.isTokenInvalidated(jti)) {

            throw new AppException(
                    ErrorCode.UNAUTHENTICATED
            );
        }

        return signedJWT;
    }

    private SignedJWT verifyRefreshToken(
            String token
    ) throws ParseException, JOSEException {

        var signedJWT =
                verifyToken(token);

        String tokenType =
                (String) signedJWT
                        .getJWTClaimsSet()
                        .getClaim("token_type");

        if (!"REFRESH".equals(tokenType)) {

            throw new AppException(
                    ErrorCode.UNAUTHENTICATED
            );
        }

        return signedJWT;
    }

    public AuthenticationResponse refreshToken(
            RefreshRequest request
    ) throws ParseException, JOSEException {

        var signedJWT =
                verifyRefreshToken(request.getToken());

        var jti =
                signedJWT
                        .getJWTClaimsSet()
                        .getJWTID();

        var expiryTime =
                signedJWT
                        .getJWTClaimsSet()
                        .getExpirationTime();

        long ttl =
                expiryTime.getTime()
                        - System.currentTimeMillis();

        if (ttl > 0) {

            redisService.invalidateToken(
                    jti,
                    Duration.ofMillis(ttl)
            );
        }

        var username =
                signedJWT
                        .getJWTClaimsSet()
                        .getSubject();

        var user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(
                                () -> new AppException(
                                        ErrorCode.UNAUTHENTICATED
                                )
                        );

        // Tạo Access Token mới
        var accessToken =
                generateToken(
                        user,
                        VALID_DURATION,
                        "ACCESS"
                );

        // Tạo Refresh Token mới
        var refreshToken =
                generateToken(
                        user,
                        REFRESHABLE_DURATION,
                        "REFRESH"
                );

        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    private String generateToken(
            User user,
            long duration,
            String tokenType
    ) {

        JWSHeader header =
                new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimSet =
                new JWTClaimsSet.Builder()
                        .subject(user.getUsername())
                        .issuer("quoc.com")
                        .issueTime(new Date())
                        .expirationTime(
                                new Date(
                                        Instant.now()
                                                .plus(
                                                        duration,
                                                        ChronoUnit.SECONDS
                                                )
                                                .toEpochMilli()
                                )
                        )
                        .jwtID(
                                UUID.randomUUID().toString()
                        )
                        .claim(
                                "token_type",
                                tokenType
                        )
                        .claim(
                                "scope",
                                buildScope(user)
                        )
                        .build();

        Payload payload =
                new Payload(
                        jwtClaimSet.toJSONObject()
                );

        JWSObject jwsObject =
                new JWSObject(
                        header,
                        payload
                );

        try {

            jwsObject.sign(
                    new MACSigner(
                            SIGNAL_KEY.getBytes()
                    )
            );

            return jwsObject.serialize();

        } catch (JOSEException e) {

            log.error(
                    "cannot create token",
                    e
            );

            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");

        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions()
                        .forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }



}
