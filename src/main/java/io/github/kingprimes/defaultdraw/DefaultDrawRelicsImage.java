package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.Relics;
import io.github.kingprimes.model.enums.RarityEnum;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 遗物卡片渲染器 — 三列卡片网格 + 看板娘，列流式布局
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawRelicsImage {

    private static final int CONTENT_X = 50;
    private static final int COLS = 3;
    private static final int COL_GAP = 20;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 20;

    private static final int TITLE_Y = 80;
    private static final int CONTENT_START_Y = 150;

    private DefaultDrawRelicsImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawRelicsImage(List<Relics> relics) {
        if (relics == null || relics.isEmpty()) return new byte[0];

        int n = relics.size();
        boolean isOdd = n % COLS != 0;
        int cardW = 562;
        int textW = cardW - CARD_PAD * 2;
        int CANVAS_W = CONTENT_X + COLS * cardW + (COLS - 1) * COL_GAP + CONTENT_X;

        int[] colX = new int[COLS];
        for (int c = 0; c < COLS; c++) colX[c] = CONTENT_X + c * (cardW + COL_GAP);

        // 预计算高度
        int[] cardHeights = new int[n];
        for (int i = 0; i < n; i++) cardHeights[i] = calcCardHeight(relics.get(i));

        // 列流式 Y
        int[] colEndY = new int[COLS];
        java.util.Arrays.fill(colEndY, CONTENT_START_Y);
        for (int i = 0; i < n; i++) {
            int col = i % COLS;
            colEndY[col] += cardHeights[i] + COL_GAP;
        }
        for (int c = 0; c < COLS; c++) {
            if (colEndY[c] > CONTENT_START_Y) colEndY[c] -= COL_GAP;
        }

        int totalHeight;
        if (isOdd) {
            int tallerEnd = Math.max(colEndY[0], colEndY[1]);
            totalHeight = Math.max(tallerEnd, colEndY[COLS - 1] + cardW);
        } else {
            int maxEnd = CONTENT_START_Y;
            for (int c = 0; c < COLS; c++) maxEnd = Math.max(maxEnd, colEndY[c]);
            totalHeight = maxEnd + 10 + cardW;
        }

        ImageCombiner cb = new ImageCombiner(CANVAS_W, totalHeight, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, totalHeight);
        cb.drawTooRoundRect();

        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 44))
                .addCenteredText("遗物查询结果", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 50, CANVAS_W - CONTENT_X, TITLE_Y + 50);

        int[] drawY = new int[COLS];
        java.util.Arrays.fill(drawY, CONTENT_START_Y);
        for (int i = 0; i < n; i++) {
            int col = i % COLS;
            drawCard(cb, relics.get(i), colX[col], drawY[col], cardW, cardHeights[i], textW);
            drawY[col] += cardHeights[i] + COL_GAP;
        }

        int standingX = colX[COLS - 1];
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

    private static int calcCardHeight(Relics relic) {
        int h = CARD_PAD + 42 + 10; // 顶部 + 标题
        if (relic.getRewards() != null) h += relic.getRewards().size() * 36;
        h += CARD_PAD;
        return Math.max(h, 140);
    }

    private static void drawCard(ImageCombiner cb, Relics relic,
                                 int cardX, int cardY, int cardW, int cardH, int textW) {
        int innerX = cardX + CARD_PAD;
        int rightX = innerX + textW;

        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(DIVIDER_COLOR).setStroke(1).drawRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);

        int cy = cardY + CARD_PAD;

        // 遗物名称
        String name = relic.getName() != null ? relic.getName() : "未知遗物";
        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 22f));
        cb.addText(name, innerX, cy + 24);
        cy += 36;

        // 分隔线
        cb.setColor(DIVIDER_COLOR).drawLine(innerX, cy + 4, rightX, cy + 4);
        cy += 14;

        // 奖励列表（稀有度颜色）
        if (relic.getRewards() != null) {
            Font rf = FONT.deriveFont(18f);
            for (Relics.Rewards reward : relic.getRewards()) {
                Color rc = getRarityColor(reward.getRarity());
                String rname = reward.getName() != null ? reward.getName() : "未知";
                if (rname.length() > 26) rname = rname.substring(0, 24) + "..";
                cb.setColor(rc).setFont(rf);
                cb.addText("● " + rname, innerX + 8, cy + 22);
                cy += 36;
            }
        }
    }

    private static Color getRarityColor(RarityEnum rarity) {
        if (rarity == null) return TEXT_COLOR;
        return switch (rarity) {
            case COMMON -> VOID_T2_COLOR;
            case UNCOMMON -> VOID_T3_COLOR;
            case RARE -> VOID_T4_COLOR;
            default -> TEXT_COLOR;
        };
    }
}
