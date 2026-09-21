package control;

import excepcions.OrdreInvalidaException;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// converteix el text del jugador en una ordre. accepta sinonims, ignora articles i no distingeix majuscules ni accents
public class AnalitzadorOrdres {

    // paraula escrita -> verb del joc
    private final HashMap<String, Verb> SINONIMS = new HashMap<>();

    // paraules que no aporten res (es treuen abans d'analitzar)
    private static final String[] PARAULES_BUIDES = {
        "el", "la", "els", "les", "l", "un", "una", "uns", "unes",
        "a", "al", "als", "de", "del", "dels", "en", "cap", "the"
    };

    // separadors entre el primer i el segon complement
    private static final String[] SEPARADORS = {"amb", "sobre", "contra", "dins"};

    // direccions que es poden escriure sense anar davant
    private static final String[] DIRECCIONS = {"nord", "sud", "est", "oest"};

    public AnalitzadorOrdres() {
        afegir(Verb.ANAR, "anar", "ves", "vas", "va", "camina", "mou", "moure", "ir");
        afegir(Verb.AGAFAR, "agafar", "agafa", "agaf", "pren", "prendre", "coge", "coger");
        afegir(Verb.DEIXAR, "deixar", "deixa", "dona", "donar", "solta", "soltar");
        afegir(Verb.USAR, "usar", "usa", "utilitza", "utilitzar", "fer servir");
        afegir(Verb.OBRIR, "obrir", "obre", "obra");
        afegir(Verb.TANCAR, "tancar", "tanca");
        afegir(Verb.ENCENDRE, "encendre", "encen", "engega", "engegar");
        afegir(Verb.APAGAR, "apagar", "apaga");
        afegir(Verb.PARLAR, "parlar", "parla", "digues", "dir", "pregunta", "preguntar");
        afegir(Verb.FER_FOTO, "fotografiar", "fotografia", "foto", "retratar");
        afegir(Verb.INVENTARI, "inventari", "motxilla", "bossa", "i");
        afegir(Verb.MIRAR, "mirar", "mira", "observar", "observa", "examinar", "examina", "veure");
    }

    private void afegir(Verb v, String... paraules) {
        for (String p : paraules) {
            SINONIMS.put(p, v);
        }
    }

    // llança OrdreInvalidaException si no troba cap verb
    public Ordre analitzar(String text) throws OrdreInvalidaException {
        String net = normalitzar(text);
        if (net.isEmpty()) {
            throw new OrdreInvalidaException(text);
        }

        // "fer servir" son dues paraules, les ajuntem abans de partir el text
        net = net.replace("fer servir", "usar");

        List<String> paraules = new ArrayList<>();
        for (String p : net.split(" ")) {
            if (!p.isEmpty() && !esBuida(p)) {
                paraules.add(p);
            }
        }
        if (paraules.isEmpty()) {
            throw new OrdreInvalidaException(text);
        }

        // una direccio sola equival a anar cap alla
        if (paraules.size() == 1 && esDireccio(paraules.get(0))) {
            return new Ordre(Verb.ANAR, paraules.get(0), null);
        }

        Verb verb = cercarVerb(paraules.get(0));
        if (verb == null) {
            throw new OrdreInvalidaException(text);
        }
        paraules.remove(0);

        // partim la resta pel separador si n'hi ha
        StringBuilder c1 = new StringBuilder();
        StringBuilder c2 = new StringBuilder();
        boolean segona = false;
        for (String p : paraules) {
            if (!segona && esSeparador(p)) {
                segona = true;
                continue;
            }
            StringBuilder desti = segona ? c2 : c1;
            if (desti.length() > 0) {
                desti.append(' ');
            }
            desti.append(p);
        }

        String comp1 = c1.length() == 0 ? null : c1.toString();
        String comp2 = c2.length() == 0 ? null : c2.toString();
        return new Ordre(verb, comp1, comp2);
    }

    // minuscules, sense accents i sense espais sobrers
    public String normalitzar(String text) {
        if (text == null) {
            return "";
        }
        String s = Normalizer.normalize(text.trim().toLowerCase(), Normalizer.Form.NFD);
        s = s.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        s = s.replace("·", "").replace("'", " ").replace("\"", " ");
        s = s.replaceAll("[^a-z0-9 ]", " ");
        return s.replaceAll("\\s+", " ").trim();
    }

    // el verb d'una paraula, o null si no la coneixem
    public Verb cercarVerb(String paraula) {
        return SINONIMS.get(paraula);
    }

    private boolean esBuida(String p) {
        return Arrays.asList(PARAULES_BUIDES).contains(p);
    }

    private boolean esSeparador(String p) {
        return Arrays.asList(SEPARADORS).contains(p);
    }

    private boolean esDireccio(String p) {
        return Arrays.asList(DIRECCIONS).contains(p);
    }
}
