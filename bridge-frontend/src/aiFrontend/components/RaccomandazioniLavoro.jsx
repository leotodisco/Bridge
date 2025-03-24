import { useEffect, useState } from "react";
import { getJobRecommendations } from "../api";
import { useNavigate } from "react-router-dom";
import Card from "../../GestioneEvento/components/Card.jsx";
import "../../GestioneEvento/css/card.css";

const RaccomandazioniLavoro = () => {
    const [recommendations, setRecommendations] = useState([]);
    const [bestHousing, setBestHousing] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [userImages, setUserImages] = useState({});
    const navigate = useNavigate();

    const fetchRecommendations = async () => {
        const email = localStorage.getItem("email");
        const token = localStorage.getItem("authToken");

        if (!email || !token) {
            setError("Non sei autenticato. Effettua il login.");
            setLoading(false);
            return;
        }

        try {
            const response = await getJobRecommendations(email);

            const enrichedJobs = [];
            const imageMap = {};

            for (const job of response.recommendations) {
                const jobRes = await fetch(`http://localhost:8080/api/annunci/view_lavori/retrieve/${job.id}`, {
                    method: "GET",
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    }
                });

                if (!jobRes.ok) {
                    console.warn(`❌ Job ${job.id} non trovato. Status: ${jobRes.status}`);
                    continue;
                }

                const fullJob = await jobRes.json();
                console.log(`✅ Job ${job.id} recuperato:`, fullJob);
                enrichedJobs.push({ ...fullJob, similarita: job.similarita });

                const ownerEmail = fullJob.proprietario?.email;
                if (ownerEmail && !imageMap[ownerEmail]) {
                    const imgRes = await fetch(`http://localhost:8080/areaPersonale/DatiFotoUtente/${ownerEmail}`, {
                        method: "GET",
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json',
                        }
                    });

                    if (imgRes.ok) {
                        const base64 = await imgRes.text();
                        imageMap[ownerEmail] = `data:image/jpeg;base64,${base64}`;
                    }
                }
            }

            setRecommendations(enrichedJobs);
            setUserImages(imageMap);

            // Ottieni ID alloggio dal best_housing
            const bestJobId = response.recommendations[0]?.id;
            const bestHousingId = response.best_housing?.[bestJobId];
            if (bestHousingId) {
                const housingRes = await fetch(`http://localhost:8080/alloggi/${bestHousingId}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (housingRes.ok) {
                    const housingData = await housingRes.json();
                    setBestHousing(housingData);
                }

                else {
                    console.warn(`❌ Alloggio ${bestHousingId} non trovato. Status: ${housingRes.status}`);
                }
            }

            console.log("✅ JSON ricevuto da Python:", response);
            console.log("✅ ID lavori:", response.recommendations.map(r => r.id));
            console.log("🏠 Mappa alloggi:", response.best_housing);
            console.log("👉 ID alloggio da aprire:", bestHousingId);

        } catch (err) {
            console.error("Errore nel recupero dei dati:", err);
            setError("Errore durante il recupero delle raccomandazioni.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchRecommendations();
    }, []);

    return (
        <div className="popupContainer">
            <h2>Raccomandazioni Personalizzate</h2>

            {loading && <p>⏳ Caricamento in corso...</p>}
            {error && <p style={{ color: "red" }}>{error}</p>}

            {!loading && (
                <>
                    {recommendations.length > 0 && (
                        <>
                            <h3>💼 Lavori Consigliati</h3>
                            <div className="cards-container">
                                {recommendations.map((job) => (
                                    <Card
                                        key={job.id}
                                        data={{
                                            title: job.titolo,
                                            image: userImages[job.proprietario?.email] || "https://via.placeholder.com/150/cccccc/000000?text=Lavoro",
                                            userName: `${job.proprietario?.nome || "N/A"} ${job.proprietario?.cognome || ""}`,
                                            parameter1: job.posizioneLavorativa,
                                            parameter2: job.retribuzione ? `€ ${job.retribuzione}` : "Non specificata",
                                            parameter3: job.nomeAzienda || "Azienda sconosciuta",
                                        }}
                                        labels={{
                                            parameter1: "Posizione",
                                            parameter2: "Retribuzione",
                                            parameter3: "Azienda",
                                        }}
                                        onInfoClick={() => navigate(`/view-lavoro?id=${job.id}`)}
                                    />
                                ))}
                            </div>
                        </>
                    )}

                    {bestHousing && (
                        <>
                            <h3>🏠 Alloggio Consigliato</h3>
                            <div className="cards-container">
                                <Card
                                    key={bestHousing.id}
                                    data={{
                                        title: bestHousing.titolo,
                                        image: "https://via.placeholder.com/150/cccccc/000000?text=Alloggio",
                                        userName: bestHousing.proprietario?.nome || "Proprietario",
                                        parameter1: `${bestHousing.maxPersone} persone`,
                                        parameter2: `${bestHousing.metratura} mq`,
                                        parameter3: bestHousing.provincia,
                                    }}
                                    labels={{
                                        parameter1: "Max Persone",
                                        parameter2: "Metratura",
                                        parameter3: "Provincia",
                                    }}
                                    onClick={() => navigate(`/alloggi/SingoloAlloggio/${bestHousing.titolo}`)}
                                />
                            </div>
                        </>
                    )}

                    {recommendations.length === 0 && !bestHousing && (
                        <p>⚠️ Nessuna raccomandazione trovata al momento.</p>
                    )}
                </>
            )}
        </div>
    );
};

export default RaccomandazioniLavoro;
