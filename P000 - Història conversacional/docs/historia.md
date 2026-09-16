# P000 · Història Conversacional — "L'Excursió del Puig de les Bruixes"

> Document de disseny narratiu i mecànic del joc.
> Enunciat original: [`enunciat/P000-enunciat.pdf`](./enunciat/P000-enunciat.pdf)
> Verificació punt per punt dels mínims: [`verificacio-requisits.md`](./verificacio-requisits.md)

---

## 1. Idea general

Aventura conversacional en **Java** amb **interfície gràfica i imatges**. Cada zona es mostra amb una
il·lustració i sobre ella hi ha botons/zones clicables: fletxes per canviar de sala, icones sobre els
objectes, un botó de diàleg quan hi ha un personatge.

**Entrada híbrida (important).** L'enunciat demana literalment que *«el joc ha d'entendre els següents
texts: ANAR, ENCENDRE, USAR, DEIXAR, APAGAR, PARLAR, AGAFAR, OBRIR, TANCAR»*. Per això la interfície
manté **sempre una caixa d'entrada de text** amb un analitzador d'ordres real (`AnalitzadorOrdres`):

- El jugador pot **escriure** `USAR LLANTERNA`, `ANAR NORD`, `AGAFAR NAVALLA`… i funciona.
- Els **botons** no són una via alternativa: **generen la mateixa cadena de text** i la fan passar pel
  mateix analitzador. Són una drecera d'accessibilitat, no un motor diferent.

Així es compleix el requisit textual de l'enunciat i, alhora, tenim l'extra gràfic. La lògica interna
(i per tant el diagrama de classes) és la mateixa que en un joc de text pur; només canvia la capa de
presentació.

**Imatges.** Cada zona, objecte i personatge tindrà una il·lustració amb un estil visual consistent
(còmic/anime suau). Es carreguen com a recursos del projecte (`resources/img/…`).

**Cicle de llum.** Cada zona té 3 variants d'imatge (dia / capvespre / nit) segons el rellotge intern.
Com més tarda el jugador, més fosc es veu el bosc. No afegeix lògica nova: només un índex que tria
quina imatge es mostra.

---

## 2. La història

Institut Pia. Última setmana de setembre. La classe fa l'excursió anual al **Puig de les Bruixes**,
una muntanya coneguda pels rumors estranys que hi expliquen els avis del poble.

> — Va, afanya't, que el bus torna a les 18h en punt i el Bernat no espera a ningú! — crida la Mireia,
> la teva millor amiga, mentre puges corrent el corriol.
>
> — Ja vinc, ja vinc... només vull fer una foto d'aquest fong tan flipant — dius tu, apartant-te un
> moment del grup per enfocar bé la càmera.
>
> Just quan prems el disparador, un soroll sec ressona entre els arbres. Alces el cap: el grup ha
> desaparegut corriol amunt i la boira ha començat a baixar de sobte, espessa i freda.
>
> — Mireia? Iu? Algú...? — la teva veu es perd entre els pins.
>
> No hi ha ningú. Només tu, la motxilla, i un bosc que, juraries, no tenia aquest aspecte fa dos minuts.

Has de trobar el camí de tornada al **Camp Base** abans que el bus marxi a les 18h, travessant un bosc
ple de dreceres amagades, un torrent, una cova fosca, un pont penjant mig trencat i una ermita
abandonada on viu algú... o alguna cosa... que fa temps que ningú visita.

Pel camí topes amb en **Tomeu**, un ermità (o potser un follet vell disfressat d'ermità, mai queda
clar) que et podrà ajudar — o posar-te la vida impossible — depenent del que li ofereixis. I val més
que no topis de cara amb el **senglar de la cova**, que és de mala llet si no li dones alguna cosa per
distreure'l.

**La trampa de la boira.** La primera vegada que arribes a la **Clariana**, la boira esborra el corriol
darrere teu: la sortida **Corriol → Camp Base queda bloquejada**. Ja no pots tornar per on has vingut.
L'única manera de baixar és trobar el **mapa de la muntanya** i la drecera que marca des del Refugi
Amagat. Això és el que justifica tot el recorregut.

**Detall ambiental.** A la Clariana, la Cascada i l'Ermita hi ha petits senyals que no fan res a nivell
de joc però reforcen el misteri: marques gravades en un tronc, un cercle de pedres perfecte enmig del
no-res, un ninot fet de branques penjat d'una rama.

