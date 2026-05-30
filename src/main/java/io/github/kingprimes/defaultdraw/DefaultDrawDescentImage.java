package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.Descent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 深层下降图像绘制实现
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawDescentImage {

    private static final int DESCENT_IMAGE_WIDTH = 1100;
    private static final int DESCENT_IMAGE_MIN_HEIGHT = 700;
    private static final int CHALLENGE_CARD_HEIGHT = 80;

    private DefaultDrawDescentImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawDescentImage class");
    }

    /**
     * 绘制深层下降图像
     *
     * @param descents 深层下降数据列表
     * @return 图像字节数组
     */
    public static byte[] drawDescentImage(List<Descent> descents) {
        if (descents == null || descents.isEmpty()) {
            return new byte[0];
        }

        int height = calculateImageHeight(descents);
        ImageCombiner combiner = new ImageCombiner(
                new BufferedImage(DESCENT_IMAGE_WIDTH, height, BufferedImage.TYPE_INT_ARGB),
                ImageCombiner.OutputFormat.PNG);

        // 背景 + 双层边框
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, DESCENT_IMAGE_WIDTH, height)
                .drawTooRoundRect();

        // 标题
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 40))
                .addCenteredText("深层下降", 80);

        int currentY = 130;

        for (int d = 0; d < descents.size(); d++) {
            Descent descent = descents.get(d);
            currentY = drawDescentCard(combiner, descent, d, currentY);
            currentY += 15;
        }

        addFooter(combiner, height - IMAGE_FOOTER_HEIGHT);
        combiner.drawStandingAt(DESCENT_IMAGE_WIDTH, height, STANDING_RATIO);
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }

    private static int drawDescentCard(ImageCombiner combiner, Descent descent, int index, int startY) {
        List<Descent.Challenge> challenges = descent.getChallenges();
        if (challenges == null || challenges.isEmpty()) return startY;

        int cardWidth = DESCENT_IMAGE_WIDTH - 2 * IMAGE_MARGIN;
        long extraRows = challenges.stream().filter(c ->
                (c.getSpecs() != null && !c.getSpecs().isEmpty()) ||
                (c.getAuras() != null && !c.getAuras().isEmpty())).count();
        int extraH = (int) (extraRows * 26);
        int cardHeight = 55 + challenges.size() * CHALLENGE_CARD_HEIGHT + 20 + extraH;

        // 卡片背景
        combiner.setColor(CARD_BACKGROUND_COLOR)
                .fillRoundRect(IMAGE_MARGIN, startY, cardWidth, cardHeight, 12, 12);

        // 边框
        combiner.setColor(DIVIDER_COLOR)
                .drawRoundRect(IMAGE_MARGIN, startY, cardWidth, cardHeight, 12, 12);

        // 编号 + Seed
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 26))
                .addText("下降 #" + (index + 1), IMAGE_MARGIN + 20, startY + 35);

        combiner.setColor(TEXT_SECONDARY_COLOR)
                .setFont(FONT.deriveFont(18f))
                .addText("RandSeed: " + descent.getRandSeed(),
                        DESCENT_IMAGE_WIDTH - IMAGE_MARGIN - 250, startY + 35);

        // 分割线
        int lineY = startY + 52;
        combiner.setColor(DIVIDER_COLOR)
                .drawLine(IMAGE_MARGIN + 20, lineY, DESCENT_IMAGE_WIDTH - IMAGE_MARGIN - 20, lineY);

        int challengeY = startY + 60;
        for (Descent.Challenge ch : challenges) {
            challengeY = drawChallengeRow(combiner, ch, challengeY);
        }

        return startY + cardHeight;
    }

    private static int drawChallengeRow(ImageCombiner combiner, Descent.Challenge ch, int y) {
        int indexNum = ch.getIndex() != null ? ch.getIndex() + 1 : 0;

        // 序号
        combiner.setColor(TEXT_SECONDARY_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 20))
                .addText(String.format("#%02d", indexNum), IMAGE_MARGIN + 35, y + 22);

        // 类型
        String type = resolveChallengeType(ch.getType());
        combiner.setColor(ACCENT_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 20))
                .addText(type, IMAGE_MARGIN + 100, y + 22);

        // 描述
        String desc = ch.getChallenge() != null ? ch.getChallenge() : "";
        if (!desc.isEmpty()) {
            // 截断过长文本
            if (desc.length() > 40) desc = desc.substring(0, 37) + "...";
            combiner.setColor(TEXT_COLOR)
                    .setFont(FONT.deriveFont(20f))
                    .addText(desc, IMAGE_MARGIN + 280, y + 22);
        }

        // Level
        if (ch.getLevel() != null) {
            combiner.setColor(ACCENT_GOLD_COLOR)
                    .setFont(FONT.deriveFont(Font.BOLD, 18))
                    .addText(ch.getLevel(), DESCENT_IMAGE_WIDTH - IMAGE_MARGIN - 120, y + 22);
        }

        // Specs 标签
        if (ch.getSpecs() != null && !ch.getSpecs().isEmpty()) {
            y += 24;
            int tagX = IMAGE_MARGIN + 280;
            for (String spec : ch.getSpecs()) {
                String tag = "[ " + resolveSpecTag(spec) + " ]";
                combiner.setFont(FONT.deriveFont(14f))
                        .setColor(ACCENT_GREEN_COLOR)
                        .addText(tag, tagX, y + 2);
                tagX += combiner.getFontMetrics(FONT.deriveFont(14f)).stringWidth(tag) + 10;
            }
            // Auras 标签
            if (ch.getAuras() != null && !ch.getAuras().isEmpty()) {
                for (String aura : ch.getAuras()) {
                    String tag = "[ " + resolveAuraTag(aura) + " ]";
                    combiner.setFont(FONT.deriveFont(14f))
                            .setColor(ACCENT_COLOR)
                            .addText(tag, tagX, y + 2);
                    tagX += combiner.getFontMetrics(FONT.deriveFont(14f)).stringWidth(tag) + 10;
                }
            }
        }

        return y + 26;
    }

    // ---- 名称映射 ----

    private static String resolveChallengeType(String type) {
        if (type == null) return "未知";
        return switch (type) {
            case "CT_KILL" -> "击杀";
            case "CT_SURVIVAL" -> "生存";
            case "CT_DEFENSE" -> "防御";
            case "CT_RESCUE" -> "救援";
            case "CT_MOBILE_DEFENSE" -> "移动防御";
            case "CT_CAPTURE" -> "捕获";
            case "CT_SPY" -> "间谍";
            case "CT_SABOTAGE" -> "破坏";
            case "CT_HIVE" -> "清巢";
            case "CT_EXCAVATION" -> "挖掘";
            case "CT_ARENA" -> "竞技场";
            default -> type.replace("CT_", "");
        };
    }

    private static String resolveSpecTag(String spec) {
        if (spec == null) return "";
        return switch (spec) {
            case "SPEC_ENEMY_ARMOR" -> "高护甲";
            case "SPEC_ENEMY_SHIELD" -> "高护盾";
            case "SPEC_ENEMY_HEALTH" -> "高生命";
            case "SPEC_ENHANCED_DAMAGE" -> "伤害增强";
            case "SPEC_ENERGY_DRAIN" -> "能量吸取";
            case "SPEC_ABILITY_RESISTANCE" -> "技能抗性";
            default -> spec.replace("SPEC_", "").replace("_", " ");
        };
    }

    private static String resolveAuraTag(String aura) {
        if (aura == null) return "";
        return switch (aura) {
            case "AURA_HEALTH" -> "生命光环";
            case "AURA_ARMOR" -> "护甲光环";
            case "AURA_SHIELD" -> "护盾光环";
            case "AURA_DAMAGE" -> "伤害光环";
            case "AURA_SPEED" -> "速度光环";
            case "AURA_REGENERATION" -> "再生光环";
            default -> aura.replace("AURA_", "").replace("_", " ");
        };
    }

    private static int calculateImageHeight(List<Descent> descents) {
        int height = 150;
        for (Descent d : descents) {
            int challengeCount = d.getChallenges() != null ? d.getChallenges().size() : 0;
            long extraRows = d.getChallenges() != null ?
                    d.getChallenges().stream().filter(c ->
                            (c.getSpecs() != null && !c.getSpecs().isEmpty()) ||
                            (c.getAuras() != null && !c.getAuras().isEmpty())).count() : 0;
            int cardHeight = 55 + challengeCount * CHALLENGE_CARD_HEIGHT + 20 + (int) (extraRows * 26);
            height += cardHeight + 15;
        }
        height += IMAGE_FOOTER_HEIGHT + 50;
        return Math.max(height, DESCENT_IMAGE_MIN_HEIGHT);
    }
}
