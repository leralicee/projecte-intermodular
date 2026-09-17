package excepcions;

public class AccioNoPermesaException extends JocException {

    public AccioNoPermesaException(String motiuTecnic, String missatgeJugador) {
        super(motiuTecnic, missatgeJugador);
    }
}
