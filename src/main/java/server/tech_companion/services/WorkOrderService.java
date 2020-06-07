package server.tech_companion.services;

import java.time.Duration;
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
import server.tech_companion.payload.*;
import server.tech_companion.repositories.PartsRepository;
import server.tech_companion.repositories.WorkOrderRepo;

@Service
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
    public List<WorkOrderJson> fetchAllIncompleteForTech(String tech) {
    	// TODO: CHANGE THE WORKORDER REPO CALL TO INCLUDE TECH WHEN THAT CHANGE IS MADE
        List<WorkOrder> workOrders = workOrderRepo.findByIsCompleted(false);
        List<WorkOrderJson> workOrderJson = new ArrayList<WorkOrderJson>();
        
        for(WorkOrder workOrder : workOrders) {
        	WorkOrderJson json = new WorkOrderJson();
        	CommonMethods.copyNonNullProperties(workOrder, json);
        	json.setStringId(workOrder.getString_id());
        	json.setCustomer(customerService.fetchCustomerByString_id(workOrder.getCustomer_id()));
        	workOrderJson.add(json);
        }
        
        return workOrderJson;
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
        	System.out.println(json);
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
        	if (customerJson == null)
        		json.setCustomer(new CustomerJson());
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
        
        json.setString_id(workOrder.getString_id());
        json.setDispatched(workOrder.getDispatched());
        return json;
    }

    // update one work order from office (typos, missing info)
    public DispatchJson updateWorkOrderFromOffice(String id, DispatchJson json) {
    	System.out.println(id);
        WorkOrder workOrder = workOrderRepo.findBy_id(new ObjectId(json.getString_id()));
        System.out.println(json);
        
        CommonMethods.copyNonNullProperties(json, workOrder);
        workOrderRepo.save(workOrder);

        // update any changes to customer, then reflect those changes back to json
        CustomerJson editedCustomer = customerService.upsertCustomer(json.getCustomer());
        json.setCustomer(editedCustomer);

        return json;
    }

    // complete workorder from tech
    public WorkOrderJson completeWorkOrder(
    		String id, 
    		WorkOrderJson json) {
    	System.out.println("inside completeWO");
    	System.out.println(json);
        WorkOrder workOrder = workOrderRepo.findBy_id(new ObjectId(id));
        workOrder.setPartsUsed(json.getPartsUsed());
        workOrder.setIssues(json.getIssues());
        workOrder.setTimeStarted(json.getTimeStarted());
        workOrder.setTimeEnded(json.getTimeEnded());
        workOrder.setIsCompleted(true);
        
        // calculate charges
        // get time spent in minutes
        
        Duration duration = Duration.between(json.getTimeStarted(), json.getTimeEnded());
        // duration to minutes results in long primitive
        Double timeElapsed = (double) Math.abs(duration.toMinutes()); 
        
        Double labor, subTotal, tax;
        labor = WorkOrder.getLaborCharges(json.getCustomer(), timeElapsed);
        workOrder.setLabor(labor);
        
        subTotal = Part.getSubtotal(json.getPartsUsed());
        workOrder.setSubTotal(subTotal);
        
        // tax rate is inputed as double percentage (ie 9.75%, not 0.0975)
        // also protect against flat rates (9% --> 9.0%)
        if (subTotal == 0.0) {
        	workOrder.setTax(0.0);
        	workOrder.setTotal(labor + subTotal);
        } else {
	        tax = workOrder.getSubTotal() * (json.getCustomer().getTaxRate() / 100.0);
	        workOrder.setTax(tax);
	        workOrder.setTotal(labor + subTotal + tax);
        }

        workOrderRepo.save(workOrder);
        return json;
    }

    // parts database access
    public List<Part> getParts() {
    	return partsRepo.findAll();
    }
    
}