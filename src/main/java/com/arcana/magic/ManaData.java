package com.arcana.magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ManaData {
    public static final int MAX_LEVEL = 50;

    public static final Codec<ManaData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("mana").forGetter(d -> d.mana),
            Codec.INT.fieldOf("level").forGetter(d -> d.level),
            Codec.INT.fieldOf("xp").forGetter(d -> d.xp),
            Codec.INT.fieldOf("spell").forGetter(d -> d.spell)
    ).apply(i, ManaData::new));

    public int mana;
    public int level;
    public int xp;
    public int spell;

    public ManaData() {
        this(100, 1, 0, 0);
    }

    public ManaData(int mana, int level, int xp, int spell) {
        this.mana = mana;
        this.level = level;
        this.xp = xp;
        this.spell = spell;
    }

    public int maxMana() {
        return 100 + (level - 1) * 20;
    }

    public int xpToNext() {
        return 20 + level * 15;
    }

    public int regenPerSecond() {
        return 2 + level / 5;
    }

    public Spell currentSpell() {
        return Spell.byIndex(spell);
    }

    public void cycleSpell() {
        spell = Math.floorMod(spell + 1, Spell.values().length);
    }

    /** @return true if the player levelled up */
    public boolean addXp(int amount) {
        if (level >= MAX_LEVEL) return false;
        xp += amount;
        boolean up = false;
        while (level < MAX_LEVEL && xp >= xpToNext()) {
            xp -= xpToNext();
            level++;
            up = true;
        }
        if (up) mana = maxMana();
        return up;
    }
}
