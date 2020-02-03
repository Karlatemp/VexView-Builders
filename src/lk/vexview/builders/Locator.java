/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: Locator.java@author: karlatemp@vip.qq.com: 2020/1/28 下午1:25@version: 2.0
 */

package lk.vexview.builders;

import java.lang.invoke.MethodHandles;
import java.util.function.Consumer;

/**
 * 一个用于相对移动/区块设计用的定位器.
 *
 * <pre>{@code
 * VexSlot slot = Locator.locator()
 *      .offset(200, 0)
 *      .offset(0, 50)
 *      .buildComponent((x, y) -> new VexSlot(id, x, y, item));
 * }</pre>
 *
 * @author Karlatemp
 * @since 1.0.0
 */
public class Locator {
    static {
        ReflectionUtil.register(Locator.class, MethodHandles.lookup());
    }

    /**
     * 定位器中的相对位置, 用于创建组件
     */
    public int xOffset, yOffset;

    public static Locator locator() {
        return new Locator();
    }

    protected Locator() {
    }

    public Locator apply(Consumer<? super Locator> action) {
        action.accept(this);
        return this;
    }

    /**
     * 使此构建组件用的原点位置移动
     * <p>
     * 原点位置为 30,20<br/>
     * 使用 offset(10,5) 后,<br/>
     * 原点位置为 30+10,20+5 (40,25)
     * </p>
     *
     * @param x X轴长度
     * @param y Y轴长度
     * @return 构造器本身
     * @see #location(int, int)
     */
    public Locator offset(int x, int y) {
        xOffset += x;
        yOffset += y;
        return this;
    }

    /**
     * 改变此构建器构建组件用的原点位置
     *
     * @param xOffset 原点x坐标
     * @param yOffset 原点y坐标
     * @return 构造器本身
     * @see #offset(int, int)
     */
    public Locator location(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        return this;
    }

    public interface ComponentBuildAction<R> {
        R build(int x, int y);
    }

    /**
     * 生成此对象的副本.
     *
     * <pre>{@code
     * final ButtonBuilder baseButton = ButtonBuilder.builder()
     *      .size(width, height).background(......);
     *
     * GuiBuilder.builder()
     *      .button(loc -> baseButton.copy(loc).id( "Button 1 id" ).text( "Button 1 Text" ))
     *      .button(loc -> baseButton.copy(loc).id( "Button 2 id" ).text( "Button 2 Text" ))
     *      ...;
     * }</pre>
     *
     * @param newLocation 如果有, 副本位置将于此位置相同
     * @return 副本
     * @since 1.0.3
     */
    @BuildersModuleVersion("1.0.3")
    public Locator copy(Locator newLocation) {
        Locator loc = ReflectionUtil.copyTo(this, getClass());
        if (newLocation != null) {
            loc.location(newLocation.xOffset, newLocation.yOffset);
        }
        return loc;
    }

    /**
     * 直接通过传入构建器构建组件, x,y 为定位好的位置, 不需要在过多修改
     *
     * <pre>{@code
     * VexSlot slot = Locator.locator()
     *      .offset(200, 0)
     *      .offset(0, 50)
     *      .buildComponent((x, y) -> new VexSlot(id, x, y, item));
     * }</pre>
     *
     * @param builder 构建器
     * @param <T>     返回类型
     * @return 构建器执行返回
     */
    public final <T> T buildComponent(ComponentBuildAction<T> builder) {
        return builder.build(xOffset, yOffset);
    }
}
