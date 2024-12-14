import { useState, useEffect } from "react";
import '../css/ConsulenzeList.css';
import Card from "../../GestioneEvento/components/Card.jsx";
import "../../GestioneEvento/css/card.css";
import ConsulenzaView from "./ConsulenzaRetrive.jsx";
/*
* @author Geraldine Montella
*
* Componente per visualizzare tutti gli
* annunci di consulenza
*
* Si utilizza un componente Card per
* visualizzare le informazioni caratterizzanti
*
* Campi Personalizzati:
* - tipo consulenza
* - Orari Disponibili
* - contatto
*/

const AllConsulenzaView = () => {
    // Stato per memorizzare le consulenze
    const [consulenze, setConsulenze] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedConsulenzaId, setSelectedConsulenzaId] = useState(null);

    // Funzione per ottenere tutte le consulenze
    const fetchConsulenze = async () => {
        try {
            // Usa fetch per recuperare i dati
            const response = await fetch('http://localhost:8080/api/annunci/view_consulenze');
            if (!response.ok) {
                throw new Error('Errore durante il recupero delle consulenze');
            }
            const data = await response.json(); // Converte la risposta in formato JSON
            setConsulenze(data);
        } catch (err) {
            setError('Si è verificato un errore nel recupero delle consulenze: ' + err.message);
        } finally {
            setLoading(false);
        }
    };

    const closePopup = () => {
        setSelectedConsulenzaId(null);
    };

    // Funzione per ottenere le consulenze di un proprietario specifico
    useEffect(() => {
        fetchConsulenze();
    }, []);

    if (loading) {
        return <p>Caricamento in corso...</p>;
    }

    if (error) {
        return <p>Errore nel caricamento della consulenza: {error}</p>;
    }

    return (
        <div>
            <h1>Tutte le Consulenze</h1>
            {consulenze.length > 0 ? (
                <div className="cards-container">
                    {consulenze.map((event) => (
                        <Card
                            key={event.id}
                            data={{
                                title: event.titolo,
                                image: event.proprietario.fotoProfilo
                                    ? event.proprietario.fotoProfilo
                                    : "https://via.placeholder.com/150/cccccc/000000?text=No+Image",
                                userName: `${event.proprietario.nome} ${event.proprietario.cognome}`,
                                parameter1: event.tipo,
                                parameter2: event.orariDisponibili,
                                parameter3: event.proprietario.email,
                            }}
                            labels={{
                                parameter1: "Titolo",
                                    parameter2: "Orari",
                                parameter3: "Email",
                            }}
                            onClick={() => console.log(`Cliccato su consulenza: ${event.titolo}`)}
                            onInfoClick={() => setSelectedConsulenzaId(event.id)}
                        />
                    ))}
                </div>
            ) : (
                <p>Nessuna consulenza trovata</p>
            )}

            {/* Mostra il popup se un ID è selezionato */}
            {selectedConsulenzaId && (
                <ConsulenzaView
                    id={selectedConsulenzaId}
                    onClose={closePopup}
                />
            )}
        </div>
    );
};

export default AllConsulenzaView;
