package model;

import java.util.ArrayList;

// el que porta el jugador a la motxilla
public class Inventari {

    public static final int CAPACITAT_MAXIMA = 8;

    private final ArrayList<Objecte> objectes = new ArrayList<>();

    public boolean esPle() {
        return objectes.size() >= CAPACITAT_MAXIMA;
    }

    public void afegir(Objecte o) {
        objectes.add(o);
    }

    public Objecte treure(String nom) {
        Objecte o = cercar(nom);
        if (o != null) {
            objectes.remove(o);
        }
        return o;
    }

    public boolean conte(String nom) {
        return cercar(nom) != null;
    }

    public Objecte cercar(String nom) {
        if (nom == null) {
            return null;
        }
        String n = nom.toLowerCase();
        for (Objecte o : objectes) {
            if (o.getNom().toLowerCase().equals(n)) {
                return o;
            }
        }
        for (Objecte o : objectes) {
            if (o.getNom().toLowerCase().contains(n)) {
                return o;
            }
        }
        return null;
    }

    public String llistar() {
        if (objectes.isEmpty()) {
            return "No portes res a la motxilla.";
        }
        StringBuilder sb = new StringBuilder("A la motxilla portes:\n");
        for (Objecte o : objectes) {
            sb.append("  - ").append(o.getNom()).append('\n');
        }
        return sb.toString().trim();
    }

    public ArrayList<Objecte> getObjectes() {
        return objectes;
    }

    public void buidar() {
        objectes.clear();
    }
}
