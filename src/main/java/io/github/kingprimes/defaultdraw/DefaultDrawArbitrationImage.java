package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.Arbitration;

import java.awt.*;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 单条仲裁卡片渲染器 — 左侧卡片 + 右侧看板娘并排布局
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawArbitrationImage {

    private static final int CANVAS_W = 1450;
    private static final int CONTENT_X = 50;
    private static final int CARD_W = 850;
    private static final int CARD_H = 210;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 24;
    private static final int ROW_H = 52;

    private static final int TITLE_Y = 80;
    private static final int CARD_Y = 150;

    private DefaultDrawArbitrationImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawArbitrationImage(Arbitration arbitration) {
        if (arbitration == null) return new byte[0];

        box sz = scaleByPct(CANVAS_W, CANVAS_W, STANDING_RATIO);
        int standingX = CANVAS_W - sz.x();
        int standingY = CARD_Y;
        int canvasH = standingY + Math.max(CARD_H, sz.y());

        ImageCombiner cb = new ImageCombiner(CANVAS_W, canvasH, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, canvasH);
        cb.drawTooRoundRect();

        // 标题
        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 36));
        cb.addText("仲裁任务", CONTENT_X, TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 55, CONTENT_X + CARD_W, TITLE_Y + 55);

        // 卡片
        drawArbitrationCard(cb, arbitration);
        // 看板娘
        return getBytes(sz, standingX, standingY, canvasH, cb);
    }

    private static void drawArbitrationCard(ImageCombiner cb, Arbitration a) {
        Font dataFont = FONT.deriveFont(20f);
        Font worthFont = FONT.deriveFont(Font.BOLD, 24);

        int innerX = DefaultDrawArbitrationImage.CONTENT_X + CARD_PAD;
        int innerW = CARD_W - CARD_PAD * 2;
        int rightX = innerX + innerW / 2 + 10;

        // 卡片背景
        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(DefaultDrawArbitrationImage.CONTENT_X, DefaultDrawArbitrationImage.CARD_Y, CARD_W, CARD_H, CARD_RADIUS, CARD_RADIUS);

        int row1Y = DefaultDrawArbitrationImage.CARD_Y + 45;
        int row2Y = row1Y + ROW_H;
        int row3Y = row2Y + ROW_H;

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
