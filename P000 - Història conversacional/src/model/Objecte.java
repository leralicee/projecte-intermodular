package model;

import control.ResultatAccio;
import control.Verb;

import java.util.HashMap;

// objecte agafable. usosValids diu amb que serveix i que passa: "cofre" -> "Fas saltar el pany del cofre amb la navalla."
public class Objecte extends Element implements Usable {

    private boolean agafable;
    private final HashMap<String, String> usosValids = new HashMap<>();

    public Objecte(String nom, String descripcio) {
        this(nom, descripcio, true);
    }

    public Objecte(String nom, String descripcio, boolean agafable) {
        super(nom, descripcio);
        this.agafable = agafable;
    }

    // registra que aquest objecte serveix amb un altre element
    public void afegirUs(String nomElement, String resultat) {
        usosValids.put(nomElement.toLowerCase(), resultat);
    }

    @Override
    public String descriure() {
        return descripcio;
    }

    @Override
    public ResultatAccio interactuar(Verb v, Jugador j) {
        if (v == Verb.MIRAR) {
            return ResultatAccio.okSenseTemps(descriure());
        }
        return ResultatAccio.error("Amb " + nom + " no pots fer aixo.");
    }

    @Override
    public ResultatAccio usarAmb(Element e) {
        if (e == null) {
            return ResultatAccio.error("Has de dir amb que vols fer servir " + nom + ".");
        }
        String efecte = usosValids.get(e.getNom().toLowerCase());
        if (efecte == null) {
            return ResultatAccio.error("Fer servir " + nom + " amb " + e.getNom() + " no serveix de res.");
        }
        return ResultatAccio.ok(efecte);
    }

    public boolean esAgafable() {
        return agafable;
    }

    public void setAgafable(boolean agafable) {
        this.agafable = agafable;
    }
}
