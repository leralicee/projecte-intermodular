package model;

// tria quina de les tres imatges de la zona es mostra
public enum FaseDelDia {

    DIA, CAPVESPRE, NIT;

    // posicio dins array Zona.imatges[]
    public int index() {
        return ordinal();
    }

    // sufix del fitxer d'imatge
    public String sufix() {
        return name().toLowerCase();
    }
}
