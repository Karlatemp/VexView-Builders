/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ScrollingListBuilder.java@author: karlatemp@vip.qq.com: 2020/1/27 下午9:14@version: 2.0
 */

package lk.vexview.builders;

import lk.vexview.gui.components.*;
import org.bukkit.inventory.ItemStack;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 滚动栏构建器
 *
 * @author Karlatemp
 * @see GuiBuilder
 * @see #componentLocation(int, int)
 * @see #componentOffset(int, int)
 * @since 2.6
 */
public class ScrollingListBuilder extends Locator {
    public int width, height, fullHeight, rightOffset, bottomOffset;
    private ScrollingListBuilder parent;
    /**
     * 内部组件偏移
     */
    public int topOffset, leftOffset;

    public static ScrollingListBuilder builder() {
        return new ScrollingListBuilder();
    }

    /**
     * 与 {@link #location(int, int)} 不同，这确定子组件的相对位置
     *
     * @param leftOffset XOffset
     * @param topOffset  YOffset
     * @return 构建器本身
     */
    public ScrollingListBuilder componentLocation(int leftOffset, int topOffset) {
        this.leftOffset = leftOffset;
        this.topOffset = topOffset;
        return this;
    }

    static {
        ReflectionUtil.register(ScrollingListBuilder.class, MethodHandles.lookup());
    }

    @Override
    public ScrollingListBuilder copy(Locator newLocation) {
        return (ScrollingListBuilder) super.copy(newLocation);
    }

    /**
     * 与 {@link #offset(int, int)} (int, int)} 不同，这确定子组件的相对位置
     *
     * @param leftOffset XOffset
     * @param topOffset  YOffset
     * @return 构建器本身
     * @see Locator#offset(int, int)
     */
    public ScrollingListBuilder componentOffset(int leftOffset, int topOffset) {
        this.leftOffset += leftOffset;
        this.topOffset += topOffset;
        return this;
    }

    /**
     * 看 {@link GuiBuilder#border(int, int)} 去
     *
     * @param rightOffset  .
     * @param bottomOffset .
     * @return .
     */
    public ScrollingListBuilder border(int rightOffset, int bottomOffset) {
        if (parent != null) {
            parent.border(rightOffset, bottomOffset);
            return this;
        }
        this.rightOffset = rightOffset;
        this.bottomOffset = bottomOffset;
        return this;
    }

    protected Collection<ScrollingListComponent> components = new ConcurrentLinkedQueue<>();

    public Collection<ScrollingListComponent> getComponents() {
        return components;
    }

    public void setComponents(Collection<ScrollingListComponent> components) {
        this.components = components;
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
    public ScrollingListBuilder location(int x, int y) {
        // @version 1.0.1: 当不是根构建器的时候用于组件构建原点设置
        if (parent != null) return componentLocation(x, y);
        super.location(x, y);
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
    public ScrollingListBuilder offset(int x, int y) {
        // @version 1.0.1: 当不是根构建器的时候用于组件构建原点移动
        if (parent != null) return componentOffset(x, y);
        super.offset(x, y);
        return this;
    }

    /**
     * 设置滚动栏的大小
     *
     * @param width  宽度
     * @param height 高度
     * @return 构建器本身
     */
    public ScrollingListBuilder size(int width, int height) {
        if (parent != null) {
            parent.size(width, height);
            return this;
        }
        this.width = width;
        this.fullHeight = height;
        return this;
    }

    /**
     * 设置玩家可以看见的高度
     *
     * @param height 可视高度
     * @return 构建器本身
     */
    public ScrollingListBuilder visitHeight(int height) {
        if (parent != null) {
            parent.visitHeight(height);
            return this;
        }
        this.height = height;
        return this;
    }

    protected ScrollingListBuilder() {
    }

    public VexScrollingList build() {
        if (parent != null) return parent.build();
        VexScrollingList list = new VexScrollingList(
                xOffset, yOffset, width, height, fullHeight
        );
        for (ScrollingListComponent component : components) {
            list.addComponent(component);
        }
        return list;
    }

    /**
     * @param action .
     * @return .
     * @see GuiBuilder#image(Function)
     */
    public ScrollingListBuilder image(Function<ImageBuilder, ImageBuilder> action) {
        ImageBuilder builder = ImageBuilder.builder();
        builder.location(leftOffset, topOffset);
        return addComponent(action.apply(builder).build());
    }

    /**
     * 添加组件
     *
     * @param component 子组件
     * @return 构建器本身
     */
    public ScrollingListBuilder addComponent(ScrollingListComponent component) {
        components.add(component);
        return this;
    }

    /**
     * .
     *
     * @param action 操作
     * @return 构架器本身
     * @see ButtonBuilder
     * @see GuiBuilder#slot(Function)
     */
    public ScrollingListBuilder slot(Function<SlotBuilder, SlotBuilder> action) {
        return addComponent(action.apply(SlotBuilder.builder().location(xOffset, yOffset)).build());
    }

    /**
     * @param action .
     * @return .
     * @see GuiBuilder#text(Function)
     */
    public ScrollingListBuilder text(Function<TextBuilder, TextBuilder> action) {
        TextBuilder builder = TextBuilder.builder();
        builder.location(leftOffset, topOffset);
        return addComponent(action.apply(builder).build());
    }

    /**
     * @param action .
     * @return .
     * @see GuiBuilder#button(Function)
     */
    public ScrollingListBuilder button(Function<ButtonBuilder, ButtonBuilder> action) {
        ButtonBuilder builder = ButtonBuilder.builder();
        builder.location(leftOffset, topOffset);
        return addComponent(action.apply(builder).build());
    }

    public ScrollingListBuilder newContext(Consumer<ScrollingListBuilder> action) {
        newContext().accept(action);
        return this;
    }

    public ScrollingListBuilder accept(Consumer<? super ScrollingListBuilder> action) {
        action.accept(this);
        return this;
    }

    public ScrollingListBuilder newContext() {
        ScrollingListBuilder copy = ReflectionUtil.allocate(getClass());
        copy.components = components;
        copy.height = height;
        copy.width = width;
        copy.xOffset = xOffset;
        copy.yOffset = yOffset;
        copy.bottomOffset = bottomOffset;
        copy.rightOffset = rightOffset;
        copy.fullHeight = fullHeight;
        copy.parent = this;
        // @version 1.0.1: 没有LT复制
        copy.leftOffset = leftOffset;
        copy.topOffset = topOffset;
        return copy;
    }

    /**
     * 自动计算滚动栏的大小
     *
     * @return 构建器本身
     * @see GuiBuilder#calculateSize()
     */
    @SuppressWarnings("DuplicatedCode")
    public ScrollingListBuilder calculateSize() {
        if (parent != null) {
            parent.calculateSize();
            return this;
        }
        AtomicInteger width = new AtomicInteger(0);
        AtomicInteger height = new AtomicInteger(0);
        GuiBuilder.calculateSize(components, width, height);
        this.width = width.get() + rightOffset;
        this.fullHeight = height.get() + bottomOffset;
        return this;
    }

    public static ScrollingListBuilder of(VexScrollingList list) {
        ScrollingListBuilder builder = ReflectionUtil.allocate(ScrollingListBuilder.class);
        builder.components = list.getComponents();
        builder.height = list.getHeight();
        builder.width = list.getWidth();
        builder.xOffset = list.getX();
        builder.yOffset = list.getY();
        return builder;
    }
}
