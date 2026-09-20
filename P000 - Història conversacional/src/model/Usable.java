package model;

import control.ResultatAccio;

// element que es pot fer servir sobre un altre element
public interface Usable {

    ResultatAccio usarAmb(Element e);
}
