package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 帮助中心指令标签渲染器 — 多列标签 + 看板娘，列流式布局
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawHelpImage {

    private static final int CONTENT_X = 50;
    private static final int COLS = 4;
    private static final int COL_GAP = 20;
    private static final int CARD_RADIUS = 10;
    private static final int CARD_PAD = 16;

    private static final int TITLE_Y = 80;
    private static final int CONTENT_START_Y = 150;

    private static final Color DOT_COLOR = new Color(0x8669A7);

    private DefaultDrawHelpImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawHelpImage(List<String> commands) {
        if (commands == null || commands.isEmpty()) return new byte[0];

        int n = commands.size();
        boolean isOdd = n % COLS != 0;

        int cardW = 380;
        int CANVAS_W = CONTENT_X + COLS * cardW + (COLS - 1) * COL_GAP + CONTENT_X;

        int[] colX = new int[COLS];
        for (int c = 0; c < COLS; c++) colX[c] = CONTENT_X + c * (cardW + COL_GAP);

        int cardH = 60;
        int[] cardHeights = new int[n];
        for (int i = 0; i < n; i++) cardHeights[i] = cardH;

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
            int tallerEnd = Math.max(Math.max(colEndY[0], colEndY[1]), colEndY[2]);
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
                .addCenteredText("帮助中心", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 50, CANVAS_W - CONTENT_X, TITLE_Y + 50);

        int[] drawY = new int[COLS];
        java.util.Arrays.fill(drawY, CONTENT_START_Y);
        for (int i = 0; i < n; i++) {
            int col = i % COLS;
            drawTag(cb, commands.get(i), colX[col], drawY[col], cardW, cardH);
            drawY[col] += cardH + COL_GAP;
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

    private static void drawTag(ImageCombiner cb, String cmd,
                                int x, int y, int w, int h) {
        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(x, y, w, h, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(DIVIDER_COLOR).setStroke(1)
                .drawRoundRect(x, y, w, h, CARD_RADIUS, CARD_RADIUS);

        // 左侧紫色圆点
        cb.setColor(DOT_COLOR).fillOval(x + CARD_PAD, y + h / 2 - 5, 10, 10);

        // 指令文字
        cb.setColor(TEXT_COLOR).setFont(FONT.deriveFont(Font.BOLD, 22f));
        cb.addText(cmd, x + CARD_PAD + 20, y + h / 2 + 8);
    }
}
