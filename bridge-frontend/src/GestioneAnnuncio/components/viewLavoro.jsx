import { useState, useEffect } from "react";
import LavoroView from "./LavoroRetrieve.jsx";
import Card from "../../GestioneEvento/components/Card.jsx";
import "../../GestioneEvento/css/card.css";
import {useNavigate} from "react-router-dom";

/**
 * @author Vito Vernellati
 * Componente per visualizzare tutti gli annunci di lavoro.
 * Utilizza il componente Card per mostrare informazioni principali
 * sugli annunci.
 *
 * Campi visualizzati:
 * - Titolo
 * - Posizione lavorativa
 * - Retribuzione
 * - Nome azienda
 */

const AllLavoroView = () => {
    const [lavori, setLavori] = useState([]); // Stato per memorizzare gli annunci di lavoro
    const [loading, setLoading] = useState(true); // Stato di caricamento
    const [error, setError] = useState(null); // Stato di errore
    const [selectedLavoroId, setSelectedLavoroId] = useState(null); // ID annuncio selezionato
    const nav = useNavigate();

    // Funzione per recuperare gli annunci di lavoro
    const fetchLavori = async () => {
        try {
            const email = localStorage.getItem('email');
            const token = localStorage.getItem('authToken');

            if (!email || !token) {
                alert("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await
                fetch("http://localhost:8080/api/annunci/view_lavori", {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                });

            if (!response.ok) {
                throw new Error("Errore durante il recupero degli annunci di lavoro");
            }
            const data = await response.json();
            setLavori(data);
        } catch (err) {
            setError("Si è verificato un errore nel recupero degli annunci: " + err.message);
        } finally {
            setLoading(false);
        }
    };

    // Effettua il fetch degli annunci al primo rendering
    useEffect(() => {
        fetchLavori();
    }, []);

    // Chiude il popup di dettaglio
    const closePopup = () => {
        setSelectedLavoroId(null);
    };

    if (loading) {
        return <p>Caricamento in corso...</p>;
    }

    if (error) {
        return <p>Errore nel caricamento degli annunci: {error}</p>;
    }

    return (
        <div>
            <h1>Tutti gli Annunci di Lavoro</h1>
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
                            onInfoClick={() => setSelectedLavoroId(lavoro.id)}
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
};

export default AllLavoroView;
