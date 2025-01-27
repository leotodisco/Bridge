import { useEffect, useState } from "react";
import Card from "./Card.jsx";
import "../../GestioneEvento/css/card.css";
import EventView from "./EventoRetrieveView.jsx";
// Per installare la libreria: npm install date-fns
import { format } from "date-fns";
import "../css/AllEventiStyle.css";
import { useNavigate } from "react-router-dom";
import CreaEvento from "./formEvento.jsx";
import { toast } from "react-toastify";

/*
 * @author Alessia De Filippo
 *
 * Componente per visualizzare tutti gli eventi.
 * Utilizza il componente Card per
 * visualizzare le informazioni principali di ogni evento.
 * Campi personalizzabili:
 * - Data
 * - Ora
 * - Lingua Parlata
 */

const formatDate = (dateString) => {
    return format(new Date(dateString), "dd-MM-yyyy"); // Converte nel formato dd-MM-yyyy
};

const formatTime = (timeString) => {
    const [hours, minutes] = timeString.split(":");
    return `${hours}:${minutes}`; // HH:mm
};

const AllEventsView = () => {
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedEventId, setSelectedEventId] = useState(null); // Stato per il popup
    const [showCreatePopup, setShowCreatePopup] = useState(false); // Stato per il popup di creazione evento
    const [userImages, setUserImages] = useState({}); // Stato per le immagini degli utenti
    const nav = useNavigate();
    const ruolo = localStorage.getItem("ruolo");

    const fetchEvents = async () => {
        try {
            const email = localStorage.getItem('email');
            const token = localStorage.getItem('authToken');

            if (!email || !token) {
                toast.error("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await fetch("http://localhost:8080/api/eventi/all", {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error("Errore durante il recupero degli eventi");
            }
            const data = await response.json();
            setEvents(data);

            const eventImagesData = {};
            for (const event of data) {
                const email = event.organizzatore.email;
                try {
                    const imgResponse = await fetch(`http://localhost:8080/areaPersonale/DatiFotoUtente/${email}`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json',
                        },
                    });

                    if (imgResponse.ok) {
                        const imgBase64 = await imgResponse.text();
                        eventImagesData[email] = imgBase64;
                    } else {
                        eventImagesData[email] = "https://via.placeholder.com/150/cccccc/000000?text=No+Image";
                    }
                } catch (error) {
                    console.error(error);
                    eventImagesData[email] = "https://via.placeholder.com/150/cccccc/000000?text=No+Image";
                }
            }
            setUserImages(eventImagesData);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const closePopup = () => {
        setSelectedEventId(null); // Chiudi il popup
    };

    const closeCreatePopup = () => {
        setShowCreatePopup(false); // Chiudi il popup di creazione evento
    };

    useEffect(() => {
        fetchEvents();
    }, []);

    if (loading) {
        return <p>Caricamento in corso...</p>;
    }

    if (error) {
        return <p>Errore: {error}</p>;
    }

    return (
        <div id="allEventsView">
            <div>
                {/* Contenitore del titolo e del pulsante */}
                <div className="headerForm-container">
                    <h1 className="header-title">Tutti gli Eventi</h1>
                    {/* Pulsante per aggiungere un nuovo evento */}
                    {ruolo === "Volontario" && (
                        <button
                            className="btn btn-circle"
                            onClick={() => setShowCreatePopup(true)} // Apri il popup
                        >
                            +
                        </button>
                    )}
                </div>

                {events.length > 0 ? (
                    <div className="cards-container">
                        {events.map((event) => {
                            const eventImage = userImages[event.organizzatore.email]
                                ? `data:image/jpeg;base64,${userImages[event.organizzatore.email]}`
                                : "https://via.placeholder.com/150/cccccc/000000?text=No+Image";
                            return (
                                <Card
                                    key={event.id}
                                    data={{
                                        title: event.nome,
                                        image: eventImage,
                                        userName: `${event.organizzatore.nome} ${event.organizzatore.cognome}`,
                                        parameter1: formatDate(event.data), // Questo è Parametro 1
                                        parameter2: formatTime(event.ora),  // Questo è Parametro 2
                                        parameter3: event.linguaParlata, // Questo è Parametro 3
                                    }}
                                    labels={{
                                        parameter1: "Data",
                                        parameter2: "Ora",
                                        parameter3: "Lingua",
                                    }}
                                    onClick={() => console.log(`Cliccato su evento: ${event.nome}`)}
                                    onInfoClick={() => setSelectedEventId(event.id)} // Mostra il popup
                                />
                            );
                        })}
                    </div>
                ) : (
                    <p>Nessun evento trovato.</p>
                )}

                {/* Mostra il popup se selectedEventId è impostato */}
                {selectedEventId && (
                    <EventView id={selectedEventId} onClose={closePopup} />
                )}

                {/* Popup creazione evento */}
                {showCreatePopup && (
                    <CreaEvento onClose={closeCreatePopup} />
                )}
            </div>
        </div>
    );
};

export default AllEventsView;
