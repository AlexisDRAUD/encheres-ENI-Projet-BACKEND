package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.PasswordDto;
import fr.eni.ecole.enchereseniprojetbackend.Security.JwtUtils;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.LoginInput;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormInput;
import fr.eni.ecole.enchereseniprojetbackend.Security.UtilisateurSpringSecurity;
import fr.eni.ecole.enchereseniprojetbackend.DTO.response.JwtPayload;
import fr.eni.ecole.enchereseniprojetbackend.bll.SecurityService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UtilisateurService us;

	@Autowired
	SecurityService ss;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginInput loginInput) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginInput.getUsername(), loginInput.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) authentication.getPrincipal();

		return ResponseEntity.ok(new JwtPayload(jwt, userDetails.getUtilisateur().getId(), userDetails.getUtilisateur().isAdministrateur() ));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserFormInput userFormInput, BindingResult br) {
		Map<String, String> errors = new HashMap<>();

		if (br.hasErrors()) {
			for (FieldError error : br.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(errors);
		}

		if (us.usernameAlreadyExist(userFormInput.getUsername())) {
			errors.put("username", "Username is already taken!");
		}

		if (us.emailAlreadyExist(userFormInput.getEmail())) {
			errors.put("email", "Email is already in use!");
		}

		if (userFormInput.getPassword() != null) {
			if (!userFormInput.getPassword().isBlank()) {
				if (userFormInput.getPassword().length() > 6 && userFormInput.getPassword().length() < 30) {
					if (!us.isValidPassword(userFormInput.getPassword(), userFormInput.getPasswordConfirmation())) {
						errors.put("password", "Passwords do not match!");
					}
				} else {
					errors.put("password", "Le taille du mot de passe doit être compris entre 6 et 30!");
				}
			} else {
				errors.put("password", "Le mot de passe ne doit pas être vide!");
			}
		} else {
			errors.put("password", "Le mot de passe ne doit pas être nul!");
		}

		if (!errors.isEmpty()) {
			return ResponseEntity
					.badRequest()
					.body(errors);
		}

		us.addUser(userFormInput);

		return ResponseEntity.ok("User registered successfully!");
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestParam("email") String userEmail) {
		Map<String, String> errors = new HashMap<>();
		Utilisateur user = us.getUserByEmail(userEmail);
		if (user == null) {
			errors.put("email", "Cet email n'existe pas!");
			return ResponseEntity.badRequest().body(errors);
		}
		String token = UUID.randomUUID().toString();
		us.createPasswordResetTokenForUser(user, token);
		mailSender.send(constructResetTokenEmail(token, user));
		return ResponseEntity.ok("Email envoyé");
	}

	@PostMapping("/savePassword")
	public ResponseEntity<?> savePassword(@Valid @RequestBody PasswordDto passwordDto) {
		Map<String, String> errors = new HashMap<>();
		String result = ss.validatePasswordResetToken(passwordDto.getToken());

		if(result != null) {
			errors.put("url", "Ce lien n'est pas valide!");
			return ResponseEntity.badRequest().body(errors);
		}

		Utilisateur user = ss.getUserByPasswordResetToken(passwordDto.getToken());
		if (passwordDto.getPassword() != null) {
			if (!passwordDto.getPassword().isBlank()) {
				if (passwordDto.getPassword().length() > 6 && passwordDto.getPassword().length() < 30) {
					if (!us.isValidPassword(passwordDto.getPassword(), passwordDto.getPasswordConfirmation())) {
						errors.put("password", "Passwords do not match!");
					}
				} else {
					errors.put("password", "Le taille du mot de passe doit être compris entre 6 et 30!");
				}
			} else {
				errors.put("password", "Le mot de passe ne doit pas être vide!");
			}
		} else {
			errors.put("password", "Le mot de passe ne doit pas être nul!");
		}

		if (!errors.isEmpty()) {
			return ResponseEntity
					.badRequest()
					.body(errors);
		}

		us.changeUserPassword(user, passwordDto.getPassword());
		return ResponseEntity.ok("Password reset successfully!");
	}

	private SimpleMailMessage constructResetTokenEmail(String token, Utilisateur user) {
		String url = "http://localhost:3000/change-password/" + token;
		String message ="Cliquez sur le lien suivant pour modifier votre mot de passe : ";
		return constructEmail("Lien de changement de mot de passe", message + " \r\n" + url, user);
	}

	private SimpleMailMessage constructEmail(String subject, String body,
											 Utilisateur user) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom("enchere.app@outlook.fr");
		return email;
	}
}
