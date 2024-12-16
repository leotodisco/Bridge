import { useEffect, useState } from "react";
import Card from "../../GestioneEvento/components/Card.jsx";
import "../../GestioneEvento/css/card.css";
import CorsoView from "./corsoView.jsx";


const ListaCorsiView = () => {
    // Stato per memorizzare i dati del corso
    const [corsi, setCorsi] = useState([]);
    // Stato per gestire gli errori
    const [error, setError] = useState(false);
    // Stato per gestire il caricamento
    const [loading, setLoading] = useState(true);
    // Stato per memorizzare l'id del corso selezionato
    const [selectedCorsoId, setSelectedCorsoId] = useState(null); // Stato per il popup

    // Funzione per recuperare i dati del corso dall'API
    const fetchCorsi = async () => {
        try {
            const response = await fetch("http://localhost:8080/api/corsi/listaCorsi");
            if (response.ok) {
                const data = await response.json();
                setCorsi(data); // Imposta i dati recuperati nello stato
            } else {
                setError(true); // Imposta lo stato di errore se la risposta non è ok
            }
        } catch (error) {
            console.error("Errore nel recupero dei corsi:", error);
            setError(true); // Imposta lo stato di errore in caso di eccezione
        }finally {
            setLoading(false);
        }
    };

    const closePopup = () => {
        setSelectedCorsoId(null); // Chiudi il popup
    }

    // Hook useEffect per recuperare i dati al montaggio del componente
    useEffect(() => {
        fetchCorsi();
    }, []);

    if(loading) {
        return <p>Caricamento in corso...</p>;
    }

    if (error) {
        return <p>Errore: {error}</p>;
    }

    return (
        <div>
            <h1>Tutti i Corsi</h1>
            {corsi.length > 0 ? (
                    <div className={"cards-container"}>
                        {corsi.map((corso) => (
                            <Card
                                key={corso.id}
                                data={{
                                    title: corso.titolo,
                                    image: corso.proprietario.fotoUtente
                                                ? corso.proprietario.fotoUtente
                                                : "https://via.placeholder.com/150/cccccc/000000?text=No+Image",
                                    userName: `${corso.proprietario.nome} ${corso.proprietario.cognome}`,
                                    parameter1: corso.lingua,
                                    parameter2: corso.categoriaCorso,
                                    parameter3: corso.proprietario ? corso.proprietario.nome : "Non specificato",
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
                    <p>Nessun corso disponibile.</p>
                )}
            {/* Mostra il popup se selectedCorsoId è impostato */}
            {selectedCorsoId && (
                <CorsoView id={selectedCorsoId} onClose={closePopup} />
            )}
        </div>
    );
};

export default ListaCorsiView;