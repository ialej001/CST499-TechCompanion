package server.tech_companion.payload;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.tech_companion.config.CommonMethods;
import server.tech_companion.models.Customer;
import server.tech_companion.models.GateDetails;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerJson {
	private String error;
	
    private String string_id;
    private LocalDateTime createdAt;

    // property info
    private String propertyName;
    private String streetAddress;
    private String city;
    private Integer zipCode;
    private String propertyType;
    private String serviceAddress;
    private String managementCompany;

    // customer info
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String billingMethod;
    private Double laborRate;
    private Double taxRate;

    // gate details
    private List<GateDetails> gateDetails;
    
    public static List<CustomerJson> createJsonList(List<Customer> customersInDb) {
        List<CustomerJson> customersJson = new ArrayList<CustomerJson>();
    	for(Customer customer : customersInDb) {
        	CustomerJson json = new CustomerJson();
        	CommonMethods.copyNonNullProperties(customer, json);
        	json.setServiceAddress(customer.getStreetAddress() + " " + customer.getCity() + ", CA "
                    + customer.getZipCode());
        	customersJson.add(json);
        }
    	return customersJson;
    }
}