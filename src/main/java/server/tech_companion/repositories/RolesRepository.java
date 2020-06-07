package server.tech_companion.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import server.tech_companion.models.ERole;
import server.tech_companion.models.Role;

@Repository
public interface RolesRepository extends MongoRepository<Role, ObjectId>{

	Role findByRole(ERole role);
}
