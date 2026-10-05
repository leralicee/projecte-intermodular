package control;

import excepcions.AccioNoPermesaException;
import excepcions.JocException;
import excepcions.ObjecteNoTrobatException;
import model.Album;
import model.Camera;
import model.Connexio;
import model.Contenidor;
import model.Element;
import model.Encenible;
import model.Inventari;
import model.Jugador;
import model.Objecte;
import model.Personatge;
import model.PersonatgeFix;
import model.Usable;
import model.Zona;

// executa una ordre ja analitzada i aplica les regles del joc (un metode per verb)
public class MotorDeJoc {

    private final Joc joc;

    public MotorDeJoc(Joc joc) {
        this.joc = joc;
    }

    public ResultatAccio executar(Ordre o) throws JocException {
        switch (o.getVerb()) {
            case ANAR:      return anar(o.getComplement1());
            case MIRAR:     return mirar(o.getComplement1());
            case INVENTARI: return inventari();
            case AGAFAR:    return agafar(o.getComplement1());
            case DEIXAR:    return deixar(o.getComplement1(), o.getComplement2());
            case USAR:      return usar(o.getComplement1(), o.getComplement2());
            case OBRIR:     return obrir(o.getComplement1());
            case TANCAR:    return tancar(o.getComplement1());
            case ENCENDRE:  return encendre(o.getComplement1());
            case APAGAR:    return apagar(o.getComplement1());
            case PARLAR:    return parlar(o.getComplement1(), o.getComplement2());
            case FER_FOTO:  return ferFoto(o.getComplement1());
            default:        return ResultatAccio.error("Encara no se fer aixo.");
        }
    }

    // MOVIMENT

    private ResultatAccio anar(String desti) throws JocException {
        if (desti == null) {
            return ResultatAccio.error("On vols anar? Prova ANAR NORD o ANAR A LA CLARIANA.");
        }
        Zona actual = joc.getJugador().getZonaActual();
        Connexio c = actual.getConnexio(desti);
        if (c == null) {
            throw new AccioNoPermesaException("Sortida inexistent: " + desti,
                "Des d'aqui no hi ha cap cami cap a " + desti + ".");
        }
        if (c.esSecreta() && !joc.getJugador().teLlumEncesa()) {
            throw new AccioNoPermesaException("Sortida inexistent: " + desti,
                "Des d'aqui no hi ha cap cami cap a " + desti + ".");
        }
        if (!c.esTransitable(joc.getJugador())) {
            throw new AccioNoPermesaException("Connexio tancada cap a " + desti, c.getMotiuTancada());
        }
        joc.getJugador().moureA(c.getDesti());
        joc.enEntrarAZona(c.getDesti());
        return ResultatAccio.ok("Vas cap a " + c.getDesti().getNom() + ".");
    }

    private ResultatAccio mirar(String que) throws JocException {
        Zona z = joc.getJugador().getZonaActual();
        if (que == null) {
            return ResultatAccio.okSenseTemps(joc.descriureZonaActual());
        }
        Element e = cercar(que);
        if (e == null) {
            throw new ObjecteNoTrobatException(que);
        }
        if (!hiVeu(z)) {
            return ResultatAccio.error("Esta massa fosc per veure res.");
        }
        return ResultatAccio.okSenseTemps(e.descriure());
    }

    private ResultatAccio inventari() {
        return ResultatAccio.okSenseTemps(joc.getJugador().getInventari().llistar());
    }

    // OBJECTES

    private ResultatAccio agafar(String nom) throws JocException {
        if (nom == null) {
            return ResultatAccio.error("Que vols agafar?");
        }
        Zona z = joc.getJugador().getZonaActual();
        if (!hiVeu(z)) {
            throw new AccioNoPermesaException("Zona fosca sense llum",
                "A les fosques no trobaries res. Et caldria llum.");
        }
        Element e = z.cercarElement(nom);
        if (e == null) {
            throw new ObjecteNoTrobatException(nom);
        }
        if (!(e instanceof Objecte)) {
            throw new AccioNoPermesaException("Element no agafable: " + e.getNom(),
                "No pots endur-te " + e.getNom() + ".");
        }
        Objecte o = (Objecte) e;
        if (!o.esAgafable()) {
            throw new AccioNoPermesaException("Objecte no agafable: " + o.getNom(),
                "No pots endur-te " + o.getNom() + ".");
        }
        Inventari inv = joc.getJugador().getInventari();
        if (inv.esPle()) {
            throw new AccioNoPermesaException("Inventari ple",
                "La motxilla es plena. Hauries de deixar alguna cosa.");
        }
        z.treureElement(o.getNom());
        inv.afegir(o);
        return ResultatAccio.ok("Agafes " + o.getNom() + ".");
    }

