package com.bonsai.accountservice.controllers.login;

import com.bonsai.accountservice.config.TokenHandler;
import com.bonsai.accountservice.dto.request.UserCredentialDto;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.accountservice.services.UserCredentialService;
import com.bonsai.sharedservice.dtos.response.ErrorResponse;
import com.bonsai.sharedservice.dtos.response.LoginResponse;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserCredentialService userCredentialService;

    @PostMapping
    public ResponseEntity<?> loginUser(@RequestBody UserCredentialDto user) {

        UserCredentialDto foundUser = userCredentialService.findByEmailAndRole(user.email(), user.role());

        if (foundUser == null || !bCryptPasswordEncoder.matches(user.password(), foundUser.password())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Login failed"));
        }

        return ResponseEntity.ok().body(
                new SuccessResponse("Login successful as " + foundUser.role(),
                        new LoginResponse("Access token generated",
                                TokenHandler.generateToken(foundUser.email())
                        )
                )
        );
    }
}
