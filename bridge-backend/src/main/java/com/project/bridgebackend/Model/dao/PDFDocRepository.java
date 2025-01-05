package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.CDN.Document.PDFDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PDFDocRepository extends MongoRepository <PDFDoc, String> {
}
