package server.tech_companion.services;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import server.tech_companion.models.Customer;
import server.tech_companion.models.GateDetails;
import server.tech_companion.models.SafetyChecklist;
import server.tech_companion.repositories.SafetyChecklistRepository;

@AllArgsConstructor
@Service
public class SafetyChecklistService {
    @Autowired
    SafetyChecklistRepository checklistRepo;

    public void upsertChecklist(SafetyChecklist checklist) {
        checklistRepo.save(checklist);
    }

    public List<SafetyChecklist> fetchListsByServiceAddress(String address) {
        return checklistRepo.findByServiceAddress(address);
    }

    public SafetyChecklist fetchListById(ObjectId _id) {
        return checklistRepo.findBy_id(_id);
    }

    public List<SafetyChecklist> initializeLists(Customer customer) {
        List<SafetyChecklist> lists = new ArrayList<>();
        List<GateDetails> gateDetails = customer.getGateDetails();

        for (GateDetails gateDetail : gateDetails) {
            SafetyChecklist newChecklist = new SafetyChecklist();
            newChecklist.set_id(ObjectId.get());
            String serviceAddress = customer.getStreetAddress() + " " + customer.getCity() + ", CA"
                    + customer.getZipCode();

            newChecklist.setLocationOfGate(gateDetail.getLocation());
            newChecklist.setServiceAddress(serviceAddress);
            
            // TODO: SET OPERATOR DETAILS
            
            // newChecklist.setTypeOfGate(customer.getGateType().get(i));
            // newChecklist.setOperatorModel(customer.getOperatorModel().get(i));
            lists.add(newChecklist);
            checklistRepo.save(newChecklist);
        }

        return lists;
    }
}