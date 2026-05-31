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
    private static final int CARD_H = 200;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 24;
    private static final int ROW_GAP = 20;
    private static final int ACCENT_STRIP_H = 6;

    private static final int TITLE_Y = 80;
    private static final int CONTENT_START_Y = 150;

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
        box sz = scaleByPct(CANVAS_W, CANVAS_W, STANDING_RATIO);
        int canvasH = CONTENT_START_Y + cardsH + sz.y();

        ImageCombiner cb = new ImageCombiner(CANVAS_W, canvasH, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, CANVAS_W, canvasH);
        cb.drawTooRoundRect();

        cb.setColor(ACCENT_COLOR).setFont(FONT.deriveFont(Font.BOLD, 48))
                .addCenteredText("入侵任务", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 55, CONTENT_X + CONTENT_W, TITLE_Y + 55);

        Font nodeFont = FONT.deriveFont(Font.BOLD, 32);
        Font factionFont = FONT.deriveFont(26f);
        Font rewardFont = FONT.deriveFont(22f);
        Font progressFont = FONT.deriveFont(24f);

        for (int i = 0; i < sorted.size(); i++) {
            drawInvasionCard(cb, sorted.get(i),
                    CONTENT_START_Y + i * (CARD_H + ROW_GAP),
                    nodeFont, factionFont, rewardFont, progressFont);
        }
        addFooter(cb, canvasH - 25);

        cb.drawStandingAt(CANVAS_W - sz.x(), canvasH - sz.y(), sz.x(), sz.y()).combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }

    private static void drawInvasionCard(ImageCombiner cb, Invasion inv, int cardY,
                                         Font nodeFont, Font factionFont, Font rewardFont, Font progressFont) {
        int innerX = DefaultDrawInvasionImage.CONTENT_X + CARD_PAD;
        int innerW = CARD_W - CARD_PAD * 2;

        double progress = inv.getGoal() != null && inv.getGoal() != 0 && inv.getCount() != null
                ? Math.min(Math.abs(inv.getCount()) / inv.getGoal(), 1.0) : 0;

        // 卡片背景
        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(DefaultDrawInvasionImage.CONTENT_X, cardY, CARD_W, CARD_H, CARD_RADIUS, CARD_RADIUS);

        // 顶部双色进度条
        int splitX = DefaultDrawInvasionImage.CONTENT_X + (int) (CARD_W * progress);
        if (splitX - DefaultDrawInvasionImage.CONTENT_X > CARD_RADIUS) {
            cb.setColor(ATK_COLOR)
                    .fillRect(DefaultDrawInvasionImage.CONTENT_X + CARD_RADIUS, cardY + 2, splitX - DefaultDrawInvasionImage.CONTENT_X - CARD_RADIUS, ACCENT_STRIP_H);
        }
        if (DefaultDrawInvasionImage.CONTENT_X + CARD_W - splitX > CARD_RADIUS) {
            cb.setColor(DEF_COLOR)
                    .fillRect(splitX, cardY + 2, DefaultDrawInvasionImage.CONTENT_X + CARD_W - splitX - CARD_RADIUS, ACCENT_STRIP_H);
        }

        // 行 1: 节点（左） + 进度（右）
        cb.setColor(TEXT_COLOR).setFont(nodeFont);
        cb.addText(inv.getNode() != null ? inv.getNode() : "未知节点", innerX, cardY + 40);
        String pct = String.format("进度: %.1f%%", progress * 100);
        cb.setColor(TITLE_COLOR).setFont(progressFont);
        int pctW = cb.getFontMetrics(progressFont).stringWidth(pct);
        cb.addText(pct, innerX + innerW - pctW, cardY + 42);

        // 行 2: 阵营对抗（居中）
        int row2Y = cardY + 88;
        String atkName = inv.getFaction() != null ? inv.getFaction().getName() : "未知";
        Color atkColor = inv.getFaction() != null ? inv.getFaction().getColor() : TEXT_MUTED_COLOR;
        String atkIcon = inv.getFaction() != null ? inv.getFaction().getIcon() : "";
        String defName = inv.getDefenderFaction() != null ? inv.getDefenderFaction().getName() : "未知";
        Color defColor = inv.getDefenderFaction() != null ? inv.getDefenderFaction().getColor() : TEXT_MUTED_COLOR;
        String defIcon = inv.getDefenderFaction() != null ? inv.getDefenderFaction().getIcon() : "";
        String sep = "  VS  ";

        java.awt.FontMetrics ffm = cb.getFontMetrics(factionFont);
        Font iconFont = FONT_WARFRAME_ICON;
        java.awt.FontMetrics ifm = cb.getFontMetrics(iconFont);
        int atkIconW = (atkIcon != null && !atkIcon.isEmpty()) ? ifm.stringWidth(atkIcon) + 4 : 0;
        int defIconW = (defIcon != null && !defIcon.isEmpty()) ? ifm.stringWidth(defIcon) + 4 : 0;
        int atkW = ffm.stringWidth(atkName);
        int sepW = ffm.stringWidth(sep);
        int defW = ffm.stringWidth(defName);
        int totalW = atkIconW + atkW + sepW + defIconW + defW;
        int curX = innerX + (innerW - totalW) / 2;

        if (atkIcon != null && !atkIcon.isEmpty()) {
            cb.setColor(atkColor).setFont(iconFont).addText(atkIcon, curX, row2Y);
            curX += atkIconW;
        }
        cb.setColor(atkColor).setFont(factionFont).addText(atkName, curX, row2Y + 3);
        curX += atkW;
        cb.setColor(TEXT_MUTED_COLOR).setFont(factionFont).addText(sep, curX, row2Y + 3);
        curX += sepW;
        if (defIcon != null && !defIcon.isEmpty()) {
            cb.setColor(defColor).setFont(iconFont).addText(defIcon, curX, row2Y);
            curX += defIconW;
        }
        cb.setColor(defColor).setFont(factionFont).addText(defName, curX, row2Y + 3);

        // 行 3: 奖励
        int rewardY = cardY + 140;
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
