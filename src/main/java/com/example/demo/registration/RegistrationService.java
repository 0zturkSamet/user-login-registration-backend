package com.example.demo.registration;

import com.example.demo.dto.RegistrationDto;
import org.springframework.stereotype.Service;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserRole;
import com.example.demo.appuser.AppUserService;
import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
	private final EmailValidator emailValidator;
	private final AppUserService appUserService;
	private final ConfirmationTokenService confirmationTokenService;

	public String register(RegistrationDto request) {
		boolean isValidEmail = emailValidator.test(request.getEmail());
		if(!isValidEmail) {
			throw new IllegalStateException("Email is not valid");
		}

		return appUserService.signUpUser(
				new AppUser(
						request.getFirstName(),
						request.getLastName(),
						request.getEmail(),
						request.getPassword(),
						AppUserRole.USER
				)
		);
	}

	public void confirmToken(String token) {
		ConfirmationToken confirmationToken = confirmationTokenService
				.getToken(token)
				.orElseThrow(() -> new IllegalStateException("Token not found"));

		if (confirmationToken.getConfirmedAt() != null) {
			throw new IllegalStateException("Email already confirmed");
		}

		LocalDateTime expiredAt = confirmationToken.getExpiresAt();

		if (expiredAt.isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("Token expired");
		}

		confirmationTokenService.setConfirmedAt(token);
		appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
	}
}
