package server.tech_companion.models;

import java.util.List;
import com.mongodb.lang.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "Customers")
public class Customer {
    @Id
    private ObjectId _id;

    // property info
    private String propertyName;
    @NonNull
    private String streetAddress;
    @NonNull
    private String city;
    @NonNull
    private Integer zipCode;
    @NonNull
    private String propertyType;
    private String serviceAddress;

    // customer info
    @NonNull
    private String contactName;
    @NonNull
    private String contactPhone;
    private String contactEmail;
    private String billingMethod;

    // gate details
    private List<String> gateLocations;
    private List<String> accessCodes;
    private List<String> operatorModel;
    private List<String> gateType;
    private List<Boolean> isMasterSlave;
    
}