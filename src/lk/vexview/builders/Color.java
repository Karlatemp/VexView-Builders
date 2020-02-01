/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: Color.java@author: karlatemp@vip.qq.com: 2020/1/28 上午12:53@version: 2.0
 */

package lk.vexview.builders;

import java.util.Locale;

/**
 * 一个颜色
 *
 * @author Karlatemp
 * @since 2.6
 */
public class Color {
    public int code = 0xFF000000;

    public int code() {
        return code;
    }

    public Color code(int code) {
        this.code = code;
        return this;
    }

    public Color code(int red, int green, int blue) {
        return code(alpha(), red, green, blue);
    }

    public Color code(int alpha, int red, int green, int blue) {
        return alpha(alpha).red(red).green(green).blue(blue);
    }

    public static Color create() {
        return new Color();
    }

    public static Color create(int code) {
        return new Color().code(code);
    }

    protected Color() {
    }

    public int alpha() {
        return get(Byte.SIZE * 3);
    }

    public Color alpha(int alpha) {
        set(alpha, Byte.SIZE * 3);
        return this;
    }

    private void set(int val, int at) {
        code = code & (~(0xFF << at)) | ((val & 0xFF) << at);
    }

    private int get(int at) {
        return (code >> at) & 0xFF;
    }

    public Color red(int red) {
        set(red, Byte.SIZE * 2);
        return this;
    }

    public int red() {
        return get(Byte.SIZE * 2);
    }

    public Color blue(int blue) {
        set(blue, 0);
        return this;
    }

    public int blue() {
        return get(0);
    }

    public Color green(int green) {
        set(green, Byte.SIZE);
        return this;
    }

    public int green() {
        return get(Byte.SIZE);
    }

    public String toString() {
        String KoKoDaYo = Integer.toHexString(code).toUpperCase(Locale.ROOT);
        // 0xFFFFFFFF: 8

        return "Color{r=" + red() + ", g=" + green() + ", b=" + blue() + ", a=" + alpha() + ", 0x" + "FFFFFFFF".substring(KoKoDaYo.length()) + KoKoDaYo + "}";
    }
}
