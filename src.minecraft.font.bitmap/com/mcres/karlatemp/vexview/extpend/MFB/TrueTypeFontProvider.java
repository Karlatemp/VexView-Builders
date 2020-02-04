/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: TrueTypeFontProvider.java@author: karlatemp@vip.qq.com: 2020/2/4 下午9:02@version: 2.0
 */

package com.mcres.karlatemp.vexview.extpend.MFB;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.BitSet;

public class TrueTypeFontProvider implements FontProvider {
    private final STBTTFontinfo info;
    private final float size;
    private final float oversample;
    private final float shiftX;
    private final float shiftY;
    private final BitSet skip;
    private final float pointScale;
    private final float ascent;

    public TrueTypeFontProvider(
            Resource resource,
            float size,
            float oversample,
            float shiftX,
            float shiftY,
            BitSet skip) throws IOException {
        this.size = size;
        this.oversample = oversample;
        this.skip = skip;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        STBTTFontinfo info = STBTTFontinfo.create();

        if (!STBTruetype.stbtt_InitFont(info, resource.read())) {
            throw new IOException("Invalid ttf");
        }
        this.info = info;

        this.pointScale = STBTruetype.stbtt_ScaleForPixelHeight(info,
                size * oversample);

        try (MemoryStack var7 = MemoryStack.stackPush()) {
            IntBuffer var8 = var7.mallocInt(1);
            IntBuffer var9 = var7.mallocInt(1);
            IntBuffer var10 = var7.mallocInt(1);
            STBTruetype.stbtt_GetFontVMetrics(info, var8, var9, var10);

            this.ascent = var8.get(0) * this.pointScale;
        }
    }

    @Override
    public Glyph getGlyph(char c) {
        if (skip.get(c))
            return null;
        try (MemoryStack var2 = MemoryStack.stackPush()) {
            IntBuffer var3 = var2.mallocInt(1);
            IntBuffer var4 = var2.mallocInt(1);
            IntBuffer var5 = var2.mallocInt(1);
            IntBuffer var6 = var2.mallocInt(1);

            int var7 = STBTruetype.stbtt_FindGlyphIndex(this.info, c);
            if (var7 == 0) {
                return null;
            }

            STBTruetype.stbtt_GetGlyphBitmapBoxSubpixel(this.info, var7, this.pointScale, this.pointScale, this.shiftX, this.shiftY, var3, var4, var5, var6);

            int var8 = var5.get(0) - var3.get(0);
            int var9 = var6.get(0) - var4.get(0);

            if (var8 == 0 || var9 == 0) {
                return null;
            }

            IntBuffer var10 = var2.mallocInt(1);
            IntBuffer var11 = var2.mallocInt(1);
            STBTruetype.stbtt_GetGlyphHMetrics(this.info, var7, var10, var11);


            return new GlyphTTF(var3.get(0), var5.get(0), -var4.get(0), -var6.get(0), var10.get(0) * this.pointScale, var11.get(0) * this.pointScale, var7);
        }
    }

    public static BitSet toSet(char[] str) {
        BitSet bs = new BitSet();
        for (char c : str) {
            bs.set(c, true);
        }
        return bs;
    }

    public static TrueTypeFontProvider fromJson(
            JsonObject var0,
            ResourceLoader loader
    ) throws IOException {
        float var1 = 0.0F;
        float var2 = 0.0F;
        if (var0.has("shift")) {
            JsonArray var3 = var0.getAsJsonArray("shift");
            if (var3.size() != 2) {
                throw new JsonParseException("Expected 2 elements in 'shift', found " + var3.size());
            }

            var1 = var3.get(0).getAsFloat();
            var2 = var3.get(1).getAsFloat();
        }

        StringBuilder var4 = new StringBuilder();

        if (var0.has("skip")) {
            JsonElement var5 = var0.get("skip");
            if (var5.isJsonArray()) {
                JsonArray var6 = var5.getAsJsonArray();
                for (int var7 = 0; var7 < var6.size(); var7++) {
                    var4.append(var6.get(var7).getAsString());
                }
            } else {
                var4.append(var5.getAsString());
            }
        }
        return new TrueTypeFontProvider(
                loader.getResource(NamespaceKey.parse(var0.get("file").getAsString())),
                var0.has("size") ? var0.get("size").getAsFloat() : 11.0F,
                var0.has("oversample") ? var0.get("oversample").getAsFloat() : 1.0F,
                var1,
                var2,
                toSet(var4.toString().toCharArray())
        );
    }

    class GlyphTTF implements Glyph {
        private final int width;
        private final int height;
//        private final float bearingX;
//        private final float bearingY;
        private final float advance;
//        private final int index;

        GlyphTTF(int var1, int var2, int var3, int var4, float var5, float var6, int var7) {
            this.width = var2 - var1;
            this.height = var3 - var4;
//
            this.advance = var5 / TrueTypeFontProvider.this.oversample;
//
//            this.bearingX = (var6 + var1 +
//                    TrueTypeFontProvider.this.shiftX)
//                    / TrueTypeFontProvider.this.oversample;
//            this.bearingY = (TrueTypeFontProvider.this.ascent -
//                    var3 + TrueTypeFontProvider.this.shiftY) / TrueTypeFontProvider.this.
//                    oversample;
//
//            this.index = var7;
        }

        @Override
        public float getAdvance() {
            return advance;
        }

        @Override
        public int width() {
            return width;
        }

        @Override
        public int height() {
            return height;
        }
    }
}
