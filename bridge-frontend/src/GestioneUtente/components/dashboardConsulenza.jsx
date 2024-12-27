import { useState, useEffect } from 'react';
import Card from "../../GestioneEvento/components/Card.jsx";
import ConsulenzaView from "../../GestioneAnnuncio/components/ConsulenzaRetrive.jsx";
import '../css/PopupCandidati.css';

const ConsulenzaUtente = () => {
    const [consulenza, setConsulenza] = useState([]);
    const [error, setError] = useState(null);
    const [selectedConsulenzaId, setSelectedConsulenzaId] = useState(null); // Stato per l'evento selezionato (per dettagli)
    const [selectedCandidates, setSelectedCandidates] = useState(null); // Stato per i candidati


    // Recupera l'email dell'utente loggato (esempio: salvata in localStorage)
    const email = localStorage.getItem('email'); // Assicurati che l'email sia salvata.

    useEffect(() => {
        const token = localStorage.getItem('authToken');

        if (!token) {
            alert("Token non trovato. Effettua nuovamente il login.");
            return;
        }

        // Recupera gli eventi dell'utente
        fetch(`http://localhost:8080/api/annunci/pubblicati?email=${email}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Errore nella risposta del server');
                }
                return response.json();
            })
            .then((data) => setConsulenza(data))
            .catch((error) => {
                setError('Errore durante il recupero delle consulenze');
                console.error(error);
            });
    }, [email]);

    // Funzione per gestire il click sul pulsante di maggiori informazioni
    const handleInfoClick = (consulenzaId) => {
        setSelectedConsulenzaId(consulenzaId); // Imposta l'ID dell'evento selezionato
    };

    // Funzione per chiudere il popup
    const closePopup = () => {
        setSelectedConsulenzaId(null); // Reset dell'evento selezionato, chiudendo così il popup
    };

    // Funzione per gestire la visualizzazione dei candidati
    const handleShowCandidates = (candidati) => {
        if (candidati.length > 0) {
            setSelectedCandidates(candidati);
        } else {
            setError('Nessun candidato disponibile per questa consulenza.');
        }
    };

    // Funzione per chiudere il popup dei candidati
    const closeCandidatesPopup = () => {
        setSelectedCandidates(null); // Reset dei candidati selezionati
    };

    return (
        <div>
            <h1>Le mie consulenze</h1>
            {error && <p>{error}</p>}
            {consulenza.length > 0 ? (
                <div className="cards-container">
                    {consulenza.map((event) => {
                        return (
                            <Card
                                key={event.id}
                                data={{
                                    title: event.titolo,
                                    image: event.proprietario?.immagineProfilo,
                                    userName: `${event.proprietario.nome} ${event.proprietario.cognome}`,
                                    parameter1: event.tipo,
                                    parameter2: event.orariDisponibili,
                                    parameter3: event.proprietario.email,
                                }}
                                labels={{
                                    parameter1: "Tipo",
                                    parameter2: "Orari Disponibili",
                                    parameter3: "Email",
                                }}
                                onClick={() => console.log(`Cliccato su consulenza: ${event.titolo}`)}
                                onInfoClick={() => handleInfoClick(event.id)}
                            >
                                {/* Nuovo pulsante */}
                                <button onClick={() => handleShowCandidates(event.candidati)}>
                                    Mostra Candidati
                                </button>
                            </Card>

                        );
                    })}
                </div>
            ) : (
                <p>Non hai ancora Consulenza pubblicati.</p>
            )}
            {/* Mostra il popup se selectedEventId è impostato */}
            {selectedConsulenzaId && (
                <ConsulenzaView id={selectedConsulenzaId} onClose={closePopup}/>
            )}
            {/* Mostra i candidati in un popup */}
            {selectedCandidates && (
                <div className="popup">
                    <h2>Candidati</h2>
                    <ul>
                        {selectedCandidates.map((email, index) => (
                            <li key={index}>
                                {email}
                            </li>
                        ))}
                    </ul>
                    <button onClick={closeCandidatesPopup}>Chiudi</button>
                </div>
            )}

        </div>
    );
};

export default ConsulenzaUtente;
