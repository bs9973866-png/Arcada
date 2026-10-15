# Arcana (Fabric, Minecraft 1.21.11)

Stage 1 of an all-in-one mod: magic + mana + RPG levels.

## Build
Requires JDK 21.
- IntelliJ IDEA: open the folder as a Gradle project, then run `build`.
- Or install Gradle 9.x and run: `gradle wrapper` then `./gradlew build`.
The jar appears in `build/libs/`. Test in game with `./gradlew runClient`.

## What works in stage 1
- Mana (regenerates, saved per player), levels, XP from kills
- Arcane Staff: right-click casts, Shift+right-click switches spell
- 5 spells: Arcane Bolt, Heal, Blink, Fire Nova, Storm
- Mana Crystal (+50 mana), Mana Altar (full restore)
- HUD mana bar, /arcana info, /arcana spells, EN + RU lang

## Roadmap
2. Spell tomes + skill tree GUI
3. Dimension with biomes, structures and a boss
4. Tech: energy network, machines, pipes
5. RPG: classes, quests, loot affixes
6. Colony NPCs
