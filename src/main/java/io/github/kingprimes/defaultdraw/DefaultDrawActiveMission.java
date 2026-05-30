package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.ActiveMission;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 裂隙卡片渲染器 — 两列卡片网格布局
 * <p>对应 Python card_fissures.py：居中标题 + 每张卡片顶部 tier 颜色强调条</p>
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawActiveMission {

    private static final int CANVAS_W = 1200;
    private static final int CONTENT_X = 60;
    private static final int CONTENT_W = 1080;
    private static final int COLS = 2;
    private static final int COL_GAP = 20;
    private static final int CARD_W = (CONTENT_W - COL_GAP) / COLS;
    private static final int CARD_H = 160;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 20;
    private static final int ROW_GAP = 16;
    private static final int ACCENT_STRIP_H = 4;

    private static final int[] COL_X = {CONTENT_X, CONTENT_X + CARD_W + COL_GAP};
    private static final int TITLE_Y = 60;
    private static final int CONTENT_START_Y = 175;
    private static final int FOOTER_OFFSET = 50;

    private static final int STANDING_ODD_W = 310;
    private static final int STANDING_ODD_H = 360;
    private static final int STANDING_EVEN_W = 260;
    private static final int STANDING_EVEN_H = 390;

    private DefaultDrawActiveMission() {
        throw new AssertionError("Cannot instantiate DefaultDrawActiveMission class");
    }

    public static byte[] drawActiveMissionImage(List<ActiveMission> missions) {
        if (missions == null || missions.isEmpty()) return new byte[0];

        int n = missions.size();
        int rows = (int) Math.ceil((double) n / COLS);
        int cardsH = rows * CARD_H + (rows - 1) * ROW_GAP;
        boolean isOdd = n % COLS != 0;
        int lastRowY = CONTENT_START_Y + (rows - 1) * (CARD_H + ROW_GAP);

        int canvasH;
        if (isOdd) {
            canvasH = Math.max(CONTENT_START_Y + cardsH, lastRowY + STANDING_ODD_H + FOOTER_OFFSET + 10);
        } else {
            canvasH = CONTENT_START_Y + cardsH + STANDING_EVEN_H + FOOTER_OFFSET + 20;
        }

        ImageCombiner cb = new ImageCombiner(CANVAS_W, canvasH, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, canvasH);
        cb.drawTooRoundRect();

        ActiveMission first = missions.getFirst();
        String title;
        Color titleColor;
        if (Boolean.TRUE.equals(first.getVoidStorms())) {
            title = "虚空风暴";
            titleColor = TITLE_COLOR;
        } else if (Boolean.TRUE.equals(first.getHard())) {
            title = "钢铁裂隙";
            titleColor = ACCENT_COLOR;
        } else {
            title = "虚空裂隙";
            titleColor = TITLE_COLOR;
        }
        cb.setColor(titleColor).setFont(FONT.deriveFont(Font.BOLD, 56)).addCenteredText(title, TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 68, CONTENT_X + CONTENT_W, TITLE_Y + 68);

        Font tierFont = FONT.deriveFont(Font.BOLD, 30);
        Font bodyFont = FONT.deriveFont(26f);
        Font timeFont = FONT.deriveFont(24f);
        Font locationFont = FONT.deriveFont(22f);

        for (int i = 0; i < n; i++) {
            int row = i / COLS;
            int col = i % COLS;
            drawFissureCard(cb, missions.get(i), COL_X[col],
                    CONTENT_START_Y + row * (CARD_H + ROW_GAP),
                    tierFont, bodyFont, timeFont, locationFont);
        }

        if (isOdd) {
            cb.drawStandingAt(COL_X[1] + (CARD_W - STANDING_ODD_W) / 2, lastRowY, STANDING_ODD_W, STANDING_ODD_H);
        } else {
            cb.drawStandingAt(CONTENT_X + CONTENT_W - STANDING_EVEN_W - 20,
                    CONTENT_START_Y + cardsH + 10, STANDING_EVEN_W, STANDING_EVEN_H);
        }

        addFooter(cb, canvasH - FOOTER_OFFSET);
        cb.combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }

    private static void drawFissureCard(ImageCombiner cb, ActiveMission m, int cardX, int cardY,
                                         Font tierFont, Font bodyFont, Font timeFont, Font locationFont) {
        int innerX = cardX + CARD_PAD;
        int innerW = CARD_W - CARD_PAD * 2;

        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(cardX, cardY, CARD_W, CARD_H, CARD_RADIUS, CARD_RADIUS);

        // tier 强调条
        Color tierRgb = m.getModifierColor();
        if (tierRgb != null) {
            cb.setColor(tierRgb).fillRect(cardX + CARD_RADIUS, cardY + 2, CARD_W - 2 * CARD_RADIUS, ACCENT_STRIP_H);
        }

        // 行 1: tier 名称 + 剩余时间
        String tierText = m.getModifierName() + " " + getVoidEnName(m.getModifierName());
        cb.setColor(tierRgb != null ? lighten(tierRgb, 0.45f) : TEXT_SECONDARY_COLOR).setFont(tierFont);
        cb.addText(tierText, innerX, cardY + 32);

        String eta = m.getTimeLeft() != null ? m.getTimeLeft() : "未知";
        cb.setColor(getTimeColor(eta)).setFont(timeFont);
        int etaW = cb.getFontMetrics(timeFont).stringWidth(eta);
        cb.addText(eta, innerX + innerW - etaW, cardY + 33);

        // 行 2: 任务类型 + 派系（居中）
        String mt = m.getMissionTypeName();
        Color mtCol = m.getMissionTypeColor();
        String fn = m.getFactionName() != null ? m.getFactionName() : "";
        Color fnCol = m.getFactionColor();
        int row2Y = cardY + 72;
        java.awt.FontMetrics bfm = cb.getFontMetrics(bodyFont);
        int mtW = bfm.stringWidth(mt);
        int gap = fn.isEmpty() ? 0 : 8;
        int fnW = fn.isEmpty() ? 0 : bfm.stringWidth(fn);
        int totalW = mtW + gap + fnW;
        int curX = innerX + (innerW - totalW) / 2;
        cb.setColor(mtCol).setFont(bodyFont).addText(mt, curX, row2Y + 3);
        curX += mtW + gap;
        if (!fn.isEmpty()) {
            cb.setColor(fnCol).setFont(bodyFont).addText(fn, curX, row2Y + 3);
        }

        // 行 3: 节点
        cb.setColor(TEXT_COLOR).setFont(locationFont);
        cb.addText(m.getNode() != null ? m.getNode() : "未知节点", innerX, cardY + 118);
    }

    private static String getVoidEnName(String name) {
        return switch (name) {
            case "古纪" -> "Lith";
            case "前纪" -> "Meso";
            case "中纪" -> "Neo";
            case "后纪" -> "Axi";
            case "安魂" -> "Requiem";
            case "全能" -> "Omnia";
            default -> "";
        };
    }

    private static Color getTimeColor(String t) {
        if (t == null || t.isEmpty()) return ACTIVE_MISSION_TIME_LOW_COLOR;
        String lo = t.toLowerCase();
        if (lo.contains("h")) return ACTIVE_MISSION_TIME_HIGH_COLOR;
        if (lo.contains("m")) {
            try {
                return Integer.parseInt(lo.replaceAll("\\D", "")) < 10
                        ? ACTIVE_MISSION_TIME_LOW_COLOR : ACTIVE_MISSION_TIME_MEDIUM_COLOR;
            } catch (NumberFormatException ignored) {}
        }
        return ACTIVE_MISSION_TIME_LOW_COLOR;
    }

    private static Color lighten(Color c, float f) {
        if (c == null) return TEXT_SECONDARY_COLOR;
        return new Color(
                (int) (c.getRed() + (255 - c.getRed()) * f),
                (int) (c.getGreen() + (255 - c.getGreen()) * f),
                (int) (c.getBlue() + (255 - c.getBlue()) * f));
    }
}
