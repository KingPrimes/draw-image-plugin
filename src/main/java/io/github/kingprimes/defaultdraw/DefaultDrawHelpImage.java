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

        // 预计算布局参数
        int columnCount = (helpInfo.size() + HELP_IMAGE_ITEMS_PER_COLUMN - 1) / HELP_IMAGE_ITEMS_PER_COLUMN;
        int maxItemsInColumn = (helpInfo.size() + columnCount - 1) / columnCount;
        int totalHeight = IMAGE_MARGIN + IMAGE_TITLE_HEIGHT + IMAGE_HEADER_HEIGHT + (maxItemsInColumn * HELP_IMAGE_ROW_HEIGHT) + IMAGE_MARGIN + IMAGE_FOOTER_HEIGHT;
        int rounderWidth = Math.multiplyExact(IMAGE_WIDTH, 9) / 10;
        int centerX = (IMAGE_WIDTH - rounderWidth) / 2;
        int columnWidth = rounderWidth / 2;  // 预计算列宽


        ImageCombiner combiner = new ImageCombiner(IMAGE_WIDTH, totalHeight, ImageCombiner.OutputFormat.PNG);
        combiner.setFont(Fonts.FONT_TEXT)
                .setColor(Color.WHITE)
                .fillRect(0, 0, IMAGE_WIDTH, totalHeight).drawTooRoundRect();

        // 标题绘制
        String title = "帮助中心";
        combiner.setColor(HELP_IMAGE_TITLE_COLOR)
                .addCenteredText(title, IMAGE_MARGIN + IMAGE_TITLE_HEIGHT / 2);

        // 表头绘制
        int y = IMAGE_MARGIN + IMAGE_TITLE_HEIGHT;
        ImageCombiner roundedCombiner = new ImageCombiner(rounderWidth, IMAGE_HEADER_HEIGHT, ImageCombiner.OutputFormat.PNG);
        roundedCombiner.setColor(HELP_IMAGE_HEADER_BG_COLOR)
                .fillRect(0, 0, rounderWidth, IMAGE_HEADER_HEIGHT)
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
            currentColumn = i / HELP_IMAGE_ITEMS_PER_COLUMN;
            currentRowInColumn = i % HELP_IMAGE_ITEMS_PER_COLUMN;

            // 预计算坐标
            int rowX = centerX + (currentColumn * columnWidth);
            int rowY = y + (currentRowInColumn * HELP_IMAGE_ROW_HEIGHT) + HELP_IMAGE_ROW_HEIGHT;  // +ROW_HEIGHT是因为初始y已包含表头高度

            // 预解析数据
            String line = helpInfo.get(i);
            String[] parts = PIPE_PATTERN.split(line, 2);
            String command = parts.length > 0 ? parts[0] : "";

            RowInfo rowInfo = new RowInfo(rowX, rowY, currentRowInColumn % 2 == 0, command);
            rowInfos.add(rowInfo);
            if (rowInfo.isEvenRow) {
                combiner.setColor(HELP_IMAGE_EVEN_ROW_COLOR);
                combiner.fillRect(rowInfo.x, rowInfo.y + (HELP_IMAGE_ROW_HEIGHT / 2) - (FONT_SIZE / 2), columnWidth, HELP_IMAGE_ROW_HEIGHT);
            }
        }

        // 2. 绘制背景图片
        BufferedImage backgroundImage = ImageIOUtils.getRandomXiaoMeiWangImage();
        int maxImageWidth = IMAGE_WIDTH / 2;
        int maxImageHeight = totalHeight - imageY - IMAGE_MARGIN;
        combiner.drawImageWithAspectRatio(backgroundImage, IMAGE_WIDTH / 2 + 40, imageY + IMAGE_MARGIN, maxImageWidth, maxImageHeight);

        // 3. 绘制所有数据行文字（批量操作）
        combiner.setColor(HELP_IMAGE_TEXT_COLOR);
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
