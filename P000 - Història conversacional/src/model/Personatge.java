package model;

import control.ResultatAccio;
import control.Verb;

import java.util.HashMap;

// personatge amb qui es pot parlar. sense inteligencia: nomes una taulade paraules clau -> resposta, com demana l'enunciat
public abstract class Personatge extends Element {

    protected Zona zonaActual;
    protected final HashMap<String, String> respostes = new HashMap<>();
    protected String respostaPerDefecte = "Et mira i no diu res.";

    protected Personatge(String nom, String descripcio, Zona zonaActual) {
        super(nom, descripcio);
        this.zonaActual = zonaActual;
    }

    public void afegirResposta(String clau, String resposta) {
        respostes.put(clau.toLowerCase(), resposta);
    }

    public void setRespostaPerDefecte(String r) {
        this.respostaPerDefecte = r;
    }

    // busca la clau dins del que ha dit el jugador
    public abstract String parlar(String clau);

    public Zona getZonaActual() {
        return zonaActual;
    }

    public void setZonaActual(Zona z) {
        this.zonaActual = z;
    }

    @Override
    public String descriure() {
        return descripcio;
    }

    @Override
    public ResultatAccio interactuar(Verb v, Jugador j) {
        if (v == Verb.PARLAR) {
            return ResultatAccio.ok(parlar(null));
        }
        if (v == Verb.MIRAR) {
            return ResultatAccio.okSenseTemps(descriure());
        }
        return ResultatAccio.error("Amb " + nom + " no pots fer aixo.");
    }
}
