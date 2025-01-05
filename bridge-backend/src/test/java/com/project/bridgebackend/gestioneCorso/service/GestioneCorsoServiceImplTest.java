package com.project.bridgebackend.gestioneCorso.service;

import com.project.bridgebackend.BridgeBackendApplication;
import com.project.bridgebackend.GestioneCorso.Service.GestioneCorsoServiceImpl;
import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.enumeration.*;
import com.project.bridgebackend.Model.dao.CorsoDAO;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = BridgeBackendApplication.class)
public class GestioneCorsoServiceImplTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    private FiguraSpecializzata figura1;

    @Mock
    private FiguraSpecializzataDAO figuraSpecializzataDAO;

    @Mock
    private CorsoDAO corsoDAO;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private GestioneCorsoServiceImpl gestioneCorsoServiceImpl;

    @AfterEach
    void tearDown() {
        Mockito.reset(figuraSpecializzataDAO, corsoDAO);
    }

    @SneakyThrows
    @BeforeEach
    void setup() {
         figura1 = new FiguraSpecializzata(
                "figura1@example.com",
                "Laura",
                "Pausini",
                "Italiano, Inglese",
                "6772dfdc30698b3a76d304a3",
                "Medicina",
                LocalDate.of(1980, 5, 20),
                TitoloDiStudio.Specializzazione,
                Ruolo.FiguraSpecializzata,
                Gender.Femmina,
                "Italia",
                passwordEncoder.encode("Passroad69!"),
                "09:00-17:00"
        );

    };

    @Test
    public void testInserimentoCorsoTitolo_FormatoErrato() {
        Corso corso = new Corso();
        corso.setProprietario(figura1);
        corso.setTitolo("");
        corso.setCategoriaCorso(CategoriaCorso.LINGUE);
        corso.setDescrizione("Questo è un corso introduttivo all’apprendimento della lingua italiana");
        corso.setPdf("CorsoLingue.pdf");
        corso.setProprietario(figura1);
        corso.setLingua(Lingua.ITALIANO);

        assertThrows(Exception.class, () -> {
            this.gestioneCorsoServiceImpl.creaCorso(corso);
        });
    }

    @Test
    public void testInsrimentoCorsoCategoria_FormatoNonValido() {
        Corso corso = new Corso();
        corso.setProprietario(figura1);
        corso.setTitolo("Corso di Italiano");
        corso.setDescrizione("Questo è un corso introduttivo all’apprendimento della lingua italiana");
        corso.setPdf("CorsoLingue.pdf");
        corso.setProprietario(figura1);
        corso.setLingua(Lingua.ITALIANO);

        assertThrows(IllegalArgumentException.class, () -> {
            corso.setCategoriaCorso(CategoriaCorso.valueOf(" "));
        });
    }

    @Test
    public void testInserimentoCorsoDescrizione_Vuota(){
        Corso corso = new Corso();
        corso.setProprietario(figura1);
        corso.setTitolo("Corso di Italiano");
        corso.setCategoriaCorso(CategoriaCorso.LINGUE);
        corso.setPdf("CorsoLingue.pdf");
        corso.setProprietario(figura1);
        corso.setLingua(Lingua.ITALIANO);

        corso.setDescrizione(" ");
        assertThrows(Exception.class, () -> {
            this.gestioneCorsoServiceImpl.creaCorso(corso);
        });
    }

    @Test
    public void testInserimentoCorsoLingua_FormatoNonValido(){
        Corso corso = new Corso();
        corso.setProprietario(figura1);
        corso.setTitolo("Corso di Italiano");
        corso.setCategoriaCorso(CategoriaCorso.LINGUE);
        corso.setDescrizione("Questo è un corso introduttivo all’apprendimento della lingua italiana");
        corso.setPdf("CorsoLingue.pdf");
        corso.setProprietario(figura1);

        assertThrows(IllegalArgumentException.class, () -> {
            corso.setLingua(Lingua.valueOf(" "));
        });
    }

    @Test
    public void testInserimentoCorsoProprietario_nonPresenteNelDB() {
        Corso corso = new Corso();
        corso.setTitolo("Corso di Italiano");
        corso.setCategoriaCorso(CategoriaCorso.LINGUE);
        corso.setDescrizione("Questo è un corso introduttivo all’apprendimento della lingua italiana");
        corso.setPdf("CorsoLingue.pdf");
        corso.setLingua(Lingua.ITALIANO);

        FiguraSpecializzata proprietarioNonPresente = new FiguraSpecializzata(
                "nonpresente@example.com",
                "Mario",
                "Rossi",
                "Italiano",
                "password123",
                "Informatica",
                LocalDate.of(1980, 1, 1),
                TitoloDiStudio.Specializzazione,
                Ruolo.FiguraSpecializzata,
                Gender.Maschio,
                "Italia",
                "hashedPassword",
                "09:00-17:00"
        );

        corso.setProprietario(proprietarioNonPresente);

        Mockito.when(figuraSpecializzataDAO.findByEmail(corso.getProprietario().getEmail())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
                this.gestioneCorsoServiceImpl.creaCorso(corso);
        });
    }

    @Test
    public void testInserimentoCorsoTitolo_FormatoCorretto() throws Exception {
        // Creazione dell'oggetto Corso
        Corso corso = new Corso();
        corso.setId(1000L);
        corso.setProprietario(figura1);
        corso.setTitolo("Corso di Italiano");
        corso.setCategoriaCorso(CategoriaCorso.LINGUE);
        corso.setDescrizione("Questo è un corso introduttivo all’apprendimento della lingua italiana");
        corso.setPdf("6772e95230698b3a76d304af");
        corso.setLingua(Lingua.ITALIANO);

        // Mock del comportamento di corsoDAO
        Mockito.when(corsoDAO.save(Mockito.any(Corso.class)))
                .thenAnswer(invocazione -> invocazione.getArgument(0));

        // Mock del comportamento di figuraSpecializzataDAO
        Mockito.when(figuraSpecializzataDAO.findByEmail(corso.getProprietario().getEmail()))
                .thenReturn(corso.getProprietario());

        assertDoesNotThrow( () -> this.gestioneCorsoServiceImpl.creaCorso(corso));
    }
}