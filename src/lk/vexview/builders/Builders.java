/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: Builders.java@author: karlatemp@vip.qq.com: 2020/1/28 上午1:19@version: 2.0
 */

package lk.vexview.builders;

import org.bukkit.entity.Entity;

/**
 * @author Karlatemp
 * @since 1.0.0
 */
public class Builders {
    public static TextBuilder texts() {
        return TextBuilder.builder();
    }

    public static ChannelBuilder channel() {
        return ChannelBuilder.builder();
    }

    public static Base64ImageBuilder base64() {
        return ImageBuilder.builder().base64();
    }

    public static SplitImageBuilder splitImage() {
        return ImageBuilder.builder().split();
    }

    public static SplitImageBuilder mcImage() {
        return ImageBuilder.builder().mcImage();
    }

    public static GuiBuilder gui() {
        return GuiBuilder.builder();
    }

    public static InputFieldBuilder input() {
        return InputFieldBuilder.builder();
    }

    public static ScrollingListBuilder scrolling() {
        return ScrollingListBuilder.builder();
    }

    public static Color color() {
        return Color.create();
    }

    public static Locator locator() {
        return Locator.locator();
    }

    public static SlotBuilder slot() {
        return SlotBuilder.builder();
    }

    public static <T extends Entity> EntityDrawBuilder<T> draw() {
        return EntityDrawBuilder.builder();
    }
}
