package server.tech_companion.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.leangen.graphql.annotations.GraphQLQuery;
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
    @GraphQLQuery(name = "_id", description = "optional")
    private String string_id;
    @GraphQLQuery(name = "isCompleted")
    private Boolean isCompleted = false;

    @GraphQLQuery(name = "customer", description = "optional")
    private Customer customer;
    @GraphQLQuery(name = "issues", description = "optional")
    private List<Issue> issues;

    // technician input
    @GraphQLQuery(name = "techAssigned", description = "optional")
    private String techAssigned;
    @GraphQLQuery(name = "safetyChecklists", description = "optional")
    private List<SafetyChecklist> safetyChecklists;
    @GraphQLQuery(name = "workCompleted", description = "optional")
    private List<String> workCompleted;
    @GraphQLQuery(name = "partsUsed", description = "optional")
    private List<Part> partsUsed;
    @GraphQLQuery(name = "timeStarted", description = "optional")
    private LocalTime timeStarted;
    @GraphQLQuery(name = "timeEnded", description = "optional")
    private LocalTime timeEnded;
    @GraphQLQuery(name = "dateDispatched", description = "optional")
    private LocalDate date;
}



