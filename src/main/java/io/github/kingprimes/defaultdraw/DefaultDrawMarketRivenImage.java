package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.enums.ElementEnum;
import io.github.kingprimes.model.enums.FactionEnum;
import io.github.kingprimes.model.enums.IconEnum;
import io.github.kingprimes.model.market.MarketRiven;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 市场紫卡卡片渲染器 — 三列卡片网格 + 看板娘，列流式布局
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawMarketRivenImage {

    private static final int CONTENT_X = 50;
    private static final int COLS = 3;
    private static final int COL_GAP = 20;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 20;

    private static final int TITLE_Y = 80;
    private static final int CONTENT_START_Y = 150;

    private static final Color RIVEN_COLOR = new Color(0x8669A7);
    private static final Color POSITIVE_COLOR = new Color(0x3B9A21);
    private static final Color NEGATIVE_COLOR = new Color(0xAC1818);

    private DefaultDrawMarketRivenImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawMarketRivenImage(MarketRiven marketRiven) {
        if (marketRiven == null || marketRiven.getPayload() == null
                || marketRiven.getPayload().getAuctions() == null) {
            return new byte[0];
        }

        List<MarketRiven.Auctions> auctions = marketRiven.getPayload().getAuctions();
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
        for (int i = 0; i < n; i++) {
            cardHeights[i] = calcCardHeight(auctions.get(i));
        }

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

        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 44))
                .addCenteredText("Warframe Market 紫卡市场", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 50,
                CANVAS_W - CONTENT_X, TITLE_Y + 50);

        String itemName = marketRiven.getItemName();

        // 列流式绘制
        int[] drawY = new int[COLS];
        java.util.Arrays.fill(drawY, CONTENT_START_Y);
        for (int i = 0; i < n; i++) {
            int col = i % COLS;
            drawCard(cb, auctions.get(i), itemName, colX[col], drawY[col], cardW, cardHeights[i], textW);
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

    private static int calcCardHeight(MarketRiven.Auctions auction) {
        int h = CARD_PAD;
        h += 42;    // 紫卡名称
        h += 8;     // 分隔线
        h += 32;    // 段位 + 循环
        h += 30;    // MOD等级（数字）
        h += 8;     // 分隔线
        MarketRiven.Item item = auction.getItem();
        if (item != null && item.getAttributes() != null) {
            h += item.getAttributes().size() * 32;
        }
        h += 8;     // 分隔线
        h += 42;    // 卖家 + 价格
        h += CARD_PAD;
        return Math.max(h, 210);
    }

    private static void drawCard(ImageCombiner cb, MarketRiven.Auctions auction,
                                 String itemName, int cardX, int cardY,
                                 int cardW, int cardH, int textW) {
        int innerX = cardX + CARD_PAD;
        int rightX = innerX + textW;

        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(DIVIDER_COLOR).setStroke(1)
                .drawRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);

        MarketRiven.Item item = auction.getItem();
        if (item == null) return;

        Font headerFont = FONT.deriveFont(Font.BOLD, 22f);
        Font bodyFont = FONT.deriveFont(18f);

        int cy = cardY + CARD_PAD;

        // 紫卡名称 + 极性图标
        String name = itemName != null ? itemName : (item.getName() != null ? item.getName() : "未知紫卡");
        cb.setColor(RIVEN_COLOR).setFont(headerFont);
        cb.addText(name, innerX, cy + 24);
        if (item.getPolarity() != null) {
            cb.setColor(RIVEN_COLOR).setFont(FONT_WARFRAME_ICON.deriveFont(28f));
            String pIcon = item.getPolarity().getIcon();
            int pW = cb.getFontMetrics(FONT_WARFRAME_ICON.deriveFont(28f)).stringWidth(pIcon);
            cb.addText(pIcon, rightX - pW, cy + 26);
        }
        cy += 42;

        // 分隔线
        cb.setColor(DIVIDER_COLOR).drawLine(innerX, cy + 4, rightX, cy + 4);
        cy += 12;

        // 段位 + 循环次数
        String mr = item.getMasteryLevel() != null ? "段位 " + item.getMasteryLevel() : "段位 -";
        String reRolls = item.getReRolls() != null ? item.getReRolls().toString() : "-";
        cb.setColor(TEXT_COLOR).setFont(bodyFont);
        cb.addText(mr, innerX, cy + 20);

        // MOD等级 — 纯数字
        String rank = item.getModRank() != null ? "等级 " + item.getModRank() : "等级 -";
        cb.setColor(ACCENT_GOLD_COLOR).setFont(bodyFont);
        cb.addText(rank, innerX + textW / 3, cy + 20);

        cb.setColor(TEXT_SECONDARY_COLOR).setFont(bodyFont);
        int rrW = cb.getFontMetrics(bodyFont).stringWidth("循环 " + reRolls);
        cb.addText("循环 " + reRolls, rightX - rrW, cy + 20);
        cy += 32;

        // 分隔线
        cb.setColor(DIVIDER_COLOR).drawLine(innerX, cy + 4, rightX, cy + 4);
        cy += 12;

        // 属性词条（带元素图标，横向居中）
        if (item.getAttributes() != null) {
            Font iconFont = FONT_WARFRAME_ICON.deriveFont(22f);
            for (MarketRiven.Attributes attr : item.getAttributes()) {
                boolean positive = attr.getPositive() != null && attr.getPositive();
                Color attrColor = positive ? POSITIVE_COLOR : NEGATIVE_COLOR;
                String prefix = positive ? "+" : "";
                String val = attr.getValue() != null ? String.valueOf(attr.getValue()) : "?";
                String attrName = attr.getUrlName() != null ? attr.getUrlName() : "?";

                ElementEnum elem = findElement(attrName);
                String icon = elem != null ? elem.getICON() : null;
                Color iconColor = elem != null ? elem.getCOLOR() : null;

                String line = prefix + val + "%  " + attrName;
                int iconW = icon != null ? cb.getFontMetrics(iconFont).stringWidth(icon) + 6 : 0;
                int lineW = cb.getFontMetrics(bodyFont).stringWidth(line);
                int totalW = iconW + lineW;
                int centerX = cardX + (cardW - totalW) / 2;

                if (icon != null) {
                    cb.setColor(iconColor).setFont(iconFont);
                    cb.addText(icon, centerX, cy + 22);
                }
                cb.setColor(attrColor).setFont(bodyFont);
                cb.addText(line, centerX + iconW, cy + 20);
                cy += 32;
            }
        }

        // 分隔线
        cb.setColor(DIVIDER_COLOR).drawLine(innerX, cy + 4, rightX, cy + 4);
        cy += 12;

        // 卖家 + 买断价
        String seller = auction.getOwner() != null && auction.getOwner().getIngameName() != null
                ? auction.getOwner().getIngameName() : "未知";
        String price = auction.getBuyoutPrice() != null
                ? auction.getBuyoutPrice().toString() : "-";

        cb.setColor(TITLE_COLOR)
                .setFont(FONT_WARFRAME_ICON.deriveFont(24f))
                .addText(FactionEnum.FC_TENNO.getIcon(), innerX, cy + 22);
        cb.setColor(TITLE_COLOR)
                .setFont(bodyFont)
                .addText(seller, innerX + 28, cy + 22);

        String priceText = price + " ";
        int priceW = cb.getFontMetrics(bodyFont).stringWidth(priceText);
        cb.setColor(ACCENT_GOLD_COLOR)
                .setFont(FONT_WARFRAME_ICON.deriveFont(24f))
                .addText(IconEnum.PLATINUM.getIcon(), rightX - priceW - 32, cy + 22);
        cb.setColor(ACCENT_GOLD_COLOR)
                .setFont(bodyFont)
                .addText(priceText, rightX - priceW, cy + 22);
    }

    private static ElementEnum findElement(String urlName) {
        if (urlName == null) return null;
        String lower = urlName.toLowerCase();
        for (ElementEnum e : ElementEnum.values()) {
            if (lower.contains(e.name().toLowerCase())
                    || lower.contains(e.getNAME())) {
                return e;
            }
        }
        return null;
    }
}
