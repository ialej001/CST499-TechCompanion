package server.tech_companion.models;

import java.time.LocalDateTime;
import java.util.List;
import com.mongodb.lang.NonNull;
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
@Document(collection = "customers")
public class Customer {
    @Id
    private ObjectId _id;
    private LocalDateTime createdAt;

    // property info
    @GraphQLQuery(name = "propertyName", description = "optional")
    private String propertyName;

    @GraphQLQuery(name = "streetAddress", description = "A person's name")
    @NonNull
    private String streetAddress;

    @GraphQLQuery(name = "city", description = "A person's name")
    @NonNull
    private String city;

    @GraphQLQuery(name = "zipCode", description = "A person's name")
    @NonNull
    private Integer zipCode;

    @GraphQLQuery(name = "propertyType", description = "A person's name")
    @NonNull
    private String propertyType;

    @GraphQLQuery(name = "serviceAddress", description = "A person's name")
    private String serviceAddress;

    // customer info
    @NonNull
    @GraphQLQuery(name = "contactName", description = "A person's name")
    private String contactName;

    @NonNull
    @GraphQLQuery(name = "contactPhone", description = "A person's name")
    private String contactPhone;

    @GraphQLQuery(name = "contactEmail", description = "A person's name")
    private String contactEmail;

    @GraphQLQuery(name = "billingMethod", description = "A person's name")
    private String billingMethod;

    // gate details
    @GraphQLQuery(name = "gateLocations", description = "A person's name")
    private List<String> gateLocations;

    @GraphQLQuery(name = "accessCodes", description = "A person's name")
    private List<String> accessCodes;

    @GraphQLQuery(name = "operatorModel", description = "A person's name")
    private List<String> operatorModel;

    @GraphQLQuery(name = "gateType", description = "A person's name")
    private List<String> gateType;

    @GraphQLQuery(name = "isMasterSlave", description = "A person's name")
    private List<Boolean> isMasterSlave;
    
}