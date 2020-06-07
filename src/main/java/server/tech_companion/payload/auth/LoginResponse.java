package server.tech_companion.payload.auth;

import java.util.List;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
public class LoginResponse {
	private String token;
	private String type = "Bearer";
	private ObjectId id;
	private String username;
	private String email;
	private List<String> roles;
	
	public LoginResponse(
			String accessToken, 
			ObjectId id, 
			String username, 
			String email, 
			List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}
}
