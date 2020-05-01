package server.tech_companion.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "workOrders")
public class WorkOrder {
    @Id
    private ObjectId _id;
    private String string_id;

    private Customer customer;
    private List<Issue> issues;

    // technician input
    private String techAssigned;
    private List<SafetyChecklist> safetyChecklists;
    private List<String> workCompleted;
    private List<Part> partsUsed;
    private LocalTime timeStarted;
    private LocalTime timeEnded;
    private LocalDate date;
}



