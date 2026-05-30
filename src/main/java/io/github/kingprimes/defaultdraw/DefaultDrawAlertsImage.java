package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.enums.MissionTypeEnum;
import io.github.kingprimes.model.worldstate.Alert;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 警报卡片渲染器 — 两列卡片网格布局
 * <p>对应 Python card_alert.py：居中标题 + 双层装饰边框 + 每张警报为独立圆角卡片</p>
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawAlertsImage {

    private static final int CANVAS_W = 1200;
    private static final int CONTENT_X = 60;
    private static final int CONTENT_W = 1080;
    private static final int COLS = 2;
    private static final int COL_GAP = 20;
    private static final int CARD_W = (CONTENT_W - COL_GAP) / COLS;
    private static final int CARD_H = 150;
    private static final int CARD_PAD = 20;
    private static final int CARD_RADIUS = 14;
    private static final int ROW_GAP = 20;

    private static final int[] COL_X = {CONTENT_X, CONTENT_X + CARD_W + COL_GAP};

    private static final int TITLE_Y = 100;
    private static final int DIVIDER_Y = TITLE_Y + 40;
    private static final int CONTENT_START_Y = 170;
    private static final int FOOTER_OFFSET = 60;

    // 看板娘尺寸
    private static final int STANDING_ODD_W = 310;
    private static final int STANDING_ODD_H = 360;
    private static final int STANDING_EVEN_W = 260;
    private static final int STANDING_EVEN_H = 390;

    private DefaultDrawAlertsImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawAlertsImage class");
    }

    public static byte[] drawAlertsImage(List<Alert> alerts) {
        if (alerts == null || alerts.isEmpty()) return new byte[0];

        int n = alerts.size();
        int rows = (int) Math.ceil((double) n / COLS);
        int cardsH = rows * CARD_H + (rows - 1) * ROW_GAP;
        boolean isOdd = n % COLS != 0;
        int lastRowY = CONTENT_START_Y + (rows - 1) * (CARD_H + ROW_GAP);

        int canvasH;
        if (isOdd) {
            int contentEnd = CONTENT_START_Y + cardsH;
            canvasH = Math.max(contentEnd, lastRowY + STANDING_ODD_H + FOOTER_OFFSET + 10);
        } else {
            int contentEnd = CONTENT_START_Y + cardsH;
            canvasH = contentEnd + STANDING_EVEN_H + FOOTER_OFFSET + 20;
        }

        ImageCombiner cb = new ImageCombiner(CANVAS_W, canvasH, ImageCombiner.OutputFormat.PNG);

        // 背景 + 双层边框
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, canvasH);
        cb.drawTooRoundRect();

        // 标题
        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 56))
                .addCenteredText("警报", TITLE_Y);

        // 分割线
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, DIVIDER_Y, CONTENT_X + CONTENT_W, DIVIDER_Y);

        // 卡片网格
        for (int i = 0; i < n; i++) {
            int row = i / COLS;
            int col = i % COLS;
            int cardX = COL_X[col];
            int cardY = CONTENT_START_Y + row * (CARD_H + ROW_GAP);
            drawAlertCard(cb, alerts.get(i), cardX, cardY);
        }

        // 看板娘
        if (isOdd) {
            int standingX = COL_X[1] + (CARD_W - STANDING_ODD_W) / 2;
            cb.drawStandingAt(standingX, lastRowY, STANDING_ODD_W, STANDING_ODD_H);
        } else {
            int contentEnd = CONTENT_START_Y + cardsH;
            int standingX = CONTENT_X + CONTENT_W - STANDING_EVEN_W - 20;
            cb.drawStandingAt(standingX, contentEnd + 10, STANDING_EVEN_W, STANDING_EVEN_H);
        }

        addFooter(cb, canvasH - FOOTER_OFFSET);

        cb.combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }

    private static void drawAlertCard(ImageCombiner cb, Alert alert, int cardX, int cardY) {
        int innerX = cardX + CARD_PAD;
        int innerW = CARD_W - CARD_PAD * 2;
        var mi = alert.getMissionInfo();

        // 卡片背景
        cb.setColor(CARD_BACKGROUND_COLOR)
                .fillRoundRect(cardX, cardY, CARD_W, CARD_H, CARD_RADIUS, CARD_RADIUS);

        // 行 1: 地点（左） + 剩余时间（右）
        String location = (mi != null && mi.getLocation() != null) ? mi.getLocation() : "未知节点";
        cb.setColor(TEXT_COLOR).setFont(FONT.deriveFont(28f));
        cb.addText(location, innerX, cardY + 30);

        String eta = alert.getTimeLeft() != null ? alert.getTimeLeft() : "未知";
        cb.setColor(ACCENT_GOLD_COLOR).setFont(FONT.deriveFont(20f));
        java.awt.FontMetrics fm = cb.getFontMetrics(FONT.deriveFont(20f));
        int etaW = fm.stringWidth(eta);
        cb.addText(eta, innerX + innerW - etaW, cardY + 30);

        // 行 2: 任务类型（有色） + 派系（有色） + 奖励
        int badgeY = cardY + 74;
        int cursorX = innerX;

        Font badgeFont = FONT.deriveFont(22f);
        Font bodyFont = FONT.deriveFont(Font.BOLD, 24);
        java.awt.FontMetrics badgeFm = cb.getFontMetrics(badgeFont);
        java.awt.FontMetrics bodyFm = cb.getFontMetrics(bodyFont);

        if (mi != null && mi.getMissionType() != null) {
            Color mtColor = MissionTypeEnum.getColor(mi.getMissionType());
            String mtName = mi.getMissionType().getName();
            cb.setColor(mtColor).setFont(badgeFont);
            cb.addText(mtName, cursorX, badgeY + 3);
            cursorX += badgeFm.stringWidth(mtName) + 10;
        }

        if (mi != null && mi.getFaction() != null) {
            Color fColor = mi.getFaction().getColor();
            String fName = mi.getFaction().getName();
            cb.setColor(fColor).setFont(badgeFont);
            cb.addText(fName, cursorX, badgeY + 3);
            cursorX += badgeFm.stringWidth(fName) + 14;
        }

        // 奖励文字
        String rewardText = buildRewardText(alert);
        if (!rewardText.isEmpty()) {
            cb.setColor(ACCENT_GOLD_COLOR).setFont(bodyFont);
            int maxRewardW = innerX + innerW - cursorX - 6;
            int rewardW = bodyFm.stringWidth(rewardText);
            if (rewardW > maxRewardW) {
                while (rewardW > maxRewardW && rewardText.length() > 3) {
                    rewardText = rewardText.substring(0, rewardText.length() - 1);
                    rewardW = bodyFm.stringWidth(rewardText + "..");
                }
                rewardText += "..";
            }
            cb.addText(rewardText, cursorX, badgeY + 1);
        }
    }

    private static String buildRewardText(Alert alert) {
        var mi = alert.getMissionInfo();
        if (mi == null) return "";
        var reward = mi.getMissionReward();
        if (reward == null) return "";

        StringBuilder sb = new StringBuilder();
        if (reward.getCredits() != null && reward.getCredits() > 0) {
            sb.append(reward.getCredits()).append("星币");
        }
        if (reward.getItems() != null && !reward.getItems().isEmpty()) {
            if (!sb.isEmpty()) sb.append("  ");
            var itemTexts = reward.getItems().stream().limit(5)
                    .filter(it -> it != null && !it.isEmpty())
                    .toList();
            sb.append(String.join("  ", itemTexts));
        }
        return sb.toString();
    }
}