    private ResultatAccio deixar(String nom, String aQui) throws JocException {
        if (nom == null) {
            return ResultatAccio.error("Que vols deixar?");
        }
        Inventari inv = joc.getJugador().getInventari();
        Objecte o = inv.cercar(nom);
        if (o == null) {
            throw new ObjecteNoTrobatException(nom);
        }
        // deixar alguna cosa a un personatge es una ofrena
        String desti = aQui != null ? aQui : nom;
        Element destinatari = joc.getJugador().getZonaActual().cercarElement(desti);
        if (aQui != null && destinatari instanceof PersonatgeFix) {
            PersonatgeFix p = (PersonatgeFix) destinatari;
            // si no la vol, et quedes l'objecte
            if (!p.accepta(o)) {
                return ResultatAccio.error(p.rebreOfrena(o));
            }
            inv.treure(o.getNom());
            StringBuilder sb = new StringBuilder(p.rebreOfrena(o));
            Objecte premi = p.lliurarRecompensa();
            if (premi != null) {
                inv.afegir(premi);
                sb.append("\nTe dona ").append(premi.getNom()).append(".");
            }
            return ResultatAccio.ok(sb.toString());
        }
        inv.treure(o.getNom());
        joc.getJugador().getZonaActual().afegirElement(o);
        return ResultatAccio.ok("Deixes " + o.getNom() + " a " + joc.getJugador().getZonaActual().getNom() + ".");
    }

    private ResultatAccio usar(String queNom, String ambNom) throws JocException {
        if (queNom == null) {
            return ResultatAccio.error("Que vols fer servir?");
        }
        Element que = cercar(queNom);
        if (que == null) {
            throw new ObjecteNoTrobatException(queNom);
        }
        if (!(que instanceof Usable)) {
            return ResultatAccio.error(que.getNom() + " no es fa servir amb res.");
        }
        // el mapa es fa servir tot sol i marca la drecera
        if (que.getNom().equalsIgnoreCase("mapa") && ambNom == null) {
            if (joc.getMapa().obrirDrecera()) {
                return ResultatAccio.ok("Despleges el mapa. Hi ha una drecera marcada que baixa\n"
                                      + "del refugi fins al corriol, per sota de la boira.");
            }
            return ResultatAccio.okSenseTemps("Tornes a mirar el mapa. La drecera ja la tens localitzada.");
        }
        if (ambNom == null) {
            return ResultatAccio.error("Amb que vols fer servir " + que.getNom() + "?");
        }
        Element amb = cercar(ambNom);
        if (amb == null) {
            throw new ObjecteNoTrobatException(ambNom);
        }
        ResultatAccio especial = usQueCanviaElMon(que, amb);
        if (especial != null) {
            return especial;
        }
        return ((Usable) que).usarAmb(amb);
    }

