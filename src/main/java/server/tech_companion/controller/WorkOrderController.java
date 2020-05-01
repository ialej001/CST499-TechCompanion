package server.tech_companion.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
import server.tech_companion.models.WorkOrder;
import server.tech_companion.services.CustomerService;
import server.tech_companion.services.WorkOrderService;

@CrossOrigin(origins = { "http://localhost:9080", "http://localhost:8080" })
@RestController
@RequestMapping("/api")
public class WorkOrderController {
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private CustomerService customerService;

    // get all for one tech on a date
    @GetMapping("/{tech}/{date}")
    public ResponseEntity<List<WorkOrder>> fetchAllForTechOnDate(@PathVariable String tech, @PathVariable LocalDate date) {
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
    public ResponseEntity<WorkOrder> updateWorkOrderFromOffice(@PathVariable String id, @Valid @RequestBody WorkOrder json) {
        WorkOrder updatedWorkOrder = workOrderService.updateWorkOrderFromOffice(id, json);
        if (updatedWorkOrder == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedWorkOrder);
        }
    }

    // complete work order from tech
    @PutMapping("/complete/{id}")
    public ResponseEntity<WorkOrder> completeWorkOrder(@PathVariable String id, @Valid @RequestBody WorkOrder json) {
        // System.out.println(json.toString());
        WorkOrder updatedWorkOrder = workOrderService.completeWorkOrder(id, json);
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
    **Customers**
    *************/

    // find a customer
    @PostMapping("/findCustomer")
    public ResponseEntity<Customer> findCustomer(@Valid @RequestBody Map<String, String> serviceAddressInfo) {
        Customer customer = customerService.fetchCustomerByServiceAddress(serviceAddressInfo.get("serviceAddress"));
        if (customer == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(customer);
        }
    }
}