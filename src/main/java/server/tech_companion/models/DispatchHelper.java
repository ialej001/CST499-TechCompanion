package server.tech_companion.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DispatchHelper {
    private Customer customer;
    private String callType;
    private List<Issue> issues;
    private String techAssigned;
}

