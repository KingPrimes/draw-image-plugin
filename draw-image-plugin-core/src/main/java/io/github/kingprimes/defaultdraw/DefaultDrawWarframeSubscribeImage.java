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
 * @version 1.1.0
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
                                                    Map<Integer, String> missionType, Map<Integer, String> invasionReward) {
        box sz = scaleByPct(CANVAS_W, CANVAS_W, STANDING_RATIO);
        int contentW = CANVAS_W - CONTENT_X * 2;

        // 预计算各区域高度
        int currentY = CONTENT_X + 50; // 标题区域后开始

        Font bodyFont = FONT.deriveFont(Font.BOLD, 26f);
        Font tableFont = FONT.deriveFont(Font.BOLD, 24f);
        Font smallFont = FONT.deriveFont(22f);

        // 用法说明卡片高度（5 行说明）
        int usageH = CARD_PAD + 40 + 5 * 36 + CARD_PAD;

        // 订阅类型表卡片高度
        int subscribeRows = subscribe != null ? (subscribe.size() + 4) / 5 : 0;
        int subscribeH = CARD_PAD + 42 + 8 + subscribeRows * 38 + CARD_PAD;

        // 任务类型表卡片高度
        int missionRows = missionType != null ? (missionType.size() + 4) / 5 : 0;
        int missionH = CARD_PAD + 42 + 8 + missionRows * 38 + CARD_PAD;

        // 入侵奖励表卡片高度
        int invasionRows = invasionReward != null ? (invasionReward.size() + 4) / 5 : 0;
        int invasionH = invasionRows > 0 ? CARD_PAD + 42 + 8 + invasionRows * 38 + CARD_PAD : 0;

        // 构建各节卡片 Y
        int[] sectionHeights = {usageH, subscribeH, missionH, invasionH};
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
                "订阅内容类型数值", subscribe, RED_COLOR, tableFont);
        // ---- 任务类型表 ----
        drawTableCard(cb, sectionY[2], contentW, missionH,
                "订阅任务类型数值", missionType, PURPLE_COLOR, tableFont);
        // ---- 入侵奖励表 ----
        if (invasionH > 0) {
            drawTableCard(cb, sectionY[3], contentW, invasionH,
                    "入侵奖励类型数值", invasionReward, BLUE_COLOR, tableFont);
        }

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
        cb.setColor(RED_COLOR).addText("订阅 ", x, cy + 8);
        x += cb.getFontMetrics(smallFont).stringWidth("订阅 ");
        cb.setColor(TEXT_COLOR).addText("<类型编号>[-<子参数>]", x+10, cy + 8);
        cy += 36;

        // 各类型参数说明
        String[][] typeParams = {
                {"裂隙 9", "类型编号-任务类型-遗物等级", "9-11-4"},
                {"入侵 6", "类型编号-奖励类型", "6-3"},
                {"仲裁 2", "类型编号-任务类型", "2-1"},
                {"其他", "仅类型编号", "1"},
        };

        for (String[] row : typeParams) {
            cb.setFont(smallFont);
            x = innerX;
            cb.setColor(RED_COLOR).addText(row[0], x, cy + 8);
            x += cb.getFontMetrics(smallFont).stringWidth(row[0] + "  ");
            cb.setColor(TEXT_COLOR).addText(row[1], x, cy + 8);
            x += cb.getFontMetrics(smallFont).stringWidth(row[1] + "  ");
            cb.setColor(BROWN_COLOR).addText("例: " + row[2], x, cy + 8);
            cy += 36;
        }
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

            for (int i = 0; i < entries.size(); i++) {
                int col = i % cols;
                int row = i / cols;
                int cx = innerX + col * colW;
                int rowY = cy + row * 38;
                String text = entries.get(i).getKey() + " = " + entries.get(i).getValue();
                cb.setColor(TEXT_COLOR).setFont(cellFont);
                cb.addText(text, cx, rowY + 24);
            }
        }
    }
}