---

## 3. Mapa de zones (10 zones)

```
                     [9. Ermita Abandonada]
                             |
                        [8. Cim del Puig]
                             |
                       [7. Pont Penjant]
                             |
   [10. Refugi Amagat]—[6. Cova Fosca]—[5. Cascada]
          |                    |            |
          |               [3. Clariana]—[4. El Riu]
          |                    |
          +————————————[2. Corriol del Bosc]
                               |
                      [1. Camp Base] (INICI / FI)
```

| # | Zona | Connecta amb | Sortides | Particularitat |
|---|------|--------------|:--------:|----------------|
| 1 | Camp Base | Corriol del Bosc | 1 | Inici i final del joc |
| 2 | Corriol del Bosc | Camp Base, Clariana, Refugi Amagat | 3 | La sortida cap al Camp Base **es bloqueja** en entrar per primer cop a la Clariana |
| 3 | Clariana de les Flors | Corriol, El Riu, Cova Fosca | 3 | Dispara l'esdeveniment de la boira |
| 4 | El Riu | Clariana, Cascada | 2 | — |
| 5 | Cascada | El Riu, Cova Fosca | 2 | La sortida cap a la Cova és **secreta**: cal la llanterna encesa per descobrir-la |
| 6 | Cova Fosca | Clariana, Cascada, Refugi Amagat, Pont Penjant | 4 | A les fosques no s'hi veu res sense llanterna |
| 7 | Pont Penjant | Cova Fosca, Cim del Puig | 2 | La sortida cap al Cim **només s'obre** amb el pont reparat amb la corda |
| 8 | Cim del Puig | Pont Penjant, Ermita Abandonada | 2 | — |
| 9 | Ermita Abandonada | Cim del Puig, Refugi Amagat | 2 | En Tomeu **bloqueja** la sortida cap al Refugi fins que li ofereixes alguna cosa |
| 10 | Refugi Amagat | Cova Fosca, Ermita, Corriol del Bosc | 3 | La **drecera** cap al Corriol/Camp Base només s'obre usant el mapa |

Com es veu, les zones **no tenen totes les mateixes sortides**: van d'1 (Camp Base) a 4 (Cova Fosca),
i tres d'elles tenen sortides condicionades.

---

## 4. Recorregut i dependències

El joc té tres cadenes de puzles paral·leles que conflueixen al final:

```
  bastó ──> poma ─────────────> distreu el SENGLAR
  llanterna ──> navalla ──> corda ──> repara el PONT
  cantimplora ──> ofrena a TOMEU ──> mapa ──> obre la DRECERA
```

Cap objecte queda inaccessible abans de necessitar-lo: el **bastó i la poma són al Corriol**, abans de
trepitjar cap zona on patrulla el senglar.

Recorregut mínim de victòria:

1. **Camp Base** — AGAFAR la cantimplora de la motxilla.
2. **Corriol** — AGAFAR el bastó, USAR el bastó amb el pomer, AGAFAR la poma.
3. **Clariana** — OBRIR el tronc buit, AGAFAR la llanterna. *(la boira tanca el camí de tornada)*
4. **El Riu** — USAR la cantimplora per omplir-la.
5. **Cova Fosca** — ENCENDRE la llanterna, AGAFAR la navalla. Si hi ha el senglar, USAR la poma.
6. **Refugi Amagat** — USAR la navalla per OBRIR el cofre, AGAFAR la corda.
7. **Pont Penjant** — USAR la corda per reforçar el pont i poder travessar.
8. **Cim del Puig** — OBRIR la caixa del vèrtex geodèsic (pista narrativa).
9. **Ermita** — PARLAR amb en Tomeu, DEIXAR-li la cantimplora plena, AGAFAR el mapa.
10. **Refugi Amagat** — USAR el mapa → s'obre la drecera → **ANAR al Camp Base** abans de les 18h.

---

## 5. Accions per zona (mínim 2 a cada una, a més de canviar de zona)

