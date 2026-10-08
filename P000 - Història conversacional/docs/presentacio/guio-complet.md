# Guió complet de la presentació

**L'Excursió del Puig de les Bruixes** · MP13 · 9 minuts · una sola persona

Com es llegeix aquest document:

- **El text en cursiva és el que dius**, literal. Està escrit per llegir-se en veu alta.
- Les línies que comencen per `>` són el que has de fer o clicar. No es diuen.
- Els números de diapositiva es refereixen a `presentacio.pptx`, que va en aquest mateix ordre: no has de saltar enrere en cap moment.

Repartiment del temps: explicar el joc 0:00–2:30 · demostrar-lo 2:30–4:45 · el codi 4:45–7:30 · procés i tancament 7:30–8:45. Sobra un minut de coixí.

---

## Abans d'entrar a l'aula

1. El joc obert al Camp Base i el `presentacio.pptx` obert en mode presentador, els dos col·locats.
2. El joc engegat **des de la carpeta `P000 - Història conversacional`**. Si l'engegues des d'un altre lloc les imatges no carreguen.
3. Provat al projector de debò, no només a la teva pantalla.
4. La xuleta d'ordres impresa al costat del teclat.
5. Decideix ara que faràs **dos canvis de finestra en tota la presentació**: un per entrar a la demo i un per sortir-ne.

---

## Regla d'emergència

Durant la demo, si surt l'avís que el senglar et barra el pas, la següent ordre que escrius és aquesta, sense pensar-ho:

```
usar la poma amb el senglar
```

I ho expliques com si estigués previst: *«el senglar es mou sol cada dos torns; si te'l trobes et dona un torn per reaccionar»*. Queda millor que la ruta neta.

---

# BLOC 1 · Explicar el joc

## 0:00 — Diapositiva 1 · Portada

> El joc ja ha d'estar obert darrere.

*Això és L'Excursió del Puig de les Bruixes, una aventura conversacional feta en Java.*

*El plantejament és aquest: una excursió de l'institut, el protagonista es queda enrere fent una foto, baixa la boira i quan aixeca el cap el grup ja no hi és. Ha de tornar al bus abans de les sis, i el bus no l'espera.*

*No es juga amb botons: es juga escrivint. El jugador escriu què vol fer i el joc l'entén.*

> Canvia al joc. Escriu: **nord**. Espera que canviï la imatge.

*Cada zona té la seva il·lustració i a dalt a l'esquerra hi ha l'hora. Comencem a les quatre de la tarda i el bus marxa a les sis. Cada ordre vàlida gasta dos minuts, així que hi ha seixanta torns per tornar: el temps és el que fa que les decisions importin.*

*I les il·lustracions canvien amb l'hora. Cada zona té tres versions, de dia, de capvespre i de nit, i el joc tria la que toca segons el rellotge. A mesura que se't fa tard, se't va fent fosc a la pantalla.*

> Torna a les diapositives.

---

## 0:45 — Diapositiva 2 · Què demanava l'enunciat

*L'enunciat demanava deu zones connectades, un inventari amb límit, com a mínim sis objectes, un personatge amb qui parlar i que el joc es pogués acabar. Tot això hi és, i de llarg: hi ha vint-i-quatre elements i dos personatges, el Bernat al Camp Base i en Tomeu, l'ermità, a dalt.*

*Però hi havia una frase que va condicionar tot el disseny.*

> Assenyala la cita de la dreta.

*Deia que «el joc ha d'entendre els següents texts». No botons: textos. Entendre.*

*Per això el joc porta un analitzador d'ordres propi, que és la peça més feinejada de tot el projecte. I per això els botons i els dibuixos dels objectes que veureu de seguida no són un camí alternatiu: escriuen la mateixa ordre que escriuria el jugador i la fan passar pel mateix lloc. Són una drecera, no un motor diferent.*

---

## 1:30 — Diapositiva 3 · El mapa

*Aquest és el mapa. Deu zones, i cap no té les mateixes sortides que les altres: del Camp Base només se'n surt per un costat i de la Cova Fosca se'n surt per quatre.*

> Assenyala amb el cursor mentre parles. Comença a baix, al Camp Base, i puja.

