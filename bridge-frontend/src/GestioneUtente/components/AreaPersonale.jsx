import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "../css/AreaPersonaleStyle.css";
import '@fortawesome/fontawesome-free/css/all.min.css';
import ModificaPassword from "./formModificaPassword.jsx";
import ModificaUtente from "./formModificaUtente.jsx";
import {toast} from "react-toastify";

// eslint-disable-next-line react/prop-types
const AreaPersonale = ({ onLogout }) => {
    const [userData, setUserData] = useState(null); // Stato per i dati utente
    const [imgData, setImgData] = useState(null); // Stato per l'immagine profilo
    const [fotoProfilo, setFotoProfilo] = useState(null);  // Stato per la foto caricata nel frontend
    const [errorMessages, setErrorMessages] = useState({});
    const nav = useNavigate();
    const [showImageForm, setShowImageForm] = useState(false);  // Stato per la visibilità del form
    const [imageFile, setImageFile] = useState(null);  // Stato per il file immagine selezionato
    const [showModifyForm, setShowModifyForm] = useState(false);  // Stato per la visibilità del form di modifica dati
    const [showPasswordForm, setShowPasswordForm] = useState(false);
    const [showDeletePopup, setShowDeletePopup] = useState(false);

    const ruolo = localStorage.getItem('ruolo');
    console.log('Ruolo letto:', ruolo);

    const fetchUserData = async () => {
        try {
            const email = localStorage.getItem('email');
            const token = localStorage.getItem('authToken');

            if (!email || !token) {
                toast.error("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const [userResponse, imgResponse] = await Promise.all([
                fetch(`http://localhost:8080/areaPersonale/DatiUtente/${email}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                }),
                fetch(`http://localhost:8080/areaPersonale/DatiFotoUtente/${email}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                }),
            ]);


            if (!userResponse.ok) {
                throw new Error(`Errore HTTP userResponse: ${userResponse.status}`);
            }

            const userData = await userResponse.json();
            const imgBase64 = await imgResponse.text(); // Usa `.text()` per leggere direttamente la stringa Base64

            setUserData(userData);
            setImgData(imgBase64);
            localStorage.setItem('ruolo', userData.ruoloUtente);
        } catch (error) {
            console.error("Errore durante il recupero dei dati personali o immagine:", error);
            toast.error("Errore durante il caricamento dei dati.");
        }
    };

    useEffect(() => {
        fetchUserData();
    }, [nav]);

    const updateUserData = (newUserData) => {
        setUserData(newUserData);
    };

    const separaParole = (str) => {
        return str.replace(/([A-Z])/g, ' $1').trim();
    };

    const eliminaAccount = async () => {
        try {
            const email = localStorage.getItem('email');
            const token = localStorage.getItem('authToken');

            if (!email || !token) {
                toast.error("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await fetch(`http://localhost:8080/areaPersonale/elimina/${email}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error(`Errore HTTP: ${response.status}`);
            }
            // Effettua il logout
            await fetch('/authentication/logout', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });

            // Rimuove i dati dal client
            localStorage.clear();
            sessionStorage.clear();

            onLogout();
            // Reindirizza alla pagina di login
            nav('/login');
        } catch (error) {
            console.error("Errore durante l'eliminazione dell'account:", error);
        }
    };


    // Funzione per gestire il cambiamento dell'immagine (già presente)
    const aggiornaFotoProfilo = (event) => {
        const file = event.target.files[0];
        if (file && (file.type === "image/jpeg" || file.type === "image/jpg")) {
            const reader = new FileReader();
            reader.onload = () => {
                setFotoProfilo(reader.result); // Visualizza la preview dell'immagine
                setImageFile(file); // Salva il file selezionato
            };
            reader.readAsDataURL(file);
            setErrorMessages((prev) => {
                const updatedErrors = { ...prev };
                delete updatedErrors.fotoProfilo;
                return updatedErrors;
            });
        } else {
            setErrorMessages((prev) => ({
                ...prev,
                fotoProfilo: "Il file deve essere in formato JPG o JPEG.",
            }));
        }
    };

    const handleSubmitImage = async () => {
        if (!imageFile) {
            toast.warning("Per favore seleziona un'immagine.");
            return;
        }

        try {
            const email = localStorage.getItem('email');
            const token = localStorage.getItem('authToken');  // Assicurati che il token sia nel localStorage

            if (!token) {
                toast.error("Token non trovato. Effettua nuovamente il login.");
                return;
            }

            const formData = new FormData();
            formData.append('image', imageFile); // Aggiungi il file selezionato a FormData

            const response = await fetch(`http://localhost:8080/areaPersonale/modificaFotoUtente/${email}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`, // Invia il token nell'intestazione
                },
                body: formData, // Invia il FormData con il file
            });

            if (!response.ok) {
                throw new Error(`Errore HTTP: ${response.status}`);
            }

            const updatedImg = await response.text();  // Ricevi la risposta dal backend
            setImgData(updatedImg);  // Aggiorna l'immagine del profilo nel frontend
            setShowImageForm(false);  // Nasconde il form dopo il successo
        } catch (error) {
            console.error("Errore durante l'aggiornamento della foto del profilo:", error);
            toast.error("Errore durante l'aggiornamento della foto.");
        }
    };

    const navigate = useNavigate();


    return (
        <div className="area-personale-container">
            {userData ? (
                <>
                    {/* Sezione sinistra */}
                    <div className="sinistra">
                        <div className="profile-section">
                            {/* Foto Profilo */}
                            <img
                                src={fotoProfilo || (imgData ? `data:image/jpeg;base64,${imgData}` : '/immagineProfiloVuota.jpg')}
                                alt="Foto Profilo"
                                className="profile-picture"
                            />

                            <button onClick={() => setShowImageForm(!showImageForm)} className="modifyIMG">
                                <i className="fas fa-camera"></i>
                                <span> Cambia Foto</span>
                            </button>


                            {showImageForm && (
                                <div className="uploadImageForm">
                                    <input
                                        type="file"
                                        accept="image/*"
                                        onChange={aggiornaFotoProfilo}
                                        className="formEditText"
                                    />
                                    {errorMessages.fotoProfilo && <p className="error">{errorMessages.fotoProfilo}</p>}
                                    <button onClick={handleSubmitImage} className="caricaIMG">Carica Immagine</button>
                                    <button onClick={() => setShowImageForm(false)} className="caricaIMG">Annulla
                                    </button>
                                </div>
                            )}

                            <div className="action-buttons">
                                {/* Pulsante Logout */}
                                <button onClick={onLogout} className="logoutButton" title="Log Out">
                                    <i className="fas fa-sign-out-alt"></i>
                                </button>

                                {/* Pulsante Delete Account */}
                                <button onClick={() => setShowDeletePopup(true)} className="deleteButton"
                                        title="Elimina Account">
                                    <i className="fas fa-trash-alt"></i>
                                </button>

                                {/* Pulsante Modifica Dati */}
                                <button onClick={() => setShowModifyForm(!showModifyForm)} className="editButton"
                                        title="Modifica Dati">
                                    <i className="fas fa-edit"></i>
                                </button>

                                {/* Pulsante Modifica Password */}
                                <button onClick={() => setShowPasswordForm(!showPasswordForm)} className="editButton"
                                        title="Modifica Password">
                                    <i className="fa fa-key" aria-hidden="true"></i>
                                </button>
                            </div>

                            {showPasswordForm && <div className="popup-overlay">
                                <div className="popup-content">
                                    <button onClick={() => setShowPasswordForm(false)} className="close-popup">&times;
                                    </button>
                                    <ModificaPassword userData={userData}/>
                                </div>
                            </div>}

                            <div className="sectionButtons">
                                {ruolo=="Volontario" ? (
                                    <div>
                                        <button onClick={() => navigate('/eventi-utente')}>I miei Eventi</button>
                                        <button onClick={() => navigate('/view-my-alloggi/:email')}>I miei Alloggi
                                        </button>
                                        <button onClick={() => navigate('/lavori-utente')}>I miei Annunci di Lavoro
                                        </button>
                                    </div>
                                ) : null}

                                {ruolo == "FiguraSpecializzata" ? (
                                    <div>
                                        <button onClick={() => navigate('/corsi-utente')}>I miei Corsi</button>
                                        <button onClick={() => navigate('/consuleza-utente')}>Le mie Consulenze</button>
                                        <button onClick={() => navigate('/lavori-utente')}>I miei Annunci di Lavoro
                                        </button>
                                    </div>
                                ) : null}

                                {ruolo === "Rifugiato" ? (
                                    <div>
                                        <button>Candidature Lavoro</button>
                                        <button onClick={() => navigate('/view-my-alloggi/:email')}>Richieste di alloggio</button>
                                        <button onClick={() => navigate('/eventi-utente')}>Partecipazione Eventi</button>
                                        <button onClick={() => navigate('/consulenze-candidato')}>Richieste di Consulenza</button>
                                    </div>
                                ) : null}

                            </div>
                        </div>
                    </div>

                    {/* Divisore */}
                    <div className="divisore"></div>

                    {/* Sezione destra */}
                    <div className="destra">
                        <h1>Dati Personali</h1>
                        {userData ? (
                            <>
                                <div className="data-row">
                                    <span className="data-label">Nome:</span>
                                    <span className="data-value">{userData.nomeUtente}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Cognome:</span>
                                    <span className="data-value">{userData.cognomeUtente}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Email:</span>
                                    <span className="data-value">{userData.emailUtente}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Nazionalità:</span>
                                    <span className="data-value">{userData.nazionalitaUtente}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Data di nascita:</span>
                                    <span className="data-value">{userData.dataNascitaUtente}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Genere:</span>
                                    <span className="data-value">{userData.genderUtente ? separaParole(userData.genderUtente) : 'Non Disponibile'}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Titolo di Studio:</span>
                                    <span className="data-value">{userData.titoloDiStudioUtente ? separaParole(userData.titoloDiStudioUtente) : 'Non Disponibile'}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Skill:</span>
                                    <span className="data-value">{userData.skillUtente}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Ruolo:</span>
                                    <span className="data-value">{userData.ruoloUtente ? separaParole(userData.ruoloUtente) : 'Non Disponibile'}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Lingue Parlate:</span>
                                    <span className="data-value">{userData.lingueParlateUtente}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Disponibilità:  </span>
                                    <div className="data-value orariDisp">
                                        {userData.disponibilitaUtente ? (
                                            <ul>
                                                {userData.disponibilitaUtente.split(',').map((disponibilita, index) => (
                                                    <li key={index}>
                                                        {disponibilita.trim()}
                                                    </li>
                                                ))}
                                            </ul>
                                        ) : (
                                            <p>Non disponibile</p>
                                        )}
                                    </div>
                                </div>
                            </>
                        ) : (
                            <p>Caricamento dei dati personali...</p>
                        )}
                    </div>
                </>
            ) : (
                <p>Caricamento dei dati personali...</p>
            )}




            {/* Modifica Dati Form */}
            {showModifyForm && (
                <div className="popup-overlay">
                    <div className="popup-content">
                        {/* Pulsante di chiusura per il popup di modifica */}
                        <button onClick={() => {
                            setShowModifyForm(false);
                            fetchUserData(); // Forza il fetch dei dati al momento della chiusura
                        }} className="close-popup ">&times;</button>
                        <ModificaUtente userData={userData} setUserData={updateUserData}
                                        onSuccess={() => {
                                            setShowModifyForm(false); // Chiude il popup
                                            fetchUserData(); // Aggiorna i dati
                                        }}/>
                    </div>
                </div>
            )}

            {showDeletePopup && (
                <div className="popup-overlay">
                    <div className="popup-content popup-eliminazione"> {/* Aggiungi la classe popup-eliminazione */}
                        <button onClick={() => setShowDeletePopup(false)} className="close-popup"></button>
                        <h2>Sei sicuro di voler eliminare il tuo account?</h2>
                        <p>Questa azione è irreversibile.</p>
                        <div className="popup-buttons">
                            <button onClick={eliminaAccount} className="popup-button confirm">
                                Conferma
                            </button>
                            <button onClick={() => setShowDeletePopup(false)} className="popup-button cancel">
                                Annulla
                            </button>
                        </div>
                    </div>
                </div>
            )}

        </div>
    );
};

export default AreaPersonale;
