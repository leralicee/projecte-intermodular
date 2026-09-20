package model;

import control.ResultatAccio;
import control.Verb;

// la camera que portes des de l'inici. jo resol cap puzle (omple album)
public class Camera extends Objecte {

    private final Album album;

    public Camera(Album album) {
        super("camera", "La teva camera de fotos. Va ser la culpable de tot.");
        this.album = album;
    }

    public Foto ferFoto(Zona z, Rellotge r) {
        Foto f = new Foto(z.getNom(), z.getNom(), r.getHoraFormatada());
        album.afegir(f);
        return f;
    }

    @Override
    public ResultatAccio interactuar(Verb v, Jugador j) {
        if (v == Verb.FER_FOTO) {
            return ResultatAccio.error("Digues que vols fotografiar.");
        }
        return super.interactuar(v, j);
    }
}
