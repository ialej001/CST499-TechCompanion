package server.tech_companion.repositories;

import java.time.LocalDate;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import server.tech_companion.models.WorkOrder;

@Repository
public interface WorkOrderRepo extends MongoRepository<WorkOrder, ObjectId>
{
    List<WorkOrder> findByTechAssignedAndDate(String tech, LocalDate date);

    List<WorkOrder> findByTechAssigned(String tech);

    WorkOrder findBy_id(ObjectId _id);

    List<WorkOrder> findByIsCompleted(Boolean isCompleted);
}