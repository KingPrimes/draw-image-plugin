package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 订阅指令表卡片渲染器
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawWarframeSubscribeImage {

    private static final int CANVAS_W = 1600;
    private static final int CONTENT_X = 50;
    private static final int CARD_PAD = 24;
    private static final int CARD_RADIUS = 14;
    private static final int SECTION_GAP = 20;

    private static final int TITLE_Y = 80;

    private static final Color BLUE_COLOR = SUBSCRIBE_IMAGE_BLUE_COLOR;
    private static final Color PURPLE_COLOR = SUBSCRIBE_IMAGE_PURPLE_COLOR;
    private static final Color RED_COLOR = SUBSCRIBE_IMAGE_RED_COLOR;
    private static final Color BROWN_COLOR = SUBSCRIBE_IMAGE_BROWN_COLOR;

    private DefaultDrawWarframeSubscribeImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawWarframeSubscribeImage(Map<Integer, String> subscribe,
                                                    Map<Integer, String> missionType) {
        box sz = scaleByPct(CANVAS_W, CANVAS_W, STANDING_RATIO);
        int contentW = CANVAS_W - CONTENT_X * 2;

        // 预计算各区域高度
        int currentY = CONTENT_X + 50; // 标题区域后开始

        Font bodyFont = FONT.deriveFont(Font.BOLD, 26f);
        Font tableFont = FONT.deriveFont(Font.BOLD, 24f);
        Font smallFont = FONT.deriveFont(22f);

        // 用法说明卡片高度
        int usageH = CARD_PAD + 40 + 4 * 36 + CARD_PAD;

        // 订阅类型表卡片高度
        int subscribeRows = subscribe != null ? (subscribe.size() + 5) / 6 : 0;
        int subscribeH = CARD_PAD + 42 + 8 + subscribeRows * 38 + CARD_PAD;

        // 任务类型表卡片高度
        int missionRows = missionType != null ? (missionType.size() + 5) / 6 : 0;
        int missionH = CARD_PAD + 42 + 8 + missionRows * 38 + CARD_PAD;

        // 构建各节卡片 Y
        int[] sectionHeights = {usageH, subscribeH, missionH};
        int[] sectionY = new int[sectionHeights.length];
        int cy = currentY;
        for (int i = 0; i < sectionHeights.length; i++) {
            sectionY[i] = cy;
            cy += sectionHeights[i] + SECTION_GAP;
        }
        int contentEnd = cy - SECTION_GAP;
        int standingY = contentEnd + 10;
        int totalHeight = Math.max(standingY + sz.y(), contentEnd + sz.y());

        ImageCombiner cb = new ImageCombiner(CANVAS_W, totalHeight, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, totalHeight);
        cb.drawTooRoundRect();

        // 标题
        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 44))
                .addCenteredText("订阅指令说明", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 50, CANVAS_W - CONTENT_X, TITLE_Y + 50);

        // ---- 用法说明卡片 ----
        drawUsageCard(cb, sectionY[0], contentW, usageH, bodyFont, smallFont);
        // ---- 订阅类型表 ----
        drawTableCard(cb, sectionY[1], contentW, subscribeH,
                "订阅内容类型数值", subscribe, PURPLE_COLOR, tableFont);
        // ---- 任务类型表 ----
        drawTableCard(cb, sectionY[2], contentW, missionH,
                "订阅任务类型数值", missionType, RED_COLOR, tableFont);

        cb.drawStandingAt(CANVAS_W - sz.x(), totalHeight - sz.y(), sz.x(), sz.y());
        addFooter(cb, totalHeight - 25);
        cb.combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }

    private static void drawUsageCard(ImageCombiner cb, int cardY,
                                      int cardW, int cardH, Font bodyFont, Font smallFont) {
        int innerX = DefaultDrawWarframeSubscribeImage.CONTENT_X + CARD_PAD;

        cb.setColor(CARD_BACKGROUND_COLOR)
                .fillRoundRect(DefaultDrawWarframeSubscribeImage.CONTENT_X, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(BLUE_COLOR).fillRect(DefaultDrawWarframeSubscribeImage.CONTENT_X + CARD_RADIUS, cardY + 2, cardW - 2 * CARD_RADIUS, 5);
        cb.setColor(DIVIDER_COLOR).setStroke(1)
                .drawRoundRect(DefaultDrawWarframeSubscribeImage.CONTENT_X, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);

        int cy = cardY + CARD_PAD + 30;

        // 使用方式
        cb.setColor(TEXT_COLOR).setFont(bodyFont);
        cb.addText("使用方式：", innerX, cy + 8);
        cb.setFont(smallFont);
        int x = innerX + cb.getFontMetrics(bodyFont).stringWidth("使用方式：");
        cb.setColor(BLUE_COLOR).addText("订阅 ", x, cy + 8);
        x += cb.getFontMetrics(smallFont).stringWidth("订阅 ");
        cb.setColor(PURPLE_COLOR).addText("[订阅类型]", x, cy + 8);
        x += cb.getFontMetrics(smallFont).stringWidth("[订阅类型]");
        cb.setColor(RED_COLOR).addText(" [-任务类型]", x, cy + 8);
        x += cb.getFontMetrics(smallFont).stringWidth(" [-任务类型]");
        cb.setColor(BROWN_COLOR).addText(" [-遗物等级]", x, cy + 8);
        cy += 36;

        // 示例
        cb.setColor(TEXT_COLOR).setFont(bodyFont);
        cb.addText("示例：", innerX, cy + 8);
        cb.setFont(smallFont);
        x = innerX + cb.getFontMetrics(bodyFont).stringWidth("示例：");
        cb.setColor(BLUE_COLOR).addText("订阅 ", x, cy + 8);
        x += cb.getFontMetrics(smallFont).stringWidth("订阅 ");
        cb.setColor(PURPLE_COLOR).addText("裂隙", x, cy + 8);
        x += cb.getFontMetrics(smallFont).stringWidth("裂隙");
        cb.setColor(RED_COLOR).addText(" 生存模式", x, cy + 8);
        x += cb.getFontMetrics(smallFont).stringWidth(" 生存模式");
        cb.setColor(BROWN_COLOR).addText(" 后纪", x, cy + 8);
        cy += 36;

        // 指令例子
        cb.setColor(TEXT_COLOR).setFont(bodyFont);
        cb.addText("指令例子：", innerX, cy + 8);
        cb.setFont(smallFont);
        x = innerX + cb.getFontMetrics(bodyFont).stringWidth("指令例子：");
        cb.setColor(BLUE_COLOR).addText("订阅 ", x, cy + 8);
        x += cb.getFontMetrics(smallFont).stringWidth("订阅 ");
        cb.setColor(PURPLE_COLOR).addText("9", x, cy + 8);
        x += cb.getFontMetrics(smallFont).stringWidth("9");
        cb.setColor(RED_COLOR).addText(" -11", x, cy + 8);
        x += cb.getFontMetrics(smallFont).stringWidth(" -11");
        cb.setColor(BROWN_COLOR).addText(" -4", x, cy + 8);
        cy += 36;

        // 注意事项
        cb.setColor(TEXT_COLOR).setFont(bodyFont);
        cb.addText("注意事项：", innerX, cy + 8);
        cb.setFont(smallFont);
        x = innerX + cb.getFontMetrics(bodyFont).stringWidth("注意事项：");
        cb.setColor(BROWN_COLOR).addText("遗物等级 ", x, cy + 8);
        x += cb.getFontMetrics(smallFont).stringWidth("遗物等级 ");
        cb.setColor(TEXT_COLOR).addText("仅在订阅 ", x, cy + 8);
        x += cb.getFontMetrics(smallFont).stringWidth("仅在订阅 ");
        cb.setColor(PURPLE_COLOR).addText("裂隙", x, cy + 8);
        x += cb.getFontMetrics(smallFont).stringWidth("裂隙");
        cb.setColor(TEXT_COLOR).addText(" 时有效", x, cy + 8);
    }

    private static void drawTableCard(ImageCombiner cb, int cardY,
                                      int cardW, int cardH, String title,
                                      Map<Integer, String> data, Color accent, Font font) {
        int innerX = DefaultDrawWarframeSubscribeImage.CONTENT_X + CARD_PAD;
        int rightX = DefaultDrawWarframeSubscribeImage.CONTENT_X + cardW - CARD_PAD;

        cb.setColor(CARD_BACKGROUND_COLOR)
                .fillRoundRect(DefaultDrawWarframeSubscribeImage.CONTENT_X, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(accent).fillRect(DefaultDrawWarframeSubscribeImage.CONTENT_X + CARD_RADIUS, cardY + 2, cardW - 2 * CARD_RADIUS, 5);
        cb.setColor(DIVIDER_COLOR).setStroke(1)
                .drawRoundRect(DefaultDrawWarframeSubscribeImage.CONTENT_X, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);

        int cy = cardY + CARD_PAD;
        cb.setColor(accent).setFont(font);
        cb.addText(title, innerX, cy + 28);
        cy += 42;

        cb.setColor(DIVIDER_COLOR).drawLine(innerX, cy + 4, rightX, cy + 4);
        cy += 12;

        if (data != null && !data.isEmpty()) {
            List<Map.Entry<Integer, String>> entries = new ArrayList<>(data.entrySet());
            entries.sort(Map.Entry.comparingByKey());

            int cols = 5;
            int colW = (cardW - CARD_PAD * 2) / cols;
            Font cellFont = font.deriveFont(20f);

            int rowY;
            for (int i = 0; i < entries.size(); i++) {
                int col = i % cols;
                int row = i / cols;
                int cx = innerX + col * colW;
                rowY = cy + row * 38;
                String text = entries.get(i).getKey() + " = " + entries.get(i).getValue();
                cb.setColor(TEXT_COLOR).setFont(cellFont);
                cb.addText(text, cx, rowY + 24);
            }
        }
    }
}
