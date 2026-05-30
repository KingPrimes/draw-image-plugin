package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.Arbitration;

import java.awt.*;
import java.io.ByteArrayOutputStream;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 仲裁卡片渲染器 — 对应 Python card_arbitration.py
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawArbitrationImage {

    private static final int CANVAS_W = 800;
    private static final int MARGIN = 40;
    private static final int ROW_H = 55;
    private static final int TITLE_Y = MARGIN + 5;
    private static final int CONTENT_START = MARGIN + 45;

    private static final Color WORTH_COLOR = new Color(0x27ae60);
    private static final Color NOT_WORTH_COLOR = new Color(0xe74c3c);

    private DefaultDrawArbitrationImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawArbitrationImage(Arbitration arbitration) {
        if (arbitration == null) return new byte[0];

        int canvasH = 500;
        int contentX = MARGIN + 10;

        ImageCombiner cb = new ImageCombiner(CANVAS_W, canvasH, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, canvasH);
        cb.drawTooRoundRect();

        // 标题
        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 28));
        cb.addCenteredText("仲裁任务", TITLE_Y);
        // 分割线
        cb.setColor(DIVIDER_COLOR).drawLine(MARGIN, TITLE_Y + 30, CANVAS_W - MARGIN, TITLE_Y + 30);

        int y = CONTENT_START;
        Font bodyFont = FONT.deriveFont(20f);

        // 节点
        cb.setColor(TEXT_COLOR).setFont(bodyFont);
        cb.addText("节点: " + (arbitration.getNode() != null ? arbitration.getNode() : "未知节点"), contentX, y);

        // 敌人
        y += ROW_H;
        cb.setColor(TEXT_COLOR).setFont(bodyFont);
        cb.addText("敌人: " + arbitration.getEnemyName(), contentX, y);

        // 任务类型
        y += ROW_H;
        cb.setColor(TEXT_COLOR).setFont(bodyFont);
        cb.addText("任务类型: " + (arbitration.getType() != null ? arbitration.getType() : "未知"), contentX, y);

        // 开始时间
        y += ROW_H;
        cb.setColor(TEXT_SECONDARY_COLOR).setFont(bodyFont);
        cb.addText("开始时间: " + arbitration.getActivationFormat(), contentX, y);

        // 剩余时间
        y += ROW_H;
        cb.setColor(ACCENT_GOLD_COLOR).setFont(bodyFont);
        cb.addText("剩余时间: " + arbitration.getTimeLeft(), contentX, y);

        // 价值判断
        y += ROW_H + 5;
        String worthText = arbitration.isWorth() ? "值得参与" : "不值得参与";
        Color worthColor = arbitration.isWorth() ? WORTH_COLOR : NOT_WORTH_COLOR;
        cb.setColor(worthColor).setFont(FONT.deriveFont(Font.BOLD, 24));
        cb.addText(worthText, contentX, y);

        cb.drawStandingAt(CANVAS_W - 280, canvasH - 410, 240, 360);
        addFooter(cb, canvasH - 30);

        cb.combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }
}
