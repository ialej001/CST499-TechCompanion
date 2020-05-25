package server.tech_companion.services;

import server.tech_companion.config.CommonMethods;
import server.tech_companion.models.Part;
import server.tech_companion.models.Json.PartJson;
import server.tech_companion.repositories.PartsRepository;

import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartsService {
    @Autowired PartsRepository partsRepo;
    
    public List<PartJson> getParts() {
    	List<Part> partsInDb = partsRepo.findAll();
    	List<PartJson> partsPackage = new ArrayList<PartJson>();
    	
    	for(Part part : partsInDb) {
    		PartJson json = new PartJson();
    		CommonMethods.copyNonNullProperties(part, json);
    		partsPackage.add(json);
    	}
    	return partsPackage;
    }
    
    public Part createPart(Part json) {
    	Part newPart = new Part();
    	newPart.set_id(ObjectId.get());
    	CommonMethods.copyNonNullProperties(json, newPart);
    	partsRepo.save(newPart);
    	return newPart;
    }
    
    public PartJson updatePart(PartJson json) {
    	Part partToBeUpdated = partsRepo.findBy_id(new ObjectId(json.getString_id()));
    	CommonMethods.copyNonNullProperties(json, partToBeUpdated);
    	partsRepo.save(partToBeUpdated);
    	
    	// Rewrite json to reflect changes
    	CommonMethods.copyNonNullProperties(partToBeUpdated, json);
    	return json;
    }
    
    public void deletePart(ObjectId _id) {
    	partsRepo.delete(partsRepo.findBy_id(_id));
    }
    
}