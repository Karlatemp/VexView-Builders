/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MinecraftFontSizeCalculation.java@author: karlatemp@vip.qq.com: 2020/2/4 下午10:27@version: 2.0
 */

package lk.vexview.builders;

import org.bukkit.map.MinecraftFont;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

@BuildersModuleVersion("1.0.5")
public class MinecraftFontSizeCalculation {
    private static final byte[] sizes = new byte[(0xFFFF + 1) * Float.BYTES];

    private static void read(InputStream stream) {
        try {
            if (stream == null) {
                ChannelBuilder.plugin.getLogger().log(Level.SEVERE, "Unfounded resource minecraft_font_sizes.bin!");
            } else {
                try (InputStream autoClose = stream) {
                    int read = sizes.length;
                    int start = 0;
                    do {
                        int r = autoClose.read(sizes, start, read);
                        if (r == -1) {
                            ChannelBuilder.plugin.getLogger().log(Level.SEVERE,
                                    "Incomplete reading but file has ended.");
                            break;
                        }
                        read -= r;
                        start += r;
                    } while (read > 0);
                }
            }
        } catch (Throwable error) {
            // error.printStackTrace();
            ChannelBuilder.plugin.getLogger().log(Level.SEVERE, "Error in reading minecraft_font_sizes.bin", error);
        }
    }

    static {
        String property = System.getProperty("vexview.static.boot");
        if (property == null) {
            read(MinecraftFontSizeCalculation.class.getResourceAsStream("/minecraft_font_sizes.bin"));
            // User custom font size
            File file = new File(ChannelBuilder.plugin.getDataFolder(), "minecraft_font_size.bin");
            if (file.isFile()) {
                try {
                    read(new FileInputStream(file));
                } catch (FileNotFoundException e) {
                    ChannelBuilder.plugin.getLogger().log(Level.WARNING, "Error in opening " + file);
                }
            }
        } else {
            try {
                read(new FileInputStream(property));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static float getWidth(char c) {
        return ByteBuffer.wrap(sizes, c * Float.SIZE, Float.SIZE).getFloat();
    }

    public static void calculatedSize(AtomicInteger width, AtomicInteger height, List<String> strings) {
        int lines = 0;
        for (String value : strings) {
            float wid = 0;
            for (String line : InputFieldBuilder.split(value)) {
                lines++;

                CharBuffer buffer = CharBuffer.wrap(line);
                while (buffer.hasRemaining()) {
                    char next = buffer.get();
                    if (next == '\u00a7') {
                        if (buffer.hasRemaining()) {
                            buffer.get();
                            continue;
                        }
                    }
                    wid += getWidth(next);
                }
            }
            width.set(Math.max(width.get(), (int) wid));
        }
        height.set((int) (lines * 9.5));
    }
}
