import { useEffect, useState } from "react";
import Card from "../../GestioneEvento/components/Card.jsx";
import "../../GestioneEvento/css/card.css";
import "../../GestioneCorso/css/listaCorsiStyle.css";
import CorsoView from "./corsoView.jsx";
import { Link } from 'react-router-dom';  // Importa il componente Link

const ListaCorsiView = () => {
    const [corsi, setCorsi] = useState([]);
    const [error, setError] = useState(false);
    const [loading, setLoading] = useState(true);
    const [selectedCorsoId, setSelectedCorsoId] = useState(null);

    const fetchCorsi = async () => {
        try {
            const response = await fetch("http://localhost:8080/api/corsi/listaCorsi");
            if (response.ok) {
                const data = await response.json();
                setCorsi(data);
            } else {
                setError(true);
            }
        } catch (error) {
            console.error("Errore nel recupero dei corsi:", error);
            setError(true);
        } finally {
            setLoading(false);
        }
    };

    const closePopup = () => {
        setSelectedCorsoId(null);
    }

    useEffect(() => {
        fetchCorsi();
    }, []);

    if (loading) {
        return <p>Caricamento in corso...</p>;
    }

    if (error) {
        return <p>Errore: {error}</p>;
    }

    return (
        <div>
            <div className="header-container">  {/* Aggiunto container per titolo e bottone */}
                <h1>Tutti i Corsi</h1>
                <Link to="/crea-corso" className="btn btn-primary">Aggiungi Nuovo Corso</Link>
            </div>
            {corsi.length > 0 ? (
                <div className="cards-container">
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
            {selectedCorsoId && (
                <CorsoView id={selectedCorsoId} onClose={closePopup} />
            )}
        </div>
    );
};

export default ListaCorsiView;
