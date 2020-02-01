/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: SudokuResize.java@author: karlatemp@vip.qq.com: 2020/1/28 下午2:34@version: 2.0
 */

package lk.vexview.builders;

import java.util.function.Consumer;

/**
 * 9 Slice 缩放信息.
 * <p>
 * 使用9 Slice作为背景:
 * <pre>{@code
 * Slice9 slice9 = Slice9.slice9()
 *      .left(3) // 左切线
 *      .right(3)
 *      .top(3)
 *      .bottom(3) // 切线位置
 *
 *      .imageSize(
 *          7, 7, // 9 Slice位点图片大小
 *          1, 1 // 9 Slice中心图片大小
 *      )
 *      .address("[local]slice9.png", "[local]slice9.center.png");
 * VexGui gui = GuiBuilder.builder()
 *      .size(233, 233)
 *      .buildWith(slice9);
 * VexViewAPI.openGui(p, gui);
 * }</pre>
 * <p>
 * 渲染一个九格宫图片
 * <pre>{@code
 * VexViewAPI.openGui(p, GuiBuilder.builder()
 *      .newContext(
 *          Slice9.slice9()
 *              .left(3).right(3).top(3).bottom(3)........
 *              .size(50, 50) // 确定要显示的大小
 *              .offset(0, 20) // 进行相对移动
 *      )
 *      .build()
 * );
 * }</pre>
 *
 * @author Karlatemp
 * @since 2.6
 */
public class Slice9 extends Locator implements Consumer<GuiBuilder> {
    public int left, right, top, bottom;
    public int width, height;
    public String sudoku, center;
    public int
            isw, ish,
            icw, ich;

    public static Slice9 slice9() {
        return new Slice9();
    }

    protected Slice9() {
    }

    @Override
    public Slice9 clone() {
        Slice9 sr = new Slice9();
        sr.left = left;
        sr.right = right;
        sr.top = top;
        sr.bottom = bottom;
        sr.width = width;
        sr.height = height;
        sr.sudoku = sudoku;
        sr.center = center;
        sr.isw = isw;
        sr.ish = ish;
        sr.icw = icw;
        sr.ich = ich;
        return sr;
    }

    @Override
    public Slice9 location(int xOffset, int yOffset) {
        super.location(xOffset, yOffset);
        return this;
    }

    @Override
    public Slice9 offset(int x, int y) {
        super.offset(x, y);
        return this;
    }

    /**
     * 上切线位置
     *
     * @param top 切线与图片顶部的距离
     * @return 九格宫信息
     */
    public Slice9 top(int top) {
        this.top = top;
        return this;
    }

    /**
     * 下切线位置
     *
     * @param bottom 切线与图片底部的距离
     * @return 九格宫信息
     */
    public Slice9 bottom(int bottom) {
        this.bottom = bottom;
        return this;
    }

    /**
     * 左切线位置
     *
     * @param left 切线与图片左侧的距离
     * @return 九格宫信息
     */
    public Slice9 left(int left) {
        this.left = left;
        return this;
    }

    /**
     * 右切线位置
     *
     * @param right 切线与图片右侧距离
     * @return 九格宫信息
     */
    public Slice9 right(int right) {
        this.right = right;
        return this;
    }

    /**
     * 此9 Slice图片的渲染大小
     *
     * @param width  宽度
     * @param height 高度
     * @return 9 Slice信息本身
     */
    public Slice9 size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public Slice9 imageSize(int sudokuWidth, int sudokuHeight,
                            int centerWidth, int centerHeight) {
        isw = sudokuWidth;
        ish = sudokuHeight;

        icw = centerWidth;
        ich = centerHeight;
        return this;
    }

    /**
     * 定义9 Slice图片渲染用图片
     *
     * @param sudoku 9 Slice切割图片
     * @param center 9 Slice中心图片
     * @return 9 Slice信息
     */
    public Slice9 address(String sudoku, String center) {
        this.sudoku = sudoku;
        this.center = center;
        return this;
    }

    /**
     * 渲染9 Slice
     *
     * @param guiBuilder 一个新的Guider Context
     */
    @Override
    public void accept(GuiBuilder guiBuilder) {
        accept(guiBuilder, true);
    }

