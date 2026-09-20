package model;

import java.util.ArrayList;

// una zona del mapa: descripcio, les tres imatges, les sortides i els elements
public class Zona {

    private final String nom;
    private final String descripcio;

    // array de mida fixa, indexat per FaseDelDia: dia / capvespre / nit
    private final String[] imatges = new String[FaseDelDia.values().length];

    private final ArrayList<Connexio> connexions = new ArrayList<>();
    private final ArrayList<Element> elements = new ArrayList<>();

    private boolean visitada;
    private boolean fosca;

    public Zona(String nom, String descripcio) {
        this(nom, descripcio, false);
    }

    public Zona(String nom, String descripcio, boolean fosca) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.fosca = fosca;
        String base = nom.toLowerCase().replace(' ', '_');
        for (FaseDelDia f : FaseDelDia.values()) {
            imatges[f.index()] = base + "_" + f.sufix() + ".png";
        }
    }

    // text complet que es mostra en entrar a la zona
    public String descriure(FaseDelDia fase) {
        StringBuilder sb = new StringBuilder();
        sb.append("== ").append(nom).append(" ==\n");
        sb.append(descripcio).append('\n');
        if (fase == FaseDelDia.NIT) {
            sb.append("Ja es gairebe fosc i costa distingir els camins.\n");
        } else if (fase == FaseDelDia.CAPVESPRE) {
            sb.append("La llum comenca a caure.\n");
        }
        String sortides = llistarSortides();
        sb.append('\n').append(sortides);
        return sb.toString();
    }

    // les sortides que el jugador pot veure amb el nom de la zona de desti
    public String llistarSortides() {
        StringBuilder sb = new StringBuilder("Sortides: ");
        boolean cap = true;
        for (Connexio c : connexions) {
            if (!c.esVisible()) {
                continue;
            }
            if (!cap) {
                sb.append(" | ");
            }
            sb.append(c.getDireccio()).append(" (").append(c.getDesti().getNom()).append(')');
            cap = false;
        }
        if (cap) {
            return "Sortides: cap de visible.";
        }
        return sb.toString();
    }

    public void afegirConnexio(Connexio c) {
        connexions.add(c);
    }

    // busca per direccio o per nom de zona, valen "ANAR NORD" i "ANAR A LA CLARIANA"
    public Connexio getConnexio(String desti) {
        if (desti == null) {
            return null;
        }
        String d = desti.toLowerCase();
        for (Connexio c : connexions) {
            if (c.getDireccio().toLowerCase().equals(d)) {
                return c;
            }
        }
        for (Connexio c : connexions) {
            String nomDesti = c.getDesti().getNom().toLowerCase();
            if (nomDesti.equals(d) || nomDesti.contains(d)) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Connexio> getConnexions() {
        return connexions;
    }

    // les zones veines a les quals es pot passar (ho fa servir el senglar)
    public ArrayList<Zona> zonesVeines() {
        ArrayList<Zona> veines = new ArrayList<>();
        for (Connexio c : connexions) {
            if (!veines.contains(c.getDesti())) {
                veines.add(c.getDesti());
            }
        }
        return veines;
    }

    public void afegirElement(Element e) {
        elements.add(e);
    }

    public Element treureElement(String nom) {
        Element e = cercarElement(nom);
        if (e != null) {
            elements.remove(e);
        }
        return e;
    }

    public Element cercarElement(String nom) {
        if (nom == null) {
            return null;
        }
        String n = nom.toLowerCase();
        for (Element e : elements) {
            if (e.getNom().toLowerCase().equals(n)) {
                return e;
            }
        }
        for (Element e : elements) {
            if (e.getNom().toLowerCase().contains(n)) {
                return e;
            }
        }
        return null;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public String getNom() {
        return nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public String getImatge(FaseDelDia fase) {
        return imatges[fase.index()];
    }

    public boolean esVisitada() {
        return visitada;
    }

    public void marcarVisitada() {
        visitada = true;
    }

    public boolean esFosca() {
        return fosca;
    }

    public void setFosca(boolean fosca) {
        this.fosca = fosca;
    }

    @Override
    public String toString() {
        return nom;
    }
}
