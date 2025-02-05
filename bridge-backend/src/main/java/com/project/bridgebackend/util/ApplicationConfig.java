package com.project.bridgebackend.util;

import com.project.bridgebackend.Model.dao.UtenteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

/**
 * Classe di configurazione principale dell'applicazione.
 * Contiene i bean per la gestione dell'autenticazione,
 * della codifica delle password, e delle funzionalità di posta elettronica.
 *
 * @author Benedetta Colella.
 */
@Configuration
@EnableAsync
public class ApplicationConfig {

    /**
     * Servizio per l'invio di email all'interno dell'applicazione.
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * DAO per l'accesso ai dati degli utenti.
     */
    @Autowired
    @Qualifier("utenteDAO")
    private UtenteDAO usrdao;

    /**
     * Estrae il bean relativo al service.
     * @return user detail, contiene le info del token.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usrdao.findByEmail(username);
    }

    /**
     * Configura un encoder per cifrare le password utilizzando
     * l'algoritmo BCrypt.
     *
     * @return un'istanza di {@link PasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura un provider di autenticazione basato su DAO,
     * utilizzando un {@link PasswordEncoder} e un,
     * {@link UserDetailsService}.
     *
     * @param passwordEncoder il componente per la cifratura,
     *                       delle password.
     * @param userDetailsService il servizio per caricare i,
     *                          dettagli degli utenti.
     * @return un'istanza di {@link AuthenticationProvider}.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(
            final PasswordEncoder passwordEncoder,
            final UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder);
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }


    /**
     * Configura il gestore di autenticazione,
     * per l'applicazione.
     *
     * @param authenticationConfiguration configurazione,
     *                                   dell'autenticazione.
     * @return un'istanza di {@link AuthenticationManager}.
     * @throws Exception se si verifica un errore durante la configurazione.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura un servizio per l'invio di email tramite,
     * il protocollo SMTP.
     *
     * @return un'istanza configurata di,
     * {@link JavaMailSender}.
     */
    @Bean
    public static JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("beehaveofficial@gmail.com");
        mailSender.setPassword("wqvjngkoeuuafctd");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    /**
    * parte del modulo fia
     * */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
