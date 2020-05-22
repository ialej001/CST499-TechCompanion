package server.tech_companion.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;
import server.tech_companion.models.Customer;
import server.tech_companion.models.DispatchHelper;
import server.tech_companion.models.Issue;
import server.tech_companion.models.Part;
import server.tech_companion.models.WorkOrder;
import server.tech_companion.services.CustomerService;
import server.tech_companion.services.PartsService;
import server.tech_companion.services.WorkOrderService;

//@CrossOrigin(origins = { "http://localhost:9080", "http://localhost:8080" })
@CrossOrigin
@RestController
@RequestMapping("/api")
public class TCRestController {
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PartsService partsService;

    // get all for one tech on a date
    @GetMapping("/{tech}/{date}")
    public ResponseEntity<List<WorkOrder>> fetchAllForTechOnDate(@PathVariable String tech,
            @PathVariable LocalDateTime date) {
        List<WorkOrder> workOrders = workOrderService.fetchAllForTechOnDate(tech, date);
        if (workOrders.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(workOrders);
        }

    }

    // get all for one tech
    @GetMapping("/{tech}")
    public ResponseEntity<List<WorkOrder>> fetchAllForTech(@PathVariable String tech) {
        List<WorkOrder> workOrders = workOrderService.fetchAllForTech(tech);
        if (workOrders.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(workOrders);
        }
    }

    // get all
    @GetMapping("/all")
    public ResponseEntity<List<WorkOrder>> fetchAll() {
        List<WorkOrder> workOrders = workOrderService.fetchAll();
        if (workOrders.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(workOrders);
        }
    }

    // get incomplete
    @GetMapping("/incomplete")
    public ResponseEntity<List<WorkOrder>> fetchIncomplete() {
    	List<WorkOrder> workOrders = workOrderService.fetchIncompleteWO(false);
    	if (workOrders.isEmpty()) {
    		return ResponseEntity.notFound().build();
    	} else {
    		return ResponseEntity.ok(workOrders);
    	}
    }
    
    // get complete
    @GetMapping("/complete")
    public ResponseEntity<List<WorkOrder>> fetchComplete() {
    	List<WorkOrder> workOrders = workOrderService.fetchIncompleteWO(true);
    	if (workOrders.isEmpty()) {
    		return ResponseEntity.notFound().build();
    	} else {
    		return ResponseEntity.ok(workOrders);
    	}
    }
    // create work order - working 4/29
    @PostMapping("/dispatch/work-order")
    public ResponseEntity<WorkOrder> dispatchWorkOrder(@Valid @RequestBody DispatchHelper json)
            throws URISyntaxException {

        WorkOrder workOrder = workOrderService.dispatchWorkOrder(json);
        if (workOrder == null) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/dispatch/work-order")
                    .buildAndExpand(workOrder.get_id()).toUri();

            return ResponseEntity.created(uri).body(workOrder);
        }
    }

    // update one work order
    @PutMapping("/update/{id}")
    public ResponseEntity<WorkOrder> updateWorkOrderFromOffice(@PathVariable String id,
            @Valid @RequestBody WorkOrder json) {
        WorkOrder updatedWorkOrder = workOrderService.updateWorkOrderFromOffice(id, json);
        if (updatedWorkOrder == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedWorkOrder);
        }
    }

    // complete work order from tech
    @PutMapping("/complete/{id}")
    public ResponseEntity<WorkOrder> completeWorkOrder(
    		@PathVariable String id, 
    		@Valid @RequestBody WorkOrder body) {
        WorkOrder updatedWorkOrder = workOrderService.completeWorkOrder(id, body);
        if (updatedWorkOrder == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedWorkOrder);
        }
    }

    // delete one work order
    @DeleteMapping("/work-order/{id}")
    public ResponseEntity<?> deleteWorkOrder(@PathVariable String id) throws URISyntaxException {
        workOrderService.deleteWorkOrder(new ObjectId(id));
        return ResponseEntity.noContent().build();
    }

    /************
     ** Customers**
     *************/

    // find a customer
    @PostMapping("/findCustomer")
    public ResponseEntity<List<Customer>> findCustomer(@Valid @RequestBody Map<String, String> addressInfo) {
        List<Customer> customers = customerService.fetchCustomerByStreetAddress(addressInfo.get("streetAddress"));
        return ResponseEntity.ok(customers);
    } 
    
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> findCustomers() {
    	List<Customer> customers = customerService.getCustomers();
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(customers);
        }
    }
    
    @PostMapping("/customer/new")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer json) {
        Customer customer = customerService.upsertCustomer(json);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/customer/new")
                    .buildAndExpand(customer.get_id()).toUri();

            return ResponseEntity.created(uri).body(customer);
        }
    }
    
    @PutMapping("/customer/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
    	Customer updatedCustomer = customerService.upsertCustomer(customer);
        if (updatedCustomer == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedCustomer);
        }
    }
    
    @DeleteMapping("/customer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
    	System.out.println(id.getClass());
    	System.out.println(id);
        customerService.deleteCustomer(new ObjectId(id));
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/parts/all")
    public ResponseEntity<List<Part>> findAllParts() {
    	List<Part> parts = partsService.getParts();
        if (parts.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(parts);
        }
    }
}