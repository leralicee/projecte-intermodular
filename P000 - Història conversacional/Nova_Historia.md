# P000 · Història Conversacional — "L'Excursió del Puig de les Bruixes"

---

## 1. Idea general

En comptes d'un parser de text pur, cada zona es mostra amb una **il·lustració** i unes **zones clicables / botons** superposats: fletxes per canviar de sala, icones sobre els objectes per agafar-los/usar-los, i un botó de diàleg quan hi ha un personatge. Els verbs que demana l'enunciat (ANAR, AGAFAR, DEIXAR, USAR, OBRIR, TANCAR, ENCENDRE, APAGAR, PARLAR) **segueixen existint com a accions internes** del motor de joc — són els mètodes que criden els botons — només canvia que el jugador els selecciona en lloc d'escriure'ls. Això vol dir que la lògica (i per tant el diagrama de classes) és pràcticament la mateixa que en un joc de text pur; només canvia la capa de presentació (Swing/JavaFX amb imatges) i la manera d'entrada (click en lloc de teclat).

**Generació d'imatges:** cada zona, objecte i el personatge tindran una il·lustració generada amb Higgsfield AI / Grok, amb un estil visual consistent (per exemple, còmic/anime suau, o retro-Pixar) perquè es vegi coherent.

**Cicle de llum:** cada zona té 2-3 variants d'imatge (dia / capvespre / nit) segons el rellotge intern del joc. Com més tarda el jugador a avançar, més s'enfosqueix el bosc a les il·lustracions — reforça visualment la pressió del temps sense afegir lògica nova (només un índex que tria quina imatge mostrar).

---

## 2. La història

Institut Pia. Última setmana de setembre. La classe fa l'excursió anual al **Puig de les Bruixes**, una muntanya coneguda pels rumors estranys que hi expliquen els avis del poble.

> — Va, afanya't, que el bus torna a les 18h en punt i el Bernat no espera a ningú! — crida la Mireia, la teva millor amiga, mentre puges corrent el corriol.
>
> — Ja vinc, ja vinc... només vull fer una foto d'aquest fong tan flipant — dius tu, apartant-te un moment del grup per enfocar bé la càmera.
>
> Just quan prems el disparador, un soroll sec ressona entre els arbres. Alces el cap: el grup ha desaparegut corriol amunt i la boira ha començat a baixar de sobte, espessa i freda.
>
> — Mireia? Iu? Algú...? — la teva veu es perd entre els pins.
>
> No hi ha ningú. Només tu, la motxilla, i un bosc que, juraries, no tenia aquest aspecte fa dos minuts.

Comença així la teva petita odissea: has de trobar el camí de tornada al **Camp Base** abans que el bus marxi a les 18h, travessant un bosc ple de dreceres amagades, un torrent, una cova fosca, un pont penjant mig trencat i una ermita abandonada on viu algú... o alguna cosa... que fa temps que ningú visita.