*Les línies taronges són passos condicionats, i són el cor del joc. Per travessar el pont penjant cal reforçar-lo amb una corda. La corda és dins un cofre tancat. El cofre només s'obre amb una navalla. I la navalla és en una cova on no s'hi veu res si no portes la llanterna encesa.*

*O sigui: llanterna, navalla, cofre, corda, pont. Aquest ordre no és casualitat ni està escrit en cap guia: surt sol del disseny del mapa. Si no tens la llanterna, la resta de la muntanya no existeix.*

*I quan arribes a dalt hi ha en Tomeu, que té el mapa de la drecera per baixar. No el dona de franc: li has d'oferir alguna cosa primer.*

> Assenyala la línia verda de punts, la de baix a l'esquerra.

*Aquesta és la drecera, que està oculta fins que tens el mapa. I aquesta fletxa d'aquí dalt només va en un sentit: de l'ermita es baixa al refugi, però del refugi no es puja a l'ermita. Això ho vaig posar expressament, i al final explicaré per què.*

---

# BLOC 2 · Demostració

## 2:30 — Diapositiva 4 · Demostració

*Ara us ho ensenyo funcionant.*

> **Primer canvi de finestra.** Ves al joc. A partir d'aquí, escriu a poc a poc i deixa llegir el text.

**`agafar cantimplora`**

*Agafar un objecte. Fixeu-vos en la franja de sota: allà hi ha dibuixat el que hi ha a la zona, i la cantimplora n'acaba de desaparèixer perquè ara la porto jo. La franja no és decoració, és l'estat real de la zona.*

**`nord`**

*Em moc. Han passat dos minuts.*

**`agafar basto`**

**`usar el basto amb el pomer`**

*Aquí combino dos objectes. Hi ha una poma massa amunt per arribar-hi, i amb el bastó li pico la branca. Això no és un diàleg ni un menú: ho he escrit en llenguatge normal i el joc ha entès què volia fer amb què.*

**`agafar poma`**

*Em quedo la poma. Us demano que us la recordeu.*

**`nord`**

> Si surt l'avís del senglar: `usar la poma amb el senglar`, i ho expliques com si fos previst.

**`obrir tronc`**

*Això és un contenidor. Hi havia una llanterna a dins i, en obrir-lo, la llanterna passa a estar a la zona: ara ja la puc agafar. No ha aparegut a la motxilla sola, ha caigut a terra.*

**`agafar llanterna`**

**`encendre llanterna`**

*La llanterna té estat: pot estar encesa o apagada. I té pila, que només es gasta mentre està encesa. Per això val la pena apagar-la quan no et fa falta: si t'acabes la pila a mitja cova, et quedes sense res.*

**`nord`**

*I això és la Cova Fosca. Si hi entres sense llum no hi veus res: el text t'ho diu i la imatge també s'enfosqueix. Amb la llanterna encesa, la cova és una zona normal i la navalla és aquí.*

> Si et sobren segons, **una** de les dues, no les dues:

**`agafar la lanterna`** → *Si m'equivoco escrivint, el joc no em diu simplement que no ho entén: em proposa què volia dir.*

> O bé clica un dibuix de la franja de baix i ensenya el menú que surt.

*Clicant un objecte surt el menú de què hi puc fer, i triar una opció escriu l'ordre al camp de text. És la drecera que us deia abans.*

> **Segon canvi de finestra.** Torna a les diapositives i no hi tornis més.

---

# BLOC 3 · El codi

## 4:45 — Diapositiva 5 · Arquitectura

*Això són trenta-sis classes i unes tres mil cinc-centes línies, repartides en quatre paquets.*

*`model` és l'estat del món: les zones, els elements, l'inventari, el rellotge. `control` executa les ordres. `vista` ho ensenya. I `excepcions` són els errors propis del joc.*

*La separació no és decorativa, i es nota en una cosa concreta.*

> Assenyala la franja verda de sota.

*`vista` no és una classe, és una **interfície**. Hi ha dues implementacions: una per consola i una amb finestra. El joc funciona igual amb les dues i es canvia d'una a l'altra en una línia del `Main`. El model no sap que existeix una finestra, i per això es pot provar el joc sencer sense obrir-ne cap.*

---

## 5:05 — Diapositiva 6 · Decisió 1 · la jerarquia