    /**
     * 渲染9 Slice
     *
     * @param guiBuilder 使用的Builder
     * @param doCenter   时候添加中心图片
     */
    public void accept(GuiBuilder guiBuilder, boolean doCenter) {
        GuiBuilder context = guiBuilder.newContext().offset(xOffset, yOffset);
        boolean wmin = width < left + right;
        boolean hmin = height < top + bottom;
        if (wmin) {
            if (hmin) {
                context
                        .image(lt -> lt.split().background(sudoku).splitOffset(0, 0)
                                .imageSize(isw, ish)
                                .splitSize(left, top).size(width / 2, height / 2))
                        .image(rt -> rt.split().background(sudoku).splitOffset(isw - right, 0)
                                .splitSize(right, top).size(width / 2, height / 2)
                                .imageSize(isw, ish)
                                .offset(width / 2, 0))
                        .image(lb -> lb.split().background(sudoku).splitOffset(0, ish - bottom)
                                .splitSize(left, bottom).size(width / 2, height / 2)
                                .imageSize(isw, ish)
                                .offset(0, height / 2))
                        .image(rb -> rb.split().background(sudoku).splitOffset(isw - right, ish - bottom)
                                .splitSize(right, bottom).size(width / 2, height / 2)
                                .offset(width / 2, height / 2)
                                .imageSize(isw, ish)
                        );
            } else {
                context
                        .image(lt -> lt.split().background(sudoku).splitOffset(0, 0)
                                .splitSize(left, top).size(width / 2, top)
                                .imageSize(isw, ish))
                        .image(rt -> rt.split().background(sudoku).splitOffset(isw - right, 0)
                                .splitSize(right, top).size(width / 2, top)
                                .offset(width / 2, 0)
                                .imageSize(isw, ish))
                        .image(lb -> lb.split().background(sudoku).splitOffset(0, ish - bottom)
                                .splitSize(left, bottom).size(width / 2, bottom)
                                .offset(0, height - top)
                                .imageSize(isw, ish))
                        .image(rb -> rb.split().background(sudoku).splitOffset(isw - right, ish - bottom)
                                .splitSize(right, bottom).size(width / 2, bottom)
                                .offset(width / 2, height - top)
                                .imageSize(isw, ish))
                        .image(lf -> lf.split().background(sudoku).splitOffset(0, top)
                                .splitSize(left, ish - top - bottom)
                                .size(width / 2, height - top - bottom)
                                .offset(0, top)
                                .imageSize(isw, ish))
                        .image(rg -> rg.split().background(sudoku).splitOffset(isw - right, top)
                                .splitSize(right, ish - top - bottom)
                                .size(width / 2, height - top - bottom)
                                .offset(width / 2, top)
                                .imageSize(isw, ish));
            }
        } else if (hmin) {
            context.image(
                    lt -> lt.split().splitSize(left, top).size(left, height / 2).background(sudoku).imageSize(isw, ish)
            ).image(
                    lb -> lb.split().splitSize(left, bottom).size(left, height / 2).background(sudoku).imageSize(isw, ish)
                            .splitOffset(0, ish - bottom)
                            .offset(0, height / 2)
            ).image(
                    rt -> rt.split().splitSize(right, top).size(right, height / 2).background(sudoku).imageSize(isw, ish)
                            .splitOffset(isw - right, 0)
                            .offset(width - left, 0)
            ).image(
                    rb -> rb.split().splitSize(right, bottom).size(right, height / 2).background(sudoku).imageSize(isw, ish)
                            .splitOffset(isw - right, ish - bottom)
                            .offset(width - left, height / 2)
            ).image(
                    tp -> tp.split().splitSize(isw - left - right, top).size(width - left - right, height / 2)
                            .background(sudoku).splitOffset(left, 0).imageSize(isw, ish)
                            .offset(left, 0)
            ).image(
                    bom -> bom.split().splitSize(isw - left - right, bottom).size(width - left - right, height / 2)
                            .background(sudoku).splitOffset(left, ish - bottom)
                            .offset(left, height / 2).imageSize(isw, ish)
            );
        } else {
            context.image(
                    // Left and Top
                    lt -> lt.split().splitSize(left, top).size(left, top).background(sudoku).imageSize(isw, ish)
            ).image(
                    // Right And Top
                    rt -> rt.split().splitSize(right, top).size(right, top).background(sudoku).imageSize(isw, ish)
                            .splitOffset(isw - right, 0).offset(width - right, 0)
            ).image(
                    // Left And Bottom
                    lb -> lb.split().splitSize(left, bottom).size(left, bottom).background(sudoku).imageSize(isw, ish)
                            .splitOffset(0, ish - bottom).offset(0, height - bottom)
            ).image(
                    // Right And Bottom
                    rb -> rb.split().splitSize(right, bottom).size(right, bottom).background(sudoku).imageSize(isw, ish)
                            .splitOffset(isw - right, ish - bottom).offset(width - right, height - bottom)
            ).image(
                    // Left
                    lf -> lf.split().splitSize(left, ish - top - bottom).size(left, height - top - bottom)
                            .background(sudoku).splitOffset(0, top).imageSize(isw, ish)
                            .offset(0, top)
            ).image(
                    // Right
                    rt -> rt.split().splitSize(right, ish - top - bottom).size(right, height - top - bottom)
                            .background(sudoku).splitOffset(isw - right, top).imageSize(isw, ish)
                            .offset(width - right, top)
            ).image(
                    // Top
                    tp -> tp.split().splitSize(isw - left - right, top).size(width - left - right, top)
                            .background(sudoku).splitOffset(left, 0).imageSize(isw, ish)
                            .offset(left, 0)
            ).image(
                    // Bottom
                    bm -> bm.split().splitSize(isw - left - right, bottom).size(width - left - right, bottom)
                            .background(sudoku).splitOffset(left, ish - bottom).imageSize(isw, ish)
                            .offset(left, height - bottom)
            );
            if (doCenter) {
                context.image(
                        ct -> ct.offset(left, top).size(width - left - right, height - top - bottom)
                                .imageSize(icw, ich).background(center)
                );
            }
        }
    }

}
