/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: CheckboxBuilder.java@author: karlatemp@vip.qq.com: 2020/1/28 下午1:34@version: 2.0
 */

package lk.vexview.builders;

import lk.vexview.gui.components.VexCheckBox;
import lk.vexview.gui.components.VexHoverText;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

/**
 * 一个{@link VexCheckBox}的构建器
 * <p>
 * <pre>{@code
 * VexCheckBox check = CheckboxBuilder.builder()
 *      .background("[local]c.unchecked.png","[local]c.checked.png")
 *      .size(10,10)
 *      .build();
 * }</pre>
 *
 * @author Karlatemp
 * @since 2.6
 */
public class CheckboxBuilder extends Locator {
    protected int id;
    protected boolean checked;
    protected int width;
    protected int height;
    protected VexHoverText hover;
    protected String background, focus;

    static {
        ReflectionUtil.register(CheckboxBuilder.class, MethodHandles.lookup());
    }

    public static CheckboxBuilder builder() {
        return new CheckboxBuilder();
    }

    @Override
    public CheckboxBuilder copy(Locator newLocation) {
        return (CheckboxBuilder) super.copy(newLocation);
    }

    /**
     * 定义此勾选框的大小
     *
     * @param width  此勾选框的宽度
     * @param height 此勾选框的高度
     * @return 构建器本身
     */
    public CheckboxBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public CheckboxBuilder id(int id) {
        this.id = id;
        return this;
    }

    @Override
    public CheckboxBuilder location(int xOffset, int yOffset) {
        super.location(xOffset, yOffset);
        return this;
    }

    @Override
    public CheckboxBuilder offset(int x, int y) {
        super.offset(x, y);
        return this;
    }

    public CheckboxBuilder hover(VexHoverText hover) {
        this.hover = hover;
        return this;
    }

    public CheckboxBuilder checked(boolean checked) {
        this.checked = checked;
        return this;
    }

    /**
     * 设置此勾选框的图片链接
     *
     * @param background 未勾选显示的连接
     * @param focus      勾选后显示的连接
     * @return 构架器本身
     */
    public CheckboxBuilder background(String background, String focus) {
        this.background = background;
        this.focus = focus;
        return this;
    }

    public VexCheckBox build() {
        Objects.requireNonNull(background, "Checkbox's address cannot be null");
        Objects.requireNonNull(/* */focus, "Checkbox's address cannot be null");
        VexCheckBox lbwnb = new VexCheckBox(
                id, background, focus,
                xOffset, yOffset, width, height, checked
        );
        if (hover != null) lbwnb.setHover(hover);
        return lbwnb;
    }

    protected CheckboxBuilder() {
    }
}
