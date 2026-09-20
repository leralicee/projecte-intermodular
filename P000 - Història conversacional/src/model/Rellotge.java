package model;

// de 16:00 a 18:00, a 2 minuts per ordre valida: 60 torns de marge
public class Rellotge {

    public static final int MINUTS_PER_ORDRE = 2;
    public static final int HORA_INICI = 16 * 60;   // 16:00
    public static final int HORA_LIMIT = 18 * 60;   // 18:00

    private int minutsTranscorreguts;

    public Rellotge() {
        reiniciar();
    }

    public void avancar() {
        minutsTranscorreguts += MINUTS_PER_ORDRE;
    }

    public int getMinutsTranscorreguts() {
        return minutsTranscorreguts;
    }

    // minuts que falten perque marxi el bus
    public int minutsRestants() {
        return Math.max(0, (HORA_LIMIT - HORA_INICI) - minutsTranscorreguts);
    }

    public String getHoraFormatada() {
        int total = HORA_INICI + minutsTranscorreguts;
        return String.format("%02d:%02d", total / 60, total % 60);
    }

    // els primers 40 minuts es de dia, els 40 seguents capvespre i la resta nit
    public FaseDelDia getFaseDelDia() {
        if (minutsTranscorreguts < 40) {
            return FaseDelDia.DIA;
        }
        if (minutsTranscorreguts < 80) {
            return FaseDelDia.CAPVESPRE;
        }
        return FaseDelDia.NIT;
    }

    public boolean sHaAcabatElTemps() {
        return HORA_INICI + minutsTranscorreguts >= HORA_LIMIT;
    }

    public void reiniciar() {
        minutsTranscorreguts = 0;
    }
}
