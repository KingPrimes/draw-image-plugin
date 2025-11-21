package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.Ducats;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 市场金/银垃圾杜卡币图像绘制实现类
 *
 * @author KingPrimes
 * @version 1.0.0
 */
final class DefaultDrawMarketDucatsImage {

    private static final int IMAGE_WIDTH = 1400;
    private static final int IMAGE_MARGIN = 40;
    private static final int COLUMN_WIDTH = (IMAGE_WIDTH - 3 * IMAGE_MARGIN) / 2;
    private static final int TABLE_WIDTH = COLUMN_WIDTH;
    private static final int ROW_HEIGHT = 50;
    private static final int HEADER_HEIGHT = 50;
    private static final int TITLE_HEIGHT = 60;
    private static final int FOOTER_HEIGHT = 40;
    private static final Font HEADER_FONT = FONT.deriveFont(Font.BOLD, 18f);
    private static final Font ROW_FONT = FONT.deriveFont(16f);

    /**
     * 绘制市场金垃圾杜卡币图像
     *
     * @param dump 金垃圾数据
     * @return 图像流
     */
    public static byte[] drawMarketDucatsImage(Map<Ducats.DumpType, java.util.List<Ducats.Ducat>> dump, String title) {
        if (dump == null || dump.isEmpty()) {
            return new byte[0];
        }

        java.util.List<Ducats.Ducat> dayList = dump.get(Ducats.DumpType.DAY);
        List<Ducats.Ducat> hourList = dump.get(Ducats.DumpType.HOUR);

        // 计算图像高度
        int maxListSize = Math.max(
                dayList != null ? dayList.size() : 0,
                hourList != null ? hourList.size() : 0
        );

        int contentHeight = HEADER_HEIGHT * 2 + Math.max(1, maxListSize) * ROW_HEIGHT;
        int totalHeight = TITLE_HEIGHT + contentHeight + FOOTER_HEIGHT + 100; // 为看板娘预留空间

        // 创建图像合成器
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, totalHeight, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        // 设置背景色
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, IMAGE_WIDTH, totalHeight)
                .drawTooRoundRect();

        // 绘制标题
        int currentY = TITLE_HEIGHT;
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT)
                .addCenteredText(title, currentY);

        // 绘制表头
        currentY += 30;

        // 左右列的起始X坐标
        int leftColumnX = IMAGE_MARGIN;
        int rightColumnX = IMAGE_MARGIN + COLUMN_WIDTH + IMAGE_MARGIN;

        // 绘制"当天"和"每小时"标题
        combiner.setColor(CARD_BACKGROUND_COLOR)
                .fillRect(leftColumnX, currentY, TABLE_WIDTH, HEADER_HEIGHT)
                .fillRect(rightColumnX, currentY, TABLE_WIDTH, HEADER_HEIGHT);

        combiner
                .setColor(TEXT_COLOR)
                .addText("当天", TABLE_WIDTH / 2, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("每小时", rightColumnX + TABLE_WIDTH / 2, currentY + HEADER_HEIGHT / 2 + 8);

        currentY += HEADER_HEIGHT + 10;

        // 绘制详细表头
        combiner.setColor(CARD_BACKGROUND_COLOR)
                .fillRect(leftColumnX, currentY, TABLE_WIDTH, HEADER_HEIGHT)
                .fillRect(rightColumnX, currentY, TABLE_WIDTH, HEADER_HEIGHT);

        combiner.setColor(TEXT_COLOR)
                .setFont(HEADER_FONT)
                .addText("名称", leftColumnX + 10, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("杜卡币", leftColumnX + 250, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("?杜卡币/白金", leftColumnX + 350, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("均价", leftColumnX + 500, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("库存", leftColumnX + 600, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("名称", rightColumnX + 10, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("杜卡币", rightColumnX + 250, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("?杜卡币/白金", rightColumnX + 350, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("均价", rightColumnX + 500, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("库存", rightColumnX + 600, currentY + HEADER_HEIGHT / 2 + 8);

        currentY += HEADER_HEIGHT + 5;

        // 绘制数据行
        int maxRows = Math.max(
                dayList != null ? dayList.size() : 0,
                hourList != null ? hourList.size() : 0
        );

        for (int i = 0; i < maxRows; i++) {
            // 绘制当天数据
            if (dayList != null && i < dayList.size()) {
                drawDucatRow(combiner, dayList.get(i), leftColumnX, currentY, i);
            } else {
                // 绘制空白行
                if (i % 2 == 1) {
                    combiner.setColor(new Color(0xE8F4FD))
                            .fillRect(leftColumnX, currentY, TABLE_WIDTH, ROW_HEIGHT);
                } else {
                    combiner.setColor(Color.WHITE)
                            .fillRect(leftColumnX, currentY, TABLE_WIDTH, ROW_HEIGHT);
                }
            }

            // 绘制每小时数据
            if (hourList != null && i < hourList.size()) {
                drawDucatRow(combiner, hourList.get(i), rightColumnX, currentY, i);
            } else {
                // 绘制空白行
                if (i % 2 == 1) {
                    combiner.setColor(new Color(0xE8F4FD))
                            .fillRect(rightColumnX, currentY, TABLE_WIDTH, ROW_HEIGHT);
                } else {
                    combiner.setColor(Color.WHITE)
                            .fillRect(rightColumnX, currentY, TABLE_WIDTH, ROW_HEIGHT);
                }
            }

            currentY += ROW_HEIGHT;
        }
        // 添加底部署名
        addFooter(combiner, totalHeight - 40);

        // 合成并返回图像
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }

    /**
     * 绘制单行杜卡币数据
     *
     * @param combiner 图像合成器
     * @param ducat    杜卡币数据
     * @param startX   起始X坐标
     * @param y        Y坐标
     * @param index    行索引，用于交替背景色
     */
    private static void drawDucatRow(ImageCombiner combiner, Ducats.Ducat ducat, int startX, int y, int index) {
        // 交替行背景色
        if (index % 2 == 1) {
            combiner.setColor(new Color(0xE8F4FD))
                    .fillRect(startX, y, TABLE_WIDTH, ROW_HEIGHT);
        } else {
            combiner.setColor(Color.WHITE)
                    .fillRect(startX, y, TABLE_WIDTH, ROW_HEIGHT);
        }

        combiner.setColor(TEXT_COLOR)
                .setFont(ROW_FONT);

        // 截取物品名称，避免过长
        String itemName = ducat.getItem();
        if (itemName != null && itemName.length() > 15) {
            itemName = itemName.substring(0, 12) + "...";
        }

        combiner.addText(itemName != null ? itemName : "未知物品", startX + 10, y + ROW_HEIGHT / 2 + 8)
                .addText(ducat.getDucats() != null ? ducat.getDucats().toString() : "N/A", startX + 250, y + ROW_HEIGHT / 2 + 8)
                .addText(ducat.getDucatsPerPlatinumWa() != null ? String.format("%.2f", ducat.getDucatsPerPlatinumWa()) : "N/A", startX + 350, y + ROW_HEIGHT / 2 + 8)
                .addText(ducat.getWaPrice() != null ? String.format("%.2f", ducat.getWaPrice()) : "N/A", startX + 500, y + ROW_HEIGHT / 2 + 8)
                .addText(ducat.getVolume() != null ? ducat.getVolume().toString() : "N/A", startX + 600, y + ROW_HEIGHT / 2 + 8);
    }
}
