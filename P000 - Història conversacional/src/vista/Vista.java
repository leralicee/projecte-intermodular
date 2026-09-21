package vista;

import model.Album;
import model.EstatPartida;
import model.FaseDelDia;
import model.Inventari;
import model.Zona;

// contracte de presentacio. el motor no sap si al davant hi ha swing o una consola
public interface Vista {

    void mostrarZona(Zona z, FaseDelDia fase, String descripcio);

    void mostrarText(String text);

    void mostrarInventari(Inventari i);

    String llegirOrdre();

    void mostrarFinal(EstatPartida estat, Album album, String epileg);
}
