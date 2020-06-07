package server.tech_companion.models;

import java.util.Set;
import javax.validation.constraints.NotEmpty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
	@Id
	ObjectId _id;
	
	@Indexed(unique = true, direction = IndexDirection.DESCENDING)
	private String username;
	
	@NotEmpty
	private String password;
	
//	private boolean enabled;
	
	private String email;
	
	@DBRef
	private Set<Role> roles;
	
	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}
}
