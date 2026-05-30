package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.Invasion;
import io.github.kingprimes.model.worldstate.Reward;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 入侵卡片渲染器 — 单列宽卡片布局
 * <p>对应 Python card_invasions.py：进攻/防守双色进度条 + 阵营对抗 + 奖励</p>
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawInvasionImage {

    private static final int CANVAS_W = 1300;
    private static final int CONTENT_X = 60;
    private static final int CONTENT_W = 1180;
    private static final int CARD_W = CONTENT_W;
    private static final int CARD_H = 180;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 24;
    private static final int ROW_GAP = 16;
    private static final int ACCENT_STRIP_H = 6;

    private static final int TITLE_Y = 60;
    private static final int CONTENT_START_Y = 175;
    private static final int FOOTER_OFFSET = 50;

    private static final int STANDING_W = 260;
    private static final int STANDING_H = 390;

    private static final Color ATK_COLOR = DrawConstants.ATTACKER_COLOR;
    private static final Color DEF_COLOR = DrawConstants.DEFENDER_COLOR;

    private DefaultDrawInvasionImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawInvasionImage class");
    }

    public static byte[] drawInvasionImage(List<Invasion> invasions) {
        if (invasions == null || invasions.isEmpty()) return new byte[0];

        List<Invasion> sorted = invasions.stream()
                .sorted(Comparator.comparing(Invasion::getGoal, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
        int n = sorted.size();
        int cardsH = n * CARD_H + (n - 1) * ROW_GAP;
        int contentEnd = CONTENT_START_Y + cardsH;
        int canvasH = contentEnd + STANDING_H + FOOTER_OFFSET + 20;

        ImageCombiner cb = new ImageCombiner(CANVAS_W, canvasH, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, canvasH);
        cb.drawTooRoundRect();

        cb.setColor(ACCENT_COLOR).setFont(FONT.deriveFont(Font.BOLD, 60))
                .addCenteredText("入侵任务", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 68, CONTENT_X + CONTENT_W, TITLE_Y + 68);

        Font nodeFont = FONT.deriveFont(Font.BOLD, 36);
        Font factionFont = FONT.deriveFont(30f);
        Font rewardFont = FONT.deriveFont(26f);
        Font progressFont = FONT.deriveFont(28f);

        for (int i = 0; i < sorted.size(); i++) {
            drawInvasionCard(cb, sorted.get(i), CONTENT_X,
                    CONTENT_START_Y + i * (CARD_H + ROW_GAP),
                    nodeFont, factionFont, rewardFont, progressFont);
        }

        cb.drawStandingAt(CONTENT_X + CONTENT_W - STANDING_W - 20,
                canvasH - STANDING_H - 10, STANDING_W, STANDING_H);
        addFooter(cb, canvasH - FOOTER_OFFSET);

        cb.combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }

    private static void drawInvasionCard(ImageCombiner cb, Invasion inv, int cardX, int cardY,
                                          Font nodeFont, Font factionFont, Font rewardFont, Font progressFont) {
        int innerX = cardX + CARD_PAD;
        int innerW = CARD_W - CARD_PAD * 2;

        double progress = inv.getGoal() != null && inv.getGoal() != 0 && inv.getCount() != null
                ? Math.min(Math.abs(inv.getCount()) / (double) inv.getGoal(), 1.0) : 0;

        // 卡片背景
        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(cardX, cardY, CARD_W, CARD_H, CARD_RADIUS, CARD_RADIUS);

        // 顶部双色进度条
        int splitX = cardX + (int) (CARD_W * progress);
        if (splitX - cardX > CARD_RADIUS) {
            cb.setColor(ATK_COLOR)
                    .fillRect(cardX + CARD_RADIUS, cardY + 2, splitX - cardX - CARD_RADIUS, ACCENT_STRIP_H);
        }
        if (cardX + CARD_W - splitX > CARD_RADIUS) {
            cb.setColor(DEF_COLOR)
                    .fillRect(splitX, cardY + 2, cardX + CARD_W - splitX - CARD_RADIUS, ACCENT_STRIP_H);
        }

        // 行 1: 节点（左） + 进度（右）
        cb.setColor(TEXT_COLOR).setFont(nodeFont);
        cb.addText(inv.getNode() != null ? inv.getNode() : "未知节点", innerX, cardY + 34);
        String pct = String.format("进度: %.1f%%", progress * 100);
        cb.setColor(TITLE_COLOR).setFont(progressFont);
        int pctW = cb.getFontMetrics(progressFont).stringWidth(pct);
        cb.addText(pct, innerX + innerW - pctW, cardY + 36);

        // 行 2: 阵营对抗（居中）
        int row2Y = cardY + 82;
        String atkName = inv.getFaction() != null ? inv.getFaction().getName() : "未知";
        Color atkColor = inv.getFaction() != null ? inv.getFaction().getColor() : TEXT_MUTED_COLOR;
        String defName = inv.getDefenderFaction() != null ? inv.getDefenderFaction().getName() : "未知";
        Color defColor = inv.getDefenderFaction() != null ? inv.getDefenderFaction().getColor() : TEXT_MUTED_COLOR;
        String sep = "  VS  ";

        java.awt.FontMetrics ffm = cb.getFontMetrics(factionFont);
        int atkW = ffm.stringWidth(atkName);
        int sepW = ffm.stringWidth(sep);
        int defW = ffm.stringWidth(defName);
        int totalW = atkW + sepW + defW;
        int curX = innerX + (innerW - totalW) / 2;

        cb.setColor(atkColor).setFont(factionFont).addText(atkName, curX, row2Y + 3);
        curX += atkW;
        cb.setColor(TEXT_MUTED_COLOR).setFont(factionFont).addText(sep, curX, row2Y + 3);
        curX += sepW;
        cb.setColor(defColor).setFont(factionFont).addText(defName, curX, row2Y + 3);

        // 行 3: 奖励
        int rewardY = cardY + 134;
        String atkReward = getFirstRewardText(inv.getAttackerReward());
        if (!atkReward.isEmpty()) {
            cb.setColor(ATK_COLOR).setFont(rewardFont);
            cb.addText("进攻: " + atkReward, innerX, rewardY + 3);
        }
        String defReward = "";
        if (inv.getDefenderReward() != null && inv.getDefenderReward().getCountedItems() != null
                && !inv.getDefenderReward().getCountedItems().isEmpty()) {
            var item = inv.getDefenderReward().getCountedItems().getFirst();
            defReward = (item.getCount() != null ? item.getCount() : "?") + "x "
                    + (item.getName() != null ? item.getName() : "?");
        }
        if (!defReward.isEmpty()) {
            cb.setColor(DEF_COLOR).setFont(rewardFont);
            int labelW = cb.getFontMetrics(rewardFont).stringWidth("防守: " + defReward);
            cb.addText("防守: " + defReward, innerX + innerW - labelW, rewardY + 3);
        }
    }

    private static String getFirstRewardText(List<Reward> rewards) {
        if (rewards == null) return "";
        for (Reward rw : rewards) {
            if (rw.getCountedItems() != null && !rw.getCountedItems().isEmpty()) {
                var item = rw.getCountedItems().getFirst();
                return (item.getCount() != null ? item.getCount() : "?") + "x "
                        + (item.getName() != null ? item.getName() : "?");
            }
        }
        return "";
    }
}
