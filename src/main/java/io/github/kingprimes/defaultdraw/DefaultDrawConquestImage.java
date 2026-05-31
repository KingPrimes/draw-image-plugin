package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.Conquest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 深层征服图像绘制实现
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawConquestImage {

    private static final int CONQUEST_IMAGE_WIDTH = 1200;
    private static final int CONQUEST_IMAGE_MIN_HEIGHT = 600;
    private static final int MISSION_ROW_HEIGHT = 42;
    private static final int RISK_TAG_HEIGHT = 30;

    private DefaultDrawConquestImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawConquestImage class");
    }

    /**
     * 绘制深层征服图像
     *
     * @param conquests 深层征服数据列表
     * @return 图像字节数组
     */
    public static byte[] drawConquestImage(List<Conquest> conquests) {
        if (conquests == null || conquests.isEmpty()) {
            return new byte[0];
        }

        int height = calculateImageHeight(conquests);
        ImageCombiner combiner = new ImageCombiner(
                new BufferedImage(CONQUEST_IMAGE_WIDTH, height, BufferedImage.TYPE_INT_ARGB),
                ImageCombiner.OutputFormat.PNG);

        // 背景 + 双层边框
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, CONQUEST_IMAGE_WIDTH, height)
                .drawTooRoundRect();

        // 标题
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 40))
                .addCenteredText("深层征服", 80);

        int currentY = 130;

        // 遍历每个征服
        for (Conquest conquest : conquests) {
            currentY = drawConquestCard(combiner, conquest, currentY);
            currentY += 20; // 间隔
        }

        addFooter(combiner, height - IMAGE_FOOTER_HEIGHT);
        combiner.drawStandingAt(CONQUEST_IMAGE_WIDTH, height, STANDING_RATIO);
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }

    private static int drawConquestCard(ImageCombiner combiner, Conquest conquest, int startY) {
        List<Conquest.Mission> missions = conquest.getMissions();
        if (missions == null || missions.isEmpty()) return startY;

        int cardWidth = CONQUEST_IMAGE_WIDTH - 2 * IMAGE_MARGIN;
        int totalMissionRows = missions.stream()
                .mapToInt(m -> m.getDifficulties() != null ? m.getDifficulties().size() : 1)
                .sum();
        int cardHeight = 90 + totalMissionRows * MISSION_ROW_HEIGHT + 30;

        // 卡片背景
        combiner.setColor(CARD_BACKGROUND_COLOR)
                .fillRoundRect(IMAGE_MARGIN, startY, cardWidth, cardHeight, 12, 12);

        // 边框
        combiner.setColor(DIVIDER_COLOR)
                .drawRoundRect(IMAGE_MARGIN, startY, cardWidth, cardHeight, 12, 12);

        // 类型标签
        String typeLabel = conquest.getType() != null ? conquest.getType() : "未知类型";
        String typeName = resolveConquestType(typeLabel);
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 28))
                .addText(typeName, IMAGE_MARGIN + 20, startY + 35);

        // RandomSeed
        if (conquest.getRandomSeed() != null) {
            combiner.setColor(TEXT_SECONDARY_COLOR)
                    .setFont(FONT.deriveFont(18f))
                    .addText("Seed: " + conquest.getRandomSeed(),
                            CONQUEST_IMAGE_WIDTH - IMAGE_MARGIN - 200, startY + 35);
        }

        // 分割线
        int lineY = startY + 55;
        combiner.setColor(DIVIDER_COLOR)
                .drawLine(IMAGE_MARGIN + 20, lineY, CONQUEST_IMAGE_WIDTH - IMAGE_MARGIN - 20, lineY);

        // Variables
        if (conquest.getVariables() != null && !conquest.getVariables().isEmpty()) {
            combiner.setColor(TEXT_SECONDARY_COLOR)
                    .setFont(FONT.deriveFont(18f))
                    .addText("变量: " + String.join(", ", conquest.getVariables()),
                            IMAGE_MARGIN + 20, startY + 75);
        }

        int missionY = startY + 85;
        for (Conquest.Mission mission : missions) {
            missionY = drawMissionBlock(combiner, mission, missionY);
        }

        return startY + cardHeight;
    }

    private static int drawMissionBlock(ImageCombiner combiner, Conquest.Mission mission, int y) {
        // 任务类型 + 阵营
        String missionType = resolveMissionType(mission.getMissionType());
        String faction = resolveFaction(mission.getFaction());
        combiner.setColor(ACCENT_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 20))
                .addText(missionType + " — " + faction, IMAGE_MARGIN + 40, y + 18);

        y += 5;

        // 难度选项
        if (mission.getDifficulties() != null) {
            for (Conquest.Difficulty diff : mission.getDifficulties()) {
                String diffText = resolveDifficulty(diff.getType());
                combiner.setColor(TEXT_SECONDARY_COLOR)
                        .setFont(FONT.deriveFont(18f))
                        .addText("  " + diffText, IMAGE_MARGIN + 55, y + RISK_TAG_HEIGHT);

                // 变异
                if (diff.getDeviation() != null && !diff.getDeviation().isEmpty()) {
                    combiner.setColor(TEXT_MUTED_COLOR)
                            .setFont(FONT.deriveFont(16f))
                            .addText("偏差: " + diff.getDeviation(), IMAGE_MARGIN + 260, y + RISK_TAG_HEIGHT);
                }

                // 风险标签
                if (diff.getRisks() != null && !diff.getRisks().isEmpty()) {
                    int tagX = IMAGE_MARGIN + 500;
                    for (String risk : diff.getRisks()) {
                        String tag = resolveRiskTag(risk);
                        combiner.setFont(FONT.deriveFont(Font.BOLD, 16))
                                .setColor(ACCENT_GREEN_COLOR)
                                .addText(tag, tagX, y + RISK_TAG_HEIGHT);
                        tagX += combiner.getFontMetrics(FONT.deriveFont(Font.BOLD, 16)).stringWidth(tag) + 15;
                    }
                }

                y += MISSION_ROW_HEIGHT;
            }
        }
        return y;
    }

    // ---- 名称映射 ----

    private static String resolveConquestType(String type) {
        return switch (type) {
            case "CT_LAB" -> "深层实验室";
            case "CT_LAB_HARD" -> "深层实验室（困难）";
            case "CT_OPEN" -> "开放征服";
            default -> type;
        };
    }

    private static String resolveMissionType(String mt) {
        if (mt == null) return "未知";
        return switch (mt) {
            case "MT_SURVIVAL" -> "生存";
            case "MT_EXTERMINATION" -> "歼灭";
            case "MT_DEFENSE" -> "防御";
            case "MT_MOBILE_DEFENSE" -> "移动防御";
            case "MT_RESCUE" -> "救援";
            case "MT_CAPTURE" -> "捕获";
            case "MT_SPY" -> "间谍";
            case "MT_SABOTAGE" -> "破坏";
            case "MT_ASSASSINATION" -> "刺杀";
            case "MT_TERRITORY" -> "拦截";
            case "MT_HIVE" -> "清巢";
            case "MT_EXCAVATE" -> "挖掘";
            case "MT_EVACUATION" -> "撤离";
            case "MT_ARENA" -> "竞技场";
            case "MT_ASSAULT" -> "强袭";
            default -> mt.replace("MT_", "");
        };
    }

    private static String resolveFaction(String faction) {
        if (faction == null) return "未知";
        return switch (faction) {
            case "FC_GRINEER" -> "Grineer";
            case "FC_CORPUS" -> "Corpus";
            case "FC_INFESTATION" -> "Infested";
            case "FC_CORRUPTED" -> "Orokin";
            case "FC_SENTIENT" -> "Sentient";
            case "FC_NARMER" -> "Narmer";
            case "FC_MURMUR" -> "Murmur";
            case "FC_SCALDRA" -> "Scaldra";
            case "FC_TECHROT" -> "Techrot";
            default -> faction;
        };
    }

    private static String resolveDifficulty(String difficulty) {
        if (difficulty == null) return "未知";
        return switch (difficulty) {
            case "LOW" -> "低难度";
            case "MEDIUM" -> "中难度";
            case "HIGH" -> "高难度";
            default -> difficulty;
        };
    }

    private static String resolveRiskTag(String risk) {
        if (risk == null) return "";
        return switch (risk) {
            case "RISK_HAZARD" -> "⚡环境危害";
            case "RISK_ENHANCED_ENEMIES" -> "强化敌人";
            case "RISK_LIMITED_RESOURCES" -> "资源限制";
            case "RISK_MODIFIER" -> "条件限制";
            default -> risk.replace("RISK_", "").replace("_", " ");
        };
    }

    private static int calculateImageHeight(List<Conquest> conquests) {
        int height = 160; // header
        for (Conquest c : conquests) {
            int difficultyRows = c.getMissions() != null
                    ? c.getMissions().stream().mapToInt(m -> m.getDifficulties() != null ? m.getDifficulties().size() : 1).sum()
                    : 0;
            height += 90 + difficultyRows * MISSION_ROW_HEIGHT + 50;
        }
        height += IMAGE_FOOTER_HEIGHT + 50;
        return Math.max(height, CONQUEST_IMAGE_MIN_HEIGHT);
    }
}