*Vull destacar tres decisions de disseny. La primera és la jerarquia.*

*Tot allò amb què el jugador pot interactuar hereta d'`Element`, que és una classe **abstracta**: no té sentit que existeixi «un element» sense més. D'`Element` pengen tres coses: `Objecte`, que és el que es pot agafar; `Contenidor`, que és el tronc i el cofre; i `Personatge`, que també és abstracta i que es divideix en els personatges fixos, el Bernat i en Tomeu, i el mòbil, que és el senglar.*

*I a sobre d'això hi ha dues interfícies: `Usable`, que diu que una cosa es pot fer servir sobre una altra, i `Encenible`, que diu que es pot encendre i apagar.*

> Assenyala el quadre de la dreta.

*Aquí ve la pregunta important: per què interfícies i no classes? Perquè la llanterna **ja és** un `Objecte`, i Java no deixa heretar de dues classes. «Ser un objecte» és el que la llanterna és; «poder-se encendre» és el que la llanterna fa. Això segon és un comportament, i els comportaments van en interfícies. És el cas de llibre.*

*I té una conseqüència pràctica. Quan el jugador escriu una ordre, el motor no va preguntant de quin tipus és cada cosa. Fa això:*

```java
return ((Usable) que).usarAmb(amb);
```

*El motor només comprova que allò implementi `Usable` i li demana que reaccioni. Què vol dir «fer servir» ho decideix cada classe. Això és polimorfisme, i és el que fa que afegir un objecte nou no obligui a tocar el motor.*

---

## 5:55 — Diapositiva 7 · Decisió 2 · el recorregut d'una ordre

*La segona decisió és que tot entra per un sol lloc. Us segueixo l'ordre `agafar llanterna` des del teclat fins a la pantalla, que és el camí que fa tot el joc.*

*Un: a la finestra hi ha un mètode `enviar`, que rep un `String`. És l'únic camí d'entrada que hi ha. Hi acaben el camp de text, els botons de dalt, els botons de sortida i el menú dels dibuixos: tots quatre acaben convertint-se en text i passant per aquí.*

> Assenyala el codi de l'esquerra.

*Això és el codi que crea els botons de sortida, i és la prova que no són un motor a part. Recorre les connexions visibles de la zona, posa una fletxa segons la direcció, i el que fa el botó quan el cliques és:*

```java
b.addActionListener(e -> enviar("ANAR " + c.getDireccio()));
```

*Escriu text. I com que es generen recorrent el mapa, si jo canvio el mapa els botons canvien sols: no n'hi ha cap escrit a mà.*

*Dos: aquest text va a `Joc.processarEntrada`, que és el bucle de la partida.*

*Tres: l'`AnalitzadorOrdres` el converteix en un objecte `Ordre`, que són tres coses: un verb i dos complements. Pel camí posa el text en minúscules, li treu els accents, treu els articles i les paraules que no aporten res, i reconeix sinònims: «agafar», «agafa», «pren», «recull» i «coge» són el mateix verb. També entén separadors, així que «amb», «sobre», «contra» i «dins» serveixen per partir l'ordre en dues parts.*

*Quatre: els verbs no són textos solts, són un **enum** de tretze valors. Per això el motor pot fer un `switch` tancat: no hi ha cap verb escrit a mà enmig del codi i no me'n puc deixar cap.*

```java
switch (o.getVerb()) {
    case AGAFAR: return agafar(o.getComplement1());
    case USAR:   return usar(o.getComplement1(), o.getComplement2());
    ...
}
```

*Cinc: cada verb té el seu mètode privat, i cadascun retorna un `ResultatAccio`. Aquest objecte no és només el text de resposta: porta també si l'acció ha anat bé i **si gasta temps de joc**. Això és el que fa que mirar l'inventari o equivocar-se no et costin dos minuts del rellotge. Si retornés només un `String` no ho podria distingir.*

*I sis: el `Joc` mira aquest `ResultatAccio` i només si gasta temps avança el rellotge, mou el senglar i comprova si la partida s'ha acabat.*

---

## 6:35 — Diapositiva 8 · Decisió 3 · les excepcions

*La tercera decisió respon directament a una competència que demanava l'enunciat: la gestió d'errors amb excepcions.*

