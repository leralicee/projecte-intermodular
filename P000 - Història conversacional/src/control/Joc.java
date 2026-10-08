package control;

import excepcions.JocException;
import model.Album;
import model.Camera;
import model.Connexio;
import model.Contenidor;
import model.Element;
import model.EstatPartida;
import model.FaseDelDia;
import model.Inventari;
import model.Jugador;
import model.Llanterna;
import model.MapaJoc;
import model.Objecte;
import model.Objectiu;
import model.PersonatgeFix;
import model.PersonatgeMobil;
import model.Rellotge;
import model.Zona;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

// estat de la partida i bucle de joc. cada torn segueix l'ordre de l'enunciat: es mostra la zona, s'espera una ordre, s'avalua i es comprova el final
public class Joc {

    private MapaJoc mapa;
    private Jugador jugador;
    private Rellotge rellotge;
    private PersonatgeMobil senglar;
    private final ArrayList<PersonatgeFix> aliats = new ArrayList<>();
    private Album album;
    private EstatPartida estat;
    private int torns;
    private boolean boiraActivada;

    // torns seguits a la mateixa zona que el senglar (al segon carrega)
    private int tornsAmbSenglar;

    // aquests dos no es reinicien: passen d'una partida a la seguent
    private boolean recordDelPuig;
    private boolean dejaVu;

    private final AnalitzadorOrdres analitzador = new AnalitzadorOrdres();
    private final MotorDeJoc motor = new MotorDeJoc(this);

    // avisos que genera el joc pel seu compte (pistes, boira...)
    private final StringBuilder avisos = new StringBuilder();

    public Joc() {
        iniciarPartida();
    }

    public final void iniciarPartida() {
        mapa = new MapaJoc();
        rellotge = new Rellotge();
        album = new Album();
        jugador = new Jugador("excursionista", mapa.getZonaInicial());
        senglar = mapa.getSenglar();
        aliats.clear();
        if (mapa.getTomeu() != null) {
            aliats.add(mapa.getTomeu());
        }
        jugador.getInventari().afegir(new Camera(album));
        if (recordDelPuig) {
            jugador.getInventari().afegir(new Objecte("record",
                "Un palet blanc del torrent que et va donar en Tomeu. No serveix per a res."));
        }
        jugador.getZonaActual().marcarVisitada();
        estat = EstatPartida.EN_CURS;
        torns = 0;
        boiraActivada = false;
        tornsAmbSenglar = 0;
        avisos.setLength(0);
    }

    // TORN

    // analitza i executa el que escriu el jugador. si l'accio gasta temps avança el torn
    public ResultatAccio processarEntrada(String text) {
        if (estat.esFinal()) {
            return ResultatAccio.error("La partida ja s'ha acabat.");
        }
        String objectiuAbans = objectiuActual();
        ResultatAccio r;
        try {
            Ordre o = analitzador.analitzar(text);
            r = motor.executar(o);
        } catch (JocException e) {
            r = ResultatAccio.error(e.getMissatgeJugador());
        }

        // un error o un MIRAR no son un torn: no gasten el marge davant del senglar
        // el senglar es mou despres de comprovar el final: si no, marxava just abans de carregar
        if (r.consumeixTemps()) {
            avancarTorn();
            comprovarFinal();
            moureSenglar();
            // el marge es compta sobre torns que ACABEN amb el senglar al damunt. si avisessim
            // o comptessim abans, el senglar encara es podia moure despres: o llegies que et
            // barrava el pas quan ja havia marxat, o et carregava sense haver-te avisat mai
            if (!estat.esFinal()) {
                if (trobatAmbSenglar()) {
                    avisarSenglar();
                } else {
                    tornsAmbSenglar = 0;
                }
            }
        }

        // si l'ordre ha fet avancar la historia, diem quina es la fita seguent. nomes quan canvia:
        // repetir-la cada torn seria soroll
        if (!estat.esFinal()) {
            String objectiuAra = objectiuActual();
            if (objectiuAra != null && !objectiuAra.equals(objectiuAbans)) {
                avisar("Nou objectiu: " + objectiuAra);
            }
        }

        String extra = recollirAvisos();
        if (!extra.isEmpty()) {
            r = new ResultatAccio(r.getText() + "\n" + extra, r.esCorrecta(), r.consumeixTemps());
        }
        return r;
    }

