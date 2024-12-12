// src/components/ConsulenzeList.js
import { useState } from "react";
import {useEffect} from "react";
import '../css/ConsulenzeList.css'

const ConsulenzeList = () => {
    // Stato per memorizzare le consulenze
    const [consulenze, setConsulenze] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [proprietarioId, setProprietarioId] = useState("");
    // Funzione per ottenere tutte le consulenze
    const fetchConsulenze = async () => {
        try {
            // Usa fetch per recuperare i dati
            const response = await fetch('http://localhost:8080/api/annunci/view_consulenze', {
                method: 'POST', // metodo POST
                headers: {
                    'Content-Type': 'application/json', // Specifica il tipo di contenuto JSON
                },
            });

            if (!response.ok) {
                throw new Error('Errore nella risposta dalla API');
            }

            const data = await response.json(); // Converte la risposta in formato JSON
            setConsulenze(data);
            setLoading(false);
        } catch (err) {
            setError('Si è verificato un errore nel recupero delle consulenze' + err);
            setLoading(false);
        }
    };
    // Funzione per ottenere le consulenze di un proprietario specifico
    const fetchConsulenzeByProprietario = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/annunci/view_consulenze/proprietario/${id}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error('Errore nella risposta dalla API');
            }

            const data = await response.json();
            setConsulenze(data);
            setLoading(false);
        } catch (err) {
            setError('Si è verificato un errore nel recupero delle consulenze per il proprietario ' +err);
            setLoading(false);
        }
    };



    // Effetto per caricare le consulenze quando il componente è montato
    useEffect(() => {
        fetchConsulenze();
    }, []);

    // Funzione per gestire la ricerca per proprietario
    const handleSearch = () => {
        if (proprietarioId) {
            fetchConsulenzeByProprietario(proprietarioId);
        }
    };

    // Rendering
    if (loading) {
        return <div>Caricamento in corso...</div>;
    }

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div className="container">
            <h1>Lista delle Consulenze</h1>
            <div className="search-bar">
                <input
                    type="text"
                    value={proprietarioId}
                    onChange={(e) => setProprietarioId(e.target.value)}
                    placeholder="Inserisci ID proprietario"
                />
                <button onClick={handleSearch}>Cerca</button>
            </div>

            <div className="grid-container">
                {consulenze.map((consulenza) => (
                    <div className="card consulenza-card" key={consulenza.id}>
                        <div className="card-title">
                            {consulenza.titolo}
                        </div>
                        <table className="card-info">
                            <tbody>
                            <tr>
                                <td>Nome</td>
                                <td>{consulenza.numero}</td>
                            </tr>
                            <tr>
                                <td>Descrizione</td>
                                <td>{consulenza.descrizione}</td>
                            </tr>
                            <tr>
                                <td>Orari Disponibili</td>
                                <td>{consulenza.orariDisponibili}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                ))}
            </div>
        </div>
    );

};

export default ConsulenzeList;
