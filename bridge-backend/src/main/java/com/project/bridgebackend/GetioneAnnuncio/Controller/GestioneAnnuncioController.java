package com.project.bridgebackend.GetioneAnnuncio.Controller;


import com.project.bridgebackend.GetioneAnnuncio.Service.GestioneAnnuncioService;
import com.project.bridgebackend.Model.Entity.Consulenza;
import com.project.bridgebackend.Model.dto.ConsulenzaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/annunci")
public class GestioneAnnuncioController {
    @Autowired
    private GestioneAnnuncioService gestioneAnnuncioService;

    @PostMapping
    public ResponseEntity<Consulenza> creaConsulenza(@Valid @RequestBody Consulenza consulenza) {

        Consulenza nuovaConsulenza = gestioneAnnuncioService.inserimentoConsulenza(consulenza);

        return new ResponseEntity<>(nuovaConsulenza, HttpStatus.CREATED);
    }
}
