package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.Arbitration;

import java.awt.*;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 仲裁列表卡片渲染器 — 两列卡片网格 + 双列数据布局
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawArbitrationsImage {

    private static final int CANVAS_W = 1300;
    private static final int CONTENT_X = 50;
    private static final int CONTENT_W = 1200;
    private static final int COLS = 2;
    private static final int COL_GAP = 20;
    private static final int CARD_W = (CONTENT_W - COL_GAP) / COLS;
    private static final int[] COL_X = {CONTENT_X, CONTENT_X + CARD_W + COL_GAP};
    private static final int CARD_H = 190;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 20;
    private static final int ROW_GAP = 20;
    private static final int CARD_ROW_H = 44;
    private static final int TITLE_Y = 80;
    private static final int CONTENT_START_Y = 160;

    private DefaultDrawArbitrationsImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawArbitrationsImage(List<Arbitration> arbitrations) {
        if (arbitrations == null || arbitrations.isEmpty()) return new byte[0];

        List<Arbitration> worthList = arbitrations.stream()
                .filter(Arbitration::isWorth).limit(5).toList();
        if (worthList.isEmpty()) return new byte[0];

        int n = worthList.size();
        int rows = (int) Math.ceil((double) n / COLS);
        int cardsH = rows * CARD_H + (rows - 1) * ROW_GAP;
        boolean isOdd = n % COLS != 0;
        int lastRowY = CONTENT_START_Y + (rows - 1) * (CARD_H + ROW_GAP);

        // 看板娘尺寸
        box sz = scaleByPct(CANVAS_W, CANVAS_W, STANDING_RATIO);

        int standingX = CANVAS_W - sz.x();
        int standingY;
        if (isOdd) {
            standingY = lastRowY;
        } else {
            standingY = CONTENT_START_Y + cardsH + 10;
        }

        int canvasH = standingY + sz.y();

        ImageCombiner cb = new ImageCombiner(CANVAS_W, canvasH, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, canvasH);
        cb.drawTooRoundRect();

        // 标题
        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 40));
        cb.addCenteredText("有价值的仲裁任务", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 55, CONTENT_X + CONTENT_W, TITLE_Y + 55);

        Font dataFont = FONT.deriveFont(18f);
        Font worthFont = FONT.deriveFont(Font.BOLD, 20);

        for (int i = 0; i < n; i++) {
            int row = i / COLS;
            int col = i % COLS;
            drawArbitrationCard(cb, worthList.get(i), COL_X[col],
                    CONTENT_START_Y + row * (CARD_H + ROW_GAP),
                    dataFont, worthFont);
        }

        return getBytes(sz, standingX, standingY, canvasH, cb);
    }


    private static void drawArbitrationCard(ImageCombiner cb, Arbitration a, int cardX, int cardY,
                                            Font dataFont, Font worthFont) {
        int innerX = cardX + CARD_PAD;
        int innerW = CARD_W - CARD_PAD * 2;
        int rightX = innerX + innerW / 2 + 10;

        // 卡片背景
        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(cardX, cardY, CARD_W, CARD_H, CARD_RADIUS, CARD_RADIUS);

        int row1Y = cardY + 40;
        int row2Y = row1Y + CARD_ROW_H;
        int row3Y = row2Y + CARD_ROW_H;

        // ---- 第一行：节点 | 敌人 ----
        String node = a.getNode() != null ? a.getNode() : "未知节点";
        cb.setColor(TEXT_COLOR).setFont(dataFont);
        cb.addText("节点: " + node, innerX, row1Y);

        String enemyName = a.getEnemyName();
        String icon = a.getEnemyIcon();
        int curX = rightX;
        if (icon != null && !icon.isEmpty()) {
            cb.setColor(a.getEnemyColor()).setFont(FONT_WARFRAME_ICON);
            cb.addText(icon, curX, row1Y);
            curX += cb.getFontMetrics(FONT_WARFRAME_ICON).stringWidth(icon) + 4;
        }
        cb.setColor(a.getEnemyColor()).setFont(dataFont);
        cb.addText("敌人: " + enemyName, curX, row1Y);

        // ---- 第二行：任务类型 | 开始时间 ----
        String type = a.getType() != null ? a.getType() : "未知";
        cb.setColor(TEXT_COLOR).setFont(dataFont);
        cb.addText("任务类型: " + type, innerX, row2Y);
        cb.setColor(TEXT_SECONDARY_COLOR).setFont(dataFont);
        cb.addText("开始: " + a.getActivationFormat(), rightX, row2Y);

        // ---- 第三行：剩余时间 | 值得参与 ----
        cb.setColor(ACCENT_GOLD_COLOR).setFont(dataFont);
        cb.addText("剩余: " + a.getTimeLeft(), innerX, row3Y);

        String worthText = a.isWorth() ? "值得参与" : "不值得参与";
        cb.setColor(a.isWorth() ? WORTH_COLOR : NOT_WORTH_COLOR).setFont(worthFont);
        cb.addText(worthText, rightX, row3Y);
    }
}
