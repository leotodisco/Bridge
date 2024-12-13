import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AreaPersonale = () => {
    const [userData, setUserData] = useState(null); // Stato per i dati utente
    const nav = useNavigate();

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const email = localStorage.getItem('email'); // Email salvata nel localStorage
                const token = localStorage.getItem('token'); // Token JWT

                if (!email || !token) {
                    alert("Non sei autenticato. Effettua il login.");
                    nav('/login'); // Reindirizza al login se dati mancanti
                    return;
                }

                const response = await fetch(`http://localhost:8080/areaPersonale/DatiUtente/${email}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`, // Autenticazione JWT
                        'Content-Type': 'application/json',
                    },
                });

                if (!response.ok) {
                    throw new Error(`Errore HTTP: ${response.status}`);
                }

                const data = await response.json();
                setUserData(data);
            } catch (error) {
                console.error("Errore durante il recupero dei dati personali:", error);
                alert("Errore durante il caricamento dei dati personali.");
            }
        };

        fetchUserData(); // Richiama la funzione al caricamento del componente
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
                localStorage.clear(); // Rimuove i dati dal localStorage
                nav('/'); // Reindirizza alla homepage
            } catch (error) {
                console.error("Errore durante l'eliminazione dell'account:", error);
                alert("Errore durante l'eliminazione dell'account.");
            }
        }
    };

    return (
        <div className="area-personale-container">
            <h1>Area Personale</h1>
            {userData ? (
                <div>
                    <p><strong>Nome:</strong> {userData.nomeUtente}</p>
                    <p><strong>Cognome:</strong> {userData.cognomeUtente}</p>
                    <p><strong>Email:</strong> {userData.emailUtente}</p>
                    <p><strong>Nazionalità:</strong> {userData.nazionalitaUtente}</p>
                    <p><strong>Data di nascita:</strong> {userData.dataNascitaUtente}</p>
                    <p><strong>Genere:</strong> {userData.genderUtente}</p>
                    <p><strong>Titolo di Studio:</strong> {userData.titoloDiStudioUtente}</p>
                    <p><strong>Skill:</strong> {userData.skillUtente}</p>
                    {userData.ruoloUtente === "FiguraSpecializzata" && (
                        <p><strong>Disponibilità:</strong> {userData.disponibilitaUtente}</p>
                    )}
                    <p><strong>Ruolo:</strong> {userData.ruoloUtente}</p>
                    <p><strong>Lingue Parlate:</strong> {userData.lingueParlateUtente}</p>
                </div>
            ) : (
                <p>Caricamento dei dati personali...</p>
            )}
            <button onClick={eliminaAccount} className="delete-account-button">
                Elimina Account
            </button>
        </div>
    );
};

export default AreaPersonale;
