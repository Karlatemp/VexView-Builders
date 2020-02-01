/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: SplitImageBuilder.java@author: karlatemp@vip.qq.com: 2020/1/27 下午7:15@version: 2.0
 */

package lk.vexview.builders;

import lk.vexview.gui.components.expand.VexGifImage;
import lk.vexview.gui.components.expand.VexMcImage;
import lk.vexview.gui.components.expand.VexSplitImage;
import lk.vexview.hud.VexMcImageShow;
import lk.vexview.hud.VexSplitImageShow;
import lk.vexview.tag.TagDirection;
import lk.vexview.tag.components.VexMcImageTag;
import lk.vexview.tag.components.VexSpiltImageTag;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

/**
 * 一个SplitImage的构建器
 *
 * <pre>{@code
 *  VexSplitImage image = ImageBuilder.builder().split()
 *      .background("[local]MyProject/9Slicing.png")
 *      .splitOffset(0,0) // 左上角切点
 *      .splitSize(2, 2) // 切割大小
 *      .imageSize(9, 9)
 *      .size(4, 4)
 *      .offset(100, 0)
 *      .build();
 *  // 在 [local]MP/9S 中, 以(0,0)为起点, 从大小为9x9的图片中截取2x2大小的图片, 并拉伸成4x4的大小显示在 (100,0) 的位置
 *  // 要使用MCImage则在build()前调用 .mcImage()
 * }</pre>
 *
 * @author Karlatemp
 * @see lk.vexview.builders.ImageBuilder
 * @since 2.6
 */
public class SplitImageBuilder extends ImageBuilder {
    static class MCImageBuilder extends SplitImageBuilder {
        static {
            ReflectionUtil.register(MCImageBuilder.class, MethodHandles.lookup());
        }

        @Override
        public SplitImageBuilder split() {
            throw new UnsupportedOperationException("Unsupported from MCImage to SplitImage");
        }

        @Override
        public VexSplitImage build() {
            return buildMc();
        }

        @Override
        public SplitImageBuilder mcImage() {
            return this;
        }
    }

    static {
        ReflectionUtil.register(SplitImageBuilder.class, MethodHandles.lookup());
    }

    protected int splitYOffset;
    protected int splitXOffset;
    protected int splitWidth;
    protected int splitHeight;

    /**
     * 定义图片切割位置
     *
     * @param xOffset 图形切割左顶点
     * @param yOffset 图形切割上顶点
     * @return 构建器本身
     */
    public SplitImageBuilder splitOffset(int xOffset, int yOffset) {
        this.splitXOffset = xOffset;
        this.splitYOffset = yOffset;
        return this;
    }

    @Override
    public SplitImageBuilder mcImage() {
        return ReflectionUtil.copyTo(this, MCImageBuilder.class);
    }

    @Override
    public SplitImageBuilder split() {
        return this;
    }

    /**
     * 定义切割的图片大小
     *
     * @param splitWidth  图形切割宽度
     * @param splitHeight 图形切割长度
     * @return 构建器本身
     */
    public SplitImageBuilder splitSize(int splitWidth, int splitHeight) {
        this.splitWidth = splitWidth;
        this.splitHeight = splitHeight;
        return this;
    }

    public SplitImageBuilder imageSize(
            int imageWidth, int imageHeight
    ) {
        super.imageSize(imageWidth, imageHeight);
        return this;
    }

    @Override
    public VexSplitImage build() {
        return new VexSplitImage(
                background, xOffset, yOffset,
                splitXOffset, splitYOffset,
                width, height,
                splitWidth, splitHeight,
                imageWidth, imageHeight
        );
    }

    @Override
    public VexSplitImageShow toHUD(String id, int time, int z) {
        return new VexSplitImageShow(
                id, background, xOffset, yOffset, z,
                imageWidth, imageHeight,
                width, height,
                time, splitWidth, splitHeight,
                splitXOffset, splitYOffset
        );
    }

    @Override
    public VexSpiltImageTag toTag(String id, double x, double y, double z, float width, float height, TagDirection direction) {
        Objects.requireNonNull(background, "Image Address unset");
        return new VexSpiltImageTag(
                id, background, x, y, z, imageWidth, imageHeight, width, height,
                splitWidth, splitWidth, splitXOffset, splitYOffset,
                direction
        );
    }

    public VexMcImageTag toMcTag(String id, double x, double y, double z, float width, float height, TagDirection direction) {
        Objects.requireNonNull(background, "Image Address unset");
        return new VexMcImageTag(
                id, background, x, y, z, imageWidth, imageHeight, width, height,
                splitWidth, splitWidth, splitXOffset, splitYOffset,
                direction
        );
    }

    public VexMcImageShow toMcHUD(String id, int z, int time) {
        Objects.requireNonNull(background, "Image Address unset");
        return new VexMcImageShow(
                id, background,
                xOffset, yOffset, z,
                imageWidth, imageHeight,
                width, height,
                time,
                splitHeight, splitWidth,
                splitXOffset, splitYOffset
        );
    }

    public VexMcImage buildMc() {
        Objects.requireNonNull(background, "Image Address unset");
        return new VexMcImage(
                background, xOffset, yOffset,
                splitXOffset, splitYOffset,
                width, height,
                splitWidth, splitHeight,
                imageWidth, imageHeight
        );
    }

    @Override
    public SplitImageBuilder location(int x, int y) {
        super.location(x, y);
        return this;
    }

    @Override
    public SplitImageBuilder offset(int x, int y) {
        super.offset(x, y);
        return this;
    }

    @Override
    public SplitImageBuilder size(int width, int height) {
        super.size(width, height);
        return this;
    }

    @Override
    public SplitImageBuilder background(String background) {
        super.background(background);
        return this;
    }

    @Override
    public Base64ImageBuilder base64() {
        throw new UnsupportedOperationException();
    }

    @Override
    public VexGifImage gif(int interval) {
        throw new UnsupportedOperationException();
    }
}
