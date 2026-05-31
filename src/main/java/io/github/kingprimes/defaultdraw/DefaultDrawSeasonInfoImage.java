package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.SeasonInfo;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 电波赛季卡片渲染器 — 两列卡片 + 右下看板娘，卡片高度自适应内容
 *
 * @author KingPrimes
 * @version 1.0.8
 */
public final class DefaultDrawSeasonInfoImage {

    private static final int CANVAS_W = 1800;
    private static final int CONTENT_X = 50;
    private static final int COLS = 2;
    private static final int COL_GAP = 20;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 20;
    private static final int ROW_GAP = 20;
    private static final int ROW_H = 40;

    private static final int TITLE_Y = 80;
    private static final int CARD_MIN_H = 200;

    private static final Color DAILY_COLOR = new Color(0xFF9500);
    private static final Color WEEKLY_COLOR = TITLE_COLOR;
    private static final Color ELITE_COLOR = new Color(0x9B59B6);

    private DefaultDrawSeasonInfoImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawSeasonInfoImage(SeasonInfo seasonInfo) {
        if (seasonInfo == null) return new byte[0];

        List<SeasonInfo.ActiveChallenges> challenges = seasonInfo.getActiveChallenges();
        if (challenges == null || challenges.isEmpty()) return new byte[0];

        int n = challenges.size();

        box sz = scaleByPct(CANVAS_W, CANVAS_W, STANDING_RATIO);
        int cardsContentW = CANVAS_W - CONTENT_X * 2;
        int cardW = (cardsContentW - COL_GAP * (COLS - 1)) / COLS;
        int descMaxW = cardW - CARD_PAD * 2;
        Font descFont = FONT.deriveFont(22f);

        // 预计算每张卡片的高度（基于描述文本换行）
        int[] cardHeights = new int[n];
        for (int i = 0; i < n; i++) {
            cardHeights[i] = calcCardHeight(challenges.get(i), descMaxW, descFont);
        }

        // 按行取最大高度
        int rows = (int) Math.ceil((double) n / COLS);
        int[] rowHeights = new int[rows];
        int cardsH = 0;
        for (int r = 0; r < rows; r++) {
            int maxH = 0;
            for (int c = 0; c < COLS && r * COLS + c < n; c++) {
                maxH = Math.max(maxH, cardHeights[r * COLS + c]);
            }
            rowHeights[r] = maxH;
            cardsH += maxH;
            if (r < rows - 1) cardsH += ROW_GAP;
        }

        // 标题区域高度：标题 + 分隔线间距 + 赛季阶段信息
        int headerH = 120;
        int contentStartY = TITLE_Y + headerH;

        // 看板娘在所有卡片下方，贴画布右下角
        int standingX = CANVAS_W - sz.x();
        int standingY = contentStartY + cardsH + 10;
        int canvasH = standingY + sz.y();

        int[] colX = new int[COLS];
        for (int c = 0; c < COLS; c++) {
            colX[c] = CONTENT_X + c * (cardW + COL_GAP);
        }

        ImageCombiner cb = new ImageCombiner(CANVAS_W, canvasH, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, canvasH);
        cb.drawTooRoundRect();

        // 标题 — 全画布居中
        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 44))
                .addCenteredText("电波赛季信息", TITLE_Y);

        // 分隔线 — 横跨左右边距之间的全内容宽度
        int dividerEnd = CANVAS_W - CONTENT_X;
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 50, dividerEnd, TITLE_Y + 50);

        // 赛季阶段信息 — 标题下方
        int infoY = TITLE_Y + 85;
        cb.setColor(TEXT_SECONDARY_COLOR).setFont(FONT.deriveFont(20f));
        String infoText = "赛季 " + seasonInfo.getSeason() + "  |  阶段 " + seasonInfo.getPhase();
        cb.addText(infoText, CONTENT_X, infoY);

        // 卡片网格
        int currentY = contentStartY;
        for (int i = 0; i < n; i++) {
            int row = i / COLS;
            int col = i % COLS;
            int cardH = rowHeights[row];
            drawChallengeCard(cb, challenges.get(i), colX[col], currentY, cardW, cardH, descMaxW, descFont);
            if (col == COLS - 1 || i == n - 1) {
                currentY += cardH + ROW_GAP;
            }
        }

        return getBytes(sz, standingX, standingY, canvasH, cb);
    }

    private static int calcCardHeight(SeasonInfo.ActiveChallenges c, int descMaxW, Font descFont) {
        int h = CARD_PAD;
        h += ROW_H; // 类型标签
        h += ROW_H; // 名称
        String desc = c.getDescription();
        if (desc != null && !desc.isEmpty()) {
            int lines = calcTextLines(desc, descMaxW, descFont);
            h += lines * 30;
        } else {
            h += ROW_H;
        }
        h += ROW_H; // 声望
        h += CARD_PAD; // 底部内边距
        return Math.max(h, CARD_MIN_H);
    }

    private static int calcTextLines(String text, int maxWidth, Font font) {
        java.awt.FontMetrics fm = getFontMetrics(font);
        return wrapLines(text, maxWidth, fm).size();
    }

    private static List<String> wrapLines(String text, int maxWidth, java.awt.FontMetrics fm) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) return lines;
        StringBuilder line = new StringBuilder();
        for (char ch : text.toCharArray()) {
            String test = line.toString() + ch;
            if (fm.stringWidth(test) > maxWidth && !line.isEmpty()) {
                lines.add(line.toString());
                line = new StringBuilder(String.valueOf(ch));
            } else {
                line.append(ch);
            }
        }
        if (!line.isEmpty()) lines.add(line.toString());
        return lines;
    }

    private static java.awt.FontMetrics getFontMetrics(Font font) {
        BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tmp.createGraphics();
        g2.setFont(font);
        java.awt.FontMetrics fm = g2.getFontMetrics();
        g2.dispose();
        return fm;
    }

    private static void drawChallengeCard(ImageCombiner cb, SeasonInfo.ActiveChallenges c,
                                          int cardX, int cardY, int cardW, int cardH,
                                          int descMaxW, Font descFont) {
        int innerX = cardX + CARD_PAD;
        int innerW = cardW - CARD_PAD * 2;

        Color accent = getTypeColor(c);
        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(accent).fillRect(cardX + CARD_RADIUS, cardY + 2, cardW - 2 * CARD_RADIUS, 5);

        int cy = cardY + CARD_PAD;

        // 类型标签
        cb.setColor(accent).setFont(FONT.deriveFont(Font.BOLD, 22f));
        cb.addText(getTypeLabel(c), innerX, cy + 28);
        cy += ROW_H;

        // 任务名称
        String name = c.getName() != null ? c.getName() : "未知任务";
        cb.setColor(TEXT_COLOR).setFont(FONT.deriveFont(Font.BOLD, 24f));
        cb.addText(name, innerX, cy + 28);
        cy += ROW_H;

        // 描述（自动换行）
        String desc = c.getDescription();
        if (desc != null && !desc.isEmpty()) {
            cb.setColor(TEXT_SECONDARY_COLOR).setFont(descFont);
            java.awt.FontMetrics fm = cb.getFontMetrics(descFont);
            List<String> lines = wrapLines(desc, descMaxW, fm);
            for (String line : lines) {
                cb.addText(line, innerX, cy + 24);
                cy += 30;
            }
        }

        // 声望 — 卡片右下角
        String standing = c.getStanding() != null ? "声望: " + c.getStanding() : "声望: 0";
        cb.setColor(ACCENT_GOLD_COLOR).setFont(FONT.deriveFont(Font.BOLD, 22f));
        int standingW = cb.getFontMetrics(FONT.deriveFont(Font.BOLD, 22f)).stringWidth(standing);
        cb.addText(standing, innerX + innerW - standingW, cardY + cardH - CARD_PAD - 5);
    }

    private static String getTypeLabel(SeasonInfo.ActiveChallenges c) {
        if (Boolean.TRUE.equals(c.getElite())) return "精英挑战";
        if (Boolean.TRUE.equals(c.getWeekly())) return "每周挑战";
        if (Boolean.TRUE.equals(c.getDaily())) return "每日挑战";
        return "普通挑战";
    }

    private static Color getTypeColor(SeasonInfo.ActiveChallenges c) {
        if (Boolean.TRUE.equals(c.getElite())) return ELITE_COLOR;
        if (Boolean.TRUE.equals(c.getWeekly())) return WEEKLY_COLOR;
        if (Boolean.TRUE.equals(c.getDaily())) return DAILY_COLOR;
        return TEXT_COLOR;
    }
}
