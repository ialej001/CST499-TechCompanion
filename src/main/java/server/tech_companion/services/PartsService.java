package server.tech_companion.services;

import server.tech_companion.repositories.PartsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartsService {
    @Autowired PartsRepository partsRepo;
}