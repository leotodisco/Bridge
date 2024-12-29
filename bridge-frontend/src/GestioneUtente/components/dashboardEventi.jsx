import { useState, useEffect } from 'react';
import Card from "../../GestioneEvento/components/Card.jsx";
import EventView from "../../GestioneEvento/components/EventoRetrieveView.jsx";

const EventiUtente = () => {
    const [eventi, setEventi] = useState([]);
    const [error, setError] = useState(null);
    const [selectedEventId, setSelectedEventId] = useState(null); // Stato per l'evento selezionato (per dettagli)


    // Recupera l'email dell'utente loggato (esempio: salvata in localStorage)
    const email = localStorage.getItem('email'); // Assicurati che l'email sia salvata.

    useEffect(() => {
        const token = localStorage.getItem('authToken'); // Sostituisci con il tuo token

        if (!token) {
            alert("Token non trovato. Effettua nuovamente il login.");
            return;
        }

        // Recupera gli eventi dell'utente
        fetch(`http://localhost:8080/api/eventi/pubblicati?email=${email}`, {
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
            .then((data) => setEventi(data))
            .catch((error) => {
                setError('Errore durante il recupero degli eventi');
                console.error(error);
            });
    }, [email]);

    // Funzione per gestire il click sul pulsante di maggiori informazioni
    const handleInfoClick = (eventoId) => {
        setSelectedEventId(eventoId); // Imposta l'ID dell'evento selezionato
    };

    // Funzione per chiudere il popup
    const closePopup = () => {
        setSelectedEventId(null); // Reset dell'evento selezionato, chiudendo così il popup
    };

    return (
        <div >
            <div className="dashboardContainer">
            <h1>I miei Eventi</h1>
            <hr/>
            {error && <p>{error}</p>}
            {eventi.length > 0 ? (
                <div className="cards-container">
                    {/* Mappa gli eventi e crea una Card per ciascuno */}
                    {eventi.map((evento) => (
                        <Card
                            key={evento.id} // Usa un identificatore unico per la chiave
                            data={{
                                title: evento.nome,
                                image: evento.organizzatore?.immagineProfilo, // Usa l'immagine dell'organizzatore, se disponibile
                                userName: `${evento.organizzatore?.nome} ${evento.organizzatore?.cognome}`,
                                parameter1: evento.data, // Ad esempio, la data dell'evento
                                parameter2: evento.ora,  // L'ora dell'evento
                                parameter3: evento.linguaParlata, // Lingua parlata durante l'evento
                            }}
                            labels={{
                                parameter1: "Data",
                                parameter2: "Ora",
                                parameter3: "Lingua",
                            }}
                            onClick={() => console.log(`Evento cliccato: ${evento.nome}`)} // Gestisci il click su tutta la card
                            onInfoClick={() => handleInfoClick(evento.id)} // Gestisci il click sul pulsante di info
                        />
                    ))}
                </div>
            ) : (
                <p>Non hai eventi pubblicati.</p>
            )}
            </div>
            {/* Mostra il popup se selectedEventId è impostato */}
            {selectedEventId && (
                <EventView id={selectedEventId} onClose={closePopup}/>
            )}
        </div>

    );
};

export default EventiUtente;
