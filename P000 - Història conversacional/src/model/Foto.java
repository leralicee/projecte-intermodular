package model;

// una foto de l'album: que has retratat, on i a quina hora
public class Foto {

    private final String titol;
    private final String zona;
    private final String hora;
    private final String fitxerImatge;

    public Foto(String titol, String zona, String hora) {
        this.titol = titol;
        this.zona = zona;
        this.hora = hora;
        this.fitxerImatge = "foto_" + titol.toLowerCase().replace(' ', '_') + ".png";
    }

    public String getTitol() {
        return titol;
    }

    public String getZona() {
        return zona;
    }

    public String getHora() {
        return hora;
    }

    public String getFitxerImatge() {
        return fitxerImatge;
    }

    @Override
    public String toString() {
        return titol + " (" + zona + ", " + hora + ")";
    }
}
