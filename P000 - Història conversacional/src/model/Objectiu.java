package model;

// una fita del joc: que ha de fer el jugador i en quin punt la te. no guarda estat propi,
// la construeix el joc a partir del mon cada vegada que algu la demana
public class Objectiu {

    private final String text;
    private final boolean descobert;
    private final boolean complert;

    public Objectiu(String text, boolean descobert, boolean complert) {
        this.text = text;
        this.descobert = descobert;
        this.complert = complert;
    }

    public String getText() {
        return text;
    }

    // el jugador ja ha topat amb l'obstacle, aixi que te sentit ensenyar-li la fita
    public boolean esDescobert() {
        return descobert;
    }

    public boolean esComplert() {
        return complert;
    }

    // descoberta i encara per fer
    public boolean esPendent() {
        return descobert && !complert;
    }

    // [x] feta, [>] la que toca ara, [ ] descoberta pero encara lluny
    public String marca(boolean actual) {
        if (complert) {
            return "[x]";
        }
        return actual ? "[>]" : "[ ]";
    }

    @Override
    public String toString() {
        return text;
    }
}
