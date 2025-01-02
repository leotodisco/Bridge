package com.project.bridgebackend.CDN.Document;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PDFDocRepository extends MongoRepository <PDFDoc, String> {
}
