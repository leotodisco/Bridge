import { useState, useEffect } from "react";
import LavoroView from "./LavoroRetrieve.jsx";
import Card from "../../GestioneEvento/components/Card.jsx";
import "../../GestioneEvento/css/card.css";
import { useNavigate } from "react-router-dom";
import CreaLavoro from "./formLavoro.jsx";

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
    const [tipoFiltro, setTipoFiltro] = useState("");
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const ruolo = localStorage.getItem("ruolo");
    const [showCreatePopup, setShowCreatePopup] = useState(false);
    const [userImages, setUserImages] = useState({}); // Stato per immagini degli utenti

    const closeCreatePopup = () => {
        setShowCreatePopup(false); // Chiudi il popup di creazione evento
    };

    // Funzione per recuperare gli annunci di lavoro
    const fetchLavori = async (tipo = "") => {
        setLoading(true);
        try {
            const email = localStorage.getItem('email');
            const token = localStorage.getItem('authToken');

            const url = tipo
                ? `http://localhost:8080/api/annunci/view_lavori/filter?tipo=${tipo}`
                : `http://localhost:8080/api/annunci/view_lavori`;

            if (!email || !token) {
                toast.error("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await
                fetch(url, {
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

            // Recupera le immagini degli utenti
            const imagesData = {};
            for (const lavoro of data) {
                const emailProprietario = lavoro.proprietario.email;
                try {
                    const imgResponse = await fetch(`http://localhost:8080/areaPersonale/DatiFotoUtente/${emailProprietario}`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json',
                        },
                    });

                    if (imgResponse.ok) {
                        const imgBase64 = await imgResponse.text();
                        imagesData[emailProprietario] = `data:image/jpeg;base64,${imgBase64}`;
                    } else {
                        imagesData[emailProprietario] = "https://via.placeholder.com/150/cccccc/000000?text=No+Image";
                    }
                } catch (error) {
                    console.error("Errore nel recupero dell'immagine utente:", error);
                    imagesData[emailProprietario] = "https://via.placeholder.com/150/cccccc/000000?text=No+Image";
                }
            }
            setUserImages(imagesData);
        } catch (err) {
            setError("Si è verificato un errore nel recupero degli annunci: " + err.message);
        } finally {
            setLoading(false);
        }
    };

    const toggleDropdown = () => {
        setIsDropdownOpen(!isDropdownOpen); // Apre o chiude il dropdown
    };

    const handleFiltroChange = (event) => {
        const tipo = event.target.value;
        setTipoFiltro(tipo);
        fetchLavori(tipo); // Filtra i risultati
        setIsDropdownOpen(false);
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

    const filterOptions = {
        "": "Tutte",
        FULL_TIME: "Full Time",
        PART_TIME: "Part Time",
        APPRENDISTATO: "Apprendistato",
        INTERNSHIP: "Stage/Tirocinio",
        PROGETTO: "Contratto a Progetto",
        COLLABORAZIONE: "Collaborazione",
        COOPERATIVA: "Cooperativa",
        ALTRO: "Altro",
    };


    return (
        <div>
            <h1></h1>

            <div className="headerForm-container">
                <h1 className="header-title">Tutti gli Annunci di Lavoro</h1>
                {/* Pulsante per aggiungere un nuovo evento */}
                {ruolo === "Volontario" && (
                    <button
                        className="btn btn-circle"
                        onClick={() => setShowCreatePopup(true)}
                    >
                        +
                    </button>
                )}
            </div>

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
            {lavori.length > 0 ? (
                <div className="cards-container">
                    {lavori.map((lavoro) => (
                        <Card
                            key={lavoro.id}
                            data={{
                                title: lavoro.titolo,
                                image: userImages[lavoro.proprietario.email] || "https://via.placeholder.com/150/cccccc/000000?text=No+Image",
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

            {showCreatePopup && (
                <div className="popup-overlay show">
                    <div className="popup-content">
                        <button className="close-popup" onClick={closeCreatePopup}>
                            &times;
                        </button>
                        <CreaLavoro onClose={closeCreatePopup} />
                    </div>
                </div>
            )}

        </div>
    );
};

export default AllLavoroView;
