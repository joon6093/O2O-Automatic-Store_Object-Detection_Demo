package com.iia.store.dto.sign;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    @Email(message = "{signInRequest.email.email}")
    @NotBlank(message = "{signInRequest.email.notBlank}")
    private String email;

    @NotBlank(message = "{signInRequest.password.notBlank}")
    private String password;
}
