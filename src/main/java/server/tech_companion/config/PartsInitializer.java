package server.tech_companion.config;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;

import server.tech_companion.models.Part;
import server.tech_companion.repositories.PartsRepository;

@Component
public class PartsInitializer {
	@Autowired
	private PartsRepository partsRepo;
	private String file;
	private Reader reader;
	private CSVReader csvReader;
	
	public PartsInitializer(
			PartsRepository partsRepo
			) {
		this.partsRepo = partsRepo;
	};
	
	@PostConstruct
	public void seedPartsDb() throws IOException {
		try {
			file = "src/main/resources/parts.CSV";
			reader = Files.newBufferedReader(Paths.get(file));
			csvReader = new CSVReader(reader);
			
			String[] nextRecord;
			Part part = new Part();
			if (partsRepo.findAll().isEmpty()) {
				while ((nextRecord = csvReader.readNext()) != null) {
					if (nextRecord[1].contains("Part")) {
						System.out.println(nextRecord[0] + " Part [part#: " + nextRecord[2] + ", description: " + nextRecord[3] + ", price: " + nextRecord[6]);
						part.set_id(ObjectId.get());
						part.setDescription(nextRecord[3]);
						part.setPartNumber(nextRecord[2]);
						part.setPrice(Double.parseDouble(nextRecord[6]));
						
						partsRepo.save(part);
						
						System.out.println("saved: " + part.toString());
					}	
				}
			}
		} finally {
			System.out.println("Done seeding.");
		}
	}
}

//
