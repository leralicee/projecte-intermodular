# Entrega 1 — Diagrama de classes

> **Lliurament del 20 de setembre** · 20% de la nota del projecte
> Fitxer: [`diagrama-classes.drawio`](./diagrama-classes.drawio)

## Com obrir-lo

El fitxer és un diagrama de **draw.io** (diagrams.net) en XML sense comprimir. Tres maneres d'obrir-lo:

1. **Web** — [app.diagrams.net](https://app.diagrams.net) → *File → Open From → Device* → tria el fitxer.
2. **VS Code** — instal·la l'extensió *Draw.io Integration* (`hediet.vscode-drawio`) i obre el fitxer: s'edita dins l'editor.
3. **Escriptori** — [draw.io Desktop](https://github.com/jgraph/drawio-desktop/releases).

Té **2 pàgines** (pestanyes a baix de tot):

| Pàgina | Contingut |
|---|---|
| **Diagrama de classes** | Les 37 classes de la solució amb atributs, mètodes i relacions |
| **Mapa de zones** | El graf de les 10 zones amb les sortides condicionades i on és cada objecte |

## Com exportar-lo per entregar-lo al Moodle

A draw.io: **File → Export as → PDF** (o PNG).

- Marca **Selection Only: off** i **Crop: on** perquè agafi tot el diagrama.
- Per al PNG, apuja el **Zoom a 200–300%**: el diagrama és ample i amb el 100% el text queda petit.
- Si el vols en un sol full, a PDF tria **Fit to One Page**.

---

## Organització del diagrama

Les classes estan agrupades en els quatre paquets del projecte, cadascun amb un color:

| Color | Paquet | Responsabilitat |
|---|---|---|
| 🟦 Blau | `control` | Bucle de joc, anàlisi d'ordres de text i execució d'accions |
| 🟩 Verd | `model` | Estat del joc: zones, elements, jugador, inventari, rellotge |
| 🟧 Taronja | — | Classes **abstractes** i **interfícies** (dins de `model`) |
| 🟨 Groc | — | **Enumeracions** (`Verb`, `FaseDelDia`, `EstatPartida`) |
| 🟪 Lila | `vista` | Interfície gràfica Swing amb imatges, i vista de consola |
| 🟥 Vermell | `excepcions` | Jerarquia d'excepcions pròpies |

## Classes que hi intervenen

### Paquet `control`

| Classe | Paper |
|---|---|
| `Main` | Punt d'entrada. Crea el `Joc` i la `Vista`. |
| `Joc` | Estat global de la partida i bucle principal. Coneix el mapa, el jugador, el rellotge i els personatges. |
| `MotorDeJoc` | Executa una `Ordre` i aplica les regles. Un mètode privat per verb. |
| `AnalitzadorOrdres` | **Parser de text**: converteix `"USAR LLANTERNA"` en un objecte `Ordre`. Gestiona sinònims. |
| `Ordre` | Verb + fins a dos complements. |
| `ResultatAccio` | Text de resposta + si l'acció ha estat vàlida i si consumeix temps. |
| `Verb` *(enum)* | Els 9 verbs obligatoris de l'enunciat + `FER_FOTO`, `INVENTARI`, `MIRAR`. |

### Paquet `model`

| Classe | Paper |
|---|---|
| `Jugador` | Zona on es troba i el seu inventari. |
| `Inventari` | Llista d'objectes que porta el jugador. |
| `Rellotge` | Minuts transcorreguts, hora límit i fase del dia. |
| `FaseDelDia` *(enum)* | `DIA`, `CAPVESPRE`, `NIT` → tria quina imatge es mostra. |
| `EstatPartida` *(enum)* | `EN_CURS`, `VICTORIA`, `DERROTA_TEMPS`, `DERROTA_SENGLAR`, `FINAL_SECRET`. |
| `MapaJoc` | Construeix i guarda les 10 zones. |
| `Zona` | Descripció, imatges, connexions i elements que hi ha. |
| `Connexio` | Sortida cap a una altra zona, amb condició opcional (secreta, bloquejada...). |
| `Element` *(abstracta)* | Arrel de tot el que es pot mirar o tocar: té nom, descripció i imatge. |
| `Objecte` | Element agafable i usable amb altres elements. |
| `Llanterna` | Objecte que a més es pot encendre i apagar. |
| `Camera` | Objecte amb una acció pròpia: generar una `Foto`. |
| `Contenidor` | Element que s'obre i es tanca i guarda objectes (tronc, cofre, caixa del vèrtex). |
| `Personatge` *(abstracta)* | Element amb qui es pot parlar. |
| `PersonatgeFix` | En Tomeu i en Bernat: no es mouen, bloquegen sortides i donen objectes. |
| `PersonatgeMobil` | El senglar: es mou sol cada 2 torns entre zones adjacents. |
| `Encenible` *(interfície)* | `encendre()` / `apagar()` / `estaEncesa()`. |
| `Usable` *(interfície)* | `usarAmb(Element)`. |
| `Album` / `Foto` | El col·leccionable de fotos de l'epíleg. |

### Paquet `vista`

| Classe | Paper |
|---|---|
| `Vista` *(interfície)* | Contracte de presentació, perquè el motor no depengui de Swing. |
| `VistaGrafica` | Finestra Swing: imatge de la zona, botons de verbs i **camp de text** per escriure ordres. |
| `VistaConsola` | Implementació de text pur, útil per provar el motor sense gràfics. |
| `GestorImatges` | Carrega i cacheja les imatges de `resources/img/`. |

### Paquet `excepcions`

`JocException` *(abstracta)* estén `Exception`, i d'ella pengen `OrdreInvalidaException`,
`AccioNoPermesaException`, `ObjecteNoTrobatException` i `RecursNoTrobatException`.
Cobreixen la competència (b) de l'enunciat: *«gestiona els errors en els programes, utilitzant el
control d'excepcions facilitat pel llenguatge»*.

---

## Estructures de dades

L'enunciat demana explícitament indicar les **estructures de dades tipus arrays i vectors**:

| Estructura | On s'usa | Per què |
|---|---|---|
| `ArrayList<Objecte>` | `Inventari.objectes`, `Contenidor.contingut` | Mida variable: s'agafen i es deixen objectes contínuament |
| `ArrayList<Connexio>` | `Zona.connexions` | Cada zona en té entre 1 i 4 |
| `ArrayList<Element>` | `Zona.elements` | Objectes i personatges presents a la zona |
| `ArrayList<Foto>` | `Album.fotos` | Es van afegint durant la partida |
| `ArrayList<Zona>` | `PersonatgeMobil.zonesPermeses` | Les 3 zones que patrulla el senglar |
| `HashMap<String, Zona>` | `MapaJoc.zones` | Accés directe a una zona pel seu nom |
| `HashMap<String, Verb>` | `AnalitzadorOrdres.SINONIMS` | Traducció ràpida paraula → verb |
| `HashMap<String, String>` | `Personatge.respostes`, `Objecte.usosValids` | Taula clau → resposta (el personatge "sense intel·ligència" de l'enunciat) |
| `String[]` | `Zona.imatges` | **Array de mida fixa (3)**, indexat per `FaseDelDia`: dia / capvespre / nit |
| `String[]` | `MapaJoc.NOMS_ZONES`, `AnalitzadorOrdres.PARAULES_BUIDES` | Constants de mida fixa |
| `JButton[]` | `VistaGrafica.botonsVerbs` | Un botó per verb, nombre conegut en compilació |
| `enum` | `Verb`, `FaseDelDia`, `EstatPartida` | Conjunts tancats de valors |

## Relacions representades

- **Herència** (fletxa triangular buida): `Objecte`, `Contenidor` i `Personatge` estenen `Element`;
  `Llanterna` i `Camera` estenen `Objecte`; `PersonatgeFix` i `PersonatgeMobil` estenen `Personatge`;
  les quatre excepcions estenen `JocException`.
- **Implementació** (triangular buida discontínua): `Llanterna` → `Encenible`, `Objecte` → `Usable`,
  `VistaGrafica` i `VistaConsola` → `Vista`.
- **Composició** (rombe ple): `Joc` conté `MapaJoc`, `Jugador`, `Rellotge`, `Album` i `MotorDeJoc`;
  `MapaJoc` conté les 10 `Zona`; `Zona` conté les seves `Connexio`; `Album` conté les `Foto`.
- **Agregació** (rombe buit): `Inventari` ◇ `Objecte`, `Zona` ◇ `Element`, `Contenidor` ◇ `Objecte`
  — els objectes passen d'un contenidor a un altre sense deixar d'existir.
- **Associació / dependència**: `Connexio` → `Zona` destí, `Jugador` → zona actual,
  `PersonatgeFix` → `Connexio` que bloqueja, `AnalitzadorOrdres` ⇢ `Ordre`.