    // usos que canvien el mapa (fer caure la poma, reforcar el pont...). null si no es cap
    private ResultatAccio usQueCanviaElMon(Element que, Element amb) throws JocException {
        String a = que.getNom().toLowerCase();
        String b = amb.getNom().toLowerCase();
        Zona zona = joc.getJugador().getZonaActual();

        // fer servir la clau d'un contenidor es una altra manera d'obrir-lo
        if (amb instanceof Contenidor && a.equalsIgnoreCase(((Contenidor) amb).getClauNecessaria())) {
            Contenidor contenidor = (Contenidor) amb;
            ResultatAccio r = contenidor.obrir(joc.getJugador());
            if (r.esCorrecta()) {
                for (Objecte o : contenidor.buidar()) {
                    zona.afegirElement(o);
                }
            }
            return r;
        }

        if (a.equals("basto") && b.equals("pomer")) {
            if (zona.cercarElement("poma") != null) {
                return ResultatAccio.error("Ja has fet caure la poma.");
            }
            zona.afegirElement(new Objecte("poma",
                "Una poma vermella, una mica bonyeguda per la caiguda."));
            return ResultatAccio.ok("Piques la branca amb el basto i la poma cau a terra.");
        }

        if (a.equals("corda") && b.equals("pont")) {
            Connexio capAlCim = zona.getConnexio("nord");
            if (capAlCim == null) {
                return null;
            }
            if (capAlCim.esTransitable(joc.getJugador())) {
                return ResultatAccio.error("El pont ja esta reforcat.");
            }
            capAlCim.obrir();
            joc.getJugador().getInventari().treure("corda");
            return ResultatAccio.ok("Lligues la corda als muntants i reforces els taulons que falten.\n"
                                  + "Ara el pont aguantaria el teu pes.");
        }

        if (a.equals("poma") && b.equals("senglar")) {
            if (joc.getSenglar().estaDistret()) {
                return ResultatAccio.error("El senglar ja te prou feina amb el que li has donat.");
            }
            Objecte poma = joc.getJugador().getInventari().cercar("poma");
            if (poma == null || !joc.getSenglar().distreure(poma)) {
                return ResultatAccio.error("No portes la poma.");
            }
            joc.getJugador().getInventari().treure("poma");
            return ResultatAccio.ok("Llences la poma tan lluny com pots. El senglar hi va al darrere\n"
                                  + "bufant i ja no et torna a fer cas.");
        }

        if (a.startsWith("cantimplora") && (b.equals("torrent") || b.equals("riu"))) {
            if (a.contains("plena")) {
                return ResultatAccio.error("La cantimplora ja es plena.");
            }
            Inventari inv = joc.getJugador().getInventari();
            if (inv.treure("cantimplora") == null) {
                return ResultatAccio.error("Hauries de portar la cantimplora a sobre.");
            }
            // la canvia per una de plena, que es l'unica que en Tomeu accepta
            inv.afegir(new Objecte("cantimplora plena",
                "La teva cantimplora, ara plena d'aigua freda del torrent."));
            return ResultatAccio.ok("Omples la cantimplora amb aigua del torrent.");
        }

        // darrere la cortina d'aigua hi ha el pas cap a la cova, pero cal llum per veure'l
        if (a.equals("llanterna") && b.equals("cortina")) {
            Connexio pas = zona.getConnexio("oest");
            if (pas == null) {
                return null;
            }
            if (!joc.getJugador().teLlumEncesa()) {
                return ResultatAccio.error("Amb la llanterna apagada no hi veus res, darrere l'aigua.");
            }
            if (pas.esVisible()) {
                return ResultatAccio.error("El pas de darrere l'aigua ja el tens localitzat.");
            }
            pas.obrir();
            // un cop trobat, tampoc es amagat des de l'altra banda
            Connexio tornada = pas.getDesti().getConnexio("est");
            if (tornada != null) {
                tornada.obrir();
            }
            return ResultatAccio.ok("Il.lumines darrere la cortina d'aigua: hi ha un forat\n"
                                  + "prou gran per passar-hi de costat.");
        }

        return null;
    }

    private ResultatAccio obrir(String nom) throws JocException {
        if (nom == null) {
            return ResultatAccio.error("Que vols obrir?");
        }
        Element e = exigirElement(nom);
        if (e == null) {
            throw new ObjecteNoTrobatException(nom);
        }
        if (e.getNom().equalsIgnoreCase("pont")) {
            return passDelPont(true);
        }
        if (e instanceof Contenidor) {
            ResultatAccio r = ((Contenidor) e).obrir(joc.getJugador());
            if (r.esCorrecta()) {
                for (Objecte o : ((Contenidor) e).buidar()) {
                    joc.getJugador().getZonaActual().afegirElement(o);
                }
            }
            return r;
        }
        return e.interactuar(Verb.OBRIR, joc.getJugador());
    }

