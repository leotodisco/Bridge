package com.project.bridgebackend.GestioneAlloggio.service;

import com.project.bridgebackend.GestioneAlloggio.Service.AlloggioServiceImplementazione;
import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.Servizi;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dao.AlloggioDAO;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import com.project.bridgebackend.Service.BridgeBackendApplicationTests;
import lombok.SneakyThrows;
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

import static com.project.bridgebackend.Model.Entity.enumeration.Servizi.WIFI;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = BridgeBackendApplicationTests.class)
public class GestioneAlloggioServiceImplTest {
    @Mock
    private AlloggioDAO alloggioDAO;
    @Mock
    private VolontarioDAO volontarioDAO;
    @Mock
    private IndirizzoDAO indirizzoDAO;
    @Mock
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Mock
    private Volontario volontario;
    @InjectMocks
    private Alloggio alloggio;
    @InjectMocks
    private AlloggioServiceImplementazione alloggioServiceImplementazione;


    @SneakyThrows
    @BeforeEach
    public void setup() {
        /*volontario = new Volontario(
                        "volontario3@example.com",
                        "Edoardo",
                        "D'Erme",
                        "Italiano, Spagnolo",
                        "6772e37230698b3a76d304a7",
                        "Supporto tecnico",
                        LocalDate.of(1990, 12, 10),
                        TitoloDiStudio.ScuolaSecondariaPrimoGrado,
                        Ruolo.Volontario,
                        Gender.Maschio,
                        "Italia",
                        passwordEncoder.encode("Passroad69!"));

        Mockito.when(volontarioDAO.findByEmail(Mockito.anyString())).thenReturn(volontario);*/
    }

    @Test
    public void testInserimentoAlloggioViaIndirizzo_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
              "",
              12,
              84084,
              "",
              "c"
        );
        indirizzo.setId(1L);


        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(1);
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setDescrizione("");
        alloggio.setFoto(null);
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setMaxPersone(1);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    @Test
    public void testInserimentoAlloggioNCivicoIndirizzo_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "",
                 27000,
                84084,
                "",
                "Via Garibaldi"
        );
        indirizzo.setId(1L);

        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(1);
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setDescrizione("");
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setMaxPersone(1);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    @Test
    public void testInserimentoAlloggioCittaIndirizzo_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "MontecorvinoRovella1000",
                101,
                84084,
                "",
                "Via Garibaldi"
        );
        indirizzo.setId(1L);

        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(1);
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setDescrizione("");
        alloggio.setFoto(null);
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setMaxPersone(1);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    @Test
    public void testInserimentoAlloggioProvinciaIndirizzo_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                84084,
                "CAP-",
                "Via Garibaldi"
        );
        indirizzo.setId(1L);

        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(1);
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setDescrizione("");
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setMaxPersone(1);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    @Test
    public void testInserimentoAlloggioCAPIndirizzo_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                840960,
                "SA",
                "Via Garibaldi"
        );

        indirizzo.setId(1L);
        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(1);
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setDescrizione("");
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setMaxPersone(1);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    @Test
    public void testInserimentoAlloggioMetratura_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                84096,
                "SA",
                "Via Garibaldi"
        );

        indirizzo.setId(1L);
        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(10000000);
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setDescrizione("");
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setMaxPersone(1);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    @Test
    public void testInserimentoAlloggioMaxPersone_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                84096,
                "SA",
                "Via Garibaldi"
        );

        indirizzo.setId(1L);
        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(100);
        alloggio.setMaxPersone(200);
        alloggio.setDescrizione("");
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    @Test
    public void testInserimentoAlloggioDescrizione_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                84096,
                "SA",
                "Via Garibaldi"
        );

        indirizzo.setId(1L);
        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(100);
        alloggio.setMaxPersone(3);
        alloggio.setDescrizione("@@@");
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    @Test
    public void testInserimentoAlloggioServizi_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                84096,
                "SA",
                "Via Garibaldi"
        );

        indirizzo.setId(1L);
        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(100);
        alloggio.setMaxPersone(3);
        alloggio.setDescrizione("Casa nel centro Storico ");
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setAssegnatoA(null);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

         assertThrows(IllegalArgumentException.class, () -> {
            alloggio.setServizi(Servizi.valueOf("#####"));
        });
    }

    @Test
    public void testInserimentoAlloggioFoto_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                84096,
                "SA",
                "Via Garibaldi"
        );

        indirizzo.setId(1L);
        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(100);
        alloggio.setMaxPersone(3);
        alloggio.setDescrizione("Casa nel centro Storico ");
        alloggio.setServizi(WIFI);
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setAssegnatoA(null);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

    }

}
