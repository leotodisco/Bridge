import { useEffect, useState } from "react";
import logo from '../../assets/IMG_1582.PNG'; // Logo
import "./Homepage.css";
import Footer from "../Footer/Footer.jsx"
import EventoRetrieveView from "../../GestioneEvento/components/EventoRetrieveView.jsx";
import {Link} from "react-router-dom";
import AboutUs from "../AboutUs/AboutUs.jsx";


const Homepage = () => {
    const [jobs, setJobs] = useState([]);
    const [events, setEvents] = useState([]);
    const [accommodations, setAccommodations] = useState([]);
    const [selectedEventId, setSelectedEventId] = useState(null);
    const [isPopupOpen, setIsPopupOpen] = useState(false);
    const [isAboutUsOpen, setIsAboutUsOpen] = useState(false); // Stato per gestire la visibilità del popup About Us

    useEffect(() => {
        // Fetch jobs
        fetch("/api/annunci/random")
            .then((response) => response.json())
            .then((data) => setJobs(data))
            .catch((error) => console.error("Errore fetching jobs:", error));

        // Fetch random events
        fetch("/api/eventi/random")
            .then((response) => response.json())
            .then((data) => setEvents(data))
            .catch((error) => console.error("Errore fetching events:", error));

        // Fetch accommodations
        fetch("/api/alloggi/random")
            .then((response) => response.json())
            .then((data) => setAccommodations(data))
            .catch((error) => console.error("Errore fetching accommodations:", error));
    }, []);

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

            <button className="chatbot-btn">
                <Link to="/chatBot">
                    <h3>Apri chatbot</h3>
                </Link>
            </button>

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
                        <div className="accommodations-grid">
                            {accommodations.map((accommodation, index) => (
                                <div className="accommodation-item" key={index}>
                                    <img src={accommodation.image} alt={`Alloggio ${index}`}/>
                                </div>
                            ))}
                        </div>
                    </section>
                </div>
            </div>
            {/* Popup per la visualizzazione dettagliata dell'evento */}
            {isPopupOpen && selectedEventId && (
                <EventoRetrieveView id={selectedEventId} onClose={handleClosePopup}/>
            )}

            {/* Popup About Us */}
            {isAboutUsOpen && <AboutUs onClose={toggleAboutUsPopup} />}

            <Footer />
        </div>
    );
};

export default Homepage;