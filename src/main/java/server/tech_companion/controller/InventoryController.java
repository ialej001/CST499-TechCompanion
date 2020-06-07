package server.tech_companion.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.tech_companion.payload.PartJson;
import server.tech_companion.services.PartsService;

@CrossOrigin(origins = { "http://localhost:9080", "http://localhost:8080" })
@RestController
@RequestMapping("/api")
public class InventoryController {
	@Autowired
    private PartsService partsService;
	
    // get all items
    @GetMapping("/parts/all")
    @PreAuthorize("hasRole('OFFICE') or hasRole('ADMIN') or hasRole('TECH')")
    public ResponseEntity<List<PartJson>> findAllParts() {
    	List<PartJson> parts = partsService.getParts();
        if (parts.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(parts);
        }
    }
    
    @PutMapping("/parts/update/{id}")
    @PreAuthorize("hasRole('OFFICE') or hasRole('ADMIN')")
    public ResponseEntity<PartJson> updatePart(@PathVariable String id, @RequestBody PartJson part) {
    	PartJson updatedPart = partsService.updatePart(part);
        if (updatedPart == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedPart);
        }
    }
    
    @DeleteMapping("/parts/delete/{id}")
    public ResponseEntity<?> deletePart(@PathVariable String id) {
    	partsService.deletePart(new ObjectId(id));
    	return ResponseEntity.noContent().build();
    }
}
