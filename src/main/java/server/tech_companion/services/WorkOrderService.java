package server.tech_companion.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import server.tech_companion.config.CommonMethods;
import server.tech_companion.models.Part;
import server.tech_companion.models.WorkOrder;
import server.tech_companion.models.Json.*;
import server.tech_companion.repositories.PartsRepository;
import server.tech_companion.repositories.WorkOrderRepo;

@Service
@Component
@GraphQLApi
public class WorkOrderService {
    @Autowired
    private WorkOrderRepo workOrderRepo;
    @Autowired
    private PartsRepository partsRepo;
    @Autowired
    private CustomerService customerService;

    public WorkOrderService(WorkOrderRepo workOrderRepo) {
        this.workOrderRepo = workOrderRepo;
    }

    // get all for one tech on a date
    public List<WorkOrder> fetchAllIncompleteForTech(String tech) {
        return workOrderRepo.findByTechAssignedAndIsCompleted(tech, false);
    }

    // get all completed
    public List<HistoryJson> fetchCompleteWO() {
        List<WorkOrder> completeWorkOrders = workOrderRepo.findByIsCompleted(true);
        List<HistoryJson> customersJson = new ArrayList<HistoryJson>();
    	for(WorkOrder workOrder : completeWorkOrders) {
        	HistoryJson json = new HistoryJson();
        	CommonMethods.copyNonNullProperties(workOrder, json);
        	CustomerJson customerJson = customerService.fetchCustomerByString_id(workOrder.getCustomer_id());
        	json.setCustomer(customerJson);
        	customersJson.add(json);
        }
        return customersJson;
    }
    
    // get all incompleted
    public List<DispatchJson> fetchIncompleteWO() {
        List<WorkOrder> incompleteWorkOrders = workOrderRepo.findByIsCompleted(false);
        List<DispatchJson> customersJson = new ArrayList<DispatchJson>();
    	for(WorkOrder workOrder : incompleteWorkOrders) {
    		DispatchJson json = new DispatchJson();
        	CommonMethods.copyNonNullProperties(workOrder, json);
        	CustomerJson customerJson = customerService.fetchCustomerByString_id(workOrder.getCustomer_id());
        	json.setCustomer(customerJson);
        	customersJson.add(json);
        }
        return customersJson;
    }

    public List<WorkOrder> fetchAllForTech(String tech) {
        return workOrderRepo.findByTechAssigned(tech);
    }

    // delete one work order - completed 4/30
    public void deleteWorkOrder(ObjectId id) {
        workOrderRepo.delete(workOrderRepo.findBy_id(id));
    }

    // create work order
    public DispatchJson dispatchWorkOrder(DispatchJson json) {
        WorkOrder workOrder = new WorkOrder();
        ObjectId new_id = ObjectId.get();
        workOrder.set_id(new_id);
        workOrder.setString_id(new_id.toString());
        CommonMethods.copyNonNullProperties(json, workOrder);

        CustomerJson editedCustomer = customerService.upsertCustomer(json.getCustomer());
        workOrder.setCustomer_id(editedCustomer.getString_id());

        // find and attach relevant safety checklists

        workOrder.setIsCompleted(false);
        workOrder.setDispatched(LocalDateTime.now());
        workOrderRepo.save(workOrder);
        
        json.setWorkOrder_id(workOrder.getString_id());
        json.setDispatched(workOrder.getDispatched());
        return json;
    }

    // update one work order from office (typos, missing info)
    public DispatchJson updateWorkOrderFromOffice(String id, DispatchJson json) {
    	System.out.println(id);
        WorkOrder workOrder = workOrderRepo.findBy_id(new ObjectId(json.getWorkOrder_id()));
        System.out.println(json);
        
        CommonMethods.copyNonNullProperties(json, workOrder);
        workOrderRepo.save(workOrder);

        // update any changes to customer, then reflect those changes back to json
        CustomerJson editedCustomer = customerService.upsertCustomer(json.getCustomer());
        json.setCustomer(editedCustomer);

        return json;
    }

    // update workorder from tech
    public WorkOrder completeWorkOrder(
    		String id, 
    		WorkOrder body) {
    	System.out.println(body.getIssues());
        WorkOrder workOrder = workOrderRepo.findBy_id(new ObjectId(id));
        workOrder.setPartsUsed(body.getPartsUsed());
        workOrder.setIssues(body.getIssues());
        workOrder.setTimeStarted(body.getTimeStarted());
        workOrder.setTimeEnded(body.getTimeEnded());
        workOrder.setIsCompleted(true);

        workOrderRepo.save(workOrder);
        return workOrder;
    }

    // parts database access
    public List<Part> getParts() {
    	return partsRepo.findAll();
    }
    
}