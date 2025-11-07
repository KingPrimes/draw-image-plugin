package io.github.kingprimes.image;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文字处理工具类
 * <p>提供静态工具方法，用于处理文本相关的功能，如自动换行、居中计算、高度计算等</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public final class TextUtils {

    // 私有构造，防止实例化
    private TextUtils() {
    }

    // ---------------------- 居中坐标计算 ----------------------

    /**
     * 计算单行文本在指定宽度画布上的水平居中X坐标
     * <p>计算逻辑：通过画布宽度减去文本宽度的差值除以2，得到文本左边缘的起始X坐标，使文本在水平方向居中</p>
     * <p>特殊处理：若{@code metrics}或{@code text}为null，直接返回0以避免空指针异常</p>
     *
     * @param metrics     字体度量对象，用于获取文本宽度（通过{@link FontMetrics#stringWidth(String)}）
     * @param text        待居中的单行文本内容（文本宽度将用于居中计算）
     * @param canvasWidth 画布/容器的宽度（像素），即文本居中的参考宽度
     * @return 文本水平居中时的左边缘X坐标（像素）；若输入参数无效（metrics或text为null）则返回0
     */
    public static int calculateCenterX(FontMetrics metrics, String text, int canvasWidth) {
        if (metrics == null || text == null) return 0;
        return (canvasWidth - metrics.stringWidth(text)) / 2;
    }

    /**
     * 计算单行文字水平居中的X坐标（带偏移量）
     */
    public static int calculateCenterX(FontMetrics metrics, String text, int canvasWidth, int xOffset) {
        return calculateCenterX(metrics, text, canvasWidth) + xOffset;
    }

    /**
     * 取文本宽度
     *
     * @param msg  文本
     * @param font 字体
     * @return 宽度
     */
    public static int getFortWidth(String msg, Font font) {
        int n = msg.length();
        return n / 2 * font.getSize() + font.getSize() * 2 - 20;
    }

    /**
     * 取文本高度
     *
     * @param msg  文本
     * @param font 字体
     * @return 高度
     */
    public static int getFortHeight(String msg, Font font) {
        return font.getSize() * msg.split("\n").length;
    }

    /**
     * 计算单行文字完全居中（X、Y轴）的坐标
     *
     * @param metrics      字体度量信息，用于获取文字尺寸信息
     * @param text         要居中的文字内容
     * @param canvasWidth  画布宽度，用于X轴居中计算
     * @param canvasHeight 画布高度，用于Y轴居中计算
     * @return 返回文字居中时的坐标点，X为横向居中位置，Y为纵向居中位置（考虑文字基线）
     */
    public static Point calculateCenterXY(FontMetrics metrics, String text, int canvasWidth, int canvasHeight) {
        if (metrics == null || text == null) return new Point(0, 0);
        int x = calculateCenterX(metrics, text, canvasWidth);
        // Y轴居中需考虑文字基线（ascent），避免文字偏上
        int y = (canvasHeight + metrics.getAscent() - metrics.getDescent()) / 2;
        return new Point(x, y);
    }

    /**
     * 计算单行文字完全居中（X、Y轴）的坐标（带偏移量）
     *
     * @param metrics      字体度量信息，用于计算文字尺寸
     * @param text         要居中的文本内容
     * @param canvasWidth  画布宽度
     * @param canvasHeight 画布高度
     * @param xOffset      X轴偏移量
     * @param yOffset      Y轴偏移量
     * @return 返回带有偏移量的居中坐标点
     */
    public static Point calculateCenterXY(FontMetrics metrics, String text, int canvasWidth, int canvasHeight, int xOffset, int yOffset) {
        Point center = calculateCenterXY(metrics, text, canvasWidth, canvasHeight);
        return new Point(center.x + xOffset, center.y + yOffset);
    }

    // ---------------------- 自动换行 ----------------------

    /**
     * 根据指定的最大宽度对文本进行自动换行处理
     *
     * @param metrics  字体度量信息，用于计算文本宽度
     * @param text     需要换行处理的文本内容
     * @param maxWidth 每行文本的最大宽度限制
     * @return 按照宽度限制换行后的文本行数组
     */
    public static String[] wrapText(FontMetrics metrics, String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        if (metrics == null || text == null || maxWidth <= 0) {
            lines.add(text == null ? "" : text);
            return lines.toArray(new String[0]);
        }

        StringBuilder currentLine = new StringBuilder();
        String[] words = text.split("");

        for (String word : words) {
            // 测试当前行加单词是否超过最大宽度
            String testLine = currentLine + word + " ";
            if (metrics.stringWidth(testLine) <= maxWidth) {
                currentLine.append(word).append("");
            } else {
                // 单个单词超过最大宽度，直接单独成行
                if (currentLine.isEmpty()) {
                    lines.add(word);
                } else {
                    lines.add(currentLine.toString().trim());
                    currentLine = new StringBuilder(word);
                }
            }
        }
        // 添加最后一行
        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString().trim());
        }
        return lines.toArray(new String[0]);
    }

    /**
     * 计算自动换行后文本的总高度
     *
     * @param metrics  字体度量信息，用于计算文本尺寸
     * @param text     需要计算高度的文本内容
     * @param maxWidth 文本最大宽度，超过此宽度将自动换行
     * @param spacing  行间距，相邻两行之间的垂直距离
     * @return 自动换行后文本的总高度，包括所有行的高度和行间距的总和
     */
    public static int calculateWrappedTextHeight(FontMetrics metrics, String text, int maxWidth, int spacing) {
        if (metrics == null || text == null || maxWidth <= 0) return 0;

        // 使用wrapText方法获取换行后的文本行数组
        String[] lines = wrapText(metrics, text, maxWidth);

        // 使用现有的calculateMultilineHeight方法计算总高度
        return calculateMultilineHeight(metrics, lines, spacing);
    }

    // ---------------------- 文本高度计算 ----------------------

    /**
     * 计算多行文本总高度（含行间距）
     *
     * @param metrics 字体度量信息，用于获取单行文本高度
     * @param lines   文本行数组，每行文本内容
     * @param spacing 行间距，相邻两行之间的垂直距离
     * @return 多行文本的总高度，包括所有行的高度和行间距的总和
     */
    public static int calculateMultilineHeight(FontMetrics metrics, String[] lines, int spacing) {
        if (metrics == null || lines == null || lines.length == 0) return 0;
        // 总高度 = 每行高度之和 + 行间距之和（行数-1）
        return lines.length * metrics.getHeight() + (lines.length - 1) * spacing;
    }

    // ---------------------- 基础文本绘制 ----------------------

    /**
     * 绘制带背景框的单行文本
     *
     * @param g2           Graphics2D对象，用于绘制操作
     * @param font         文本字体，如果为null则使用Graphics2D当前字体
     * @param textColor    文本颜色，如果为null则使用黑色
     * @param text         要绘制的文本内容
     * @param x            文本绘制的x坐标位置
     * @param y            文本绘制的y坐标位置（基线位置）
     * @param bgColor      背景颜色，如果为null则不绘制背景
     * @param borderColor  边框颜色，如果为null则不绘制边框
     * @param cornerRadius 背景和边框的圆角半径
     * @param padding      背景与文本之间的内边距
     */
    public static void drawTextWithBackground(Graphics2D g2, Font font, Color textColor,
                                              String text, int x, int y,
                                              Color bgColor, Color borderColor,
                                              int cornerRadius, int padding) {
        if (g2 == null || text == null) return;

        FontMetrics metrics = g2.getFontMetrics(font != null ? font : g2.getFont());
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();

        // 1. 绘制背景（圆角矩形）
        if (bgColor != null) {
            g2.setColor(bgColor);
            g2.fillRoundRect(
                    x - padding,                      // 背景X（左移内边距）
                    y - metrics.getAscent() - padding, // 背景Y（上移内边距+文字上半部分）
                    textWidth + 2 * padding,          // 背景宽（文字宽+2倍内边距）
                    textHeight,                       // 背景高（文字高度）
                    cornerRadius, cornerRadius
            );
        }

        // 2. 绘制边框
        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.drawRoundRect(
                    x - padding,
                    y - metrics.getAscent() - padding,
                    textWidth + 2 * padding,
                    textHeight,
                    cornerRadius, cornerRadius
            );
        }

        // 3. 绘制文字（恢复文字颜色和字体）
        g2.setColor(textColor != null ? textColor : Color.BLACK);
        if (font != null) g2.setFont(font);
        g2.drawString(text, x, y);
    }

    /**
     * 绘制带背景框的多行文本
     *
     * @param g2           Graphics2D对象，用于绘制操作
     * @param font         文本字体，如果为null则使用Graphics2D当前字体
     * @param textColor    文本颜色，如果为null则默认使用黑色
     * @param text         要绘制的文本内容，支持换行符分隔多行
     * @param x            文本起始绘制位置的x坐标（左上角）
     * @param y            文本起始绘制位置的y坐标（基线位置）
     * @param bgColor      背景颜色，如果为null则不绘制背景
     * @param borderColor  边框颜色，如果为null则不绘制边框
     * @param cornerRadius 背景和边框的圆角半径
     * @param padding      文本与背景框之间的内边距
     */
    public static void drawMultilineTextWithBackground(Graphics2D g2, Font font, Color textColor,
                                                       String text, int x, int y,
                                                       Color bgColor, Color borderColor,
                                                       int cornerRadius, int padding) {
        if (g2 == null || text == null) return;

        FontMetrics metrics = g2.getFontMetrics(font != null ? font : g2.getFont());
        String[] lines = text.split("\n");
        if (lines.length == 0) return;

        // 计算多行文本最大宽度和总高度
        int maxLineWidth = 0;
        for (String line : lines) {
            maxLineWidth = Math.max(maxLineWidth, metrics.stringWidth(line));
        }
        int totalTextHeight = lines.length * metrics.getHeight();

        // 1. 绘制背景
        if (bgColor != null) {
            g2.setColor(bgColor);
            g2.fillRoundRect(
                    x - padding,
                    y - metrics.getAscent() - padding,
                    maxLineWidth + 2 * padding,
                    totalTextHeight + padding, // 总高+底部内边距
                    cornerRadius, cornerRadius
            );
        }

        // 2. 绘制边框
        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.drawRoundRect(
                    x - padding,
                    y - metrics.getAscent() - padding,
                    maxLineWidth + 2 * padding,
                    totalTextHeight + padding,
                    cornerRadius, cornerRadius
            );
        }

        // 3. 绘制每行文字
        g2.setColor(textColor != null ? textColor : Color.BLACK);
        if (font != null) g2.setFont(font);
        int currentY = y;
        for (String line : lines) {
            g2.drawString(line, x, currentY);
            currentY += metrics.getHeight(); // 下移一行高度
        }
    }

    /**
     * 绘制带渐变背景框的多行文本
     *
     * @param g2                 Graphics2D对象，用于绘制操作
     * @param font               文本字体，如果为null则使用Graphics2D当前字体
     * @param textColor          文本颜色，如果为null则使用黑色
     * @param text               要绘制的文本内容，支持换行符分隔多行
     * @param x                  文本绘制起始x坐标
     * @param y                  文本绘制起始y坐标（基准线）
     * @param startColor         渐变起始颜色
     * @param endColor           渐变结束颜色
     * @param isVerticalGradient true表示垂直渐变，false表示水平渐变
     * @param borderColor        边框颜色，如果为null则不绘制边框
     * @param cornerRadius       背景矩形圆角半径
     * @param padding            文本与背景框的内边距
     */
    public static void drawMultilineTextWithGradientBackground(Graphics2D g2, Font font, Color textColor,
                                                               String text, int x, int y,
                                                               Color startColor, Color endColor, boolean isVerticalGradient,
                                                               Color borderColor, int cornerRadius, int padding) {
        if (g2 == null || text == null || startColor == null || endColor == null) return;

        FontMetrics metrics = g2.getFontMetrics(font != null ? font : g2.getFont());
        String[] lines = text.split("\n");
        if (lines.length == 0) return;

        int maxLineWidth = 0;
        for (String line : lines) {
            maxLineWidth = Math.max(maxLineWidth, metrics.stringWidth(line));
        }
        int totalTextHeight = lines.length * metrics.getHeight();

        // 1. 绘制渐变背景
        GradientPaint gradientPaint;
        if (isVerticalGradient) {
            gradientPaint = new GradientPaint(x, y, startColor, x, y + totalTextHeight, endColor);
        } else {
            gradientPaint = new GradientPaint(x, y, startColor, x + maxLineWidth, y, endColor);
        }
        g2.setPaint(gradientPaint);
        g2.fillRoundRect(
                x - padding,
                y - metrics.getAscent() - padding,
                maxLineWidth + 2 * padding,
                totalTextHeight + padding,
                cornerRadius, cornerRadius
        );

        // 2. 绘制边框
        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.drawRoundRect(
                    x - padding,
                    y - metrics.getAscent() - padding,
                    maxLineWidth + 2 * padding,
                    totalTextHeight + padding,
                    cornerRadius, cornerRadius
            );
        }

        // 3. 绘制文字
        g2.setColor(textColor != null ? textColor : Color.BLACK);
        if (font != null) g2.setFont(font);
        int currentY = y;
        for (String line : lines) {
            g2.drawString(line, x, currentY);
            currentY += metrics.getHeight();
        }
    }

    /**
     * 绘制带阴影的文字
     *
     * @param g2            Graphics2D对象，用于绘制操作
     * @param font          文字字体，可为null
     * @param textColor     文字颜色，不可为null
     * @param shadowColor   阴影颜色，不可为null
     * @param text          要绘制的文本内容，不可为null
     * @param x             文字绘制的x坐标
     * @param y             文字绘制的y坐标
     * @param shadowOffsetX 阴影在x轴的偏移量
     * @param shadowOffsetY 阴影在y轴的偏移量
     */
    public static void drawTextWithShadow(Graphics2D g2, Font font, Color textColor, Color shadowColor,
                                          String text, int x, int y, int shadowOffsetX, int shadowOffsetY) {
        if (g2 == null || text == null || textColor == null || shadowColor == null) return;

        // 1. 先画阴影（偏移位置）
        g2.setColor(shadowColor);
        if (font != null) g2.setFont(font);
        g2.drawString(text, x + shadowOffsetX, y + shadowOffsetY);

        // 2. 再画文字（原位置）
        g2.setColor(textColor);
        g2.drawString(text, x, y);
    }

    // ---------------------- 高级文本绘制（整合所有样式） ----------------------

    /**
     * 高级多行文本绘制：自动换行 + 背景/渐变 + 边框 + 圆角 + 阴影 + 对齐
     *
     * @param g2                 Graphics2D对象，用于绘图操作
     * @param font               字体，若为null则使用Graphics2D当前字体
     * @param textColor          文字颜色，若为null默认黑色
     * @param text               待绘制的文本内容
     * @param maxWidth           单行最大宽度，超过将自动换行
     * @param canvasWidth        画布宽度，用于计算对齐和布局
     * @param canvasHeight       画布高度，用于垂直居中等布局计算
     * @param align              水平对齐方式（LEFT/CENTER/RIGHT）
     * @param isVerticalCenter   是否垂直居中显示文本区域
     * @param bgColor            背景颜色，若为null且无渐变则不绘制背景
     * @param gradientStart      渐变起始颜色，与gradientEnd同时非空时启用渐变背景
     * @param gradientEnd        渐变结束颜色
     * @param isVerticalGradient true表示纵向渐变，false表示横向渐变
     * @param borderColor        边框颜色，若为null则不绘制边框
     * @param cornerRadius       背景及边框圆角半径
     * @param padding            内边距（文本与背景边缘的距离）
     * @param shadowColor        阴影颜色，若为null则不绘制阴影
     * @param shadowOffsetX      阴影在X轴上的偏移量
     * @param shadowOffsetY      阴影在Y轴上的偏移量
     */
    public static void drawAdvancedText(Graphics2D g2, Font font, Color textColor,
                                        String text, int maxWidth, int canvasWidth, int canvasHeight,
                                        ImageCombiner.Align align, boolean isVerticalCenter,
                                        Color bgColor, Color gradientStart, Color gradientEnd, boolean isVerticalGradient,
                                        Color borderColor, int cornerRadius, int padding,
                                        Color shadowColor, int shadowOffsetX, int shadowOffsetY) {
        if (g2 == null || text == null) return;

        FontMetrics metrics = g2.getFontMetrics(font != null ? font : g2.getFont());
        // 1. 自动换行
        String[] lines = wrapText(metrics, text, maxWidth);
        if (lines.length == 0) return;

        // 2. 计算文本总宽高
        int totalTextWidth = 0;
        for (String line : lines) {
            totalTextWidth = Math.max(totalTextWidth, metrics.stringWidth(line));
        }
        int lineHeight = metrics.getHeight();
        int totalTextHeight = lines.length * lineHeight;

        // 3. 计算起始位置（X/Y）
        int startX = padding;
        int startY = padding + metrics.getAscent();

        // 水平对齐调整
        if (align == ImageCombiner.Align.CENTER) {
            startX = (canvasWidth - totalTextWidth) / 2;
        } else if (align == ImageCombiner.Align.RIGHT) {
            startX = canvasWidth - totalTextWidth - padding;
        }

        // 垂直居中调整
        if (isVerticalCenter) {
            int contentTotalHeight = totalTextHeight + 2 * padding; // 文本高+上下内边距
            startY = (canvasHeight - contentTotalHeight) / 2 + metrics.getAscent() + padding;
        }

        // 4. 绘制背景（纯色/渐变）
        if (bgColor != null || (gradientStart != null && gradientEnd != null)) {
            int bgX = startX - padding;
            int bgY = startY - metrics.getAscent() - padding;
            int bgWidth = totalTextWidth + 2 * padding;
            int bgHeight = totalTextHeight + padding;

            if (gradientStart != null && gradientEnd != null) {
                // 渐变背景
                GradientPaint gradientPaint = isVerticalGradient
                        ? new GradientPaint(bgX, bgY, gradientStart, bgX, bgY + bgHeight, gradientEnd)
                        : new GradientPaint(bgX, bgY, gradientStart, bgX + bgWidth, bgY, gradientEnd);
                g2.setPaint(gradientPaint);
            } else {
                // 纯色背景
                g2.setColor(bgColor);
            }
            g2.fillRoundRect(bgX, bgY, bgWidth, bgHeight, cornerRadius, cornerRadius);
        }

        // 5. 绘制边框
        if (borderColor != null) {
            int bgX = startX - padding;
            int bgY = startY - metrics.getAscent() - padding;
            int bgWidth = totalTextWidth + 2 * padding;
            int bgHeight = totalTextHeight + padding;
            g2.setColor(borderColor);
            g2.drawRoundRect(bgX, bgY, bgWidth, bgHeight, cornerRadius, cornerRadius);
        }

        // 6. 绘制每行文字（支持阴影）
        g2.setColor(textColor != null ? textColor : Color.BLACK);
        if (font != null) g2.setFont(font);
        int currentY = startY;

        for (String line : lines) {
            int lineX = startX;
            // 单行水平对齐（处理每行宽度不同的情况）
            if (align == ImageCombiner.Align.CENTER) {
                lineX = (canvasWidth - metrics.stringWidth(line)) / 2;
            } else if (align == ImageCombiner.Align.RIGHT) {
                lineX = canvasWidth - metrics.stringWidth(line) - padding;
            }

            // 绘制阴影（如果需要）
            if (shadowColor != null) {
                g2.setColor(shadowColor);
                g2.drawString(line, lineX + shadowOffsetX, currentY + shadowOffsetY);
                g2.setColor(textColor); // 恢复文字颜色
            }

            // 绘制文字
            g2.drawString(line, lineX, currentY);
            currentY += lineHeight; // 下移一行
        }
    }
}