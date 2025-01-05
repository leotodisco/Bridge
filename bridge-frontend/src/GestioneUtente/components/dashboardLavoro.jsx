import { useState, useEffect } from 'react';
import Card from "../../GestioneEvento/components/Card.jsx";
import LavoroView from "../../GestioneAnnuncio/components/LavoroRetrieve.jsx";

const LavoriUtente = () => {
    const [lavori, setLavori] = useState([]);
    const [error, setError] = useState(null);
    const [selectedLavoroId, setSelectedLavoroId] = useState(null);
    // Recupera l'email dell'utente loggato (esempio: salvata in localStorage)
    const email = localStorage.getItem('email'); // Assicurati che l'email sia salvata.
    const token = localStorage.getItem('authToken'); // Assicurati che il token sia salvato.

    useEffect(() => {

        // Recupera gli eventi dell'utente
        fetch(`http://localhost:8080/api/annunci/view_lavori/proprietario/${email}`, {
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
            .then((data) => setLavori(data))
            .catch((error) => {
                setError('Errore durante il recupero dei lavori');
                console.error(error);
            });
    }, [email]);

    // Funzione per gestire il click sul pulsante di maggiori informazioni
    const handleInfoClick = (lavoroId) => {
        setSelectedLavoroId(lavoroId); // Imposta l'ID dell'evento selezionato
    };

    // Funzione per chiudere il popup
    const closePopup = () => {
        setSelectedLavoroId(null); // Reset dell'evento selezionato, chiudendo così il popup
    };

    return (
        <div className="dashboardContainer">
            <h1>Tutti gli Annunci di Lavoro</h1>
            <hr/>
            {lavori.length > 0 ? (
                <div className="cards-container">
                    {lavori.map((lavoro) => (
                        <Card
                            key={lavoro.id}
                            data={{
                                title: lavoro.titolo,
                                image: lavoro.proprietario.fotoProfilo
                                    ? lavoro.proprietario.fotoProfilo
                                    : "https://via.placeholder.com/150/cccccc/000000?text=No+Image",
                                userName: `${lavoro.proprietario.nome} ${lavoro.proprietario.cognome}`,
                                parameter1: lavoro.posizioneLavorativa,
                                parameter2: lavoro.retribuzione ? `€ ${lavoro.retribuzione}` : "Non specificata",
                                parameter3: lavoro.nomeAzienda,
                            }}
                            labels={{
                                parameter1: "Posizione",
                                parameter2: "Retribuzione",
                                parameter3: "Azienda",
                            }}
                            onClick={() => console.log(`Cliccato su annuncio: ${lavoro.titolo}`)}
                            onInfoClick={() => handleInfoClick(lavoro.id)}
                        />
                    ))}
                </div>
            ) : (
                <p>Nessun annuncio di lavoro trovato</p>
            )}

            {/* Mostra il popup di dettaglio se un ID è selezionato */}
            {selectedLavoroId && (
                <LavoroView
                    id={selectedLavoroId}
                    onClose={closePopup}
                />
            )}
        </div>
    );
}
export default LavoriUtente