package io.github.kingprimes.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 图层工具类，用于管理多个图片图层，并组合成最终的图片
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public final class LayerUtils {
    private static final List<Layer> layers = new ArrayList<>();

    private LayerUtils() {
    }

    /**
     * 向图层列表中添加一个新的图层
     *
     * @param image     图层的图像数据
     * @param x         图层在画布上的x坐标
     * @param y         图层在画布上的y坐标
     * @param alpha     图层的透明度，取值范围为0.0-1.0
     * @param composite 图层的混合模式
     */
    public static void addLayer(BufferedImage image, int x, int y, float alpha, Composite composite) {
        layers.add(new Layer(image, x, y, alpha, composite));
    }

    /**
     * 移除指定索引位置的图层
     *
     * @param index 要移除的图层索引，从0开始计数
     */
    public static void removeLayer(int index) {
        // 检查索引是否在有效范围内
        if (index >= 0 && index < layers.size()) {
            layers.remove(index);
        }
    }

    /**
     * 将图层从一个位置移动到另一个位置
     *
     * @param fromIndex 源位置索引
     * @param toIndex   目标位置索引
     */
    public static void moveLayerTo(int fromIndex, int toIndex) {
        if (fromIndex >= 0 && fromIndex < layers.size() && toIndex >= 0 && toIndex < layers.size()) {
            // 移除源位置的图层并将其添加到目标位置
            Layer layer = layers.remove(fromIndex);
            layers.add(toIndex, layer);
        }
    }

    /**
     * 渲染所有图层到一个合成的BufferedImage中
     *
     * @param width   图像宽度
     * @param height  图像高度
     * @param bgColor 背景颜色，如果为null则不绘制背景
     * @return 合成后的BufferedImage对象
     */
    public static BufferedImage renderLayers(int width, int height, Color bgColor) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();
        GraphicsUtils.setQualityRenderingHints(g2);
        if (bgColor != null) {
            g2.setColor(bgColor);
            g2.fillRect(0, 0, width, height);
        }
        for (Layer layer : layers) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, layer.alpha));
            g2.drawImage(layer.image, layer.x, layer.y, null);
        }
        g2.dispose();
        return result;
    }

    /**
     * 清空所有图层数据
     * 该方法用于清除layers集合中的所有元素，将集合重置为空状态
     */
    public static void clearLayers() {
        layers.clear();
    }

    /**
     * 图层数据结构，包含图像数据、位置、透明度和混合模式等信息
     *
     * @param image     图层图像
     * @param x         图层x坐标
     * @param y         图层y坐标
     * @param alpha     图层透明度
     * @param composite 图层混合模式
     */
    public record Layer(BufferedImage image, int x, int y, float alpha, Composite composite) {
    }
}