*Hi ha una classe `JocException` **abstracta** que hereta d'`Exception`, i quatre filles: una per quan no entenc l'ordre, una per quan l'objecte no hi és, una per quan l'acció no es pot fer ara mateix i una per quan falta un recurs, com una imatge.*

*El que té d'especial és que cada excepció guarda **dos missatges**.*

> Assenyala el codi de la dreta.

```java
super("Element inexistent: " + nom,
      "Aqui no veig cap " + nom + ".");
```

*El primer és el tècnic, el que va al `getMessage` de tota la vida i em serveix a mi per depurar. El segon és el que veu el jugador. Al jugador no li pots dir «element inexistent»: li has de dir «aquí no veig cap navalla».*

*I ho fa servir de debò. Aquest és el mètode d'agafar un objecte, i té quatre comprovacions seguides, cadascuna amb la seva excepció:*

```java
if (!hiVeu(z))         throw new AccioNoPermesaException(...);  // ets a fosques
if (e == null)         throw new ObjecteNoTrobatException(...);  // no hi es
if (!o.esAgafable())   throw new AccioNoPermesaException(...);  // no es pot endur
if (inv.esPle())       throw new AccioNoPermesaException(...);  // motxilla plena
```

*Quatre maneres diferents de no poder agafar una cosa, i quatre missatges diferents per al jugador. Sense excepcions això serien quatre `if` imbricats retornant codis d'error.*

*I com que totes hereten de `JocException`, el bucle de la partida les enganxa totes amb un sol `catch`:*

```java
try {
    Ordre o = analitzador.analitzar(text);
    r = motor.executar(o);
} catch (JocException e) {
    r = ResultatAccio.error(e.getMissatgeJugador());
}
```

*Això són cinc línies, i és tot el tractament d'errors del joc. Qualsevol excepció meva, d'on vingui, acaba sent un missatge educat a la pantalla i no et gasta temps.*

---

# BLOC 4 · Com s'ha treballat i tancament

## 7:30 — Diapositiva 9 · Com s'ha treballat

*Dues coses del procés.*

*La primera és que l'he fet per capes, i cada commit compila sol. Primer les excepcions i el vocabulari, després el model, després l'analitzador, després el motor, i les vistes al final. Per això el joc no es podia executar fins ben avançat el projecte: el punt d'entrada és l'últim que té sentit escriure, perquè abans no hi ha res a entrar.*

*La segona és la que em va servir més de tot.*

> Assenyala el quadre vermell.

*Vaig fer un script que juga partides senceres sol, sense mi. El vaig executar vint vegades i vaig descobrir que el joc es podia guanyar en setze ordres, saltant-se la llanterna, la navalla, el cofre, la corda i el pont. És a dir: es podia guanyar sense fer cap dels puzles que acabo d'explicar, perquè hi havia un camí entre dues zones que jo no havia tingut en compte.*

*Això és el que vaig arreglar amb la fletxa d'un sol sentit del mapa. I ho vaig tornar a mesurar: ara el recorregut bo guanya vint vegades de vint, la drecera guanya zero vegades de vint, i un jugador que reacciona al senglar sobreviu seixanta vegades de seixanta.*

*Aquesta part no la hauria trobat jugant jo, perquè jo ja sé el camí que vull que facis.*

---

## 8:15 — Diapositiva 10 · Tancament

*Per acabar, què hi ha i què falta.*

*Hi ha quatre finals diferents, es pot tornar a jugar sense tancar el programa, i les trenta il·lustracions de zona estan fetes.*

*Em queda pendent una galeria per veure les fotos que es fan durant la partida, que ara es guarden però no es poden mirar; opcions de diàleg amb en Tomeu que canviïn el final; i posar els dibuixos també a l'inventari, que ara només surten a la zona.*

*Però els mínims de l'enunciat hi són tots i el joc es pot acabar. Gràcies.*

---

# Preguntes probables sobre el codi

Llegeix-te-les abans. No cal que te les memoritzis, però sí que sàpigues per on va cada resposta.

### 1. Per què `JocException` és abstracta? Què passaria si no ho fos?

