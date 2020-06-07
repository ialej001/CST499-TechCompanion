package server.tech_companion.payload.auth;

import java.util.Set;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
	@NotBlank
	@Size(min = 3, max = 25)
	private String username;
	
	@NotBlank
	@Email
	private String email;
	
	private Set<String> roles;
	
	@NotBlank
	@Size(min = 6, max = 40)
	private String password;
	
}
