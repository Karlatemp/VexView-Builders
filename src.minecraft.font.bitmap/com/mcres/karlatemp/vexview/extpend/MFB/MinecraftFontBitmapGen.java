/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MinecraftFontBitmapGen.java@author: karlatemp@vip.qq.com: 2020/2/4 下午8:26@version: 2.0
 */

package com.mcres.karlatemp.vexview.extpend.MFB;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

public class MinecraftFontBitmapGen {
    public static String
            CLIENT_LOCATION,
            OUTPUT;

    static {
        CLIENT_LOCATION = System.getProperty("client", "minecraft.jar");
        OUTPUT = System.getProperty("out", "out.sizes.bin");
    }

    public static void main(String[] run) throws IOException {
        try {
            Class.forName("cn.mcres.karlatemp.mxlib.tester.Logging").getMethod(
                    "install").invoke(null);
        } catch (Throwable ignore) {
        }
        JarFile jar = new JarFile(CLIENT_LOCATION);
        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT, "rw")) {
            ResourceLoader loader = new ClientResourceLoader(jar, null, "assets/"),
                    textures = new ClientResourceLoader(jar, "textures", "assets/");
            Gson gson = new Gson();
            List<FontProvider> fp = new ArrayList<>();
            List<JsonObject> font_infos = new ArrayList<>();

            try (InputStreamReader reader = new InputStreamReader(
                    loader.getResource(NamespaceKey.parse("font/alt.json")).getStream(),
                    StandardCharsets.UTF_8)) {
                font_infos.add(gson.fromJson(reader, JsonObject.class));
            }
            try (InputStreamReader reader = new InputStreamReader(
                    loader.getResource(NamespaceKey.parse("font/default.json")).getStream(),
                    StandardCharsets.UTF_8)) {
                font_infos.add(gson.fromJson(reader, JsonObject.class));
            }
            for (JsonObject object : font_infos) {
                final JsonArray providers = object.getAsJsonArray("providers");
                for (JsonElement element : providers) {
                    JsonObject obj = element.getAsJsonObject();
                    switch (obj.get("type").getAsString()) {
                        case "bitmap": {
                            fp.add(BitmapFontProvider.fromJson(obj, textures));
                            break;
                        }
                        case "ttf": {
                            fp.add(TrueTypeFontProvider.fromJson(obj, textures));
                            break;
                        }
                        case "legacy_unicode": {
                            fp.add(LegacyUnicodeFontProvider.fromJson(obj, loader));
                            break;
                        }
                    }
                }
            }
            FontProvider.Glyph[] glyphs = new FontProvider.Glyph[0xFFFF + 1];
            for (int i = 0; i < 0x10000; i++) {
                for (FontProvider p : fp) {
                    if ((glyphs[i] = p.getGlyph((char) i)) != null) {
                        break;
                    }
                }
            }
            // byte[] zipd = new byte[0xFFFF + 1];
            for (int i = 0; i < glyphs.length; i++) {
                FontProvider.Glyph g = glyphs[i];
                if (g != null) {
                    raf.writeFloat(g.getAdvance());
                } else {
                    raf.writeFloat(0);
                }
            }
//            int counter = 0;
//            for (int w : zipd) {
//                if (((counter++) & 0xF) == 0) {
//                    System.out.format("\n0x%04x:\t", counter - 1);
//                }
//                System.out.format("%04x ", w & 0xFF);
//            }
//            System.out.println();
            raf.setLength(raf.getFilePointer());
//            raf.write(zipd);
        }
    }
}
