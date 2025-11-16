package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.SeasonInfo;

import java.awt.*;
import java.io.ByteArrayOutputStream;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 电波图像绘制默认实现
 *
 * @author KingPrimes
 * @version 1.0.3
 */
public final class DefaultDrawSeasonInfoImage {

    private static final int SEASON_IMAGE_WIDTH = 1800;
    private static final int SEASON_IMAGE_MIN_HEIGHT = 600;
    private static final int CARD_HEIGHT = 200;
    private static final int CARD_MARGIN_H = 20;
    private static final int CARD_MARGIN_V = 30;
    private static final int CARDS_PER_ROW = 3;
    private static final int CARD_PADDING = 15;
    private static final int LINE_HEIGHT = 40;
    
    private static final Color HEADER_COLOR = new Color(0x4A90E2);
    private static final Color DAILY_CHALLENGE_COLOR = new Color(0xFF9500);
    private static final Color WEEKLY_CHALLENGE_COLOR = new Color(0x4A90E2);
    private static final Color ELITE_CHALLENGE_COLOR = new Color(0x9B59B6);
    private static final Color CARD_BORDER_COLOR = new Color(0xCCCCCC);

    private DefaultDrawSeasonInfoImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawSeasonInfoImage class");
    }

    /**
     * 绘制电波图像
     *
     * @param seasonInfo 电波数据
     * @return 图像字节数组
     */
    public static byte[] drawSeasonInfoImage(SeasonInfo seasonInfo) {
        if (seasonInfo == null) {
            return new byte[0];
        }

        // 计算图像高度
        int height = calculateImageHeight(seasonInfo);

        // 创建画布
        ImageCombiner combiner = new ImageCombiner(
                SEASON_IMAGE_WIDTH,
                height,
                ImageCombiner.OutputFormat.PNG
        );

        // 填充背景色
        combiner.setFont(FONT)
                .setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, SEASON_IMAGE_WIDTH, height)
                // 绘制双层边框
                .drawTooRoundRect();

        // 绘制标题
        combiner.setColor(HEADER_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 40))
                .addCenteredText("电波赛季信息", 80);

        // 绘制任务卡片
        if (seasonInfo.getActiveChallenges() != null && !seasonInfo.getActiveChallenges().isEmpty()) {
            int startY = 140;
            drawChallengeCards(combiner, seasonInfo.getActiveChallenges(), startY);
        }

        // 添加底部署名
        addFooter(combiner, height - IMAGE_FOOTER_HEIGHT);

        // 合并图像
        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }

    /**
     * 绘制挑战任务卡片
     *
     * @param combiner   图像合成器
     * @param challenges 挑战任务列表
     * @param startY     起始Y坐标
     */
    private static void drawChallengeCards(ImageCombiner combiner, 
                                          java.util.List<SeasonInfo.ActiveChallenges> challenges, 
                                          int startY) {
        int totalWidth = SEASON_IMAGE_WIDTH - 2 * IMAGE_MARGIN;
        int availableWidth = totalWidth - (CARDS_PER_ROW - 1) * CARD_MARGIN_H;
        int cardWidth = availableWidth / CARDS_PER_ROW;
        
        for (int i = 0; i < challenges.size(); i++) {
            int row = i / CARDS_PER_ROW;
            int col = i % CARDS_PER_ROW;
            
            int cardX = IMAGE_MARGIN + col * (cardWidth + CARD_MARGIN_H);
            int cardY = startY + row * (CARD_HEIGHT + CARD_MARGIN_V);
            
            drawChallengeCard(combiner, challenges.get(i), cardX, cardY, cardWidth);
        }
    }

    /**
     * 绘制单个挑战任务卡片
     *
     * @param combiner  图像合成器
     * @param challenge 挑战任务数据
     * @param x         卡片X坐标
     * @param y         卡片Y坐标
     * @param width     卡片宽度
     */
    private static void drawChallengeCard(ImageCombiner combiner, 
                                         SeasonInfo.ActiveChallenges challenge, 
                                         int x, 
                                         int y,
                                         int width) {
        // 绘制卡片背景
        combiner.setColor(CARD_BACKGROUND_COLOR)
                .fillRoundRect(x, y, width, CARD_HEIGHT, 10, 10);
        
        // 绘制卡片边框
        combiner.setColor(CARD_BORDER_COLOR)
                .setStroke(1)
                .drawRoundRect(x, y, width, CARD_HEIGHT, 10, 10);
        
        int contentX = x + CARD_PADDING;
        int contentY = y + CARD_PADDING;
        // 第一行：任务类型
        contentY = drawChallengeType(combiner, challenge, contentX, contentY);
        
        // 第二行：任务名称
        contentY = drawChallengeName(combiner, challenge, contentX, contentY);
        
        // 第三行：任务描述
        contentY = drawChallengeDescription(combiner, challenge, contentX, contentY);
        
        // 第四行：任务奖励声望
        drawChallengeStanding(combiner, challenge, contentX, contentY);
    }

    /**
     * 绘制挑战任务类型
     *
     * @param combiner 图像合成器
     * @param challenge 挑战任务数据
     * @param x X坐标
     * @param y Y坐标
     * @return 下一行的Y坐标
     */
    private static int drawChallengeType(ImageCombiner combiner, 
                                        SeasonInfo.ActiveChallenges challenge, 
                                        int x, 
                                        int y) {
        combiner.setFont(FONT);
        
        StringBuilder typeText = new StringBuilder();
        Color typeColor = TEXT_COLOR;
        
        if (Boolean.TRUE.equals(challenge.getDaily())) {
            typeText.append("每日");
            typeColor = DAILY_CHALLENGE_COLOR;
        }
        
        if (Boolean.TRUE.equals(challenge.getWeekly())) {
            if (!typeText.isEmpty()) typeText.append(" ");
            typeText.append("每周");
            typeColor = WEEKLY_CHALLENGE_COLOR;
        }
        
        if (Boolean.TRUE.equals(challenge.getElite())) {
            if (!typeText.isEmpty()) typeText.append(" ");
            typeText.append("精英");
            typeColor = ELITE_CHALLENGE_COLOR;
        }
        
        if (typeText.isEmpty()) {
            typeText.append("普通");
        }
        
        combiner.setColor(typeColor)
                .addText(typeText.toString(), x, y + FONT_SIZE);
        
        return y + LINE_HEIGHT;
    }

    /**
     * 绘制挑战任务名称
     *
     * @param combiner 图像合成器
     * @param challenge 挑战任务数据
     * @param x X坐标
     * @param y Y坐标
     * @return 下一行的Y坐标
     */
    private static int drawChallengeName(ImageCombiner combiner, 
                                        SeasonInfo.ActiveChallenges challenge, 
                                        int x, 
                                        int y) {
        String name = challenge.getName() != null ? challenge.getName() : "未知任务";
        
        combiner.setColor(TEXT_COLOR)
                .setFont(FONT)
                .addText(name, x, y + FONT_SIZE);
        
        return y + LINE_HEIGHT;
    }

    /**
     * 绘制挑战任务描述
     *
     * @param combiner 图像合成器
     * @param challenge 挑战任务数据
     * @param x X坐标
     * @param y Y坐标
     * @return 下一行的Y坐标
     */
    private static int drawChallengeDescription(ImageCombiner combiner, 
                                               SeasonInfo.ActiveChallenges challenge, 
                                               int x, 
                                               int y) {
        String description = challenge.getDescription() != null ? challenge.getDescription() : "无描述";
        
        combiner.setColor(TEXT_COLOR)
                .setFont(FONT)
                .addText(description, x, y + FONT_SIZE);
        
        return y + LINE_HEIGHT;
    }

    /**
     * 绘制挑战任务奖励声望
     *
     * @param combiner 图像合成器
     * @param challenge 挑战任务数据
     * @param x X坐标
     * @param y Y坐标
     * @return 下一行的Y坐标
     */
    private static int drawChallengeStanding(ImageCombiner combiner, 
                                            SeasonInfo.ActiveChallenges challenge, 
                                            int x, 
                                            int y) {
        String standing = challenge.getStanding() != null ? 
                "声望: " + challenge.getStanding() : "声望: 0";
        
        combiner.setColor(TEXT_COLOR)
                .setFont(FONT)
                .addText(standing, x, y + FONT_SIZE);
        
        return y + LINE_HEIGHT;
    }

    /**
     * 计算图像高度
     *
     * @param seasonInfo 电波数据
     * @return 图像高度
     */
    private static int calculateImageHeight(SeasonInfo seasonInfo) {
        int headerHeight = 140; // 标题区域
        int footerHeight = IMAGE_FOOTER_HEIGHT + 20;

        int totalHeight = headerHeight;

        // 如果有挑战任务，计算卡片区域高度
        if (seasonInfo.getActiveChallenges() != null && !seasonInfo.getActiveChallenges().isEmpty()) {
            int challengeCount = seasonInfo.getActiveChallenges().size();
            int rows = (int) Math.ceil((double) challengeCount / CARDS_PER_ROW);
            int cardsHeight = rows * CARD_HEIGHT + (rows - 1) * CARD_MARGIN_V;
            totalHeight += cardsHeight + 40; // 额外空间
        }

        totalHeight += footerHeight;

        return Math.max(totalHeight, SEASON_IMAGE_MIN_HEIGHT);
    }
}