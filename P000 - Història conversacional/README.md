# P000 — Història Conversacional: "L'Excursió del Puig de les Bruixes"

Adaptació d'una aventura conversacional clàssica a **aventura gràfica clicable** (estil point-and-click), implementada en **Java** amb bon ús de la programació orientada a objectes.

📄 Disseny complet de la història i el mapa: [`Nova_Historia.md`](./Nova_Historia.md)

---

## 1. Què és aquest projecte

En comptes d'escriure ordres per teclat ("ANAR NORD", "AGAFAR CLAU"), el jugador interactua **clicant botons i zones sobre imatges**: fletxes per moure's entre sales, icones sobre els objectes, un botó de diàleg quan hi ha un personatge. Per sota, la lògica del joc segueix funcionant amb els mateixos verbs/accions que demana l'enunciat original (ANAR, AGAFAR, DEIXAR, USAR, OBRIR, TANCAR, ENCENDRE, APAGAR, PARLAR) — només canvia com el jugador els dispara.

Les imatges de zones, objectes i personatges es generaran amb **Higgsfield AI / Grok**, amb un estil visual consistent.

## 2. Resum de la història

Durant una excursió d'institut al **Puig de les Bruixes**, el protagonista es despista del grup fent una foto i queda atrapat en una boira estranya. Ha de trobar el camí de tornada al **Camp Base** abans que el bus marxi, travessant bosc, riu, cascada, cova, pont penjant, cim i una ermita on viu en **Tomeu** — que no és tan sols un ermità qualsevol.

Detalls complets (mapa de 10 zones, connexions, objectes, personatge, antagonista, finals) al document de disseny enllaçat a dalt.

### Elements afegits sobre els mínims
- 🌗 **Cicle de llum**: cada zona canvia d'imatge (dia/capvespre/nit) segons avança el rellotge intern → pressió visual del temps.
- 📷 **Càmera + àlbum de fotos**: mini-col·leccionable opcional que anima a explorar totes les zones i rejugar.
- 🎭 **Gir narratiu d'en Tomeu**: es revela com el guardià de la llegenda del Puig.
- 🔁 **Dejà vu** en morir (senglar) en comptes d'un game over sec.
- 🌟 **Final secret** si es fan totes dues ofrenes a en Tomeu.
- 🌲 Petits senyals ambientals (marques, cercles de pedres...) que reforcen el misteri sense afegir lògica.

Cap d'aquests extres substitueix cap requisit obligatori de l'enunciat; són capes de polish per sobre.

## 3. Checklist de requisits mínims de l'enunciat

- [x] 10 zones diferents amb descripció en entrar-hi
- [x] No totes les zones amb les mateixes sortides/entrades
- [x] Mínim 2 accions per zona (a més de canviar de zona)
- [x] Inventari: agafar objectes d'una zona i deixar-los a una altra
- [x] El joc entén (internament, com a accions) ANAR / ENCENDRE / USAR / DEIXAR / APAGAR / PARLAR / AGAFAR / OBRIR / TANCAR
- [x] Mínim 6 objectes agafables, deixables i usables (en tenim 6 obligatoris + 2 opcionals: bastó i càmera)
- [x] 1 personatge sense IA real, que reacciona a frases/accions concretes i pot bloquejar una sortida o donar un objecte
- [x] El joc té final(s) i permet jugar una altra partida

## 4. Decisions tècniques

| Aspecte | Decisió |
|---|---|
| Llenguatge | Java |
| Paradigma | POO amb herència real (no decorativa): `Element` abstracta → `Objecte` / `Personatge`; `Personatge` abstracta → `PersonatgeAliat` / `PersonatgeAntagonista` |
| Interfície | Gràfica (Swing o JavaFX, a decidir), imatges + botons en comptes de consola/teclat |
| Imatges | Generades amb Higgsfield AI / Grok |
| Persistència de partida | No prevista de moment (es pot valorar si sobra temps) |

## 5. Estructura de classes (esborrany previ al diagrama UML)

```
Joc
 ├─ Zona (nom, descripció, imatges per FaseDelDia, connexions, objectes, personatge)
 │   └─ Connexio (destí, etiqueta, condició opcional)
 ├─ Element (abstracta: nom, descripció, imatge)
 │   ├─ Objecte (agafable, usable-amb)
 │   │   └─ Camera (subclasse especial: genera Foto en comptes d'usar-se)
 │   └─ Personatge (abstracta: reaccions a frases)
 │       ├─ PersonatgeAliat      (en Tomeu)
 │       └─ PersonatgeAntagonista (el senglar, es mou sol)
 ├─ Jugador (zona actual, Inventari)
 ├─ Inventari (llista d'Objecte)
 ├─ Album (llista de Foto)
 ├─ Verb (enum: ANAR, AGAFAR, DEIXAR, USAR, OBRIR, TANCAR, ENCENDRE, APAGAR, PARLAR, FER_FOTO)
 └─ Final (tipus + text d'epíleg)
```

Aquest esquema és la base sobre la qual es farà el **diagrama de classes UML formal** (primera entrega).

## 6. Pla de treball

| Fase | Contingut | Estat |
|---|---|---|
| 0 | Definició de la nova història i mapa de zones | ✅ Fet (aquest document + `Nova_Historia.md`) |
| 1 | Diagrama de classes: classes, atributs, estructures de dades | 🔜 Següent pas |
| 2 | Moviments bàsics: interfície gràfica, canvi de zona amb botons | Pendent |
| 3 | Agafar/deixar/usar objectes, inventari, interacció amb en Tomeu i el senglar | Pendent |
| 4 | Solució final totalment funcional | Pendent |
| 5 | Presentació a classe | Pendent |