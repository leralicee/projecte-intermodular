package control;

import excepcions.JocException;
import model.Album;
import model.Camera;
import model.EstatPartida;
import model.FaseDelDia;
import model.Jugador;
import model.MapaJoc;
import model.PersonatgeFix;
import model.PersonatgeMobil;
import model.Rellotge;
import model.Zona;

import java.util.ArrayList;

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
        ResultatAccio r;
        try {
            Ordre o = analitzador.analitzar(text);
            r = motor.executar(o);
        } catch (JocException e) {
            r = ResultatAccio.error(e.getMissatgeJugador());
        }

        if (r.consumeixTemps()) {
            avancarTorn();
        }
        comprovarFinal();

        String extra = recollirAvisos();
        if (!extra.isEmpty()) {
            r = new ResultatAccio(r.getText() + "\n" + extra, r.esCorrecta(), r.consumeixTemps());
        }
        return r;
    }

    // passa el temps i mou el senglar si toca
    public void avancarTorn() {
        torns++;
        rellotge.avancar();
        if (senglar != null && torns % PersonatgeMobil.CADA_N_TORNS == 0) {
            senglar.moure();
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
        if (senglar != null && !senglar.estaDistret()
                && senglar.getZonaActual() == jugador.getZonaActual()) {
            tornsAmbSenglar++;
            if (tornsAmbSenglar >= 2) {
                estat = EstatPartida.DERROTA_SENGLAR;
                return estat;
            }
            avisar("El senglar et barra el pas, esbufegant. No et treu els ulls de sobre.\n"
                 + "Tens un moment per fer alguna cosa abans que carregui.");
        } else {
            tornsAmbSenglar = 0;
        }
        if (jugador.getZonaActual() == mapa.getZonaInicial() && boiraActivada) {
            PersonatgeFix tomeu = mapa.getTomeu();
            boolean totesLesOfrenes = tomeu != null && tomeu.haRebutTot();
            estat = totesLesOfrenes ? EstatPartida.FINAL_SECRET : EstatPartida.VICTORIA;
            return estat;
        }
        if (rellotge.sHaAcabatElTemps()) {
            estat = EstatPartida.DERROTA_TEMPS;
            return estat;
        }
        return estat;
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
        return sb.toString();
    }

    public String textIntroduccio() {
        return "L'EXCURSIO DEL PUIG DE LES BRUIXES\n"
             + "----------------------------------\n"
             + "Just quan prems el disparador, un soroll sec ressona entre els arbres.\n"
             + "Alces el cap: el grup ha desaparegut corriol amunt i la boira ha comencat\n"
             + "a baixar, espessa i freda. No hi ha ningu. Nomes tu, la motxilla i un bosc\n"
             + "que, juraries, no tenia aquest aspecte fa dos minuts.\n\n"
             + "El bus marxa a les 18:00. Escriu AJUDA per veure que pots fer.\n";
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
