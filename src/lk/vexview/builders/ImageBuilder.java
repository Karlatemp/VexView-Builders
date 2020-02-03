/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ImageBuilder.java@author: karlatemp@vip.qq.com: 2020/1/27 下午6:24@version: 2.0
 */

package lk.vexview.builders;

import lk.vexview.gui.components.VexHoverText;
import lk.vexview.gui.components.VexImage;
import lk.vexview.gui.components.expand.VexGifImage;
import lk.vexview.hud.VexImageShow;
import lk.vexview.hud.VexShow;
import lk.vexview.tag.TagDirection;
import lk.vexview.tag.components.VexGifImageTag;
import lk.vexview.tag.components.VexImageTag;
import lk.vexview.tag.components.VexTag;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

/**
 * 一个[图片/HUD/Tag]构造器.
 *
 * <pre>{@code
 * VexImage image = ImageBuilder.builder()
 *      .background("[local]login.png")
 *      .imageSize(564, 507)
 *      .size(400, 200)
 *      .offset(0, 20)// 位置
 *      .build();
 * }</pre>
 *
 * @author Karlatemp
 * @see SplitImageBuilder
 * @see Base64ImageBuilder
 * @since 2.6
 */
public class ImageBuilder extends Locator {
    protected int width, height;
    protected String background;
    protected int imageWidth;
    protected int imageHeight;

    static {
        ReflectionUtil.register(ImageBuilder.class, MethodHandles.lookup());
    }

    protected VexHoverText hover;


    public static ImageBuilder builder() {
        return new ImageBuilder();
    }

    protected ImageBuilder() {
    }

    /**
     * 定义图片的真实大小
     *
     * @param imageWidth  图片的真实宽度
     * @param imageHeight 图片的真实高度
     * @return 构建器本身
     */
    public ImageBuilder imageSize(
            int imageWidth, int imageHeight
    ) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        return this;
    }

    /**
     * 改变此图片的<b>绝对</b>位置
     *
     * @param x 图片x坐标
     * @param y 图片y坐标
     * @return 构造器本身
     * @see #offset(int, int)
     */
    @Override
    public ImageBuilder location(int x, int y) {
        super.location(x, y);
        return this;
    }

    /**
     * 使得图片的位置相对移动一段距离
     * <p>
     * 按钮源位置为 30,20<br/>
     * 使用 offset(10,5) 后,<br/>
     * 按钮位置为 30+10,20+5 (40,25)
     * </p>
     *
     * @param x X轴长度
     * @param y Y轴长度
     * @return 构造器本身
     * @see #location(int, int)
     */
    @Override
    public ImageBuilder offset(int x, int y) {
        super.offset(x, y);
        return this;
    }

    /**
     * 定义图片的显示大小
     *
     * @param width  显示宽度
     * @param height 显示高度
     * @return 构建器本身
     */
    public ImageBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 定义图片链接
     *
     * @param background 图片链接
     * @return 构建器本身
     */
    public ImageBuilder background(String background) {
        this.background = background;
        return this;
    }

    /**
     * 设置Hover信息
     *
     * @param hover Hover信息
     * @return 构建器本身
     * @since 1.0.3
     */
    @BuildersModuleVersion("1.0.3")
    public ImageBuilder hover(VexHoverText hover) {
        this.hover = hover;
        return this;
    }

    /**
     * 构建图片
     *
     * @return 构建的图片
     */
    public VexImage build() {
        VexImage image = build0();
        if (hover != null)
            image.setHover(hover);
        return image;
    }

    @BuildersModuleVersion("1.0.3; for edit hover text.")
    protected VexImage build0() {
        return new VexImage(background, xOffset, yOffset, width, height);
    }

    @Override
    public ImageBuilder copy(Locator newLocation) {
        return (ImageBuilder) super.copy(newLocation);
    }

    /**
     * 构建GIF图
     *
     * @param interval 每一帧的时间间隔
     * @return GIF图片
     */
    public VexGifImage gif(int interval) {
        VexGifImage gif = new VexGifImage(background, xOffset, yOffset, width, height, interval);
        if (hover != null) gif.setHover(hover);
        return gif;
    }

    /**
     * 转为Base64构建器
     *
     * @return Base64图片构建器
     */
    public Base64ImageBuilder base64() {
        return ReflectionUtil.copyTo(this, Base64ImageBuilder.class);
    }

    /**
     * 转为SplitImage构建器
     *
     * @return SplitImage构建器
     */
    public SplitImageBuilder split() {
        return ReflectionUtil.copyTo(this, SplitImageBuilder.class);
    }

    /**
     * 转为 MCImage构建器
     *
     * @return MCImage构建器
     */
    public SplitImageBuilder mcImage() {
        return ReflectionUtil.copyTo(this, SplitImageBuilder.MCImageBuilder.class);
    }

    /**
     * 转为HUD
     *
     * @param id   HUD ID
     * @param time HUD显示持续时间, 0为无限
     * @param z    HUD Z轴
     * @return 构建的HUD
     */
    public VexShow toHUD(String id, int time, int z) {
        Objects.requireNonNull(background, "Image Address unset");
        return new VexImageShow(
                id, background, xOffset, yOffset, z, imageWidth, imageHeight, width, height, time
        );
    }

    /**
     * 转为 Tag
     *
     * @param id        Tag ID
     * @param x         X坐标偏移量
     * @param y         Y坐标偏移量
     * @param z         Z坐标偏移量
     * @param width     图片显示宽度(与Gui不同，如1代表1格宽)
     * @param height    图片显示高度
     * @param direction Tag显示方向
     * @return 构建的Tag
     */
    public VexTag toTag(String id, double x, double y, double z, float width, float height, TagDirection direction) {
        Objects.requireNonNull(background, "Image Address unset");
        return new VexImageTag(
                id, x, y, z, background, imageWidth, imageHeight, width, height, direction
        );
    }

    /**
     * 转为 Tag
     *
     * @param id        Tag ID
     * @param x         X坐标偏移量
     * @param y         Y坐标偏移量
     * @param z         Z坐标偏移量
     * @param width     图片显示宽度（与Gui不同，如1代表1格宽）
     * @param height    图片显示高度
     * @param direction Tag显示方向
     * @return 构建的Tag
     */
    public VexGifImageTag toGifTag(String id, double x, double y, double z, float width, float height, TagDirection direction) {
        Objects.requireNonNull(background, "Image Address unset");
        return new VexGifImageTag(
                id, x, y, z, background, imageWidth, imageHeight, width, height, direction
        );
    }
}
