package excepcions;

// pare de totes les excepcions del joc. guarda dos missatges: el tecnic per depurar i el que es mostra al jugador
public abstract class JocException extends Exception {

    private final String missatgeJugador;

    public JocException(String missatgeTecnic, String missatgeJugador) {
        super(missatgeTecnic);
        this.missatgeJugador = missatgeJugador;
    }

    public String getMissatgeJugador() {
        return missatgeJugador;
    }
}
