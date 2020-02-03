/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ButtonBuilder.java@author: karlatemp@vip.qq.com: 2020/1/27 下午8:40@version: 2.0
 */

package lk.vexview.builders;

import lk.vexview.gui.components.ButtonFunction;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexHoverText;
import lk.vexview.gui.components.expand.VexClickableButton;
import lk.vexview.hud.VexButtonShow;
import org.bukkit.map.MinecraftFont;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

/**
 * 这是一个按钮的构造器
 * <p>
 * <pre>
 * {@code
 *      VexButton button = ButtonBuilder.builder().background(
 *          "[local]button.png","[local]button2.png"
 *      ).text("This is a button")
 *      .calculateSize(10,10,0,0) // 由于服务器计算的大小与客户端的未必一样，所以建议left/right为5以上的数字
 *      .build();
 *
 *      VexClickableButton clickable = (VexClickableButton)ButtonBuilder.builder()
 *              .background("[local]button.png", "[local]button2.png")
 *              .text("This is a clickable button")
 *              .calculateSize(10,10,0,0)
 *              .unClickable("[local]button.disable.png")
 *              .clickable(false)
 *              .build();
 * }
 * </pre>
 *
 * @author Karlatemp
 * @since 2.6
 */
@BuilderCommit("1.0.3: extends Locator")
public class ButtonBuilder extends Locator {//@version 1.0.3: Extends Locator.

    static class ClickableButtonBuilder extends ButtonBuilder {
        protected boolean clickable;
        protected String unclickable;

        @Override
        public ButtonBuilder clickable(boolean clickable) {
            this.clickable = clickable;
            return this;
        }

        @Override
        public VexButton build() {
            checkup();
            Objects.requireNonNull(unclickable, "Unclickable Image unset");
            VexButton button = new VexClickableButton(id, text,
                    background, focus, unclickable, xOffset, yOffset,
                    width, height, clickable);
            if (c != null) button.setFunction(c);
            if (hover != null) button.setHover(hover);
            return button;
        }

        @Override
        public ButtonBuilder unClickable(String image) {
            this.unclickable = image;
            return this;
        }
    }

    @Override
    public ButtonBuilder copy(Locator newLocation) {
        return (ButtonBuilder) super.copy(newLocation);
    }

    static {
        ReflectionUtil.register(ButtonBuilder.class, MethodHandles.lookup());
    }

    protected String id;
    // @version 1.0.3: Move to Locator
    // protected int x;
    // protected int y;
    protected ButtonFunction c;
    protected VexHoverText hover;
    protected String focus;
    protected String background;
    protected int height;
    protected int width;
    protected String text;

    protected ButtonBuilder() {
    }

    /**
     * 定义此按钮的 ID
     *
     * @param id 按钮ID
     * @return this
     */
    public ButtonBuilder id(String id) {
        this.id = id;
        return this;
    }

    /**
     * 定义按钮的背景图片
     *
     * @param background 按钮未选中状态贴图链接
     * @param focus      按钮被选中状态贴图链接
     * @return 构造器本身
     */
    public ButtonBuilder background(String background, String focus) {
        if (focus == null) focus = background;
        this.background = background;
        this.focus = focus;
        return this;
    }

    /**
     * 改变此按钮的<b>绝对</b>位置
     *
     * @param x 按钮x坐标
     * @param y 按钮y坐标
     * @return 构造器本身
     * @see #offset(int, int)
     */
    // @version: 1.0.3 Override
    @Override
    public ButtonBuilder location(int x, int y) {
        super.location(x, y);
        return this;
    }

    /**
     * 定义按钮的大小
     *
     * @param width  按钮宽度
     * @param height 按钮高度
     * @return 构造器本身
     */
    public ButtonBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 使得按钮的位置相对移动一段距离
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
    // @version: 1.0.3 Override
    @Override
    public ButtonBuilder offset(int x, int y) {
        super.offset(x, y);
        return this;
    }

    /**
     * 自动确定按钮的大小
     *
     * @param leftOffset   按钮文字与按钮左端的距离
     * @param rightOffset  按钮文字与按钮右端的距离
     * @param topOffset    按钮文字与按钮顶部的距离
     * @param bottomOffset 按钮文字与按钮底部的距离
     * @return 构造器本身
     */
    public ButtonBuilder calculateSize(int leftOffset, int rightOffset, int topOffset, int bottomOffset) {
        if (text == null) {
            return size(leftOffset + rightOffset, topOffset + bottomOffset);
        } else {
            return size(leftOffset + rightOffset + MinecraftFont.Font.getWidth(text),
                    topOffset + bottomOffset + MinecraftFont.Font.getHeight());
        }
    }

    /**
     * 设置按钮被点击时调用的回调
     *
     * @param action 回调
     * @return 构造器本身
     */
    public ButtonBuilder click(ButtonFunction action) {
        this.c = action;
        return this;
    }

    /**
     * 定义按钮的文字
     *
     * @param text 按钮文字
     * @return 构造器本身
     */
    public ButtonBuilder text(String text) {
        this.text = text;
        return this;
    }

    /**
     * 设置鼠标悬浮在按钮上的显示文字
     *
     * @param hover 悬浮文字
     * @return 构造器本身
     */
    public ButtonBuilder hover(VexHoverText hover) {
        this.hover = hover;
        return this;
    }

    /**
     * 获取一个新的ButtonBuilder
     *
     * @return 一个新的ButtonBuilder
     */
    public static ButtonBuilder builder() {
        return new ButtonBuilder();
    }

    /**
     * 设置按钮不能点击显示的背景图片
     *
     * @param image 图片链接
     * @return 一个新的ClickableButtonBuilder
     */
    public ButtonBuilder unClickable(String image) {
        ClickableButtonBuilder cb = ReflectionUtil.copyTo(this, ClickableButtonBuilder.class);
        cb.clickable = true;
        return cb.unClickable(image);
    }

    /**
     * 定义按钮是否可以点击
     *
     * @param clickable 时候能点击
     * @return 一个新的ClickableButtonBuilder
     */
    public ButtonBuilder clickable(boolean clickable) {
        ClickableButtonBuilder cb = ReflectionUtil.copyTo(this, ClickableButtonBuilder.class);
        return cb.clickable(clickable);
    }

    /**
     * 构建按钮, 如果先前使用了 {@link #clickable(boolean)}/{@link #unClickable(String)}的话,
     * 会返回 {@link VexClickableButton}
     *
     * @return 一个按钮
     */
    public VexButton build() {
        return build0();
    }

    protected void checkup() {
        Objects.requireNonNull(id, "Button id unset");
        Objects.requireNonNull(background, "Button background unset");
        Objects.requireNonNull(focus, "Button focus unset");
    }

    private VexButton build0() {
        checkup();
        VexButton button = new VexButton(id, text,
                background, focus, xOffset, yOffset,
                width, height);
        if (width == 0 || height == 0) {
            ChannelBuilder.plugin.getLogger().warning("Button [" + id + "] size is 0.");
        }
        if (c != null) button.setFunction(c);
        if (hover != null) button.setHover(hover);
        return button;
    }

    /**
     * 构建 {@link VexButtonShow}, 不支持VexClickableButton
     *
     * @param id   HUD ID, 不可重复
     * @param time HUD持续显示时间, 0为无限显示
     * @param z    HUD Z轴
     * @return 构建好的Button
     */
    public VexButtonShow toHud(String id, int time, int z) {
        return new VexButtonShow(id, build0(), time, z);
    }
}
