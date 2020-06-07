package server.tech_companion.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.tech_companion.models.WorkOrder;
import server.tech_companion.payload.WorkOrderJson;
import server.tech_companion.services.WorkOrderService;

@CrossOrigin(origins = { "http://localhost:9080", "http://localhost:8080" })
@RestController
@RequestMapping("/api")
public class TechnicianController {
    @Autowired
    private WorkOrderService workOrderService;
    
    // get all for one tech on a date (TODO: rework when authentication is added)
    @GetMapping("/incomplete/{tech}")
    @PreAuthorize("hasRole('TECH') or hasRole('ADMIN')")
    public ResponseEntity<List<WorkOrderJson>> fetchAllIncompleteForTech(@PathVariable String tech) {
        List<WorkOrderJson> workOrders = workOrderService.fetchAllIncompleteForTech(tech);
        System.out.println("Sending tech this:");
        System.out.println(workOrders);
        if (workOrders.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(workOrders);
        }
    }
    
    // get all for one tech (TODO: likely remove)
    @GetMapping("/{tech}")
    @PreAuthorize("hasRole('TECH') or hasRole('ADMIN')")
    public ResponseEntity<List<WorkOrder>> fetchAllForTech(@PathVariable String tech) {
        List<WorkOrder> workOrders = workOrderService.fetchAllForTech(tech);
        if (workOrders.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(workOrders);
        }
    }
    
    // complete work order from tech
    @PutMapping("/complete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<WorkOrderJson> completeWorkOrder(
    		@PathVariable String id, 
    		@Valid @RequestBody WorkOrderJson body) {
    	System.out.println(body);
        WorkOrderJson updatedWorkOrder = workOrderService.completeWorkOrder(id, body);
        if (updatedWorkOrder == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedWorkOrder);
        }
    }
}
