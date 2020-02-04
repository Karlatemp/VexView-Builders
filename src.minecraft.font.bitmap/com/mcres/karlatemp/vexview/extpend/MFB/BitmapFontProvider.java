/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: BitmapFontProvider.java@author: karlatemp@vip.qq.com: 2020/2/4 下午9:18@version: 2.0
 */

package com.mcres.karlatemp.vexview.extpend.MFB;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.*;

public class BitmapFontProvider implements FontProvider {

    private final Map<Integer, Glyph> gps;
    private int height;
    private BitSet chars;

    public BitmapFontProvider(BitSet chars, Map<Integer, Glyph> gps, int height) {
        this.chars = chars;
        this.gps = gps;
        this.height = height;
    }

    public static BitmapFontProvider fromJson(JsonObject obj, ResourceLoader loader) throws IOException {
        int hei = 8;
        if (obj.has("height"))
            hei = obj.get("height").getAsInt();
        int ascent = obj.get("ascent").getAsInt();
        if (ascent > hei)
            throw new JsonParseException("Ascent " + ascent + " higher than height " + hei);
        List<String> str = new ArrayList<>();
        JsonArray arr = obj.getAsJsonArray("chars");
        String one = null;
        for (int ind = 0; ind < arr.size(); ind++) {
            String line = arr.get(ind).getAsString();
            if (ind != 0) {
                int curr = line.length();
                int fir = one.length();
                if (curr != fir) {
                    throw new JsonParseException("Elements of chars have to be the same length (found: " + curr + ", expected: " + fir + "), pad with space or \\u0000");
                }
            }
            str.add(one = line);
        }
        if (str.isEmpty() || str.get(0).isEmpty()) {
            throw new JsonParseException("Expected to find data in chars, found none.");
        }
        final NativeImage image = NativeImage.read(loader.getResource(NamespaceKey.parse(obj.get("file").getAsString())).read());

        int g_width = image.getWidth() / str.get(0).length();
        int g_height = image.getHeight() / str.size();

        float scale = (float) hei / g_height;
        Map<Integer, Glyph> gps = new HashMap<>();
        for (int line = 0; line < str.size(); line++) {
            String line0 = str.get(line);
            for (int var12 = 0; var12 < line0.length(); var12++) {
                char g = line0.charAt(var12);

                if (g != '\000' && g != ' ') {
                    int var14 = getActualGlyphWidth(image, g_width, g_height, var12, line);
                    int advance = (int) (0.5D + (double) ((float) var14 * scale)) + 1;
                    gps.put((int) g, new Glyph() {
                        @Override
                        public int width() {
                            return g_width;
                        }

                        @Override
                        public float getAdvance() {
                            return advance;
                        }
                    });

                }
            }
        }

        return new BitmapFontProvider(TrueTypeFontProvider.toSet(
                String.join("", str).toCharArray()
        ), gps, hei);
    }

    private static int getActualGlyphWidth(NativeImage image, int g_width, int g_height, int c_index, int l_index) {
        int xOffset = g_width * c_index;
        int yOffset = g_height * l_index;
        int ws = g_width - 1;
        for (; ws >= 0; ws--) {
            for (int l = 0; l < g_height; l++) {
                if (image.getLuminanceOrAlpha(xOffset + ws, yOffset + l) != 0) {
                    return ws + 1;
                }
            }
        }
        return ws + 1;
    }

    @Override
    public Glyph getGlyph(char c) {
        if (!chars.get(c))
            return null;
        return gps.get((int) c);

    }
}
