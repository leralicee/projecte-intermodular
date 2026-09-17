package control;

// que ha passat en executar una ordre: el text de resposta (si ha anat be i si gasta temps)
public class ResultatAccio {

    private final String text;
    private final boolean correcta;
    private final boolean consumeixTemps;

    public ResultatAccio(String text, boolean correcta, boolean consumeixTemps) {
        this.text = text;
        this.correcta = correcta;
        this.consumeixTemps = consumeixTemps;
    }

    public static ResultatAccio ok(String text) {
        return new ResultatAccio(text, true, true);
    }

    // accions que no fan avancar el rellotge (mirar, inventari)
    public static ResultatAccio okSenseTemps(String text) {
        return new ResultatAccio(text, true, false);
    }

    public static ResultatAccio error(String text) {
        return new ResultatAccio(text, false, false);
    }

    public String getText() {
        return text;
    }

    public boolean esCorrecta() {
        return correcta;
    }

    public boolean consumeixTemps() {
        return consumeixTemps;
    }
}
