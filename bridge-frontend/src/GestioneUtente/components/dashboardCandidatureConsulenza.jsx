import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Card from "../../GestioneEvento/components/Card.jsx";
import ConsulenzaView from "../../GestioneAnnuncio/components/ConsulenzaRetrive.jsx";
import '../css/PopupCandidati.css';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


const ConsulenzaUtente = () => {
    const [consulenza, setConsulenza] = useState([]);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const [selectedConsulenzaId, setSelectedConsulenzaId] = useState(null); // Stato per l'evento selezionato (per dettagli)
    const [isDetailPopupOpen, setIsDetailPopupOpen] = useState(false); // Stato per il popup dei dettagli
    const email = localStorage.getItem('email'); // Assicurati che l'email sia salvata.
    const token = localStorage.getItem('authToken');
    const ruolo = localStorage.getItem('ruolo');

    console.log(ruolo);
    useEffect(() => {

        if (!token) {
            toast.error("Token non trovato. Effettua nuovamente il login.");
            return;
        }

        // Recupera gli eventi dell'utente
        fetch(`http://localhost:8080/api/annunci/candidature?email=${email}`, {
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
        setIsDetailPopupOpen(true); // Mostra il popup dei dettagli
    };

    // Funzione per chiudere il popup dei dettagli
    const closePopup = () => {
        setIsDetailPopupOpen(false); // Chiude il popup dei dettagli
    };



    return (
        <div>
            <h1>Consulenze candidate</h1>
            <hr />

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
                            </Card>
                        );
                    })}
                </div>
            ) : (
                <div className="container">
                    <p>Non ti sei candidato a nessuna consulenza</p>
                    <hr/>
                    <p>Cerca la tra le consulenze la migliore per le tue esigenze</p>
                    <button onClick={() => navigate('/view-consulenza')}>Consulenze</button>
                </div>
            )}
            {/* Mostra il popup dei dettagli */}
            {isDetailPopupOpen && (
                <ConsulenzaView id={selectedConsulenzaId} onClose={closePopup} />
            )}
        </div>
    );
};

export default ConsulenzaUtente;