Pel camí topes amb en **Tomeu**, un ermità (o potser un follet vell disfressat d'ermità, mai queda clar) que viu a l'ermita i que et podrà ajudar — o posar-te la vida impossible — depenent del que li diguis. I val més que no topis de cara amb el **senglar de la cova**, que és de mala llet si no li ofereixes alguna cosa per distreure'l.

Aconseguiràs arribar al Camp Base abans que el bus marxi? O et quedaràs a passar la nit al Puig de les Bruixes?

**Detall ambiental:** en 2-3 zones (Clariana, Cascada, Ermita) hi ha petits senyals que no fan res a nivell de joc però reforcen el misteri: marques estranyes gravades en un tronc, un cercle de pedres perfecte enmig del no-res, un ninot fet de branques penjat d'una rama. No calen lògica extra — són només text/imatge — però ajuden a justificar per què en diuen "el Puig de les Bruixes".

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

**Notes de connexió (no totes les zones tenen les mateixes sortides):**

| # | Zona | Connecta amb | Entrades |
|---|------|---------------|----------|
| 1 | Camp Base | Corriol del Bosc | 1 sortida (cap al bosc) |
| 2 | Corriol del Bosc | Camp Base, Clariana | 2 |
| 3 | Clariana de les Flors | Corriol, El Riu, Cova Fosca | 3 |
| 4 | El Riu | Clariana, Cascada | 2 |
| 5 | Cascada | El Riu, Cova Fosca (pas secret rere l'aigua) | 2 (una d'amagada) |
| 6 | Cova Fosca | Clariana, Cascada (secret), Refugi Amagat, Pont Penjant | 4 |
| 7 | Pont Penjant | Cova Fosca, Cim del Puig | 1 sola entrada real (el pont només es pot travessar un cop reparat) |
| 8 | Cim del Puig | Pont Penjant, Ermita Abandonada | 2 |
| 9 | Ermita Abandonada | Cim del Puig, Refugi Amagat | 2 |
| 10 | Refugi Amagat | Cova Fosca, Ermita Abandonada, **Camp Base** (drecera final, només s'obre amb el mapa) | 3 |

La drecera de Refugi Amagat → Camp Base només s'activa quan tens el **mapa de la muntanya** a l'inventari (aquesta és la condició de victòria: torna al Camp Base per aquest camí abans que s'acabi el temps).

---

## 4. Accions per zona (mínim 2 a cada una, a més de moure's)

| Zona | Accions disponibles |
|---|---|
| Camp Base | Mirar el rellotge del bus · Parlar amb el conductor (dona pistes al principi/al final) |
| Corriol del Bosc | Examinar les petjades · Agafar un **bastó** (opcional, no compta pels 6 mínims) |
| Clariana de les Flors | Obrir un **tronc buit** (hi ha la llanterna) · Escoltar el bosc (pista narrativa) |
| El Riu | Omplir la **cantimplora** · Agafar pedres per travessar (narrativa) |
| Cascada | Apagar/encendre la llanterna per veure el pas amagat · Obrir el pas secret (cal la llanterna encesa) |
| Cova Fosca | Encendre la llanterna (obligatori per veure-hi) · Agafar l'**eina/molla del pont** · Usar la **poma** amb el senglar |
| Pont Penjant | Usar la **corda** per reforçar el pont · Tancar/fermar el pas si el senglar t'ha seguit |
| Cim del Puig | Mirar amb el mapa (orientar-se) · Agafar el **mapa de la muntanya** si abans has parlat amb en Tomeu |
| Ermita Abandonada | Parlar amb en Tomeu · Deixar-li un objecte (pot voler la poma o la cantimplora a canvi d'ajuda) |
| Refugi Amagat | Obrir el cofre (hi ha la **navalla**) · Usar el mapa per desbloquejar la drecera |

---

## 5. Objectes (6+ per complir el mínim; en poso 7 amb marge)

1. **Cantimplora** — es troba a *El Riu* (cal omplir-la). S'usa amb en Tomeu (té set) per fer-lo més col·laboratiu.
2. **Llanterna** — amagada dins d'un tronc buit a la *Clariana*. Imprescindible per veure-hi a la *Cova Fosca* i per trobar el pas secret a la *Cascada*.
3. **Poma** — es pot agafar del terra prop de l'*Ermita* (hi ha un pomerar salvatge). S'usa amb el **senglar** de la Cova per distreure'l i poder passar.
4. **Corda** — es troba al *Refugi Amagat* (dins d'un cofre, cal la navalla per obrir el pany). S'usa al *Pont Penjant* per reforçar-lo abans de travessar-lo.
5. **Navalla** — al terra de la *Cova Fosca*, mig amagada entre pedres (només visible amb la llanterna encesa). S'usa per obrir el cofre del *Refugi Amagat*.
6. **Mapa de la muntanya** — el dona en **Tomeu** a l'Ermita, però només si abans li has donat la cantimplora (o la poma; a triar en el disseny final). Sense el mapa no es pot desbloquejar la drecera final.
7. *(Opcional, no imprescindible)* **Bastó** — es pot agafar al Corriol, purament decoratiu/narratiu (per si es vol ampliar amb un puzle extra).
8. **Càmera de fotos** — ja la portes des de l'inici (la de la intro). No es "usa" amb res per resoldre puzles; té una acció pròpia, **FER FOTO**, disponible a totes les zones, sobre qualsevol element interessant (el fong, el senglar de lluny, la boira, un senyal estrany...). No compta pels verbs obligatoris ni pels 6 objectes mínims — és un afegit de col·leccionisme.

### 5.1 Mini-col·leccionable: l'àlbum de fotos

Cada foto que fas queda guardada. En arribar a qualsevol final, es mostra un petit **epíleg amb una galeria** de les fotos aconseguides ("Has capturat 4 de 8 moments del Puig de les Bruixes"). No afecta la victòria/derrota, però dona un motiu real per explorar totes les zones i per tornar a jugar intentant completar l'àlbum sencer.

---

## 6. El personatge: en Tomeu, l'ermità

Viu a l'*Ermita Abandonada*. No té "intel·ligència" real: només reacciona a paraules/accions concretes (implementat com un mapa de frases clau → resposta, tal com demana l'enunciat):

- Si li **parles** sense donar-li res, repeteix queixes sobre la boira i no ajuda.
- Si li **dones la cantimplora** (té set), es posa content i et deixa passar cap al Cim sense problemes.
- Si li **demanes el mapa** havent-li fet un favor abans, te'l dona.
- Si intentes passar cap a l'Ermita sense haver-li donat res, **bloqueja la sortida** cap al Cim ("Aquí no es passa sense oferir res a un vell cansat!").

**Gir narratiu (es revela en qualsevol dels finals):** en Tomeu no és un simple ermità perdut — és el **guardià del Puig**, la mateixa figura de la llegenda que expliquen els avis del poble. Tota l'estona t'ha anat "posant a prova" (per això demana coses a canvi d'ajudar) per decidir si et deixa tornar o no. No canvia cap mecànica, només el text de l'epíleg final, però fa que rejugar la partida es llegeixi diferent un cop ho saps.

## 7. L'antagonista: el senglar de la Cova

Es mou lliurement entre *Cova Fosca*, *Clariana* i *Cascada* (cada 2 moviments del jugador, es desplaça una zona, com el "Malien" de l'exemple original). Si el jugador entra a la mateixa zona que el senglar sense haver-lo distret abans amb la **poma**, la partida acaba (final dolent: "El senglar no estava d'humor per a visites..."). Es pot preguntar sempre "on és el senglar?" (equivalent al iHall que dona pistes) — aquí ho pot fer, per exemple, sentint-lo gruntar més fort o més fluix segons la distància (pista ambiental en comptes d'un oracle fiable).

---

## 8. Finals del joc

1. **Final bo:** arribes al Camp Base pel Refugi Amagat abans que el rellotge marqui les 18h → "Arribes just quan el Bernat tanca la porta del bus. La Mireia et mira al·lucinada: 'Se't pot deixar sol dos minuts o què?!'"
2. **Final dolent (temps):** el rellotge arriba a les 18h i encara ets al bosc → "El bus marxa sense tu. Ara toca trucar als teus pares... i explicar-los per què fas tard."
3. **Final dolent (senglar):** trobes el senglar sense haver-lo distret → fi de partida immediata. En comptes d'un "game over" sec, es viu com un petit **dejà vu**: et despertes just abans que baixi la boira, amb un flaix confús del que ha passat ("Això... ja ho he viscut?"), i tornes a començar amb aquesta pista implantada — manté la tensió sense penalitzar tant.
4. **Final secret ("l'hospitalitat del Puig"):** si li dones a en Tomeu **totes dues** coses (la cantimplora *i* la poma) en lloc de triar-ne només una, es desbloqueja un quart final alternatiu: et convida a passar la nit a l'ermita en comptes de fer-te tornar corrents, i et dona un petit "record del Puig" que apareix a l'inventari de la propera partida (easter egg per qui rejuga).
5. En acabar (qualsevol final), es proposa **jugar una altra partida** (reinicialitzar l'estat del joc), i es mostra sempre l'epíleg amb la galeria de fotos (secció 5.1).

---

## 9. Com encaixa amb els requisits mínims de l'enunciat

| Requisit de l'enunciat | Com es compleix |
|---|---|
| 10 zones amb descripció en entrar | ✅ Taula de mapa (secció 3) |
| No totes les zones amb les mateixes sortides | ✅ Veure taula de connexions |
| Mínim 2 accions per zona, a més de canviar de zona | ✅ Taula secció 4 |
| Inventari (agafar/deixar) | ✅ Sistema d'inventari, els objectes es poden deixar a qualsevol zona |
| Verbs ANAR/AGAFAR/DEIXAR/USAR/OBRIR/TANCAR/ENCENDRE/APAGAR/PARLAR | ✅ Implementats com a mètodes/accions cridades pels botons, no per teclat |
| Mínim 6 objectes agafables/usables/deixables | ✅ 6 objectes obligatoris + 1 opcional |
| 1 personatge sense IA real, reacciona a frases | ✅ En Tomeu |
| Final(s) i possibilitat de nova partida | ✅ 4 finals (2 dolents, 1 bo, 1 secret) + reinici |

**Nota:** tots els extres d'aquesta versió (càmera/galeria, cicle de llum, gir d'en Tomeu, dejà vu, final secret, senyals ambientals) són afegits *per sobre* dels mínims — cap d'ells substitueix cap requisit obligatori, són capes addicionals de polish narratiu i visual.