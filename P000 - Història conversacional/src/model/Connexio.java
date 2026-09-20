package model;

// una sortida cap a una altra zona. pot estar tancada, ser secreta o exigir
// portar un objecte concret a sobre
public class Connexio {

    private final Zona desti;
    private final String direccio;
    private boolean oberta;
    private boolean secreta;
    private String condicio;
    private String motiuTancada;

    public Connexio(Zona desti, String direccio) {
        this(desti, direccio, true, false, null, null);
    }

    public Connexio(Zona desti, String direccio, boolean oberta, boolean secreta,
                    String condicio, String motiuTancada) {
        this.desti = desti;
        this.direccio = direccio;
        this.oberta = oberta;
        this.secreta = secreta;
        this.condicio = condicio;
        this.motiuTancada = motiuTancada;
    }

    // pot passar el jugador ara mateix?
    public boolean esTransitable(Jugador j) {
        if (!oberta) {
            return false;
        }
        if (condicio != null && (j == null || !j.getInventari().conte(condicio))) {
            return false;
        }
        return true;
    }

    // el jugador la veu a la descripcio de la zona?
    public boolean esVisible() {
        return !secreta && oberta;
    }

    public void obrir() {
        oberta = true;
        secreta = false;
    }

    public void tancar() {
        oberta = false;
    }

    public Zona getDesti() {
        return desti;
    }

    public String getDireccio() {
        return direccio;
    }

    public boolean esSecreta() {
        return secreta;
    }

    public String getCondicio() {
        return condicio;
    }

    public void setCondicio(String condicio) {
        this.condicio = condicio;
    }

    public String getMotiuTancada() {
        return motiuTancada == null ? "Per aqui no es pot passar." : motiuTancada;
    }

    public void setMotiuTancada(String motiu) {
        this.motiuTancada = motiu;
    }
}
