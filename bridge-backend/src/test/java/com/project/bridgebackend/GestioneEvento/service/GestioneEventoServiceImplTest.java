package com.project.bridgebackend.GestioneEvento.service;


import com.project.bridgebackend.Model.Entity.Consulenza;
import com.project.bridgebackend.Model.Entity.Evento;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import com.project.bridgebackend.Model.dao.EventoDAO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GestioneEventoServiceImplTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Mock
    private EventoDAO eventoDAO;

    @InjectMocks
    private GestioneEventoServiceImpl eventoService;

    private Evento evento;
    private Indirizzo indirizzo;

    @AfterEach
    void tearDown() {
        // Resetta tutti i mock di Mockito per evitare interferenze tra i test
        Mockito.reset(eventoDAO);
    }

    @BeforeEach
    public void setup() {

        this.indirizzo = new Indirizzo(
                "Salerno",
                12,
                "84100",
                "SA",
                "Via Roma"
        );
        indirizzo.setId(1L);

        this.evento = new Evento();
        evento.setNome("Evento Test");
        evento.setDescrizione("Incontro dedicato agli appassionati di tecnologia, con interventi di esperti del settore.");
        evento.setLinguaParlata(Lingua.ITALIANO);
        evento.setData(LocalDate.of(2025, 1, 10));
        evento.setOra(LocalTime.of(14, 0));
        evento.setLuogo(indirizzo);
        evento.setMaxPartecipanti(50);
        evento.setOrganizzatore(null);
        evento.setId(1L);
    }


    @Test
    public void testCreazioneEventoNomeFormatoErrato() {
        evento.setNome("Ev");

        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));

    }

    @Test
    public void testCreazioneEventoNomeVuoto() {
        evento.setNome("");

        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));
    }

    @Test
    public void testCreazioneEventoDataFormatoErrato() {

        assertThrows(Exception.class, () -> {evento.setData(LocalDate.of(2025, 13, 45));});
    }

    @Test
    public void testCreazioneEventoDataVuota() {

        evento.setData(null);

        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));

    }

    @Test
    public void testCreazioneEventoOraFormatoErrato() {

        assertThrows(DateTimeException.class, () -> {evento.setOra(LocalTime.of(25,61));});
    }

    @Test
    public void testCreazioneEventoOraVuota() {
        evento.setOra(null);

        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));

    }

    @Test
    public void testCreazioneEventoViaFormatoErrato() {

        indirizzo.setVia("V*");
        evento.setLuogo(indirizzo);

        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));
    }

    @Test // rivedi
    public void testCreazioneEventoNumeroCivicoFormatoErrato() {

        indirizzo.setNumCivico(-1);
        evento.setLuogo(indirizzo);
        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));
    }

    @Test
    public void testCreazioneEventoCittaErrata() {

        indirizzo.setCitta("Salern@");
        evento.setLuogo(indirizzo);
        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));
    }

    @Test
    public void testCreazioneEventoProvinciaErrata() {

        indirizzo.setProvincia("SAA");
        evento.setLuogo(indirizzo);
        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));
    }

    @Test
    public void testCreazioneEventoCAPErrato() {

        indirizzo.setCap("8410");
        evento.setLuogo(indirizzo);
        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));
    }

    @Test
    public void testCreazioneEventoLinguaParlataVuota() {

        evento.setLinguaParlata(null);
        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));
    }

    @Test
    public void testCreazioneEventoDescrizioneFormatoErrato() {
        evento.setDescrizione("@Descrizione");
        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));
    }

    @Test
    public void testCreazioneEventoDescrizioneLunghezzaErrata() {
        evento.setDescrizione("a");
        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));
    }

    @Test
    public void testCreazioneEventoDescrizioneVuota() {
        evento.setDescrizione("");
        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));
    }

    @Test
    public void testCreazioneEventoMaxPartecipantiErrato() {
        evento.setMaxPartecipanti(-1);
        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));
    }

    @Test
    public void testCreazioneEventoMaxPartecipantiErrato2() {
        evento.setMaxPartecipanti(1000);
        // Convalida manuale del DTO
        Set<ConstraintViolation<Evento>> violations = validator.validate(evento);

        // Verifica che ci siano violazioni
        assertFalse(violations.isEmpty(), "La validazione dovrebbe fallire per un entity non valida");

        // Stampa le violazioni (facoltativo)
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));
    }

    @Test
    public void testCreazioneEventoCorretto() {

        // Configura il comportamento predefinito del mock per il salvataggio di una consulenza
        Mockito.when(eventoDAO.save(Mockito.any(Evento.class))).thenAnswer(
                invocazione -> {
                    return invocazione.getArgument(0);
                }
        );

        assertEquals(evento, this.eventoService.insertEvento(evento));
    }

}