Si no fos abstracta es podria fer `throw new JocException(...)`, és a dir llançar un error genèric sense dir de quina mena és. Fent-la abstracta obligo a triar sempre una filla concreta, i això vol dir que qui llegeix el `throw` ja sap quin tipus de problema és. A més, `JocException` no té comportament propi: només guarda els dos missatges i serveix per poder-les caçar totes juntes. Una classe que existeix només per agrupar les seves filles és el cas típic de classe abstracta.

### 2. Per què una ordre errònia no fa avançar el rellotge?

Perquè el rellotge no avança al bucle, avança segons el que retorna l'acció. Cada mètode del motor retorna un `ResultatAccio`, que porta un booleà `consumeixTemps`. Hi ha tres constructors de conveniència: `ok` (gasta temps), `okSenseTemps` (ha anat bé però no en gasta, com mirar o obrir la motxilla) i `error` (no en gasta). I quan es captura una excepció es construeix amb `error`, així que tampoc en gasta. El bucle només fa `if (r.consumeixTemps())`. Era una decisió de joc: equivocar-te escrivint no t'ha de costar temps real de partida.

### 3. Si vols afegir el verb `OLORAR`, quins fitxers toques?

Tres. `Verb`, per afegir el valor a l'enum. `AnalitzadorOrdres`, per donar-li les paraules que el jugador pot escriure («olorar», «olora», «fa olor»). I `MotorDeJoc`, per afegir el `case` i el mètode privat. La vista no s'ha de tocar.

I no és teoria: el verb `OBJECTIUS` el vaig afegir exactament així, aquests tres fitxers. Després vaig tocar la vista, però només perquè volia un botó i el rètol de dalt, que són extres: el verb ja funcionava escrivint-lo.

### 4. Per què `Encenible` és una interfície i no una classe?

Perquè `Llanterna` ja hereta d'`Objecte` i Java no té herència múltiple de classes. I conceptualment també toca: ser un objecte és què és la llanterna, poder-se encendre és què sap fer. Si demà vull una torxa o un fanal que no siguin `Objecte`, també poden implementar `Encenible` sense compartir cap pare amb la llanterna.

### 5. Per què `Zona.imatges` és un array i `Zona.elements` un `ArrayList`?

Perquè són coses diferents. Les imatges són sempre exactament tres, una per fase del dia, i van indexades per l'enum `FaseDelDia`: `new String[FaseDelDia.values().length]`. Mai en seran dues ni quatre, i l'accés és directe per índex. Els elements, en canvi, entren i surten durant la partida: quan agafes la cantimplora surt de la llista i quan obres el tronc la llanterna hi entra. Allà necessito una mida variable.

### 6. A `agafar` fas `instanceof`. No contradiu el polimorfisme que has explicat?

És la pregunta justa. Hi ha un `instanceof` i és a propòsit, en un sol lloc: a la frontera. `Zona.cercarElement` retorna un `Element`, perquè en una zona hi pot haver objectes, contenidors i persones. Però a la motxilla només hi caben `Objecte`. Aquesta comprovació és el punt on converteixo «el que he trobat» en «el que l'inventari accepta», i si no és un objecte el missatge és «no pots endur-te en Tomeu». A partir d'aquí ja no torno a preguntar tipus: quan es fa servir una cosa amb una altra, es crida `usarAmb` a través de la interfície i cada classe decideix. El que evito és fer `if` de tipus per decidir **comportament**; comprovar-ho una vegada per validar l'entrada és una altra cosa.

### 7. Com funciona el «Volies dir...?»

Amb distància d'edició, el que es coneix com a distància de Levenshtein: quantes lletres has de canviar, afegir o treure per passar d'una paraula a l'altra. El mètode `suggerir` agafa tots els elements de la zona i de la motxilla, en calcula la distància amb el que ha escrit el jugador (la paraula sencera i també paraula per paraula, per si el nom té dues) i es queda el més a prop sempre que estigui a distància dos o menys. Si no n'hi ha cap prou a prop, retorna `null` i l'excepció surt sense suggeriment. El càlcul el fa `distancia`, implementat amb una sola fila en lloc de la matriu sencera.

### 8. Què passa si el jugador escriu només `agafar`, sense dir què?

El complement arriba a `null` i el mètode retorna `ResultatAccio.error("Que vols agafar?")`. No llanço excepció perquè no és un error del jugador, és una ordre incompleta: és una pregunta, i tampoc gasta temps.

### 9. I si escriu una cosa que no té cap verb?

