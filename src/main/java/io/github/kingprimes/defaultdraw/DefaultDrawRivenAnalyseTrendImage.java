package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.RivenAnalyseTrendModel;

import java.awt.*;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 紫卡分析趋势卡片渲染器 — 两列卡片 + 右侧看板娘，卡片高度自适应属性数量
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawRivenAnalyseTrendImage {

    private static final int CANVAS_W = 1750;
    private static final int CONTENT_X = 50;
    private static final int COLS = 2;
    private static final int COL_GAP = 50;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 24;

    private static final int TITLE_Y = 80;
    private static final int CONTENT_START_Y = 150;

    private static final Color UP_COLOR = new Color(0x3B9A21);
    private static final Color DOWN_COLOR = new Color(0xAC1818);
    private static final Color LETHAL_COLOR = new Color(0xCC6600);

    private static String lethalLabel(String level) {
        if (level == null) return "";
        return switch (level) {
            case "fatal" -> "⚡致命";
            case "serious" -> "⚠严重";
            case "harmful" -> "△有害";
            case "acceptable" -> "可接受";
            case "beneficial" -> "✓有益";
            default -> level;
        };
    }

    private DefaultDrawRivenAnalyseTrendImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawRivenAnalyseTrendImage(List<RivenAnalyseTrendModel> models) {
        if (models == null || models.isEmpty()) return new byte[0];

        int n = models.size();
        boolean isOdd = n % COLS != 0;

        box sz = scaleByPct(CANVAS_W, CANVAS_W, STANDING_RATIO);
        int cardsContentW = CANVAS_W - CONTENT_X - sz.x() - 30;
        int cardW = (cardsContentW - COL_GAP * (COLS - 1)) / COLS;
        int textW = cardW - CARD_PAD * 2;

        // 预计算卡片高度
        int[] cardHeights = new int[n];
        for (int i = 0; i < n; i++) {
            cardHeights[i] = calcCardHeight(models.get(i));
        }

        // 行高度（同列卡片取最大）
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
            if (r < rows - 1) cardsH += COL_GAP;
        }

        int lastRowY = CONTENT_START_Y;
        for (int r = 0; r < rows - 1; r++) {
            lastRowY += rowHeights[r] + COL_GAP;
        }

        int standingX = CANVAS_W - sz.x();
        int standingY;
        if (isOdd) {
            standingY = lastRowY;
        } else {
            standingY = CONTENT_START_Y + cardsH + 10;
        }
        int canvasH = standingY + sz.y();

        int[] colX = new int[COLS];
        for (int c = 0; c < COLS; c++) {
            colX[c] = CONTENT_X + c * (cardW + COL_GAP);
        }

        ImageCombiner cb = new ImageCombiner(CANVAS_W, canvasH, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, canvasH);
        cb.drawTooRoundRect();

        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 44))
                .addCenteredText("紫卡分析趋势", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 50,
                CONTENT_X + cardsContentW + sz.x() + 30, TITLE_Y + 50);

        int currentY = CONTENT_START_Y;
        for (int i = 0; i < n; i++) {
            int row = i / COLS;
            int col = i % COLS;
            drawCard(cb, models.get(i), colX[col], currentY, cardW, cardHeights[i], textW);
            if (col == COLS - 1 || i == n - 1) {
                currentY += rowHeights[row] + COL_GAP;
            }
        }

        return getBytes(sz, standingX, standingY, canvasH, cb);
    }

    private static int calcCardHeight(RivenAnalyseTrendModel m) {
        int h = CARD_PAD;                    // 顶部内边距
        h += 34;                              // 武器名称
        h += 28;                              // 紫卡名称
        h += 16;                              // 间距 + 分隔线
        h += 30;                              // 倾向 + 数值
        h += 26;                              // 武器类型
        h += 16;                              // 间距 + 分隔线
        List<RivenAnalyseTrendModel.Attribute> attrs = m.getAttributes();
        if (attrs != null) h += attrs.size() * 52;  // 主行 32 + 副行 20
        h += CARD_PAD;
        return Math.max(h, 200);
    }

    private static void drawCard(ImageCombiner cb, RivenAnalyseTrendModel m,
                                 int cardX, int cardY, int cardW, int cardH, int textW) {
        int innerX = cardX + CARD_PAD;
        int rightX = innerX + textW;

        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(DIVIDER_COLOR).setStroke(1)
                .drawRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);

        int cy = cardY + CARD_PAD;
        Font nameFont = FONT.deriveFont(Font.BOLD, 24f);
        Font bodyFont = FONT.deriveFont(20f);

        // 武器名称
        String weapon = "武器: " + (m.getWeaponName() != null ? m.getWeaponName() : "未知");
        cb.setColor(TITLE_COLOR).setFont(nameFont);
        cb.addText(weapon, innerX, cy + 26);
        cy += 34;

        // 紫卡名称
        String riven = "紫卡: " + (m.getRivenName() != null ? m.getRivenName() : "未知");
        cb.setColor(TEXT_COLOR).setFont(bodyFont);
        cb.addText(riven, innerX, cy + 22);
        cy += 30;

        // 分隔线
        cb.setColor(DIVIDER_COLOR).drawLine(innerX, cy + 6, rightX, cy + 6);
        cy += 14;

        // 倾向 + 数值
        String dotStr = m.getDot() != null ? m.getDot() : "-";
        String numStr = m.getNum() != null ? String.format("%.2f", m.getNum()) : "-";
        cb.setColor(ACCENT_GOLD_COLOR).setFont(bodyFont);
        cb.addText("倾向 " + dotStr + "  " + numStr, innerX, cy + 20);
        cy += 30;

        // 武器类型
        String type = "类型 " + (m.getWeaponType() != null ? m.getWeaponType() : "未知");
        cb.setColor(TEXT_SECONDARY_COLOR).setFont(bodyFont);
        cb.addText(type, innerX, cy + 20);
        cy += 28;

        // 分隔线
        cb.setColor(DIVIDER_COLOR).drawLine(innerX, cy + 6, rightX, cy + 6);
        cy += 16;

        // 属性列表
        List<RivenAnalyseTrendModel.Attribute> attrs = m.getAttributes();
        if (attrs != null) {
            Font subFont = FONT.deriveFont(16f);
            for (RivenAnalyseTrendModel.Attribute attr : attrs) {
                String diff = attr.getAttrDiff() != null ? attr.getAttrDiff() : "";
                boolean isUp = !diff.contains("-");
                Color attrColor = isUp ? UP_COLOR : DOWN_COLOR;

                String name = attr.getAttributeName() != null ? attr.getAttributeName() : "?";
                String low = attr.getLowAttr() != null ? attr.getLowAttr() : "?";
                String high = attr.getHighAttr() != null ? attr.getHighAttr() : "?";
                String grade = attr.getGrade() != null && !"-".equals(attr.getGrade()) ? " [" + attr.getGrade() + "]" : "";
                String line = name + " (" + low + "%-" + high + "%)" + "    " + diff + grade;

                cb.setColor(attrColor).setFont(bodyFont);
                cb.addText(line, innerX, cy + 20);
                cy += 32;

                // 副行：比率 + 综合分析
                String ratio = attr.getRatio() != null && !"-".equals(attr.getRatio()) ? "比率 " + attr.getRatio() : "";
                String analysis = attr.getAnalysis() != null ? attr.getAnalysis() : "";
                String lethal = attr.getLethalLevel() != null ? " " + lethalLabel(attr.getLethalLevel()) : "";
                String subLine = (ratio + "  " + analysis + lethal).trim();
                if (!subLine.isEmpty()) {
                    cb.setColor(TEXT_SECONDARY_COLOR).setFont(subFont);
                    cb.addText(subLine, innerX + 8, cy + 18);
                    cy += 20;
                }
            }
        }
    }
}
