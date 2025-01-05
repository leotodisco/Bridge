package com.project.bridgebackend.GestioneUtente.Service;

import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.dao.AdminDAO;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import com.project.bridgebackend.Model.dao.RifugiatoDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import com.project.bridgebackend.Service.BridgeBackendApplicationTests;
import com.project.bridgebackend.registrazione.service.RegistrazioneServiceImpl;
import com.project.bridgebackend.util.AuthenticationRequest;
import com.project.bridgebackend.util.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BridgeBackendApplicationTests.class)
public class LoginServiceImplTest {

    @Mock
    private VolontarioDAO volontarioDAO;

    @Mock
    private RifugiatoDAO rifugiatoDAO;

    @Mock
    private FiguraSpecializzataDAO figuraSpecializzataDAO;

    @Mock
    private JwtService jwtService; //serve per generare token


    @InjectMocks
    private RegistrazioneServiceImpl registrazioneService;

    //encoder per la crittografia delle password
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private AdminDAO adminDAO;

    @Mock
    private AuthenticationRequest request;

    @Mock
    private AuthenticationManager authenticationManager;


    private Volontario volontario;

    /**
     * Metodo di configurazione eseguito prima di ogni test.
     */
    @BeforeEach
    public void setUp() {
        //creazione di un oggetto volontario simulato
        volontario = new Volontario(
                "g.montella@email.com",
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
                passwordEncoder.encode("123Password!"));

        //comportamento del volontario dao
        Mockito.when(volontarioDAO.findByEmail(Mockito.anyString())).thenReturn(volontario);

        //configura un mock di AuthenticationManager per simulare l'autenticazione
        Authentication dummyAuthentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(dummyAuthentication);

        // Configura il mock di JwtService
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("dummy-jwt-token");
    }

    /**
     * Test per verificare il comportamento con un'email errata.
     */
    @Test
    public void TestLoginEmailSbagliata() {
        // Configura i mock per restituire null per tutti i tipi di utenti
        request = new AuthenticationRequest(
                "123Password!", //password corretta
                "g.montella@email.falsa" // email errata
        );
        // Configura i mock per restituire null per tutti i tipi di utenti
        Mockito.when(this.adminDAO.findByEmail(Mockito.anyString())).thenReturn(null);
        Mockito.when(this.rifugiatoDAO.findByEmail(any())).thenReturn(null);
        Mockito.when(this.figuraSpecializzataDAO.findByEmail(any())).thenReturn(null);
        Mockito.when(this.volontarioDAO.findByEmail(any())).thenReturn(null);

        // Verifica che venga sollevata un'eccezione
        assertThrows(Exception.class, () -> {this.registrazioneService.login(request);});
    }

    /**
     * Test per verificare il comportamento con una password errata.
     */
    @Test
    public void TestLoginPasswordSbagliata() throws Exception {
        // Richiesta di autenticazione con una password errata
        request = new AuthenticationRequest(
                "NonSonoLaPasswordCorretta45",// Password errata
                "g.montella@email.com"                 // Email corretta
        );
        // Configura i mock per restituire null per altri tipi di utenti
        Mockito.when(this.adminDAO.findByEmail(Mockito.anyString())).thenReturn(null);
        Mockito.when(this.rifugiatoDAO.findByEmail(any())).thenReturn(null);
        Mockito.when(this.figuraSpecializzataDAO.findByEmail(any())).thenReturn(null);
        Mockito.when(this.volontarioDAO.findByEmail(any())).thenReturn(null);

        // Verifica che venga sollevata un'eccezione
        assertThrows(Exception.class, () -> {this.registrazioneService.login(request);});
    }
    /**
     * Test per verificare il comportamento corretto del login.
     */
    @Test
    public void TestLoginCorretto() throws Exception {
        // Richiesta di autenticazione con credenziali corrette
        request = new AuthenticationRequest(
                "123Password!",    //Password corretta
                "g.montella@email.com"      //Email corretta
        );
        // Simula che il VolontarioDAO trovi un utente corrispondente
        Mockito.when(this.volontarioDAO.findByEmail(any())).thenReturn(volontario);
        Mockito.when(this.adminDAO.findByEmail(Mockito.anyString())).thenReturn(null);
        Mockito.when(this.rifugiatoDAO.findByEmail(any())).thenReturn(null);
        Mockito.when(this.figuraSpecializzataDAO.findByEmail(any())).thenReturn(null);


        // Verifica che non venga sollevata alcuna eccezione
        assertDoesNotThrow(() -> {this.registrazioneService.login(request);});
    }

}
