/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: GuiBuilder.java@author: karlatemp@vip.qq.com: 2020/1/27 下午8:30@version: 2.0
 */

package lk.vexview.builders;

import lk.vexview.gui.VexGui;
import lk.vexview.gui.VexInventoryGui;
import lk.vexview.gui.components.*;
import lk.vexview.gui.components.expand.VexBase64Image;
import org.bukkit.entity.Player;
import org.bukkit.map.MapFont;
import org.bukkit.map.MinecraftFont;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.CharBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

/**
 * VexGui的快速构造器.
 *
 * <pre>{@code
 * VexViewAPI.openGUI(p, GuiBuilder.builder()
 *      // 各种方法.. 详情看方法注释
 * .build());
 * }</pre>
 *
 * @author Karlatemp
 * @since 1.0.0
 */
public class GuiBuilder extends Locator {
    /**
     * 已经注册进Builder的组件
     */
    public Collection<VexComponents> components = new ConcurrentLinkedQueue<>();
    /**
     * @see #calculateSize()
     * @see #border(int, int)
     */
    public int rightOffset, bottomOffset;
    /**
     * 构建使用的GUI大小
     */
    public int width, height;
    /**
     * @see #newContext()
     */
    protected GuiBuilder parent;
    /**
     * 此GUI是否可以关闭
     */
    public boolean closable = true;

    static {
        ReflectionUtil.register(GuiBuilder.class, MethodHandles.lookup());
    }

    /**
     * 设置Gui生成时使用的大小
     *
     * @param width  GUI的宽度
     * @param height GUI的长度
     * @return 构造器本身
     */
    public GuiBuilder size(int width, int height) {
        if (parent != null) {
            parent.size(width, height);
            return this;
        }
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * calculateSize 用, 定义自动确定大小后的追加大小
     *
     * @param rightOffset  追加宽度
     * @param bottomOffset 追加高度
     * @return 构造器本身
     * @see #calculateSize()
     */
    public GuiBuilder border(int rightOffset, int bottomOffset) {
        this.rightOffset = rightOffset;
        this.bottomOffset = bottomOffset;
        return this;
    }

    static void calculateSize(Collection<?> components0, AtomicInteger width, AtomicInteger height) {
        for (Object components : components0) {
            if (components instanceof VexSlot) {
                VexSlot slot = (VexSlot) components;
                width.set(Math.max(width.get(), slot.getX() + 16));
                height.set(Math.max(height.get(), slot.getY() + 16));
            } else if (components instanceof VexText) {
                VexText text = (VexText) components;
                int longest = 0;
                final MinecraftFont font = MinecraftFont.Font;
                int linesHeight = 0;
                for (String comp : text.getText()) {
                    for (String line : InputFieldBuilder.split(comp)) {
                        linesHeight++;
                        CharBuffer buffer = CharBuffer.wrap(line);
                        int currentWidth = 0;
                        while (buffer.hasRemaining()) {
                            char next = buffer.get();
                            if (next == '§') {
                                // Color code, skip
                                if (buffer.hasRemaining()) buffer.get();
                                continue;
                            }
                            final MapFont.CharacterSprite sprite = font.getChar(next);
                            if (sprite == null) {
                                // Error Glyph
                                currentWidth += 13;
                            } else {
                                currentWidth += sprite.getWidth();
                            }
                        }
                        longest = Math.max(longest, currentWidth);
                    }
                }
                linesHeight *= font.getHeight();
                width.set(Math.max(width.get(), (int) (text.getX() + Math.abs(text.getScale() * longest))));
                height.set(Math.max(height.get(), (int) (text.getY() + Math.abs(text.getScale() * linesHeight))));
            } else if (components instanceof VexScrollingList) {
                VexScrollingList list = (VexScrollingList) components;
                width.set(Math.max(width.get(), list.getX() + list.getWidth()));
                height.set(Math.max(height.get(), list.getY() + list.getHeight()));
            } else if (components instanceof VexImage) {
                VexImage image = (VexImage) components;
                width.set(Math.max(image.getX() + image.getXs(), width.get()));
                height.set(Math.max(image.getY() + image.getYs(), height.get()));
            } else if (components instanceof VexButton) {
                VexButton button = (VexButton) components;
                height.set(Math.max(button.getY() + button.getH(), height.get()));
                width.set(Math.max(button.getX() + button.getW(), width.get()));
            } else if (components instanceof VexBase64Image) {
                VexBase64Image image = (VexBase64Image) components;
                byte[] source = image.getBase64();
                try {
                    try (final ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(source))) {
                        final Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
                        if (readers.hasNext()) {
                            final ImageReader next = readers.next();
                            try {
                                next.setInput(iis);
                                width.set(Math.max(width.get(), image.getX() + next.getWidth(0)));
                                height.set(Math.max(height.get(), image.getY() + next.getWidth(0)));
                            } finally {
                                next.dispose();
                            }
                        }// else This Image Dont have any reader.
                    }
                } catch (IOException error) {
                    ChannelBuilder.plugin.getLogger().log(Level.WARNING,
                            "Base64 Image[" + image.getId() + "] not a valid image.", error);
                }
            } else if (components instanceof VexTextField) {
                VexTextField field = (VexTextField) components;
                width.set(Math.max(width.get(), field.getX() + field.getWidth()));
                height.set(Math.max(height.get(), field.getY() + field.getHeight()));
            } else if (components instanceof VexEntityDraw) {
                VexEntityDraw draw = (VexEntityDraw) components;
                height.set(Math.max(height.get(), draw.getY()));
                width.set(Math.max(width.get(), draw.getX() + (draw.getScale() / 2)));
            } else if (components instanceof VexTextArea) {
                VexTextArea area = (VexTextArea) components;
                width.set(Math.max(width.get(), area.getX() + area.getWidth()));
                height.set(Math.max(height.get(), area.getY() + area.getHeight()));
            }
        }
    }

