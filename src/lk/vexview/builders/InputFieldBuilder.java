/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: InputFieldBuilder.java@author: karlatemp@vip.qq.com: 2020/1/27 下午10:37@version: 2.0
 */

package lk.vexview.builders;

import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexHoverText;
import lk.vexview.gui.components.VexTextArea;
import lk.vexview.gui.components.VexTextField;
import lk.vexview.gui.components.expand.VexColorfulTextArea;
import lk.vexview.gui.components.expand.VexColorfulTextField;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * VexInputField和VexTextArea的快速构造器.
 *
 * <pre>{@code
 * InputFieldBuilder.builder()
 *      // .area()  // 这是一个VexTextArea
 *      // .field() // 这是一个VexTextField
 *      .build(); // 若没有执行area/field, 默认是VexTextField
 * }</pre>
 *
 * @author Karlatemp
 * @since 1.0.0
 */
public class InputFieldBuilder extends Locator {
    protected int width, height, maxLength = Integer.MAX_VALUE, id;
    protected String value = "";
    protected boolean isArea;
    protected VexHoverText hover;

    public static InputFieldBuilder builder() {
        return new InputFieldBuilder();
    }

    static {
        ReflectionUtil.register(InputFieldBuilder.class, MethodHandles.lookup());
    }

    @Override
    public InputFieldBuilder copy(Locator newLocation) {
        return (InputFieldBuilder) super.copy(newLocation);
    }

    /**
     * 改变此组件的<b>绝对</b>位置
     *
     * @param x 按钮x坐标
     * @param y 按钮y坐标
     * @return 构造器本身
     * @see #offset(int, int)
     */
    @Override
    public InputFieldBuilder location(int x, int y) {
        super.location(x, y);
        return this;
    }

    /**
     * 设置当鼠标悬浮在此组件时显示的信息
     *
     * @param hover 显示信息
     * @return 构建器本身
     */
    public InputFieldBuilder hover(VexHoverText hover) {
        this.hover = hover;
        return this;
    }

    /**
     * 使得组件的位置相对移动一段距离
     * <p>
     * 组件源位置为 30,20<br/>
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
    public InputFieldBuilder offset(int x, int y) {
        super.offset(x, y);
        return this;
    }

    /**
     * 设置此组件的大小
     *
     * @param width  组件宽度
     * @param height 组件高度
     * @return 构建器本身
     */
    public InputFieldBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 设置输入框可输入的内容的最大长度
     *
     * @param max 最大长度
     * @return 构建器本身
     */
    public InputFieldBuilder maxLength(int max) {
        maxLength = max;
        return this;
    }

    /**
     * 设置组件ID
     *
     * @param id 组件ID
     * @return 构建器本身
     */
    public InputFieldBuilder id(int id) {
        this.id = id;
        return this;
    }

    protected InputFieldBuilder() {
    }

    /**
     * 使用VexTextArea构建模式
     *
     * @return 构建器本身
     */
    public InputFieldBuilder area() {
        isArea = true;
        return this;
    }

    /**
     * 使用VexTextField构建模式
     *
     * @return 构建器本身
     */
    public InputFieldBuilder field() {
        isArea = false;
        return this;
    }

    protected Color main_color, side_color;

    /**
     * 与 color 一样的效果
     *
     * @see #color(Color, Color)
     * @since 1.0.0
     */
    public InputFieldBuilder fieldColor(Color main_color, Color side_color) {
        return color(main_color, side_color);
    }

    /**
     * 带颜色的输入框
     *
     * @param main_color 主要颜色（例如0x70EEAD0E颜色，70是alpha值（透明度），EEAD0E是实际颜色）
     * @param side_color 同上
     * @return 构建器本身
     * @see VexColorfulTextField
     * @see VexColorfulTextArea
     * @since 1.0.3
     */
    @BuildersModuleVersion("1.0.3")
    public InputFieldBuilder color(Color main_color, Color side_color) {

        this.main_color = main_color;
        this.side_color = side_color;
        return this;
    }

    public VexTextField buildField() {
        final VexTextField field;
        if (main_color != null && side_color != null) {
            field = new VexColorfulTextField(xOffset, yOffset, width, height, maxLength, id,
                    main_color.code, side_color.code, value);
        } else {
            field = new VexTextField(xOffset, yOffset, width, height, maxLength, id, value);
        }
        if (hover != null) field.setHover(hover);
        return field;
    }

    public VexTextArea buildArea() {
        final VexTextArea area;
        if (main_color != null && side_color != null) {
            area = new VexColorfulTextArea(xOffset, yOffset, width, height, maxLength, id, main_color.code, side_color.code, split(value));
        } else {
            area = new VexTextArea(xOffset, yOffset, width, height, maxLength, id, split(value));
        }
        if (hover != null) area.setHover(hover);
        return area;
    }

    /**
     * 以 {@code '\n'} 为分割符的切割方法
     *
     * @param val 文本值
     * @return 分割好的List
     */
    public static List<String> split(String val) {
        if (val == null) return Collections.emptyList();
        int first = val.indexOf('\n');
        if (first == -1) return Collections.singletonList(val);
        List<String> list = new ArrayList<>();
        list.add(val.substring(0, first));
        first++;
        do {
            int sdd = val.indexOf(first, '\n');
            if (sdd == -1) {
                list.add(val.substring(first));
                return list;
            } else {
                list.add(val.substring(first, sdd));
                first = sdd + 1;
            }
        } while (true);
    }

    public VexComponents build() {
        if (isArea) buildArea();
        return buildField();
    }

    /**
     * 设置组件默认值
     *
     * @param value 默认值
     * @return 构建器本身
     */
    public InputFieldBuilder value(String value) {
        this.value = value;
        return this;
    }
}
