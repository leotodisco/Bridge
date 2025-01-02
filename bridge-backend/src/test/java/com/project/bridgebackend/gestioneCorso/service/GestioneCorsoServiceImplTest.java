package com.project.bridgebackend.gestioneCorso.service;

import com.project.bridgebackend.GestioneCorso.Service.GestioneCorsoServiceImpl;
import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.enumeration.CategoriaCorso;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dao.CorsoDAO;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GestioneCorsoServiceImplTest {

    @InjectMocks
    private GestioneCorsoServiceImpl gestioneCorsoServiceImpl;

    @BeforeEach
    void setup() {
    }

    @Test
    public void testInserimentoCorsoTitolo_FormatoErrato() {
        Corso corso = new Corso();
        corso.setTitolo("@@@");
        corso.setCategoriaCorso(CategoriaCorso.LINGUE);
        corso.setDescrizione("Descrizione di prova");
        corso.setPdf("CorsoLingue.pdf");

        assertThrows(Exception.class, () -> {
            this.gestioneCorsoServiceImpl.creaCorso(corso);
        });
    }

    @Test
    public void testInserimentoCorsoTitolo_FormatoInferiore() {
        Corso corso = new Corso();
        corso.setTitolo("a");
        corso.setCategoriaCorso(CategoriaCorso.LINGUE);
        corso.setDescrizione("Descrizione di prova");
        corso.setPdf("CorsoLingue.pdf");

        assertThrows(Exception.class, () -> {
            this.gestioneCorsoServiceImpl.creaCorso(corso);
        });
    }

    @Test
    public void testInserimentoCorsoTitolo_TitoloVuoto() {
        Corso corso = new Corso();
        corso.setTitolo("");  // Titolo vuoto
        corso.setCategoriaCorso(CategoriaCorso.INFORMATICA);
        corso.setDescrizione("Altro test");
        corso.setPdf("CorsoInformatica.pdf");

        // Verifica che venga lanciata l'eccezione
        assertThrows(Exception.class, () -> {
            this.gestioneCorsoServiceImpl.creaCorso(corso);
        });
    }

    @Test
    public void testInsrimentoCorsoCategoria_FormatoNonValido() {
        Corso corso = new Corso();
        corso.setTitolo("Corso di Italiano");
        corso.setDescrizione("Descrizione di prova");
        corso.setPdf("CorsoLingue.pdf");
        assertThrows(IllegalArgumentException.class, () -> {
            // Tentativo di usare un valore enum inesistente
            corso.setCategoriaCorso(CategoriaCorso.valueOf("Categoria123"));
        });
    }

    @Test
    public void testInserimentoCorsoCategoria_CategoriaVuoto(){
        Corso corso = new Corso();
        corso.setTitolo("Corso di Italiano");
        corso.setDescrizione("Descrizione di prova");
        corso.setPdf("CorsoLingue.pdf");
        assertThrows(IllegalArgumentException.class, () -> {
            // Tentativo di usare un valore enum inesistente
            corso.setCategoriaCorso(CategoriaCorso.valueOf(" "));
        });
    }

    @Test
    public void testInserimentoCorsoDescrizione_FormatoErrato(){
        Corso corso = new Corso();
        corso.setTitolo("Corso di Italiano");
        corso.setCategoriaCorso(CategoriaCorso.LINGUE);
        corso.setPdf("CorsoLingue.pdf");

        corso.setDescrizione("@@@");
        assertThrows(Exception.class, () -> {
            this.gestioneCorsoServiceImpl.creaCorso(corso);
        });
    }

    @Test
    public void testInserimentoCorsoDescrizione_FormatoInferiore(){
        Corso corso = new Corso();
        corso.setTitolo("Corso di Italiano");
        corso.setCategoriaCorso(CategoriaCorso.LINGUE);
        corso.setPdf("CorsoLingue.pdf");

        corso.setDescrizione("a");
        assertThrows(Exception.class, () -> {
            this.gestioneCorsoServiceImpl.creaCorso(corso);
        });
    }

    @Test
    public void testInserimentoCorsoDescrizione_Vuota(){
        Corso corso = new Corso();
        corso.setTitolo("Corso di Italiano");
        corso.setCategoriaCorso(CategoriaCorso.LINGUE);
        corso.setPdf("CorsoLingue.pdf");

        corso.setDescrizione("");
        assertThrows(Exception.class, () -> {
            this.gestioneCorsoServiceImpl.creaCorso(corso);
        });
    }

    @Test
    public void testInserimentoCorsoPdf_EstensioneErrata(){
        Corso corso = new Corso();
        corso.setTitolo("Corso di Italiano");
        corso.setCategoriaCorso(CategoriaCorso.LINGUE);
        corso.setDescrizione("Descrizione di prova");

        corso.setPdf("CorsoLingue.docx");
        assertThrows(Exception.class, () -> {
            this.gestioneCorsoServiceImpl.creaCorso(corso);
        });
    }
}
