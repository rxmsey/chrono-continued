# Chrono Continued region audit — 28 September 2026

## Result

The region data was incomplete. This is a catalog-wide audit, not a certification that every historical tile is correct. The existing 298 region IDs were compared with 397 named areas in RuneLite. Public wiki release fields were available for 348 names; aliases and broad area dates require interpretation.

Corrected 95 region assignments: 92 missing regions added and three dates corrected. The dataset now contains 390 unique regions. Independently recorded fixtures cover 69 reviewed area groups (105 unique regions) at every selectable date.

Pest Control: the outpost is region 10537 and the battle island is 10536. Only 10537 was listed. Both now unlock on 18 April 2006. Before that date both remain locked. The user’s selected date was not supplied, so an earlier-date visual discrepancy is not yet reproduced.

Other confirmed fixes include Keldagrim’s missing southwest region, Dorgesh-Kaan’s missing southern region, Barrows crypts, minigame interiors, several quest dungeons and Runecrafting altars. Trouble Brewing (15150) moves from 7 February to 4 July 2006; Castle Wars (9520) from 30 to 13 December 2004; the outer TzHaar city (9808) from 4 October to 19 September 2005. Fight Caves itself (9551) unlocks on 4 October 2005.

## Limits and unresolved findings

- Original surface data still has monthly approximate dates. A city’s release date does not prove that every modern region associated with it was added on that day. These candidates were recorded rather than mass-redated.
- Some modern features share a 64×64 region with older terrain. Whole-region locking cannot recreate the old map: e.g. Warriors’ Guild/Burthorpe, modern Prifddinas/older elven terrain. Tile-level historical boundaries would be needed.
- Wiki map markers sometimes refer to entrances or transformed map display coordinates, not the location’s actual game region. The former audit’s TzHaar and Waterbirth anchors illustrate why coordinates alone are insufficient.
- Waterbirth still needs a floor-by-floor chronology (initial August 2005 dungeon versus November 2005 expansion); the existing 10647 anchor conflicts with RuneLite’s Ardougne Sewers label. It was not blindly replaced.
- Broad groups such as Morytania, Karamja, Kandarin and the desert span multiple releases; a single location date cannot validate their entire region lists.
- Modern inner Mor Ul Rek regions, Inferno, raids, Nightmare Zone, Ourania and Warriors’ Guild basement remain locked. Their shared parent names are not treated as historical authorization.
- The comparison catalog is not an exhaustive list of every game region, instance template or later tile expansion. Unlisted regions still fail closed. Further gaps may remain.
- The current running client has not been restarted for this data change. Build/tests verify the patched data; in-game map and instance checks remain necessary.

## Evidence

