package excepcions;

public class ObjecteNoTrobatException extends JocException {

    public ObjecteNoTrobatException(String nom) {
        this(nom, null);
    }

    // amb suggeriment quan el que ha escrit s'assembla a alguna cosa que si que hi ha
    public ObjecteNoTrobatException(String nom, String sembla) {
        super("Element inexistent: " + nom,
              "Aqui no veig cap " + nom + "."
              + (sembla != null ? " Volies dir " + sembla + "?" : ""));
    }
}
