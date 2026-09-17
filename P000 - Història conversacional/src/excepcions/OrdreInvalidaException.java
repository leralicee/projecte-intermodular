package excepcions;

public class OrdreInvalidaException extends JocException {

    public OrdreInvalidaException(String text) {
        super("Ordre no reconeguda: " + text,
              "No entenc que vols dir amb \"" + text + "\". Escriu AJUDA per veure els verbs.");
    }
}
