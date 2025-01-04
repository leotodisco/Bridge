package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.CDN.Document.FotoAlloggio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FotoAlloggioRepository extends MongoRepository<FotoAlloggio,String> {
}
