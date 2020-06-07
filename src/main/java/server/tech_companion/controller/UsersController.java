package server.tech_companion.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.tech_companion.payload.UserJson;
import server.tech_companion.services.UserService;

@CrossOrigin(origins = { "http://localhost:9080", "http://localhost:8080" })
@RestController
@RequestMapping("/api")
public class UsersController {
	@Autowired
	UserService userService;

	
	@GetMapping("/users/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserJson>> getUsers() {
		List<UserJson> users = userService.getUsers();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users);
        }
	}
	
	// update
	@PutMapping("/users/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserJson user) {
		UserJson updatedUser = userService.updateUser(user);
		if (updatedUser == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(updatedUser);
		}
	}
	
	// delete
	@DeleteMapping("/users/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUser(@PathVariable String id) {
		userService.deleteUser(new ObjectId(id));
		return ResponseEntity.noContent().build();
	}
}
