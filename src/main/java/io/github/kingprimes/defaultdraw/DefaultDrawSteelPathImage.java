package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.SteelPathOffering;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 钢铁奖励图像绘制默认实现
 *
 * @author KingPrimes
 * @version 1.0.3
 */
public class DefaultDrawSteelPathImage {

    private static final int STEEL_PATH_IMAGE_WIDTH = 800;
    private static final int STEEL_PATH_IMAGE_MIN_HEIGHT = 400;
    private static final Color HEADER_COLOR = new Color(0x4A90E2);

    private DefaultDrawSteelPathImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawSteelPathImage class");
    }

    /**
     * 绘制钢铁奖励图像
     *
     * @param steelPath 钢铁奖励数据
     * @return 图像字节数组
     */
    public static byte[] drawSteelPathImage(SteelPathOffering steelPath) {
        if (steelPath == null) {
            return new byte[0];
        }

        // 计算图像高度
        int height = STEEL_PATH_IMAGE_MIN_HEIGHT;

        // 创建图像组合器
        BufferedImage image = new BufferedImage(STEEL_PATH_IMAGE_WIDTH, height, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        // 填充背景色
        combiner
                .setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, STEEL_PATH_IMAGE_WIDTH, height)
                // 绘制双层边框
                .drawTooRoundRect();

        // 绘制标题
        combiner.setColor(HEADER_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 40))
                .addCenteredText("钢铁奖励", 80)
                .drawStandingDrawing();

        // 绘制当前奖励信息
        int currentY = 120;
        if (steelPath.getCurrentReward() != null) {
            combiner
                    .setColor(TEXT_COLOR)
                    .setFont(FONT)
                    .addText("当前奖励: " + steelPath.getCurrentReward(), IMAGE_MARGIN, currentY + 25);
            currentY += 40;
        }

        // 绘制下一个奖励信息
        if (steelPath.getNextReward() != null) {
            combiner.setFont(FONT)
                    .setColor(TEXT_COLOR)
                    .addText("下一个奖励: " + steelPath.getNextReward(), IMAGE_MARGIN, currentY + 25);
            currentY += 40;
        }

        // 绘制剩余时间
        combiner.setFont(FONT)
                .setColor(TEXT_COLOR)
                .addText("剩余时间: " + steelPath.getRemaining(), IMAGE_MARGIN, currentY + 25);

        // 添加底部署名
        addFooter(combiner, height - IMAGE_FOOTER_HEIGHT);

        // 合并图像
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }
}