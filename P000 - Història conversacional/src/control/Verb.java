package control;

public enum Verb {

    ANAR, AGAFAR, DEIXAR, USAR, OBRIR, TANCAR, ENCENDRE, APAGAR, PARLAR,
    FER_FOTO, INVENTARI, MIRAR;

    public static Verb desDeText(String s) {
        if (s == null) {
            return null;
        }
        for (Verb v : values()) {
            if (v.name().equalsIgnoreCase(s)) {
                return v;
            }
        }
        return null;
    }
}
