package excepcions;

public class RecursNoTrobatException extends JocException {

    public RecursNoTrobatException(String ruta) {
        super("No s'ha trobat el recurs: " + ruta, "No s'ha pogut carregar una imatge.");
    }
}
