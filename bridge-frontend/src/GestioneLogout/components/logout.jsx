import { useNavigate } from 'react-router-dom';
import PropTypes from 'prop-types';
import "../css/logoutStyle.css";
import '@fortawesome/fontawesome-free/css/all.min.css';

function LogoutButton({ onLogout }) {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            const token = localStorage.getItem('authToken');

            if (!token) {
                alert("Non sei autenticato. Effettua il login.");
                navigate('/login');
                return;
            }

            const response = await
                fetch('http://localhost:8080/authentication/logout', {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            // Rimuovi il token dal client
            localStorage.removeItem('authToken');
            sessionStorage.removeItem('authToken');

            // Aggiorna lo stato di autenticazione nel componente genitore
            if (onLogout) {
                onLogout();
            }

            // Reindirizza alla pagina di login
            navigate('/login');

        } catch (error) {
            console.error('Errore durante il logout:', error);
        }
    };

    return (

        <button className="logoutButton" onClick={handleLogout}>
            <i className="fas fa-sign-out-alt"></i>
        </button>

    );
}

// Validazione delle props
LogoutButton.propTypes = {
    onLogout: PropTypes.func.isRequired,
};

export default LogoutButton;