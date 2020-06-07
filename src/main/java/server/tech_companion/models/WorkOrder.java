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
import server.tech_companion.payload.CustomerJson;

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
    private Double tax;
    
    public static Double getLaborCharges(CustomerJson customer, Double timeElapsed) {
    	// most calls are under an hour, so return if less than 1 hour
        if (timeElapsed <= 60) 
        	return customer.getLaborRate();
        else {
        	// if we go over, then pro rate every half hour past the full hour
        	timeElapsed -= 60;
        	Double halfHourBlocks = 2 + (timeElapsed / 30);
        	// check for remainder. if we have any remainder amount, add another block
        	if (timeElapsed % 30 > 0) {
        		halfHourBlocks++;
        	}
        	
        	// set our labor charge
        	return (customer.getLaborRate() * (halfHourBlocks / 2));
        }
    }
}



