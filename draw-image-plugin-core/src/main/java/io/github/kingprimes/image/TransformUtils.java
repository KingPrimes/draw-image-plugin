package io.github.kingprimes.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片变换工具类（静态工具方法，无状态）
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public final class TransformUtils {
    private TransformUtils() {
    }

    /**
     * 将指定的BufferedImage缩放到目标宽度和高度，使用高质量渲染模式以保证缩放效果
     *
     * @param image     原始图片对象（待缩放的BufferedImage）
     * @param newWidth  缩放后的目标宽度（像素）
     * @param newHeight 缩放后的目标高度（像素）
     * @return 缩放后的新BufferedImage对象，格式为TYPE_INT_ARGB
     */
    public static BufferedImage scaleImage(BufferedImage image, int newWidth, int newHeight) {
        BufferedImage scaled = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        GraphicsUtils.setQualityRenderingHints(g2);
        g2.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2.dispose();
        return scaled;
    }

    /**
     * 将指定图片按给定角度旋转，并返回旋转后的新图片对象
     * <p>旋转围绕图片中心点进行，使用高质量渲染模式保证图像清晰度</p>
     *
     * @param image          原始图片对象（待旋转的BufferedImage）
     * @param angleInDegrees 旋转角度（单位：度），正值表示顺时针旋转，负值表示逆时针旋转
     * @return 旋转后的新BufferedImage对象，尺寸与原始图片一致，格式为TYPE_INT_ARGB
     */
    public static BufferedImage rotateImage(BufferedImage image, double angleInDegrees) {
        double angle = Math.toRadians(angleInDegrees);
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage rotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = rotated.createGraphics();
        GraphicsUtils.setQualityRenderingHints(g2);
        g2.rotate(angle, w / 2.0, h / 2.0);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return rotated;
    }

    /**
     * 裁剪图片指定区域，返回新的BufferedImage对象
     *
     * @param image  原始图片对象（待裁剪的BufferedImage）
     * @param x      裁剪区域的起始横坐标（像素）
     * @param y      裁剪区域的起始纵坐标（像素）
     * @param width  裁剪区域的宽度（像素）
     * @param height 裁剪区域的高度（像素）
     * @return 裁剪后的新BufferedImage对象
     */
    public static BufferedImage cropImage(BufferedImage image, int x, int y, int width, int height) {
        return image.getSubimage(x, y, width, height);
    }
}