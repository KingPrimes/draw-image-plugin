package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.enums.ElementEnum;
import io.github.kingprimes.model.enums.FactionEnum;
import io.github.kingprimes.model.enums.IconEnum;
import io.github.kingprimes.model.market.MarketLichSister;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 市场 Lich/Sister 拍卖卡片渲染器 — 三列卡片网格 + 看板娘，列流式布局
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawMarketLichSisterImage {

    private static final int CONTENT_X = 50;
    private static final int COLS = 3;
    private static final int COL_GAP = 20;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 20;

    private static final int TITLE_Y = 80;
    private static final int CONTENT_START_Y = 160;

    private DefaultDrawMarketLichSisterImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawMarketLichesImage(MarketLichSister marketLichs) {
        if (marketLichs == null || marketLichs.getPayload() == null
                || marketLichs.getPayload().getAuctions() == null) {
            return new byte[0];
        }

        List<MarketLichSister.Auctions> auctions = marketLichs.getPayload().getAuctions();
        if (auctions.isEmpty()) return new byte[0];

        int n = auctions.size();
        boolean isOdd = n % COLS != 0;

        int cardW = 562;
        int textW = cardW - CARD_PAD * 2;
        int CANVAS_W = CONTENT_X + COLS * cardW + (COLS - 1) * COL_GAP + CONTENT_X;

        int[] colX = new int[COLS];
        for (int c = 0; c < COLS; c++) {
            colX[c] = CONTENT_X + c * (cardW + COL_GAP);
        }

        // 预计算卡片高度
        int[] cardHeights = new int[n];
        Arrays.fill(cardHeights, calcCardHeight());

        // 列流式 Y 终点
        int[] colEndY = new int[COLS];
        java.util.Arrays.fill(colEndY, CONTENT_START_Y);
        for (int i = 0; i < n; i++) {
            int col = i % COLS;
            colEndY[col] += cardHeights[i] + COL_GAP;
        }
        for (int c = 0; c < COLS; c++) {
            if (colEndY[c] > CONTENT_START_Y) colEndY[c] -= COL_GAP;
        }

        int standingX = colX[COLS - 1];
        int totalHeight;
        if (isOdd) {
            // 看板娘从右列底部开始，与左中列共享垂直空间
            int tallerEnd = Math.max(colEndY[0], colEndY[1]);
            totalHeight = Math.max(tallerEnd, colEndY[COLS - 1] + cardW);
        } else {
            int maxEnd = CONTENT_START_Y;
            for (int c = 0; c < COLS; c++) {
                maxEnd = Math.max(maxEnd, colEndY[c]);
            }
            totalHeight = maxEnd + 10 + cardW;
        }

        ImageCombiner cb = new ImageCombiner(CANVAS_W, totalHeight, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, totalHeight);
        cb.drawTooRoundRect();

        String title = marketLichs.getPayload().getItemName() + " 拍卖信息";
        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 44))
                .addCenteredText(title, TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 50,
                CANVAS_W - CONTENT_X, TITLE_Y + 50);

        // 列流式绘制
        int[] drawY = new int[COLS];
        java.util.Arrays.fill(drawY, CONTENT_START_Y);
        for (int i = 0; i < n; i++) {
            int col = i % COLS;
            drawCard(cb, auctions.get(i), colX[col], drawY[col], cardW, cardHeights[i], textW);
            drawY[col] += cardHeights[i] + COL_GAP;
        }

        int standingY = isOdd ? colEndY[COLS - 1] : totalHeight - cardW;
        cb.drawStandingAt(standingX, standingY, cardW, cardW);
        addFooter(cb, totalHeight - 25);
        cb.combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }

    private static int calcCardHeight() {
        int h = CARD_PAD;     // 顶部
        h += 42;               // 元素名称 + 图标
        h += 8;                // 分隔线
        h += 30;               // 伤害 / 幻纹
        h += 8;                // 分隔线
        h += 30;               // 起拍价 + 买断价
        h += 28;               // 卖家
        h += 28;               // 声望
        h += CARD_PAD;         // 底部
        return Math.max(h, 200);
    }

    private static void drawCard(ImageCombiner cb, MarketLichSister.Auctions auction,
                                 int cardX, int cardY, int cardW, int cardH, int textW) {
        int innerX = cardX + CARD_PAD;
        int rightX = innerX + textW;

        MarketLichSister.Item item = auction.getItem();
        ElementEnum element = item != null ? item.getElement() : null;
        Color accent = element != null ? element.getCOLOR() : TEXT_COLOR;

        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(accent).fillRect(cardX + CARD_RADIUS, cardY + 2, cardW - 2 * CARD_RADIUS, 5);
        cb.setColor(DIVIDER_COLOR).setStroke(1)
                .drawRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);

        Font headerFont = FONT.deriveFont(Font.BOLD, 26f);
        Font bodyFont = FONT.deriveFont(20f);

        int cy = cardY + CARD_PAD;

        // 元素图标 + 名称
        if (element != null) {
            cb.setColor(accent).setFont(FONT_WARFRAME_ICON.deriveFont(28f));
            cb.addText(element.getICON(), innerX, cy + 28);
            cb.setColor(accent).setFont(headerFont);
            cb.addText(element.getNAME(), innerX + 36, cy + 28);
        } else {
            cb.setColor(TEXT_COLOR).setFont(headerFont);
            cb.addText("未知元素", innerX, cy + 28);
        }
        cy += 42;

        // 分隔线
        cb.setColor(DIVIDER_COLOR).drawLine(innerX, cy + 4, rightX, cy + 4);
        cy += 12;

        // 伤害加成 + 幻纹标记
        String damage = item != null && item.getDamage() != null
                ? "伤害 +" + item.getDamage() + "%" : "伤害 未知";
        cb.setColor(TEXT_COLOR).setFont(bodyFont);
        cb.addText(damage, innerX, cy + 20);

        if (item != null && Boolean.TRUE.equals(item.getHavingEphemera())) {
            cb.setColor(new Color(0x9B59B6)).setFont(FONT.deriveFont(Font.BOLD, 18f));
            int dw = cb.getFontMetrics(bodyFont).stringWidth(damage);
            cb.addText("[幻纹]", innerX + dw + 12, cy + 20);
        }
        cy += 30;

        // 分隔线
        cb.setColor(DIVIDER_COLOR).drawLine(innerX, cy + 4, rightX, cy + 4);
        cy += 12;

        // 起拍价 + 买断价
        String starting = "起拍 " + (auction.getStartingPrice() != null
                ? auction.getStartingPrice().toString() : "-");
        cb.setColor(TEXT_SECONDARY_COLOR).setFont(bodyFont);
        cb.addText(starting, innerX, cy + 20);

        String buyout = auction.getBuyoutPrice() != null
                ? auction.getBuyoutPrice().toString() : "-";
        Font pf = FONT_WARFRAME_ICON.deriveFont(28f);
        cb.setColor(ACCENT_GOLD_COLOR).setFont(pf);
        String priceIcon = IconEnum.PLATINUM.getIcon();
        int piW = cb.getFontMetrics(pf).stringWidth(priceIcon);
        int buyW = cb.getFontMetrics(bodyFont).stringWidth(buyout);
        cb.addText(priceIcon, rightX - piW - buyW - 4, cy + 22);
        cb.setColor(ACCENT_GOLD_COLOR).setFont(bodyFont);
        cb.addText(buyout, rightX - buyW, cy + 22);
        cy += 30;

        // 卖家
        String seller = auction.getOwner() != null && auction.getOwner().getIngameName() != null
                ? auction.getOwner().getIngameName() : "未知";
        cb.setColor(TITLE_COLOR).setFont(FONT_WARFRAME_ICON.deriveFont(28f));
        cb.addText(FactionEnum.FC_TENNO.getIcon(), innerX, cy + 22);
        cb.setColor(TEXT_COLOR).setFont(bodyFont);
        cb.addText(seller, innerX + 30, cy + 20);
        cy += 28;

        // 声望
        String rep = auction.getOwner() != null && auction.getOwner().getReputation() != null
                ? "声望 " + auction.getOwner().getReputation() : "声望 -";
        cb.setColor(TEXT_SECONDARY_COLOR).setFont(FONT.deriveFont(18f));
        cb.addText(rep, innerX, cy + 20);
    }
}