- Region membership: [RuneLite source at inspected revision](https://github.com/runelite/runelite/blob/6610375cca74469040ebcf03613866f6b3360668/runelite-client/src/main/java/net/runelite/client/plugins/discord/DiscordGameEventType.java).
- Dates: linked individual location pages below, including their preserved release announcements. Downloaded 28 September 2026; earlier cached pages were also reused.
- Tests: `HistoricalRegionAuditTest` reads `region-boundaries.json`, checks each reviewed group at every selector, rejects duplicate IDs, and checks dedicated modern exclusions.

## Applied changes

| Region | Location | Previous date | Correct date |
|---|---|---|---|
| 10834 | Dorgesh-Kaan | Missing / always locked | 2007-03-20 |
| 11422 | Keldagrim | Missing / always locked | 2005-05-31 |
| 13873 | Burgh de Rott | Missing / always locked | 2006-03-22 |
| 14130 | Burgh de Rott | Missing / always locked | 2006-03-22 |
| 14129 | Burgh de Rott | Missing / always locked | 2006-03-22 |
| 14132 | Meiyerditch | Missing / always locked | 2006-09-04 |
| 14387 | Meiyerditch | Missing / always locked | 2006-09-04 |
| 14385 | Meiyerditch | Missing / always locked | 2006-09-04 |
| 14646 | Port Phasmatys | Missing / always locked | 2005-02-15 |
| 11065 | Mountain Camp | Missing / always locked | 2005-03-07 |
| 12950 | Dorgeshuun Mines | Missing / always locked | 2005-05-31 |
| 13206 | Dorgeshuun Mines | Missing / always locked | 2005-05-31 |
| 12693 | Lumbridge Swamp Caves | Missing / always locked | 2005-03-14 |
| 12949 | Lumbridge Swamp Caves | Missing / always locked | 2005-03-14 |
| 12948 | Chasm of Tears | Missing / always locked | 2005-05-04 |
| 10910 | Brine Rat Cavern | Missing / always locked | 2007-04-10 |
| 12423 | Enakhra's Temple | Missing / always locked | 2006-01-23 |
| 9796 | Evil Chicken's Lair | Missing / always locked | 2006-03-15 |
| 14235 | Experiment Cave | Missing / always locked | 2005-01-31 |
| 13979 | Experiment Cave | Missing / always locked | 2005-01-31 |
| 10907 | Fremennik Slayer Dungeon | Missing / always locked | 2005-01-26 |
| 10908 | Fremennik Slayer Dungeon | Missing / always locked | 2005-01-26 |
| 11164 | Fremennik Slayer Dungeon | Missing / always locked | 2005-01-26 |
| 12694 | H.A.M. Hideout | Missing / always locked | 2005-02-22 |
| 10321 | H.A.M. Store room | Missing / always locked | 2006-06-21 |
| 9631 | Jatizso Mines | Missing / always locked | 2007-02-06 |
| 9875 | Jiggig Burial Tomb | Missing / always locked | 2005-05-17 |
| 9874 | Jiggig Burial Tomb | Missing / always locked | 2005-05-17 |
| 10658 | KGP Headquarters | Missing / always locked | 2007-01-29 |
| 9377 | Lunar Isle Mine | Missing / always locked | 2006-07-24 |
| 9544 | Meiyerditch Mine | Missing / always locked | 2006-09-04 |
| 10144 | Miscellania Dungeon | Missing / always locked | 2006-05-22 |
| 10400 | Miscellania Dungeon | Missing / always locked | 2006-05-22 |
| 11924 | Mogre Camp | Missing / always locked | 2006-03-15 |
| 14994 | Mos Le'Harmless Caves | Missing / always locked | 2006-07-04 |
| 14995 | Mos Le'Harmless Caves | Missing / always locked | 2006-07-04 |
| 15251 | Mos Le'Harmless Caves | Missing / always locked | 2006-07-04 |
| 9046 | Mouse Hole | Missing / always locked | 2007-06-04 |
| 10575 | Shadow Dungeon | Missing / always locked | 2005-04-18 |
| 10831 | Shadow Dungeon | Missing / always locked | 2005-04-18 |
| 12946 | Smoke Dungeon | Missing / always locked | 2005-04-18 |
| 13202 | Smoke Dungeon | Missing / always locked | 2005-04-18 |
| 13200 | Sophanem Dungeon | Missing / always locked | 2007-01-10 |
| 7505 | Stronghold of Security | Missing / always locked | 2006-07-04 |
| 8017 | Stronghold of Security | Missing / always locked | 2006-07-04 |
| 8530 | Stronghold of Security | Missing / always locked | 2006-07-04 |
| 9297 | Stronghold of Security | Missing / always locked | 2006-07-04 |
| 12616 | Tarn's Lair | Missing / always locked | 2007-01-22 |
| 12615 | Tarn's Lair | Missing / always locked | 2007-01-22 |
| 7496 | Temple of Light | Missing / always locked | 2005-10-17 |
| 13209 | Dungeon of Tolna | Missing / always locked | 2006-04-03 |
| 12625 | Tunnel of Chaos | Missing / always locked | 2007-03-27 |
| 14234 | Werewolf Agility Course | Missing / always locked | 2005-01-31 |
| 10646 | Ardougne Rat Pits | Missing / always locked | 2005-11-28 |
| 7508 | Barbarian Assault | Missing / always locked | 2007-01-04 |
| 7509 | Barbarian Assault | Missing / always locked | 2007-01-04 |
| 10322 | Barbarian Assault | Missing / always locked | 2007-01-04 |
| 14231 | Barrows | Missing / always locked | 2005-05-09 |
| 7757 | Blast Furnace | Missing / always locked | 2005-08-23 |
| 11157 | Brimhaven Agility Arena | Missing / always locked | 2004-07-27 |
| 8781 | Burthorpe Games Room | Missing / always locked | 2004-11-24 |
| 9520 | Castle Wars | 2004-12-30 | 2004-12-13 |
| 9620 | Castle Wars | Missing / always locked | 2004-12-13 |
| 7499 | Fishing Trawler | Missing / always locked | 2003-07-28 |
| 13462 | Mage Training Arena | Missing / always locked | 2006-01-04 |
| 13463 | Mage Training Arena | Missing / always locked | 2006-01-04 |
| 10536 | Pest Control | Missing / always locked | 2006-04-18 |
| 7749 | Pyramid Plunder | Missing / always locked | 2006-07-17 |
| 11854 | Rogues' Den | Missing / always locked | 2005-06-22 |
| 11855 | Rogues' Den | Missing / always locked | 2005-06-22 |
| 12109 | Rogues' Den | Missing / always locked | 2005-06-22 |
| 12110 | Rogues' Den | Missing / always locked | 2005-06-22 |
| 12111 | Rogues' Den | Missing / always locked | 2005-06-22 |
| 11605 | Sorceress's Garden | Missing / always locked | 2007-02-12 |
| 15150 | Trouble Brewing | 2006-02-07 | 2006-07-04 |
| 9551 | Tzhaar Fight Caves | Missing / always locked | 2005-10-04 |
| 9552 | Tzhaar Fight Pits | Missing / always locked | 2005-09-19 |
| 11339 | Air Altar | Missing / always locked | 2004-03-29 |
| 10059 | Body Altar | Missing / always locked | 2004-03-29 |
| 9035 | Chaos Altar | Missing / always locked | 2004-03-29 |
| 8523 | Cosmic Altar | Missing / always locked | 2004-03-29 |
| 8779 | Death Altar | Missing / always locked | 2005-10-17 |
| 10571 | Earth Altar | Missing / always locked | 2004-03-29 |
| 10315 | Fire Altar | Missing / always locked | 2004-03-29 |
| 11083 | Mind Altar | Missing / always locked | 2004-03-29 |
| 9547 | Nature Altar | Missing / always locked | 2004-03-29 |
| 10827 | Water Altar | Missing / always locked | 2004-03-29 |
| 11595 | Rune Essence Mine | Missing / always locked | 2004-03-29 |
| 9276 | Fremennik Isles | Missing / always locked | 2007-02-06 |
| 10558 | Iceberg | Missing / always locked | 2007-01-29 |
| 10559 | Iceberg | Missing / always locked | 2007-01-29 |
| 9775 | Jiggig | Missing / always locked | 2005-05-17 |
| 8763 | Pirates' Cove | Missing / always locked | 2006-07-24 |
| 11343 | Ratcatchers Mansion | Missing / always locked | 2005-11-28 |
| 9808 | Outer TzHaar city | 2005-10-04 | 2005-09-19 |

## Every configured region after corrections

“Reviewed group” means its explicit group/date fixture was checked. “Unverified legacy” means a valid ID with retained date, not historically certified.

| Region | Unlock date | RuneLite labels | Status |
|---|---|---|---|
| 7496 | 2005-10-17 | Temple of Light | Reviewed group |
| 7499 | 2003-07-28 | Fishing Trawler | Reviewed group |
| 7505 | 2006-07-04 | Stronghold of Security | Reviewed group |
| 7508 | 2007-01-04 | Barbarian Assault | Reviewed group |
| 7509 | 2007-01-04 | Barbarian Assault | Reviewed group |
| 7749 | 2006-07-17 | Pyramid Plunder | Reviewed group |
| 7757 | 2005-08-23 | Blast Furnace | Reviewed group |
| 8017 | 2006-07-04 | Stronghold of Security | Reviewed group |
| 8252 | 2006-07-24 | Lunar Isle | Unverified legacy |
| 8253 | 2006-07-24 | Lunar Isle | Unverified legacy |
| 8508 | 2006-07-24 | Lunar Isle | Unverified legacy |
| 8509 | 2006-07-24 | Lunar Isle | Unverified legacy |
| 8523 | 2004-03-29 | Cosmic Altar | Reviewed group |
| 8530 | 2006-07-04 | Stronghold of Security | Reviewed group |
| 8752 | 2004-09-30 | Poison Waste | Unverified legacy |
| 8753 | 2004-09-30 | Isafdar | Unverified legacy |
| 8754 | 2004-09-30 | Isafdar | Unverified legacy |
| 8755 | 2004-09-30 | Prifddinas | Unverified legacy |
| 8763 | 2006-07-24 | Pirates' Cove | Reviewed group |
| 8779 | 2005-10-17 | Death Altar | Reviewed group |
| 8781 | 2004-11-24 | Burthorpe Games Room | Reviewed group |
| 9008 | 2004-09-30 | Poison Waste | Unverified legacy |
| 9009 | 2004-09-30 | Isafdar | Unverified legacy |
| 9010 | 2004-09-30 | Isafdar | Unverified legacy |
| 9011 | 2004-09-30 | Prifddinas | Unverified legacy |
| 9035 | 2004-03-29 | Chaos Altar | Reviewed group |
| 9046 | 2007-06-04 | Mouse Hole | Reviewed group |
| 9264 | 2004-09-30 | Kandarin | Unverified legacy |
| 9266 | 2004-09-30 | Arandar | Unverified legacy |
| 9267 | 2004-09-30 | Arandar | Unverified legacy |
| 9272 | 2006-11-21 | Piscatoris Hunter Area | Unverified legacy |
| 9273 | 2006-05-02 | Piscatoris | Unverified legacy |
| 9275 | 2007-02-06 | Neitiznot | Unverified legacy |
| 9276 | 2007-02-06 | Fremennik Isles | Reviewed group |
| 9297 | 2006-07-04 | Stronghold of Security | Reviewed group |
| 9377 | 2006-07-24 | Lunar Isle Mine | Reviewed group |
| 9520 | 2004-12-13 | Castle Wars | Reviewed group |
| 9521 | 2004-09-30 | No catalog label | Unverified legacy |
| 9522 | 2004-09-30 | No catalog label | Unverified legacy |
| 9523 | 2004-09-30 | Arandar | Unverified legacy |
| 9524 | 2004-09-30 | Kandarin | Unverified legacy |
| 9525 | 2002-12-31 | Tree Gnome Stronghold | Unverified legacy |
| 9526 | 2002-12-31 | Tree Gnome Stronghold | Unverified legacy |
| 9527 | 2003-12-31 | Kandarin | Unverified legacy |
| 9531 | 2007-02-06 | Jatizso | Unverified legacy |
| 9532 | 2007-02-06 | Fremennik Isles | Reviewed group |
| 9544 | 2006-09-04 | Meiyerditch Mine | Reviewed group |
| 9547 | 2004-03-29 | Nature Altar | Reviewed group |
| 9551 | 2005-10-04 | Tzhaar Fight Caves | Reviewed group |
| 9552 | 2005-09-19 | Tzhaar Fight Pits | Reviewed group |
| 9620 | 2004-12-13 | Castle Wars | Reviewed group |
| 9631 | 2007-02-06 | Jatizso Mines | Reviewed group |
| 9774 | 2003-05-31 | Feldip Hills | Unverified legacy |
| 9775 | 2005-05-17 | Jiggig | Reviewed group |
| 9776 | 2004-12-30 | Kandarin | Unverified legacy |
| 9777 | 2003-03-31 | Observatory | Unverified legacy |
| 9778 | 2003-03-31 | Ourania Hunter Area | Unverified legacy |
| 9779 | 2002-07-31 | Ardougne | Unverified legacy |
| 9780 | 2002-12-31 | Ardougne | Unverified legacy |
| 9781 | 2002-12-31 | Tree Gnome Stronghold | Unverified legacy |
| 9782 | 2002-12-31 | Tree Gnome Stronghold | Unverified legacy |
| 9783 | 2003-12-31 | Kandarin | Unverified legacy |
| 9784 | 2004-11-30 | No catalog label | Unverified legacy |
| 9785 | 2004-11-30 | No catalog label | Unverified legacy |
| 9786 | 2004-11-30 | No catalog label | Unverified legacy |
| 9787 | 2004-11-30 | No catalog label | Unverified legacy |
| 9788 | 2004-11-30 | No catalog label | Unverified legacy |
| 9789 | 2004-11-30 | No catalog label | Unverified legacy |
| 9796 | 2006-03-15 | Evil Chicken's Lair | Reviewed group |
| 9808 | 2005-09-19 | Mor Ul Rek | Reviewed group |
| 9874 | 2005-05-17 | Jiggig Burial Tomb | Reviewed group |
| 9875 | 2005-05-17 | Jiggig Burial Tomb | Reviewed group |
| 10029 | 2006-11-21 | Feldip Hills | Unverified legacy |
| 10030 | 2003-05-31 | Feldip Hills | Unverified legacy |
| 10031 | 2003-05-31 | Gu'Tanoth | Unverified legacy |
| 10032 | 2002-10-31 | Yanille | Unverified legacy |
| 10033 | 2002-07-31 | Tree Gnome Village | Unverified legacy |
| 10034 | 2002-07-31 | Battlefield | Unverified legacy |
| 10035 | 2002-08-31 | Ardougne | Unverified legacy |
| 10036 | 2002-08-31 | Ardougne | Unverified legacy |
| 10037 | 2002-09-30 | Kandarin | Unverified legacy |
| 10038 | 2002-09-30 | Otto's Grotto | Unverified legacy |
| 10039 | 2002-12-31 | Barbarian Outpost | Unverified legacy |
| 10040 | 2004-11-30 | Lighthouse | Unverified legacy |
| 10041 | 2004-11-30 | No catalog label | Unverified legacy |
| 10042 | 2005-08-01 | Waterbirth Island | Unverified legacy |
| 10043 | 2004-11-30 | No catalog label | Unverified legacy |
| 10044 | 2004-11-30 | Miscellania | Unverified legacy |
| 10045 | 2004-11-30 | No catalog label | Unverified legacy |
| 10059 | 2004-03-29 | Body Altar | Reviewed group |
| 10144 | 2006-05-22 | Miscellania Dungeon | Reviewed group |
| 10286 | 2003-05-31 | Feldip Hills | Unverified legacy |
| 10287 | 2003-05-31 | Feldip Hills | Unverified legacy |
| 10288 | 2002-10-31 | Yanille | Unverified legacy |
| 10289 | 2002-07-31 | Fight Arena | Unverified legacy |
| 10290 | 2002-05-31 | Kandarin | Unverified legacy |
| 10291 | 2002-04-30 | Ardougne | Unverified legacy |
| 10292 | 2002-04-30 | Ardougne | Unverified legacy |
| 10293 | 2002-06-30 | Fishing Guild | Unverified legacy |
| 10294 | 2002-07-31 | Kandarin | Unverified legacy |
| 10295 | 2003-12-31 | No catalog label | Unverified legacy |
| 10296 | 2004-11-30 | Fremennik Province | Unverified legacy |
| 10297 | 2004-11-30 | Rellekka | Unverified legacy |
| 10298 | 2004-11-30 | No catalog label | Unverified legacy |
| 10299 | 2004-11-30 | No catalog label | Unverified legacy |
| 10300 | 2004-11-30 | Etceteria | Unverified legacy |
| 10301 | 2004-11-30 | No catalog label | Unverified legacy |
| 10307 | 2007-06-11 | Puro-Puro | Unverified legacy |
| 10315 | 2004-03-29 | Fire Altar | Reviewed group |
| 10321 | 2006-06-21 | H.A.M. Store room | Reviewed group |
| 10322 | 2007-01-04 | Barbarian Assault | Reviewed group |
| 10400 | 2006-05-22 | Miscellania Dungeon | Reviewed group |
| 10536 | 2006-04-18 | Pest Control | Reviewed group |
| 10537 | 2006-04-18 | Void Knights' Outpost | Reviewed group |
| 10542 | 2004-05-31 | Feldip Hills | Unverified legacy |
| 10543 | 2003-05-31 | Feldip Hills | Unverified legacy |
| 10544 | 2002-10-31 | Hazelmere's Island | Unverified legacy |
| 10545 | 2002-07-31 | Port Khazard | Unverified legacy |
| 10546 | 2002-05-31 | Kandarin | Unverified legacy |
| 10547 | 2002-04-30 | Ardougne | Unverified legacy |
| 10548 | 2002-04-30 | Ardougne | Unverified legacy |
| 10549 | 2002-05-31 | Ranging Guild | Unverified legacy |
| 10550 | 2002-05-31 | McGrubor's Wood | Unverified legacy |
| 10551 | 2003-12-31 | Kandarin | Unverified legacy |
| 10552 | 2004-11-30 | Fremennik Province | Unverified legacy |
| 10553 | 2004-11-30 | Rellekka | Unverified legacy |
| 10554 | 2004-11-30 | No catalog label | Unverified legacy |
| 10555 | 2004-11-30 | No catalog label | Unverified legacy |
| 10556 | 2004-11-30 | No catalog label | Unverified legacy |
| 10557 | 2004-11-30 | No catalog label | Unverified legacy |
| 10558 | 2007-01-29 | Iceberg | Reviewed group |
| 10559 | 2007-01-29 | Iceberg | Reviewed group |
| 10571 | 2004-03-29 | Earth Altar | Reviewed group |
| 10575 | 2005-04-18 | Shadow Dungeon | Reviewed group |
| 10646 | 2005-11-28 | Ardougne Rat Pits | Reviewed group |
| 10647 | 2005-08-01 | Ardougne Sewers | Unverified legacy |
| 10658 | 2007-01-29 | KGP Headquarters | Reviewed group |
| 10794 | 2004-12-30 | Ape Atoll | Unverified legacy |
| 10795 | 2004-12-30 | Ape Atoll | Unverified legacy |
| 10796 | 2004-12-30 | No catalog label | Unverified legacy |
| 10797 | 2003-08-31 | No catalog label | Unverified legacy |
| 10798 | 2003-01-31 | No catalog label | Unverified legacy |
| 10799 | 2003-01-31 | No catalog label | Unverified legacy |
| 10800 | 2003-01-31 | No catalog label | Unverified legacy |
| 10801 | 2002-02-28 | Karamja | Unverified legacy |
| 10802 | 2002-02-28 | Karamja | Unverified legacy |
| 10803 | 2002-04-30 | Witchaven | Unverified legacy |
| 10804 | 2002-04-30 | Legends' Guild | Unverified legacy |
| 10805 | 2002-03-31 | Kandarin | Unverified legacy |
| 10806 | 2002-03-31 | Seers' Village | Unverified legacy |
| 10807 | 2003-06-30 | Sinclair Mansion | Unverified legacy |
| 10808 | 2004-11-30 | Fremennik Province | Unverified legacy |
| 10809 | 2004-11-30 | Fremennik Province | Unverified legacy |
| 10810 | 2005-05-31 | Fremennik Province | Unverified legacy |
| 10811 | 2006-11-21 | Fremennik Province | Unverified legacy |
| 10827 | 2004-03-29 | Water Altar | Reviewed group |
| 10831 | 2005-04-18 | Shadow Dungeon | Reviewed group |
| 10833 | 2007-03-20 | Dorgesh-Kaan South Dungeon | Reviewed group |
| 10834 | 2007-03-20 | Dorgesh-Kaan | Reviewed group |
| 10835 | 2007-03-20 | Dorgesh-Kaan | Reviewed group |
| 10907 | 2005-01-26 | Fremennik Slayer Dungeon | Reviewed group |
| 10908 | 2005-01-26 | Fremennik Slayer Dungeon | Reviewed group |
| 10910 | 2007-04-10 | Brine Rat Cavern | Reviewed group |
| 11050 | 2004-12-30 | Ape Atoll | Unverified legacy |
| 11051 | 2004-12-30 | Marim | Unverified legacy |
| 11052 | 2004-12-30 | No catalog label | Unverified legacy |
| 11053 | 2003-08-31 | Kharazi Jungle | Unverified legacy |
| 11054 | 2003-01-31 | Karamja | Unverified legacy |
| 11055 | 2003-01-31 | Tai Bwo Wannai | Unverified legacy |
| 11056 | 2003-01-31 | Tai Bwo Wannai | Unverified legacy |
| 11057 | 2002-02-28 | Brimhaven | Unverified legacy |
| 11058 | 2002-02-28 | Brimhaven | Unverified legacy |
| 11059 | 2002-09-30 | Fishing Platform | Unverified legacy |
| 11060 | 2002-02-28 | Entrana | Unverified legacy |
| 11061 | 2002-02-28 | Catherby | Unverified legacy |
| 11062 | 2002-02-28 | Kandarin | Unverified legacy |
| 11063 | 2004-08-30 | Max Island | Unverified legacy |
| 11064 | 2004-11-30 | Fremennik Province | Unverified legacy |
| 11065 | 2005-03-07 | Mountain Camp | Reviewed group |
| 11066 | 2005-01-31 | Trollweiss Mountain | Unverified legacy |
| 11067 | 2005-01-31 | Trollweiss Mountain | Unverified legacy |
| 11068 | 2005-01-31 | Trollweiss Mountain | Unverified legacy |
| 11083 | 2004-03-29 | Mind Altar | Reviewed group |
| 11157 | 2004-07-27 | Brimhaven Agility Arena | Reviewed group |
| 11164 | 2005-01-26 | Fremennik Slayer Dungeon | Reviewed group |
| 11306 | 2004-12-30 | No catalog label | Unverified legacy |
| 11307 | 2004-12-30 | No catalog label | Unverified legacy |
| 11308 | 2004-12-30 | No catalog label | Unverified legacy |
| 11309 | 2003-08-31 | Kharazi Jungle | Unverified legacy |
| 11310 | 2003-01-31 | Shilo Village | Unverified legacy |
| 11311 | 2003-01-31 | Karamja | Unverified legacy |
| 11312 | 2003-01-31 | Karamja | Unverified legacy |
| 11313 | 2001-06-30 | Karamja | Unverified legacy |
| 11314 | 2001-09-30 | Crandor | Unverified legacy |
| 11315 | 2001-09-30 | Crandor | Unverified legacy |
| 11316 | 2002-02-28 | Entrana | Unverified legacy |
| 11317 | 2002-02-28 | Catherby | Unverified legacy |
| 11318 | 2002-02-28 | Catherby | Unverified legacy |
| 11319 | 2004-08-30 | Burthorpe | Unverified legacy |
| 11320 | 2004-08-30 | Death Plateau | Unverified legacy |
| 11321 | 2004-08-30 | Troll Stronghold | Unverified legacy |
| 11322 | 2005-01-31 | Ice Path | Unverified legacy |
| 11339 | 2004-03-29 | Air Altar | Reviewed group |
| 11343 | 2005-11-28 | Ratcatchers Mansion | Reviewed group |
| 11422 | 2005-05-31 | Keldagrim | Reviewed group |
| 11423 | 2005-05-31 | Keldagrim | Reviewed group |
| 11562 | 2004-12-30 | Crash Island | Unverified legacy |
| 11563 | 2004-12-30 | No catalog label | Unverified legacy |
| 11564 | 2004-12-30 | No catalog label | Unverified legacy |
| 11565 | 2003-08-31 | Kharazi Jungle | Unverified legacy |
| 11566 | 2003-01-31 | Karamja | Unverified legacy |
| 11567 | 2003-01-31 | Karamja | Unverified legacy |
| 11568 | 2003-01-31 | Karamja | Unverified legacy |
| 11569 | 2001-06-30 | Karamja | Unverified legacy |
| 11570 | 2001-09-30 | Rimmington | Unverified legacy |
| 11571 | 2002-02-28 | Crafting Guild | Unverified legacy |
| 11572 | 2002-02-28 | Falador | Unverified legacy |
| 11573 | 2002-02-28 | Taverley | Unverified legacy |
| 11574 | 2002-02-28 | Taverley | Unverified legacy |
| 11575 | 2004-08-30 | Burthorpe | Unverified legacy |
| 11576 | 2004-08-30 | Troll Arena | Unverified legacy |
| 11577 | 2004-08-30 | Trollheim | Unverified legacy |
| 11579 | 2004-10-31 | No catalog label | Unverified legacy |
| 11580 | 2004-10-31 | No catalog label | Unverified legacy |
| 11595 | 2004-03-29 | Rune Essence Mine | Reviewed group |
| 11605 | 2007-02-12 | Sorceress's Garden | Reviewed group |
| 11678 | 2005-05-31 | Keldagrim | Reviewed group |
| 11679 | 2005-05-31 | Keldagrim | Reviewed group |
| 11821 | 2003-08-31 | Kharazi Jungle | Unverified legacy |
| 11822 | 2002-12-31 | Karamja | Unverified legacy |
| 11823 | 2002-12-31 | Ship Yard | Unverified legacy |
| 11825 | 2001-04-30 | Asgarnia | Unverified legacy |
| 11826 | 2001-04-30 | Rimmington | Unverified legacy |
| 11827 | 2001-04-30 | Falador | Unverified legacy |
| 11828 | 2001-04-30 | Falador | Unverified legacy |
| 11829 | 2001-04-30 | Asgarnia | Unverified legacy |
| 11830 | 2001-04-30 | Asgarnia | Unverified legacy |
| 11831 | 2001-08-31 | No catalog label | Unverified legacy |
| 11832 | 2001-08-31 | No catalog label | Unverified legacy |
| 11833 | 2001-08-31 | No catalog label | Unverified legacy |
| 11834 | 2004-03-31 | No catalog label | Unverified legacy |
| 11835 | 2002-05-31 | No catalog label | Unverified legacy |
| 11836 | 2001-08-31 | No catalog label | Unverified legacy |
| 11837 | 2002-05-31 | No catalog label | Unverified legacy |
| 11854 | 2005-06-22 | Rogues' Den | Reviewed group |
| 11855 | 2005-06-22 | Rogues' Den | Reviewed group |
| 11924 | 2006-03-15 | Mogre Camp | Reviewed group |
| 12079 | 2002-09-30 | Tutorial Island | Unverified legacy |
| 12080 | 2002-09-30 | Tutorial Island | Unverified legacy |
| 12081 | 2001-04-30 | Port Sarim | Unverified legacy |
| 12082 | 2001-04-30 | Port Sarim | Unverified legacy |
| 12083 | 2001-04-30 | Falador Farm | Unverified legacy |
| 12084 | 2001-04-30 | Falador | Unverified legacy |
| 12085 | 2001-04-30 | Asgarnia | Unverified legacy |
| 12086 | 2001-04-30 | Asgarnia | Unverified legacy |
| 12087 | 2001-08-31 | No catalog label | Unverified legacy |
| 12088 | 2002-02-28 | No catalog label | Unverified legacy |
| 12089 | 2002-02-28 | No catalog label | Unverified legacy |
| 12090 | 2001-08-31 | No catalog label | Unverified legacy |
| 12091 | 2001-08-31 | No catalog label | Unverified legacy |
| 12092 | 2001-08-31 | No catalog label | Unverified legacy |
| 12093 | 2002-05-31 | No catalog label | Unverified legacy |
| 12109 | 2005-06-22 | Rogues' Den | Reviewed group |
| 12110 | 2005-06-22 | Rogues' Den | Reviewed group |
| 12111 | 2005-06-22 | Rogues' Den | Reviewed group |
| 12336 | 2002-09-30 | Tutorial Island | Unverified legacy |
| 12337 | 2001-01-04 | Wizards' Tower | Unverified legacy |
| 12338 | 2001-01-04 | Draynor | Unverified legacy |
| 12339 | 2001-01-04 | Draynor | Unverified legacy |
| 12340 | 2001-01-04 | Draynor Manor | Unverified legacy |
| 12341 | 2001-01-04 | Barbarian Village | Unverified legacy |
| 12342 | 2001-01-04 | Edgeville | Unverified legacy |
| 12343 | 2001-08-31 | No catalog label | Unverified legacy |
| 12345 | 2001-08-31 | No catalog label | Unverified legacy |
| 12346 | 2001-08-31 | No catalog label | Unverified legacy |
| 12347 | 2001-08-31 | No catalog label | Unverified legacy |
| 12348 | 2001-08-31 | No catalog label | Unverified legacy |
| 12349 | 2003-09-30 | No catalog label | Unverified legacy |
| 12423 | 2006-01-23 | Enakhra's Temple | Reviewed group |
| 12591 | 2003-04-30 | Bedabin Camp | Unverified legacy |
| 12592 | 2002-09-30 | Tutorial Island | Unverified legacy |
| 12593 | 2001-01-04 | Lumbridge Swamp | Unverified legacy |
| 12594 | 2001-01-04 | Misthalin | Unverified legacy |
| 12595 | 2001-01-04 | Misthalin | Unverified legacy |
| 12596 | 2001-01-04 | Varrock | Unverified legacy |
| 12597 | 2001-01-04 | Varrock | Unverified legacy |
| 12599 | 2001-08-31 | No catalog label | Unverified legacy |
| 12601 | 2001-08-31 | No catalog label | Unverified legacy |
| 12602 | 2002-05-31 | No catalog label | Unverified legacy |
| 12603 | 2002-02-28 | No catalog label | Unverified legacy |
| 12604 | 2002-02-28 | No catalog label | Unverified legacy |
| 12605 | 2002-05-31 | No catalog label | Unverified legacy |
| 12615 | 2007-01-22 | Tarn's Lair | Reviewed group |
| 12616 | 2007-01-22 | Tarn's Lair | Reviewed group |
| 12625 | 2007-03-27 | Tunnel of Chaos | Reviewed group |
| 12693 | 2005-03-14 | Lumbridge Swamp Caves | Reviewed group |
| 12694 | 2005-02-22 | H.A.M. Hideout | Reviewed group |
| 12845 | 2005-04-18 | Kharidian Desert | Unverified legacy |
| 12847 | 2003-04-30 | Kharidian Desert | Unverified legacy |
| 12848 | 2003-04-30 | Kharidian Desert | Unverified legacy |
| 12849 | 2001-01-04 | Lumbridge Swamp | Unverified legacy |
| 12850 | 2001-01-04 | Lumbridge | Unverified legacy |
| 12851 | 2001-01-04 | Misthalin | Unverified legacy |
| 12852 | 2001-01-04 | Varrock | Unverified legacy |
| 12853 | 2001-01-04 | Varrock | Unverified legacy |
| 12854 | 2001-01-04 | Varrock | Unverified legacy |
| 12855 | 2001-08-31 | No catalog label | Unverified legacy |
| 12856 | 2001-08-31 | No catalog label | Unverified legacy |
| 12857 | 2001-08-31 | No catalog label | Unverified legacy |
| 12858 | 2001-08-31 | No catalog label | Unverified legacy |
| 12859 | 2002-02-28 | No catalog label | Unverified legacy |
| 12860 | 2002-02-28 | No catalog label | Unverified legacy |
| 12861 | 2002-05-31 | No catalog label | Unverified legacy |
| 12946 | 2005-04-18 | Smoke Dungeon | Reviewed group |
| 12948 | 2005-05-04 | Chasm of Tears | Reviewed group |
| 12949 | 2005-03-14 | Lumbridge Swamp Caves | Reviewed group |
| 12950 | 2005-05-31 | Dorgeshuun Mines | Reviewed group |
| 13099 | 2005-04-26 | Sophanem | Unverified legacy |
| 13103 | 2003-04-30 | Kharidian Desert | Unverified legacy |
| 13104 | 2003-04-30 | Kharidian Desert | Unverified legacy |
| 13105 | 2001-01-04 | Al Kharid | Unverified legacy |
| 13106 | 2001-01-04 | Al Kharid | Unverified legacy |
| 13107 | 2001-01-04 | Al Kharid Mine | Unverified legacy |
| 13108 | 2001-01-04 | Varrock | Unverified legacy |
| 13109 | 2001-01-04 | Varrock | Unverified legacy |
| 13110 | 2001-01-04 | Varrock | Unverified legacy |
| 13111 | 2001-08-31 | No catalog label | Unverified legacy |
| 13112 | 2001-08-31 | No catalog label | Unverified legacy |
| 13113 | 2001-08-31 | No catalog label | Unverified legacy |
| 13114 | 2001-08-31 | No catalog label | Unverified legacy |
| 13115 | 2001-08-31 | No catalog label | Unverified legacy |
| 13116 | 2001-08-31 | No catalog label | Unverified legacy |
| 13117 | 2002-05-31 | No catalog label | Unverified legacy |
| 13200 | 2007-01-10 | Sophanem Dungeon | Reviewed group |
| 13202 | 2005-04-18 | Smoke Dungeon | Reviewed group |
| 13206 | 2005-05-31 | Dorgeshuun Mines | Reviewed group |
| 13209 | 2006-04-03 | Dungeon of Tolna | Reviewed group |
| 13358 | 2005-04-04 | Pollnivneach | Unverified legacy |
| 13362 | 2004-03-31 | Emir's Arena | Unverified legacy |
| 13363 | 2004-03-31 | Emir's Arena | Unverified legacy |
| 13364 | 2003-07-31 | Exam Centre | Unverified legacy |
| 13365 | 2003-07-31 | Digsite | Unverified legacy |
| 13366 | 2004-06-30 | Silvarea | Unverified legacy |
| 13367 | 2004-03-31 | No catalog label | Unverified legacy |
| 13368 | 2004-03-31 | No catalog label | Unverified legacy |
| 13369 | 2004-03-31 | No catalog label | Unverified legacy |
| 13370 | 2004-03-31 | No catalog label | Unverified legacy |
| 13462 | 2006-01-04 | Mage Training Arena | Reviewed group |
| 13463 | 2006-01-04 | Mage Training Arena | Reviewed group |
| 13613 | 2005-12-05 | Nardah | Unverified legacy |
| 13618 | 2004-12-30 | Abandoned Mine | Unverified legacy |
| 13619 | 2004-10-31 | Morytania | Unverified legacy |
| 13620 | 2004-07-30 | Morytania | Unverified legacy |
| 13621 | 2004-07-30 | Morytania | Unverified legacy |
| 13622 | 2004-06-30 | Morytania | Unverified legacy |
| 13623 | 2005-01-31 | Slayer Tower | Unverified legacy |
| 13624 | 2005-01-31 | No catalog label | Unverified legacy |
| 13872 | 2005-04-11 | Uzer | Unverified legacy |
| 13873 | 2006-03-22 | Burgh de Rott | Reviewed group |
| 13874 | 2006-03-22 | Burgh de Rott | Reviewed group |
| 13875 | 2004-10-31 | Mort'ton | Unverified legacy |
| 13876 | 2004-07-30 | Morytania | Unverified legacy |
| 13877 | 2004-07-30 | Morytania | Unverified legacy |
| 13878 | 2004-07-30 | Canifis | Unverified legacy |
| 13879 | 2005-01-31 | Morytania | Unverified legacy |
| 13979 | 2005-01-31 | Experiment Cave | Reviewed group |
| 14129 | 2006-03-22 | Burgh de Rott | Reviewed group |
| 14130 | 2006-03-22 | Burgh de Rott | Reviewed group |
| 14131 | 2005-05-09 | Barrows | Reviewed group |
| 14132 | 2006-09-04 | Meiyerditch | Reviewed group |
| 14134 | 2004-10-31 | Morytania | Unverified legacy |
| 14135 | 2005-01-31 | Fenkenstrain's Castle | Unverified legacy |
| 14231 | 2005-05-09 | Barrows | Reviewed group |
| 14234 | 2005-01-31 | Werewolf Agility Course | Reviewed group |
| 14235 | 2005-01-31 | Experiment Cave | Reviewed group |
| 14385 | 2006-09-04 | Meiyerditch | Reviewed group |
| 14386 | 2006-09-04 | Meiyerditch | Reviewed group |
| 14387 | 2006-09-04 | Meiyerditch | Reviewed group |
| 14390 | 2004-10-31 | Morytania | Unverified legacy |
| 14638 | 2006-02-07 | Mos Le'Harmless | Unverified legacy |
| 14639 | 2006-02-07 | Mos Le'Harmless | Unverified legacy |
| 14646 | 2005-02-15 | Port Phasmatys | Reviewed group |
| 14894 | 2006-02-07 | Mos Le'Harmless | Unverified legacy |
| 14895 | 2006-02-07 | Mos Le'Harmless | Unverified legacy |
| 14994 | 2006-07-04 | Mos Le'Harmless Caves | Reviewed group |
| 14995 | 2006-07-04 | Mos Le'Harmless Caves | Reviewed group |
| 15148 | 2007-03-06 | Harmony Island | Unverified legacy |
| 15150 | 2006-07-04 | Trouble Brewing | Reviewed group |
| 15151 | 2006-02-07 | Mos Le'Harmless | Unverified legacy |
| 15251 | 2006-07-04 | Mos Le'Harmless Caves | Reviewed group |

## Full named-area comparison

This table is a triage list, not an automatic change list. “Missing” is expected for post-backup content. Date differences can be legitimate when an area contains older terrain or later extensions.

| Location and date source | Reported date | Regions absent from configured data | Existing regions with different date |
|---|---|---|---|
| [Abyssal Sire](https://oldschool.runescape.wiki/w/Abyssal_Sire) | 2015-10-01 | 11851, 11850, 12363, 12362 | None |
| [Araxxor](https://oldschool.runescape.wiki/w/Araxxor) | 2024-08-28 | 14489 | None |
| [Cerberus](https://oldschool.runescape.wiki/w/Cerberus) | 2015-08-27 | 4883, 5140, 5395 | None |
| [Commander Zilyana](https://oldschool.runescape.wiki/w/Commander_Zilyana) | 2013-10-17 | 11602 | None |
| [Corporeal Beast](https://oldschool.runescape.wiki/w/Corporeal_Beast) | 2014-10-16 | 11842, 11844 | None |
| [Dagannoth Kings](https://oldschool.runescape.wiki/w/Dagannoth_Kings) | 2005-11-07 | 11588, 11589 | None |
| [Duke Sucellus](https://oldschool.runescape.wiki/w/Duke_Sucellus) | 2023-07-26 | 12132 | None |
| [General Graardor](https://oldschool.runescape.wiki/w/General_Graardor) | 2013-10-17 | 11347 | None |
| [Giant Mole](https://oldschool.runescape.wiki/w/Giant_Mole) | 2006-03-07 | 6993, 6992 | None |
| [Grotesque Guardians](https://oldschool.runescape.wiki/w/Grotesque_Guardians) | 2017-10-26 | 6727 | None |
| [Hespori](https://oldschool.runescape.wiki/w/Hespori) | 2019-01-10 | 5021 | None |
| [Alchemical Hydra](https://oldschool.runescape.wiki/w/Alchemical_Hydra) | 2019-01-10 | 5536 | None |
| [Kalphite Queen](https://oldschool.runescape.wiki/w/Kalphite_Queen) | 2004-09-07 | 13972 | None |
| [Kraken](https://oldschool.runescape.wiki/w/Kraken) | 2014-04-10 | 9116 | None |
| [Kree'arra](https://oldschool.runescape.wiki/w/Kree'arra) | 2013-10-17 | 11346 | None |
| [K'ril Tsutsaroth](https://oldschool.runescape.wiki/w/K'ril_Tsutsaroth) | 2013-10-17 | 11603 | None |
| [Nex](https://oldschool.runescape.wiki/w/Nex) | 2022-01-05 | 11601 | None |
| [Nightmare of Ashihama](https://oldschool.runescape.wiki/w/Nightmare_of_Ashihama) | 2020-02-06 | 15515 | None |
| [Phantom Muspah](https://oldschool.runescape.wiki/w/Phantom_Muspah) | 2023-01-11 | 11330 | None |
| [Sarachnis](https://oldschool.runescape.wiki/w/Sarachnis) | 2019-07-04 | 7322 | None |
| [Skotizo](https://oldschool.runescape.wiki/w/Skotizo) | 2016-06-16 | 9048 | None |
| [Thermonuclear smoke devil](https://oldschool.runescape.wiki/w/Thermonuclear_smoke_devil) | 2014-04-10 | 9363, 9619 | None |
| [Tempoross](https://oldschool.runescape.wiki/w/Tempoross) | 2021-03-24 | 12076 | None |
| [The Leviathan](https://oldschool.runescape.wiki/w/The_Leviathan) | 2023-07-26 | 8291 | None |
| [The Royal Titans](https://oldschool.runescape.wiki/w/The_Royal_Titans) | 2025-02-05 | 11669 | None |
| [The Whisperer](https://oldschool.runescape.wiki/w/The_Whisperer) | 2023-07-26 | 10595 | None |
| [Vardorvis](https://oldschool.runescape.wiki/w/Vardorvis) | 2023-07-26 | 4405 | None |
| [Vorkath](https://oldschool.runescape.wiki/w/Vorkath) | 2018-01-04 | 9023 | None |
| [Wintertodt](https://oldschool.runescape.wiki/w/Wintertodt) | 2016-09-08 | 6462 | None |
| [Yama](https://oldschool.runescape.wiki/w/Yama) | 2025-05-14 | 6045 | None |
| [Zalcano](https://oldschool.runescape.wiki/w/Zalcano) | 2019-07-25 | 12126 | None |
| [Zulrah](https://oldschool.runescape.wiki/w/Zulrah) | 2015-01-08 | 9007 | None |
| [Al Kharid](https://oldschool.runescape.wiki/w/Al_Kharid) | 2001-01-19 | None | 13105: 2001-01-04, 13106: 2001-01-04 |
| [Arceuus](https://oldschool.runescape.wiki/w/Arceuus) | 2016-01-07 | 6458, 6459, 6460, 6714, 6715 | None |
| [Ardougne](https://oldschool.runescape.wiki/w/Ardougne) | 2002-04-30 | None | 9779: 2002-07-31, 9780: 2002-12-31, 10035: 2002-08-31, 10036: 2002-08-31 |
| [Bandit Camp](https://oldschool.runescape.wiki/w/Bandit_Camp) | Unverified | 12590 | None |
| [Barbarian Outpost](https://oldschool.runescape.wiki/w/Barbarian_Outpost) | 2002-03-25 | None | 10039: 2002-12-31 |
| [Barbarian Village](https://oldschool.runescape.wiki/w/Barbarian_Village) | 2001-01-04 | None | None |
| [Bedabin Camp](https://oldschool.runescape.wiki/w/Bedabin_Camp) | 2003-04-14 | None | 12591: 2003-04-30 |
| [Brimhaven](https://oldschool.runescape.wiki/w/Brimhaven) | 2002-02-27 | None | 11057: 2002-02-28, 11058: 2002-02-28 |
| [Burgh de Rott](https://oldschool.runescape.wiki/w/Burgh_de_Rott) | 2006-03-22 | None | None |
| [Burthorpe](https://oldschool.runescape.wiki/w/Burthorpe) | 2004-08-09 | None | 11319: 2004-08-30, 11575: 2004-08-30 |
| [Cam Torum](https://oldschool.runescape.wiki/w/Cam_Torum) | 2024-03-20 | 5525, 5780, 5781, 6037 | None |
| [Canifis](https://oldschool.runescape.wiki/w/Canifis) | 2004-06-29 | None | 13878: 2004-07-30 |
| [Catherby](https://oldschool.runescape.wiki/w/Catherby) | 2002-02-27 | None | 11317: 2002-02-28, 11318: 2002-02-28, 11061: 2002-02-28 |
| [Civitas Illa Fortis](https://oldschool.runescape.wiki/w/Civitas_Illa_Fortis) | Unverified | 6448, 6449, 6704, 6705, 6960, 6961 | None |
| [Corsair Cove](https://oldschool.runescape.wiki/w/Corsair_Cove) | 2017-12-07 | 10028, 10284 | None |
| [Darkmeyer](https://oldschool.runescape.wiki/w/Darkmeyer) | 2020-06-04 | 14388, 14644 | None |
| [Dorgesh-Kaan](https://oldschool.runescape.wiki/w/Dorgesh-Kaan) | 2007-03-20 | None | None |
| [Draynor](https://oldschool.runescape.wiki/w/Draynor) | 2001-01-28 | None | 12338: 2001-01-04, 12339: 2001-01-04 |
| [Edgeville](https://oldschool.runescape.wiki/w/Edgeville) | 2001-01-04 | None | None |
| [Entrana](https://oldschool.runescape.wiki/w/Entrana) | 2002-02-27 | None | 11060: 2002-02-28, 11316: 2002-02-28 |
| [Etceteria](https://oldschool.runescape.wiki/w/Etceteria) | 2004-11-29 | None | 10300: 2004-11-30 |
| [Falador](https://oldschool.runescape.wiki/w/Falador) | 2001-04-06 | None | 11828: 2001-04-30, 11572: 2002-02-28, 11827: 2001-04-30, 12084: 2001-04-30 |
| [Gu'Tanoth](https://oldschool.runescape.wiki/w/Gu'Tanoth) | 2003-05-07 | None | 10031: 2003-05-31 |
| [Gwenith](https://oldschool.runescape.wiki/w/Gwenith) | 2019-07-25 | 8757 | None |
| [Hosidius](https://oldschool.runescape.wiki/w/Hosidius) | 2016-01-07 | 6710, 6711, 6712, 6455, 6456, 6966, 6967, 6968, 7221, 7223, 7224, 7478, 7479 | None |
| [Jatizso](https://oldschool.runescape.wiki/w/Jatizso) | 2007-02-06 | None | None |
| [Keldagrim](https://oldschool.runescape.wiki/w/Keldagrim) | 2005-05-31 | None | None |
| [Land's End](https://oldschool.runescape.wiki/w/Land's_End) | 2016-11-17 | 5941 | None |
| [Lassar Undercity](https://oldschool.runescape.wiki/w/Lassar_Undercity) | 2023-07-26 | 9314, 9315, 9316, 9571, 9572, 9828, 10338, 10339, 10340, 10596, 10852 | None |
| [Lletya](https://oldschool.runescape.wiki/w/Lletya) | 2005-07-19 | 9265, 11103 | None |
| [Lovakengj](https://oldschool.runescape.wiki/w/Lovakengj) | 2016-01-07 | 5692, 5691, 5947, 6203, 6202, 5690, 5946 | None |
| [Lumbridge](https://oldschool.runescape.wiki/w/Lumbridge) | 2001-01-04 | None | None |
| [Lunar Isle](https://oldschool.runescape.wiki/w/Lunar_Isle) | 2006-07-24 | None | None |
| [Marim](https://oldschool.runescape.wiki/w/Marim) | 2004-12-06 | None | 11051: 2004-12-30 |
| [Meiyerditch](https://oldschool.runescape.wiki/w/Meiyerditch) | 2006-09-04 | None | None |
| [Menaphos](https://oldschool.runescape.wiki/w/Menaphos) | 2005-04-26 | 12843 | None |
| [Miscellania](https://oldschool.runescape.wiki/w/Miscellania) | 2004-11-29 | None | 10044: 2004-11-30 |
| [Mor Ul Rek](https://oldschool.runescape.wiki/w/Mor_Ul_Rek) | 2005-09-19 | 9807, 10064, 10063 | None |
| [Mort'ton](https://oldschool.runescape.wiki/w/Mort'ton) | 2004-10-18 | None | 13875: 2004-10-31 |
| [Mos Le'Harmless](https://oldschool.runescape.wiki/w/Mos_Le'Harmless) | 2006-02-07 | 14637, 15406, 15407 | None |
| [Mount Karuulm](https://oldschool.runescape.wiki/w/Mount_Karuulm) | 2019-01-10 | 5179, 4923, 5180 | None |
| [Mountain Camp](https://oldschool.runescape.wiki/w/Mountain_Camp) | 2005-03-07 | None | None |
| [Mynydd](https://oldschool.runescape.wiki/w/Mynydd) | 2019-07-25 | 8501 | None |
| [Nardah](https://oldschool.runescape.wiki/w/Nardah) | 2005-12-05 | None | None |
| [Neitiznot](https://oldschool.runescape.wiki/w/Neitiznot) | 2007-02-06 | None | None |
| [Port Piscarilius](https://oldschool.runescape.wiki/w/Port_Piscarilius) | 2016-01-07 | 6969, 6971, 7227, 6970, 7225, 7226 | None |
| [Piscatoris](https://oldschool.runescape.wiki/w/Piscatoris) | 2006-11-21 | None | 9273: 2006-05-02 |
| [Pollnivneach](https://oldschool.runescape.wiki/w/Pollnivneach) | 2005-04-04 | None | None |
| [Port Khazard](https://oldschool.runescape.wiki/w/Port_Khazard) | 2002-07-23 | None | 10545: 2002-07-31 |
| [Port Phasmatys](https://oldschool.runescape.wiki/w/Port_Phasmatys) | 2005-02-15 | None | None |
| [Port Sarim](https://oldschool.runescape.wiki/w/Port_Sarim) | 2001-04-06 | None | 12081: 2001-04-30, 12082: 2001-04-30 |
| [Prifddinas](https://oldschool.runescape.wiki/w/Prifddinas) | 2019-07-25 | 8499, 8500, 8756, 9012, 9013, 12894, 12895, 13150, 13151 | 8755: 2004-09-30, 9011: 2004-09-30 |
| [Rellekka](https://oldschool.runescape.wiki/w/Rellekka) | 2004-11-02 | None | 10297: 2004-11-30, 10553: 2004-11-30 |
| [Rimmington](https://oldschool.runescape.wiki/w/Rimmington) | 2001-04-06 | None | 11826: 2001-04-30, 11570: 2001-09-30 |
| [Seers' Village](https://oldschool.runescape.wiki/w/Seers'_Village) | 2002-03-25 | None | 10806: 2002-03-31 |
| [Shayzien](https://oldschool.runescape.wiki/w/Shayzien) | 2016-01-07 | 5944, 5943, 6200, 6199, 5686, 5687, 5688, 5689, 5945 | None |
| [Shilo Village](https://oldschool.runescape.wiki/w/Shilo_Village) | Unverified | None | None |
| [Slepe](https://oldschool.runescape.wiki/w/Slepe) | 2018-05-24 | 14643, 14899, 14900, 14901 | None |
| [Sophanem](https://oldschool.runescape.wiki/w/Sophanem) | 2005-04-26 | None | None |
| [Tai Bwo Wannai](https://oldschool.runescape.wiki/w/Tai_Bwo_Wannai) | 2002-10-23 | None | 11056: 2003-01-31, 11055: 2003-01-31 |
| [Taverley](https://oldschool.runescape.wiki/w/Taverley) | 2002-02-27 | None | 11574: 2002-02-28, 11573: 2002-02-28 |
| [Tree Gnome Stronghold](https://oldschool.runescape.wiki/w/Tree_Gnome_Stronghold) | 2002-12-12 | None | 9525: 2002-12-31, 9526: 2002-12-31, 9782: 2002-12-31, 9781: 2002-12-31 |
| [Tree Gnome Village](https://oldschool.runescape.wiki/w/Tree_Gnome_Village) | Unverified | None | None |
| [Troll Stronghold](https://oldschool.runescape.wiki/w/Troll_Stronghold) | Unverified | 11421 | None |
| [Uzer](https://oldschool.runescape.wiki/w/Uzer) | 2005-04-11 | None | None |
| [Uzer Oasis](https://oldschool.runescape.wiki/w/Uzer_Oasis) | 2023-01-11 | 13871 | None |
| [Varrock](https://oldschool.runescape.wiki/w/Varrock) | 2001-01-04 | None | None |
| [Ver Sinhaza](https://oldschool.runescape.wiki/w/Ver_Sinhaza) | 2018-05-24 | 14642 | None |
| [Void Knights' Outpost](https://oldschool.runescape.wiki/w/Void_Knights'_Outpost) | 2006-04-18 | None | None |
| [Weiss](https://oldschool.runescape.wiki/w/Weiss) | 2018-09-06 | 11325, 11581 | None |
| [Witchaven](https://oldschool.runescape.wiki/w/Witchaven) | 2006-09-04 | None | 10803: 2002-04-30 |
| [Yanille](https://oldschool.runescape.wiki/w/Yanille) | 2002-10-23 | None | 10288: 2002-10-31, 10032: 2002-10-31 |
| [Zanaris](https://oldschool.runescape.wiki/w/Zanaris) | 2002-02-27 | 9285, 9541, 9540, 9797 | None |
| [Zul-Andra](https://oldschool.runescape.wiki/w/Zul-Andra) | 2015-01-08 | 8495, 8751 | None |
| [Abandoned Mine](https://oldschool.runescape.wiki/w/Abandoned_Mine) | 2004-12-21 | 13718, 11079, 11078, 11077, 10823, 10822, 10821 | 13618: 2004-12-30 |
| [Ah Za Rhoon](https://oldschool.runescape.wiki/w/Ah_Za_Rhoon) | 2003-01-27 | 11666 | None |
| [Ancient Cavern](https://oldschool.runescape.wiki/w/Ancient_Cavern) | 2007-07-03 | 6483, 6995 | None |
| [Ape Atoll Dungeon](https://oldschool.runescape.wiki/w/Ape_Atoll_Dungeon) | 2004-12-06 | 11150, 10894 | None |
| [Ape Atoll Banana Plantation](https://oldschool.runescape.wiki/w/Ape_Atoll_Banana_Plantation) | Unverified | 10895 | None |
| [West Ardougne Basement](https://oldschool.runescape.wiki/w/West_Ardougne_Basement) | Unverified | 10135 | None |
| [Ardougne Sewers](https://oldschool.runescape.wiki/w/Ardougne_Sewers) | 2002-08-27 | 10134, 10136, 10391 | 10647: 2005-08-01 |
| [Asgarnian Ice Caves](https://oldschool.runescape.wiki/w/Asgarnian_Ice_Caves) | 2001-04-06 | 11925, 12181 | None |
| [Tomb of Bervirius](https://oldschool.runescape.wiki/w/Tomb_of_Bervirius) | 2003-01-27 | 11154 | None |
| [Brimhaven Dungeon](https://oldschool.runescape.wiki/w/Brimhaven_Dungeon) | 2005-01-17 | 10901, 10900, 10899, 10645, 10644, 10643 | None |
| [Brine Rat Cavern](https://oldschool.runescape.wiki/w/Brine_Rat_Cavern) | 2007-04-10 | None | None |
| [Catacombs of Kourend](https://oldschool.runescape.wiki/w/Catacombs_of_Kourend) | 2016-06-09 | 6557, 6556, 6813, 6812 | None |
| [Champions' Challenge](https://oldschool.runescape.wiki/w/Champions'_Challenge) | 2005-12-12 | 12696 | None |
| [Chaos Druid Tower](https://oldschool.runescape.wiki/w/Chaos_Druid_Tower) | 2002-04-30 | 10392 | None |
| [Chasm of Fire](https://oldschool.runescape.wiki/w/Chasm_of_Fire) | 2017-04-27 | 5789 | None |
| [Chasm of Tears](https://oldschool.runescape.wiki/w/Chasm_of_Tears) | 2005-05-04 | None | None |
| [Chinchompa Hunting Ground](https://oldschool.runescape.wiki/w/Chinchompa_Hunting_Ground) | Unverified | 10129 | None |
| [Civitas illa Fortis Underground](https://oldschool.runescape.wiki/w/Civitas_illa_Fortis_Underground) | Unverified | 6549, 6804, 6805 | None |
| [Clock Tower Basement](https://oldschool.runescape.wiki/w/Clock_Tower_Basement) | 2002-06-17 | 10390 | None |
| [Corsair Cove Dungeon](https://oldschool.runescape.wiki/w/Corsair_Cove_Dungeon) | 2017-12-07 | 8076, 8332 | None |
| [Crabclaw Caves](https://oldschool.runescape.wiki/w/Crabclaw_Caves) | 2017-11-09 | 6553, 6809 | None |
| [Crandor Dungeon](https://oldschool.runescape.wiki/w/Crandor_Dungeon) | 2001-06-11 | 11414 | None |
| [Crash Site Cavern](https://oldschool.runescape.wiki/w/Crash_Site_Cavern) | 2016-05-06 | 8280, 8536 | None |
| [Crumbling Tower](https://oldschool.runescape.wiki/w/Crumbling_Tower) | 2021-02-03 | 7827 | None |
| [Daeyalt Essence Mine](https://oldschool.runescape.wiki/w/Daeyalt_Essence_Mine) | 2020-06-04 | 14744 | None |
| [Digsite Dungeon](https://oldschool.runescape.wiki/w/Digsite_Dungeon) | 2003-07-09 | 13464, 13465 | None |
| [Dorgesh-Kaan South Dungeon](https://oldschool.runescape.wiki/w/Dorgesh-Kaan_South_Dungeon) | 2007-03-20 | None | None |
| [Dorgeshuun Mines](https://oldschool.runescape.wiki/w/Dorgeshuun_Mines) | 2005-05-31 | None | None |
| [Draynor Sewers](https://oldschool.runescape.wiki/w/Draynor_Sewers) | 2005-04-18 | 12439, 12438 | None |
| [Dwarven Mines](https://oldschool.runescape.wiki/w/Dwarven_Mines) | 2001-04-06 | 12185, 12184, 12183 | None |
| [Eagles' Peak Dungeon](https://oldschool.runescape.wiki/w/Eagles'_Peak_Dungeon) | 2006-11-28 | 8013 | None |
| [Ectofuntus](https://oldschool.runescape.wiki/w/Ectofuntus) | 2005-02-15 | 14746 | None |
| [Edgeville Dungeon](https://oldschool.runescape.wiki/w/Edgeville_Dungeon) | 2001-01-04 | 12441, 12442, 12443, 12698 | None |
| [Elemental Workshop](https://oldschool.runescape.wiki/w/Elemental_Workshop) | Unverified | 10906, 7760 | None |
| [Elven rabbit cave](https://oldschool.runescape.wiki/w/Elven_rabbit_cave) | 2019-07-25 | 13252 | None |
| [Enakhra's Temple](https://oldschool.runescape.wiki/w/Enakhra's_Temple) | 2006-01-23 | None | None |
| [Evil Chicken's Lair](https://oldschool.runescape.wiki/w/Evil_Chicken's_Lair) | 2006-03-15 | None | None |
| [Experiment Cave](https://oldschool.runescape.wiki/w/Experiment_Cave) | 2005-01-31 | None | None |
| [Ferox Enclave Dungeon](https://oldschool.runescape.wiki/w/Ferox_Enclave_Dungeon) | 2020-07-16 | 12700 | None |
| [Forthos Dungeon](https://oldschool.runescape.wiki/w/Forthos_Dungeon) | 2019-07-04 | 7323 | None |
| [Fremennik Slayer Dungeon](https://oldschool.runescape.wiki/w/Fremennik_Slayer_Dungeon) | 2005-01-26 | None | None |
| [Glarial's Tomb](https://oldschool.runescape.wiki/w/Glarial's_Tomb) | 2002-09-24 | 10137 | None |
| [Goblin Cave](https://oldschool.runescape.wiki/w/Goblin_Cave) | 2003-05-27 | 10393 | None |
| [Grand Tree Tunnels](https://oldschool.runescape.wiki/w/Grand_Tree_Tunnels) | 2002-12-12 | 9882 | None |
| [H.A.M. Hideout](https://oldschool.runescape.wiki/w/H.A.M._Hideout) | 2005-02-22 | None | None |
| [H.A.M. Store room](https://oldschool.runescape.wiki/w/H.A.M._Store_room) | 2006-06-21 | None | None |
| [Heroes' Guild Mine](https://oldschool.runescape.wiki/w/Heroes'_Guild_Mine) | 2002-02-27 | 11674 | None |
| [Iorwerth Dungeon](https://oldschool.runescape.wiki/w/Iorwerth_Dungeon) | 2019-07-25 | 12737, 12738, 12993, 12994 | None |
| [Isle of Souls Dungeon](https://oldschool.runescape.wiki/w/Isle_of_Souls_Dungeon) | 2021-02-03 | 8593 | None |
| [Jatizso Mines](https://oldschool.runescape.wiki/w/Jatizso_Mines) | 2007-02-06 | None | None |
| [Jiggig Burial Tomb](https://oldschool.runescape.wiki/w/Jiggig_Burial_Tomb) | 2005-05-17 | None | None |
| [Jogre Dungeon](https://oldschool.runescape.wiki/w/Jogre_Dungeon) | 2002-10-23 | 11412 | None |
| [Karamja Dungeon](https://oldschool.runescape.wiki/w/Karamja_Dungeon) | 2001-06-11 | 11413 | None |
| [Karuulm Slayer Dungeon](https://oldschool.runescape.wiki/w/Karuulm_Slayer_Dungeon) | 2019-01-10 | 5280, 5279, 5023, 5535, 5022, 4766, 4510, 4511, 4767, 4768, 4512 | None |
| [KGP Headquarters](https://oldschool.runescape.wiki/w/KGP_Headquarters) | 2007-01-29 | None | None |
| [Kruk's Dungeon](https://oldschool.runescape.wiki/w/Kruk's_Dungeon) | 2016-05-06 | 9358, 9359, 9360, 9615, 9616, 9871, 10125, 10126, 10127, 10128, 10381, 10382, 10383, 10384, 10637, 10638, 10639, 10640 | None |
| [Legends' Guild Dungeon](https://oldschool.runescape.wiki/w/Legends'_Guild_Dungeon) | Unverified | 10904 | None |
| [Lighthouse](https://oldschool.runescape.wiki/w/Lighthouse) | 2004-11-17 | 10140 | None |
| [Lizardman Caves](https://oldschool.runescape.wiki/w/Lizardman_Caves) | 2017-07-13 | 5275 | None |
| [Lizardman Temple](https://oldschool.runescape.wiki/w/Lizardman_Temple) | 2019-01-10 | 5277 | None |
| [Lumbridge Swamp Caves](https://oldschool.runescape.wiki/w/Lumbridge_Swamp_Caves) | 2005-03-14 | None | None |
| [Lunar Isle Mine](https://oldschool.runescape.wiki/w/Lunar_Isle_Mine) | 2006-07-24 | None | None |
| [Maniacal Monkey Hunter Area](https://oldschool.runescape.wiki/w/Maniacal_Monkey_Hunter_Area) | Unverified | 11662 | None |
| [Meiyerditch Mine](https://oldschool.runescape.wiki/w/Meiyerditch_Mine) | 2006-09-04 | None | None |
| [Miscellania Dungeon](https://oldschool.runescape.wiki/w/Miscellania_Dungeon) | 2006-05-22 | None | None |
| [Mogre Camp](https://oldschool.runescape.wiki/w/Mogre_Camp) | 2006-03-15 | None | None |
| [Mos Le'Harmless Caves](https://oldschool.runescape.wiki/w/Mos_Le'Harmless_Caves) | 2006-07-04 | None | None |
| [Motherlode Mine](https://oldschool.runescape.wiki/w/Motherlode_Mine) | 2014-04-24 | 14679, 14680, 14681, 14935, 14936, 14937, 15191, 15192, 15193 | None |
| [Mourner Tunnels](https://oldschool.runescape.wiki/w/Mourner_Tunnels) | 2005-07-19 | 7752, 8008 | None |
| [Mouse Hole](https://oldschool.runescape.wiki/w/Mouse_Hole) | 2007-06-04 | None | None |
| [Meiyerditch Laboratories](https://oldschool.runescape.wiki/w/Meiyerditch_Laboratories) | 2020-06-04 | 14232, 14233, 14487, 14488 | None |
| [Myreque Hideout](https://oldschool.runescape.wiki/w/Myreque_Hideout) | 2005-01-10 | 13721, 13974, 13977, 13978 | None |
| [Myths' Guild Dungeon](https://oldschool.runescape.wiki/w/Myths'_Guild_Dungeon) | 2017-12-07 | 7564, 7820, 7821 | None |
| [Observatory Dungeon](https://oldschool.runescape.wiki/w/Observatory_Dungeon) | 2003-03-17 | 9362 | None |
| [Ogre Enclave](https://oldschool.runescape.wiki/w/Ogre_Enclave) | 2003-05-07 | 10387 | None |
| [Ourania Cave](https://oldschool.runescape.wiki/w/Ourania_Cave) | 2016-10-13 | 12119 | None |
| [Quidamortem Cave](https://oldschool.runescape.wiki/w/Quidamortem_Cave) | 2018-04-19 | 4763 | None |
| [Rashiliyta's Tomb](https://oldschool.runescape.wiki/w/Rashiliyta's_Tomb) | Unverified | 11668 | None |
| [Ruins of Camdozaal](https://oldschool.runescape.wiki/w/Ruins_of_Camdozaal) | 2021-04-14 | 11609, 11610, 11611, 11865, 11866, 11867, 12121, 12122, 12123 | None |
| [Salt Mine](https://oldschool.runescape.wiki/w/Salt_Mine) | 2018-09-06 | 11425 | None |
| [Saradomin Shrine (Paterdomus)](https://oldschool.runescape.wiki/w/Saradomin_Shrine_(Paterdomus)) | Unverified | 13722 | None |
| [Shade Catacombs](https://oldschool.runescape.wiki/w/Shade_Catacombs) | 2004-10-18 | 13975 | None |
| [Shadow Dungeon](https://oldschool.runescape.wiki/w/Shadow_Dungeon) | 2005-04-18 | None | None |
| [Shayzien Crypts](https://oldschool.runescape.wiki/w/Shayzien_Crypts) | 2018-01-04 | 6043 | None |
| [Sisterhood Sanctuary](https://oldschool.runescape.wiki/w/Sisterhood_Sanctuary) | 2020-02-06 | 14999, 15000, 15001, 15255, 15256, 15257, 15511, 15512, 15513 | None |
| [Smoke Dungeon](https://oldschool.runescape.wiki/w/Smoke_Dungeon) | 2005-04-18 | None | None |
| [Sophanem Dungeon](https://oldschool.runescape.wiki/w/Sophanem_Dungeon) | 2007-01-10 | None | None |
| [Sourhog Cave](https://oldschool.runescape.wiki/w/Sourhog_Cave) | 2020-09-10 | 12695 | None |
| [Stronghold of Security](https://oldschool.runescape.wiki/w/Stronghold_of_Security) | 2006-07-04 | None | None |
| [Stronghold Slayer Cave](https://oldschool.runescape.wiki/w/Stronghold_Slayer_Cave) | 2014-01-23 | 9624, 9625, 9880, 9881 | None |
| [Tarn's Lair](https://oldschool.runescape.wiki/w/Tarn's_Lair) | 2007-01-22 | None | None |
| [Taverley Dungeon](https://oldschool.runescape.wiki/w/Taverley_Dungeon) | 2002-02-27 | 11416, 11417, 11671, 11672, 11673, 11928, 11929 | None |
| [Temple of Ikov](https://oldschool.runescape.wiki/w/Temple_of_Ikov) | Unverified | 10649, 10905, 10650 | None |
| [Temple of Light](https://oldschool.runescape.wiki/w/Temple_of_Light) | 2005-10-17 | None | None |
| [Temple of Marimbo](https://oldschool.runescape.wiki/w/Temple_of_Marimbo) | 2004-12-06 | 11151 | None |
| [The Burrow](https://oldschool.runescape.wiki/w/The_Burrow) | 2024-03-20 | 6291 | None |
| [The Warrens](https://oldschool.runescape.wiki/w/The_Warrens) | 2017-11-09 | 7070, 7326 | None |
| [Dungeon of Tolna](https://oldschool.runescape.wiki/w/Dungeon_of_Tolna) | 2006-04-03 | None | None |
| [Tower of Life Basement](https://oldschool.runescape.wiki/w/Tower_of_Life_Basement) | Unverified | 12100 | None |
| [Trahaearn Mine](https://oldschool.runescape.wiki/w/Trahaearn_Mine) | 2019-07-25 | 13250 | None |
| [Tunnel of Chaos](https://oldschool.runescape.wiki/w/Tunnel_of_Chaos) | 2007-03-27 | None | None |
| [Underground Pass](https://oldschool.runescape.wiki/w/Underground_Pass) | Unverified | 9369, 9370 | None |
| [Varrock Sewers](https://oldschool.runescape.wiki/w/Varrock_Sewers) | 2001-01-04 | 12954, 13210 | None |
| [Viyeldi Caves](https://oldschool.runescape.wiki/w/Viyeldi_Caves) | 2003-08-20 | 9545, 11153 | None |
| [Warriors' Guild Basement](https://oldschool.runescape.wiki/w/Warriors'_Guild_Basement) | 2006-06-13 | 11675 | None |
| [Water Ravine](https://oldschool.runescape.wiki/w/Water_Ravine) | Unverified | 13461 | None |
| [Waterbirth Dungeon](https://oldschool.runescape.wiki/w/Waterbirth_Dungeon) | 2005-08-01 | 9886, 10142, 7492, 7748 | None |
| [Waterfall Dungeon](https://oldschool.runescape.wiki/w/Waterfall_Dungeon) | 2002-09-24 | 10394 | None |
| [Werewolf Agility Course](https://oldschool.runescape.wiki/w/Werewolf_Agility_Course) | 2005-01-31 | None | None |
| [White Wolf Mountain Caves](https://oldschool.runescape.wiki/w/White_Wolf_Mountain_Caves) | 2002-02-27 | 11418, 11419 | None |
| [Witchhaven Shrine Dungeon](https://oldschool.runescape.wiki/w/Witchhaven_Shrine_Dungeon) | Unverified | 10903 | None |
| [Wizards' Tower Basement](https://oldschool.runescape.wiki/w/Wizards'_Tower_Basement) | Unverified | 12437 | None |
| [Woodcutting Guild Dungeon](https://oldschool.runescape.wiki/w/Woodcutting_Guild_Dungeon) | Unverified | 6298 | None |
| [Wyvern Cave](https://oldschool.runescape.wiki/w/Wyvern_Cave) | 2017-09-07 | 14495, 14496 | None |
| [Yanille Agility Dungeon](https://oldschool.runescape.wiki/w/Yanille_Agility_Dungeon) | 2002-12-12 | 10388 | None |
| [Ardougne Rat Pits](https://oldschool.runescape.wiki/w/Ardougne_Rat_Pits) | 2005-11-28 | None | None |
| [Barbarian Assault](https://oldschool.runescape.wiki/w/Barbarian_Assault) | 2007-01-04 | None | None |
| [Barrows](https://oldschool.runescape.wiki/w/Barrows) | 2005-05-09 | None | None |
| [Blast Furnace](https://oldschool.runescape.wiki/w/Blast_Furnace) | 2005-08-23 | None | None |
| [Brimhaven Agility Arena](https://oldschool.runescape.wiki/w/Brimhaven_Agility_Arena) | 2004-07-27 | None | None |
| [Burthorpe Games Room](https://oldschool.runescape.wiki/w/Burthorpe_Games_Room) | 2004-11-24 | None | None |
| [Castle Wars](https://oldschool.runescape.wiki/w/Castle_Wars) | 2004-12-13 | None | None |
| [Clan Wars](https://oldschool.runescape.wiki/w/Clan_Wars) | 2014-06-19 | 12621, 12622, 12623, 13130, 13131, 13133, 13134, 13135, 13386, 13387, 13390, 13641, 13642, 13643, 13644, 13645, 13646, 13647, 13899, 13900, 14155, 14156 | None |
| [Emir's Arena](https://oldschool.runescape.wiki/w/Emir's_Arena) | 2022-07-13 | None | 13362: 2004-03-31, 13363: 2004-03-31 |
| [Fishing Trawler](https://oldschool.runescape.wiki/w/Fishing_Trawler) | 2003-07-28 | None | None |
| [Fortis Colosseum](https://oldschool.runescape.wiki/w/Fortis_Colosseum) | 2024-03-20 | 7216 | None |
| [Fortis Colosseum Lobby](https://oldschool.runescape.wiki/w/Fortis_Colosseum_Lobby) | Unverified | 7316 | None |
| [The Gauntlet](https://oldschool.runescape.wiki/w/The_Gauntlet) | 2019-07-25 | 12127, 7512 | None |
| [Corrupted Gauntlet](https://oldschool.runescape.wiki/w/Corrupted_Gauntlet) | 2019-07-25 | 7768 | None |
| [Giants' Foundry](https://oldschool.runescape.wiki/w/Giants'_Foundry) | 2022-06-08 | 13491 | None |
| [Guardians of the Rift](https://oldschool.runescape.wiki/w/Guardians_of_the_Rift) | 2022-03-23 | 14484 | None |
| [Hallowed Sepulchre](https://oldschool.runescape.wiki/w/Hallowed_Sepulchre) | 2020-06-04 | 8797, 9051, 9052, 9053, 9054, 9309, 9563, 9565, 9821, 10074, 10075, 10077 | None |
| [The Inferno](https://oldschool.runescape.wiki/w/The_Inferno) | 2017-06-01 | 9043 | None |
| [Keldagrim Rat Pits](https://oldschool.runescape.wiki/w/Keldagrim_Rat_Pits) | Unverified | 7753 | None |
| [LMS - Deserted Island](https://oldschool.runescape.wiki/w/LMS_-_Deserted_Island) | Unverified | 13658, 13659, 13660, 13914, 13915, 13916 | None |
| [LMS - Wild Varrock](https://oldschool.runescape.wiki/w/LMS_-_Wild_Varrock) | Unverified | 13918, 13919, 13920, 14174, 14175, 14176, 14430, 14431, 14432 | None |
| [Mage Training Arena](https://oldschool.runescape.wiki/w/Mage_Training_Arena) | 2006-01-04 | None | None |
| [Nightmare Zone](https://oldschool.runescape.wiki/w/Nightmare_Zone) | 2013-09-05 | 9033 | None |
| [Pest Control](https://oldschool.runescape.wiki/w/Pest_Control) | 2006-04-18 | None | None |
| [Port Sarim Rat Pits](https://oldschool.runescape.wiki/w/Port_Sarim_Rat_Pits) | Unverified | 11926 | None |
| [Pyramid Plunder](https://oldschool.runescape.wiki/w/Pyramid_Plunder) | 2006-07-17 | None | None |
| [Rogues' Den](https://oldschool.runescape.wiki/w/Rogues'_Den) | 2005-06-22 | None | None |
| [Sorceress's Garden](https://oldschool.runescape.wiki/w/Sorceress's_Garden) | 2007-02-12 | None | None |
| [Soul Wars](https://oldschool.runescape.wiki/w/Soul_Wars) | 2021-01-06 | 8493, 8748, 8749, 9005 | None |
| [Temple Trekking](https://oldschool.runescape.wiki/w/Temple_Trekking) | 2006-03-28 | 8014, 8270, 8256, 8782, 9038, 9294, 9550, 9806 | None |
| [Tithe Farm](https://oldschool.runescape.wiki/w/Tithe_Farm) | 2016-01-07 | 7222 | None |
| [Trouble Brewing](https://oldschool.runescape.wiki/w/Trouble_Brewing) | 2006-07-04 | None | None |
| [Tzhaar Fight Caves](https://oldschool.runescape.wiki/w/Tzhaar_Fight_Caves) | 2005-10-04 | None | None |
| [Tzhaar Fight Pits](https://oldschool.runescape.wiki/w/Tzhaar_Fight_Pits) | 2005-09-19 | None | None |
| [Varrock Rat Pits](https://oldschool.runescape.wiki/w/Varrock_Rat_Pits) | Unverified | 11599 | None |
| [Volcanic Mine](https://oldschool.runescape.wiki/w/Volcanic_Mine) | 2017-09-07 | 15263, 15262 | None |
| [Chambers of Xeric](https://oldschool.runescape.wiki/w/Chambers_of_Xeric) | 2017-01-05 | 12889, 13136, 13137, 13138, 13139, 13140, 13141, 13145, 13393, 13394, 13395, 13396, 13397, 13401 | None |
| [Theatre of Blood](https://oldschool.runescape.wiki/w/Theatre_of_Blood) | 2018-06-07 | 12611, 12612, 12613, 12867, 12869, 13122, 13123, 13125, 13379 | None |
| [Tombs of Amascut](https://oldschool.runescape.wiki/w/Tombs_of_Amascut) | 2022-08-24 | 14160, 14162, 14164, 14674, 14676, 15184, 15186, 15188, 15696, 15698, 15700 | None |
| [Jaltevas Pyramid](https://oldschool.runescape.wiki/w/Jaltevas_Pyramid) | 2022-04-27 | 13454 | None |
| [Osmumten's Burial Chamber](https://oldschool.runescape.wiki/w/Osmumten's_Burial_Chamber) | Unverified | 14672 | None |
| [Abyssal Area](https://oldschool.runescape.wiki/w/Abyssal_Area) | 2006-07-11 | 12108 | None |
| [Abyssal Nexus](https://oldschool.runescape.wiki/w/Abyssal_Nexus) | 2015-10-01 | 12106 | None |
| [Agility Pyramid](https://oldschool.runescape.wiki/w/Agility_Pyramid) | 2006-01-16 | 12105, 13356 | None |
| [Air Altar](https://oldschool.runescape.wiki/w/Air_Altar) | 2004-03-29 | None | None |
| [Al Kharid Mine](https://oldschool.runescape.wiki/w/Al_Kharid_Mine) | 2001-01-04 | None | None |
| [Ancient Vault](https://oldschool.runescape.wiki/w/Ancient_Vault) | 2023-01-11 | 12644, 13156 | None |
| [Ape Atoll](https://oldschool.runescape.wiki/w/Ape_Atoll) | 2004-12-06 | 10974 | 10794: 2004-12-30, 10795: 2004-12-30, 11050: 2004-12-30 |
| [Arandar](https://oldschool.runescape.wiki/w/Arandar) | 2004-09-20 | None | 9266: 2004-09-30, 9267: 2004-09-30, 9523: 2004-09-30 |
| [Asgarnia](https://oldschool.runescape.wiki/w/Asgarnia) | 2001-04-06 | None | 11825: 2001-04-30, 11829: 2001-04-30, 11830: 2001-04-30, 12085: 2001-04-30, 12086: 2001-04-30 |
| [Avium Savannah](https://oldschool.runescape.wiki/w/Avium_Savannah) | 2024-03-20 | 5935, 5936, 5937, 6189, 6445, 6446, 6447, 6701, 6702, 6703, 6957, 6958, 6959, 7215 | None |
| [Battlefield](https://oldschool.runescape.wiki/w/Battlefield) | 2002-07-23 | None | 10034: 2002-07-31 |
| [Battlefront](https://oldschool.runescape.wiki/w/Battlefront) | 2019-01-10 | 5433, 5434 | None |
| [Blast Mine](https://oldschool.runescape.wiki/w/Blast_Mine) | 2016-01-07 | 5948 | None |
| [Body Altar](https://oldschool.runescape.wiki/w/Body_Altar) | 2004-03-29 | None | None |
| [Chaos Altar](https://oldschool.runescape.wiki/w/Chaos_Altar) | 2004-03-29 | None | None |
| [Cosmic Altar](https://oldschool.runescape.wiki/w/Cosmic_Altar) | 2004-03-29 | None | None |
| [Cosmic Entity's Plane](https://oldschool.runescape.wiki/w/Cosmic_Entity's_Plane) | Unverified | 8267 | None |
| [Crabclaw Isle](https://oldschool.runescape.wiki/w/Crabclaw_Isle) | 2016-11-17 | 6965 | None |
| [Crafting Guild](https://oldschool.runescape.wiki/w/Crafting_Guild) | 2002-02-27 | None | 11571: 2002-02-28 |
| [Crandor](https://oldschool.runescape.wiki/w/Crandor) | 2001-09-23 | None | 11314: 2001-09-30, 11315: 2001-09-30 |
| [Crash Island](https://oldschool.runescape.wiki/w/Crash_Island) | 2004-12-06 | None | 11562: 2004-12-30 |
| [Dark Altar](https://oldschool.runescape.wiki/w/Dark_Altar) | 2016-01-07 | 6716 | None |
| [Death Altar](https://oldschool.runescape.wiki/w/Death_Altar) | 2005-10-17 | None | None |
| [Death Plateau](https://oldschool.runescape.wiki/w/Death_Plateau) | Unverified | None | None |
| [Dense Essence Mine](https://oldschool.runescape.wiki/w/Dense_Essence_Mine) | Unverified | 6972 | None |
| [Desert Plateau](https://oldschool.runescape.wiki/w/Desert_Plateau) | 2020-07-16 | 13361, 13617 | None |
| [Digsite](https://oldschool.runescape.wiki/w/Digsite) | 2003-07-09 | None | 13365: 2003-07-31 |
| [Dragontooth Island](https://oldschool.runescape.wiki/w/Dragontooth_Island) | 2005-02-15 | 15159 | None |
| [Draynor Manor](https://oldschool.runescape.wiki/w/Draynor_Manor) | 2001-01-04 | None | None |
| [Drill Sergeant's Training Camp](https://oldschool.runescape.wiki/w/Drill_Sergeant's_Training_Camp) | Unverified | 12619 | None |
| [Eagles' Peak](https://oldschool.runescape.wiki/w/Eagles'_Peak) | Unverified | 9270 | None |
| [Earth Altar](https://oldschool.runescape.wiki/w/Earth_Altar) | 2004-03-29 | None | None |
| [Enchanted Valley](https://oldschool.runescape.wiki/w/Enchanted_Valley) | 2006-07-11 | 12102 | None |
| [Evil Twin Crane Room](https://oldschool.runescape.wiki/w/Evil_Twin_Crane_Room) | Unverified | 7504 | None |
| [Exam Centre](https://oldschool.runescape.wiki/w/Exam_Centre) | 2003-07-09 | None | 13364: 2003-07-31 |
| [Falador Farm](https://oldschool.runescape.wiki/w/Falador_Farm) | 2001-04-06 | None | 12083: 2001-04-30 |
| [Farming Guild](https://oldschool.runescape.wiki/w/Farming_Guild) | 2019-01-10 | 4922 | None |
| [Feldip Hills](https://oldschool.runescape.wiki/w/Feldip_Hills) | 2003-05-07 | 9773, 10285 | 9774: 2003-05-31, 10029: 2006-11-21, 10030: 2003-05-31, 10286: 2003-05-31, 10287: 2003-05-31, 10542: 2004-05-31, 10543: 2003-05-31 |
| [Fenkenstrain's Castle](https://oldschool.runescape.wiki/w/Fenkenstrain's_Castle) | 2005-01-31 | None | None |
| [Fight Arena](https://oldschool.runescape.wiki/w/Fight_Arena) | Unverified | None | None |
| [Fire Altar](https://oldschool.runescape.wiki/w/Fire_Altar) | 2004-03-29 | None | None |
| [Fisher Realm](https://oldschool.runescape.wiki/w/Fisher_Realm) | 2002-07-23 | 10569 | None |
| [Fishing Guild](https://oldschool.runescape.wiki/w/Fishing_Guild) | 2002-06-17 | None | 10293: 2002-06-30 |
| [Fishing Platform](https://oldschool.runescape.wiki/w/Fishing_Platform) | 2002-09-09 | None | 11059: 2002-09-30 |
| [The Forsaken Tower](https://oldschool.runescape.wiki/w/The_Forsaken_Tower) | Unverified | 5435 | None |
| [Fossil Island](https://oldschool.runescape.wiki/w/Fossil_Island) | 2017-09-07 | 14650, 14651, 14652, 14906, 14907, 14908, 15162, 15163, 15164 | None |
| [Freaky Forester's Clearing](https://oldschool.runescape.wiki/w/Freaky_Forester's_Clearing) | Unverified | 10314 | None |
| [Fremennik Province](https://oldschool.runescape.wiki/w/Fremennik_Province) | 2004-11-02 | None | 10296: 2004-11-30, 10552: 2004-11-30, 10808: 2004-11-30, 10809: 2004-11-30, 10810: 2005-05-31, 10811: 2006-11-21, 11064: 2004-11-30 |
| [Fremennik Isles](https://oldschool.runescape.wiki/w/Fremennik_Isles) | 2007-02-06 | None | None |
| [Frogland](https://oldschool.runescape.wiki/w/Frogland) | Unverified | 9802 | None |
| [Galvek Shipwrecks](https://oldschool.runescape.wiki/w/Galvek_Shipwrecks) | Unverified | 6486, 6487, 6488, 6489, 6742, 6743, 6744, 6745 | None |
| [Ghorrock Dungeon](https://oldschool.runescape.wiki/w/Ghorrock_Dungeon) | 2023-01-11 | 11681 | None |
| [Gorak's Plane](https://oldschool.runescape.wiki/w/Gorak's_Plane) | Unverified | 12115 | None |
| [Grand Exchange](https://oldschool.runescape.wiki/w/Grand_Exchange) | 2015-02-26 | 12598 | None |
| [God Wars Dungeon](https://oldschool.runescape.wiki/w/God_Wars_Dungeon) | 2013-10-17 | 11578 | None |
| [Harmony Island](https://oldschool.runescape.wiki/w/Harmony_Island) | 2007-03-06 | None | None |
| [Hazelmere's Island](https://oldschool.runescape.wiki/w/Hazelmere's_Island) | Unverified | None | None |
| [Hunter Guild](https://oldschool.runescape.wiki/w/Hunter_Guild) | 2024-03-20 | 6191 | None |
| [Ice Path](https://oldschool.runescape.wiki/w/Ice_Path) | 2005-04-18 | 11323 | 11322: 2005-01-31 |
| [Iceberg](https://oldschool.runescape.wiki/w/Iceberg) | 2007-01-29 | None | None |
| [Icyene Graveyard](https://oldschool.runescape.wiki/w/Icyene_Graveyard) | 2020-06-04 | 14641, 14897, 14898 | None |
| [Isafdar](https://oldschool.runescape.wiki/w/Isafdar) | 2004-09-20 | 8497 | 8753: 2004-09-30, 8754: 2004-09-30, 9009: 2004-09-30, 9010: 2004-09-30 |
| [Island of Stone](https://oldschool.runescape.wiki/w/Island_of_Stone) | 2019-09-26 | 9790 | None |
| [Isle of Souls](https://oldschool.runescape.wiki/w/Isle_of_Souls) | 2020-12-09 | 8236, 8237, 8238, 8491, 8492, 8494, 8747, 8750, 9003, 9004, 9006, 9260, 9261, 9262 | None |
| [Jiggig](https://oldschool.runescape.wiki/w/Jiggig) | 2005-05-17 | None | None |
| [Kandarin](https://oldschool.runescape.wiki/w/Kandarin) | 2002-02-27 | 9268, 9269, 9014, 9263, 9519 | 9264: 2004-09-30, 9524: 2004-09-30, 9527: 2003-12-31, 9776: 2004-12-30, 9783: 2003-12-31, 10037: 2002-09-30, 10290: 2002-05-31, 10294: 2002-07-31, 10546: 2002-05-31, 10551: 2003-12-31, 10805: 2002-03-31, 11062: 2002-02-28 |
| [Karamja](https://oldschool.runescape.wiki/w/Karamja) | 2001-06-11 | None | 10801: 2002-02-28, 10802: 2002-02-28, 11054: 2003-01-31, 11311: 2003-01-31, 11312: 2003-01-31, 11313: 2001-06-30, 11566: 2003-01-31, 11567: 2003-01-31, 11568: 2003-01-31, 11569: 2001-06-30, 11822: 2002-12-31 |
| [Kebos Lowlands](https://oldschool.runescape.wiki/w/Kebos_Lowlands) | 2017-01-05 | 4665, 4666, 4667, 4921, 5178 | None |
| [Kebos Swamp](https://oldschool.runescape.wiki/w/Kebos_Swamp) | 2019-01-10 | 4664, 4920, 5174, 5175, 5176, 5430, 5431 | None |
| [Kharazi Jungle](https://oldschool.runescape.wiki/w/Kharazi_Jungle) | 2003-08-20 | None | 11053: 2003-08-31, 11309: 2003-08-31, 11565: 2003-08-31, 11821: 2003-08-31 |
| [Kharidian Desert](https://oldschool.runescape.wiki/w/Kharidian_Desert) | 2003-04-14 | 12587, 12844, 12846, 13100, 13101, 13102, 13357, 13359, 13360, 13614, 13615, 13616, 13869, 13870 | 12845: 2005-04-18, 12847: 2003-04-30, 12848: 2003-04-30, 13103: 2003-04-30, 13104: 2003-04-30 |
| [Killerwatt Plane](https://oldschool.runescape.wiki/w/Killerwatt_Plane) | 2006-02-20 | 10577 | None |
| [Great Kourend](https://oldschool.runescape.wiki/w/Great_Kourend) | 2016-01-07 | 6201, 6457, 6713 | None |
| [Kourend Woodland](https://oldschool.runescape.wiki/w/Kourend_Woodland) | 2016-11-17 | 5942, 6197, 6453 | None |
| [Law Altar](https://oldschool.runescape.wiki/w/Law_Altar) | 2004-08-24 | 9803 | None |
| [Legends' Guild](https://oldschool.runescape.wiki/w/Legends'_Guild) | 2003-08-20 | None | 10804: 2002-04-30 |
| [Lighthouse](https://oldschool.runescape.wiki/w/Lighthouse) | 2004-11-17 | None | 10040: 2004-11-30 |
| [Lithkren](https://oldschool.runescape.wiki/w/Lithkren) | 2018-01-04 | 14142, 14398 | None |
| [Lumbridge Swamp](https://oldschool.runescape.wiki/w/Lumbridge_Swamp) | 2001-01-04 | None | None |
| [Max Island](https://oldschool.runescape.wiki/w/Max_Island) | Unverified | None | None |
| [McGrubor's Wood](https://oldschool.runescape.wiki/w/McGrubor's_Wood) | 2002-05-28 | None | 10550: 2002-05-31 |
| [Mime's Stage](https://oldschool.runescape.wiki/w/Mime's_Stage) | Unverified | 8010 | None |
| [Mind Altar](https://oldschool.runescape.wiki/w/Mind_Altar) | 2004-03-29 | None | None |
| [Misthalin](https://oldschool.runescape.wiki/w/Misthalin) | 2001-01-04 | None | None |
| [Molch](https://oldschool.runescape.wiki/w/Molch) | 2019-01-10 | 5177 | None |
| [Molch Island](https://oldschool.runescape.wiki/w/Molch_Island) | 2019-01-10 | 5432 | None |
| [Morytania](https://oldschool.runescape.wiki/w/Morytania) | 2004-06-29 | 14133, 14389, 14391, 14645, 14647 | 13619: 2004-10-31, 13620: 2004-07-30, 13621: 2004-07-30, 13622: 2004-06-30, 13876: 2004-07-30, 13877: 2004-07-30, 13879: 2005-01-31, 14134: 2004-10-31, 14390: 2004-10-31 |
| [Mount Quidamortem](https://oldschool.runescape.wiki/w/Mount_Quidamortem) | 2017-01-05 | 4662, 4663, 4918, 4919 | None |
| [Mr. Mordaut's Classroom](https://oldschool.runescape.wiki/w/Mr._Mordaut's_Classroom) | Unverified | 7502 | None |
| [Mudskipper Point](https://oldschool.runescape.wiki/w/Mudskipper_Point) | 2005-10-24 | 11824 | None |
| [Mysterious Old Man's Maze](https://oldschool.runescape.wiki/w/Mysterious_Old_Man's_Maze) | Unverified | 11591 | None |
| [Myths' Guild](https://oldschool.runescape.wiki/w/Myths'_Guild) | 2017-12-07 | 9772 | None |
| [Nature Altar](https://oldschool.runescape.wiki/w/Nature_Altar) | 2004-03-29 | None | None |
| [Necropolis](https://oldschool.runescape.wiki/w/Necropolis) | 2022-04-27 | 13098, 13353, 13354, 13609, 13610 | None |
| [Northern Tundras](https://oldschool.runescape.wiki/w/Northern_Tundras) | 2016-09-08 | 6204, 6205, 6717 | None |
| [Observatory](https://oldschool.runescape.wiki/w/Observatory) | 2003-03-17 | None | 9777: 2003-03-31 |
| [Odd One Out](https://oldschool.runescape.wiki/w/Odd_One_Out) | Unverified | 7754 | None |
| [Ortus Farm](https://oldschool.runescape.wiki/w/Ortus_Farm) | 2024-03-20 | 6192, 6193 | None |
| [Otto's Grotto](https://oldschool.runescape.wiki/w/Otto's_Grotto) | 2007-07-03 | None | 10038: 2002-09-30 |
| [Ourania Hunter Area](https://oldschool.runescape.wiki/w/Ourania_Hunter_Area) | Unverified | None | None |
| [Pirates' Cove](https://oldschool.runescape.wiki/w/Pirates'_Cove) | 2006-07-24 | None | None |
| [Piscatoris Hunter Area](https://oldschool.runescape.wiki/w/Piscatoris_Hunter_Area) | 2006-11-21 | 9015, 9016, 9271, 9528 | None |
| [Player Owned House](https://oldschool.runescape.wiki/w/Player_Owned_House) | Unverified | 7534, 7535, 7790, 7791, 8046, 8047, 8302, 8303 | None |
| [Poison Waste](https://oldschool.runescape.wiki/w/Poison_Waste) | 2004-09-20 | None | 8752: 2004-09-30, 9008: 2004-09-30 |
| [Port Tyras](https://oldschool.runescape.wiki/w/Port_Tyras) | 2006-08-22 | 8496 | None |
| [Puro-Puro](https://oldschool.runescape.wiki/w/Puro-Puro) | 2007-06-11 | None | None |
| [Quarry](https://oldschool.runescape.wiki/w/Quarry) | 2006-01-23 | 12589 | None |
| [Ralos' Rise](https://oldschool.runescape.wiki/w/Ralos'_Rise) | 2024-03-20 | 5424, 5425, 5679, 5680, 5681, 5682 | None |
| [Ranging Guild](https://oldschool.runescape.wiki/w/Ranging_Guild) | 2004-01-21 | None | 10549: 2002-05-31 |
| [Ratcatchers Mansion](https://oldschool.runescape.wiki/w/Ratcatchers_Mansion) | 2005-11-28 | None | None |
| [Ruins of Unkah](https://oldschool.runescape.wiki/w/Ruins_of_Unkah) | 2021-03-24 | 12588 | None |
| [Ruins of Ullek](https://oldschool.runescape.wiki/w/Ruins_of_Ullek) | 2022-04-27 | 13355, 13611, 13612 | None |
| [Rune Essence Mine](https://oldschool.runescape.wiki/w/Rune_Essence_Mine) | 2004-03-29 | None | None |
| [ScapeRune](https://oldschool.runescape.wiki/w/ScapeRune) | 2005-08-09 | 10058, 7758, 8261 | None |
| [Sea Spirit Dock](https://oldschool.runescape.wiki/w/Sea_Spirit_Dock) | 2021-03-24 | 12332 | None |
| [Ship Yard](https://oldschool.runescape.wiki/w/Ship_Yard) | 2002-12-12 | None | 11823: 2002-12-31 |
| [Silvarea](https://oldschool.runescape.wiki/w/Silvarea) | 2004-06-29 | None | 13366: 2004-06-30 |
| [Sinclair Mansion](https://oldschool.runescape.wiki/w/Sinclair_Mansion) | 2003-06-09 | None | 10807: 2003-06-30 |
| [Slayer Tower](https://oldschool.runescape.wiki/w/Slayer_Tower) | 2005-01-26 | 13723 | 13623: 2005-01-31 |
| [Soul Altar](https://oldschool.runescape.wiki/w/Soul_Altar) | 2016-01-07 | 7228 | None |
| [Stranglewood Temple](https://oldschool.runescape.wiki/w/Stranglewood_Temple) | Unverified | 4761 | None |
| [Sunset Coast](https://oldschool.runescape.wiki/w/Sunset_Coast) | 2024-03-20 | 5934, 6190 | None |
| [The Scar](https://oldschool.runescape.wiki/w/The_Scar) | 2023-07-26 | 8036, 8292 | None |
| [The Stranglewood](https://oldschool.runescape.wiki/w/The_Stranglewood) | 2023-07-26 | 4403, 4404, 4659, 4660, 4661, 4916, 4917 | None |
| [Troll Arena](https://oldschool.runescape.wiki/w/Troll_Arena) | 2004-08-24 | None | 11576: 2004-08-30 |
| [Trollheim](https://oldschool.runescape.wiki/w/Trollheim) | 2004-08-24 | None | 11577: 2004-08-30 |
| [Trollweiss Mountain](https://oldschool.runescape.wiki/w/Trollweiss_Mountain) | 2005-01-05 | None | 11066: 2005-01-31, 11067: 2005-01-31, 11068: 2005-01-31 |
| [Tutorial Island](https://oldschool.runescape.wiki/w/Tutorial_Island) | 2002-09-24 | 12335, 12436 | 12079: 2002-09-30, 12080: 2002-09-30, 12336: 2002-09-30, 12592: 2002-09-30 |
| [Underwater](https://oldschool.runescape.wiki/w/Underwater) | 2017-09-07 | 15008, 15264 | None |
| [Water Altar](https://oldschool.runescape.wiki/w/Water_Altar) | 2004-03-29 | None | None |
| [Waterbirth Island](https://oldschool.runescape.wiki/w/Waterbirth_Island) | 2005-08-01 | None | None |
| [Wintertodt Camp](https://oldschool.runescape.wiki/w/Wintertodt_Camp) | 2016-09-08 | 6461 | None |
| [Wizards' Tower](https://oldschool.runescape.wiki/w/Wizards'_Tower) | 2001-01-04 | None | None |
| [Woodcutting Guild](https://oldschool.runescape.wiki/w/Woodcutting_Guild) | 2016-06-02 | 6198, 6454 | None |
| [Wrath Altar](https://oldschool.runescape.wiki/w/Wrath_Altar) | 2018-01-04 | 9291 | None |
