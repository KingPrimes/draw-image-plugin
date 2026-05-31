package io.github.kingprimes.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 图片拼接工具类
 *
 * @author KingPrimes
 * @version 1.0.0
 */

@SuppressWarnings("unused")
public final class CombineUtils {
    private CombineUtils() {
    }

    /**
     * 将多个图片按指定方向（水平/垂直）拼接为单个图片，并在图片间保留指定间隙
     * <p>拼接规则：</p>
     * <ul>
     *   <li>水平拼接：总宽度为所有图片宽度之和 + 间隙总和，高度为所有图片中的最大高度</li>
     *   <li>垂直拼接：总高度为所有图片高度之和 + 间隙总和，宽度为所有图片中的最大宽度</li>
     * </ul>
     * <p>拼接背景为白色，使用高质量渲染模式保证图像清晰度</p>
     *
     * @param images     待拼接的图片列表（BufferedImage集合），若为null或空列表则返回null
     * @param gap        图片间的间隙大小（像素），间隙区域填充为白色
     * @param horizontal 拼接方向标识：true表示水平拼接，false表示垂直拼接
     * @return 拼接后的新BufferedImage对象，格式为TYPE_INT_ARGB；若输入图片列表无效则返回null
     */
    public static BufferedImage combineImages(List<BufferedImage> images, int gap, boolean horizontal) {
        if (images == null || images.isEmpty()) return null;

        int totalWidth = 0;
        int totalHeight = 0;
        int maxWidth = 0;
        int maxHeight = 0;

        for (BufferedImage img : images) {
            maxWidth = Math.max(maxWidth, img.getWidth());
            maxHeight = Math.max(maxHeight, img.getHeight());
            if (horizontal) {
                totalWidth += img.getWidth() + gap;
                totalHeight = maxHeight;
            } else {
                totalHeight += img.getHeight() + gap;
                totalWidth = maxWidth;
            }
        }

        BufferedImage result = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();
        GraphicsUtils.setQualityRenderingHints(g2);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, totalWidth, totalHeight);

        int currentX = 0;
        int currentY = 0;

        for (BufferedImage img : images) {
            g2.drawImage(img, currentX, currentY, null);
            if (horizontal) {
                currentX += img.getWidth() + gap;
            } else {
                currentY += img.getHeight() + gap;
            }
        }
        g2.dispose();
        return result;
    }
}