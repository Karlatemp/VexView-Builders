/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: SlotBuilder.java@author: karlatemp@vip.qq.com: 2020/1/28 下午4:38@version: 2.0
 */

package lk.vexview.builders;

import lk.vexview.gui.components.VexSlot;
import org.bukkit.inventory.ItemStack;

import java.lang.invoke.MethodHandles;

/**
 * 一个{@link VexSlot}的快速构造器.
 *
 * <pre>{@code
 * VexSlot slot = SlotBuilder.builder()
 *      .offset(0, 20) //位置
 *      .item( stack )
 *      .id(0) // 如果是在GuiBuilder中则不需要调用此方法
 *      .build();
 * }</pre>
 *
 * @author Karlatemp
 * @since 1.0.0
 */
public class SlotBuilder extends Locator {
    protected int id;
    protected ItemStack item;

    protected SlotBuilder() {
    }

    static {
        ReflectionUtil.register(SlotBuilder.class, MethodHandles.lookup());
    }

    @Override
    public SlotBuilder copy(Locator newLocation) {
        return (SlotBuilder) super.copy(newLocation);
    }

    public static SlotBuilder builder() {
        return new SlotBuilder();
    }

    @Override
    public SlotBuilder offset(int x, int y) {
        super.offset(x, y);
        return this;
    }

    @Override
    public SlotBuilder location(int xOffset, int yOffset) {
        super.location(xOffset, yOffset);
        return this;
    }

    public SlotBuilder item(ItemStack stack) {
        item = stack;
        return this;
    }

    /**
     * 在GuiBuilder中不需要使用此方法，因为GuiBuilder会为VexSlot自动分配ID
     *
     * @param id ID
     * @return 构建器本身
     */
    public SlotBuilder id(int id) {
        this.id = id;
        return this;
    }

    public VexSlot build() {
        // @version 1.0.4: Location error fix.
        return new VexSlot(id, xOffset, yOffset, item);
    }
}
