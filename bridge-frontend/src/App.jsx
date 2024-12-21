import './App.css';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { useState } from 'react';
import Sidebar from './Components/Sidebar/Sidebar.jsx';
import CreaEvento from './GestioneEvento/components/formEvento.jsx';
import CreaConsulenza from './GestioneAnnuncio/components/formConsulenza.jsx';
import CreaCorso from './GestioneCorso/components/formCorso.jsx';
import CreaUtente from "./Registrazione/components/formRegistrazione.jsx";
import Login from "./GestioneLogin/components/login.jsx";
import CreaAlloggio from "./GestioneAlloggio/components/formAlloggio.jsx";
import ListaCorsiView from "./GestioneCorso/components/listaCorsiView.jsx";
import CreaLavoro from './GestioneAnnuncio/components/formLavoro.jsx';
import logo from './assets/IMG_1580.PNG';
import ViewConsulenza from "./GestioneAnnuncio/components/viewConsulenza.jsx";
import ViewLavoro from "./GestioneAnnuncio/components/viewLavoro.jsx";
import LogoutButton from "./GestioneLogout/components/logout.jsx";
import AreaPersonale from "./GestioneUtente/components/AreaPersonale.jsx";
import AllEventsView from "./GestioneEvento/components/ViewAllEventi.jsx";
import EventView from "./GestioneEvento/components/EventoRetrieveView.jsx";
import MostraAlloggi from "./GestioneAlloggio/components/MostraAlloggi.jsx";
import DettaglioAlloggio from "./GestioneAlloggio/components/Alloggio.jsx";
import CorsoView from "./GestioneCorso/components/corsoView.jsx";

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    // Controlla se il token JWT è presente al caricamento
    //useEffect(() => {
    //    const token = localStorage.getItem('authToken');
    //    setIsAuthenticated(!!token);
    //}, []);

    // Definizione di handleLogin
    const handleLogin = () => {
        setIsAuthenticated(true); // Imposta lo stato su true
    };

    const handleLogout = () => {
        setIsAuthenticated(false); // Imposta lo stato su false
        localStorage.clear(); // Rimuove tutti i dati salvati
        sessionStorage.clear();
    };

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };

    return (
        <Router>
            <Sidebar isOpen={isSidebarOpen} toggleSidebar={toggleSidebar} />
            <main className={`main-content ${isSidebarOpen ? 'shifted' : ''}`}>
                <div className="logo-container">
                    <img src={logo} alt="Logo" className="site-logo"/>
                </div>
                {isAuthenticated ? (
                    <>
                        <nav className="nav-links">
                            <Link to="/" className="nav-link">Home</Link>
                            <Link to="/crea-evento" className="nav-link">Crea Evento</Link>
                            <Link to="/crea-consulenza" className="nav-link">Crea Consulenza</Link>
                            <Link to="/crea-lavoro" className="nav-link">Crea Annuncio di Lavoro</Link>
                            <Link to="/crea-corso" className="nav-link">Crea Corso</Link>
                            <Link to="/crea-alloggio" className="nav-link">Crea Alloggio</Link>
                            <Link to="/view-listaCorsi" className="nav-link">View Corsi</Link>
                            <Link to="/area-personale" className="nav-link">Area Personale</Link>
                            <Link to="/view-consulenza" className="nav-link">View Consulenze</Link>
                            <Link to="/view-lavoro" className="nav-link">Visualizza Annunci di Lavoro</Link>
                            <Link to="/view-eventi" className="nav-link">View Eventi</Link>
                            <Link to="/mostraAlloggi" className="nav-link">View Alloggi</Link>
                            <LogoutButton onLogout={handleLogout}/>
                        </nav>
                        <Routes>
                            <Route path="/" element={<p>Benvenuto nel sistema di gestione!</p>}/>
                            <Route path="/crea-evento" element={<CreaEvento/>}/>
                            <Route path="/crea-consulenza" element={<CreaConsulenza/>}/>
                            <Route path="/crea-lavoro" element={<CreaLavoro/>}/>
                            <Route path="/crea-corso" element={<CreaCorso/>}/>
                            <Route path="/crea-utente" element={<CreaUtente/>}/>
                            <Route path="/login" element={<Login onLogin={handleLogin} />} />
                            <Route path="/crea-alloggio" element={<CreaAlloggio/>}/>
                            <Route path="/view-listaCorsi" element={<ListaCorsiView/>}/>
                            <Route path="/view-consulenza" element={<ViewConsulenza/>}/>
                            <Route path="/view-lavoro" element={<ViewLavoro/>}/>
                            <Route path="/area-personale" element={<AreaPersonale onLogout={handleLogout}/>}/>
                            <Route path="/view-eventi" element={<AllEventsView />}/>
                            <Route path="/eventi/retrieve/:id" element={<EventView />} />
                            <Route path="/corso/view-corso/:id" element={<CorsoView/>}/>
                            <Route path="/mostraAlloggi" element={<MostraAlloggi/>}/>
                            <Route path="/alloggi/SingoloAlloggio/:titolo" element={<DettaglioAlloggio />} />
                        </Routes>
                    </>
                ) : (
                    <>
                        <nav className="nav-links">
                            <Link to="/login" className="nav-link">Login</Link>
                        </nav>
                        <Routes>
                            <Route path="/" element={<p>Benvenuto nel sistema di gestione!</p>}/>
                            <Route path="/login" element={<Login onLogin={handleLogin} />} />
                            <Route path="/crea-utente" element={<CreaUtente/>}/>
                        </Routes>
                    </>
                )}
            </main>
        </Router>
    );
}

export default App;
