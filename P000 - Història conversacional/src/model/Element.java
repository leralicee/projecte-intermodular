package model;

import control.ResultatAccio;
import control.Verb;

// arrel de tot amb que es pot interactuar. cada filla decideix com respon a cada verb
public abstract class Element {

    protected String nom;
    protected String descripcio;
    protected String imatge;

    protected Element(String nom, String descripcio) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.imatge = nom.toLowerCase().replace(' ', '_') + ".png";
    }

    // que veu el jugador quan mira aquest element
    public abstract String descriure();

    // com reacciona aquest element a un verb concret
    public abstract ResultatAccio interactuar(Verb v, Jugador j);

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
