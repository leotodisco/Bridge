package com.project.bridgebackend;
import com.project.bridgebackend.GestioneAlloggio.service.GestioneAlloggioServiceImplTest;
import com.project.bridgebackend.GestioneAnnuncio.service.GestioneAnnuncioLavoroServiceImplTest;
import com.project.bridgebackend.GestioneAnnuncio.service.GestioneAnnuncioServiceImplTest;
import com.project.bridgebackend.GestioneEvento.service.GestioneEventoServiceImplTest;
import com.project.bridgebackend.GestioneUtente.Service.RegistrazioneServiceImplTest;
import com.project.bridgebackend.gestioneCorso.service.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectClasses({GestioneAnnuncioLavoroServiceImplTest.class,
        GestioneAlloggioServiceImplTest.class,
        RegistrazioneServiceImplTest.class,
        GestioneAnnuncioServiceImplTest.class,
        GestioneEventoServiceImplTest.class,
        GestioneCorsoServiceImplTest.class
})
public class TestSuite {

}