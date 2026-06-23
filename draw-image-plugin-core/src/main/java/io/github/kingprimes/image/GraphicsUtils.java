package io.github.kingprimes.image;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Graphics绘制工具类
 * <p>提供一系列工具方法，用于处理Graphics2D对象，如配置高质量渲染参数、创建圆角矩形边框图片、创建渐变背景图片等</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */

@SuppressWarnings("unused")
public final class GraphicsUtils {
    private GraphicsUtils() {
    }

    /**
     * 为Graphics2D对象配置高质量渲染参数，优化图像绘制和文本显示效果
     * <p>通过设置一系列渲染提示（RenderingHints），提升图像边缘平滑度、文本清晰度及缩放/变换质量，适用于需要高精度视觉效果的场景</p>
     * <p>配置的关键渲染参数包括：</p>
     * <ul>
     *   <li>{@link RenderingHints#KEY_ANTIALIASING}: 启用抗锯齿（VALUE_ANTIALIAS_ON），使图形边缘更平滑</li>
     *   <li>{@link RenderingHints#KEY_TEXT_ANTIALIASING}: 使用LCD HRGB文本抗锯齿（VALUE_TEXT_ANTIALIAS_LCD_HRGB），优化LCD屏幕文本显示</li>
     *   <li>{@link RenderingHints#KEY_TEXT_LCD_CONTRAST}: 设置LCD文本对比度为140，增强文本可读性</li>
     *   <li>{@link RenderingHints#KEY_RENDERING}: 优先渲染质量（VALUE_RENDER_QUALITY），牺牲部分性能换取更高视觉效果</li>
     *   <li>{@link RenderingHints#KEY_INTERPOLATION}: 使用双三次插值（VALUE_INTERPOLATION_BICUBIC），提升图像缩放/变换时的细节保留</li>
     *   <li>{@link RenderingHints#KEY_FRACTIONALMETRICS}: 启用分数度量（VALUE_FRACTIONALMETRICS_ON），优化文本布局精度</li>
     * </ul>
     *
     * @param g2 需要配置渲染参数的Graphics2D对象（非null）
     */
    public static void setQualityRenderingHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 140);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    /**
     * 将原始图片处理为圆角矩形边框效果，通过图形上下文裁剪实现圆角 corners
     * <p>实现原理：创建与原始图片尺寸一致的透明图像，使用{@link RoundRectangle2D.Float}定义圆角裁剪区域，
     * 并通过高质量渲染参数确保圆角边缘平滑无锯齿</p>
     *
     * @param image        原始图片对象（待处理的BufferedImage）
     * @param cornerRadius 圆角半径（像素），控制圆角的弯曲程度，值越大圆角越圆润
     * @return 带圆角边框的新BufferedImage对象，尺寸与原始图片一致，格式为TYPE_INT_ARGB，背景透明
     */
    public static BufferedImage createRoundedImage(BufferedImage image, int cornerRadius) {
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        setQualityRenderingHints(g2);
        g2.setClip(new RoundRectangle2D.Float(0, 0, image.getWidth(), image.getHeight(), cornerRadius, cornerRadius));
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return output;
    }

    /**
     * 在目标图像上绘制指定的形状
     *
     * @param target      目标缓冲图像，形状将被绘制在此图像上
     * @param shape       要绘制的形状对象
     * @param fillColor   填充颜色，如果为null则不填充
     * @param strokeColor 描边颜色，如果为null或 strokeWidth &lt;= 0 则不描边
     * @param strokeWidth 描边宽度，必须大于0才生效
     */
    public static void drawShape(BufferedImage target, Shape shape, Color fillColor, Color strokeColor, int strokeWidth) {
        Graphics2D g2 = target.createGraphics();
        setQualityRenderingHints(g2);
        if (fillColor != null) {
            g2.setColor(fillColor);
            g2.fill(shape);
        }
        if (strokeColor != null && strokeWidth > 0) {
            g2.setColor(strokeColor);
            g2.setStroke(new BasicStroke(strokeWidth));
            g2.draw(shape);
        }
        g2.dispose();
    }

    /**
     * 创建指定尺寸和渐变效果的背景图片，支持横向或纵向渐变方向
     * <p>实现原理：通过{@link GradientPaint}定义颜色过渡区域，结合高质量渲染参数确保渐变过渡平滑，最终填充整个图像区域</p>
     * <p>渐变方向规则：</p>
     * <ul>
     *   <li>当{@code vertical=true}时：从上到下垂直渐变（起点(0,0)到终点(0,height)）</li>
     *   <li>当{@code vertical=false}时：从左到右水平渐变（起点(0,0)到终点(width,0)）</li>
     * </ul>
     *
     * @param width      背景图片宽度（像素）
     * @param height     背景图片高度（像素）
     * @param startColor 渐变起始颜色（渐变开始位置的颜色）
     * @param endColor   渐变结束颜色（渐变终止位置的颜色）
     * @param vertical   渐变方向标识：true表示垂直渐变，false表示水平渐变
     * @return 带渐变背景的新BufferedImage对象，格式为TYPE_INT_ARGB，尺寸与输入宽高一致
     */
    public static BufferedImage createGradientBackground(int width, int height, Color startColor, Color endColor, boolean vertical) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        setQualityRenderingHints(g2);
        GradientPaint gp = vertical
                ? new GradientPaint(0, 0, startColor, 0, height, endColor)
                : new GradientPaint(0, 0, startColor, width, 0, endColor);
        g2.setPaint(gp);
        g2.fillRect(0, 0, width, height);
        g2.dispose();
        return image;
    }
}