# Verificació de requisits — P000 Història Conversacional

Contrast punt per punt entre l'enunciat ([`enunciat/P000-enunciat.pdf`](./enunciat/P000-enunciat.pdf))
i el disseny de [`historia.md`](./historia.md).

---

## 1. Mínims obligatoris de l'enunciat

| # | Requisit literal de l'enunciat | Estat | On es compleix |
|---|--------------------------------|:-----:|----------------|
| 1 | «El joc ha de tenir **10 zones diferents** amb la seva descripció que apareix a l'entrar a la zona» | ✅ | `historia.md` §3 — 10 zones amb descripció i imatge per fase del dia |
| 2 | «**No totes les zones han de tenir les mateixes sortides**. Algunes poden tenir 1 sol punt d'entrada i altres més d'un» | ✅ | §3 — de 1 sortida (Camp Base) a 4 (Cova Fosca); 3 sortides són condicionades |
| 3 | «A cada zona s'ha de poder fer **almenys 2 accions** a part de poder canviar de zona» | ✅ | §5 — taula d'accions, totes amb 2 o més |
| 4 | «Tindrem un **inventari**. Podrem agafar coses de les zones i deixar-les a altres zones» | ✅ | §6 — els 8 objectes són agafables i deixables a qualsevol zona |
| 5 | «El joc ha d'entendre els següents texts: **ANAR ENCENDRE USAR / DEIXAR APAGAR PARLAR / AGAFAR OBRIR TANCAR**» | ✅ | §1 — caixa d'entrada de text + `AnalitzadorOrdres`; els botons generen la mateixa cadena |
| 6 | «Un mínim de **6 objectes** que es puguin agafar, deixar en una zona i que **algun es pugui fer servir** en alguna zona» | ✅ | §6 — 6 obligatoris (cantimplora, bastó, poma, llanterna, navalla, corda) + mapa + càmera |
| 7 | «Es podrà **parlar amb 1 personatge**. No ha de tenir intel·ligència, només reaccionar a certes frases. Pot **bloquejar una sortida**, o **donar-nos algun objecte**» | ✅ | §8.1 — en Tomeu fa les dues coses: bloqueja Ermita→Refugi i dona el mapa |
| 8 | «Cal que el joc tingui **final** i permeti fer una **altra partida**» | ✅ | §10 — 4 finals + reinici complet de l'estat |

## 2. Cobertura dels 9 verbs obligatoris

Cap verb queda decoratiu: tots tenen com a mínim un ús que fa avançar el joc.

| Verb | Usos reals |
|------|-----------|
| **ANAR** | Moviment entre les 10 zones |
| **AGAFAR** | Cantimplora, bastó, poma, llanterna, navalla, corda, mapa |
| **DEIXAR** | Deixar qualsevol objecte a qualsevol zona; oferir la cantimplora/poma a en Tomeu |
| **USAR** | Bastó+pomer · cantimplora+riu · poma+senglar · navalla+cofre · corda+pont · mapa+cim · mapa+refugi |
| **OBRIR** | Tronc buit (Clariana) · pas secret (Cascada) · caixa del vèrtex (Cim) · cofre (Refugi) |
| **TANCAR** | Tronc buit · caixa del vèrtex · cofre · pas del pont (bloqueja el senglar) |
| **ENCENDRE** | Llanterna (obligatòria a la Cova Fosca i a la Cascada) |
| **APAGAR** | Llanterna |
| **PARLAR** | En Tomeu (ermita) i en Bernat (camp base) |

## 3. Competències transversals de l'enunciat

| Competència | Com es cobreix |
|---|---|
| a) Característiques avançades de l'orientació a objectes | Jerarquia `Element` → `Objecte`/`Contenidor`/`Personatge`, classes abstractes, interfícies (`Encenible`, `Usable`), polimorfisme a `executar()` |
| b) Gestió d'errors amb control d'excepcions | Paquet `excepcions` amb `OrdreInvalidaException`, `AccioNoPermesaException`, `ObjecteNoTrobatException`, `RecursNoTrobatException` |
| c) Gestió d'entorns de desenvolupament | Projecte Java estructurat en paquets + repositori Git |
| d) Tutorials i manuals | `README.md` del projecte + aquests documents de `docs/` |
| e-h) Treball en equip, comunicació, innovació | Projecte en parella amb repartiment de tasques documentat al README |

