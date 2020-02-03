/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: Base64ImageBuilder.java@author: karlatemp@vip.qq.com: 2020/1/27 下午7:03@version: 2.0
 */

package lk.vexview.builders;

import lk.vexview.gui.components.VexHoverText;
import lk.vexview.gui.components.VexImage;
import lk.vexview.gui.components.expand.VexBase64Image;
import lk.vexview.hud.VexBase64ImageShow;
import lk.vexview.tag.TagDirection;
import lk.vexview.tag.components.VexGifImageTag;
import lk.vexview.tag.components.VexTag;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

/**
 * Base64 图片构造器.
 *
 * <p>
 * <pre>{@code
 *      VexBase64Image image = ImageBuilder.builder().base64().source(
 *              new FileInputStream("plugin/MyPlugin/SourceImage.jpg")
 *          ).id("")
 *          .size(60,60) // 显示大小
 *          .offset(0,0) // 图片(相对)位置
 *          .image();
 * }</pre>
 *
 * @author Karlatemp
 * @see lk.vexview.builders.ImageBuilder
 * @since 1.0.0
 */
public class Base64ImageBuilder extends ImageBuilder {
    protected String id;

    protected VexHoverText hover;

    static {
        ReflectionUtil.register(Base64ImageBuilder.class, MethodHandles.lookup());
    }

    protected InputStream source;

    /**
     * 定义Base64图片源
     *
     * @param stream 图片源
     * @return this
     */
    public Base64ImageBuilder source(InputStream stream) {
        Objects.requireNonNull(stream, "Image source cannot be null.");
        source = stream;
        return this;
    }

    /**
     * 定义Base64图片源
     *
     * @param source 图片源
     * @return this
     */
    public Base64ImageBuilder source(byte[] source) {
        Objects.requireNonNull(source, "Image source cannot be null.");
        this.source = new ByteArrayInputStream(source);
        return this;
    }

    /**
     * 定义Base64图片源
     *
     * @param image  读取的图片
     * @param format 格式化使用的格式, 比如 "png"
     * @return this
     * @throws IOException IO错误
     */
    public Base64ImageBuilder source(BufferedImage image, String format) throws IOException {
        Objects.requireNonNull(image, "Image source cannot be null.");
        if (format == null) format = "jpg";
        ByteArrayOutputStream bos = new ByteArrayOutputStream(image.getHeight() * image.getWidth());
        ImageIO.write(image, format, bos);
        return source(bos.toByteArray());
    }

    @Override
    public Base64ImageBuilder location(int x, int y) {
        super.location(x, y);
        return this;
    }

    @Override
    public Base64ImageBuilder offset(int x, int y) {
        super.offset(x, y);
        return this;
    }

    /**
     * 定义该Base64图片的ID
     *
     * @param id ID
     * @return this
     */
    public Base64ImageBuilder id(String id) {
        this.id = id;
        return this;
    }

    /**
     * 定义图片的大小
     *
     * @param width  显示的图片宽度
     * @param height 显示的图片高度
     * @return this
     */
    @Override
    public Base64ImageBuilder size(int width, int height) {
        super.size(width, height);
        return this;
    }

    /**
     * 在Base64中, imageSize和size是一样的，因为base64不需要图片实际高度这个值
     *
     * @param imageWidth  显示的图片宽度
     * @param imageHeight 显示的图片高度
     * @return this
     * @see #size(int, int)
     */
    @Override
    public Base64ImageBuilder imageSize(int imageWidth, int imageHeight) {
        size(imageWidth, imageHeight);
        return this;
    }

    /**
     * Base64 并没有URL一说
     *
     * @param background 任何字符串
     * @return this
     */
    @Override
    @Deprecated
    public Base64ImageBuilder background(String background) {
        return this;
    }

    /**
     * 返回本身
     *
     * @return 返回本身
     */
    @Override
    public Base64ImageBuilder base64() {
        return this;
    }

    /**
     * 请使用image()
     *
     * @return A UnsupportedOperationException()
     * @see #image()
     */
    @Override
    @Deprecated
    public VexImage build() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected VexImage build0() {
        throw new UnsupportedOperationException();
    }

    /**
     * 使用此构建器的信息构建一个Base64图片
     *
     * @return 构建好的图片
     * @throws IOException 流关闭错误
     */
    public VexBase64Image image() throws IOException {
        Objects.requireNonNull(source, "Base64 Image Source unset.");
        try (InputStream source = this.source) {
            if (hover == null) {
                return new VexBase64Image(source, id, xOffset, yOffset, width, height);
            }
            return new VexBase64Image(source, id, xOffset, yOffset, width, height, hover);
        }
    }

    @Override
    public Base64ImageBuilder copy(Locator newLocation) {
        return (Base64ImageBuilder) super.copy(newLocation);
    }

    /**
     * 定义图片的hover信息
     *
     * @param hover hover信息
     * @return this
     */
    public Base64ImageBuilder hover(VexHoverText hover) {
        this.hover = hover;
        return this;
    }

    @Override
    public VexBase64ImageShow toHUD(String id, int time, int z) {
        try {
            if (id != null) this.id = id;
            return new VexBase64ImageShow(z, image());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @Override
    public VexTag toTag(String id, double x, double y, double z, float width, float height, TagDirection direction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public VexGifImageTag toGifTag(String id, double x, double y, double z, float width, float height, TagDirection direction) {
        throw new UnsupportedOperationException();
    }
}