    // passa el temps
    public void avancarTorn() {
        torns++;
        rellotge.avancar();
        gastarPila();
    }

    // cada N torns el senglar canvia de zona. si arriba on ets tu, comença el torn de marge
    public void moureSenglar() {
        if (senglar == null || estat.esFinal() || torns % PersonatgeMobil.CADA_N_TORNS != 0) {
            return;
        }
        Zona abans = senglar.getZonaActual();
        senglar.moure();
        if (senglar.getZonaActual() != abans && trobatAmbSenglar()) {
            tornsAmbSenglar = 1;
        }
    }

    // la pila baixa nomes mentre la llanterna esta encesa, per aixo val la pena apagar-la
    private void gastarPila() {
        for (Objecte o : jugador.getInventari().getObjectes()) {
            if (!(o instanceof Llanterna)) {
                continue;
            }
            Llanterna l = (Llanterna) o;
            if (!l.estaEncesa()) {
                continue;
            }
            l.gastarBateria(Rellotge.MINUTS_PER_ORDRE);
            if (l.getBateria() == 0) {
                l.apagar();
                avisar("La llanterna parpelleja un cop i s'apaga. La pila s'ha acabat.");
            } else if (l.getBateria() == 20 || l.getBateria() == 10) {
                avisar("La llum de la llanterna es va afeblint.");
            }
        }
    }

    // es crida en entrar a una zona. aqui salta la boira i la pista del senglar
    public void enEntrarAZona(Zona z) {
        if (!boiraActivada && z == mapa.getZona("Clariana de les Flors")) {
            activarBoira();
        }
        if (senglar != null) {
            String pista = senglar.pista(z);
            if (pista != null) {
                avisar(pista);
            }
        }
    }

    // la boira esborra el corriol de tornada (nomes un cop)
    public void activarBoira() {
        boiraActivada = true;
        mapa.bloquejarCorriol();
        avisar("La boira baixa de cop i el corriol per on has pujat desapareix darrere teu.");
    }

    // mira si s'ha arribat a algun final
    public EstatPartida comprovarFinal() {
        if (estat.esFinal()) {
            return estat;
        }
        // trobar-se el senglar no mata de cop: dona un torn per treure la poma o fugir
        if (trobatAmbSenglar()) {
            tornsAmbSenglar++;
            if (tornsAmbSenglar >= 2) {
                estat = EstatPartida.DERROTA_SENGLAR;
                dejaVu = true;
                return estat;
            }
        } else {
            tornsAmbSenglar = 0;
        }
        if (jugador.getZonaActual() == mapa.getZonaInicial() && boiraActivada) {
            PersonatgeFix tomeu = mapa.getTomeu();
            boolean totesLesOfrenes = tomeu != null && tomeu.haRebutTot();
            estat = totesLesOfrenes ? EstatPartida.FINAL_SECRET : EstatPartida.VICTORIA;
            if (estat == EstatPartida.FINAL_SECRET) {
                recordDelPuig = true;
            }
            return estat;
        }
        if (rellotge.sHaAcabatElTemps()) {
            estat = EstatPartida.DERROTA_TEMPS;
            return estat;
        }
        return estat;
    }

    private boolean trobatAmbSenglar() {
        return senglar != null && !senglar.estaDistret()
            && senglar.getZonaActual() == jugador.getZonaActual();
    }

    private void avisarSenglar() {
        avisar("El senglar et barra el pas, esbufegant. No et treu els ulls de sobre.\n"
             + "Tens un moment per fer alguna cosa abans que carregui.");
    }

    public void reiniciar() {
        iniciarPartida();
    }

    // TEXT

    // descripcio de la zona on es el jugador
    public String descriureZonaActual() {
        Zona z = jugador.getZonaActual();
        if (z.esFosca() && !jugador.teLlumEncesa()) {
            return "== " + z.getNom() + " ==\nEsta tot fosc. No s'hi veu absolutament res.\n"
                 + "\nHauries d'encendre alguna cosa abans de moure't.";
        }
        StringBuilder sb = new StringBuilder(z.descriure(rellotge.getFaseDelDia()));
        if (!z.getElements().isEmpty()) {
            sb.append("\nHi veus: ");
            for (int i = 0; i < z.getElements().size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(z.getElements().get(i).getNom());
            }
        }
        // la tornada es la part que mes costa d'endevinar: marquem per on es baixa
        String tornada = direccioCapAlBus();
        if (tornada != null) {
            Connexio c = z.getConnexio(tornada);
            sb.append("\nCap al bus: ").append(tornada);
            if (c != null) {
                sb.append(" (").append(c.getDesti().getNom()).append(')');
            }
        }
        return sb.toString();
    }

