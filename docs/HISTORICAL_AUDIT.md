# Historical completeness audit

This audit covers the plugin's selectable timeline from 4 January 2001 through the 10 August 2007 Old School backup.

## Sources

- The [Old School RuneScape game-update archive](https://oldschool.runescape.wiki/w/Game_updates) provides the dated update index and links to preserved, verbatim Jagex news posts.
- The [quest release-date table](https://oldschool.runescape.wiki/w/Quests/Release_dates) and [miniquest table](https://oldschool.runescape.wiki/w/Miniquests) provide the complete pre-backup quest set. `quest-sources.tsv` records the source used for each RuneLite quest constant.
- Individual location pages supply release dates and map coordinates for historically added regions. Their release announcement URLs are stored with the release rows.
- RuneLite's current `InterfaceID`, `ObjectID`, `ObjectID1`, `Skill`, `Prayer`, and `Quest` definitions supply runtime identifiers. Historical dates never come from identifier ordering.

## Validated coverage

| Area | Validated result |
| --- | --- |
| Selectors | 330 unique dated states, ending 10 August 2007; legacy enum names retained for saved settings |
| Quests and miniquests | 146 RuneLite entries, including 10 Recipe for Disaster subquests and the pre-backup miniquests |
| Skills | 23 historical skills; exact introduction dates; Sailing excluded at every date |
| Prayers | 26 historical prayers; 2005, 2006, and King's Ransom additions gated on their release days |
| Spells | 127 historical standard, Ancient, and Lunar spells; post-backup spell widgets fail closed |
| Items | 24,735 definitions checked; exact dates are inclusive; missing/malformed dates fail closed; three verified legacy ID gaps are restored by a curated override ledger |
| NPCs/monsters | 3,000 definitions checked; exact dates are inclusive; missing, malformed, unknown, and post-backup IDs remain locked |
| Regions | Superseded by `REGION_AUDIT_2026_09_28.md`: 390 configured regions, with 69 explicitly reviewed groups. Original anchor-only validation was incomplete; legacy dates and later expansions still require review. |
| Sailing | Skill, UI groups, 1,250 current Sailing object IDs, and Sailing-labelled actions remain permanently unavailable |

## Enforcement and compatibility fixes

- Release comparisons use `LocalDate`, fixing off-by-one and release-day exclusion errors.
- Selected spells and items are rechecked when used on NPCs, objects, ground items, players, or widgets.
- Item gates cover action types as well as a fixed menu-label list. Disposal and banking remain possible.
- Null-date item records were audited separately. The source dataset mixes real items with interface-only pseudo-items, so there is no broad fallback. Only externally verified player-relevant gaps are overridden: half plain pizza IDs 2291/2292 (11 June 2001) and old red disk ID 9947 (conservatively 13 November 2006, the end of its documented 6–13 November hidden-update window).
- Scene, minimap, and world-map masks use the same fail-closed region state. Instances are checked through their template coordinates.
- Scene hiding uses RuneLite's current `RenderCallbackManager`; widget identifiers use current `gameval` constants.
- The regression suite checks all dated entity records and each release boundary. `./gradlew clean test` is the required local validation.

The client cannot recreate removed server behaviour, hard-disable every quest start that reuses an older NPC/object, or split a 64-by-64 map region when old and modern content occupy the same region. Exact object-level cache equivalence would require a historical object-definition allowlist. These are representation limits rather than permissive fallbacks: content without verified date coverage is blocked.
