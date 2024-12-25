import PropTypes from "prop-types";
import '../../GestioneEvento/css/card.css';

/*
 * @author Alessia De Filippo
 *
 * Componente Card da utilizzare per le pagine:
 * - Tutti gli Eventi
 * - Tutte le Consulenze
 * - Tutti i Corsi
 * - Tutti gli Alloggi
 * - Tutte i Lavori
 *
 * Fornisce 3 campi standard:
 * titolo, immagineProfilo e nome-cognome dell'utente.
 * Fornisce 3 campi personalizzabili:
 * parametro1, parametro2 e parametro3.
 * Questi possono essere utilizzati per fornire informazioni specifiche
 * per il componente che utilizza la Card.
 *
 * Il componente fornisce un pulsante per maggiori informazioni.
 */

// Componente Card
const Card = ({ data, labels, onClick, onInfoClick, children }) => {
    return (
        <div className="card" onClick={onClick}>
            {/* Header con il titolo */}
            <div className="card-header">
                <h2 className="card-title">{data.title}</h2>
            </div>

            {/* Body con le informazioni */}
            <div className="card-body">
                {/* Immagine e nome dell'utente */}
                {data.image && (
                    <div className="card-user-info">
                        <img src={data.image} alt={data.userName || "Image"} className="card-user-image" />
                        <span>{data.userName}</span>
                    </div>
                )}
                {/* Dettagli dell'evento */}
                <div className="card-details">
                    {/* Parametro1: */}
                    {data.parameter1 && (
                        <div className="card-info-row">
                            <strong>{labels.parameter1 || "Parametro 1"}:</strong> {data.parameter1}
                        </div>
                    )}
                    {/* Parametro2: */}
                    {data.parameter2 && (
                        <div className="card-info-row">
                            <strong>{labels.parameter2 || "Parametro 2"}:</strong> {data.parameter2}
                        </div>
                    )}
                    {/* Parametro3: */}
                    {data.parameter3 && (
                        <div className="card-info-row">
                            <strong>{labels.parameter3 || "Parametro 3"}:</strong> {data.parameter3}
                        </div>
                    )}
                </div>

                {/* Area per contenuti dinamici */}
                {children && <div className="card-extra-content">{children}</div>}

                {/* Footer con il pulsante per maggiori informazioni */}
                <div className="card-footer">
                    <button className="card-button" onClick={onInfoClick}>
                        Maggiori Info
                    </button>
                </div>
            </div>
        </div>
    );
};

// Campi del componente Card
Card.propTypes = {
    data: PropTypes.shape({
        // Titolo
        title: PropTypes.string.isRequired,
        // dell'immagine
        image: PropTypes.string,
        // Nome dell'utente
        userName: PropTypes.string,
        // Parametro 1 da specificare nel componente padre
        parameter1: PropTypes.string,
        // Parametro 2 da specificare nel componente padre
        parameter2: PropTypes.string,
        // Parametro 3 da specificare nel componente padre
        parameter3: PropTypes.string,
    }).isRequired,
    // Etichette per i parametri
    labels: PropTypes.shape({
        // Etichetta per il parametro 1
        parameter1: PropTypes.string,
        // Etichetta per il parametro 2
        parameter2: PropTypes.string,
        // Etichetta per il parametro 3
        parameter3: PropTypes.string,
    }),
    onClick: PropTypes.func,
    // Funzione da eseguire al click sul pulsante per maggiori informazioni
    onInfoClick: PropTypes.func.isRequired,
     //contenuti aggiuntivi per personalizzare le card se necessario
    children: PropTypes.node,
};

// Valori di default per le etichette
Card.defaultProps = {
    labels: {
        parameter1: "Parametro 1",
        parameter2: "Parametro 2",
        parameter3: "Parametro 3",
    },
};

export default Card;
