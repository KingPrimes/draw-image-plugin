package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 帮助中心卡片渲染器 — 三列卡片网格 + 看板娘，列流式布局
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawHelpImage {

    private static final int CONTENT_X = 50;
    private static final int COLS = 3;
    private static final int COL_GAP = 20;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 20;

    private static final int TITLE_Y = 80;
    private static final int CONTENT_START_Y = 150;

    private DefaultDrawHelpImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawHelpImage(Map<String, String> helpInfo) {
        if (helpInfo == null || helpInfo.isEmpty()) return new byte[0];

        List<Map.Entry<String, String>> entries = new ArrayList<>(helpInfo.entrySet());
        int n = entries.size();
        boolean isOdd = n % COLS != 0;

        int cardW = 562;
        int CANVAS_W = CONTENT_X + COLS * cardW + (COLS - 1) * COL_GAP + CONTENT_X;

        int[] colX = new int[COLS];
        for (int c = 0; c < COLS; c++) {
            colX[c] = CONTENT_X + c * (cardW + COL_GAP);
        }

        // 固定卡片高度
        int cardH = 100;
        int[] cardHeights = new int[n];
        Arrays.fill(cardHeights, cardH);

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
                .addCenteredText("帮助中心", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 50,
                CANVAS_W - CONTENT_X, TITLE_Y + 50);

        // 列流式绘制
        int[] drawY = new int[COLS];
        java.util.Arrays.fill(drawY, CONTENT_START_Y);
        for (int i = 0; i < n; i++) {
            int col = i % COLS;
            Map.Entry<String, String> e = entries.get(i);
            drawCard(cb, e.getKey(), e.getValue(), colX[col], drawY[col], cardW, cardH);
            drawY[col] += cardH + COL_GAP;
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

    private static void drawCard(ImageCombiner cb, String command, String desc,
                                 int cardX, int cardY, int cardW, int cardH) {
        int innerX = cardX + CARD_PAD;

        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(new Color(0x8669A7)).fillRect(cardX + CARD_RADIUS, cardY + 2, cardW - 2 * CARD_RADIUS, 4);
        cb.setColor(DIVIDER_COLOR).setStroke(1)
                .drawRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);

        int cy = cardY + CARD_PAD;

        // 指令名
        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 24f));
        cb.addText(command, innerX, cy + 28);
        cy += 34;

        // 描述
        String descText = desc != null ? desc : "";
        if (descText.length() > 26) descText = descText.substring(0, 24) + "..";
        cb.setColor(TEXT_SECONDARY_COLOR).setFont(FONT.deriveFont(18f));
        cb.addText(descText, innerX, cy + 20);
    }
}
