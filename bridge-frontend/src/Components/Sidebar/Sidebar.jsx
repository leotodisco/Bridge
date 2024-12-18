import './Sidebar.css'; // Import dello stile della sidebar
import logo from '../../assets/IMG_1581.PNG'; // Logo
import { Link } from 'react-router-dom';
import { FaHome, FaHeart, FaList, FaSignOutAlt, FaChevronDown, FaChevronUp } from "react-icons/fa";
import PropTypes from 'prop-types';
import { useState } from 'react';

// Sidebar per la navigazione
const Sidebar = ({ isOpen, toggleSidebar }) => {

    // Stato per gestire l'espansione del menu a tendina "Servizi"
    const [isServicesExpanded, setIsServicesExpanded] = useState(false); // Stato per il menu a tendina

    // Funzione per espandere il menu a tendina "Servizi"
    const toggleServices = () => {
        setIsServicesExpanded(!isServicesExpanded);
    };

    return (
        <>
            {/* Linguetta per aprire la Sidebar */}
            {!isOpen && (
                <button className="sidebar-toggle-btn" onClick={toggleSidebar}>
                    ☰
                </button>
            )}

            <div className={`sidebar ${isOpen ? 'open' : ''}`}>
                {/* Pulsante per chiudere la Sidebar */}
                <button className="close-btn" onClick={toggleSidebar}>×</button>

                {/* Sezione del logo */}
                <div className={`logo ${isServicesExpanded ? 'logo-shifted' : ''}`}>
                    <img src={logo} alt="Bridge Logo"/>
                </div>

                {/* Menu principale */}
                <ul className={`menu ${isServicesExpanded ? 'shifted-up' : ''}`}>
                    {/* Voce di menu: Home */}
                    <li>
                        <Link to="/">
                            <FaHome className="icon" /> <span>Home</span>
                        </Link>                    </li>

                    {/* Voce di menu: Preferiti */}
                    <li>
                        <Link to="/preferiti">
                            <FaHeart className="icon" /> <span>Preferiti</span>
                        </Link>
                    </li>

                    {/* Voce di menu: Servizi */}
                    <li onClick={toggleServices} className="menu-item">
                        <FaList className="icon" /> <span>Servizi</span>

                        {/* Icona per espandere o comprimere il menu a tendina */}
                        {isServicesExpanded ? <FaChevronUp className="expand-icon" /> : <FaChevronDown className="expand-icon" />}
                    </li>

                    {/* Sottomenu di Servizi (visibile solo se espanso) */}
                    {isServicesExpanded && (
                        <ul className="submenu">
                            <li>
                                <Link to="/view-eventi">Eventi</Link>
                            </li>
                            <li>
                                <Link to="/view-lavoro">Lavori</Link>
                            </li>
                            <li>
                                <Link to="/view-consulenza">Consulenze</Link>
                            </li>
                            <li>
                                <Link to="/view-listaCorsi">Corsi</Link>
                            </li>
                            <li>
                                <Link to="/mostraAlloggi">Alloggi</Link>
                            </li>
                        </ul>
                    )}

                    {/* Separatore */}
                    <hr/>

                    {/* Voce di menu: Log out */}
                    <li className={`log-out ${isServicesExpanded ? 'shifted-down' : ''}`}>
                        <Link to="/login">
                            <FaSignOutAlt className="icon" /> <span>Log out</span>
                        </Link>
                    </li>
                </ul>
            </div>
        </>
    );
};

// Validazione dei tipi delle props
Sidebar.propTypes = {
    isOpen: PropTypes.bool.isRequired, // Indica se la Sidebar è aperta o chiusa
    toggleSidebar: PropTypes.func.isRequired, // Funzione per alternare l'apertura/chiusura della Sidebar
};

export default Sidebar;
