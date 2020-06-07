package server.tech_companion.payload;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.tech_companion.models.Issue;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DispatchJson {
	private String string_id;
    private CustomerJson customer;
    private String callType;
    private List<Issue> issues;
    private String techAssigned;
    private LocalDateTime dispatched;
}

