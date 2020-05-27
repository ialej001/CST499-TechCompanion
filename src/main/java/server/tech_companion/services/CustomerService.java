package server.tech_companion.services;

import java.time.LocalDateTime;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import server.tech_companion.config.CommonMethods;
import server.tech_companion.models.Customer;
import server.tech_companion.models.Json.CustomerJson;
import server.tech_companion.repositories.CustomerRepository;

@Service
@Component
@GraphQLApi
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepo = customerRepository;
    }

    public CustomerJson fetchCustomerByString_id(String id) {
        Customer customerInDb = customerRepo.findBy_id(new ObjectId(id));
        CustomerJson json = new CustomerJson();
        CommonMethods.copyNonNullProperties(customerInDb, json);
        return json;
    }

    public List<CustomerJson> fetchCustomerByStreetAddress(String streetAddress) {
        List<Customer> customersInDb = customerRepo.findByStreetAddress(streetAddress);
        List<CustomerJson> customersJson = CustomerJson.createJsonList(customersInDb);
        return customersJson;
    }

    public CustomerJson upsertCustomer(CustomerJson json) {
    	System.out.println("*** Customer ***");
    	System.out.println("Received:");
    	System.out.println(json);
    	
    	// if id is null, it's a new customer
        if (json.getString_id() == null) {
        	Customer newCustomer = new Customer();
        	CommonMethods.copyNonNullProperties(json, newCustomer);
        	
        	ObjectId newId = ObjectId.get();
        	newCustomer.set_id(newId);
        	
        	newCustomer.setString_id(newId.toString());
        	newCustomer.setServiceAddress(json.getStreetAddress() + " " +
        			json.getCity() + ", CA " + json.getZipCode());
        	newCustomer.setCreatedAt(LocalDateTime.now());
            customerRepo.save(newCustomer);
            
            json.setString_id(newCustomer.getString_id());
            System.out.println("Returning:");
            System.out.println(json);
            System.out.println("*** End ***");
            return json;
        } else {
        	Customer updateCustomer = customerRepo.findBy_id(
        			new ObjectId(json.getString_id()));
        	System.out.println("Lookup:");
        	System.out.println(updateCustomer);
        	CommonMethods.copyNonNullProperties(json, updateCustomer);
        	customerRepo.save(updateCustomer);
        	return json;
        }
    }

    public List<CustomerJson> getCustomers() {
        return CustomerJson.createJsonList(customerRepo.findAll());
    }
    
    // working 5/18
    public void deleteCustomer(ObjectId _id) {
    	customerRepo.delete(customerRepo.findBy_id(_id));
    }
    
}

