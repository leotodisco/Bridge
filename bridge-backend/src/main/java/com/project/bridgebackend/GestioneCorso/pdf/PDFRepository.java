package com.project.bridgebackend.GestioneCorso.pdf;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PDFRepository extends MongoRepository<PDFDoc, String> {
}
