- [x] A nehézségi szint meg van valósítva: ki tudjuk választani nehézségi szintet (legalább) 3
opció közül. A döntés alapján különböző mennyiség¶ aranyat kapunk: src/game/states/MainMenu.java#L90

- [x] A h®sünk rendelkezik 6 tulajdonsággal, ezek listázva vannak. Kezdetben mindegyik tulajdonságra 1-1 tulajdonságpont van elosztva: src/game/states/ShopState.java#L71

- [x] Tulajdonságpontokat tudunk vásárolni és elosztani a tulajdonságok között. Egy adott
  tulajdonságon legfeljebb 10 tulajdonságpont lehet, ez jól le van kezelve. Nem lehet több
  tulajdonságot vásárolni, ha elfogyott az összes aranyunk: src/game/states/ShopState.java#L132

- [x] Minden tulajdonságpont 10%-kal drágább, mint az el®z®. Az ár kiszámításánál mindig
  felfele kerekítünk.: src/game/payable/Hero.java#L74

- [x] Legalább 3 különböz® varázslat közül választhatunk a játék elején, amelyeket megvásárolhatunk. A varázslatok ára, mannaköltsége is listázva van. Tetsz®leges számú varázslatot
  vásárolhatunk, mindegyiket legfeljebb 1 alkalommal. A varázslatok ára levonódik az aranyunkból. Ha a varázslat ára magasabb, mint a rendelkezésre álló aranyunk mennyisége,
  akkor nem tudjuk megvásárolni: src/game/states/ShopState.java#L25

- [x] Össze tudjuk válogatni a sereget. Legalább 3 különböz® egységb®l választhatunk. Egy
  adott egységb®l tetsz®leges mennyiség¶t tudunk venni, és több fajta egységet is lehet
  vásárolni. Az egységek ára levonódik a rendelkezésre álló aranyból. Több egységet nem
  tudunk vásárolni, mint amennyi aranyunk van. Legalább egy darab egységet kötelez® a
  csatába vinni: src/game/states/ShopState.java#L116

- [x] Az el®készítési fázis után megjelenik a pálya, amely 12x10-es méret¶, tehát 10 mez® magas
  és 12 mez® széles: src/game/states/PlayingState.java#L142

- [ ] A csata el®tt a játékos elhelyezheti az egységeket. Egy egység csak egyszer kerülhet a
  pályára és a csata elkezdéséhez minden egységet el kell helyezni, különben a játék nem
  kezdhet® el

- [x] Az ellenfél is választott magának egy vagy több egységet, és ezeket magával hozta a
  pályára. Miután elhelyeztük az egységeket, a gép is elhelyezi az övéit, tetsz®leges módon.
  Az elhelyezés jól m¶ködik: a megadott oszlopokba kerülnek az egységek, nem kerülhetnek
  pályán kívülre, illetve egymás tetejére sem.Az egységeket csak a pálya bal oldalának els® 2 oszlopába lehet helyezni. Máshova nem
  lehet ®ket helyezni, a pályán kívülre sem. Az egységeket nem lehet egymás tetejére
  helyezni. src/game/states/PlayingState.java#L41