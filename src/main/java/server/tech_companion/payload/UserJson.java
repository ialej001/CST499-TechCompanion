package server.tech_companion.payload;

import lombok.Data;

@Data
public class UserJson {
	private String id;
	private String username;
	private String password;
	private String email;
	private String role;
}
