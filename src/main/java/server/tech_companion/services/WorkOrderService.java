package server.tech_companion.services;

import java.beans.PropertyDescriptor;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.tech_companion.models.SafetyChecklist;
import server.tech_companion.models.WorkOrder;
import server.tech_companion.repositories.WorkOrderRepo;

@Service
public class WorkOrderService {
    @Autowired
    private WorkOrderRepo workOrderRepo;
    @Autowired
    private SafetyChecklistService checklistService;

    public WorkOrderService(WorkOrderRepo workOrderRepo) {
        this.workOrderRepo = workOrderRepo;
    }

    // get all for one tech on a date
    public List<WorkOrder> fetchAllForTechOnDate(String tech, LocalDate date) {
        return workOrderRepo.findByTechAssignedAndDate(tech, date);
    }

    // get all for one tech
    public List<WorkOrder> fetchAllForTech(String tech) {
        return workOrderRepo.findByTechAssigned(tech);
    }

    // get all
    public List<WorkOrder> fetchAll() {
        return workOrderRepo.findAll();
    }

    // delete one work order
    public void deleteWorkOrder(ObjectId id) {
        workOrderRepo.delete(workOrderRepo.findBy_id(id));
    }

    // create work order
    public WorkOrder dispatchWorkOrder(String techAssigned, WorkOrder json) {
        WorkOrder workOrder = new WorkOrder();
        ObjectId new_id = ObjectId.get();
        workOrder.set_id(new_id);
        workOrder.setString_id(new_id.toHexString());
        copyNonNullProperties(json, workOrder);

        // find and attach relevant safety checklists
        String serviceAddress = json.getCustomer().getStreetAddress() + " " + json.getCustomer().getCity()
                + ", " + json.getCustomer().getZipCode();
        List<SafetyChecklist> allChecklistsInDb = checklistService.fetchListsByServiceAddress(serviceAddress);
        if (allChecklistsInDb.isEmpty()) {
            System.out.println("empty checklist");
            workOrder.setSafetyChecklists(checklistService.initializeLists(json.getCustomer()));
        } else {
            System.out.println("exisitng checklists");
            workOrder.setSafetyChecklists(allChecklistsInDb);
        }

        workOrderRepo.save(workOrder);

        return workOrder;
    }

    // update one work order from office (typos, missing info)
    public WorkOrder updateWorkOrderFromOffice(String id, WorkOrder json) {
        WorkOrder workOrder = workOrderRepo.findBy_id(new ObjectId(id));
        copyNonNullProperties(json, workOrder);

        workOrderRepo.save(workOrder);

        return workOrder;
    }

    // update workorder from tech
    public WorkOrder completeWorkOrder(String id, WorkOrder json) {
        WorkOrder workOrder = workOrderRepo.findBy_id(new ObjectId(id));
        copyNonNullProperties(json, workOrder);

        workOrderRepo.save(workOrder);
        return workOrder;
    }

    // some helper methods - code refactored
    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }
    
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
    
        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}