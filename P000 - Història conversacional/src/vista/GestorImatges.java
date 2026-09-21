package vista;

import javax.swing.ImageIcon;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

// carrega les imatges i les guarda en memoria. les busca al classpath i despres al disc, aixi funciona des de l'ide i des de la consola. si no la troba retorna null
public class GestorImatges {

    // carpeta dins del classpath
    public static final String RUTA_CLASSPATH = "/img/";

    // on buscar-la al disc (en ordre)
    private static final String[] RUTES_DISC = {
        "src/recursos/img/",
        "recursos/img/",
        "P000 - Història conversacional/src/recursos/img/",
        "../src/recursos/img/"
    };

    private final HashMap<String, ImageIcon> cache = new HashMap<>();

    // la imatge, o null si no la troba enlloc
    public ImageIcon carregar(String nomFitxer) {
        if (nomFitxer == null) {
            return null;
        }
        if (cache.containsKey(nomFitxer)) {
            return cache.get(nomFitxer);
        }
        ImageIcon icona = buscarAlClasspath(nomFitxer);
        if (icona == null) {
            icona = buscarAlDisc(nomFitxer);
        }
        cache.put(nomFitxer, icona);
        return icona;
    }

    private ImageIcon buscarAlClasspath(String nomFitxer) {
        URL url = getClass().getResource(RUTA_CLASSPATH + nomFitxer);
        if (url == null) {
            return null;
        }
        ImageIcon icona = new ImageIcon(url);
        return icona.getIconWidth() > 0 ? icona : null;
    }

    private ImageIcon buscarAlDisc(String nomFitxer) {
        for (String ruta : RUTES_DISC) {
            File f = new File(ruta + nomFitxer);
            if (f.isFile()) {
                ImageIcon icona = new ImageIcon(f.getAbsolutePath());
                if (icona.getIconWidth() > 0) {
                    return icona;
                }
            }
        }
        return null;
    }

    public boolean teImatge(String nomFitxer) {
        return carregar(nomFitxer) != null;
    }

    // quantes de les imatges s'han trobat (per provar)
    public int comptarTrobades(String[] nomsFitxer) {
        int n = 0;
        for (String nom : nomsFitxer) {
            if (teImatge(nom)) {
                n++;
            }
        }
        return n;
    }

    public void buidarCache() {
        cache.clear();
    }
}
