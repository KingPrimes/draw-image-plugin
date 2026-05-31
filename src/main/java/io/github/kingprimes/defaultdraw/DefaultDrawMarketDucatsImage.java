package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.Ducats;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 杜卡币卡片渲染器 — 列流式布局，当天/每小时双列 + 看板娘
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawMarketDucatsImage {

    private static final int CONTENT_X = 50;
    private static final int COLS = 4;
    private static final int COL_GAP = 20;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 20;

    private static final int TITLE_Y = 80;
    private static final int CONTENT_START_Y = 150;

    private DefaultDrawMarketDucatsImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawMarketDucatsImage(Map<Ducats.DumpType, List<Ducats.Ducat>> dump, String title) {
        if (dump == null || dump.isEmpty()) return new byte[0];

        List<Ducats.Ducat> dayList = dump.get(Ducats.DumpType.DAY);
        List<Ducats.Ducat> hourList = dump.get(Ducats.DumpType.HOUR);
        int maxRows = Math.max(dayList != null ? dayList.size() : 0,
                hourList != null ? hourList.size() : 0);
        if (maxRows == 0) return new byte[0];

        // 4列数据 + 右列看板娘
        int cardW = 400;
        int textW = cardW - CARD_PAD * 2;
        int CANVAS_W = CONTENT_X + COLS * cardW + (COLS - 1) * COL_GAP + COL_GAP + cardW + CONTENT_X;

        int[] colX = new int[COLS];
        for (int c = 0; c < COLS; c++) colX[c] = CONTENT_X + c * (cardW + COL_GAP);

        int cardH = 180;

        int contentEnd = getContentEnd(dayList, hourList, cardH);
        int totalHeight = Math.max(contentEnd, cardW) + 60;

        ImageCombiner cb = new ImageCombiner(CANVAS_W, totalHeight, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, totalHeight);
        cb.drawTooRoundRect();

        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 44))
                .addCenteredText(title, TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 50, CANVAS_W - CONTENT_X, TITLE_Y + 50);

        // 当天：col0, col1 交替
        int[] dayY = {CONTENT_START_Y, CONTENT_START_Y};
        if (dayList != null) {
            for (int i = 0; i < dayList.size(); i++) {
                int c = i % 2;
                drawCard(cb, "当天", dayList.get(i), colX[c], dayY[c], cardW, cardH, textW);
                dayY[c] += cardH + COL_GAP;
            }
        }

        // 每小时：col2, col3 交替
        int[] hourY = {CONTENT_START_Y, CONTENT_START_Y};
        if (hourList != null) {
            for (int i = 0; i < hourList.size(); i++) {
                int c = i % 2;
                drawCard(cb, "每小时", hourList.get(i), colX[2 + c], hourY[c], cardW, cardH, textW);
                hourY[c] += cardH + COL_GAP;
            }
        }

        // 看板娘 — 右侧独立列，底部对齐
        int standingColX = CANVAS_W - CONTENT_X - cardW;
        cb.drawStandingAt(standingColX, totalHeight - cardW, cardW, cardW);
        addFooter(cb, totalHeight - 25);
        cb.combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }

    private static int getContentEnd(List<Ducats.Ducat> dayList, List<Ducats.Ducat> hourList, int cardH) {
        int daySize = dayList != null ? dayList.size() : 0;
        int hourSize = hourList != null ? hourList.size() : 0;
        int dayRows = (daySize + 1) / 2;
        int hourRows = (hourSize + 1) / 2;
        int dayEndY = CONTENT_START_Y + dayRows * (cardH + COL_GAP) - COL_GAP;
        int hourEndY = CONTENT_START_Y + hourRows * (cardH + COL_GAP) - COL_GAP;
        if (daySize == 0) dayEndY = CONTENT_START_Y;
        if (hourSize == 0) hourEndY = CONTENT_START_Y;

        return Math.max(dayEndY, hourEndY);
    }

    private static void drawCard(ImageCombiner cb, String label, Ducats.Ducat d,
                                 int cardX, int cardY, int cardW, int cardH, int textW) {
        int innerX = cardX + CARD_PAD;
        int rightX = innerX + textW;

        Color labelBg = "当天".equals(label) ? new Color(0x6C5CE7) : new Color(0xE67E22);
        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(labelBg).fillRect(cardX + CARD_RADIUS, cardY + 2, cardW - 2 * CARD_RADIUS, 5);
        cb.setColor(DIVIDER_COLOR).setStroke(1)
                .drawRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);

        Font bodyFont = FONT.deriveFont(20f);
        Font smallFont = FONT.deriveFont(18f);

        int cy = cardY + CARD_PAD;

        // 标签 + 物品名
        cb.setColor(labelBg).setFont(FONT.deriveFont(Font.BOLD, 18f));
        cb.addText("[" + label + "]", innerX, cy + 22);
        cb.setColor(TEXT_COLOR).setFont(FONT.deriveFont(Font.BOLD, 22f));
        String item = d.getItem() != null ? d.getItem() : "未知";
        if (item.length() > 22) item = item.substring(0, 20) + "..";
        cb.addText(item, innerX + 70, cy + 22);
        cy += 42;

        // 杜卡币 + 杜卡币/白金
        String ducats = d.getDucats() != null ? "杜卡币 " + d.getDucats() : "杜卡币 -";
        cb.setColor(ACCENT_GOLD_COLOR).setFont(bodyFont);
        cb.addText(ducats, innerX, cy + 20);

        String ratio = d.getDucatsPerPlatinumWa() != null
                ? String.format("比率 %.1f/P", d.getDucatsPerPlatinumWa()) : "比率 -";
        cb.setColor(TEXT_SECONDARY_COLOR).setFont(smallFont);
        cb.addText(ratio, innerX + textW / 2, cy + 20);
        cy += 36;

        // 均价 + 库存
        String avgPrice = d.getWaPrice() != null ? String.format("均价 %.1f", d.getWaPrice()) : "均价 -";
        cb.setColor(TEXT_COLOR).setFont(smallFont);
        cb.addText(avgPrice, innerX, cy + 20);

        String volume = d.getVolume() != null ? "库存 " + d.getVolume() : "库存 -";
        cb.setColor(TEXT_SECONDARY_COLOR).setFont(smallFont);
        int vW = cb.getFontMetrics(smallFont).stringWidth(volume);
        cb.addText(volume, rightX - vW, cy + 20);
    }
}
