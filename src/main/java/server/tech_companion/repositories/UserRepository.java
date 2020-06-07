package server.tech_companion.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import server.tech_companion.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
	User findBy_id(ObjectId id);
	
	User findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
}
