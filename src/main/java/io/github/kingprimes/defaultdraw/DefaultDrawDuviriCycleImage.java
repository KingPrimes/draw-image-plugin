package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.image.ImageIOUtils;
import io.github.kingprimes.model.worldstate.DuvalierCycle;
import io.github.kingprimes.model.worldstate.EndlessXpChoices;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认双衍王境图片绘制工具类
 *
 * @author KingPrimes
 * @version 1.0.0
 */
final class DefaultDrawDuviriCycleImage {

    private static final int DUVIRI_CYCLE_IMAGE_WIDTH = 1000;
    private static final int DUVIRI_CYCLE_IMAGE_HEIGHT = 600;

    private static final Color NORMAL_HEADER_COLOR = new Color(0xFFD700);
    private static final Color HARD_HEADER_COLOR = new Color(0xFF6B6B);

    private DefaultDrawDuviriCycleImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawDuviriCycleImage class");
    }

    /**
     * 绘制双衍王境循环图像
     *
     * @param duvalierCycle 双衍王境循环数据
     * @return 生成的双衍王境循环图像的 PNG 格式字节数组
     */
    public static byte[] drawDuviriCycleImage(DuvalierCycle duvalierCycle) {
        if (duvalierCycle == null) {
            return new byte[0];
        }

        // 创建画布
        ImageCombiner combiner = new ImageCombiner(
                DUVIRI_CYCLE_IMAGE_WIDTH,
                DUVIRI_CYCLE_IMAGE_HEIGHT,
                ImageCombiner.OutputFormat.PNG
        );

        // 填充背景色
        combiner.setFont(FONT)
                .setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, DUVIRI_CYCLE_IMAGE_WIDTH, DUVIRI_CYCLE_IMAGE_HEIGHT)
                // 绘制双层边框
                .drawTooRoundRect();

        // 绘制标题
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 32))
                .addCenteredText("双衍王境", 80);

        // 绘制表格
        int tableY = 120;
        int tableWidth = DUVIRI_CYCLE_IMAGE_WIDTH - 2 * IMAGE_MARGIN;
        int cellWidth = tableWidth / 2;
        int headerHeight = 60;
        int contentHeight = DUVIRI_CYCLE_IMAGE_HEIGHT - tableY - headerHeight - IMAGE_FOOTER_HEIGHT - 40;

        // 绘制表头
        // 普通表头
        combiner.setColor(NORMAL_HEADER_COLOR)
                .fillRect(IMAGE_MARGIN, tableY, cellWidth, headerHeight);

        combiner.setColor(TEXT_COLOR)
                .addText("普通", IMAGE_MARGIN + cellWidth / 2, tableY + headerHeight / 2 + 8);

        // 钢铁表头
        combiner.setColor(HARD_HEADER_COLOR)
                .fillRect(IMAGE_MARGIN + cellWidth, tableY, cellWidth, headerHeight);

        combiner.setColor(TEXT_COLOR)
                .addText("钢铁", IMAGE_MARGIN + cellWidth + cellWidth / 2, tableY + headerHeight / 2 + 8);

        // 绘制表格边框
        combiner.setColor(TEXT_COLOR)
                .setStroke(2)
                .drawLine(IMAGE_MARGIN, tableY, IMAGE_MARGIN + tableWidth, tableY) // 顶部线
                .drawLine(IMAGE_MARGIN, tableY + headerHeight, IMAGE_MARGIN + tableWidth, tableY + headerHeight) // 表头底部线
                .drawLine(IMAGE_MARGIN, tableY + headerHeight + contentHeight, IMAGE_MARGIN + tableWidth, tableY + headerHeight + contentHeight) // 底部线
                .drawLine(IMAGE_MARGIN, tableY, IMAGE_MARGIN, tableY + headerHeight + contentHeight) // 左侧线
                .drawLine(IMAGE_MARGIN + cellWidth, tableY, IMAGE_MARGIN + cellWidth, tableY + headerHeight + contentHeight) // 中间线
                .drawLine(IMAGE_MARGIN + tableWidth, tableY, IMAGE_MARGIN + tableWidth, tableY + headerHeight + contentHeight); // 右侧线

        // 绘制内容
        if (duvalierCycle.getChoices() != null) {
            drawChoices(combiner, duvalierCycle.getChoices(), tableY + headerHeight, cellWidth, contentHeight);
        }

        // 添加看板娘图片
        BufferedImage xiaoMeiWangImage = ImageIOUtils.getRandomXiaoMeiWangImage();
        combiner.drawImageWithAspectRatio(
                xiaoMeiWangImage,
                DUVIRI_CYCLE_IMAGE_WIDTH - 280,
                140,
                320,
                450
        );

        // 添加底部署名
        addFooter(combiner, DUVIRI_CYCLE_IMAGE_HEIGHT - IMAGE_FOOTER_HEIGHT);

        // 合并图像
        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }

    /**
     * 绘制选择项内容
     *
     * @param combiner      图像合成器
     * @param choices       选择项列表
     * @param startY        起始Y坐标
     * @param cellWidth     单元格宽度
     * @param contentHeight 内容区域高度
     */
    private static void drawChoices(ImageCombiner combiner, List<EndlessXpChoices> choices, int startY, int cellWidth, int contentHeight) {
        int padding = 20;
        int itemWidth = (cellWidth - 3 * padding) / 2;
        int itemHeight = 100;
        int maxItemsPerColumn = (contentHeight - padding) / (itemHeight + padding);

        int normalX = DrawConstants.IMAGE_MARGIN + padding;
        int hardX = DrawConstants.IMAGE_MARGIN + cellWidth + padding;
        int currentNormalY = startY + padding;
        int currentHardY = startY + padding;

        for (EndlessXpChoices choice : choices) {
            if (choice.getCategory() == EndlessXpChoices.Category.EXC_NORMAL) {
                drawChoiceItems(combiner, choice.getChoices(), normalX, currentNormalY, itemWidth, itemHeight, maxItemsPerColumn);
            }
            if (choice.getCategory() == EndlessXpChoices.Category.EXC_HARD) {
                drawChoiceItems(combiner, choice.getChoices(), hardX, currentHardY, itemWidth, itemHeight, maxItemsPerColumn);
            }
        }
    }

    /**
     * 绘制选择项
     *
     * @param combiner          图像合成器
     * @param choices           选择项列表
     * @param x                 X坐标
     * @param y                 Y坐标
     * @param width             宽度
     * @param height            高度
     * @param maxItemsPerColumn 每列最大项目数
     */
    private static void drawChoiceItems(ImageCombiner combiner, List<String> choices, int x, int y, int width, int height, int maxItemsPerColumn) {
        if (choices == null || choices.isEmpty()) {
            return;
        }

        int itemsToDraw = Math.min(choices.size(), maxItemsPerColumn);
        int itemHeight = height / itemsToDraw;
        int itemY = y + itemHeight / 2;
        for (String choice : choices) {
            combiner.setColor(TEXT_COLOR)
                    .addText(choice, x + width / 2, itemY);
            itemY += itemHeight;
        }
    }
}