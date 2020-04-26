package server.tech_companion.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;

import server.tech_companion.models.WorkOrder;
import server.tech_companion.services.WorkOrderService;

@RestController
@RequestMapping("/api")
public class WorkOrderController {
    @Autowired
    private WorkOrderService workOrderService;

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

    // create work order
    @PostMapping("/dispatch/{tech}")
    public ResponseEntity<WorkOrder> dispatchWorkOrder(@PathVariable String tech, @Valid @RequestBody WorkOrder json)
            throws URISyntaxException {

        WorkOrder workOrder = workOrderService.dispatchWorkOrder(tech, json);
        if (workOrder == null) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/dispatch/{tech}")
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteWorkOrder(@PathVariable ObjectId id) {
        workOrderService.deleteWorkOrder(id);
        return ResponseEntity.noContent().build();
    }
}