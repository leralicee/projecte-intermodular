package control;

import model.Zona;
import vista.VistaConsola;
import vista.VistaGrafica;

// punt d'entrada. sense arguments obre la finestra, amb "consola" la versio de text
public class Main {

    // ordres del programa, no del joc
    public static final String AJUDA =
          "Verbs que entenc:\n"
        + "  ANAR <direccio o zona>     ENCENDRE <objecte>    USAR <a> AMB <b>\n"
        + "  DEIXAR <objecte>           APAGAR <objecte>      PARLAR AMB <personatge>\n"
        + "  AGAFAR <objecte>           OBRIR <objecte>       TANCAR <objecte>\n"
        + "  MIRAR [objecte]            INVENTARI             FOTO\n"
        + "Tambe pots escriure nomes la direccio: NORD, SUD, EST, OEST.\n"
        + "AJUDA torna a mostrar aixo. SORTIR abandona la partida.";

    public static void main(String[] args) {
        boolean consola = args.length > 0 && args[0].equalsIgnoreCase("consola");
        if (consola) {
            jugarPerConsola();
        } else {
            new VistaGrafica(new Joc()).obrir();
        }
    }

    private static void jugarPerConsola() {
        Joc joc = new Joc();
        VistaConsola vista = new VistaConsola();

        vista.mostrarText(joc.textIntroduccio());
        vista.mostrarZona(joc.getJugador().getZonaActual(), joc.getFaseDelDia(),
                          joc.descriureZonaActual());

        while (!joc.getEstat().esFinal()) {
            String entrada = vista.llegirOrdre();
            String meta = entrada == null ? "" : entrada.trim().toLowerCase();

            if (meta.equals("sortir") || meta.equals("surt")) {
                vista.mostrarText("Deixes la partida a mitges.");
                vista.tancar();
                return;
            }
            if (meta.equals("ajuda") || meta.equals("help") || meta.equals("?")) {
                vista.mostrarText(AJUDA);
                continue;
            }
            if (meta.isEmpty()) {
                continue;
            }

            Zona anterior = joc.getJugador().getZonaActual();
            ResultatAccio r = joc.processarEntrada(entrada);
            vista.mostrarText("[" + joc.getRellotge().getHoraFormatada() + "] " + r.getText());

            if (joc.getJugador().getZonaActual() != anterior && !joc.getEstat().esFinal()) {
                vista.mostrarZona(joc.getJugador().getZonaActual(), joc.getFaseDelDia(),
                                  joc.descriureZonaActual());
            }
        }

        vista.mostrarFinal(joc.getEstat(), joc.getAlbum(), joc.textFinal());
        vista.tancar();
    }
}
