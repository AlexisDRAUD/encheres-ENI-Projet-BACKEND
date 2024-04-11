package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.Security.JwtUtils;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.LoginInput;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormInput;
import fr.eni.ecole.enchereseniprojetbackend.Security.UtilisateurSpringSecurity;
import fr.eni.ecole.enchereseniprojetbackend.DTO.response.JwtPayload;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

		return ResponseEntity.ok(new JwtPayload(jwt, userDetails.getUtilisateur().getId()));
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
				if (userFormInput.getPassword().length() < 6 || userFormInput.getPassword().length() > 30) {
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
}
