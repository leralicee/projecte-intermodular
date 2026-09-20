package model;

import java.util.ArrayList;

// les fotos que ha fet el jugador. es mostra a l'epileg de qualsevol final
public class Album {

    public static final int TOTAL_MOMENTS = 8;

    private final ArrayList<Foto> fotos = new ArrayList<>();

    public void afegir(Foto f) {
        for (Foto existent : fotos) {
            if (existent.getTitol().equalsIgnoreCase(f.getTitol())) {
                return;
            }
        }
        fotos.add(f);
    }

    public String getProgres() {
        return "Has capturat " + fotos.size() + " de " + TOTAL_MOMENTS
             + " moments del Puig de les Bruixes.";
    }

    public String mostrarGaleria() {
        if (fotos.isEmpty()) {
            return "No has fet cap foto.";
        }
        StringBuilder sb = new StringBuilder(getProgres()).append('\n');
        for (Foto f : fotos) {
            sb.append("  - ").append(f).append('\n');
        }
        return sb.toString();
    }

    public ArrayList<Foto> getFotos() {
        return fotos;
    }

    public void buidar() {
        fotos.clear();
    }
}
