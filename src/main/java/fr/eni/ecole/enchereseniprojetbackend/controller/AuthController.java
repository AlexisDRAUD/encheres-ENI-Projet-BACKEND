package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.Security.JwtUtils;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.LoginRequest;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormRequest;
import fr.eni.ecole.enchereseniprojetbackend.Security.UtilisateurSpringSecurity;
import fr.eni.ecole.enchereseniprojetbackend.DTO.response.JwtResponse;
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
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) authentication.getPrincipal();

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUtilisateur().getId()));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserFormRequest userFormRequest, BindingResult br) {
		Map<String, String> errors = new HashMap<>();

		if (br.hasErrors()) {
			for (FieldError error : br.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(errors);
		}

		if (us.usernameAlreadyExist(userFormRequest.getUsername())) {
			errors.put("username", "Username is already taken!");
		}

		if (us.emailAlreadyExist(userFormRequest.getEmail())) {
			errors.put("email", "Email is already in use!");
		}

		if (userFormRequest.getPassword() != null) {
			if (!userFormRequest.getPassword().isBlank()) {
				if (userFormRequest.getPassword().length() < 6 || userFormRequest.getPassword().length() > 30) {
					if (!us.isValidPassword(userFormRequest.getPassword(), userFormRequest.getPasswordConfirmation())) {
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

		us.addUser(userFormRequest);

		return ResponseEntity.ok("User registered successfully!");
	}
}
