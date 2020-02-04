/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: LegacyUnicodeFontProvider.java@author: karlatemp@vip.qq.com: 2020/2/4 下午9:41@version: 2.0
 */

package com.mcres.karlatemp.vexview.extpend.MFB;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.ByteBuffer;

public class LegacyUnicodeFontProvider implements FontProvider {
    private final ByteBuffer buf;

    public LegacyUnicodeFontProvider(ByteBuffer buf) {
        this.buf = buf;
    }

    public static LegacyUnicodeFontProvider fromJson(JsonObject obj, ResourceLoader loader) throws IOException {
        return new LegacyUnicodeFontProvider(loader.getResource(
                NamespaceKey.parse(obj.get("sizes").getAsString())
        ).read());
    }

    @Override
    public Glyph getGlyph(char c) {
        if (buf.capacity() > c) {
            byte info = buf.duplicate().position(c).get();
            if (info == 0) {
                return null;
            }
            int wid = ((info & 0xF) + 1) - ((info >> 4) & 0xF);
            return new Glyph() {
                @Override
                public int width() {
                    return wid;
                }

                @Override
                public int height() {
                    return 16;
                }

                @Override
                public float getAdvance() {
                    return (float) wid / 2 + 1;
                }
            };
        }
        return null;
    }
}
