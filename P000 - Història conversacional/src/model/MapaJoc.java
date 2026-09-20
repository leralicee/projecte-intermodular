package model;

import java.util.HashMap;
import java.util.LinkedHashMap;

// construeix les deu zones i les lliga. les sortides van d'una a quatre, i tres estan condicionades: el pas secret, el pont trencat i la drecera
public class MapaJoc {

    public static final String[] NOMS_ZONES = {
        "Camp Base", "Corriol del Bosc", "Clariana de les Flors", "El Riu", "Cascada",
        "Cova Fosca", "Pont Penjant", "Cim del Puig", "Ermita Abandonada", "Refugi Amagat"
    };

    private final HashMap<String, Zona> zones = new LinkedHashMap<>();

    // sortida Corriol -> Camp Base: la tanca la boira
    private Connexio tornadaCampBase;

    private PersonatgeMobil senglar;
    private PersonatgeFix tomeu;

    // les dues puntes de la drecera Refugi <-> Corriol. Les obre el mapa
    private Connexio dreceraAvall;
    private Connexio dreceraAmunt;

    public MapaJoc() {
        construir();
    }

    public final void construir() {
        zones.clear();

        Zona campBase = crear("Camp Base",
            "El prat on ha aparcat el bus. La motxilla del grup encara es aqui, oberta,\n"
          + "i a dins hi ha la teva cantimplora. El Bernat fuma recolzat a la porta.");

        Zona corriol = crear("Corriol del Bosc",
            "Un corriol estret entre pins. Hi ha petjades fresques a la terra molla i,\n"
          + "una mica mes amunt, un pomer salvatge amb una poma massa alta per arribar-hi.");

        Zona clariana = crear("Clariana de les Flors",
            "Una clariana rodona, massa rodona. Al mig hi ha un tronc buit caigut i,\n"
          + "gravades a l'escorca d'un pi, unes marques que no semblen fetes per ningu d'avui.");

        Zona riu = crear("El Riu",
            "Un torrent d'aigua clara que baixa forta. Hi ha pedres planes per fer un gual\n"
          + "i el soroll de l'aigua tapa qualsevol altre so.");

        Zona cascada = crear("Cascada",
            "L'aigua cau des de deu metres i aixeca una boira freda. Darrere la cortina\n"
          + "d'aigua s'endevina una ombra que podria ser un forat, o podria no ser res.");

        Zona cova = crear("Cova Fosca",
            "Una galeria de roca viva que fa olor d'humitat. El terra es ple de pedres\n"
          + "soltes i d'algun os petit que val mes no mirar de prop.", true);

        Zona pont = crear("Pont Penjant",
            "Un pont de corda sobre un congost. Falten taulons i les cordes laterals\n"
          + "estan desfilades. Tal com esta, no aguantaria el teu pes.");

        Zona cim = crear("Cim del Puig",
            "El punt mes alt. Des d'aqui es veu tota la vall i, molt avall, el prat del bus.\n"
          + "Hi ha un vertex geodesic de formigo amb una caixa metal.lica encastada.");

        Zona ermita = crear("Ermita Abandonada",
            "Una ermita de pedra mig ensorrada. Surt fum per un forat del sostre, aixi que\n"
          + "de abandonada no en te res. D'una rama hi penja un ninot fet de branques.");

        Zona refugi = crear("Refugi Amagat",
            "Un refugi de pastor, fosc i sec. Hi ha un cofre de fusta amb un pany rovellat\n"
          + "i una porta baixa que dona cap avall, cap al bosc.");

        // SORTIDES
        unir(campBase, corriol, "nord", "sud");
        unir(corriol, clariana, "nord", "sud");
        unir(clariana, riu, "est", "oest");
        unir(riu, cascada, "nord", "sud");
        unir(clariana, cova, "nord", "sud");
        unir(cova, pont, "nord", "sud");
        unir(pont, cim, "nord", "sud");
        unir(cim, ermita, "nord", "sud");
        unir(cova, refugi, "oest", "est");
        unir(ermita, refugi, "oest", "nord");

        // la tornada pel corriol es el que tancara la boira
        tornadaCampBase = corriol.getConnexio("sud");

        // pas secret de la cascada cap a la cova: nomes amb la llanterna encesa
        Connexio secret = new Connexio(cova, "oest", true, true, null, null);
        cascada.afegirConnexio(secret);
        Connexio tornadaSecret = new Connexio(cascada, "est", true, true, null, null);
        cova.afegirConnexio(tornadaSecret);

        // el pont nomes es pot travessar un cop reforcat amb la corda
        Connexio capAlCim = pont.getConnexio("nord");
        capAlCim.tancar();
        capAlCim.setMotiuTancada("El pont no aguantaria. Caldria reforcar-lo amb alguna cosa.");

        // la drecera entre el refugi i el corriol nomes s'obre amb el mapa
        String pistaDrecera = "Hi ha una porta baixa, pero darrere nomes s'hi veu bosc tancat i boira.";
        dreceraAvall = new Connexio(corriol, "sud", false, true, null, pistaDrecera);
        dreceraAmunt = new Connexio(refugi, "oest", false, true, null, pistaDrecera);
        refugi.afegirConnexio(dreceraAvall);
        corriol.afegirConnexio(dreceraAmunt);

        situarElements(campBase, corriol, clariana, riu, cascada, cova, pont, cim, ermita, refugi);
    }