| Zona | Accions disponibles |
|---|---|
| 1. Camp Base | **AGAFAR** la cantimplora (motxilla) · **PARLAR** amb en Bernat, el conductor (pistes) · mirar el rellotge del bus |
| 2. Corriol del Bosc | **AGAFAR** el bastó · **USAR** el bastó amb el pomer (cau la poma) · **AGAFAR** la poma · examinar les petjades |
| 3. Clariana de les Flors | **OBRIR** el tronc buit · **AGAFAR** la llanterna · **TANCAR** el tronc |
| 4. El Riu | **USAR** la cantimplora (omplir-la al torrent) · **AGAFAR** pedres per fer el gual (narrativa) |
| 5. Cascada | **ENCENDRE** la llanterna · **OBRIR** el pas secret rere l'aigua (cal la llanterna encesa) · **APAGAR** la llanterna |
| 6. Cova Fosca | **ENCENDRE** la llanterna (obligatori per veure-hi) · **AGAFAR** la navalla (només visible amb llum) · **USAR** la poma amb el senglar |
| 7. Pont Penjant | **USAR** la corda per reforçar el pont · **TANCAR** el pas darrere teu (impedeix que el senglar et segueixi) |
| 8. Cim del Puig | **OBRIR** la caixa del vèrtex geodèsic (hi ha el quadern de registre) · **TANCAR**-la · **USAR** el mapa per orientar-se |
| 9. Ermita Abandonada | **PARLAR** amb en Tomeu · **DEIXAR**-li la cantimplora o la poma · **AGAFAR** el mapa que et dona |
| 10. Refugi Amagat | **USAR** la navalla per **OBRIR** el cofre · **AGAFAR** la corda · **USAR** el mapa per desbloquejar la drecera · **TANCAR** el cofre |

A més, **FER FOTO** està disponible a totes les zones (extra opcional, secció 7).

---

## 6. Objectes

Sis objectes obligatoris agafables/deixables/usables, més dos afegits:

| # | Objecte | On és | Com s'obté | S'usa amb / per a |
|---|---------|-------|------------|-------------------|
| 1 | **Cantimplora** | Camp Base (motxilla) | AGAFAR | **USAR** a El Riu per omplir-la; **DEIXAR** a en Tomeu (té set) |
| 2 | **Bastó** | Corriol del Bosc | AGAFAR | **USAR** amb el pomer del Corriol per fer caure la poma |
| 3 | **Poma** | Corriol del Bosc (a la branca) | Cal fer-la caure amb el bastó, després AGAFAR | **USAR** amb el senglar per distreure'l; **DEIXAR** a en Tomeu |
| 4 | **Llanterna** | Clariana, dins un tronc buit | OBRIR el tronc i AGAFAR | **ENCENDRE/APAGAR**: imprescindible a la Cova Fosca i per trobar el pas secret de la Cascada |
| 5 | **Navalla** | Cova Fosca, entre pedres | Només visible amb la llanterna encesa | **USAR** per obrir el pany del cofre del Refugi |
| 6 | **Corda** | Refugi Amagat, dins el cofre | Obrir el cofre amb la navalla i AGAFAR | **USAR** al Pont Penjant per reforçar-lo |
| 7 | **Mapa de la muntanya** | El dona en Tomeu | Oferir-li la cantimplora plena o la poma | **USAR** al Cim (orientar-se) i al Refugi (obrir la drecera final) |
| 8 | **Càmera de fotos** | La portes des de l'inici | — | Acció pròpia **FER FOTO**, disponible a totes les zones |

Tots vuit es poden **AGAFAR** i **DEIXAR** en qualsevol zona, tal com demana l'enunciat.

---

## 7. Extra opcional: l'àlbum de fotos

Cada foto que fas queda guardada. En arribar a qualsevol final es mostra un **epíleg amb la galeria**
("Has capturat 4 de 8 moments del Puig de les Bruixes"). No afecta la victòria ni la derrota, però dona
un motiu per explorar totes les zones i per rejugar. És un afegit *per sobre* dels mínims.

---

## 8. Personatges

### 8.1 En Tomeu, l'ermità (personatge principal)

Viu a l'**Ermita Abandonada**. No té intel·ligència real: és un mapa de *paraules clau → resposta*, tal
com demana l'enunciat.

- Si li **parles** sense oferir-li res, repeteix queixes sobre la boira i no ajuda.
- **Bloqueja la sortida Ermita → Refugi Amagat**: *«Aquí no es passa sense oferir res a un vell cansat!»*
- Si li **dones la cantimplora plena** (té set) o **la poma** (té gana), et desbloqueja el pas **i et dona
  el mapa de la muntanya**.
