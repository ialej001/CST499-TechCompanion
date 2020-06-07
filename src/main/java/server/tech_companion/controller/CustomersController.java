package server.tech_companion.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import server.tech_companion.payload.CustomerJson;
import server.tech_companion.services.CustomerService;

@CrossOrigin(origins = { "http://localhost:9080", "http://localhost:8080" })
@RestController
@RequestMapping("/api")
public class CustomersController {
	@Autowired
    private CustomerService customerService;
	
    // find a customer
    @PostMapping("/findCustomer")
    @PreAuthorize("hasRole('OFFICE') or hasRole('ADMIN')")
    public ResponseEntity<List<CustomerJson>> findCustomer(@Valid @RequestBody Map<String, String> addressInfo) {
        List<CustomerJson> customers = customerService.fetchCustomerByStreetAddress(addressInfo.get("streetAddress"));
        return ResponseEntity.ok(customers);
    } 
    
    // find all customers
    @GetMapping("/customers")
    @PreAuthorize("hasRole('OFFICE') or hasRole('ADMIN')")
    public ResponseEntity<List<CustomerJson>> findCustomers() {
    	List<CustomerJson> customers = customerService.getCustomers();
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(customers);
        }
    }
    
    // create a customer
    @PostMapping("/customer/new")
    @PreAuthorize("hasRole('OFFICE') or hasRole('ADMIN')")
    public ResponseEntity<CustomerJson> createCustomer(@RequestBody CustomerJson json) {
    	CustomerJson customer = customerService.upsertCustomer(json);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/customer/new")
                    .buildAndExpand(customer.getString_id()).toUri();

            return ResponseEntity.created(uri).body(customer);
        }
    }
    
    // update one customer
    @PutMapping("/customer/{id}")
    @PreAuthorize("hasRole('OFFICE') or hasRole('ADMIN')")
    public ResponseEntity<CustomerJson> updateCustomer(@PathVariable String id, @RequestBody CustomerJson customer) {
    	CustomerJson updatedCustomer = customerService.upsertCustomer(customer);
        if (updatedCustomer == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedCustomer);
        }
    }
    
    // remove one customer
    @DeleteMapping("/customer/{id}")
    @PreAuthorize("hasRole('OFFICE') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(new ObjectId(id));
        return ResponseEntity.noContent().build();
    }
}
