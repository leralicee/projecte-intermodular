package model;

// en bond de l'original, aqui l'excursionista. sap on es i que porta
public class Jugador {

    private final String nom;
    private Zona zonaActual;
    private final Inventari inventari = new Inventari();

    public Jugador(String nom, Zona zonaInicial) {
        this.nom = nom;
        this.zonaActual = zonaInicial;
    }

    public void moureA(Zona z) {
        this.zonaActual = z;
        z.marcarVisitada();
    }

    public Zona getZonaActual() {
        return zonaActual;
    }

    public Inventari getInventari() {
        return inventari;
    }

    public String getNom() {
        return nom;
    }

    // porta una llanterna encesa a sobre? decideix si veu a la cova
    public boolean teLlumEncesa() {
        for (Objecte o : inventari.getObjectes()) {
            if (o instanceof Encenible && ((Encenible) o).estaEncesa()) {
                return true;
            }
        }
        return false;
    }

    public void reiniciar(Zona zonaInicial) {
        this.zonaActual = zonaInicial;
        inventari.buidar();
    }
}