- Si li preguntes **«on és el senglar»**, respon — però només encerta un **50%** de les vegades (com el
  iHall de l'enunciat original).

Així en Tomeu compleix les dues opcions que suggereix l'enunciat alhora: *bloqueja una sortida* i *dona
un objecte*.

**Gir narratiu** (es revela a l'epíleg): en Tomeu no és un ermità perdut, és el **guardià del Puig**, la
mateixa figura de la llegenda que expliquen els avis. T'ha anat posant a prova tota l'estona. No canvia
cap mecànica, només el text final — però fa que rejugar es llegeixi diferent. La pista és al quadern de
registre del Cim: hi ha la seva signatura fa 80 anys.

### 8.2 En Bernat, el conductor (secundari)

Al **Camp Base**. Dona la pista inicial i el missatge de final. No bloqueja res.

### 8.3 El senglar de la cova (antagonista)

Es mou lliurement entre **Cova Fosca, Clariana i Cascada**. **Cada 2 moviments del jugador es desplaça
una zona**, només a zones adjacents (mateixa mecànica que el "Malien" de l'enunciat original).

- Si entres a la seva zona **sense haver-lo distret amb la poma**, s'acaba la partida.
- Si li **USES la poma**, queda entretingut la resta de la partida.
- **Pista ambiental automàtica**: en entrar a qualsevol zona, la descripció diu si se'l sent grunyir
  *a prop* (1 zona de distància) o *lluny*. Així mai es mor de forma injusta.

---

## 9. El rellotge

- La partida comença a les **16:00** i el bus marxa a les **18:00**.
- **Cada ordre vàlida consumeix 2 minuts** → 60 torns de marge.
- El rellotge marca també la **fase del dia** que tria quina imatge es mostra:
  - 16:00 – 16:40 → `DIA`
  - 16:40 – 17:20 → `CAPVESPRE`
  - 17:20 – 18:00 → `NIT`

---

## 10. Finals del joc

| # | Final | Condició |
|---|-------|----------|
| 1 | **Victòria** | Arribes al Camp Base per la drecera abans de les 18:00 → *«Arribes just quan el Bernat tanca la porta del bus. La Mireia et mira al·lucinada: "Se't pot deixar sol dos minuts o què?!"»* |
| 2 | **Derrota (temps)** | El rellotge arriba a les 18:00 i encara ets al bosc → *«El bus marxa sense tu. Ara toca trucar als teus pares...»* |
| 3 | **Derrota (senglar)** | Entres a la zona del senglar sense haver-lo distret. En comptes d'un *game over* sec es viu com un **dejà vu**: et despertes just abans que baixi la boira, amb un flaix del que ha passat (*«Això... ja ho he viscut?»*), i tornes a començar amb la pista implantada |
| 4 | **Final secret** | Dones a en Tomeu **la cantimplora *i* la poma**: et convida a passar la nit a l'ermita i et dona un "record del Puig" que apareix a l'inventari de la propera partida |

> Nota de disseny: el final secret és una decisió arriscada real. Si dones la poma a en Tomeu et quedes
> sense la defensa contra el senglar — però a aquelles altures ja no has de tornar a passar per la Cova,
> perquè la drecera del Refugi surt directament cap al Corriol.

En acabar qualsevol final es mostra l'epíleg amb la galeria de fotos i es proposa **jugar una altra
partida** (reinicialitza tot l'estat).

---

## 11. Esquema del bucle de joc

Segueix l'ordre que marca l'enunciat:

1. Es mostra la descripció de la zona actual + la imatge (segons fase del dia) + els objectes visibles.
2. S'espera una ordre (escrita o per botó).
3. L'`AnalitzadorOrdres` converteix el text en un objecte `Ordre` (verb + complements).
4. Si l'ordre no s'entén → missatge d'error i tornem al pas 2.
5. Si és un canvi de zona vàlid → es canvia i tornem al pas 1.
6. Si és una acció vàlida a la zona → s'executa i s'informa de les conseqüències; tornem al pas 2.
7. Si no es pot fer aquí → s'avisa i tornem al pas 2.
8. Després de cada ordre vàlida: avança el rellotge, es mou el senglar si toca, i es comprova si s'ha
   arribat a alguna condició de final.