    private ResultatAccio tancar(String nom) throws JocException {
        if (nom == null) {
            return ResultatAccio.error("Que vols tancar?");
        }
        Element e = exigirElement(nom);
        if (e == null) {
            throw new ObjecteNoTrobatException(nom);
        }
        if (e.getNom().equalsIgnoreCase("pont")) {
            return passDelPont(false);
        }
        return e.interactuar(Verb.TANCAR, joc.getJugador());
    }

    // al pont pots abaixar els taulons darrere teu, i tornar-los a posar quan vulguis
    private ResultatAccio passDelPont(boolean obrir) throws JocException {
        Connexio tornada = joc.getJugador().getZonaActual().getConnexio("sud");
        if (tornada == null) {
            throw new AccioNoPermesaException("El pont no te pas de tornada", "Aqui no hi ha res a tancar.");
        }
        if (obrir) {
            if (tornada.esVisible()) {
                return ResultatAccio.error("El pas ja es obert.");
            }
            tornada.obrir();
            return ResultatAccio.ok("Tornes a posar els taulons. El pas cap a la cova queda obert.");
        }
        if (!tornada.esVisible()) {
            return ResultatAccio.error("El pas ja es tancat.");
        }
        tornada.tancar();
        tornada.setMotiuTancada("Has abaixat els taulons: per aqui ja no es pot tornar fins que els tornis a posar.");
        return ResultatAccio.ok("Abaixes els taulons darrere teu. Res no et podra seguir pel pont.");
    }

    private ResultatAccio encendre(String nom) throws JocException {
        Element e = exigirElement(nom == null ? "llanterna" : nom);
        if (e == null) {
            return ResultatAccio.error("No portes res per fer llum.");
        }
        if (!(e instanceof Encenible)) {
            return ResultatAccio.error(e.getNom() + " no s'encen.");
        }
        return e.interactuar(Verb.ENCENDRE, joc.getJugador());
    }

    private ResultatAccio apagar(String nom) throws JocException {
        Element e = exigirElement(nom == null ? "llanterna" : nom);
        if (e == null) {
            return ResultatAccio.error("No portes res per apagar.");
        }
        if (!(e instanceof Encenible)) {
            return ResultatAccio.error(e.getNom() + " no s'apaga.");
        }
        return e.interactuar(Verb.APAGAR, joc.getJugador());
    }

    private ResultatAccio parlar(String ambQui, String sobreQue) throws JocException {
        Zona z = joc.getJugador().getZonaActual();
        Element e = ambQui == null ? primerPersonatge(z) : z.cercarElement(ambQui);
        if (!(e instanceof Personatge)) {
            throw new AccioNoPermesaException("Cap personatge a la zona",
                "Aqui no hi ha ningu amb qui parlar.");
        }
        Personatge p = (Personatge) e;
        String tema = sobreQue != null ? sobreQue : ambQui;
        return ResultatAccio.ok(p.getNom() + ": " + p.parlar(tema));
    }

    private ResultatAccio ferFoto(String que) throws JocException {
        Camera camera = (Camera) joc.getJugador().getInventari().cercar("camera");
        if (camera == null) {
            return ResultatAccio.error("No portes la camera.");
        }
        Zona z = joc.getJugador().getZonaActual();
        if (!hiVeu(z)) {
            return ResultatAccio.error("Sense llum la foto sortiria negra.");
        }
        camera.ferFoto(z, joc.getRellotge());
        Album a = joc.getAlbum();
        return ResultatAccio.ok("Fas una foto. " + a.getProgres());
    }

    // AJUDES

    // busca primer a la zona i despres a l'inventari
    private Element cercar(String nom) {
        if (nom == null) {
            return null;
        }
        Element e = joc.getJugador().getZonaActual().cercarElement(nom);
        if (e != null) {
            return e;
        }
        return joc.getJugador().getInventari().cercar(nom);
    }

    private Element exigirElement(String nom) {
        return nom == null ? null : cercar(nom);
    }

    private Element primerPersonatge(Zona z) {
        for (Element e : z.getElements()) {
            if (e instanceof Personatge) {
                return e;
            }
        }
        return null;
    }

    // a les zones fosques cal portar la llanterna encesa
    private boolean hiVeu(Zona z) {
        return !z.esFosca() || joc.getJugador().teLlumEncesa();
    }
}