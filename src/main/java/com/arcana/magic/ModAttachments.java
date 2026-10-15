package com.arcana.magic;

import com.arcana.Arcana;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class ModAttachments {
    public static final AttachmentType<ManaData> MANA = AttachmentRegistry.create(
            Arcana.id("mana"),
            builder -> builder
                    .persistent(ManaData.CODEC)
                    .copyOnDeath()
                    .initializer(ManaData::new)
    );

    public static void init() {
    }
}
