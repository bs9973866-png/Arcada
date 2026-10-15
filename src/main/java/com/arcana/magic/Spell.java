package com.arcana.magic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public enum Spell {
    ARCANE_BOLT("arcane_bolt", 10, 1),
    HEAL("heal", 15, 1),
    BLINK("blink", 20, 2),
    FIRE_NOVA("fire_nova", 30, 3),
    STORM("storm", 45, 5);

    public final String key;
    public final int cost;
    public final int minLevel;

    Spell(String key, int cost, int minLevel) {
        this.key = key;
        this.cost = cost;
        this.minLevel = minLevel;
    }

    public static Spell byIndex(int i) {
        Spell[] v = values();
        return v[Math.floorMod(i, v.length)];
    }

    public Text displayName() {
        return Text.translatable("spell.arcana." + key);
    }

    public boolean cast(ServerWorld w, ServerPlayerEntity p, int level) {
        return switch (this) {
            case ARCANE_BOLT -> bolt(w, p, level);
            case HEAL -> heal(w, p, level);
            case BLINK -> blink(w, p);
            case FIRE_NOVA -> nova(w, p, level);
            case STORM -> storm(w, p, level);
        };
    }

    private static void sound(ServerWorld w, ServerPlayerEntity p, SoundEvent s) {
        w.playSound(null, p.getX(), p.getY(), p.getZ(), s, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

    private static boolean bolt(ServerWorld w, ServerPlayerEntity p, int level) {
        Vec3d start = p.getEyePos();
        Vec3d dir = p.getRotationVec(1.0f);
        for (double d = 1; d <= 24; d += 0.5) {
            Vec3d pt = start.add(dir.multiply(d));
            BlockPos bp = BlockPos.ofFloored(pt);
            w.spawnParticles(ParticleTypes.END_ROD, pt.x, pt.y, pt.z, 1, 0, 0, 0, 0);
            if (w.getBlockState(bp).isSolidBlock(w, bp)) break;
            List<LivingEntity> hit = w.getEntitiesByClass(LivingEntity.class,
                    new Box(pt, pt).expand(0.7), e -> e != p && e.isAlive());
            if (!hit.isEmpty()) {
                hit.get(0).damage(w, w.getDamageSources().magic(), 6 + level);
                break;
            }
        }
        sound(w, p, SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL);
        return true;
    }

    private static boolean heal(ServerWorld w, ServerPlayerEntity p, int level) {
        if (p.getHealth() >= p.getMaxHealth()) return false;
        p.heal(6 + level);
        w.spawnParticles(ParticleTypes.HEART, p.getX(), p.getY() + 1.0, p.getZ(), 8, 0.4, 0.5, 0.4, 0.0);
        sound(w, p, SoundEvents.ENTITY_PLAYER_LEVELUP);
        return true;
    }

    private static boolean blink(ServerWorld w, ServerPlayerEntity p) {
        Vec3d start = p.getEyePos();
        Vec3d dir = p.getRotationVec(1.0f);
        Vec3d target = null;
        for (double d = 1; d <= 10; d += 0.5) {
            Vec3d pt = start.add(dir.multiply(d));
            BlockPos feet = BlockPos.ofFloored(pt.x, pt.y - 1.62, pt.z);
            BlockPos head = feet.up();
            if (w.getBlockState(feet).isSolidBlock(w, feet) || w.getBlockState(head).isSolidBlock(w, head)) break;
            target = new Vec3d(pt.x, pt.y - 1.62, pt.z);
        }
        if (target == null) return false;
        w.spawnParticles(ParticleTypes.PORTAL, p.getX(), p.getY() + 1.0, p.getZ(), 30, 0.3, 0.6, 0.3, 0.2);
        p.requestTeleport(target.x, target.y, target.z);
        w.spawnParticles(ParticleTypes.PORTAL, target.x, target.y + 1.0, target.z, 30, 0.3, 0.6, 0.3, 0.2);
        sound(w, p, SoundEvents.ENTITY_ENDERMAN_TELEPORT);
        return true;
    }

    private static boolean nova(ServerWorld w, ServerPlayerEntity p, int level) {
        List<LivingEntity> list = w.getEntitiesByClass(LivingEntity.class,
                p.getBoundingBox().expand(5.0), e -> e != p && e.isAlive());
        for (LivingEntity e : list) {
            e.setOnFireFor(4);
            e.damage(w, w.getDamageSources().magic(), 4 + level / 2.0f);
        }
        w.spawnParticles(ParticleTypes.FLAME, p.getX(), p.getY() + 0.5, p.getZ(), 120, 3.0, 0.3, 3.0, 0.05);
        sound(w, p, SoundEvents.ITEM_FIRECHARGE_USE);
        return true;
    }

    private static boolean storm(ServerWorld w, ServerPlayerEntity p, int level) {
        List<LivingEntity> list = w.getEntitiesByClass(LivingEntity.class,
                p.getBoundingBox().expand(8.0), e -> e != p && e.isAlive());
        for (LivingEntity e : list) {
            e.damage(w, w.getDamageSources().magic(), 8 + level);
            w.spawnParticles(ParticleTypes.ELECTRIC_SPARK, e.getX(), e.getY() + 1.0, e.getZ(), 25, 0.4, 0.8, 0.4, 0.3);
        }
        sound(w, p, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER);
        return true;
    }
}
