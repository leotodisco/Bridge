/**
 * Questo pacchetto contiene tutte le interfacce DAO,
 * per la gestione delle entità principali del sistema.
 * I DAO sono responsabili dell'interazione con il database,
 * per le rispettive entità ogni DAO estende.
 * {@link org.springframework.data.jpa.repository.JpaRepository},
 * fornendo metodi di base per le operazioni CRUD.
 * e query personalizzate per ogni entità.
 *
 * Le entità gestite in questo pacchetto sono:
 * - {@link com.project.bridgebackend.Model.Entity.Admin}
 * per la gestione degli amministratori.
 * - {@link com.project.bridgebackend.Model.Entity.Alloggio}
 * per la gestione degli alloggi.
 * - {@link com.project.bridgebackend.Model.Entity.Consulenza}
 * per la gestione delle consulenze.
 * - {@link com.project.bridgebackend.Model.Entity.Corso}
 * per la gestione dei corsi.
 * - {@link com.project.bridgebackend.Model.Entity.Evento}
 * per la gestione degli eventi.
 * - {@link com.project.bridgebackend.Model.Entity.FiguraSpecializzata}
 * per la gestione delle figure specializzate.
 * - {@link com.project.bridgebackend.Model.Entity.Indirizzo}
 * per la gestione degli indirizzi.
 * - {@link com.project.bridgebackend.Model.Entity.Lavoro}
 * per la gestione dei lavori.
 * - {@link com.project.bridgebackend.Model.Entity.Rifugiato}
 * per la gestione dei rifugiati.
 * - {@link com.project.bridgebackend.Model.Entity.Utente}
 * per la gestione degli utenti.
 * - {@link com.project.bridgebackend.Model.Entity.Volontario}
 * per la gestione dei volontari.
 *
 * Ogni DAO contiene metodi per recuperare, salvare, aggiornare,
 * ed eliminare le rispettive entità,
 * e può includere metodi personalizzati per query complesse che,
 * richiedono l'uso di JPQL o query native.
 *
 * Le entità gestite da questi DAO sono utilizzate per implementare,
 * la logica di business nelle varie funzionalità del sistema.
 */
package com.project.bridgebackend.Model.dao;
