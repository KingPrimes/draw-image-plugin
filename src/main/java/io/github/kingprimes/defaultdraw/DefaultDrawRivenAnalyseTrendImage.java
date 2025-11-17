package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.RivenAnalyseTrend;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 紫卡分析趋势图像绘制实现类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawRivenAnalyseTrendImage {

    private static final Color UP = new Color(0x3B9A21);
    private static final Color DOW = new Color(0xAC1818);

    // 私有构造函数防止实例化
    private DefaultDrawRivenAnalyseTrendImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawRivenAnalyseTrendImage class");
    }

    /**
     * 绘制紫卡分析趋势图像
     *
     * @param rivenAnalyseTrends 紫卡分析数据列表
     * @return 图像字节数组
     */
    public static byte[] drawRivenAnalyseTrendImage(List<RivenAnalyseTrend> rivenAnalyseTrends) {
        if (rivenAnalyseTrends == null || rivenAnalyseTrends.isEmpty()) {
            return new byte[0];
        }

        // 计算图像尺寸
        int cardWidth = 700;
        int cardHeight = 400;
        int cardMargin = 60;
        int columns = Math.min(2, rivenAnalyseTrends.size());
        int rows = (int) Math.ceil((double) rivenAnalyseTrends.size() / columns);

        int totalWidth = cardWidth * columns + cardMargin * (columns + 1) + 400;
        int totalHeight = cardHeight * rows + cardMargin * (rows + 1) + 200; // 为标题和看板娘预留空间

        // 创建图像合成器
        BufferedImage image = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        // 设置背景色
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, totalWidth, totalHeight)
                .drawTooRoundRect();

        // 绘制标题
        int titleY = 100;
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(48f))
                .addCenteredText("紫卡分析趋势", titleY);

        // 绘制紫卡分析卡片
        int currentY = titleY + 40;
        for (int i = 0; i < rivenAnalyseTrends.size(); i++) {
            RivenAnalyseTrend trend = rivenAnalyseTrends.get(i);
            int row = i / columns;
            int col = i % columns;

            int cardX = cardMargin + col * (cardWidth + cardMargin);
            int cardY = currentY + cardMargin + row * (cardHeight + cardMargin);

            combiner.drawImage(
                    drawRivenAnalyseTrendCard(
                            new ImageCombiner(cardWidth, cardHeight, ImageCombiner.OutputFormat.PNG),
                            trend),
                    cardX, cardY);
        }

        // 添加底部署名
        addFooter(combiner, totalHeight - 40);

        // 添加看板娘图片
        combiner.drawStandingDrawing();

        // 合成并返回图像
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }

    /**
     * 绘制单个紫卡分析卡片
     *
     * @param combiner 图像合成器
     * @param trend    紫卡分析数据
     * @return 绘制完成的图像
     */
    private static BufferedImage drawRivenAnalyseTrendCard(ImageCombiner combiner, RivenAnalyseTrend trend) {
        int width = combiner.getCanvasWidth();
        int height = combiner.getCanvasHeight();

        // 绘制卡片背景
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, width, height)
                .setStroke(2)
                .setColor(CARD_BACKGROUND_COLOR)
                .fillRoundRect(0, 0, width, height, 20, 20)
                .setColor(BLACK_COLOR)
                .drawRoundRect(0, 0, width - 2, height - 2, 20, 20);

        // 绘制武器名称和紫卡名称
        combiner.setColor(TEXT_COLOR)
                .setFont(FONT.deriveFont(24f))
                .addCenteredText("武器: " + trend.getWeaponName(), 50)
                .addCenteredText("紫卡: " + trend.getRivenName(), 80);

        // 绘制倾向点和数值
        combiner
                .addText("倾向点: " + trend.getDot(), 20, 120)
                .addText("倾向数值: " + (trend.getNum() != null ? trend.getNum() : "N/A"), 20, 150)
                .addText("武器类型: " + trend.getWeaponType(), 20, 180);

        // 绘制属性信息
        if (trend.getAttributes() != null && !trend.getAttributes().isEmpty()) {
            int attrY = 220;

            for (RivenAnalyseTrend.Attribute attribute : trend.getAttributes()) {
                String attrText = formatAttributeText(attribute);
                combiner.setColor(attribute.getAttrDiff().contains("-") ? DOW : UP)
                        .addText(attrText, 20, attrY);
                attrY += 30;
            }
        }

        combiner.combine();
        return combiner.getCombinedImage();
    }

    /**
     * 格式化属性文本
     *
     * @param attribute 属性对象
     * @return 格式化后的文本
     */
    private static String formatAttributeText(RivenAnalyseTrend.Attribute attribute) {
        if (attribute == null) {
            return "";
        }

        // 按照 attributeName (lowAttr% - highAttr%) | attrDiff 格式绘制
        return String.format("%s (%s%% - %s%%) | %s",
                attribute.getAttributeName() != null ? attribute.getAttributeName() : "N/A",
                attribute.getLowAttr() != null ? attribute.getLowAttr() : "N/A",
                attribute.getHighAttr() != null ? attribute.getHighAttr() : "N/A",
                attribute.getAttrDiff() != null ? attribute.getAttrDiff() : "N/A");
    }
}