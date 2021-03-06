/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.entity.metadata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.glowstone.entity.GlowPlayer;
import org.dragonet.entity.metadata.type.ByteMeta;
import org.dragonet.entity.metadata.type.CoordinateMeta;
import org.dragonet.entity.metadata.type.ShortMeta;
import org.dragonet.utilities.io.PEBinaryWriter;

public class EntityMetaData {

    public HashMap<Integer, EntityMetaDataObject> map;

    public EntityMetaData() {
        this.map = new HashMap<>();
    }

    public void set(int key, EntityMetaDataObject object) {
        this.map.put(key, object);
    }

    public byte[] encode() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PEBinaryWriter writer = new PEBinaryWriter(bos);
        try {
            for (Map.Entry<Integer, EntityMetaDataObject> entry : this.map.entrySet()) {
                writer.writeByte(((byte) (((entry.getValue().type() & 0xFF) << 5) | (entry.getKey() & 0x1F))));
                writer.write(entry.getValue().encode());
            }
            writer.writeByte((byte) 0x7F);
            return bos.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    public static EntityMetaData getMetaDataFromPlayer(GlowPlayer player) {
        byte flags = (byte) 0x00;
        flags |= player.getFireTicks() > 0 ? 1 : 0;
        EntityMetaData data = new EntityMetaData();
        data.set(0, new ByteMeta(flags));
        data.set(1, new ShortMeta((short) 0));
        data.set(16, new ByteMeta((byte) 0x00));
        data.set(17, new CoordinateMeta(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
        return data;
    }
}
