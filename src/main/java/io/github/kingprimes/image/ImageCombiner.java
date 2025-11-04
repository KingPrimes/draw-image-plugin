package io.github.kingprimes.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 图片绘制组合器（建造者模式实现）
 * <p>核心职责：提供一站式图像构建能力，整合画布管理、基础图形绘制、文本处理（委托给{@link TextUtils}）、样式预设及最终图像输出流程</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class ImageCombiner {

    /**
     * 目标画布图像，所有绘制操作的最终载体（尺寸不可变）
     */
    protected final BufferedImage target;
    /**
     * 图形绘制上下文，提供基础绘制API（线条、形状、图像、文本等）
     */
    protected final Graphics2D g2;
    /**
     * 图像输出格式，决定最终生成图像类型（PNG/JPG）及透明度支持（PNG支持透明，JPG不支持）
     */
    protected final OutputFormat format;
    /**
     * 合并后图像的字节流缓存，需调用{@link #combine()}后才能通过{@link #getCombinedImageOutStream()}获取
     */
    protected ByteArrayOutputStream out;

    /**
     * 当前激活字体，用于文本绘制（null时使用g2默认字体）
     */
    protected Font currentFont;
    /**
     * 当前激活颜色，用于所有绘制操作（null时默认兜底为黑色）
     */
    protected Color currentColor;

    /**
     * 构造一个图像合成器实例
     *
     * @param image  目标画布图像，用于绘制和合成操作，不可为null
     * @param format 输出格式，指定图像的输出类型和相关配置，不可为null
     * @throws IllegalArgumentException 当image或format参数为null时抛出
     */
    public ImageCombiner(BufferedImage image, OutputFormat format) {
        if (image == null || format == null) {
            throw new IllegalArgumentException("画布和输出格式不可为null");
        }
        this.target = image;
        this.g2 = target.createGraphics();
        this.format = format;
        setQualityRenderingHints(g2); // 初始化高质量渲染
    }

    /**
     * 构造一个图像合成器实例
     *
     * @param width  目标图像的宽度，必须大于0
     * @param height 目标图像的高度，必须大于0
     * @param format 输出图像的格式，不能为null
     * @throws IllegalArgumentException 当width或height小于等于0，或者format为null时抛出
     */
    public ImageCombiner(int width, int height, OutputFormat format) {
        if (width <= 0 || height <= 0 || format == null) {
            throw new IllegalArgumentException("宽高需>0，输出格式不可为null");
        }
        this.target = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.g2 = target.createGraphics();
        this.format = format;
        g2.fillRect(0, 0, width, height);
        setQualityRenderingHints(g2); // 初始化高质量渲染
    }

    protected void setQualityRenderingHints(Graphics2D g2) {
        GraphicsUtils.setQualityRenderingHints(g2);
    }

    /**
     * 设置当前绘图颜色
     *
     * @param color 要设置的颜色，如果为null则使用黑色作为兜底颜色
     * @return 返回当前ImageCombiner实例，支持链式调用
     */
    public ImageCombiner setColor(Color color) {
        this.currentColor = color;
        this.g2.setColor(color != null ? color : Color.BLACK); // 兜底黑色
        return this;
    }

    /**
     * 在图像上绘制并填充一个矩形区域
     *
     * @param x      矩形左上角的 x 坐标
     * @param y      矩形左上角的 y 坐标
     * @param width  矩形的宽度
     * @param height 矩形的高度
     * @return 当前 ImageCombiner 实例，用于支持链式调用
     */
    public ImageCombiner fillRect(int x, int y, int width, int height) {
        g2.fillRect(x, y, width, height);
        return this;
    }

    /**
     * 在图像上绘制一个圆角矩形边框
     *
     * @param x         矩形左上角的 x 坐标
     * @param y         矩形左上角的 y 坐标
     * @param width     矩形的宽度
     * @param height    矩形的高度
     * @param arcWidth  圆角的宽度
     * @param arcHeight 圆角的高度
     * @return 当前 ImageCombiner 实例，用于支持链式调用
     */
    public ImageCombiner drawRoundRect(int x, int y, int width, int height,
                                       int arcWidth, int arcHeight) {
        g2.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
        return this;
    }

    /**
     * 在图像上绘制并填充一个圆角矩形区域
     *
     * @param x         矩形左上角的 x 坐标
     * @param y         矩形左上角的 y 坐标
     * @param width     矩形的宽度
     * @param height    矩形的高度
     * @param arcWidth  圆角的宽度
     * @param arcHeight 圆角的高度
     * @return 当前 ImageCombiner 实例，用于支持链式调用
     */
    public ImageCombiner fillRoundRect(int x, int y, int width, int height,
                                       int arcWidth, int arcHeight) {
        g2.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
        return this;
    }

    /**
     * 向图像绘制一条直线，支持方法链式调用
     *
     * @param x1 直线的起点X坐标
     * @param y1 直线的起点Y坐标
     * @param x2 直线的终点X坐标
     * @param y2 直线的终点Y坐标
     * @return 当前 ImageCombiner 实例，用于支持链式调用
     */
    public ImageCombiner drawLine(int x1, int y1, int x2, int y2) {
        g2.drawLine(x1, y1, x2, y2);
        return this;
    }

    /**
     * 在图像上绘制一个椭圆边框
     *
     * @param x      椭圆左上角的 x 坐标
     * @param y      椭圆左上角的 y 坐标
     * @param width  椭圆的宽度
     * @param height 椭圆的高度
     * @return 当前 ImageCombiner 实例，用于支持链式调用
     */
    public ImageCombiner drawOval(int x, int y, int width, int height) {
        g2.drawOval(x, y, width, height);
        return this;
    }

    /**
     * 在图像上绘制并填充一个椭圆区域
     *
     * @param x      椭圆左上角的 x 坐标
     * @param y      椭圆左上角的 y 坐标
     * @param width  椭圆的宽度
     * @param height 椭圆的高度
     * @return 当前 ImageCombiner 实例，用于支持链式调用
     */
    public ImageCombiner fillOval(int x, int y, int width, int height) {
        g2.fillOval(x, y, width, height);
        return this;
    }

    /**
     * 清除指定矩形区域内的像素
     *
     * @param x      矩形左上角的 x 坐标
     * @param y      矩形左上角的 y 坐标
     * @param width  矩形的宽度
     * @param height 矩形的高度
     * @return 当前 ImageCombiner 实例，用于支持链式调用
     */
    public ImageCombiner clearRect(int x, int y, int width, int height) {
        g2.clearRect(x, y, width, height);
        return this;
    }

    /**
     * 在图像上绘制圆弧轮廓（基于指定的外接矩形和角度范围）
     *
     * @param x          外接矩形左上角的 x 坐标
     * @param y          外接矩形左上角的 y 坐标
     * @param width      外接矩形的宽度
     * @param height     外接矩形的高度
     * @param startAngle 圆弧的起始角度（以度为单位，0 度对应 3 点钟方向，顺时针递增）
     * @param arcAngle   圆弧的角度范围（以度为单位，正值表示顺时针方向绘制）
     * @return 当前 ImageCombiner 实例，用于支持链式调用
     */
    public ImageCombiner drawArc(int x, int y, int width, int height,
                                 int startAngle, int arcAngle) {
        g2.drawArc(x, y, width, height, startAngle, arcAngle);
        return this;
    }

    /**
     * 填充一个弧形区域
     *
     * @param x          要填充的弧形左上角x坐标
     * @param y          要填充的弧形左上角y坐标
     * @param width      弧形的宽度
     * @param height     弧形的高度
     * @param startAngle 弧形的起始角度（以度为单位）
     * @param arcAngle   弧形的角度范围（以度为单位）
     * @return 返回当前ImageCombiner实例，支持链式调用
     */
    public ImageCombiner fillArc(int x, int y, int width, int height,
                                 int startAngle, int arcAngle) {
        g2.fillArc(x, y, width, height, startAngle, arcAngle);
        return this;
    }

    /**
     * 在图像上绘制一条折线（由多个连续线段组成的开放多边形）
     *
     * @param xPoints 折线顶点的 x 坐标数组
     * @param yPoints 折线顶点的 y 坐标数组
     * @param nPoints 折线的顶点数量（即数组中有效坐标对的数量）
     * @return 当前 ImageCombiner 实例，用于支持链式调用
     */
    public ImageCombiner drawPolyline(int[] xPoints, int[] yPoints,
                                      int nPoints) {
        g2.drawPolyline(xPoints, yPoints, nPoints);
        return this;
    }

    /**
     * 在图像上绘制多边形的轮廓（仅描边，不填充内部）
     *
     * @param xPoints 多边形顶点的 x 坐标数组
     * @param yPoints 多边形顶点的 y 坐标数组
     * @param nPoints 多边形的顶点数量（即数组中有效坐标对的数量）
     * @return 当前 ImageCombiner 实例，用于支持链式调用
     */
    public ImageCombiner drawPolygon(int[] xPoints, int[] yPoints,
                                     int nPoints) {
        g2.drawPolygon(xPoints, yPoints, nPoints);
        return this;
    }

    /**
     * 在图像上绘制并填充一个多边形区域
     *
     * @param xPoints 多边形顶点的 x 坐标数组
     * @param yPoints 多边形顶点的 y 坐标数组
     * @param nPoints 多边形的顶点数量（即数组中有效坐标对的数量）
     * @return 当前 ImageCombiner 实例，用于支持链式调用
     */
    public ImageCombiner fillPolygon(int[] xPoints, int[] yPoints,
                                     int nPoints) {
        g2.fillPolygon(xPoints, yPoints, nPoints);
        return this;
    }

    /**
     * 向图像绘制简单文本（无样式配置，需手动指定绘制坐标），支持方法链式调用
     * <p>
     * 文本绘制使用当前Graphics2D上下文的默认样式（字体、颜色等），若文本为null则不执行绘制操作
     * </p>
     * <p>
     * 注意：调用此方法前需先调用{@link #setColor(Color)} 设置颜色，否则是白色
     * </p>
     *
     * @param text 待绘制的文本内容（null时不绘制并直接返回）
     * @param x    文本绘制的X坐标（水平方向起点，基于Graphics2D坐标系）
     * @param y    文本绘制的Y坐标（垂直方向基线位置，基于Graphics2D坐标系）
     */
    public ImageCombiner addText(String text, int x, int y) {
        if (text == null)
            return this;
        g2.drawString(text, x, y);
        return this;
    }

    /**
     * 向图像添加指定形状，并设置填充颜色、边框颜色和边框宽度，支持方法链式调用
     * <p>
     * 实现逻辑：若形状为null则不执行绘制；否则通过{@link GraphicsUtils#drawShape(BufferedImage, Shape, Color, Color, int)}绘制形状，
     * 绘制完成后若当前颜色（{@code currentColor}）不为null，则恢复Graphics2D上下文的颜色为当前颜色，避免影响后续绘制操作
     * </p>
     *
     * @param shape       待绘制的形状对象（为null时不执行绘制并直接返回当前实例）
     * @param fillColor   形状内部填充颜色（可为null，表示不填充）
     * @param strokeColor 形状边框颜色（可为null，表示无边框）
     * @param strokeWidth 形状边框宽度（像素），仅在{@code strokeColor}不为null时有效
     * @return 当前ImageCombiner实例，支持后续方法链式调用（如继续添加其他图像元素）
     */
    public ImageCombiner addShape(Shape shape, Color fillColor, Color strokeColor, int strokeWidth) {
        if (shape == null)
            return this;
        GraphicsUtils.drawShape(target, shape, fillColor, strokeColor, strokeWidth);
        if (currentColor != null) {
            g2.setColor(currentColor);
        }
        return this;
    }

    /**
     * 创建指定尺寸和渐变效果的背景图片，支持横向或纵向渐变方向
     * <p>
     * 渐变方向规则：
     * </p>
     * <ul>
     * <li>当{@code vertical=true}时：从上到下垂直渐变（起点(0,0)到终点(0,height)）</li>
     * <li>当{@code vertical=false}时：从左到右水平渐变（起点(0,0)到终点(width,0)）</li>
     * </ul>
     *
     * @param width      背景图片宽度（像素）
     * @param height     背景图片高度（像素）
     * @param startColor 渐变起始颜色（渐变开始位置的颜色）
     * @param endColor   渐变结束颜色（渐变终止位置的颜色）
     * @param vertical   渐变方向标识：true表示垂直渐变，false表示水平渐变
     */
    public ImageCombiner createGradientBackground(int width, int height, Color startColor, Color endColor,
                                                  boolean vertical) {
        g2.drawImage(GraphicsUtils.createGradientBackground(width, height, startColor, endColor, vertical), 0, 0, null);
        return this;
    }

    /**
     * 向目标图像添加一个带有指定圆角半径的图片元素
     * <p>
     * 实现原理：调用{@link GraphicsUtils#createRoundedImage(BufferedImage, int)}方法将原图像裁剪为圆角形状，
     * 然后使用Graphics2D的{@code drawImage()}方法将圆角图像绘制到目标位置
     * </p>
     * <p>
     * 参数校验：若输入图像为null或圆角半径小于0，则直接返回当前实例，不执行任何绘制操作
     * </p>
     *
     * @param image        源图像对象（为null时不执行绘制并直接返回当前实例）
     * @param x            图像左上角的X坐标（基于Graphics2D坐标系）
     * @param y            图像左上角的Y坐标（基于Graphics2D坐标系）
     * @param cornerRadius 圆角半径（像素），值小于0时不执行绘制并直接返回当前实例
     * @return 当前ImageCombiner实例，支持后续方法链式调用（如继续添加其他图像元素）
     */
    public ImageCombiner addRoundedImage(BufferedImage image, int x, int y, int cornerRadius) {
        if (image == null || cornerRadius < 0)
            return this;
        BufferedImage roundedImage = GraphicsUtils.createRoundedImage(image, cornerRadius);
        g2.drawImage(roundedImage, x, y, null);
        return this;
    }

    /**
     * 计算单行文本在指定宽度画布上的水平居中X坐标
     * <p>
     * 计算逻辑：通过画布宽度减去文本宽度的差值除以2，得到文本左边缘的起始X坐标，使文本在水平方向居中
     * </p>
     * <p>
     * 特殊处理：若{@code text}为null，直接返回0以避免空指针异常
     * </p>
     *
     * @param text 待居中的单行文本内容（文本宽度将用于居中计算）
     * @return 文本水平居中时的左边缘X坐标（像素）；若输入参数无效（metrics或text为null）则返回0
     */
    protected int calculateCenterX(String text) {
        FontMetrics metrics = g2.getFontMetrics(currentFont != null ? currentFont : g2.getFont());
        return TextUtils.calculateCenterX(metrics, text, target.getWidth());
    }

    /**
     * 计算单行文字在图像水平方向的居中X坐标，并支持X轴方向的偏移调整
     * <p>
     * 实现逻辑：通过字体度量信息（FontMetrics）获取文本宽度，结合图像目标宽度和X轴偏移量，
     * 调用{@link TextUtils#calculateCenterX(FontMetrics, String, int, int)}完成坐标计算
     * </p>
     * <p>
     * 字体优先级：优先使用当前已设置的{@code currentFont}，若未设置则使用Graphics2D上下文的默认字体
     * </p>
     *
     * @param text    待计算居中位置的单行文本内容（文本宽度将用于居中计算）
     * @param xOffset X轴方向的偏移量（像素），正值表示向右偏移，负值表示向左偏移
     * @return 水平居中且应用偏移后的文本绘制X坐标（基于Graphics2D坐标系）
     */
    protected int calculateCenterX(String text, int xOffset) {
        FontMetrics metrics = g2.getFontMetrics(currentFont != null ? currentFont : g2.getFont());
        return TextUtils.calculateCenterX(metrics, text, target.getWidth(), xOffset);
    }

    /**
     * 向图像添加单行文本并使其在X轴和Y轴方向完全居中显示，支持方法链式调用
     * <p>
     * 注意：调用此方法前需先调用{@link #setColor(Color)} 设置颜色，否则是白色
     * </p>
     * <p>
     * <strong>注意：</strong>该方法仅支持单行文本居中，若文本包含换行符（\\n）可能导致居中位置计算偏差
     * </p>
     *
     * @param text 待居中绘制的单行文本内容（null时不绘制并直接返回当前实例）
     * @return 当前ImageCombiner实例，支持后续方法链式调用（如继续添加其他元素）
     */
    protected Point calculateCenterXY(String text) {
        FontMetrics metrics = g2.getFontMetrics(currentFont != null ? currentFont : g2.getFont());
        return TextUtils.calculateCenterXY(metrics, text, target.getWidth(), target.getHeight());
    }

    /**
     * 计算单行文字完全居中（X、Y轴）的坐标（带偏移量）
     * <p>实现原理：通过Graphics2D获取字体度量信息，调用TextUtils工具类计算文本在图像中的居中坐标，
     * 并应用指定的X轴和Y轴偏移量</p>
     * <p>字体优先级：优先使用当前已设置的{@code currentFont}，若未设置则使用Graphics2D上下文的默认字体</p>
     *
     * @param text    待计算居中位置的单行文本内容
     * @param xOffset X轴方向的偏移量（像素），正值表示向右偏移，负值表示向左偏移
     * @param yOffset Y轴方向的偏移量（像素），正值表示向下偏移，负值表示向上偏移
     * @return 文本水平和垂直居中且应用偏移后的坐标点；若输入参数无效则返回默认坐标
     */
    protected Point calculateCenterXY(String text, int xOffset, int yOffset) {
        FontMetrics metrics = g2.getFontMetrics(currentFont != null ? currentFont : g2.getFont());
        return TextUtils.calculateCenterXY(metrics, text, target.getWidth(), target.getHeight(), xOffset, yOffset);
    }

    /**
     * 向图像添加水平居中的单行文字（指定Y坐标）
     * <p>实现逻辑：先计算文本水平居中时的X坐标，然后调用{@link #addText(String, int, int)}方法绘制文本</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text 待绘制的单行文本内容（null时不绘制并直接返回当前实例）
     * @param y    文本基线的Y坐标（基于Graphics2D坐标系）
     * @return 当前ImageCombiner实例，支持后续方法链式调用
     */
    public ImageCombiner addCenteredText(String text, int y) {
        if (text == null)
            return this;
        int centerX = calculateCenterX(text);
        return addText(text, centerX, y);
    }

    /**
     * 添加水平居中的单行文字（指定Y坐标，带X偏移）
     * <p>实现逻辑：先计算文本水平居中时的X坐标（考虑X轴偏移量），然后调用{@link #addText(String, int, int)}方法绘制文本</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text    待绘制的单行文本内容（null时不绘制并直接返回当前实例）
     * @param y       文本基线的Y坐标（基于Graphics2D坐标系）
     * @param xOffset X轴方向的偏移量（像素），正值表示向右偏移，负值表示向左偏移
     * @return 当前ImageCombiner实例，支持后续方法链式调用
     */
    public ImageCombiner addCenteredXText(String text, int y, int xOffset) {
        if (text == null)
            return this;
        int centerX = calculateCenterX(text, xOffset);
        return addText(text, centerX, y);
    }

    /**
     * 添加完全居中（X、Y轴）的单行文字
     * <p>实现逻辑：先计算文本水平和垂直居中时的坐标，然后调用{@link #addText(String, int, int)}方法绘制文本</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text 待绘制的单行文本内容（null时不绘制并直接返回当前实例）
     * @return 当前ImageCombiner实例，支持后续方法链式调用
     */
    public ImageCombiner addCenteredText(String text) {
        if (text == null)
            return this;
        Point center = calculateCenterXY(text);
        return addText(text, center.x, center.y);
    }

    /**
     * 添加完全居中（X、Y轴）的单行文字（带偏移）
     * <p>实现逻辑：先计算文本水平和垂直居中时的坐标（考虑X轴和Y轴偏移量），然后调用{@link #addText(String, int, int)}方法绘制文本</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text    待绘制的单行文本内容（null时不绘制并直接返回当前实例）
     * @param xOffset X轴方向的偏移量（像素），正值表示向右偏移，负值表示向左偏移
     * @param yOffset Y轴方向的偏移量（像素），正值表示向下偏移，负值表示向上偏移
     * @return 当前ImageCombiner实例，支持后续方法链式调用
     */
    public ImageCombiner addCenteredXYText(String text, int xOffset, int yOffset) {
        if (text == null)
            return this;
        Point center = calculateCenterXY(text, xOffset, yOffset);
        return addText(text, center.x, center.y);
    }

    /**
     * 添加多行居中文字（手动换行\n，指定起始Y、行高、行间距）
     * <p>实现逻辑：先将输入文本按换行符分割为多行，然后计算每行的水平居中位置（考虑X轴偏移量），最后调用{@link #addText(String, int, int)}方法绘制文本</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text       待绘制的多行文本内容（null时不绘制并直接返回当前实例），支持手动换行（\n）
     * @param startY     第一行文字基线的起始Y坐标（基于Graphics2D坐标系）
     * @param lineHeight 每一行文字的高度（像素）
     * @param spacing    每一行文字之间的垂直间距（像素）
     * @return 当前ImageCombiner实例，支持后续方法链式调用
     */
    public ImageCombiner addMultilineCenteredText(String text, int startY, int lineHeight, int spacing) {
        if (text == null)
            return this;
        String[] lines = text.split("\n");
        int currentY = startY;

        for (String line : lines) {
            int centerX = calculateCenterX(line);
            addText(line, centerX, currentY);
            currentY += lineHeight + spacing;
        }
        return this;
    }

    /**
     * 添加多行居中文字（自动垂直居中，指定行高、行间距）
     * <p>实现逻辑：先将输入文本按换行符分割为多行，然后根据当前Graphics2D上下文的字体和行高、间距计算总高度，最后调用{@link #addMultilineCenteredText(String, int, int, int)}方法绘制文本</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text       待绘制的多行文本内容（null时不绘制并直接返回当前实例），支持手动换行（\n）
     * @param lineHeight 每一行文字的高度（像素）
     * @param spacing    每一行文字之间的垂直间距（像素）
     * @return 当前ImageCombiner实例，支持后续方法链式调用
     */
    public ImageCombiner addMultilineCenteredText(String text, int lineHeight, int spacing) {
        if (text == null)
            return this;
        String[] lines = text.split("\n");
        FontMetrics metrics = g2.getFontMetrics(currentFont != null ? currentFont : g2.getFont());
        // 计算总高度（用TextUtils工具方法）
        int totalHeight = TextUtils.calculateMultilineHeight(metrics, lines, spacing);
        // 垂直居中起始Y
        int startY = (target.getHeight() - totalHeight) / 2 + lineHeight;
        return addMultilineCenteredText(text, startY, lineHeight, spacing);
    }

    /**
     * 添加多行居中文字（自动垂直居中，默认行高和间距）
     * <p>实现逻辑：调用{@link #addMultilineCenteredText(String, int, int)}方法，默认行高=字体高，间距=5</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text 待绘制的多行文本内容（null时不绘制并直接返回当前实例），支持手动换行（\n）
     * @return 当前ImageCombiner实例，支持后续方法链式调用
     */
    public ImageCombiner addMultilineCenteredText(String text) {
        FontMetrics metrics = g2.getFontMetrics(currentFont != null ? currentFont : g2.getFont());
        return addMultilineCenteredText(text, metrics.getHeight(), 5); // 默认行高=字体高，间距=5
    }

    /**
     * 绘制带背景框的单行文字
     * <p>实现逻辑：调用{@link TextUtils#drawTextWithBackground(Graphics2D, Font, Color, String, int, int, Color, Color, int, int)}方法绘制文本，同时绘制背景框</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text         待绘制的单行文本内容（null时不绘制并直接返回当前实例）
     * @param x            文字基线的X坐标（基于Graphics2D坐标系）
     * @param y            文字基线的Y坐标（基于Graphics2D坐标系）
     * @param bgColor      背景框的颜色（null时不绘制背景框）
     * @param borderColor  边框的颜色（null时不绘制边框）
     * @param cornerRadius 圆角半径（像素），0表示不圆角
     * @param padding      文字与背景框的内边距（像素）
     * @return 当前ImageCombiner实例，支持后续方法链式调用
     */
    public ImageCombiner addTextWithBackground(String text, int x, int y,
                                               Color bgColor, Color borderColor,
                                               int cornerRadius, int padding) {
        TextUtils.drawTextWithBackground(
                g2, currentFont, currentColor,
                text, x, y,
                bgColor, borderColor,
                cornerRadius, padding);
        return this;
    }

    /**
     * 绘制带背景框的居中单行文字
     * <p>实现逻辑：调用{@link TextUtils#drawTextWithBackground(Graphics2D, Font, Color, String, int, int, Color, Color, int, int)}方法绘制文本，同时绘制背景框</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text         待绘制的单行文本内容（null时不绘制并直接返回当前实例）
     * @param bgColor      背景框的颜色（null时不绘制背景框）
     * @param borderColor  边框的颜色（null时不绘制边框）
     * @param cornerRadius 圆角半径（像素），0表示不圆角
     * @param padding      文字与背景框的内边距（像素）
     * @return 当前ImageCombiner实例，支持后续方法链式调用
     */
    public ImageCombiner addCenteredTextWithBackground(String text,
                                                       Color bgColor, Color borderColor,
                                                       int cornerRadius, int padding) {
        if (text == null)
            return this;
        FontMetrics metrics = g2.getFontMetrics(currentFont != null ? currentFont : g2.getFont());
        // 计算居中坐标
        int centerX = TextUtils.calculateCenterX(metrics, text, target.getWidth());
        int centerY = (target.getHeight() + metrics.getAscent() - metrics.getDescent()) / 2;

        // 调用TextUtils绘制
        TextUtils.drawTextWithBackground(
                g2, currentFont, currentColor,
                text, centerX, centerY,
                bgColor, borderColor,
                cornerRadius, padding);
        return this;
    }

    /**
     * 绘制带背景框的多行文字（手动换行\n）
     * <p>实现逻辑：调用{@link TextUtils#drawMultilineTextWithBackground(Graphics2D, Font, Color, String, int, int, Color, Color, int, int)}方法绘制文本，同时绘制背景框</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text         待绘制的多行文本内容（null时不绘制并直接返回当前实例），支持手动换行（\n）
     * @param x            文字基线的X坐标（基于Graphics2D坐标系）
     * @param y            文字基线的Y坐标（基于Graphics2D坐标系）
     * @param bgColor      背景框的颜色（null时不绘制背景框）
     * @param borderColor  边框的颜色（null时不绘制边框）
     * @param cornerRadius 圆角半径（像素），0表示不圆角
     * @param padding      文字与背景框的内边距（像素）
     */
    public ImageCombiner addMultilineTextWithBackground(String text, int x, int y,
                                                        Color bgColor, Color borderColor,
                                                        int cornerRadius, int padding) {
        TextUtils.drawMultilineTextWithBackground(
                g2, currentFont, currentColor,
                text, x, y,
                bgColor, borderColor,
                cornerRadius, padding);
        return this;
    }

    /**
     * 绘制带渐变背景框的多行文字（手动换行\n）
     * <p>实现逻辑：调用{@link TextUtils#drawMultilineTextWithGradientBackground(Graphics2D, Font, Color, String, int, int, Color, Color, boolean, Color, int, int)}方法绘制文本，同时绘制渐变背景框</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text         待绘制的多行文本内容（null时不绘制并直接返回当前实例），支持手动换行（\n）
     * @param x            文字基线的X坐标（基于Graphics2D坐标系）
     * @param y            文字基线的Y坐标（基于Graphics2D坐标系）
     * @param startColor   渐变背景的起始颜色（null时不绘制渐变背景）
     * @param endColor     渐变背景的结束颜色（null时不绘制渐变背景）
     * @param vertical     是否垂直渐变（true：垂直；false：水平）
     * @param borderColor  边框的颜色（null时不绘制边框）
     * @param cornerRadius 圆角半径（像素），0表示不圆角
     * @param padding      文字与背景框的内边距（像素）
     */
    public ImageCombiner addMultilineTextWithGradientBackground(String text, int x, int y,
                                                                Color startColor, Color endColor, boolean vertical,
                                                                Color borderColor, int cornerRadius, int padding) {
        TextUtils.drawMultilineTextWithGradientBackground(
                g2, currentFont, currentColor,
                text, x, y,
                startColor, endColor, vertical,
                borderColor, cornerRadius, padding);
        return this;
    }

    /**
     * 绘制带阴影的文字
     * <p>实现逻辑：调用{@link TextUtils#drawTextWithShadow(Graphics2D, Font, Color, Color, String, int, int, int, int)}方法绘制文本，同时绘制阴影</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text          待绘制的文本内容（null时不绘制并直接返回当前实例）
     * @param x             文字基线的X坐标（基于Graphics2D坐标系）
     * @param y             文字基线的Y坐标（基于Graphics2D坐标系）
     * @param shadowColor   阴影颜色（null时不绘制阴影）
     * @param shadowOffsetX 阴影水平偏移量（像素）
     * @param shadowOffsetY 阴影垂直偏移量（像素）
     */
    public ImageCombiner addTextWithShadow(String text, int x, int y,
                                           Color shadowColor, int shadowOffsetX, int shadowOffsetY) {
        TextUtils.drawTextWithShadow(
                g2, currentFont, currentColor,
                shadowColor, text, x, y,
                shadowOffsetX, shadowOffsetY);
        return this;
    }

    /**
     * 自动换行文本（根据最大宽度拆分）
     * <p>实现逻辑：调用{@link TextUtils#wrapText(FontMetrics, String, int)}方法自动换行文本，根据最大宽度拆分</p>
     * <p>参数校验：若输入文本为null或最大宽度不大于0，则返回包含空字符串的数组（防止空指针异常）</p>
     *
     * @param text     待自动换行的文本内容（null时返回空字符串数组）
     * @param maxWidth 最大宽度（像素），超过此宽度的文本会自动换行
     * @return 自动换行后的字符串数组，每个元素为一行文本
     */
    public String[] wrapText(String text, int maxWidth) {
        if (text == null || maxWidth <= 0)
            return new String[]{text == null ? "" : text};
        FontMetrics metrics = g2.getFontMetrics(currentFont != null ? currentFont : g2.getFont());
        return TextUtils.wrapText(metrics, text, maxWidth);
    }

    /**
     * 高级多行文本绘制（整合：自动换行+背景/渐变+边框+圆角+阴影+对齐）
     * <p>参数校验：若输入文本为null或最大宽度不大于0，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text             待绘制的多行文本内容（null时不绘制并直接返回当前实例），支持手动换行（\n）
     * @param maxWidth         最大宽度（像素），超过此宽度的文本会自动换行
     * @param align            对齐方式（LEFT/CENTER/RIGHT）
     * @param vCenter          是否垂直居中（true：垂直居中；false：顶部对齐）
     * @param bgColor          背景颜色（null时不绘制背景）
     * @param gradientStart    渐变背景的起始颜色（null时不绘制渐变背景）
     * @param gradientEnd      渐变背景的结束颜色（null时不绘制渐变背景）
     * @param gradientVertical 是否垂直渐变（true：垂直；false：水平）
     * @param borderColor      边框的颜色（null时不绘制边框）
     * @param cornerRadius     圆角半径（像素），0表示不圆角
     * @param padding          文字与背景框的内边距（像素）
     */
    public ImageCombiner drawAdvancedText(String text, int maxWidth, Align align, boolean vCenter,
                                          Color bgColor, Color gradientStart, Color gradientEnd, boolean gradientVertical,
                                          Color borderColor, int cornerRadius, int padding,
                                          Color shadowColor, int shadowOffsetX, int shadowOffsetY) {
        TextUtils.drawAdvancedText(
                g2, currentFont, currentColor,
                text, maxWidth, target.getWidth(), target.getHeight(),
                align, vCenter,
                bgColor, gradientStart, gradientEnd, gradientVertical,
                borderColor, cornerRadius, padding,
                shadowColor, shadowOffsetX, shadowOffsetY);
        return this;
    }

    /**
     * 绘制双层边框
     *
     * @param roundRect 参数
     * @return 当前实例
     */
    public ImageCombiner drawTooRoundRect(RoundRect roundRect) {
        this.setColor(new Color(0xB1B1B1))
                .setStroke(roundRect.stroke)
                .drawRoundRect(roundRect.x, roundRect.y, roundRect.width, roundRect.height, roundRect.arcWidth, roundRect.arcHeight);

        // 内层边框（深灰色）
        this.setColor(new Color(0x333333))
                .setStroke(roundRect.stroke)
                .drawRoundRect(roundRect.offsetX, roundRect.offsetY, roundRect.offsetWidth, roundRect.offsetHeight, roundRect.arcWidth, roundRect.arcHeight);
        return this;
    }

    /**
     * 绘制双层边框
     *
     * @return 当前实例
     */
    public ImageCombiner drawTooRoundRect() {
        int borderPadding = 20;
        int innerWidth = this.getCombinedImage().getWidth() - borderPadding * 2;
        int innerHeight = this.getCombinedImage().getHeight() - borderPadding * 2;
        this.drawTooRoundRect(new RoundRect(
                borderPadding - 8,
                borderPadding,
                borderPadding - 8,
                borderPadding,
                innerWidth + 16,
                innerWidth,
                innerHeight + 16,
                innerHeight,
                20,
                20,
                4
        ));
        return this;
    }

    /**
     * 预设标题样式：居中+渐变背景+白色边框+阴影
     * <p>实现逻辑：调用{@link #drawAdvancedText(String, int, Align, boolean, Color, Color, Color, boolean, Color, int, int, Color, int, int)}方法绘制文本，同时绘制渐变背景+白色边框+阴影</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text 待绘制的标题文本内容（null时不绘制并直接返回当前实例）
     */
    public ImageCombiner drawTitle(String text) {
        return drawAdvancedText(
                text,
                target.getWidth() - 40, // 左右留边20，最大宽度=画布宽-40
                Align.CENTER, true, // 水平居中+垂直居中
                null, // 不使用纯色背景
                new Color(70, 130, 180), // 渐变起始色（SteelBlue）
                new Color(100, 149, 237), // 渐变结束色（CornflowerBlue）
                false, // 水平渐变
                Color.WHITE, // 边框色（白色）
                15, 15, // 圆角15+内边距15
                new Color(0, 0, 0, 100), // 阴影色（半透明黑）
                1, 1 // 阴影偏移（1,1）
        );
    }

    /**
     * 预设正文样式：左对齐+白底+灰色细边框
     * <p>实现逻辑：调用{@link #drawAdvancedText(String, int, Align, boolean, Color, Color, Color, boolean, Color, int, int, Color, int, int)}方法绘制文本，同时绘制白底+灰色细边框</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text 待绘制的正文文本内容（null时不绘制并直接返回当前实例）
     */
    public ImageCombiner drawContent(String text) {
        return drawAdvancedText(
                text,
                target.getWidth() - 40, // 左右留边20
                Align.LEFT, false, // 左对齐+不垂直居中
                Color.WHITE, // 白底
                null, null, false, // 无渐变
                new Color(200, 200, 200), // 边框色（浅灰）
                8, 10, // 圆角8+内边距10
                null, 0, 0 // 无阴影
        );
    }

    /**
     * 预设提示框样式：左对齐+黄色背景+橙色边框+感叹号图标
     * <p>实现逻辑：调用{@link #drawAdvancedText(String, int, Align, boolean, Color, Color, Color, boolean, Color, int, int, Color, int, int)}方法绘制文本，同时绘制黄色背景+橙色边框+感叹号图标</p>
     * <p>参数校验：若输入文本为null，则直接返回当前实例，不执行任何绘制操作</p>
     *
     * @param text 待绘制的提示框文本内容（null时不绘制并直接返回当前实例）
     */
    public ImageCombiner drawHintBox(String text) {
        // 1. 绘制提示框文本（调用高级绘制）
        drawAdvancedText(
                text,
                target.getWidth() - 80, // 左右留边40
                Align.LEFT, false, // 左对齐+不垂直居中
                new Color(255, 255, 204), // 背景色（浅黄）
                null, null, false, // 无渐变
                new Color(255, 204, 0), // 边框色（橙色）
                10, 15, // 圆角10+内边距15
                null, 0, 0 // 无阴影
        );

        // 2. 绘制左上角感叹号图标（非文字相关，本地实现）
        g2.setColor(new Color(255, 204, 0)); // 图标色（橙色）
        g2.fillOval(0, 0, 20, 20); // 圆形背景
        g2.setColor(Color.WHITE); // 文字色（白色）
        g2.setFont(new Font("Arial", Font.BOLD, 16)); // 图标字体
        g2.drawString("!", 8, 15); // 绘制感叹号

        // 恢复原字体和颜色
        if (currentFont != null)
            g2.setFont(currentFont);
        if (currentColor != null)
            g2.setColor(currentColor);

        return this;
    }

    /**
     * 合并所有绘制操作（生成最终图片）
     * <p>实现逻辑：根据指定格式（PNG/JPG）将当前画布内容写入输出流</p>
     * <p>参数校验：无</p>
     *
     * @throws RuntimeException 若图片合并过程中发生I/O错误
     */
    public void combine() {
        try {
            out = new ByteArrayOutputStream();
            // 根据格式写入输出流（PNG支持透明，JPG不支持）
            String formatStr = format == OutputFormat.PNG ? "png" : "jpg";
            ImageIO.write(target, formatStr, out);
        } catch (IOException e) {
            throw new RuntimeException("图片合并失败: " + e.getMessage(), e);
        } finally {
            g2.dispose(); // 释放画笔资源
        }
    }

    public BufferedImage getCombinedImage() {
        return target;
    }

    /**
     * 获取合并后的图片字节流（需先调用combine()）
     * <p>实现逻辑：返回包含合并后图片数据的字节数组输出流</p>
     * <p>参数校验：无</p>
     *
     * @return 包含合并后图片数据的字节数组输出流
     * @throws IllegalStateException 若未先调用combine()方法生成图片
     */
    public ByteArrayOutputStream getCombinedImageOutStream() {
        if (out == null) {
            throw new IllegalStateException("请先调用 combine() 方法生成图片");
        }
        return out;
    }

    /**
     * 获取字符串宽度（根据当前字体）
     * <p>实现逻辑：调用Graphics2D的fontMetrics.getStringBounds方法获取字符串边界框，返回其宽度（取整）</p>
     * <p>参数校验：无</p>
     *
     * @param text 待获取宽度的字符串内容
     * @return 字符串在当前字体下的宽度（像素）
     */
    public int getStringWidth(String text) {
        return (int) Math.ceil(g2.getFontMetrics().getStringBounds(text, g2).getWidth());
    }

    /**
     * 获取指定字体的字体度量信息
     *
     * @param font 要获取度量信息的字体对象
     * @return 返回指定字体的FontMetrics对象，包含字体的尺寸信息
     */
    public FontMetrics getFontMetrics(Font font) {
        g2.setFont(font);
        return g2.getFontMetrics();
    }

    /**
     * 获取当前图形上下文的字体度量信息
     *
     * @return FontMetrics对象，包含当前字体的尺寸和布局信息
     */
    public FontMetrics getFontMetrics() {
        return g2.getFontMetrics();
    }

    /**
     * 获取当前画布宽度
     *
     * @return 当前画布的宽度（像素）
     */
    public int getCanvasWidth() {
        return target.getWidth();
    }

    /**
     * 获取当前画布高度
     *
     * @return 当前画布的高度（像素）
     */
    public int getCanvasHeight() {
        return target.getHeight();
    }

    /**
     * 设置当前绘图的笔画宽度
     *
     * @param width 笔画宽度（像素）
     * @return 返回当前 ImageCombiner 实例，支持链式调用
     */
    public ImageCombiner setStroke(int width) {
        g2.setStroke(new BasicStroke(width));
        return this;
    }

    /**
     * 获取当前字体对象
     *
     * @return 返回当前设置的字体对象
     */
    public Font getFont() {
        return currentFont;
    }

    /**
     * 设置当前使用的字体
     *
     * @param font 要设置的字体对象，如果为null则使用图形上下文的默认字体
     * @return 返回当前ImageCombiner实例，支持链式调用
     */
    public ImageCombiner setFont(Font font) {
        this.currentFont = font;
        this.g2.setFont(font != null ? font : g2.getFont());
        return this;
    }

    /**
     * 在当前图形上下文中绘制指定的图片到指定位置
     *
     * @param back 要绘制的图片对象
     * @param x    图片绘制位置的x坐标
     * @param y    图片绘制位置的y坐标
     * @return 返回当前ImageCombiner实例，支持链式调用
     */
    public ImageCombiner drawImage(BufferedImage back, int x, int y) {
        g2.drawImage(back, x, y, null);
        return this;
    }

    /**
     * 在指定位置绘制图像
     *
     * @param img    要绘制的图像对象
     * @param x      图像绘制的x坐标位置
     * @param y      图像绘制的y坐标位置
     * @param width  图像绘制的宽度
     * @param height 图像绘制的高度
     * @return 返回当前ImageCombiner实例，支持链式调用
     */
    public ImageCombiner drawImage(Image img, int x, int y,
                                   int width, int height) {
        g2.drawImage(img, x, y, width, height, null);
        return this;
    }

    /**
     * 获取当前Graphics2D对象
     *
     * @return 当前Graphics2D对象
     */
    public Graphics2D getGraphics() {
        return g2;
    }

    /**
     * 在指定位置绘制图像，保持图像原始比例进行缩放
     *
     * @param img       要绘制的图像对象
     * @param x         图像绘制的x坐标位置
     * @param y         图像绘制的y坐标位置
     * @param maxWidth  图像绘制的最大宽度
     * @param maxHeight 图像绘制的最大高度
     * @return 返回当前ImageCombiner实例，支持链式调用
     */
    public ImageCombiner drawImageWithAspectRatio(Image img, int x, int y, int maxWidth, int maxHeight) {
        if (img == null) {
            return this;
        }

        int imgWidth = img.getWidth(null);
        int imgHeight = img.getHeight(null);

        // 计算缩放比例
        double scale = Math.min((double) maxWidth / imgWidth, (double) maxHeight / imgHeight);

        // 计算缩放后的尺寸
        int scaledWidth = (int) (imgWidth * scale);
        int scaledHeight = (int) (imgHeight * scale);

        // 居中放置
        int drawX = x + (maxWidth - scaledWidth) / 2;
        int drawY = y + (maxHeight - scaledHeight) / 2;

        // 绘制图像
        g2.drawImage(img, drawX, drawY, scaledWidth, scaledHeight, null);
        return this;
    }


    /**
     * 文本对齐方式
     */
    public enum Align {
        /**
         * 文本左对齐
         */
        LEFT,
        /**
         * 文本居中对齐
         */
        CENTER,
        /**
         * 文本右对齐
         */
        RIGHT
    }

    /**
     * 图片输出格式
     */
    public enum OutputFormat {
        /**
         * PNG格式
         */
        PNG,
        /**
         * JPG格式
         */
        JPG
    }

    /**
     * 双层边框参数
     *
     * @param x            外层边框的x坐标
     * @param offsetX      内层边框的x坐标
     * @param y            外层边框的y坐标
     * @param offsetY      内层边框的y坐标
     * @param width        外层边框的宽度
     * @param offsetWidth  内层边框的宽度
     * @param height       外层边框的高度
     * @param offsetHeight 内层边框的高度
     * @param arcWidth     圆角的宽度
     * @param arcHeight    圆角的高度
     * @param stroke       边框的宽度
     */
    public record RoundRect(int x, int offsetX, int y, int offsetY, int width, int offsetWidth, int height,
                            int offsetHeight, int arcWidth, int arcHeight, int stroke) {
    }
}