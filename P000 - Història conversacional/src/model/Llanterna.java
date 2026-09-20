package model;

import control.ResultatAccio;
import control.Verb;

// la llanterna de la Clariana (unic objecte que a mes s'encen i s'apaga)
public class Llanterna extends Objecte implements Encenible {

    private boolean encesa;
    private int bateria;

    public Llanterna() {
        super("llanterna", "Una llanterna vella pero solida. Encara li queda pila.");
        this.bateria = 120;
    }

    @Override
    public void encendre() {
        encesa = true;
    }

    @Override
    public void apagar() {
        encesa = false;
    }

    @Override
    public boolean estaEncesa() {
        return encesa && bateria > 0;
    }

    // la bateria nomes baixa mentre esta encesa
    public void gastarBateria(int minuts) {
        if (encesa) {
            bateria = Math.max(0, bateria - minuts);
        }
    }

    public int getBateria() {
        return bateria;
    }

    @Override
    public String descriure() {
        return super.descriure() + (estaEncesa() ? " Ara mateix esta encesa." : " Ara mateix esta apagada.");
    }

    @Override
    public ResultatAccio interactuar(Verb v, Jugador j) {
        if (v == Verb.ENCENDRE) {
            if (estaEncesa()) {
                return ResultatAccio.error("La llanterna ja esta encesa.");
            }
            if (bateria <= 0) {
                return ResultatAccio.error("La pila esta morta.");
            }
            encendre();
            return ResultatAccio.ok("Encens la llanterna.");
        }
        if (v == Verb.APAGAR) {
            if (!encesa) {
                return ResultatAccio.error("La llanterna ja esta apagada.");
            }
            apagar();
            return ResultatAccio.ok("Apagues la llanterna.");
        }
        return super.interactuar(v, j);
    }
}
