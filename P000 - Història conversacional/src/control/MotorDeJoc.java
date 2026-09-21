package control;

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

    public ResultatAccio executar(Ordre o) {
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

    private ResultatAccio anar(String desti) {
        if (desti == null) {
            return ResultatAccio.error("On vols anar? Prova ANAR NORD o ANAR A LA CLARIANA.");
        }
        Zona actual = joc.getJugador().getZonaActual();
        Connexio c = actual.getConnexio(desti);
        if (c == null) {
            return ResultatAccio.error("Des d'aqui no hi ha cap cami cap a " + desti + ".");
        }
        if (c.esSecreta() && !joc.getJugador().teLlumEncesa()) {
            return ResultatAccio.error("Des d'aqui no hi ha cap cami cap a " + desti + ".");
        }
        if (!c.esTransitable(joc.getJugador())) {
            return ResultatAccio.error(c.getMotiuTancada());
        }
        joc.getJugador().moureA(c.getDesti());
        joc.enEntrarAZona(c.getDesti());
        return ResultatAccio.ok("Vas cap a " + c.getDesti().getNom() + ".");
    }

    private ResultatAccio mirar(String que) {
        Zona z = joc.getJugador().getZonaActual();
        if (que == null) {
            return ResultatAccio.okSenseTemps(joc.descriureZonaActual());
        }
        Element e = cercar(que);
        if (e == null) {
            return ResultatAccio.error("Aqui no veig cap " + que + ".");
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

    private ResultatAccio agafar(String nom) {
        if (nom == null) {
            return ResultatAccio.error("Que vols agafar?");
        }
        Zona z = joc.getJugador().getZonaActual();
        if (!hiVeu(z)) {
            return ResultatAccio.error("A les fosques no trobaries res. Et caldria llum.");
        }
        Element e = z.cercarElement(nom);
        if (e == null) {
            return ResultatAccio.error("Aqui no veig cap " + nom + ".");
        }
        if (!(e instanceof Objecte)) {
            return ResultatAccio.error("No pots endur-te " + e.getNom() + ".");
        }
        Objecte o = (Objecte) e;
        if (!o.esAgafable()) {
            return ResultatAccio.error("No pots endur-te " + o.getNom() + ".");
        }
        Inventari inv = joc.getJugador().getInventari();
        if (inv.esPle()) {
            return ResultatAccio.error("La motxilla es plena. Hauries de deixar alguna cosa.");
        }
        z.treureElement(o.getNom());
        inv.afegir(o);
        return ResultatAccio.ok("Agafes " + o.getNom() + ".");
    }

    private ResultatAccio deixar(String nom, String aQui) {
        if (nom == null) {
            return ResultatAccio.error("Que vols deixar?");
        }
        Inventari inv = joc.getJugador().getInventari();
        Objecte o = inv.cercar(nom);
        if (o == null) {
            return ResultatAccio.error("No portes cap " + nom + ".");
        }
        // deixar alguna cosa a un personatge es una ofrena
        String desti = aQui != null ? aQui : nom;
        Element destinatari = joc.getJugador().getZonaActual().cercarElement(desti);
        if (aQui != null && destinatari instanceof PersonatgeFix) {
            PersonatgeFix p = (PersonatgeFix) destinatari;
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

    private ResultatAccio usar(String queNom, String ambNom) {
        if (queNom == null) {
            return ResultatAccio.error("Que vols fer servir?");
        }
        Element que = cercar(queNom);
        if (que == null) {
            return ResultatAccio.error("No tens cap " + queNom + ".");
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
            return ResultatAccio.error("Aqui no veig cap " + ambNom + ".");
        }
        ResultatAccio especial = usQueCanviaElMon(que, amb);
        if (especial != null) {
            return especial;
        }
        return ((Usable) que).usarAmb(amb);
    }

    // usos que canvien el mapa (fer caure la poma, reforcar el pont...). null si no es cap
    private ResultatAccio usQueCanviaElMon(Element que, Element amb) {
        String a = que.getNom().toLowerCase();
        String b = amb.getNom().toLowerCase();
        Zona zona = joc.getJugador().getZonaActual();

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

        if (a.equals("cantimplora") && (b.equals("torrent") || b.equals("riu"))) {
            return ResultatAccio.ok("Omples la cantimplora amb aigua del torrent.");
        }

        return null;
    }

    private ResultatAccio obrir(String nom) {
        Element e = exigirElement(nom);
        if (e == null) {
            return ResultatAccio.error(nom == null ? "Que vols obrir?" : "Aqui no veig cap " + nom + ".");
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

    private ResultatAccio tancar(String nom) {
        Element e = exigirElement(nom);
        if (e == null) {
            return ResultatAccio.error(nom == null ? "Que vols tancar?" : "Aqui no veig cap " + nom + ".");
        }
        return e.interactuar(Verb.TANCAR, joc.getJugador());
    }

    private ResultatAccio encendre(String nom) {
        Element e = exigirElement(nom == null ? "llanterna" : nom);
        if (e == null) {
            return ResultatAccio.error("No portes res per fer llum.");
        }
        if (!(e instanceof Encenible)) {
            return ResultatAccio.error(e.getNom() + " no s'encen.");
        }
        return e.interactuar(Verb.ENCENDRE, joc.getJugador());
    }

    private ResultatAccio apagar(String nom) {
        Element e = exigirElement(nom == null ? "llanterna" : nom);
        if (e == null) {
            return ResultatAccio.error("No portes res per apagar.");
        }
        if (!(e instanceof Encenible)) {
            return ResultatAccio.error(e.getNom() + " no s'apaga.");
        }
        return e.interactuar(Verb.APAGAR, joc.getJugador());
    }

    private ResultatAccio parlar(String ambQui, String sobreQue) {
        Zona z = joc.getJugador().getZonaActual();
        Element e = ambQui == null ? primerPersonatge(z) : z.cercarElement(ambQui);
        if (!(e instanceof Personatge)) {
            return ResultatAccio.error("Aqui no hi ha ningu amb qui parlar.");
        }
        Personatge p = (Personatge) e;
        String tema = sobreQue != null ? sobreQue : ambQui;
        return ResultatAccio.ok(p.getNom() + ": " + p.parlar(tema));
    }

    private ResultatAccio ferFoto(String que) {
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
