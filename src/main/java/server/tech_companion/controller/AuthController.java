package server.tech_companion.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.tech_companion.authentication.CustomUserDetails;
import server.tech_companion.authentication.JwtUtils;
import server.tech_companion.models.ERole;
import server.tech_companion.models.Role;
import server.tech_companion.models.User;
import server.tech_companion.payload.auth.*;
import server.tech_companion.repositories.RolesRepository;
import server.tech_companion.repositories.UserRepository;

@CrossOrigin(origins = { "http://localhost:9080", "http://localhost:8080" })
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	UserRepository users;
	@Autowired
	RolesRepository rolesRepo;
	@Autowired
	PasswordEncoder encoder;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest data) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateToken(authentication);
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new LoginResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}
	
	@PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		System.out.println("register");
		System.out.println(request);
		if (users.existsByUsername(request.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new ErrorResponse("Error: Username is already taken!"));
		}

		if (users.existsByEmail(request.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new ErrorResponse("Error: Email is already in use!"));
		}

		
		// Create new user's account
		User user = new User(
				request.getUsername(), 
				request.getEmail(),
				encoder.encode(request.getPassword())
				);

		Set<String> strRoles = request.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			try {
				Role userRole = rolesRepo.findByRole(ERole.ROLE_OFFICE);
				roles.add(userRole);	
			} catch (Exception e) {
				throw new RuntimeException("Error: Role is not found.");
			}
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					try {
						Role adminRole = rolesRepo.findByRole(ERole.ROLE_ADMIN);
						roles.add(adminRole);	
					} catch (Exception e) {
						throw new RuntimeException("Error: Role is not found.");
					}
					break;
				case "tech": {
					try {
						Role techRole = rolesRepo.findByRole(ERole.ROLE_TECH);
						roles.add(techRole);
					} catch (Exception e) {
						throw new RuntimeException("Error: Role is not found.");
					}
				}
				default:
					try {
						Role userRole = rolesRepo.findByRole(ERole.ROLE_OFFICE);
						roles.add(userRole);
					} catch (Exception e) {
						throw new RuntimeException("Error: Role is not found.");
					}
				}
			});
		}

		user.setRoles(roles);
		System.out.println(user);
		users.save(user);

		return ResponseEntity.ok(new ErrorResponse("User registered successfully!"));
	}
}
