package server.tech_companion.models.Json;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.tech_companion.models.Issue;
import server.tech_companion.models.Part;
import server.tech_companion.models.SafetyChecklist;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WorkOrderJson {
    private String stringId;
    private Boolean isCompleted;

    private CustomerJson customer;
    private List<Issue> issues;

    // technician input
    private String techAssigned;
    private List<SafetyChecklist> safetyChecklists;
    private List<String> workCompleted;
    private List<Part> partsUsed;
    private LocalDateTime timeStarted;
    private LocalDateTime timeEnded;
    private LocalDateTime date; //deprecated
    
    private Double subTotal;
    private Double total;
    private Double labor;
    private Double tax;
}



