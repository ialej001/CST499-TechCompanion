package server.tech_companion.models;

import java.time.LocalDateTime;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "workOrders")
public class WorkOrder {
    @Id
    private ObjectId _id;
    private String string_id;
    private Boolean isCompleted;

    private String customer_id;
    private List<Issue> issues;
    private String techAssigned;

    // technician input
    private List<SafetyChecklist> safetyChecklists;
    private List<String> workCompleted;
    private List<Part> partsUsed;
    private LocalDateTime timeStarted;
    private LocalDateTime timeEnded;
    private LocalDateTime dispatched;
    
    private Double subTotal;
    private Double total;
    private Double labor;
    private Double taxRate;
}



