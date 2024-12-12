import React from 'react';
import { useNavigate } from 'react-router-dom';

const AreaPersonale = () => {
    const nav = useNavigate();

    const eliminaAccount = async () => {
        if (window.confirm("Sei sicuro di voler eliminare il tuo account?")) {
            try {
                const email = localStorage.getItem('email'); // Recupera l'email dell'utente dal localStorage
                const token = localStorage.getItem('token'); // Token JWT
                console.log(token);
                const response = await fetch(`http://localhost:8080/areaPersonale/${email}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`, // Autenticazione JWT
                        'Content-Type': 'application/json',
                    },
                });

                if (!response.ok) {
                    throw new Error(`Errore HTTP: ${response.status}`);
                }

                alert("Account eliminato con successo.");
                localStorage.clear(); // Rimuove i dati dell'utente dal localStorage
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
            <p>Benvenuto nella tua area personale!</p>
            <button onClick={eliminaAccount} className="delete-account-button">
                Elimina Account
            </button>
        </div>
    );
};

export default AreaPersonale;