    // reparteix objectes, contenidors i personatges per les zones
    private void situarElements(Zona campBase, Zona corriol, Zona clariana, Zona riu,
                                Zona cascada, Zona cova, Zona pont, Zona cim,
                                Zona ermita, Zona refugi) {

        campBase.afegirElement(new Objecte("cantimplora",
            "La teva cantimplora d'alumini. Ara mateix es buida."));

        corriol.afegirElement(new Objecte("basto",
            "Un basto de bruc, prou llarg per arribar a les branques altes."));
        corriol.afegirElement(new Objecte("pomer",
            "Un pomer salvatge. Hi ha una poma vermella fora del teu abast.", false));

        Contenidor tronc = new Contenidor("tronc",
            "Un tronc buit caigut al mig de la clariana.");
        tronc.guardar(new Llanterna());
        clariana.afegirElement(tronc);

        cova.afegirElement(new Objecte("navalla",
            "Una navalla de pastor, mig amagada entre les pedres."));

        riu.afegirElement(new Objecte("torrent",
            "L'aigua baixa clara i freda. Es pot beure.", false));

        pont.afegirElement(new Objecte("pont",
            "El pont penjant. Li falten taulons i les cordes laterals estan desfilades.", false));

        Contenidor cofre = new Contenidor("cofre",
            "Un cofre de fusta amb un pany rovellat.", "navalla");
        cofre.guardar(new Objecte("corda",
            "Una corda gruixuda de canem, en bon estat."));
        refugi.afegirElement(cofre);

        Contenidor caixa = new Contenidor("caixa",
            "La caixa metal.lica del vertex geodesic.");
        caixa.guardar(new Objecte("quadern",
            "El quadern de registre del cim. L'ultima signatura llegible es d'en Tomeu,\n"
          + "i porta data de fa vuitanta anys.", false));
        cim.afegirElement(caixa);

        PersonatgeFix bernat = new PersonatgeFix("Bernat",
            "El conductor del bus. Mira el rellotge cada dos minuts.", campBase);
        bernat.setRespostaPerDefecte("A les sis en punt arrenco, amb tu o sense tu.");
        bernat.afegirResposta("hora", "Son les sis menys el que sigui. Tu afanya't.");
        bernat.afegirResposta("boira", "Aquesta boira no m'agrada gens. Puja i baixa de cop.");
        campBase.afegirElement(bernat);

        PersonatgeFix tomeu = new PersonatgeFix("Tomeu",
            "Un vell amb barba de tres setmanes, assegut en una cadira de boga.", ermita);
        tomeu.setRespostaPerDefecte("Fa molts anys que no baixa ningu. I els que baixen, "
            + "baixen amb les mans buides.");
        tomeu.afegirResposta("mapa", "Mapa? En tinc un. Pero els mapes es canvien, no es regalen.");
        tomeu.afegirResposta("senglar", "El senglar? Va on vol. Escolta i el sentiras.");
        tomeu.afegirResposta("boira", "La boira no es perd ningu que no s'hi vulgui perdre.");
        tomeu.setRecompensa(new Objecte("mapa",
            "Un mapa dibuixat a ma. Hi ha una drecera marcada que no surt a cap guia."),
            "cantimplora", "poma");
        ermita.afegirElement(tomeu);

        PersonatgeMobil senglar = new PersonatgeMobil("senglar",
            "Un senglar gros i de mal humor.", cova);
        senglar.afegirZonaPermesa(cova);
        senglar.afegirZonaPermesa(clariana);
        senglar.afegirZonaPermesa(cascada);
        senglar.situarA(cova);
        this.senglar = senglar;
        this.tomeu = tomeu;
    }

    private Zona crear(String nom, String descripcio) {
        return crear(nom, descripcio, false);
    }

    private Zona crear(String nom, String descripcio, boolean fosca) {
        Zona z = new Zona(nom, descripcio, fosca);
        zones.put(nom.toLowerCase(), z);
        return z;
    }

    // crea la sortida d'anada i la de tornada entre dues zones
    private void unir(Zona a, Zona b, String direccio, String direccioInversa) {
        a.afegirConnexio(new Connexio(b, direccio));
        b.afegirConnexio(new Connexio(a, direccioInversa));
    }

    // la boira esborra el corriol: ja no es pot tornar per on has vingut
    public void bloquejarCorriol() {
        if (tornadaCampBase != null) {
            tornadaCampBase.tancar();
            tornadaCampBase.setMotiuTancada(
                "El corriol per on has pujat ja no hi es. Nomes hi ha boira i bosc tancat.");
        }
    }

    // obre la drecera i tambe la baixada que havia esborrat la boira. Si nomes obris
    // la drecera, el cami seguiria tallat mes avall i no es podria guanyar
    public boolean obrirDrecera() {
        if (dreceraAvall == null || dreceraAvall.esTransitable(null)) {
            return false;
        }
        dreceraAvall.obrir();
        dreceraAmunt.obrir();
        if (tornadaCampBase != null) {
            tornadaCampBase.obrir();
            tornadaCampBase.setMotiuTancada(null);
        }
        return true;
    }

    public Zona getZona(String nom) {
        if (nom == null) {
            return null;
        }
        Zona z = zones.get(nom.toLowerCase());
        if (z != null) {
            return z;
        }
        for (Zona candidata : zones.values()) {
            if (candidata.getNom().toLowerCase().contains(nom.toLowerCase())) {
                return candidata;
            }
        }
        return null;
    }

    public Zona getZonaInicial() {
        return getZona(NOMS_ZONES[0]);
    }

    public PersonatgeMobil getSenglar() {
        return senglar;
    }

    public PersonatgeFix getTomeu() {
        return tomeu;
    }

    public int nombreDeZones() {
        return zones.size();
    }
}
