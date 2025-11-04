package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.image.ImageIOUtils;
import io.github.kingprimes.utils.Fonts;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认菜单图片绘制工具类
 *
 * @author KingPrimes
 * @version 1.0.0
 */
final class DefaultDrawHelpImage {

    private static final Pattern PIPE_PATTERN = Pattern.compile("\\|");

    /**
     * 根据提供的帮助信息列表生成一张帮助图片，并以字节数组形式返回。
     *
     * @param helpInfo 包含菜单指令条目的字符串列表
     * @return 生成的帮助图片的 PNG 格式字节数组。
     */
    public static byte[] drawHelpImage(List<String> helpInfo) {
        final int WIDTH = DrawConstants.IMAGE_WIDTH;
        final int MARGIN = DrawConstants.IMAGE_MARGIN;
        final int HEADER_HEIGHT = DrawConstants.HELP_IMAGE_HEADER_HEIGHT;
        final int FONT_SIZE = Fonts.FONT_TEXT.getSize();
        final int ROW_HEIGHT = DrawConstants.HELP_IMAGE_ROW_HEIGHT;
        final int ITEMS_PER_COLUMN = DrawConstants.HELP_IMAGE_ITEMS_PER_COLUMN;

        // 预计算布局参数
        int columnCount = (helpInfo.size() + ITEMS_PER_COLUMN - 1) / ITEMS_PER_COLUMN;
        int maxItemsInColumn = (helpInfo.size() + columnCount - 1) / columnCount;
        int totalHeight = MARGIN + IMAGE_TITLE_HEIGHT + HEADER_HEIGHT + (maxItemsInColumn * ROW_HEIGHT) + MARGIN + IMAGE_FOOTER_HEIGHT;
        int rounderWidth = Math.multiplyExact(WIDTH, 9) / 10;
        int centerX = (WIDTH - rounderWidth) / 2;
        int columnWidth = rounderWidth / 2;  // 预计算列宽

        // 复用颜色对象
        Color evenRowColor = DrawConstants.HELP_IMAGE_EVEN_ROW_COLOR; // 更浅的灰白色背景
        Color textColor = DrawConstants.HELP_IMAGE_TEXT_COLOR;    // 深灰色文字，提高对比度
        Color headerBgColor = DrawConstants.HELP_IMAGE_HEADER_BG_COLOR; // 更深的蓝色表头
        Color titleColor = DrawConstants.HELP_IMAGE_TITLE_COLOR;   // 标题颜色保持一致

        ImageCombiner combiner = new ImageCombiner(WIDTH, totalHeight, ImageCombiner.OutputFormat.PNG);
        combiner.setFont(Fonts.FONT_TEXT)
                .setColor(Color.WHITE)
                .fillRect(0, 0, WIDTH, totalHeight).drawTooRoundRect();

        // 标题绘制
        String title = "帮助中心";
        combiner.setColor(titleColor)
                .addCenteredText(title, MARGIN + IMAGE_TITLE_HEIGHT / 2);

        // 表头绘制
        int y = MARGIN + IMAGE_TITLE_HEIGHT;
        ImageCombiner roundedCombiner = new ImageCombiner(rounderWidth, HEADER_HEIGHT, ImageCombiner.OutputFormat.PNG);
        roundedCombiner.setColor(headerBgColor)
                .fillRect(0, 0, rounderWidth, HEADER_HEIGHT)
                .combine();
        combiner.addRoundedImage(roundedCombiner.getCombinedImage(), centerX, y - 5, 25);
        combiner.setColor(Color.WHITE)
                .addCenteredText("指令", y += Fonts.FONT_TEXT.getSize() + 5);
        int imageY = y;

        // 预计算所有行的坐标和背景信息
        List<RowInfo> rowInfos = new ArrayList<>(helpInfo.size());
        int currentColumn;
        int currentRowInColumn;

        for (int i = 0; i < helpInfo.size(); i++) {
            // 计算当前行列位置
            currentColumn = i / ITEMS_PER_COLUMN;
            currentRowInColumn = i % ITEMS_PER_COLUMN;

            // 预计算坐标
            int rowX = centerX + (currentColumn * columnWidth);
            int rowY = y + (currentRowInColumn * ROW_HEIGHT) + ROW_HEIGHT;  // +ROW_HEIGHT是因为初始y已包含表头高度

            // 预解析数据
            String line = helpInfo.get(i);
            String[] parts = PIPE_PATTERN.split(line, 2);
            String command = parts.length > 0 ? parts[0] : "";

            RowInfo rowInfo = new RowInfo(rowX, rowY, currentRowInColumn % 2 == 0, command);
            rowInfos.add(rowInfo);
            if (rowInfo.isEvenRow) {
                combiner.setColor(evenRowColor);
                combiner.fillRect(rowInfo.x, rowInfo.y + (ROW_HEIGHT / 2) - (FONT_SIZE / 2), columnWidth, ROW_HEIGHT);
            }
        }

        // 2. 绘制背景图片
        BufferedImage backgroundImage = ImageIOUtils.getRandomXiaoMeiWangImage();
        int maxImageWidth = WIDTH / 2;
        int maxImageHeight = totalHeight - imageY - MARGIN;
        combiner.drawImageWithAspectRatio(backgroundImage, WIDTH / 2 + 40, imageY + MARGIN, maxImageWidth, maxImageHeight);

        // 3. 绘制所有数据行文字（批量操作）
        combiner.setColor(textColor);
        for (RowInfo info : rowInfos) {
            if (!info.command.isEmpty()) {
                combiner.addText(info.command, info.x, info.y);
            }
        }

        // 底部署名
        addFooter(combiner, totalHeight - IMAGE_FOOTER_HEIGHT);

        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }

    // 内部辅助类：缓存行信息（坐标、背景状态、文字内容）
    private record RowInfo(int x, int y, boolean isEvenRow, String command) {
    }

}
