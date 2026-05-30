package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.Arbitration;

import java.awt.*;
import java.io.ByteArrayOutputStream;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 仲裁卡片渲染器 — 双列数据布局
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawArbitrationImage {

    private static final int CANVAS_W = 800;
    private static final int MARGIN = 65;
    private static final int CONTENT_START = MARGIN + 55;
    private static final int LEFT_X = MARGIN + 10;
    private static final int RIGHT_X = CANVAS_W / 2 + 20;
    private static final int ROW_H = 55;

    private DefaultDrawArbitrationImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawArbitrationImage(Arbitration arbitration) {
        if (arbitration == null) return new byte[0];

        int canvasH = 450;
        Font bodyFont = FONT.deriveFont(20f);

        ImageCombiner cb = new ImageCombiner(CANVAS_W, canvasH, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, canvasH);
        cb.drawTooRoundRect();

        // 标题
        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 28));
        cb.addCenteredText("仲裁任务", MARGIN + 5);
        cb.setColor(DIVIDER_COLOR).drawLine(MARGIN, MARGIN + 35, CANVAS_W - MARGIN, MARGIN + 35);

        int y = CONTENT_START;

        // ---- 左列 ----
        cb.setColor(TEXT_COLOR).setFont(bodyFont);
        cb.addText("节点: " + (arbitration.getNode() != null ? arbitration.getNode() : "未知"), LEFT_X, y);
        cb.addText("敌人: " + arbitration.getEnemyName(), RIGHT_X, y);

        // ---- 第二行 ----
        y += ROW_H;
        cb.setColor(TEXT_COLOR).setFont(bodyFont);
        cb.addText("任务类型: " + (arbitration.getType() != null ? arbitration.getType() : "未知"), LEFT_X, y);
        cb.setColor(TEXT_SECONDARY_COLOR)
                .addText("开始时间: " + arbitration.getActivationFormat(), RIGHT_X, y);

        // ---- 第三行 ----
        y += ROW_H;
        cb.setColor(ACCENT_GOLD_COLOR).setFont(bodyFont);
        cb.addText("剩余时间: " + arbitration.getTimeLeft(), LEFT_X, y);

        String worthText = arbitration.isWorth() ? "值得参与" : "不值得参与";
        cb.setColor(arbitration.isWorth() ? WORTH_COLOR : NOT_WORTH_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 24));
        cb.addText(worthText, RIGHT_X, y);
        cb.drawStandingAt(CANVAS_W, canvasH, STANDING_RATIO);
        addFooter(cb, canvasH - 30);

        cb.combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }
}
