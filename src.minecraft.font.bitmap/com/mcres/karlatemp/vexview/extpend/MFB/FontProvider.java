/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: FontProvider.java@author: karlatemp@vip.qq.com: 2020/2/4 下午8:56@version: 2.0
 */

package com.mcres.karlatemp.vexview.extpend.MFB;

public interface FontProvider {
    interface Glyph {
        int width();

        default int height() {
            return 16;
        }

        float getAdvance();
    }

    Glyph getGlyph(char c);

}
