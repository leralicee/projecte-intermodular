# P000 — Història Conversacional: "L'Excursió del Puig de les Bruixes"

Aventura conversacional en **Java** amb **interfície gràfica i imatges**, feta per al mòdul
**MP13 (Mòdul DUAL)** del CFGS de Desenvolupament d'Aplicacions Multiplataforma.

---

## Documentació

| Document | Contingut |
|---|---|
| 📄 [Enunciat original](./docs/enunciat/P000-enunciat.pdf) | PDF del professorat |
| 📖 [Disseny de la història](./docs/historia.md) | Mapa de zones, objectes, personatges, finals i regles |
| ✅ [Verificació de requisits](./docs/verificacio-requisits.md) | Contrast punt per punt amb l'enunciat |
| 📐 [Diagrama de classes](./docs/diagrames/) | **Entrega 1** — fitxer draw.io + explicació |

## Com és el joc

Durant una excursió d'institut al **Puig de les Bruixes**, el protagonista es despista del grup fent
una foto i queda atrapat en una boira estranya. Ha de trobar el camí de tornada al **Camp Base**
abans que el bus marxi a les 18h, travessant bosc, riu, cascada, cova, pont penjant, cim i una ermita
on viu en **Tomeu** — que no és tan sols un ermità qualsevol. Pel camí hi ha un **senglar** que
patrulla les coves i que només es pot esquivar distraient-lo amb una poma.

Cada zona es veu amb una **il·lustració** i el jugador hi actua de dues maneres equivalents:
**escrivint l'ordre** (`ANAR NORD`, `USAR LLANTERNA`…) o **clicant els botons** — un per cada sortida,
més Mirar i Motxilla —, que generen exactament la mateixa cadena de text i passen pel mateix
analitzador. Així el joc «entén els texts» que demana l'enunciat i alhora és còmode de jugar.

### Extres per sobre dels mínims

- 🌗 **Cicle de llum** — cada zona té 3 imatges (dia / capvespre / nit) segons el rellotge intern.
- 📷 **Càmera i àlbum de fotos** — col·leccionable de 8 moments que es mostra a l'epíleg.
- 🎭 **Gir narratiu d'en Tomeu** — es revela com el guardià de la llegenda del Puig.
- 🔁 **Dejà vu** en morir, en comptes d'un *game over* sec.
- 🌟 **Final secret** si li fas totes dues ofrenes a en Tomeu.

## Requisits mínims de l'enunciat

- [x] 10 zones diferents amb descripció en entrar-hi
- [x] Sortides no uniformes (d'1 a 4 segons la zona, tres d'elles condicionades)
- [x] Mínim 2 accions per zona a més de canviar de zona
- [x] Inventari: agafar objectes d'una zona i deixar-los a una altra
- [x] El joc entén els texts ANAR / ENCENDRE / USAR / DEIXAR / APAGAR / PARLAR / AGAFAR / OBRIR / TANCAR
- [x] 6 objectes agafables, deixables i usables (+ mapa i càmera)
- [x] 1 personatge sense intel·ligència real que bloqueja una sortida **i** dona un objecte (en Tomeu)
- [x] El joc té finals i permet jugar una altra partida

El detall de com es compleix cada punt és a [`docs/verificacio-requisits.md`](./docs/verificacio-requisits.md).

## Decisions tècniques

| Aspecte | Decisió |
|---|---|
| Llenguatge | Java |
| Interfície | Swing en estil novel·la visual: imatge de la zona, requadre de text, camp d'ordres i un botó per cada sortida |
| Paradigma | POO amb herència real: `Element` → `Objecte` / `Contenidor` / `Personatge`; interfícies `Encenible` i `Usable` |
| Errors | Jerarquia pròpia d'excepcions (`JocException` i derivades) |
| Imatges | 30 il·lustracions (10 zones × dia, capvespre i nit) amb el mateix estil, a `src/recursos/img/` |
| Persistència | No prevista (es valorarà si sobra temps) |

## Estructura del projecte

```
P000 - Història conversacional/
├── docs/
│   ├── enunciat/P000-enunciat.pdf     Enunciat del professorat
│   ├── historia.md                    Disseny narratiu i mecànic
│   ├── verificacio-requisits.md       Contrast amb l'enunciat
│   └── diagrames/
│       ├── diagrama-classes.drawio    ENTREGA 1 (2 pàgines)
│       └── README.md                  Explicació del diagrama
├── bin/                               Classes compilades (no es versiona)
└── src/
    ├── control/                       Joc, MotorDeJoc, AnalitzadorOrdres, Ordre, Verb
    ├── model/                         Zona, Element, Objecte, Personatge, Inventari...
    ├── vista/                         Vista, VistaGrafica, VistaConsola, GestorImatges
    ├── excepcions/                    JocException i derivades
    └── recursos/img/                  Les 30 il·lustracions de les zones
```

Un paquet Java per carpeta (`package control;`, `package model;`…), sense capes intermèdies.

## Com compilar-lo i executar-lo

Des de la carpeta `P000 - Història conversacional`:

```bash
javac -encoding UTF-8 -d bin $(find src -name "*.java")
```

Versió amb finestra i imatges:

```bash
java -cp bin control.Main
```

Versió de consola, per provar el motor sense la part gràfica:

```bash
java -cp bin control.Main consola
```

Les il·lustracions són a `src/recursos/img/`, tres per zona, amb el nom
`<zona>_<fase>.png` (per exemple `cova_fosca_nit.png`). Si en falta alguna, la
finestra pinta un substitut amb el nom de la zona i el joc funciona igual.

## Planificació

| Data | Entrega | Estat |
|---|---|:---:|
| 20 de setembre | **Diagrama de classes** (20%) | ✅ Fet |
| 27 de setembre | Moviments per verbs i mostrar el moviment per pantalla (30%) | ✅ Fet |
| 4 d'octubre | Agafar, deixar, usar, inventari i interacció amb personatges (30%) | 🔜 |
| 29 d'octubre | Solució final totalment funcional | ⏳ |
| Setmana del 5 al 9 d'octubre | Presentació a classe (20%) | ⏳ |
