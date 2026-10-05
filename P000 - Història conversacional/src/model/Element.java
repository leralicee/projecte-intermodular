package model;

import java.util.ArrayList;

import control.ResultatAccio;
import control.Verb;

// arrel de tot amb que es pot interactuar. cada filla decideix com respon a cada verb
public abstract class Element {

    protected String nom;
    protected String descripcio;
    protected String imatge;

    // altres maneres d'anomenar-lo: el jugador escriu "riu" i es refereix al torrent
    private final ArrayList<String> alies = new ArrayList<>();

    protected Element(String nom, String descripcio) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.imatge = nom.toLowerCase().replace(' ', '_') + ".png";
    }

    // que veu el jugador quan mira aquest element
    public abstract String descriure();

    // com reacciona aquest element a un verb concret
    public abstract ResultatAccio interactuar(Verb v, Jugador j);

    public void afegirAlies(String... noms) {
        for (String n : noms) {
            alies.add(n.toLowerCase());
        }
    }

    // es diu aixi, o el jugador li pot dir aixi?
    public boolean esDiu(String n) {
        if (n == null) {
            return false;
        }
        String b = n.toLowerCase();
        return nom.toLowerCase().equals(b) || alies.contains(b);
    }

    public String getNom() {
        return nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public String getImatge() {
        return imatge;
    }

    @Override
    public String toString() {
        return nom;
    }
}
