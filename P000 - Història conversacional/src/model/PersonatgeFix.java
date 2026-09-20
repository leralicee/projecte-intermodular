package model;

import java.util.ArrayList;

// en Tomeu i en Bernat. No es mouen, bloquegen sortides i donen objectes
public class PersonatgeFix extends Personatge {

    private Connexio bloquejaSortida;
    private Objecte objecteRecompensa;
    private final ArrayList<String> ofrenesRebudes = new ArrayList<>();
    private final ArrayList<String> ofrenesAcceptades = new ArrayList<>();

    public PersonatgeFix(String nom, String descripcio, Zona zona) {
        super(nom, descripcio, zona);
    }

    public void bloquejar(Connexio c, String motiu) {
        this.bloquejaSortida = c;
        c.tancar();
        c.setMotiuTancada(motiu);
    }

    public void setRecompensa(Objecte o, String... ofrenesValides) {
        this.objecteRecompensa = o;
        for (String s : ofrenesValides) {
            ofrenesAcceptades.add(s.toLowerCase());
        }
    }

    @Override
    public String parlar(String clau) {
        if (clau != null) {
            String c = clau.toLowerCase();
            for (String k : respostes.keySet()) {
                if (c.contains(k)) {
                    return respostes.get(k);
                }
            }
        }
        return respostaPerDefecte;
    }

    // el jugador li dona un objecte. si li serveix, desbloqueja i recompensa
    public String rebreOfrena(Objecte o) {
        if (o == null) {
            return respostaPerDefecte;
        }
        String nomOfrena = o.getNom().toLowerCase();
        if (!ofrenesAcceptades.contains(nomOfrena)) {
            return "En " + nom + " mira " + o.getNom() + " i arrufa el nas. Aixo no li interessa.";
        }
        if (ofrenesRebudes.contains(nomOfrena)) {
            return "Aixo ja te l'hi has donat.";
        }
        ofrenesRebudes.add(nomOfrena);
        desbloquejar();
        return "En " + nom + " accepta " + o.getNom() + " i se li illumina la cara.";
    }

    // entrega la recompensa un sol cop, i nomes si abans ha rebut una ofrena
    public Objecte lliurarRecompensa() {
        if (objecteRecompensa == null || ofrenesRebudes.isEmpty()) {
            return null;
        }
        Objecte premi = objecteRecompensa;
        objecteRecompensa = null;
        return premi;
    }

    public void desbloquejar() {
        if (bloquejaSortida != null) {
            bloquejaSortida.obrir();
        }
    }

    // ha rebut totes les ofrenes possibles? condicio del final secret
    public boolean haRebutTot() {
        return ofrenesRebudes.size() >= ofrenesAcceptades.size() && !ofrenesAcceptades.isEmpty();
    }

    public ArrayList<String> getOfrenesRebudes() {
        return ofrenesRebudes;
    }

    public Objecte getObjecteRecompensa() {
        return objecteRecompensa;
    }

    public Connexio getBloquejaSortida() {
        return bloquejaSortida;
    }
}
