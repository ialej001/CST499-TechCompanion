package server.tech_companion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.tech_companion.payload.HistoryJson;
import server.tech_companion.services.WorkOrderService;

@CrossOrigin(origins = { "http://localhost:9080", "http://localhost:8080" })
@RestController
@RequestMapping("/api")
public class HistoryController {
    @Autowired
    private WorkOrderService workOrderService;
    
    // get complete
    @GetMapping("/complete")
    @PreAuthorize("hasRole('OFFICE') or hasRole('ADMIN')")
    public ResponseEntity<List<HistoryJson>> fetchComplete() {
    	List<HistoryJson> workOrders = workOrderService.fetchCompleteWO();
    	if (workOrders.isEmpty()) {
    		return ResponseEntity.notFound().build();
    	} else {
    		return ResponseEntity.ok(workOrders);
    	}
    }
}