    /**
     * 自动确定GUI大小, 可用 {@link #border(int, int)} 追加大小
     *
     * @return 构造器本身
     * @see #border(int, int)
     */
    @SuppressWarnings("DuplicatedCode")
    public GuiBuilder calculateSize() {
        if (parent != null) {
            parent.calculateSize();
            return this;
        }
        AtomicInteger width = new AtomicInteger(0);
        AtomicInteger height = new AtomicInteger(0);
        calculateSize(components, width, height);
        this.width = width.get() + rightOffset;
        this.height = height.get() + bottomOffset;
        return this;
    }

    protected GuiBuilder() {
    }

    /**
     * 创建一个新的运行时用于构建组件
     * <p>
     * 新的运行时除了相对位置 xOffset,yOffset之外其他的使用的都是主构建器
     *
     * @return 新的运行时
     */
    public GuiBuilder newContext() {
        GuiBuilder copy = ReflectionUtil.copyTo(this, GuiBuilder.class, GuiBuilder.class);
        copy.parent = this;
        return copy;
    }

    /**
     * 构建新的运行时, 用于Lambda
     * <pre>{@code
     * builder.newContext(b->{
     *     b.location(0,20).text(text->text.addLine("Line"))
     * })
     * }</pre>
     *
     * @param action 操作
     * @return 构建器本身
     */
    public GuiBuilder newContext(Consumer<GuiBuilder> action) {
        newContext().accept(action);
        return this;
    }

    /**
     * 把构建器本身作为参数执行动作
     *
     * @param action 需要执行的动作
     * @return 构建器本身
     */
    public GuiBuilder accept(Consumer<? super GuiBuilder> action) {
        action.accept(this);
        return this;
    }