    public String textIntroduccio() {
        return "L'EXCURSIO DEL PUIG DE LES BRUIXES\n"
             + "----------------------------------\n"
             + "Just quan prems el disparador, un soroll sec ressona entre els arbres.\n"
             + "Alces el cap: el grup ha desaparegut corriol amunt i la boira ha comencat\n"
             + "a baixar, espessa i freda. No hi ha ningu. Nomes tu, la motxilla i un bosc\n"
             + "que, juraries, no tenia aquest aspecte fa dos minuts.\n\n"
             + "El bus marxa a les 18:00. Escriu OBJECTIUS per saber que has de fer\n"
             + "i AJUDA per veure com dir-ho.\n"
             + (objectiuActual() != null ? "\nNou objectiu: " + objectiuActual() + "\n" : "")
             + (dejaVu ? "\nUn flaix confus et travessa el cap: uns ullals, la fosca, un gruny.\n"
                       + "Aixo ja ho has viscut. Aquesta vegada ves amb compte amb el senglar.\n" : "")
             + (recordDelPuig ? "\nA la motxilla hi trobes un palet blanc que no recordes haver-hi posat.\n" : "");
    }

    public String textFinal() {
        switch (estat) {
            case VICTORIA:
                return "Arribes just quan el Bernat tanca la porta del bus. La Mireia et mira\n"
                     + "al.lucinada: \"Se't pot deixar sol dos minuts o que?!\"";
            case FINAL_SECRET:
                return "En Tomeu et posa una ma a l'espatlla i assenyala l'ermita.\n"
                     + "\"Aquesta nit et quedes. El Puig no deixa marxar qui sap ser hospitalari.\"";
            case DERROTA_TEMPS:
                return "El bus marxa sense tu. Ara toca trucar als teus pares... i explicar-los\n"
                     + "per que fas tard.";
            case DERROTA_SENGLAR:
                return "El senglar no estava d'humor per a visites.\n"
                     + "Et despertes just abans que baixi la boira, amb un flaix confus del que\n"
                     + "ha passat. Aixo... ja ho he viscut?";
            default:
                return "";
        }
    }

    // OBJECTIUS

    // les fites del joc, deduides de l'estat del mon i no d'una llista de marques. aixi no es
    // poden desincronitzar: si el jugador deixa la navalla, la fita del cofre segueix feta
    // perque el que es mira es si el cofre ja es obert
    public ArrayList<Objectiu> objectius() {
        Inventari inv = jugador.getInventari();
        Zona cova = mapa.getZona("Cova Fosca");
        Zona pont = mapa.getZona("Pont Penjant");
        Zona ermita = mapa.getZona("Ermita");
        Zona refugi = mapa.getZona("Refugi");

        boolean llum = inv.conte("llanterna");
        boolean cofreObert = contenidorObert(refugi, "cofre");
        boolean pontFet = esPasObert(pont, "nord");
        boolean teMapa = inv.conte("mapa");
        boolean drecera = esPasObert(refugi, "sud");
        boolean covaExplorada = inv.conte("navalla") || cofreObert || pontFet;

        ArrayList<Objectiu> llista = new ArrayList<>();
        llista.add(new Objectiu("Trobar alguna cosa per veure-hi a les fosques",
            true, llum));
        llista.add(new Objectiu("Explorar la cova fosca i veure que hi ha entre les pedres",
            llum || esVisitada(cova), covaExplorada));
        llista.add(new Objectiu("Obrir el cofre del refugi",
            esVisitada(refugi), cofreObert || pontFet));
        llista.add(new Objectiu("Reforcar el pont penjant per poder pujar al cim",
            esVisitada(pont), pontFet));
        llista.add(new Objectiu("Aconseguir el mapa d'en Tomeu: no el regala, el canvia",
            esVisitada(ermita), teMapa || drecera));
        llista.add(new Objectiu("Obrir la drecera del refugi i baixar al Camp Base",
            teMapa, estat == EstatPartida.VICTORIA));
        return llista;
    }

