package model;

import java.util.ArrayList;
import java.util.Random;

// el senglar. cada dos torns del jugador es mou a una zona veina de les quete permeses. trobar-se'l sense haver-lo distret acaba la partida
public class PersonatgeMobil extends Personatge {

    public static final int CADA_N_TORNS = 2;

    private final ArrayList<Zona> zonesPermeses = new ArrayList<>();
    private final Random atzar = new Random();
    private boolean distret;

    public PersonatgeMobil(String nom, String descripcio, Zona zonaInicial) {
        super(nom, descripcio, zonaInicial);
    }

    public void afegirZonaPermesa(Zona z) {
        zonesPermeses.add(z);
    }

    // el mou una zona veina. Si esta distret o no pot anar enlloc, es queda
    public void moure() {
        if (distret || zonesPermeses.isEmpty()) {
            return;
        }
        ArrayList<Zona> possibles = new ArrayList<>();
        for (Zona veina : zonaActual.zonesVeines()) {
            if (zonesPermeses.contains(veina)) {
                possibles.add(veina);
            }
        }
        if (possibles.isEmpty()) {
            return;
        }
        situarA(possibles.get(atzar.nextInt(possibles.size())));
    }

    // el canvia de zona i actualitza en quina llista d'elements apareix
    public void situarA(Zona nova) {
        if (zonaActual != null) {
            zonaActual.getElements().remove(this);
        }
        zonaActual = nova;
        if (nova != null && !nova.getElements().contains(this)) {
            nova.afegirElement(this);
        }
    }

    // li dones la poma: queda entretingut la resta de la partida
    public boolean distreure(Objecte o) {
        if (o == null || !o.getNom().equalsIgnoreCase("poma")) {
            return false;
        }
        distret = true;
        return true;
    }

    public boolean estaDistret() {
        return distret;
    }

    // zones de distancia fins a z, o -1 si no hi ha cami. per a la pista sonora
    public int distanciaA(Zona z) {
        if (z == null) {
            return -1;
        }
        if (z == zonaActual) {
            return 0;
        }
        ArrayList<Zona> visitades = new ArrayList<>();
        ArrayList<Zona> nivell = new ArrayList<>();
        nivell.add(zonaActual);
        visitades.add(zonaActual);
        int distancia = 0;
        while (!nivell.isEmpty()) {
            distancia++;
            ArrayList<Zona> seguent = new ArrayList<>();
            for (Zona actual : nivell) {
                for (Zona veina : actual.zonesVeines()) {
                    if (visitades.contains(veina)) {
                        continue;
                    }
                    if (veina == z) {
                        return distancia;
                    }
                    visitades.add(veina);
                    seguent.add(veina);
                }
            }
            nivell = seguent;
        }
        return -1;
    }

    // pista ambiental que es mostra en entrar a una zona
    public String pista(Zona onEsElJugador) {
        if (distret) {
            return null;
        }
        int d = distanciaA(onEsElJugador);
        if (d == 1) {
            return "Sents grunyits molt a prop. Ve d'alguna sortida d'aqui mateix.";
        }
        if (d == 2) {
            return "Se sent un gruny llunya entre els arbres.";
        }
        return null;
    }

    @Override
    public String parlar(String clau) {
        return "En " + nom + " no es de parlar gaire.";
    }

    public ArrayList<Zona> getZonesPermeses() {
        return zonesPermeses;
    }

    public void reiniciar(Zona zonaInicial) {
        this.zonaActual = zonaInicial;
        this.distret = false;
    }
}
