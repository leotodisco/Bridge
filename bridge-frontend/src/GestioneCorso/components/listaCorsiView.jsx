import { useEffect, useState } from "react";
import Card from "../../GestioneEvento/components/Card.jsx";
import CorsoView from "./corsoView.jsx";
import CreaCorso from "./formCorso.jsx";
import "../../GestioneEvento/css/card.css";
import "../../GestioneCorso/css/listaCorsiStyle.css";
import {FaPlus} from "react-icons/fa";
import {useNavigate} from "react-router-dom";

const ListaCorsiView = () => {
    const [corsi, setCorsi] = useState([]);
    const [error, setError] = useState(false);
    const [loading, setLoading] = useState(true);
    const [selectedCorsoId, setSelectedCorsoId] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const ruolo = localStorage.getItem("ruolo");
    const nav = useNavigate();

    const fetchCorsi = async () => {
        try {
            const token = localStorage.getItem('authToken');

            if (!token) {
                alert("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await
                fetch("http://localhost:8080/api/corsi/listaCorsi", {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                });

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

    useEffect(() => {
        fetchCorsi();
        const closeOnEscapeKeyDown = (e) => {
            if ((e.charCode || e.keyCode) === 27) {
                setShowModal(false);
            }
        };
        document.body.addEventListener('keydown', closeOnEscapeKeyDown);
        return () => {
            document.body.removeEventListener('keydown', closeOnEscapeKeyDown);
        };
    }, []);

    const toggleModal = () => setShowModal(!showModal);

    if (loading) {
        return <p>Caricamento in corso...</p>;
    }

    if (error) {
        return <p>Errore nel recupero dei corsi.</p>;
    }
    console.log("RUOLO", ruolo);

    return (
        <div>
            <div className="header-container">
                <h1>Tutti i Corsi</h1>
                {ruolo === "FiguraSpecializzata" && (
                    <button
                        onClick={toggleModal}
                        className="btn btn-primary"
                        aria-label="Aggiungi Nuovo Corso"
                    >
                        <FaPlus size={20} />
                    </button>
                )}
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
                <p>Nessun corso disponibile.</p>
            )}
            {selectedCorsoId && <CorsoView id={selectedCorsoId} onClose={() => setSelectedCorsoId(null)} />}
            {showModal && (
                <div className="modal">
                    <div >
                        <CreaCorso onClose={toggleModal} />
                    </div>
                </div>
            )}
        </div>
    );
};

export default ListaCorsiView;
