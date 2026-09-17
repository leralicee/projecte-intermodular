package excepcions;

public class ObjecteNoTrobatException extends JocException {

    public ObjecteNoTrobatException(String nom) {
        super("Element inexistent: " + nom, "Aqui no veig cap " + nom + ".");
    }
}
