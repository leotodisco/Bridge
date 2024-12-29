import { useState, useEffect } from "react";
import '../css/ConsulenzeList.css';
import '../css/PopUpForm.css';
import Card from "../../GestioneEvento/components/Card.jsx";
import "../../GestioneEvento/css/card.css";
import "../css/filterBar.css";
import ConsulenzaView from "./ConsulenzaRetrive.jsx";
import FormConsulenza from "./formConsulenza.jsx";
import {useNavigate} from "react-router-dom";
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
    const [isFiguraSpecializzata, setIsFiguraSpecializzata] = useState(false);
    const [isPopupVisible, setPopupVisible] = useState(false); // State for showing the create modal
    const [userImages, setUserImages] = useState({}); // Stato per le immagini dei profili
    const [tipoFiltro, setTipoFiltro] = useState("");
    const nav = useNavigate();
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);

    // Funzione per ottenere tutte le consulenze
    const fetchConsulenze = async (tipo = "") => {
        setLoading(true);
        try {
            // Usa fetch per recuperare i dati
            const token = localStorage.getItem('authToken');
            const url = tipo
                ? `http://localhost:8080/api/annunci/view_consulenze/filter?tipo=${tipo}`
                : `http://localhost:8080/api/annunci/view_consulenze`;

            console.log("Calling URL:", url); // Log per verificare l'URL

            if (!token) {
                alert("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await
                fetch(url,{
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                });

            if (!response.ok) {
                throw new Error('Errore durante il recupero delle consulenze');
            }
            const data = await response.json(); // Converte la risposta in formato JSON
            console.log("Fetched data:", data);
            setConsulenze(data);

            const userImagesData = {};
            for (const consulenza of data) {
                const email = consulenza.proprietario.email;
                try {
                    // check su token:
                    if (!token) {
                        alert("Non sei autenticato. Effettua il login.");
                        nav('/login');
                        return;
                    }

                    const imgResponse = await fetch(`http://localhost:8080/areaPersonale/DatiFotoUtente/${email}`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json',
                        },
                    });

                    if (imgResponse.ok) {
                        const imgBase64 = await imgResponse.text();
                        userImagesData[email] = imgBase64;
                    } else {
                        userImagesData[email] = "https://via.placeholder.com/150/cccccc/000000?text=No+Image"; // Placeholder in caso di errore
                    }
                } catch (error) {
                    console.error(`Errore durante il recupero dell'immagine per ${email}:`, error);
                    userImagesData[email] = "https://via.placeholder.com/150/cccccc/000000?text=No+Image"; // Placeholder in caso di errore
                }
            }
            setUserImages(userImagesData); // Salva tutte le immagini nello stato
        } catch (err) {
            setError('Si è verificato un errore nel recupero delle consulenze: ' + err.message);
        } finally {
            setLoading(false);
        }
    };

    const closePopup = () => {
        setPopupVisible(false);
        setSelectedConsulenzaId(null);
    };


    //Funzione per aggiornare le card in caso in cui si effettua
    //una modifica
    const handleUpdateConsulenza = (updatedConsulenza, isDeleted = false) => {
        //nel caso si tratti di un update dovuto da una eliminazione
        if (isDeleted) {
            setConsulenze((prevConsulenze) =>
                prevConsulenze.filter((consulenza) => consulenza.id !== updatedConsulenza.id)
            );
        } else {
            setConsulenze((prevConsulenze) =>
                prevConsulenze.map((consulenza) =>
                    consulenza.id === updatedConsulenza.id ? updatedConsulenza : consulenza
                )
            );
        }
    };

    const toggleDropdown = () => {
        setIsDropdownOpen(!isDropdownOpen); // Apre o chiude il dropdown
    };

    const handleFiltroChange = (event) => {
        const tipo = event.target.value;
        setTipoFiltro(tipo);
        fetchConsulenze(tipo); // Filtra i risultati
        setIsDropdownOpen(false); // Chiude il menu a tendina
    };

    // Funzione per ottenere le consulenze di un proprietario specifico
    useEffect(() => {
        const ruoloUtente = localStorage.getItem('ruolo'); // Supponiamo che il ruolo dell'utente sia memorizzato nel localStorage
        if (ruoloUtente === 'FiguraSpecializzata') {
            setIsFiguraSpecializzata(true);
        }
        fetchConsulenze();
    }, []);

    if (loading) {
        return <p>Caricamento in corso...</p>;
    }

    if (error) {
        return <p>Errore nel caricamento della consulenza: {error}</p>;
    }

    const filterOptions = {
        "": "Tutte",
        SANITARIA: "Sanitaria",
        LEGALE: "Legale",
        COMMERCIALE: "Commerciale",
        PSICOLOGICA: "Psicologica",
        TRADUTTORE: "Traduttore",
    };

    return (
        <div>
            <h1>Tutte le Consulenze</h1>
            <hr/>
            {isFiguraSpecializzata && (
                <button onClick={() => setPopupVisible(true)} className="openPopupButton">Crea una Consulenza</button>
            )}


            <div className="filter-container">
                <button className="filter-button" onClick={toggleDropdown}>
                    {filterOptions[tipoFiltro] || "Filtra per tipo"} {/* Mostra nome leggibile */}
                    <span className={`icon ${isDropdownOpen ? "open" : ""}`}>
            <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                fill="currentColor"
                viewBox="0 0 16 16"
            >
                <path
                    fillRule="evenodd"
                    d="M1 4l7 8 7-8H1z"
                />
            </svg>
        </span>
                </button>

                {isDropdownOpen && (
                    <div className={`filter-dropdown ${isDropdownOpen ? "open" : ""}`}>
                        <ul>
                            {/* Generazione dinamica della lista con nomi leggibili */}
                            {Object.entries(filterOptions).map(([value, label]) => (
                                <li
                                    key={value}
                                    onClick={() => handleFiltroChange({target: {value}})}
                                >
                                    {label}
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>

            {consulenze.length > 0 ? (
                <div className="cards-container">
                    {consulenze.map((event) => {
                        const profileImage = userImages[event.proprietario.email]
                            ? `data:image/jpeg;base64,${userImages[event.proprietario.email]}`
                            : "https://via.placeholder.com/150/cccccc/000000?text=No+Image"; // Usa Base64 o fallback
                        return (
                            <Card
                                key={event.id}
                                data={{
                                    title: event.titolo,
                                    image: profileImage,
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
                                onInfoClick={() => setSelectedConsulenzaId(event.id)}
                            />
                        );
                    })}
                </div>
            ) : (
                <p>Nessuna consulenza trovata</p>
            )}

            {/* Mostra il popup se un ID è selezionato */}
            {selectedConsulenzaId && (
                <ConsulenzaView
                    id={selectedConsulenzaId}
                    onClose={closePopup}
                    onUpdate={handleUpdateConsulenza}
                    //serve per aggiornare la pagina delle card
                    //se si effettuano modifiche agli annunci
                />
            )}

            {isPopupVisible && (
                <div className="popupOverlay" onClick={(e) => e.target === e.currentTarget && closePopup()}>
                    <div className="popupContainer">
                        <button onClick={closePopup} className="closePopupButton">X</button>
                        <FormConsulenza/>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AllConsulenzaView;
