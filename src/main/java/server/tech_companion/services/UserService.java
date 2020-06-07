package server.tech_companion.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import server.tech_companion.config.CommonMethods;
import server.tech_companion.models.ERole;
import server.tech_companion.models.Role;
import server.tech_companion.models.User;
import server.tech_companion.payload.UserJson;
import server.tech_companion.repositories.RolesRepository;
import server.tech_companion.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepo;
	@Autowired
	RolesRepository rolesRepo;
	
	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	// create found in UserDetailServices under authentication
	
	// read all
	public List<UserJson> getUsers() {
		List<User> usersInDb = userRepo.findAll();
		List<UserJson> usersToSend = new ArrayList<UserJson>();
		
		for (User user : usersInDb) {
			UserJson userToSend = new UserJson();
			CommonMethods.copyNonNullProperties(user, userToSend);
			userToSend.setId(user.get_id().toHexString());
			userToSend.setRole(getRole(user.getRoles()));
			usersToSend.add(userToSend);
		}
		
		return usersToSend;
	}
	
	// update
	public UserJson updateUser(UserJson user) {
		User updatedUser = userRepo.findBy_id(new ObjectId(user.getId()));
		CommonMethods.copyNonNullProperties(user, updatedUser);
		
		Set<Role> roles = new HashSet<>();
		System.out.println(user.getRole());
		switch (user.getRole()) {
		case "admin": 
			roles.add(rolesRepo.findByRole(ERole.ROLE_ADMIN));
			updatedUser.setRoles(roles);
			break;
		case "tech":
			roles.add(rolesRepo.findByRole(ERole.ROLE_TECH));
			updatedUser.setRoles(roles);
			break;
		default:
			roles.add(rolesRepo.findByRole(ERole.ROLE_OFFICE));
			updatedUser.setRoles(roles);
			break;
		}
		
		userRepo.save(updatedUser);
		return user;
	}
	
	// delete	
	public void deleteUser(ObjectId id) {
		userRepo.delete(userRepo.findBy_id(id));
	}
	
	// private helper function
	private String getRole(Set<Role> roles) {
		for (Role role : roles) {
			if (role.getRole() == ERole.ROLE_ADMIN) return "admin";
			if (role.getRole() == ERole.ROLE_TECH) return "tech";
		}
		return "office";
	}
}
