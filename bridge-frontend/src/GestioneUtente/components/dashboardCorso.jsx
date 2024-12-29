import { useState, useEffect } from 'react';
import Card from "../../GestioneEvento/components/Card.jsx";
import CorsoView from "../../GestioneCorso/components/corsoView.jsx";
import {toast} from "react-toastify";
import FormCorso from "../../GestioneCorso/components/formCorso.jsx";

const CorsiUtente = () =>{
    const [corsi, setCorsi] = useState([]);
    const [error, setError] = useState(null);
    const [selectedCorsoId, setSelectedCorsoId] = useState(null);
    const [isPopupVisible, setPopupVisible] = useState(false); // State for showing the create modal

    // Recupera l'email dell'utente loggato (esempio: salvata in localStorage)
    const token = localStorage.getItem('authToken'); // Sostituisci con il tuo token
    const email = localStorage.getItem('email'); // Assicurati che l'email sia salvata.
    useEffect(() => {

        if (!token) {
            toast.error("Token non trovato. Effettua nuovamente il login.");
            return;
        }
            fetch(`http://localhost:8080/api/corsi/listaCorsiUtente/${email}`, {
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
                .then((data) => setCorsi(data))
                .catch((error) => {
                    setError('Errore durante il recupero dei corsi');
                    console.error(error);
                });
    }, [email]);

    const closePopupForm = () => {
        setPopupVisible(false);
    };

    // Funzione per chiudere il popup
    const closePopup = () => {
        setSelectedCorsoId(null); // Reset dell'evento selezionato, chiudendo così il popup
    };
    return (
        <div>
            <div className="dashboardContainer">
                <h1>I miei Corsi</h1>
                <hr/>
                <button onClick={() => setPopupVisible(true)} className="openPopupButton">Crea un Corso</button>

                {error && <p>{error}</p>}
                {corsi.length > 0 ? (
                    <div className="cards-container">
                        {corsi.map((corso) => (
                            <Card
                                key={corso.id}
                                data={{
                                    title: corso.titolo,
                                    image: corso.proprietario.fotoUtente
                                        ? corso.proprietario.fotoUtente
                                        : "https://via.placeholder.com/150",
                                    userName: `${corso.proprietario.nome} ${corso.proprietario.cognome}`,
                                    parameter1: corso.lingua,
                                    parameter2: corso.categoriaCorso,
                                    parameter3: corso.proprietario.nome,
                                }}
                                labels={{
                                    parameter1: "Lingua",
                                    parameter2: "Categoria",
                                    parameter3: "Proprietario",
                                }}
                                onClick={() => console.log(`Cliccato su corso: ${corso.titolo}`)}
                                onInfoClick={() => setSelectedCorsoId(corso.id)}
                            />
                        ))}
                    </div>
                ) : (
                    <p>Non hai corsi pubblicati</p>
                )}
            </div>


            {/* Mostra il popup se selectedEventId è impostato */}
            {selectedCorsoId && (
                <CorsoView id={selectedCorsoId} onClose={closePopup}/>
            )}
            {isPopupVisible && (
                <div className="popupOverlay" onClick={(e) => e.target === e.currentTarget && closePopupForm()}>
                    <div className="popupContainer">
                        <button onClick={closePopupForm} className="closePopupButton">X</button>
                        <FormCorso onSubmitSuccess={() => {
                            fetch(`http://localhost:8080/api/corsi/listaCorsiUtente/${email}`, {
                                method: 'GET',
                                headers: {
                                    'Authorization': `Bearer ${token}`,
                                    'Content-Type': 'application/json',
                                },
                            })
                                .then((response) => response.json())
                                .then((data) => setCorsi(data)) // Aggiorna l'intera lista
                                .catch((error) => console.error('Errore durante l\'aggiornamento:', error));
                            closePopupForm(); // Chiudi il popup dopo l'aggiunta
                        }} />
                    </div>
                </div>
            )}
        </div>

    );
};

export default CorsiUtente;