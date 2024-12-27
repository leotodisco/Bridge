import { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import Card from "../../GestioneEvento/components/Card.jsx";
import ConsulenzaView from "../../GestioneAnnuncio/components/ConsulenzaRetrive.jsx";
import '../css/PopupCandidati.css';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import FormConsulenza from "../../GestioneAnnuncio/components/formConsulenza.jsx"; // Importa lo stile di default

const CandidateDetailsPopup = ({ details, imgData, onClose }) => {
    const separaParole = (str) => str.replace(/([A-Z])/g, ' $1').trim();

    return (
        <div className="popup">
            <h3>Dettagli Candidato</h3>
            <img
                src={imgData ? `data:image/jpeg;base64,${imgData}` : '/default-profile.png'}
                alt="Foto Profilo"
                className="profile-picture"
            />
            <div className="data-row">
                <span className="data-label">Nome:</span>
                <span className="data-value">{details.nomeUtente}</span>
            </div>
            <div className="data-row">
                <span className="data-label">Cognome:</span>
                <span className="data-value">{details.cognomeUtente}</span>
            </div>
            <div className="data-row">
                <span className="data-label">Email:</span>
                <span className="data-value">{details.emailUtente}</span>
            </div>
            <div className="data-row">
                <span className="data-label">Nazionalità:</span>
                <span className="data-value">{details.nazionalitaUtente}</span>
            </div>
            <div className="data-row">
                <span className="data-label">Data di nascita:</span>
                <span className="data-value">{details.dataNascitaUtente}</span>
            </div>
            <div className="data-row">
                <span className="data-label">Genere:</span>
                <span className="data-value">
                    {details.genderUtente ? separaParole(details.genderUtente) : 'Non Disponibile'}
                </span>
            </div>
            <div className="data-row">
                <span className="data-label">Titolo di Studio:</span>
                <span className="data-value">
                    {details.titoloDiStudioUtente ? separaParole(details.titoloDiStudioUtente) : 'Non Disponibile'}
                </span>
            </div>
            <div className="data-row">
                <span className="data-label">Skill:</span>
                <span className="data-value">{details.skillUtente}</span>
            </div>
            <div className="data-row">
                <span className="data-label">Lingue Parlate:</span>
                <span className="data-value">{details.lingueParlateUtente}</span>
            </div>
            <button onClick={onClose}>Chiudi</button>
        </div>
    );
};


const ConsulenzaUtente = () => {
    const [consulenza, setConsulenza] = useState([]);
    const [error, setError] = useState(null);
    const [selectedConsulenzaId, setSelectedConsulenzaId] = useState(null); // Stato per l'evento selezionato (per dettagli)
    const [isDetailPopupOpen, setIsDetailPopupOpen] = useState(false); // Stato per il popup dei dettagli
    const [selectedCandidates, setSelectedCandidates] = useState(null); // Stato per i candidati
    const [isCandidatesDetail, setIsCandidatesDetail] = useState(false); // Stato per il popup dei dettagli
    const [candidateDetails, setCandidateDetails] = useState(null); // Dettagli del candidato selezionato
    const [imgData, setImgData] = useState(null);
    const [isPopupVisible, setPopupVisible] = useState(false); // State for showing the create modal
    // Recupera l'email dell'utente loggato (esempio: salvata in localStorage)
    const email = localStorage.getItem('email'); // Assicurati che l'email sia salvata.
    const token = localStorage.getItem('authToken');

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
        setIsDetailPopupOpen(true); // Mostra il popup dei dettagli
    };

    // Funzione per chiudere il popup dei dettagli
    const closePopup = () => {
        setIsDetailPopupOpen(false); // Chiude il popup dei dettagli
    };
    const closePopupForm = () => {
        setPopupVisible(false);
    };

    // Funzione per gestire la visualizzazione dei candidati
    const handleShowCandidates = (candidati, consulenzaId) => {
        setSelectedCandidates(candidati); // Imposta i candidati selezionati
        setSelectedConsulenzaId(consulenzaId); // Imposta l'ID della consulenza selezionata
    };

    // Funzione per chiudere il popup dei candidati
    const closeCandidatesPopup = () => {
        setSelectedCandidates(null); // Reset dei candidati selezionati
    };

    const updateConsulenza = (consulenzaId, candidateEmail, isAccepted) => {
        setConsulenza((prevConsulenza) =>
            prevConsulenza.map((event) =>
                event.id === consulenzaId
                    ? {
                        ...event,
                        candidati: event.candidati.map((candidate) =>
                            candidate.email === candidateEmail
                                ? { ...candidate, isAccepted }  // Aggiorna lo stato di accettato
                                : candidate
                        ),
                    }
                    : event
            )
        );
    };
    const acceptCandidate = (candidateEmail) => {
        // 1. Aggiorna lo stato nel frontend
        updateConsulenza(selectedConsulenzaId, candidateEmail, true);  // Il candidato è accettato

        fetch(`http://localhost:8080/api/annunci/accetta/${selectedConsulenzaId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`, // Includi il token di autenticazione
            },
            body: JSON.stringify({
                email: candidateEmail, // Email del candidato da accettare
            }),
        })
            .then((response) => {
                if (response.ok) {
                    toast.info("Candidato accettato");
                    return response.text();
                } else {
                    throw new Error('Errore durante l\'accettazione del candidato.');
                }
            })
            .catch((error) => {
                toast.error("Errore durante la sincronizzazione con il backend");
                console.error(error);
                updateConsulenza(selectedConsulenzaId, candidateEmail, false); // Reverti lo stato del candidato
            });

        closeCandidatesPopup(); // Chiudi il popup dei candidati dopo l'azione

    };

    const removeCandidate = (candidateEmail) => {
        // 1. Aggiorna lo stato nel frontend
        updateConsulenza(selectedConsulenzaId, candidateEmail, false);  // Il candidato è rifiutato

        fetch(`http://localhost:8080/api/annunci/rifiuta/${selectedConsulenzaId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`, // Includi il token di autenticazione
            },
            body: JSON.stringify({
                email: candidateEmail, // Email del candidato da accettare
            }),
        })
            .then((response) => {
                if (response.ok) {
                    toast.warning("Candidato rifiutato");
                    return response.text();
                } else {
                    throw new Error('Errore durante il rifiuto del candidato.');
                }
            })
            .catch((error) => {
                // Se si verifica un errore, annulla l'aggiornamento fatto nel frontend
                toast.error("Errore durante la sincronizzazione con il backend");
                console.error(error);
                updateConsulenza(selectedConsulenzaId, candidateEmail, true); // Reverti lo stato del candidato
            });

        closeCandidatesPopup(); Error('Errore durante il rifiuto del candidato.');



    };


    const handleShowCandidateDetails = async (candidateEmail) => {
        try {
            if (!email || !token) {
                alert("Non sei autenticato. Effettua il login.");
                return;
            }

            const [userResponse, imgResponse] = await Promise.all([
                fetch(`http://localhost:8080/areaPersonale/DatiUtente/${candidateEmail}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                }),
                fetch(`http://localhost:8080/areaPersonale/DatiFotoUtente/${candidateEmail}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                }),
            ]);

            if (!userResponse.ok || !imgResponse.ok) {
                throw new Error(`Errore HTTP: ${userResponse.status} o ${imgResponse.status}`);
            }

            const userData = await userResponse.json();
            const imgBase64 = await imgResponse.text(); // Usa `.text()` per leggere direttamente la stringa Base64

            setCandidateDetails(userData);
            setImgData(imgBase64);
            setIsCandidatesDetail(true);
            localStorage.setItem('ruolo', userData.ruoloUtente);
        } catch (error) {
            console.error("Errore durante il recupero dei dati personali o immagine:", error);
            alert("Errore durante il caricamento dei dati.");
        }
    };

    const closeCandidateDetails = () => {
        setCandidateDetails(null);
        setImgData(null);
        setIsCandidatesDetail(false);
    };

    return (
        <div>
            <h1>Le mie consulenze</h1>
            <hr />

            <button onClick={() => setPopupVisible(true)} className="openPopupButton">Crea una Consulenza</button>

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
                                <button onClick={() => handleShowCandidates(event.candidati, event.id)}>
                                    Mostra Candidati
                                </button>
                            </Card>
                        );
                    })}
                </div>
            ) : (
                <p>Non hai ancora Consulenza pubblicati.</p>
            )}
            {/* Mostra il popup dei dettagli */}
            {isDetailPopupOpen && (
                <ConsulenzaView id={selectedConsulenzaId} onClose={closePopup} />
            )}
            {/* Mostra il popup dei candidati */}
            {selectedCandidates && (
                <div className="popup">
                    <h2>Candidati</h2>
                    {Object.keys(selectedCandidates).length > 0 ? (
                        <ul>
                            {Object.entries(selectedCandidates).map(([email, isAccepted], index) => (
                                <li key={index}>
                                    <div className="email-section">
                                        <span>{email}</span>
                                        <i className="fas fa-info-circle"
                                           onClick={() => handleShowCandidateDetails(email)}></i>
                                    </div>
                                    {isAccepted ? (
                                        <span> (Candidato accettato)</span>
                                    ) : (
                                        <div className="action-buttons">
                                            <button onClick={() => acceptCandidate(email)}>Accetta</button>
                                            <button onClick={() => removeCandidate(email)}>Rifiuta</button>
                                        </div>
                                    )}
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>Nessun candidato disponibile per questa consulenza.</p>
                    )}
                    <button onClick={closeCandidatesPopup}>Chiudi</button>
                    {isCandidatesDetail && candidateDetails && (
                        <CandidateDetailsPopup
                            details={candidateDetails}
                            imgData={imgData}
                            onClose={closeCandidateDetails}
                        />
                    )}


                </div>
            )}
            {isPopupVisible && (
                <div className="popupOverlay" onClick={(e) => e.target === e.currentTarget && closePopupForm()}>
                    <div className="popupContainer">
                        <button onClick={closePopupForm} className="closePopupButton">X</button>
                        <FormConsulenza />
                    </div>
                </div>
            )}
        </div>
    );
};

// Definizione dei propTypes
CandidateDetailsPopup.propTypes = {
    details: PropTypes.shape({
        nomeUtente: PropTypes.string,
        cognomeUtente: PropTypes.string,
        emailUtente: PropTypes.string,
        nazionalitaUtente: PropTypes.string,
        dataNascitaUtente: PropTypes.string,
        genderUtente: PropTypes.string,
        titoloDiStudioUtente: PropTypes.string,
        skillUtente: PropTypes.string,
        lingueParlateUtente: PropTypes.string,
    }).isRequired,
    imgData: PropTypes.string,
    onClose: PropTypes.func.isRequired,
};

export default ConsulenzaUtente;