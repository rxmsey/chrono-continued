# Rewind

Experience Old School RuneScape through its historical timeline.

Rewind is a continuation of the RuneLite **Chrono** plugin, extending its "By Release" account restrictions beyond January 2005 through the RuneScape backup dated **10 August 2007**, the snapshot that became the basis of Old School RuneScape.

## What it does

- Keeps Chrono's original pre-2005 restrictions.
- Adds every dated game-update state from RuneScape's launch through 10 August 2007 while preserving the original monthly selector constants for saved configurations.
- Unlocks quests, skills, prayers, spells, entities and major map regions according to release date.
- Blacks out historically unavailable areas in the 3D scene, minimap and world map.
- Treats unknown/post-2007 geography as locked by default.
- Permanently excludes **Sailing**, which is outside the plugin's 2007 endpoint.
- Gates standard, Ancient and Lunar spellbooks, including the Lunar Diplomacy and Dream Mentor batches.
- Locks unknown or unverified item/NPC IDs instead of allowing them through.
- Blocks modern Sailing interfaces, objects, actions and the Sailing skill itself.

## Historical endpoint

The final normal update represented is **Clan Chat — 6 August 2007**.

The final selectable state is **10 August 2007 — Old School backup**.

No later RuneScape or Old School RuneScape content is intended to unlock.

## Licence and upstream attribution

This project continues the BSD-2-Clause licensed Chrono plugin by IdylRS and preserves the upstream licence and source attribution. Region-locking source files retain their original notices.

Original project: `IdylRS/chrono-plugin`.

The dated quest and miniquest source ledger is in [`docs/quest-sources.tsv`](docs/quest-sources.tsv). Release rows also carry the preserved Jagex announcement URLs used for their dates. The automated audit checks every selector, 146 quests and miniquests, 23 historical skills, 26 historical prayers, 127 historical spells, 24,735 item records, 3,000 NPC/monster records, region fail-closed behaviour, and the Sailing exclusion.

## Important limitation

This is a RuneLite client plugin running against the modern Old School RuneScape server. It can restrict interactions and hide later content, but it cannot recreate deleted 2007 server mechanics, NPC behaviour, or the literal 2007 game cache. Region masking uses RuneScape's 64-by-64 map-region granularity, so a modern and historical location that share one region cannot be separated tile by tile from release metadata alone.

## Compatibility and input handling

Rewind keeps the existing `chrono` configuration group, all setting keys and release selector constants, and RuneLite's `chronoplugin` enabled/disabled key. The Plugin Hub entry ID remains `chrono-continued` so existing installations receive the update. Java classes, packages, bundled resources, and the development launcher use Rewind names. Upstream licence and attribution are preserved.

Minimap input reads an immutable snapshot produced on the client thread. Ordinary game/UI clicks pass through; only left-click sequences targeting a known locked minimap tile are consumed. Hidden/unavailable minimaps, logout/loading, and open context menus clear the snapshot. Minimap blocking remains active independently of the visual mask setting.

Build and run the regression tests with Java 11 and `./gradlew clean build`.
