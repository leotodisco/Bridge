/**
 * Questo pacchetto contiene le entità principali del sistema,
 * ognuna delle quali rappresenta.
 * una risorsa o un'entità del dominio.
 * Le entità sono utilizzate per mappare le tabelle del,
 * database e definire i dati strutturati gestiti dall'applicazione.
 *
 * Le principali entità incluse in questo pacchetto sono:
 *
 * - {@link com.project.bridgebackend.Model.Entity.Admin}
 *   - Rappresenta un amministratore con dati relativi all'utente e al suo ruolo nel sistema.
 *
 * - {@link com.project.bridgebackend.Model.Entity.Alloggio}
 *   - Rappresenta un alloggio, con informazioni su metratura, posti disponibili, servizi offerti, ecc.
 *
 * - {@link com.project.bridgebackend.Model.Entity.Annuncio}
 *   - Rappresenta un annuncio che può essere associato a vari servizi, come alloggi, corsi, ecc.
 *
 * - {@link com.project.bridgebackend.Model.Entity.Consulenza}
 *   - Rappresenta una consulenza offerta da un professionista a un altro utente, con i dettagli.
 *
 * - {@link com.project.bridgebackend.Model.Entity.Corso}
 *   - Rappresenta un corso disponibile, con informazioni su categoria, descrizione, lingua, e altro.
 *
 * - {@link com.project.bridgebackend.Model.Entity.Evento}
 *   - Rappresenta un evento organizzato, con informazioni su data, luogo, partecipanti, e organizzatori.
 *
 * - {@link com.project.bridgebackend.Model.Entity.FiguraSpecializzata}
 *   - Rappresenta una figura professionale specializzata che può offrire consulenze o altre attività.
 *
 * - {@link com.project.bridgebackend.Model.Entity.Indirizzo}
 *   - Rappresenta un indirizzo associato a diverse entità, come alloggi, eventi, corsi, ecc.
 *
 * - {@link com.project.bridgebackend.Model.Entity.Lavoro}
 *   - Rappresenta un lavoro o un'offerta di lavoro, associata a un contratto e a una figura professionale.
 *
 * - {@link com.project.bridgebackend.Model.Entity.Rifugiato}
 *   - Rappresenta un rifugiato con informazioni su stato di asilo, documentazione, e disponibilità.
 *
 * - {@link com.project.bridgebackend.Model.Entity.Utente}
 *   - Rappresenta un utente generico con dati di base, come nome, email, e credenziali.
 *
 * - {@link com.project.bridgebackend.Model.Entity.Volontario}
 *   - Rappresenta un volontario che offre il proprio tempo per attività di supporto, come eventi o consulenze.
 *
 * Ogni entità è annotata con {@link jakarta.persistence.Entity} e,
 * mappata a una tabella del database,
 * e molte contengono relazioni tra di loro per definire associazioni come uno-a-molti,
 * molti-a-molti, e così via.
 * Le entità sono la base per le operazioni CRUD e sono utilizzate dai servizi,
 * per manipolare i dati nel sistema.
 */

package com.project.bridgebackend.Model.Entity;
