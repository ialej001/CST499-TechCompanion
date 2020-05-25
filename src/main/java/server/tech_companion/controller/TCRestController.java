package server.tech_companion.controller;

import java.net.URI;
import java.net.URISyntaxException;
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

import server.tech_companion.models.WorkOrder;
import server.tech_companion.models.Json.*;
import server.tech_companion.services.*;

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

    /*#####################################
     * Dispatch end points                #
     #####################################*/
    
    // get all for one tech on a date (TODO: rework when authentication is added)
    @GetMapping("/{tech}/")
    public ResponseEntity<List<WorkOrder>> fetchAllForTechOnDate(@PathVariable String tech,
            @PathVariable LocalDateTime date) {
        List<WorkOrder> workOrders = workOrderService.fetchAllIncompleteForTech(tech);
        if (workOrders.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(workOrders);
        }

    }

    // get all for one tech (TODO: likely remove)
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
//    @GetMapping("/all")
//    public ResponseEntity<List<WorkOrder>> fetchAll() {
//        List<WorkOrder> workOrders = workOrderService.fetchAll();
//        if (workOrders.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(workOrders);
//        }
//    }

    // get incomplete
    @GetMapping("/incomplete")
    public ResponseEntity<List<DispatchJson>> fetchIncomplete() {
    	List<DispatchJson> workOrders = workOrderService.fetchIncompleteWO();
    	if (workOrders.isEmpty()) {
    		return ResponseEntity.notFound().build();
    	} else {
    		return ResponseEntity.ok(workOrders);
    	}
    }
    
    // get complete
    @GetMapping("/complete")
    public ResponseEntity<List<HistoryJson>> fetchComplete() {
    	List<HistoryJson> workOrders = workOrderService.fetchCompleteWO();
    	if (workOrders.isEmpty()) {
    		return ResponseEntity.notFound().build();
    	} else {
    		return ResponseEntity.ok(workOrders);
    	}
    }
    
    // create work order - working 4/29
    @PostMapping("/dispatch/work-order")
    public ResponseEntity<DispatchJson> dispatchWorkOrder(@Valid @RequestBody DispatchJson json)
            throws URISyntaxException {

        DispatchJson workOrder = workOrderService.dispatchWorkOrder(json);
        if (workOrder == null) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/dispatch/work-order")
                    .buildAndExpand(workOrder.getWorkOrder_id()).toUri();

            return ResponseEntity.created(uri).body(workOrder);
        }
    }

    // update one work order
    @PutMapping("/update/{id}")
    public ResponseEntity<DispatchJson> updateWorkOrderFromOffice(@PathVariable String id,
            @Valid @RequestBody DispatchJson json) {
    	DispatchJson updatedWorkOrder = workOrderService.updateWorkOrderFromOffice(id, json);
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

    /***************
     ** Customers **
     **************/

    // find a customer
    @PostMapping("/findCustomer")
    public ResponseEntity<List<CustomerJson>> findCustomer(@Valid @RequestBody Map<String, String> addressInfo) {
        List<CustomerJson> customers = customerService.fetchCustomerByStreetAddress(addressInfo.get("streetAddress"));
        return ResponseEntity.ok(customers);
    } 
    
    // find all customers
    @GetMapping("/customers")
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
    public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
    	System.out.println(id.getClass());
    	System.out.println(id);
        customerService.deleteCustomer(new ObjectId(id));
        return ResponseEntity.noContent().build();
    }
    
    /*###################
     * Inventory(Parts) #
     ##################*/
    
    // get all items
    @GetMapping("/parts/all")
    public ResponseEntity<List<PartJson>> findAllParts() {
    	List<PartJson> parts = partsService.getParts();
        if (parts.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(parts);
        }
    }
    
    @PutMapping("/parts/update/{id}")
    public ResponseEntity<PartJson> updatePart(@PathVariable String id, @RequestBody PartJson part) {
    	PartJson updatedPart = partsService.updatePart(part);
        if (updatedPart == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedPart);
        }
    }
    
    @DeleteMapping("/parts/delete/{id}")
    public ResponseEntity<?> deletePart(@PathVariable String id) {
    	partsService.deletePart(new ObjectId(id));
    	return ResponseEntity.noContent().build();
    }
}