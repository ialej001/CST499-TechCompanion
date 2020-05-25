package server.tech_companion.models.Json;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.tech_companion.models.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HistoryJson {
    private String string_id;
    private Boolean isCompleted;

    private CustomerJson customer;
    private List<Issue> issues;

    // technician input
    private String techAssigned;
    private List<Part> partsUsed;
    private LocalDateTime timeStarted;
    private LocalDateTime timeEnded;
    private LocalDateTime date; //deprecated
    
    private Double subTotal;
    private Double total;
    private Double labor;
    private Double taxRate;
    
//    public static List<HistoryJson> createJsonList(List<WorkOrder> workOrdersInDb, List<CustomerJson> customers) {
//        
//    	return customersJson;
//    }
}