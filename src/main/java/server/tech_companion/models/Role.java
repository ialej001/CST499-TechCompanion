package server.tech_companion.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "roles")
public class Role {
	@Id
	private ObjectId id;
	@Indexed(unique = true, direction = IndexDirection.DESCENDING)
	private ERole role;
	
	public Role(ERole role) {
		this.role = role;
	}
}
