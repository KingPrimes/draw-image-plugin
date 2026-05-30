package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.SteelPathOffering;

import java.awt.*;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 钢铁奖励卡片渲染器 — 对应 Python card_steel_path.py
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawSteelPathImage {

    private static final int CANVAS_W = 1200;
    private static final int CONTENT_X = 60;
    private static final int CONTENT_W = 1080;
    private static final int ROW_H = 55;
    private static final int TITLE_Y = 80;
    private static final int DIVIDER_Y = 115;
    private static final int CONTENT_START_Y = 155;
    private static final int FOOTER_OFFSET = 55;

    private DefaultDrawSteelPathImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawSteelPathImage class");
    }

    public static byte[] drawSteelPathImage(SteelPathOffering sp) {
        if (sp == null) return new byte[0];

        int rows = 0;
        if (sp.getCurrentReward() != null) rows++;
        if (sp.getNextReward() != null) rows++;
        if (sp.getRemaining() != null) rows++;

        int canvasH = Math.max(CONTENT_START_Y + rows * ROW_H + 240 + FOOTER_OFFSET + 50, 400);
        ImageCombiner cb = new ImageCombiner(CANVAS_W, canvasH, ImageCombiner.OutputFormat.PNG);

        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, canvasH);
        cb.drawTooRoundRect();

        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 44))
                .addCenteredText("钢铁奖励", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, DIVIDER_Y, CONTENT_X + CONTENT_W, DIVIDER_Y);

        int y = CONTENT_START_Y;
        Font rowFont = FONT.deriveFont(28f);
        if (sp.getCurrentReward() != null) {
            cb.setColor(TEXT_COLOR).setFont(rowFont);
            cb.addText("当前奖励: " + sp.getCurrentReward(), CONTENT_X, y + 18);
            y += ROW_H;
        }
        if (sp.getNextReward() != null) {
            cb.setColor(TEXT_COLOR).setFont(rowFont);
            cb.addText("下一个奖励: " + sp.getNextReward(), CONTENT_X, y + 18);
            y += ROW_H;
        }
        if (sp.getRemaining() != null) {
            cb.setColor(ACCENT_GOLD_COLOR).setFont(rowFont);
            cb.addText("剩余时间: " + sp.getRemaining(), CONTENT_X, y + 18);
        }

        cb.drawStandingAt(CANVAS_W, canvasH, STANDING_RATIO);
        addFooter(cb, canvasH - FOOTER_OFFSET);
        cb.combine();
        return cb.getCombinedImageOutStream().toByteArray();
    }
}