Allà sí que salta excepció: `OrdreInvalidaException`, llançada des de l'analitzador. Hi ha un cas especial abans, però: si el que ha escrit és una direcció sola, com `nord`, l'analitzador ho converteix en `ANAR nord` sense exigir el verb, perquè és com s'escriu a totes les aventures conversacionals.

### 10. Com saps que `usar el pomer amb el basto` és el mateix que a l'inrevés?

El mètode `provarUs` prova la parella en els dos sentits abans de rendir-se. I si el jugador no diu amb què («omplir la cantimplora» al riu), el motor recorre els elements de la zona provant la combinació amb cadascun fins que una encaixa. Això va ser perquè ningú escriu «usar la cantimplora amb el torrent»: la gent escriu «omplir la cantimplora».

### 11. Com es mou el senglar?

És un `PersonatgeMobil`, i és l'única subclasse de `Personatge` que es mou. Cada dos torns canvia a una zona veïna, i les veïnes les treu del mapa mateix, amb `Zona.zonesVeines()`, no d'una llista escrita a mà. Si acaba on ets tu, se't dona un torn de marge per reaccionar; si el gastes, et carrega. El control d'això està al `Joc` i es fa al final del torn, un cop ja s'ha mogut, perquè si es comprovava abans passaven dues coses lletges: o se't avisava quan el senglar ja havia marxat, o et matava sense haver-te avisat mai.

### 12. Per què el `MotorDeJoc` rep el `Joc` al constructor?

Perquè el motor ha de poder consultar i modificar l'estat: on ets, què portes, quina hora és, si la boira ja ha baixat. Li passo el `Joc` sencer i hi accedeix pels getters. L'alternativa era passar-li mitja dotzena de paràmetres a cada crida.

### 13. Per què no fas servir `Date` o `LocalTime` per a l'hora?

Perquè no és una hora de debò, és un comptador. El `Rellotge` guarda un enter de minuts transcorreguts i les constants `HORA_INICI`, `HORA_LIMIT` i `MINUTS_PER_ORDRE`. Tot el que necessito és sumar de dos en dos, saber quants minuts falten i formatar-ho per pantalla, i això amb un `int` i un `String.format` ja està. `LocalTime` em donaria zones horàries i canvis d'hora que no em fan cap falta.

### 14. Com evites que el jugador es quedi encallat sense poder acabar?

En dues bandes. Les connexions tancades guarden el motiu per què estan tancades i el joc l'ensenya, així que mai et quedes sense saber què et falta. I la drecera del mapa existeix justament per no haver de refer tot el camí al final. Dit això, és el que vaig comprovar amb l'script de partides automàtiques, i és el que em va descobrir el forat al revés: no que et quedessis encallat, sinó que podies acabar massa de pressa.

### 15. Quins finals hi ha i com els controles?

Amb un enum, `EstatPartida`, de cinc valors: `EN_CURS` i els quatre finals, que són victòria, derrota per temps, derrota pel senglar i final secret. L'enum té un mètode `esFinal()` que simplement comprova que no sigui `EN_CURS`, i així el bucle no ha de llistar els quatre casos cada vegada que vol saber si la partida s'ha acabat. El final secret s'aconsegueix per un camí que no és el normal, i el joc se'n recorda entre partides: si el tornes a jugar, hi ha un detall que canvia.

### 16. Per què `Vista` és una interfície?

Perquè el joc no ha de saber com es mostra. Les dues implementacions, consola i finestra, tenen els mateixos mètodes, i el `Main` tria quina es fa servir. Això em va servir per a una cosa molt concreta: l'script que juga partides soles fa servir la de consola, i per tant puc provar tota la lògica del joc sense obrir cap finestra ni fer cap clic.

---

## Marques de control

| Hora | On has d'estar |
|---|---|
| 2:30 | entrant a la demostració |
| 4:45 | sortint de la demostració |
| 7:30 | parlant del testing |
| 8:15 | tancant |

Si a les 4:45 encara ets a la demo, talla-la on sigui i passa al codi: el bloc del codi és el que puntua. Si vas tard al codi, la que es pot resumir en dues frases és la diapositiva 7, el recorregut de l'ordre. Les dues que no es toquen són la 8, les excepcions, i la 9, el testing.