    /**
     * 创建一个图片. 要进行图片的相对移动请使用 {@link ImageBuilder#offset(int, int)} 进行位置移动
     * <pre>{@code
     *  builder
     *      .image(image->image.background("[local]login.png")
     *              .imageSize(564,507)
     *              .size(400,200)
     *      )
     *      .....
     * }</pre>
     *
     * @param action 操作
     * @return 构建器本身
     */
    public GuiBuilder image(Function<ImageBuilder, ImageBuilder> action) {
        ImageBuilder builder = action.apply(ImageBuilder.builder().location(xOffset, yOffset));
        if (builder instanceof Base64ImageBuilder) {
            try {
                return addComponent(((Base64ImageBuilder) builder).image());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return addComponent(builder.build());
    }

    /**
     * 创建一个ScrollingList组件, 使用方法与 {@link #image(Function)} 一样
     *
     * @param action 操作
     * @return 构架器本身
     * @see ScrollingListBuilder
     */
    public GuiBuilder scrollingList(Function<ScrollingListBuilder, ScrollingListBuilder> action) {
        return addComponent(action.apply(ScrollingListBuilder.builder().location(xOffset, yOffset)).build());
    }

    /**
     * 创建一个输入组件, 使用方法与 {@link #image(Function)} 一样
     *
     * @param action 操作
     * @return 构架器本身
     * @see InputFieldBuilder
     */
    public GuiBuilder input(Function<InputFieldBuilder, InputFieldBuilder> action) {
        return addComponent(action.apply(InputFieldBuilder.builder().location(xOffset, yOffset)).build());
    }

    /**
     * 创建一个文本(VexText), 使用方法与 {@link #image(Function)} 一样
     *
     * @param action 操作
     * @return 构架器本身
     * @see TextBuilder
     */
    public GuiBuilder text(Function<TextBuilder, TextBuilder> action) {
        return addComponent(action.apply(TextBuilder.builder().location(xOffset, yOffset)).build());
    }

    /**
     * 创建一个{@link VexCheckBox}, 使用方法与 {@link #image(Function)} 一样
     *
     * @param action 操作
     * @return 构建器本身
     * @see CheckboxBuilder
     */
    public GuiBuilder check(Function<CheckboxBuilder, CheckboxBuilder> action) {
        return addComponent(action.apply(CheckboxBuilder.builder().location(xOffset, yOffset)).build());
    }

    /**
     * 创建一个按钮, 使用方法与 {@link #image(Function)} 一样
     *
     * @param action 操作
     * @return 构架器本身
     * @see ButtonBuilder
     */
    public GuiBuilder button(Function<ButtonBuilder, ButtonBuilder> action) {
        return addComponent(action.apply(ButtonBuilder.builder().location(xOffset, yOffset)).build());
    }

    /**
     * 添加一个 {@link VexEntityDraw} 组件, 使用方法与 {@link #image(Function)} 一样
     *
     * @param action 操作
     * @return 构架器本身
     * @see EntityDrawBuilder
     */
    public GuiBuilder draw(Function<EntityDrawBuilder<?>, EntityDrawBuilder<?>> action) {
        return addComponent(action.apply(EntityDrawBuilder.builder().location(xOffset, yOffset)).build());
    }


    /**
     * 创建一个{@link VexSlot}, 使用方法与 {@link #image(Function)} 一样
     *
     * @param action 操作
     * @return 构架器本身
     * @see ButtonBuilder
     */
    public GuiBuilder slot(Function<SlotBuilder, SlotBuilder> action) {
        return addComponent(action.apply(SlotBuilder.builder().location(xOffset, yOffset)).build());
    }

    /**
     * 使的构建组件用的原点位置移动
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
    @Override
    public GuiBuilder offset(int x, int y) {
        super.offset(x, y);
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
    @Override
    public GuiBuilder location(int xOffset, int yOffset) {
        super.location(xOffset, yOffset);
        return this;
    }

    public static GuiBuilder builder() {
        return new GuiBuilder();
    }

    /**
     * 直接往已注册组件添加值
     *
     * @param component 组件
     * @return 构建器本身
     */
    public GuiBuilder addComponent(VexComponents component) {
        Objects.requireNonNull(component, "Component cannot be null");
        this.components.add(component);
        return this;
    }

    public Collection<VexComponents> getComponents() {
        return components;
    }

    public GuiBuilder setComponents(Collection<VexComponents> components) {
        Objects.requireNonNull(components, "Components cannot be null.");
        this.components = components;
        return this;
    }

    public GuiBuilder closable(boolean closable) {
        if (parent != null) parent.closable(closable);
        else this.closable = closable;
        return this;
    }

    /**
     * 构建GUI
     *
     * @param background GUI背景图片
     * @param x          GUI的x坐标
     * @param y          GUI的y坐标
     * @return 构建好的GUI
     */
    public VexGui build(String background, int x, int y) {
        if (parent != null) return parent.build(background, x, y);
        componentErrorDetection();
        VexGui gui = new VexGui(background, x, y, width, height, ReflectionUtil.wrappedList(components));
        gui.setClosable(closable);
        return gui;
    }

    /**
     * 组件检错, 目前只有VexSlot
     *
     * @return 构建器本身
     */
    public GuiBuilder componentErrorDetection() {
        int id = 0;
        for (VexComponents comp : components) {
            if (comp instanceof VexSlot) {
                ((VexSlot) comp).setId(id++);
            }
        }
        return this;
    }

    public synchronized VexGui buildWith(Slice9 slice9, int x, int y) {
        if (parent != null) return parent.buildWith(slice9, x, y);
        Slice9 c = slice9.clone();
        c.size(width, height);
        c.location(0, 0);
        location(0, 0);
        Collection<VexComponents> current = components;
        components = new ConcurrentLinkedQueue<>();
        c.accept(this, false);
        components.addAll(current);
        current.clear();
        current.addAll(components);
        components = current;
        return build(slice9.center, x, y);
    }

    /**
     * 构建GUI
     *
     * @param background  GUI背景图片
     * @param x           GUI的x坐标
     * @param y           GUI的y坐标
     * @param imageWidth  GUI背景图片的真实宽度
     * @param imageHeight GUI背景图片的真实高度
     * @return 构建好的GUI
     */
    public VexInventoryGui buildInventory(
            String background, int x, int y,
            int imageWidth, int imageHeight,
            int slotLeft, int slotRight) {
        if (parent != null) return parent.buildInventory(
                background, x, y, imageWidth, imageHeight,
                slotLeft, slotRight);
        componentErrorDetection();
        VexInventoryGui gui = new VexInventoryGui(
                background, x, y,
                imageWidth, imageHeight,
                width, height,
                slotLeft,
                slotRight);
        gui.setComponents(ReflectionUtil.wrappedList(components));
        gui.setClosable(closable);
        return gui;
    }

    @Override
    public GuiBuilder copy(Locator newLocation) {
        return (GuiBuilder) super.copy(newLocation);
    }

    /**
     * 从VexGUI中创建一个Builder. 此Builder种的组件操作会同步到GUI中
     *
     * @param gui GUI源
     * @return Wrapped Builder
     */
    public static GuiBuilder of(VexGui gui) {
        GuiBuilder builder = ReflectionUtil.allocate(GuiBuilder.class);
        builder.components = gui.getComponents();
        builder.closable = gui.isClosable();
        builder.height = gui.getHeight();
        builder.width = gui.getWidth();
        return builder;
    }
}
