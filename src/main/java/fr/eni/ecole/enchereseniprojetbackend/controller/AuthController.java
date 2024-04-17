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

		errors = us.registerUser(userFormInput, errors);

		if (!errors.isEmpty()) {
			return ResponseEntity
					.badRequest()
					.body(errors);
		}

		return ResponseEntity.ok("User registered successfully!");
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestParam("email") String userEmail) {
		Map<String, String> errors = new HashMap<>();

		us.resetPassword(userEmail, errors);

		if (errors.isEmpty()) {
			return ResponseEntity.ok("Email envoyé");
		} else {
			return ResponseEntity.badRequest().body(errors);
		}
	}

	@PostMapping("/savePassword")
	public ResponseEntity<?> savePassword(@Valid @RequestBody PasswordDto passwordDto) {
		Map<String, String> errors = new HashMap<>();

		errors = us.savePassword(passwordDto, errors);

		if (!errors.isEmpty()) {
			return ResponseEntity
					.badRequest()
					.body(errors);
		}

		return ResponseEntity.ok("Password reset successfully!");
	}
}
