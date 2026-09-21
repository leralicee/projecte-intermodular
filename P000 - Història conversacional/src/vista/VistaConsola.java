package vista;

import model.Album;
import model.EstatPartida;
import model.FaseDelDia;
import model.Inventari;
import model.Zona;

import java.util.Scanner;

// vista de text per provar el motor sense la part grafica
public class VistaConsola implements Vista {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void mostrarZona(Zona z, FaseDelDia fase, String descripcio) {
        System.out.println();
        System.out.println(descripcio);
    }

    @Override
    public void mostrarText(String text) {
        if (text != null && !text.isEmpty()) {
            System.out.println(text);
        }
    }

    @Override
    public void mostrarInventari(Inventari i) {
        System.out.println(i.llistar());
    }

    @Override
    public String llegirOrdre() {
        System.out.print("\n> ");
        if (!scanner.hasNextLine()) {
            return "sortir";
        }
        return scanner.nextLine();
    }

    @Override
    public void mostrarFinal(EstatPartida estat, Album album, String epileg) {
        System.out.println();
        System.out.println("=====================================");
        System.out.println(epileg);
        System.out.println();
        System.out.println(album.mostrarGaleria());
        System.out.println("=====================================");
    }

    public void tancar() {
        scanner.close();
    }
}
