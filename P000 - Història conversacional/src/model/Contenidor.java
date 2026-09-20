package model;

import control.ResultatAccio;
import control.Verb;

import java.util.ArrayList;

// element que s'obre i es tanca i guarda objectes: el tronc, el cofre i la caixa
public class Contenidor extends Element {

    private boolean obert;
    private String clauNecessaria;
    private final ArrayList<Objecte> contingut = new ArrayList<>();

    public Contenidor(String nom, String descripcio) {
        this(nom, descripcio, null);
    }

    public Contenidor(String nom, String descripcio, String clauNecessaria) {
        super(nom, descripcio);
        this.clauNecessaria = clauNecessaria;
    }

    public void guardar(Objecte o) {
        contingut.add(o);
    }

    public ResultatAccio obrir(Jugador j) {
        if (obert) {
            return ResultatAccio.error("El " + nom + " ja esta obert.");
        }
        if (clauNecessaria != null && (j == null || !j.getInventari().conte(clauNecessaria))) {
            return ResultatAccio.error("El " + nom + " no cedeix. Et caldria " + clauNecessaria + ".");
        }
        obert = true;
        if (contingut.isEmpty()) {
            return ResultatAccio.ok("Obres el " + nom + ". Es buit.");
        }
        StringBuilder sb = new StringBuilder("Obres el " + nom + ". A dins hi ha:");
        for (Objecte o : contingut) {
            sb.append("\n  - ").append(o.getNom());
        }
        return ResultatAccio.ok(sb.toString());
    }

    public ResultatAccio tancar() {
        if (!obert) {
            return ResultatAccio.error("El " + nom + " ja esta tancat.");
        }
        obert = false;
        return ResultatAccio.ok("Tanques el " + nom + ".");
    }

    // treu tot el contingut de cop i el retorna
    public ArrayList<Objecte> buidar() {
        ArrayList<Objecte> fora = new ArrayList<>(contingut);
        contingut.clear();
        return fora;
    }

    public ArrayList<Objecte> getContingut() {
        return contingut;
    }

    public boolean estaObert() {
        return obert;
    }

    @Override
    public String descriure() {
        return descripcio + (obert ? " Esta obert." : " Esta tancat.");
    }

    @Override
    public ResultatAccio interactuar(Verb v, Jugador j) {
        switch (v) {
            case OBRIR:
                return obrir(j);
            case TANCAR:
                return tancar();
            case MIRAR:
                return ResultatAccio.okSenseTemps(descriure());
            default:
                return ResultatAccio.error("Amb " + nom + " no pots fer aixo.");
        }
    }
}
