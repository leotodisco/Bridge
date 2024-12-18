import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "../css/AreaPersonaleStyle.css";
import '@fortawesome/fontawesome-free/css/all.min.css';

// eslint-disable-next-line react/prop-types
const AreaPersonale = ({ onLogout }) => {
    const [userData, setUserData] = useState(null); // Stato per i dati utente
    const [imgData, setImgData] = useState(null);
    const nav = useNavigate();

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const email = localStorage.getItem('email');
                const token = localStorage.getItem('token');

                if (!email || !token) {
                    alert("Non sei autenticato. Effettua il login.");
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

                if (!userResponse.ok || !imgResponse.ok) {
                    throw new Error(`Errore HTTP: ${userResponse.status} o ${imgResponse.status}`);
                }

                const userData = await userResponse.json();
                const imgBase64 = await imgResponse.text(); // Usa `.text()` per leggere direttamente la stringa Base64

                setUserData(userData);
                setImgData(imgBase64);
            } catch (error) {
                console.error("Errore durante il recupero dei dati personali o immagine:", error);
                alert("Errore durante il caricamento dei dati.");
            }
        };

        fetchUserData();
    }, [nav]);

    const eliminaAccount = async () => {
        if (window.confirm("Sei sicuro di voler eliminare il tuo account?")) {
            try {
                const email = localStorage.getItem('email');
                const token = localStorage.getItem('token');

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

                alert("Account eliminato con successo.");
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
                alert("Errore durante l'eliminazione dell'account.");
            }
        }
    };
    return (
        <div className="area-personale-container">
            {userData ? (
                <>
                    {/* Sezione sinistra */}
                    <div className="sinistra">
                        <div className="profile-section">
                            {/* Foto Profilo */}
                            <img
                                src={imgData ? `data:image/jpeg;base64,${imgData}` : '/default-profile.png'}
                                alt="Foto Profilo"
                                className="profile-picture"
                            />


                            {/* Pulsanti */}
                            <div className="action-buttons">
                                {/* Pulsante Logout */}
                                <button onClick={onLogout} className="logoutButton">
                                    <i className="fas fa-sign-out-alt"></i>
                                    <span>Log Out</span>
                                </button>

                                {/* Pulsante Delete Account */}
                                <button onClick={eliminaAccount} className="deleteButton">
                                    <i className="fas fa-trash-alt"></i>
                                    <span>Delete Account</span>
                                </button>
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
                                    <span className="data-value">{userData.genderUtente}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Titolo di Studio:</span>
                                    <span className="data-value">{userData.titoloDiStudioUtente}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Skill:</span>
                                    <span className="data-value">{userData.skillUtente}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Ruolo:</span>
                                    <span className="data-value">{userData.ruoloUtente}</span>
                                </div>
                                <div className="data-row">
                                    <span className="data-label">Lingue Parlate:</span>
                                    <span className="data-value">{userData.lingueParlateUtente}</span>
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
        </div>
    );
};

export default AreaPersonale;
