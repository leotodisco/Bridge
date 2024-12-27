import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { useEffect, useState } from 'react';
import Sidebar from './Components/Sidebar/Sidebar.jsx';
import Homepage from './Components/Homepage/Homepage.jsx';
import Login from "./GestioneLogin/components/login.jsx";
import CreaEvento from "./GestioneEvento/components/formEvento.jsx";
import CreaLavoro from "./GestioneAnnuncio/components/formLavoro.jsx";
import CreaAlloggio from "./GestioneAlloggio/components/formAlloggio.jsx";
import CreaConsulenza from "./GestioneAnnuncio/components/formConsulenza.jsx";
import CreaUtente from "./Registrazione/components/formRegistrazione.jsx";
import CreaCorso from "./GestioneCorso/components/formCorso.jsx";
import ConsulenzaView from "./GestioneAnnuncio/components/viewConsulenza.jsx";
import AllEventsView from "./GestioneEvento/components/ViewAllEventi.jsx"; // Componente per tutti gli eventi
import ConsulenzaUtente from "./GestioneUtente/components/dashboardConsulenza.jsx";
import ViewLavoro from "./GestioneAnnuncio/components/ViewLavoro.jsx"; // Componente per tutti i lavori
import MostraAlloggi from "./GestioneAlloggio/components/MostraAlloggi.jsx";
import ListaCorsiView from "./GestioneCorso/components/listaCorsiView.jsx";
import AreaPersonale from "./GestioneUtente/components/AreaPersonale.jsx";
import AboutUs from "./Components/AboutUs/AboutUs.jsx";
import EventiUtente from "./GestioneUtente/components/dashboardEventi.jsx";
import Alloggio from "./GestioneAlloggio/components/Alloggio.jsx";
import Chatbot from "./GestioneChatbot/components/Chatbot.jsx";
import LavoriUtente from "./GestioneUtente/components/dashboardLavoro.jsx";
import AlloggiByProprietario from "./GestioneAlloggio/components/AlloggiByProprietario.jsx";
import "./GestioneChatbot/css/Chatbot.css"
import {FaRobot} from "react-icons/fa";

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    // Stato per il chatbot
    const [isChatbotOpen, setIsChatbotOpen] = useState(false);

    // Controlla se il token JWT è presente al caricamento
    useEffect(() => {
        const token = localStorage.getItem('authToken');
        setIsAuthenticated(!!token);
    }, []);

    // Gestione del login
    const handleLogin = () => {
        setIsAuthenticated(true);
    };

    const handleLogout = () => {
        setIsAuthenticated(false);
        localStorage.clear();
        sessionStorage.clear();
    };

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };

    // Funzione toggle
    const toggleChatbotPopup = () => {
        setIsChatbotOpen((prev) => !prev);
    };

    return (
        <Router>
            <Sidebar isOpen={isSidebarOpen} toggleSidebar={toggleSidebar} />
            <main className={`main-content ${isSidebarOpen ? 'shifted' : ''}`}>
                {isAuthenticated ? (
                    <>
                        <Routes>
                            <Route path="/" element={<Homepage />} />
                            <Route path="/eventi-utente" element={<EventiUtente />} />
                            <Route path="/consuleza-utente" element={<ConsulenzaUtente />} />
                            <Route path="/crea-lavoro" element={<CreaLavoro />} />
                            <Route path="/view-eventi" element={<AllEventsView />} />
                            <Route path="/view-consulenza" element={<ConsulenzaView />} />
                            <Route path="/view-lavoro" element={<ViewLavoro />} />
                            <Route path="/view-listaCorsi" element={<ListaCorsiView />} />
                            <Route path="/mostraAlloggio" element={<MostraAlloggi />} />
                            <Route path="/crea-evento" element={<CreaEvento />} />
                            <Route path="/crea-lavoro" element={<CreaLavoro />} />
                            <Route path="/crea-alloggio" element={<CreaAlloggio />} />
                            <Route path="/crea-consulenza" element={<CreaConsulenza />} />
                            <Route path="/crea-corso" element={<CreaCorso />} />
                            <Route path="/alloggi/SingoloAlloggio/:titolo" element={<Alloggio />} />
                            <Route path="/lavori-utente" element={<LavoriUtente />} />
                            <Route path="/chatBot" element={<Chatbot/>}/>
                            <Route path="/about-us" element={<AboutUs />} />
                            <Route path="/view-my-alloggi/:email" element={<AlloggiByProprietario />} />
                            <Route path="/area-personale" element={<AreaPersonale onLogout={handleLogout} />} />
                        </Routes>
                    </>
                ) : (
                    <>
                        <Routes>
                            <Route path="/" element={<Homepage />} />
                            <Route path="/view-eventi" element={<AllEventsView />} />
                            <Route path="/view-lavoro" element={<ViewLavoro />} />
                            <Route path="/alloggi/SingoloAlloggio/:titolo" element={<Alloggio />} />
                            <Route path="/view-listaCorsi" element={<ListaCorsiView />} />
                            <Route path="/mostraAlloggio" element={<MostraAlloggi />} />
                            <Route path="/login" element={<Login onLogin={handleLogin} />} />
                            <Route path="/crea-utente" element={<CreaUtente />} />
                            <Route path="/chatBot" element={<Chatbot/>}/>
                            <Route path="/about-us" element={<AboutUs />} />
                            <Route path="/view-my-alloggi/:email" element={<AlloggiByProprietario />} />
                        </Routes>
                    </>
                )}
            </main>

            {/* -------------- CHATBOT POPUP -------------- */}

            {/* Pulsante "flottante" in basso a destra per aprire il chatbot */}
            <button className="chatbot-fab" onClick={toggleChatbotPopup}>
                <FaRobot size={24} aria-hidden="true" />
            </button>

            {isChatbotOpen && (
                <div className="chatbot-modal">
                    {/* Bottone di chiusura in alto a destra nel box */}
                    <button className="close-chatbot-btn" onClick={toggleChatbotPopup}>
                        &times;
                    </button>
                    <Chatbot />
                </div>
            )}
        </Router>
    );
}

export default App;
