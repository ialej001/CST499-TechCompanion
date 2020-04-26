package server.tech_companion.repositories;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import server.tech_companion.models.SafetyChecklist;

@Repository
public interface SafetyChecklistRepository extends MongoRepository<SafetyChecklist, ObjectId> 
{
    SafetyChecklist findBy_id(ObjectId _id);

    List<SafetyChecklist> findByServiceAddress(String address);
}