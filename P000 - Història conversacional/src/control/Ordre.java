package control;

// una ordre analitzada: "USAR LA NAVALLA AMB EL COFRE" -> USAR/navalla/cofre
public class Ordre {

    private final Verb verb;
    private final String complement1;
    private final String complement2;

    public Ordre(Verb verb, String complement1, String complement2) {
        this.verb = verb;
        this.complement1 = complement1;
        this.complement2 = complement2;
    }

    public Verb getVerb() {
        return verb;
    }

    public String getComplement1() {
        return complement1;
    }

    public String getComplement2() {
        return complement2;
    }

    public boolean teComplement1() {
        return complement1 != null && !complement1.isEmpty();
    }

    public boolean teComplement2() {
        return complement2 != null && !complement2.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(verb.name());
        if (teComplement1()) {
            sb.append(' ').append(complement1);
        }
        if (teComplement2()) {
            sb.append(" amb ").append(complement2);
        }
        return sb.toString();
    }
}
