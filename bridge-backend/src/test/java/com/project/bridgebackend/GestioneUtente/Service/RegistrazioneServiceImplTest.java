package com.project.bridgebackend.GestioneUtente.Service;

import com.project.bridgebackend.Service.BridgeBackendApplicationTests;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dao.RifugiatoDAO;
import com.project.bridgebackend.registrazione.service.RegistrazioneServiceImpl;
import com.project.bridgebackend.util.AuthenticationRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BridgeBackendApplicationTests.class)
public class RegistrazioneServiceImplTest {
    @InjectMocks
    private RegistrazioneServiceImpl registrazioneService;
    @Mock
    private RifugiatoDAO rifugiatoDAO;
    @Mock
    private AuthenticationManager authenticationManager;

    private Rifugiato rifugiato;

    private String confermaPassword;

    @BeforeEach
    @SneakyThrows
    public void setUp() throws Exception{
        rifugiato = new Rifugiato();
        rifugiato.setEmail("mariozurolo88@gmail.com");
        rifugiato.setNome("Mario");
        rifugiato.setCognome("Zurolo");
        rifugiato.setPassword("Prova123!");
        rifugiato.setNazionalita("Italiana");
        rifugiato.setGender(Gender.Maschio);
        rifugiato.setRole(Ruolo.Rifugiato);
        rifugiato.setTitoloDiStudio(TitoloDiStudio.Laurea);
        rifugiato.setDataNascita(LocalDate.of(2000, 11, 18));
        rifugiato.setFotoProfilo(null);
        rifugiato.setLingueParlate("ITALIANO");
        rifugiato.setSkill("Issue");

        confermaPassword = "Prova123!";

        Mockito.when(rifugiatoDAO.findByEmail(Mockito.anyString())).thenReturn(rifugiato);
        Authentication dummyAuthentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(dummyAuthentication);
    }


    @Test
    public void TestNomeRifugiatoFormatoErrato() throws Exception{
        rifugiato.setNome("M");

        assertThrows(IllegalArgumentException.class, () -> {this.registrazioneService.registraRifugiato(rifugiato,confermaPassword);});
    }

    @Test
    public void TestCognomeRifugiatoFormatoErrato() throws Exception{
        rifugiato.setCognome("Z");

        assertThrows(IllegalArgumentException.class, () -> {this.registrazioneService.registraRifugiato(rifugiato,confermaPassword);});
    }


    @Test
    public void TestEmailRifugiatoFormatoErrato() throws Exception{
        rifugiato.setEmail("mariozurolo00@gmail");

        assertThrows(IllegalArgumentException.class, () -> {this.registrazioneService.registraRifugiato(rifugiato,confermaPassword);});
    }

    @Test
    public void TestEmailRifugiatoPresente() throws Exception {
        rifugiato.setEmail("mariozurolo00@gmail.com");

        assertThrows(IllegalArgumentException.class, () ->{this.registrazioneService.registraRifugiato(rifugiato,confermaPassword);});
    }


    @Test
    public void TestPasswordRifugiatoFormatoErrato() throws Exception {
        rifugiato.setPassword("Pr");

        assertThrows(IllegalArgumentException.class, () ->{this.registrazioneService.registraRifugiato(rifugiato,confermaPassword);});
    }

    @Test
    public void TestDataNascitaRifugiatoFormatoErrato() throws Exception {
        rifugiato.setDataNascita(LocalDate.of(3000, 11, 18));

        assertThrows(IllegalArgumentException.class, () ->{this.registrazioneService.registraRifugiato(rifugiato,confermaPassword);});
    }

    @Test
    public void TestGenderRifugiatoFormatoErrato() throws Exception {
        rifugiato.setGender(Gender.Maschio);

        assertThrows(IllegalArgumentException.class, () ->{this.rifugiato.setGender(Gender.valueOf("***"));});
    }

    @Test
    public void TestTitoloDiStudioRifugiatoFormatoErrato() throws Exception {
        rifugiato.setTitoloDiStudio(TitoloDiStudio.Laurea);

        assertThrows(IllegalArgumentException.class, () ->{this.rifugiato.setTitoloDiStudio(TitoloDiStudio.valueOf("***"));});
    }

    @Test
    public void TestRuoloRifugiatoFormatoErrato() throws Exception {
        rifugiato.setRole(Ruolo.Rifugiato);

        assertThrows(IllegalArgumentException.class, () ->{this.rifugiato.setRole(Ruolo.valueOf("***"));});
    }

    @Test
    public void TestLinguaParlataRifugiatoFormatoErrato() throws Exception{
        rifugiato.setLingueParlate("M");

        assertThrows(IllegalArgumentException.class, () -> {this.registrazioneService.registraRifugiato(rifugiato,confermaPassword);});
    }

    @Test
    public void TestNazionalitaRifugiatoFormatoErrato() throws Exception{
        rifugiato.setNazionalita("M");

        assertThrows(IllegalArgumentException.class, () -> {this.registrazioneService.registraRifugiato(rifugiato,confermaPassword);});
    }

    @Test
    public void TestSkillRifugiatoFormatoErrato() throws Exception{
        rifugiato.setSkill("M");

        assertThrows(IllegalArgumentException.class, () -> {this.registrazioneService.registraRifugiato(rifugiato,confermaPassword);});
    }

    @Test
    public void TestAllRifugiatoFormatoCorretto() throws Exception{
        Rifugiato rifugiato2 = new Rifugiato();
        rifugiato2.setNome("Leopoldo");
        rifugiato2.setCognome("Todisco");
        rifugiato2.setEmail("ciao@prova.com");
        rifugiato2.setPassword("Prova123!");
        rifugiato2.setDataNascita(LocalDate.of(2000, 11, 18));
        rifugiato2.setGender(Gender.Maschio);
        rifugiato2.setTitoloDiStudio(TitoloDiStudio.Laurea);
        rifugiato2.setRole(Ruolo.Rifugiato);
        rifugiato2.setLingueParlate("Italiano");
        rifugiato2.setNazionalita("Italiana");
        rifugiato2.setSkill("Gestione");

        Mockito.when(rifugiatoDAO.findByEmail(Mockito.anyString())).thenReturn(null);
        assertDoesNotThrow(() -> {this.registrazioneService.registraRifugiato(rifugiato2,confermaPassword);});
    }
}