---

## 4. Incoherències detectades a la versió anterior i com s'han resolt

Aquesta és la llista de problemes que tenia el document original (`Nova_Historia.md`) i la correcció
aplicada. Es documenta aquí per deixar constància del procés de revisió.

| # | Problema detectat | Gravetat | Correcció aplicada |
|---|-------------------|:--------:|--------------------|
| 1 | La interfície era **només de botons**, sense entrada de text. L'enunciat exigeix literalment que «el joc ha d'entendre els següents **texts**» | 🔴 Alta | Interfície **híbrida**: caixa de text amb analitzador real + botons que generen la mateixa cadena i passen pel mateix analitzador (§1) |
| 2 | El **mapa de la muntanya** apareixia en dos llocs contradictoris: «el dona en Tomeu» (§5) i «agafar-lo al Cim del Puig» (§4) | 🔴 Alta | El mapa **el dona en Tomeu**. Al Cim l'acció passa a ser OBRIR la caixa del vèrtex geodèsic + USAR el mapa per orientar-se |
| 3 | En Tomeu bloquejava «la sortida cap al Cim», però el Cim és **per on hi arribes**: el bloqueig no bloquejava res | 🔴 Alta | Ara bloqueja **Ermita → Refugi Amagat**, que és el camí endavant |
| 4 | La **poma** (única defensa contra el senglar) es trobava a prop de l'Ermita, però per arribar-hi calia **travessar abans la Cova Fosca**, que és territori del senglar. El jugador estava obligat a arriscar-se sense l'objecte que el salva | 🔴 Alta | La poma passa al **Corriol del Bosc**, abans de cap zona on patrulla el senglar |
| 5 | Apareixia una «**eina/molla del pont**» a la Cova Fosca que no estava a la llista d'objectes i duplicava la funció de la corda | 🟠 Mitjana | Eliminada. El pont es repara només amb la corda |
| 6 | El **bastó** era «purament decoratiu», cosa que el feia inútil per al recompte d'objectes | 🟠 Mitjana | Ara té funció real: **USAR el bastó amb el pomer** per fer caure la poma |
| 7 | La **cantimplora** «es trobava a El Riu» i calia omplir-la allà mateix — poc coherent, i OMPLIR no és un verb de l'enunciat | 🟠 Mitjana | La cantimplora és a la motxilla del **Camp Base** (AGAFAR) i al Riu s'hi fa **USAR** per omplir-la |
| 8 | El verb **TANCAR** només tenia un ús marginal | 🟡 Baixa | Quatre usos: tronc, caixa del vèrtex, cofre i pas del pont |
| 9 | No estava justificat **per què no es podia tornar al Camp Base pel camí normal**, cosa que trencava la condició de victòria | 🟠 Mitjana | Esdeveniment de la **boira**: en entrar per primer cop a la Clariana, la sortida Corriol → Camp Base es bloqueja (§2, §3) |
| 10 | El **rellotge** i el límit de les 18h no estaven quantificats, així que no es podia implementar | 🟠 Mitjana | Definit: inici 16:00, límit 18:00, 2 minuts per ordre vàlida = 60 torns (§9) |
| 11 | La pista sobre la posició del senglar era vaga («sentir-lo grunyir») | 🟡 Baixa | Formalitzada: pista automàtica a prop/lluny a la descripció de la zona + en Tomeu com a oracle amb 50% de fiabilitat, com el iHall original |
| 12 | El **final secret** demanava donar la poma a en Tomeu, però la poma és la defensa contra el senglar — podia deixar la partida sense solució | 🟡 Baixa | Comprovat que és compatible: després de l'Ermita la ruta va cap al Refugi i la drecera, sense tornar a passar per la Cova (nota de disseny a §10) |

---

## 5. Elements afegits per sobre dels mínims

Cap d'aquests substitueix un requisit obligatori; són capes de polish:

- 🌗 **Cicle de llum** — 3 imatges per zona segons el rellotge.
- 📷 **Càmera i àlbum de fotos** — mini-col·leccionable de 8 moments.
- 🎭 **Gir narratiu d'en Tomeu** — es revela com el guardià del Puig.
- 🔁 **Dejà vu** en morir, en comptes d'un *game over* sec.
- 🌟 **Final secret** amb les dues ofrenes.
- 🌲 **Senyals ambientals** a tres zones (només text i imatge).
