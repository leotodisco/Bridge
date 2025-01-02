import { useEffect, useState } from "react";
import logo from '../../assets/IMG_1582.PNG'; // Logo
import "./Homepage.css";
import Footer from "../Footer/Footer.jsx"
import EventoRetrieveView from "../../GestioneEvento/components/EventoRetrieveView.jsx";
import {Link, useNavigate} from "react-router-dom";
import AboutUs from "../AboutUs/AboutUs.jsx";


const Homepage = () => {
    const [jobs, setJobs] = useState([]);
    const [events, setEvents] = useState([]);
    const [accommodations, setAccommodations] = useState([]);
    const [selectedEventId, setSelectedEventId] = useState(null);
    const [isPopupOpen, setIsPopupOpen] = useState(false);
    const [isAboutUsOpen, setIsAboutUsOpen] = useState(false); // Stato per gestire la visibilità del popup About Us
    const nav = useNavigate();


    useEffect(() => {

        // Recupero del token da localStorage
        const token = localStorage.getItem("authToken");

        // check su token:
        if (!token) {
            alert("Non sei autenticato. Effettua il login.");
            nav('/login');
            return;
        }

        // Fetch jobs
        fetch("/api/annunci/random", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`, // <--- ecco l'header
                "Content-Type": "application/json",
            },
        })
            .then((response) => response.json())
            .then((data) => setJobs(data))
            .catch((error) => console.error("Errore fetching jobs:", error));

        // Fetch random events
        fetch("/api/eventi/random", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        })
            .then((response) => response.json())
            .then((data) => setEvents(data))
            .catch((error) => console.error("Errore fetching events:", error));

        // Fetch accommodations
        fetch("http://localhost:8080/alloggi/random", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                return response.text(); // Leggi come testo per debug
            })
            .then((text) => {
                try {
                    const data = JSON.parse(text); // Prova a parsare come JSON
                    setAccommodations(data);
                } catch (error) {
                    console.error("Errore nel parsing JSON:", error, text);
                }
            })
            .catch((error) => console.error("Errore fetching accommodations:", error));
    },[]);


    // Gestisce l'apertura del popup con l'ID dell'evento selezionato
    const handleOpenPopup = (id) => {
        setSelectedEventId(id);
        setIsPopupOpen(true);
    };

    // Gestisce la chiusura del popup
    const handleClosePopup = () => {
        setSelectedEventId(null);
        setIsPopupOpen(false);
    };

    // Funzione per aprire e chiudere il popup
    const toggleAboutUsPopup = () => {
        setIsAboutUsOpen((prev) => !prev);
    };

    return (
        <div className="homepage">
            <section className="welcome-banner">
                <img src={logo} alt="Logo Bridge" className="welcome-logo"/>
                <h2>Al tuo fianco per un futuro migliore. Scopri i servizi pensati per te.</h2>
                <button className="explore-btn" onClick={toggleAboutUsPopup}>
                    Scopri di più
                </button>
            </section>

            <div className="main-content">
                {/* Colonna sinistra */}
                <div className="left-column">
                    <section className="section">
                        <Link to="/view-eventi" className="section-title">
                            <h3>Prossimi Eventi</h3>
                        </Link>
                        <div className="scrollable-list">
                            {events.map((event, index) => (
                                <div className="list-item" key={index}>
                                    <p>{event.nome}</p>
                                    <p>{event.data}</p>
                                    <p>{event.luogo?.citta || "Non specificato"}</p>
                                    <div
                                        className="list-options"
                                        onClick={() => handleOpenPopup(event.id)}
                                    >
                                        •••
                                    </div>
                                </div>
                            ))}
                        </div>
                    </section>

                    <section className="section">
                        <Link to="/view-lavoro" className="section-title">
                            <h3>Lavori per te</h3>
                        </Link>
                        <div className="scrollable-list">
                            {jobs.map((job, index) => (
                                <div className="list-item" key={index}>
                                    <p>{job.nome}</p>
                                    <p>{job.posizioneLavorativa}</p>
                                    <p>{job.tipoContratto}</p>
                                    <div className="list-options">•••</div>
                                </div>
                            ))}
                        </div>
                    </section>
                </div>

                {/* Colonna destra */}
                <div className="right-column">
                    <section className="section">
                        <Link to="/mostraAlloggio" className="section-title">
                            <h3>Alloggi</h3>
                        </Link>
                        <div className="scrollable-list">
                            {accommodations.length > 0 ? (
                                accommodations.map((accommodation, index) => {
                                    const imageBase64 = accommodation.foto && accommodation.foto[0]
                                        ? (accommodation.foto[0].startsWith("http")
                                            ? accommodation.foto[0]
                                            : `data:image/jpeg;base64,${accommodation.foto[0]}`)
                                        : "https://via.placeholder.com/150";

                                    return (
                                        <div className="list-item" key={index}>
                                            <img
                                                src={imageBase64}
                                                alt={`Immagine di ${accommodation.descrizione || "Alloggio"}`}
                                                style={{
                                                    width: "100px",
                                                    height: "100px",
                                                    objectFit: "cover",
                                                    borderRadius: "8px",
                                                    marginBottom: "8px",
                                                }}
                                            />
                                            <p>{accommodation.descrizione || "Nessuna descrizione"}</p>
                                        </div>
                                    );
                                })
                            ) : (
                                <p>Nessun alloggio disponibile al momento.</p>
                            )}

                        </div>
                    </section>
                </div>
            </div>

            {/* Popup EVENTO dettagliato */}
            {isPopupOpen && selectedEventId && (
                <EventoRetrieveView id={selectedEventId} onClose={handleClosePopup}/>
            )}

            {/* Popup ABOUT US */}
            {isAboutUsOpen && <AboutUs onClose={toggleAboutUsPopup}/>}
            <Footer/>
        </div>
    );
};

export default Homepage;