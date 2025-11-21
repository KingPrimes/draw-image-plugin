package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.Sortie;
import io.github.kingprimes.model.worldstate.Variant;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 突击图像绘制默认实现
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawSortiesImage {

    private static final int SORTIE_IMAGE_WIDTH = 1200;
    private static final int SORTIE_IMAGE_MIN_HEIGHT = 600;
    private static final int SORTIE_ROW_HEIGHT = 60;
    private static final Color HEADER_COLOR = new Color(0x4A90E2);

    private DefaultDrawSortiesImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawSortiesImage class");
    }

    /**
     * 绘制突击图像
     *
     * @param sorties 突击数据列表
     * @return 图像字节数组
     */
    public static byte[] drawSortiesImage(Sortie sorties) {
        if (sorties == null) {
            return new byte[0];
        }

        // 计算图像高度
        int height = calculateImageHeight(sorties);

        // 创建图像组合器
        BufferedImage image = new BufferedImage(SORTIE_IMAGE_WIDTH, height, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        // 填充背景色
        combiner.setFont(FONT)
                .setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, SORTIE_IMAGE_WIDTH, height)
                // 绘制双层边框
                .drawTooRoundRect();

        // 绘制标题
        combiner.setColor(HEADER_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 40))
                .addCenteredText("突击任务", 80)
                .drawStandingDrawing();

        // 纵向排列绘制突击任务数据
        int currentY = 120;

        // 绘制Boss标题
        combiner.setColor(HEADER_COLOR)
                .setFont(FONT)
                .addText("Boss: " + sorties.getBoss(), IMAGE_MARGIN, currentY + 30);

        currentY += 100;

        // 绘制变体任务
        if (sorties.getVariants() != null) {
            for (int j = 0; j < sorties.getVariants().size(); j++) {
                Variant variant = sorties.getVariants().get(j);

                // 绘制斑马纹背景
                if (j % 2 == 1) { // 奇数行添加背景色
                    combiner.setColor(new Color(0xE0E0E0, true))
                            .fillRect(IMAGE_MARGIN, currentY, SORTIE_IMAGE_WIDTH - 2 * IMAGE_MARGIN, SORTIE_ROW_HEIGHT);
                }

                // 绘制任务信息
                combiner.setColor(TEXT_COLOR)
                        .setFont(FONT);

                // 任务类型

                if (variant.getMissionType() != null) {
                    String missionType = variant.getMissionTypeName();
                    combiner
                            .setColor(variant.getMissionTypeColor())
                            .addText("任务: " + missionType, IMAGE_MARGIN + 20, currentY + 20);
                }

                // 节点
                String node = variant.getNode() != null ? variant.getNode() : "未知";
                combiner.addText("节点: " + node, IMAGE_MARGIN + 225, currentY + 20);

                // 修饰
                String modifier = variant.getModifierType() != null ? variant.getModifierTypeStr() : "未知";
                combiner.addText(modifier, IMAGE_MARGIN + 580, currentY + 20);

                currentY += SORTIE_ROW_HEIGHT;
            }
        }

        // 添加底部署名
        addFooter(combiner, height - IMAGE_FOOTER_HEIGHT);

        // 合并图像
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }

    /**
     * 计算图像高度
     *
     * @param sorties 突击数据列表
     * @return 图像高度
     */
    private static int calculateImageHeight(Sortie sorties) {
        int headerHeight = 120;
        int sectionHeight = 50; // Boss标题高度

        int totalHeight = headerHeight;

        totalHeight += sectionHeight; // Boss标题

        if (sorties.getVariants() != null) {
            totalHeight += SORTIE_ROW_HEIGHT * sorties.getVariants().size(); // 任务行
        }

        totalHeight += IMAGE_FOOTER_HEIGHT;

        return Math.max(totalHeight, SORTIE_IMAGE_MIN_HEIGHT);
    }
}