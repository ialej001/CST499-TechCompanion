package server.tech_companion.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;

import server.tech_companion.payload.*;
import server.tech_companion.services.*;

@CrossOrigin(origins = { "http://localhost:9080", "http://localhost:8080" })
@RestController
@RequestMapping("/api")
public class DispatchController {
    @Autowired
    private WorkOrderService workOrderService;
    
    // get incomplete
    @GetMapping("/incomplete")
    @PreAuthorize("hasRole('OFFICE') or hasRole('ADMIN')")
    public ResponseEntity<List<DispatchJson>> fetchIncomplete() {
    	List<DispatchJson> workOrders = workOrderService.fetchIncompleteWO();
    	if (workOrders.isEmpty()) {
    		return ResponseEntity.notFound().build();
    	} else {
    		return ResponseEntity.ok(workOrders);
    	}
    }
    
    // create work order - working 4/29
    @PostMapping("/dispatch/work-order")
    @PreAuthorize("hasRole('OFFICE') or hasRole('ADMIN')")
    public ResponseEntity<DispatchJson> dispatchWorkOrder(@Valid @RequestBody DispatchJson json)
            throws URISyntaxException {

        DispatchJson workOrder = workOrderService.dispatchWorkOrder(json);
        if (workOrder == null) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/dispatch/work-order")
                    .buildAndExpand(workOrder.getString_id()).toUri();

            return ResponseEntity.created(uri).body(workOrder);
        }
    }

    // update one work order
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('OFFICE') or hasRole('ADMIN')")
    public ResponseEntity<DispatchJson> updateWorkOrderFromOffice(@PathVariable String id,
            @Valid @RequestBody DispatchJson json) {
    	DispatchJson updatedWorkOrder = workOrderService.updateWorkOrderFromOffice(id, json);
        if (updatedWorkOrder == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedWorkOrder);
        }
    }

    // delete one work order
    @DeleteMapping("/work-order/{id}")
    @PreAuthorize("hasRole('OFFICE') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteWorkOrder(@PathVariable String id) throws URISyntaxException {
    	ObjectId _id = new ObjectId(id);
        workOrderService.deleteWorkOrder(_id);
        return ResponseEntity.noContent().build();
    }
}