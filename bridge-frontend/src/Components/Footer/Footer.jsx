import './Footer.css';

const Footer = () => {
    return (
        <footer className="footer">
            <div className="footer-content">
                <p>&copy; 2024 Bridge - Tutti i diritti riservati</p>
                <div className="footer-links">
                    <a href="/privacy-policy">Privacy Policy</a>
                    <a href="/terms-of-service">Termini di servizio</a>
                </div>
            </div>
        </footer>
    );
};

export default Footer;
