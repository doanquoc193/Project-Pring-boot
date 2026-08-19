package com.quoc.identity.service.dto.request;

import com.quoc.identity.service.validator.DobConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;


import java.time.LocalDate;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserCreationRequest {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 5, message = "USERNAME_INVALID")
     String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 9, message = "INVALID_PASSWORD")
    String password;

    @NotBlank(message = "Firstname must not be blank")
    String firstname;

    @NotBlank(message = "Lastname must not be blank")
    String lastname;

    @Past(message = "Date of birth must be in the past")
    @DobConstraint( min = 18, message = "INVALID_DOB")
    LocalDate dob;

    List <String> roles;




}