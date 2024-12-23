import './App.css';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import {useEffect, useState} from 'react';
import Sidebar from './Components/Sidebar/Sidebar.jsx';
import Homepage from './Components/Homepage/Homepage.jsx';
import Login from "./GestioneLogin/components/login.jsx";
import CreaEvento from "./GestioneEvento/components/formEvento.jsx";
import CreaLavoro from "./GestioneAnnuncio/components/formLavoro.jsx";
import CreaAlloggio from "./GestioneAlloggio/components/formAlloggio.jsx";
import CreaConsulenza from "./GestioneAnnuncio/components/formConsulenza.jsx";
import CreaUtente from "./Registrazione/components/formRegistrazione.jsx";
import AllEventsView from "./GestioneEvento/components/ViewAllEventi.jsx"; // Componente per tutti gli eventi
import ViewLavoro from "./GestioneAnnuncio/components/ViewLavoro.jsx"; // Componente per tutti i lavori
import MostraAlloggi from "./GestioneAlloggio/components/MostraAlloggi.jsx";
import AreaPersonale from "./GestioneUtente/components/AreaPersonale.jsx";
import AboutUs from "./Components/AboutUs/AboutUs.jsx";

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

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

    return (
        <Router>
            <Sidebar isOpen={isSidebarOpen} toggleSidebar={toggleSidebar} />
            <main className={`main-content ${isSidebarOpen ? 'shifted' : ''}`}>
                {isAuthenticated ? (
                    <>

                        <Routes>
                            <Route path="/" element={<Homepage />} />
                            <Route path="/crea-lavoro" element={<CreaLavoro/>}/>
                            <Route path="/view-eventi" element={<AllEventsView />} />
                            <Route path="/view-lavoro" element={<ViewLavoro />} />
                            <Route path="/mostraAlloggio" element={<MostraAlloggi />} />
                            <Route path="/crea-evento" element={<CreaEvento />} />
                            <Route path="/crea-lavoro" element={<CreaLavoro />} />
                            <Route path="/crea-alloggio" element={<CreaAlloggio />} />
                            <Route path="/crea-consulenza" element={<CreaConsulenza />} />
                            <Route path="/about-us" element={<AboutUs />} />
                            <Route
                                path="/area-personale"
                                element={<AreaPersonale onLogout={handleLogout} />}
                            />
                        </Routes>
                    </>
                ) : (
                    <>
                        <Routes>
                            <Route path="/" element={<Homepage />} />
                            <Route path="/view-eventi" element={<AllEventsView />} />
                            <Route path="/view-lavoro" element={<ViewLavoro />} />
                            <Route path="/mostraAlloggio" element={<MostraAlloggi />} />
                            <Route path="/login" element={<Login onLogin={handleLogin} />} />
                            <Route path="/crea-utente" element={<CreaUtente />} />
                            <Route path="/about-us" element={<AboutUs />} />
                        </Routes>
                    </>
                )}
            </main>
        </Router>
    );
}

export default App;
