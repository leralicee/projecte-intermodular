package model;

// en quin punt es troba la partida
public enum EstatPartida {

    EN_CURS,
    VICTORIA,
    DERROTA_TEMPS,
    DERROTA_SENGLAR,
    FINAL_SECRET;

    public boolean esFinal() {
        return this != EN_CURS;
    }
}
