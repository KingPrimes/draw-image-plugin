package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.Relics;
import io.github.kingprimes.model.enums.RarityEnum;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 遗物图像绘制实现类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawRelicsImage {

    private static final int CARD_WIDTH = 580;
    private static final int CARD_HEIGHT = 350;
    private static final int CARD_MARGIN_X = 20;
    private static final int CARD_MARGIN_Y = 20;
    private static final int CARDS_PER_ROW = 3;
    private static final int IMAGE_WIDTH = 2250;
    private static final int TITLE_HEIGHT = 60;
    private static final int FOOTER_HEIGHT = 40;

    /**
     * 绘制遗物图像
     *
     * @param relics 遗物数据列表
     * @return 图像字节数组
     */
    public static byte[] drawRelicsImage(List<Relics> relics) {
        if (relics == null || relics.isEmpty()) {
            return new byte[0];
        }

        // 计算图像高度
        int totalHeight = calculateImageHeight(relics.size()) + TITLE_HEIGHT + FOOTER_HEIGHT + 100; // 为看板娘预留空间

        // 创建图像合成器
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, totalHeight, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        // 设置背景色
        combiner.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, IMAGE_WIDTH, totalHeight).drawTooRoundRect().drawStandingDrawing();

        // 绘制遗物卡片
        int x = IMAGE_MARGIN;
        int y = TITLE_HEIGHT + 25;
        int cardIndex = 0;

        for (Relics relic : relics) {
            // 检查是否需要换行
            if (cardIndex > 0 && cardIndex % CARDS_PER_ROW == 0) {
                x = IMAGE_MARGIN;
                y += CARD_HEIGHT + CARD_MARGIN_Y;
            }

            // 绘制遗物卡片
            BufferedImage bufferedImage = drawRelicCard(new ImageCombiner(CARD_WIDTH, CARD_HEIGHT, ImageCombiner.OutputFormat.PNG), relic);
            combiner.drawImage(bufferedImage, x, y);

            // 更新坐标
            x += CARD_WIDTH + CARD_MARGIN_X;
            cardIndex++;
        }

        // 添加底部署名
        addFooter(combiner, totalHeight - 40);

        // 合成并返回图像
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }

    /**
     * 计算图像总高度
     *
     * @param relicCount 遗物数量
     * @return 图像总高度
     */
    private static int calculateImageHeight(int relicCount) {
        int rows = (int) Math.ceil((double) relicCount / CARDS_PER_ROW);
        return rows * CARD_HEIGHT + (rows - 1) * CARD_MARGIN_Y;
    }

    /**
     * 绘制单个遗物卡片
     *
     * @param combiner 图像合成器
     * @param relic    遗物数据
     */
    private static BufferedImage drawRelicCard(ImageCombiner combiner, Relics relic) {
        // 绘制卡片背景
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT)
                .setColor(CARD_BACKGROUND_COLOR)
                .fillRoundRect(0, 0, CARD_WIDTH, CARD_HEIGHT, 15, 15)
                .setColor(new Color(0x333333))
                .setStroke(2)
                .drawRoundRect(0, 0, CARD_WIDTH, CARD_HEIGHT, 15, 15)
                .drawRoundRect(-1, -1, CARD_WIDTH, CARD_HEIGHT, 15, 15);

        // 绘制遗物名称（头部）
        combiner.setColor(TITLE_COLOR).setFont(FONT).addCenteredText(relic.getName(), 50);

        // 绘制奖励列表
        if (relic.getRewards() != null && !relic.getRewards().isEmpty()) {
            int rewardY = 100;
            for (Relics.Rewards reward : relic.getRewards()) {
                Color rarityColor = getRarityColor(reward.getRarity());
                combiner.setColor(rarityColor).addCenteredText(reward.getName(), rewardY);
                rewardY += 45;
            }
        }
        return combiner.getCombinedImage();
    }

    /**
     * 根据稀有度获取颜色
     *
     * @param rarity 稀有度枚举
     * @return 对应的颜色
     */
    private static Color getRarityColor(RarityEnum rarity) {
        if (rarity == null) {
            return TEXT_COLOR;
        }

        return switch (rarity) {
            case COMMON -> ACTIVE_MISSION_VOID_T2_COLOR; // #75562B
            case UNCOMMON -> ACTIVE_MISSION_VOID_T3_COLOR; // #B3B3B3
            case RARE -> ACTIVE_MISSION_VOID_T4_COLOR; // #C1BE39
            default -> TEXT_COLOR;
        };
    }
}