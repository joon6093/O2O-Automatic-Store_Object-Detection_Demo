package com.iia.store.dto.sign;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Email(message = "{signUpRequest.email.email}")
    @NotBlank(message = "{signUpRequest.email.notBlank}")
    private String email;

    @NotBlank(message = "{signUpRequest.password.notBlank}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
             message = "{signUpRequest.password.pattern}")
    private String password;

    @NotBlank(message = "{signUpRequest.username.notBlank}")
    @Size(min=2, message = "{signUpRequest.username.size}")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "{signUpRequest.username.pattern}")
    private String username;

    @NotBlank(message = "{signUpRequest.nickname.notBlank}")
    @Size(min=2, message = "{signUpRequest.nickname.size}")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "{signUpRequest.nickname.pattern}")
    private String nickname;
}