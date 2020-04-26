package server.tech_companion.repositories;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import server.tech_companion.models.Part;

@Repository
public interface PartsRepository extends MongoRepository<Part, ObjectId> {
    Part findByName(String name);

    Part findBy_id(ObjectId _id);

    List<Part> findAll();
}