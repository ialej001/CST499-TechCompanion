package server.tech_companion.models;

import java.time.LocalDateTime;
import java.util.List;
import com.mongodb.lang.NonNull;
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
@Document(collection = "customers")
public class Customer {
    @Id
    private ObjectId _id;
    private String string_id;
    private LocalDateTime createdAt;

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
    private String managementCompany;

    // customer info
    @NonNull
    private String contactName;
    @NonNull
    private String contactPhone;
    private String contactEmail;
    private String billingMethod;
    private Double laborRate;
    private Double taxRate;

    // gate details
    private List<GateDetails> gateDetails;
}