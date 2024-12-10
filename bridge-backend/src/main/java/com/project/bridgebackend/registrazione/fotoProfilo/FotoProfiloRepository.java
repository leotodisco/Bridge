package com.project.bridgebackend.registrazione.fotoProfilo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface FotoProfiloRepository extends MongoRepository<FotoProfilo, String> {
}
