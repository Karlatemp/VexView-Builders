/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: TextBuilder.java@author: karlatemp@vip.qq.com: 2020/1/27 下午8:16@version: 2.0
 */

package lk.vexview.builders;

import lk.vexview.gui.components.VexHoverText;
import lk.vexview.gui.components.VexText;
import lk.vexview.hud.VexTextShow;
import lk.vexview.tag.TagDirection;
import lk.vexview.tag.components.VexTextTag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * VexView 文本组件的构建器
 *
 * <pre>{@code
 * VexText text = TextBuilder.builder()
 *      .offset(20, 20)
 *      .addLine("l1")
 *      .addLine("l2")
 *      .....
 *      .build();
 * VexHoverText hover = TextBuilder.builder()
 *      .addLine("l1")
 *      ....
 *      .build();
 *
 * VexTextTag tag = TextBuilder.builder()
 *      .addLine("Text") // 只能有1行
 *      .toTag("TagId", x, y, z, black, new TagDirection(
 *          angle_x, angle_y, angle_z,
 *          for_player, player_can_see
 *      ));
 * }</pre>
 *
 * @author Karlatemp
 * @since 2.6
 */
public class TextBuilder extends Locator {
    protected int textWidth;
    protected List<String> lines;
    protected double scale = 1.0;
    protected VexHoverText hover;

    protected TextBuilder() {
    }

    public TextBuilder scale(double scale) {
        this.scale = scale;
        return this;
    }

    public TextBuilder location(int x, int y) {
        super.location(x, y);
        return this;
    }

    public TextBuilder offset(int x, int y) {
        super.offset(x, y);
        return this;
    }

    public static TextBuilder builder() {
        return new TextBuilder();
    }

    public TextBuilder addLine(String line) {
        preCheck();
        lines.add(line);
        return this;
    }

    public TextBuilder addLines(Collection<String> lines) {
        preCheck();
        this.lines.addAll(lines);
        return this;
    }

    public TextBuilder lines(List<String> lines) {
        this.lines = lines;
        return this;
    }

    public VexText build() {
        preCheck();
        if (hover == null) {
            return new VexText(xOffset, yOffset, lines, scale);
        }
        return new VexText(xOffset, yOffset, lines, scale, hover, textWidth);
    }

    @BuildersModuleVersion("1.0.3")
    public TextBuilder hover(Function<TextBuilder, TextBuilder> action, int textWidth) {
        hover = action.apply(builder()).buildHover();
        this.textWidth = textWidth;
        return this;
    }

    public VexHoverText buildHover() {
        preCheck();
        return new VexHoverText(lines);
    }

    private void preCheck() {
        if (lines == null) lines = new ArrayList<>();
    }

    public VexTextShow toHUD(String id, int time, int z) {
        preCheck();
        return new VexTextShow(
                id, xOffset, yOffset, z, lines, time
        );
    }

    /**
     * 构建 Tag
     *
     * @param id    Tag ID
     * @param x     X坐标偏移量
     * @param y     Y坐标偏移量
     * @param z     Z坐标偏移量
     * @param black 是否带有黑色背景
     * @param td    Tag显示方向
     * @return 构建的Tag
     */
    public VexTextTag toTag(String id, double x, double y, double z, boolean black, TagDirection td) {
        preCheck();
        if (lines.isEmpty()) {
            throw new NullPointerException("No any line in this builder.");
        }
        if (lines.size() != 1) {
            throw new IllegalArgumentException("Expected 1 line but get " + lines.size() + " lines.");
        }
        return new VexTextTag(
                id, x, y, z, lines.get(0), black, td
        );
    }

    @Override
    public TextBuilder copy(Locator newLocation) {
        return (TextBuilder) super.copy(newLocation);
    }
}