    // CAMI DE TORNADA

    // la direccio que has de fer servir ara per anar cap al bus, o null si de moment no hi ha cami.
    // mentre la boira tingui tallat el corriol no en troba cap, aixi que la pista nomes apareix
    // quan la tornada ja es possible de debo
    public String direccioCapAlBus() {
        Zona desti = mapa.getZonaInicial();
        Zona origen = jugador.getZonaActual();
        // abans que caigui la boira encara no s'ha perdut ningu: no cal cap pista
        if (origen == desti || !boiraActivada) {
            return null;
        }
        // cerca en amplada: de cada zona en guardem la primera direccio del cami que hi porta
        ArrayDeque<Zona> cua = new ArrayDeque<>();
        HashMap<Zona, String> primerPas = new HashMap<>();
        cua.add(origen);
        primerPas.put(origen, null);
        while (!cua.isEmpty()) {
            Zona z = cua.poll();
            for (Connexio c : z.getConnexions()) {
                if (!c.esVisible() || !c.esTransitable(jugador)) {
                    continue;
                }
                Zona seguent = c.getDesti();
                if (primerPas.containsKey(seguent)) {
                    continue;
                }
                // si encara som a la zona d'origen, el primer pas es aquesta direccio.
                // si no, arrosseguem el que ja portava la zona d'on venim
                String pas = z == origen ? c.getDireccio() : primerPas.get(z);
                if (seguent == desti) {
                    return pas;
                }
                primerPas.put(seguent, pas);
                cua.add(seguent);
            }
        }
        return null;
    }

    // el text de la fita que toca ara, o null si no en queda cap de descoberta
    public String objectiuActual() {
        for (Objectiu o : objectius()) {
            if (o.esPendent()) {
                return o.getText();
            }
        }
        return null;
    }

    // el que es mostra quan el jugador escriu OBJECTIUS
    public String llistarObjectius() {
        StringBuilder sb = new StringBuilder("OBJECTIUS\n");
        String actual = objectiuActual();
        boolean ocults = false;
        for (Objectiu o : objectius()) {
            if (!o.esDescobert()) {
                ocults = true;
                continue;
            }
            boolean esAra = actual != null && actual.equals(o.getText());
            sb.append("  ").append(o.marca(esAra)).append(' ').append(o.getText()).append('\n');
        }
        if (ocults) {
            sb.append("  [?] Encara no saps que mes et caldra.\n");
        }
        sb.append("El bus marxa a les 18:00 i et queden ")
          .append(rellotge.minutsRestants()).append(" minuts.");
        return sb.toString();
    }

    private boolean esVisitada(Zona z) {
        return z != null && z.esVisitada();
    }

    // una sortida que el jugador ja veu i pot fer servir
    private boolean esPasObert(Zona z, String direccio) {
        if (z == null) {
            return false;
        }
        Connexio c = z.getConnexio(direccio);
        return c != null && c.esVisible();
    }

    // un contenidor d'una zona que ja s'ha obert
    private boolean contenidorObert(Zona z, String nom) {
        if (z == null) {
            return false;
        }
        Element e = z.cercarElement(nom);
        return e instanceof Contenidor && ((Contenidor) e).estaObert();
    }

    private void avisar(String text) {
        if (avisos.length() > 0) {
            avisos.append('\n');
        }
        avisos.append(text);
    }

    private String recollirAvisos() {
        String s = avisos.toString();
        avisos.setLength(0);
        return s;
    }

    // GETTERS

    public MapaJoc getMapa() {
        return mapa;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Rellotge getRellotge() {
        return rellotge;
    }

    public Album getAlbum() {
        return album;
    }

    public PersonatgeMobil getSenglar() {
        return senglar;
    }

    public ArrayList<PersonatgeFix> getAliats() {
        return aliats;
    }

    public EstatPartida getEstat() {
        return estat;
    }

    public int getTorns() {
        return torns;
    }

    public boolean esBoiraActivada() {
        return boiraActivada;
    }

    public FaseDelDia getFaseDelDia() {
        return rellotge.getFaseDelDia();
    }
}
