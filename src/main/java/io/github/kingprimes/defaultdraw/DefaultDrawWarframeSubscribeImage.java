package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.image.ImageIOUtils;
import io.github.kingprimes.image.TextUtils;
import io.github.kingprimes.utils.Fonts;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 默认订阅图片绘制工具类
 *
 * @author KingPrimes
 * @version 1.0.0
 */
final class DefaultDrawWarframeSubscribeImage {

    static final int WIDTH = 1300;
    static final int HEIGHT = 1000;
    static final int MARGIN = 20;
    static final int TITLE_HEIGHT = 50;
    static final Color blackColor = Color.BLACK;
    static final Color blueColor = new Color(0x1c84c6);
    static final Color purpleColor = new Color(0x6110f3);
    static final Color redColor = new Color(0xff0000);
    static final Color brownColor = new Color(0xaf5244);
    static final Font mainFont = Fonts.FONT_TEXT;
    static final int TEXT_LINE_HEIGHT = mainFont.getSize();

    /**
     * <p>绘制 Warframe 订阅指令说明图片。</p>
     * 该方法根据传入的订阅内容类型和任务类型映射关系，生成一张包含使用说明、示例、注意事项及数值对照表的图片。
     * 图片中包含标题、使用方式、示例指令、注意事项、订阅类型与任务类型的数值表，并在右下角添加背景图和署名。
     *
     * @param subscribe   订阅内容类型映射，键为类型编号，值为类型名称
     * @param missionType 任务类型映射，键为类型编号，值为类型名称
     * @return 生成的图片字节数组，格式为 PNG
     */
    public static byte[] drawWarframeSubscribeImage(Map<Integer, String> subscribe, Map<Integer, String> missionType) {
        // 创建画布
        ImageCombiner combiner = new ImageCombiner(WIDTH, HEIGHT, ImageCombiner.OutputFormat.PNG);

        combiner.setFont(mainFont)
                .setColor(Color.WHITE)
                .fillRect(0, 0, WIDTH, HEIGHT); // 白色背景

        int borderPadding = 20;
        int innerWidth = WIDTH - borderPadding * 2;
        int innerHeight = HEIGHT - borderPadding * 2;
        int backgroundY = MARGIN + borderPadding + TITLE_HEIGHT + 30; // 文字区域下方开始
        BufferedImage backgroundImage = ImageIOUtils.getRandomXiaoMeiWangImage();
        int maxImageWidth = innerWidth / 2 + 80;
        int maxImageHeight = innerHeight - (backgroundY - MARGIN - borderPadding);
        combiner.drawTooRoundRect()
                .drawImageWithAspectRatio(backgroundImage,
                        WIDTH / 2 + borderPadding, // 右侧起始X坐标
                        backgroundY,
                        maxImageWidth,
                        maxImageHeight);

        int y = MARGIN + borderPadding + TITLE_HEIGHT / 2;
        // ================== 标题 Start ==================
        String title = "---订阅指令表---";
        combiner.setColor(blackColor)
                .setFont(mainFont)
                .addCenteredText(title, y);
        // ================== 标题 END ==================

        // ================== 命令使用方式 Start ==================
        y += MARGIN + 30;
        int x = MARGIN + 40;
        String usage = "命令使用方式：";
        String subscribeText = "订阅";
        String subscribeTypeText = "[订阅内容类型]";
        String missionTypeText = "[-订阅任务类型]";
        String relicLevelText = "[-订阅遗物等级]";

        combiner.setColor(blackColor)
                .addText(usage, x, y);

        x += TextUtils.getFortWidth(usage, mainFont) + 80;
        combiner.setColor(blueColor)
                .addText(subscribeText, x, y);
        x += TextUtils.getFortWidth(subscribeText, mainFont);
        combiner.setColor(purpleColor)
                .addText(subscribeTypeText, x, y);
        x += TextUtils.getFortWidth(subscribeTypeText, mainFont) + 60;
        combiner.setColor(redColor)
                .addText(missionTypeText, x, y);
        x += TextUtils.getFortWidth(missionTypeText, mainFont) + 60;
        combiner.setColor(brownColor)
                .addText(relicLevelText, x, y);
        // ================== 命令使用方式 END ==================


        // ================== 下方的例子 Start ==================
        x = MARGIN + 40;
        y += TEXT_LINE_HEIGHT + MARGIN;
        String example = "下方的例子是指：";
        String subscribeTypeExample = "裂隙";
        String missionTypeExample = "生存模式";
        String relicLevelExample = "后纪";
        combiner.setColor(blackColor)
                .addText(example, x, y);

        x += TextUtils.getFortWidth(example, mainFont) + 80;
        combiner.setColor(blueColor)
                .addText(subscribeText, x, y);

        x += TextUtils.getFortWidth(subscribeText, mainFont);
        combiner.setColor(purpleColor)
                .addText(subscribeTypeExample, x, y);

        x += TextUtils.getFortWidth(subscribeTypeExample, mainFont);
        combiner.setColor(redColor)
                .addText(missionTypeExample, x, y);

        x += TextUtils.getFortWidth(missionTypeExample, mainFont) + 30;
        combiner.setColor(brownColor)
                .addText(relicLevelExample, x, y);
        // ================== 下方的例子 END ==================


        // ================== 指令例子 Start ==================
        x = MARGIN + 40;
        y += TEXT_LINE_HEIGHT + MARGIN;
        String l = "指令例子：";
        String t = "9";
        String f = "-11";
        String r = "-4";
        combiner.setColor(blackColor)
                .addText(l, x, y);

        x += TextUtils.getFortWidth(l, mainFont) + 40;
        combiner.setColor(blueColor)
                .addText(subscribeText, x, y);

        x += TextUtils.getFortWidth(subscribeText, mainFont);
        combiner.setColor(purpleColor)
                .addText(t, x, y);

        x += TextUtils.getFortWidth(t, mainFont);
        combiner.setColor(redColor)
                .addText(f, x, y);

        x += TextUtils.getFortWidth(f, mainFont);
        combiner.setColor(brownColor)
                .addText(r, x, y);
        // ================== 指令例子 END ==================


        // ================== 注意事项 Start ==================
        x = MARGIN + 40;
        y += TEXT_LINE_HEIGHT + MARGIN;
        String note = "注意事项：";
        String rl = "遗物等级";
        String only = "只有在订阅";
        String f3 = "裂隙";
        String useful = "时有用";

        combiner.setColor(blackColor)
                .addText(note, x, y);

        x += TextUtils.getFortWidth(note, mainFont) + 40;
        combiner.setColor(brownColor)
                .addText(rl, x, y);

        x += TextUtils.getFortWidth(rl, mainFont) + 30;
        combiner.setColor(blackColor)
                .addText(only, x, y);

        x += TextUtils.getFortWidth(only, mainFont) + 60;
        combiner.setColor(purpleColor)
                .addText(f3, x, y);

        x += TextUtils.getFortWidth(f3, mainFont);
        combiner.setColor(blackColor)
                .addText(useful, x, y);
        // ================== 注意事项 END ==================


        // ================== 订阅内容类型数值 Start ==================
        String subscribeTitle = "订阅内容类型数值";
        y += TEXT_LINE_HEIGHT + MARGIN;
        combiner.setColor(purpleColor)
                .setFont(mainFont)
                .addCenteredText(subscribeTitle, y);

        y += TextUtils.getFortHeight(subscribeTitle, mainFont) + MARGIN;
        int j = drawTable(combiner, subscribe, y, purpleColor);
        y += j / 3 + subscribe.size() * 2;
        // ================== 订阅任务类型数值 END ==================
        String missionTypeTitle = "订阅任务类型数值";
        combiner.setColor(redColor)
                .setFont(mainFont)
                .addCenteredText(missionTypeTitle, y);

        y += TextUtils.getFortHeight(missionTypeTitle, mainFont) + MARGIN;
        j = drawTable(combiner, missionType, y, redColor);
        y += j / 3 + missionType.size() * 2 - 50;
        // ================== 底部署名 Start ==================
        String footer = "Posted by: KingPrimes";
        combiner.setColor(Color.GRAY)
                .setFont(mainFont)
                .addCenteredText(footer, y);
        // ================== 底部署名 END ==================

        // 合并图像
        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }


    /**
     * 绘制表格，每行 column 个项，自动换行
     *
     * @param combiner ImageCombiner 实例
     * @param data     键值对数据
     * @param startY   起始 Y 坐标
     * @param color    文本颜色
     */
    private static int drawTable(ImageCombiner combiner, Map<Integer, String> data, int startY,
                                 Color color) {
        if (data == null || data.isEmpty()) return startY;

        List<Map.Entry<Integer, String>> entries = new ArrayList<>(data.entrySet());
        entries.sort(Map.Entry.comparingByKey());

        int itemWidth = WIDTH / 4;
        int x = 20;
        int y = startY;
        int count = 0;
        FontMetrics fontMetrics = combiner.getFontMetrics();
        for (Map.Entry<Integer, String> entry : entries) {
            String text = entry.getKey() + " = " + entry.getValue();
            int textWidth = fontMetrics.stringWidth(text);
            int textX = x + (itemWidth - textWidth) / 2;

            combiner.setColor(color)
                    .addText(text, textX, y);

            count++;
            if (count % 4 == 0) {
                x = 20;
                y += fontMetrics.getHeight();
            } else {
                x += itemWidth;
            }
        }
        return y;
    }
}
