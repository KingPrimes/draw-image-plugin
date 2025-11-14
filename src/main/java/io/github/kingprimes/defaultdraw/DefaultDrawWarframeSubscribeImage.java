package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.image.TextUtils;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认订阅图片绘制工具类
 *
 * @author KingPrimes
 * @version 1.0.0
 */
final class DefaultDrawWarframeSubscribeImage {


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
        ImageCombiner combiner = new ImageCombiner(SUBSCRIBE_IMAGE_WIDTH, SUBSCRIBE_IMAGE_HEIGHT, ImageCombiner.OutputFormat.PNG);

        combiner.setFont(FONT)
                .setColor(Color.WHITE)
                .fillRect(0, 0, SUBSCRIBE_IMAGE_WIDTH, SUBSCRIBE_IMAGE_HEIGHT); // 白色背景

        int borderPadding = 20;
        combiner.drawTooRoundRect()
                .drawStandingDrawing();

        int y = SUBSCRIBE_IMAGE_MARGIN + borderPadding + SUBSCRIBE_IMAGE_TITLE_HEIGHT / 2;
        // ================== 标题 Start ==================
        String title = "---订阅指令表---";
        combiner.setColor(BLACK_COLOR)
                .setFont(FONT)
                .addCenteredText(title, y);
        // ================== 标题 END ==================

        // ================== 命令使用方式 Start ==================
        y += SUBSCRIBE_IMAGE_MARGIN + 30;
        int x = SUBSCRIBE_IMAGE_MARGIN + 40;
        String usage = "命令使用方式：";
        String subscribeText = "订阅";
        String subscribeTypeText = "[订阅内容类型]";
        String missionTypeText = "[-订阅任务类型]";
        String relicLevelText = "[-订阅遗物等级]";

        combiner.setColor(BLACK_COLOR)
                .addText(usage, x, y);

        x += TextUtils.getFortWidth(usage, FONT) + 80;
        combiner.setColor(SUBSCRIBE_IMAGE_BLUE_COLOR)
                .addText(subscribeText, x, y);
        x += TextUtils.getFortWidth(subscribeText, FONT);
        combiner.setColor(SUBSCRIBE_IMAGE_PURPLE_COLOR)
                .addText(subscribeTypeText, x, y);
        x += TextUtils.getFortWidth(subscribeTypeText, FONT) + 60;
        combiner.setColor(SUBSCRIBE_IMAGE_RED_COLOR)
                .addText(missionTypeText, x, y);
        x += TextUtils.getFortWidth(missionTypeText, FONT) + 60;
        combiner.setColor(SUBSCRIBE_IMAGE_BROWN_COLOR)
                .addText(relicLevelText, x, y);
        // ================== 命令使用方式 END ==================


        // ================== 下方的例子 Start ==================
        x = SUBSCRIBE_IMAGE_MARGIN + 40;
        y += FONT_SIZE + SUBSCRIBE_IMAGE_MARGIN;
        String example = "下方的例子是指：";
        String subscribeTypeExample = "裂隙";
        String missionTypeExample = "生存模式";
        String relicLevelExample = "后纪";
        combiner.setColor(BLACK_COLOR)
                .addText(example, x, y);

        x += TextUtils.getFortWidth(example, FONT) + 80;
        combiner.setColor(SUBSCRIBE_IMAGE_BLUE_COLOR)
                .addText(subscribeText, x, y);

        x += TextUtils.getFortWidth(subscribeText, FONT);
        combiner.setColor(SUBSCRIBE_IMAGE_PURPLE_COLOR)
                .addText(subscribeTypeExample, x, y);

        x += TextUtils.getFortWidth(subscribeTypeExample, FONT);
        combiner.setColor(SUBSCRIBE_IMAGE_RED_COLOR)
                .addText(missionTypeExample, x, y);

        x += TextUtils.getFortWidth(missionTypeExample, FONT) + 30;
        combiner.setColor(SUBSCRIBE_IMAGE_BROWN_COLOR)
                .addText(relicLevelExample, x, y);
        // ================== 下方的例子 END ==================


        // ================== 指令例子 Start ==================
        x = SUBSCRIBE_IMAGE_MARGIN + 40;
        y += FONT_SIZE + SUBSCRIBE_IMAGE_MARGIN;
        String l = "指令例子：";
        String t = "9";
        String f = "-11";
        String r = "-4";
        combiner.setColor(BLACK_COLOR)
                .addText(l, x, y);

        x += TextUtils.getFortWidth(l, FONT) + 40;
        combiner.setColor(SUBSCRIBE_IMAGE_BLUE_COLOR)
                .addText(subscribeText, x, y);

        x += TextUtils.getFortWidth(subscribeText, FONT);
        combiner.setColor(SUBSCRIBE_IMAGE_PURPLE_COLOR)
                .addText(t, x, y);

        x += TextUtils.getFortWidth(t, FONT);
        combiner.setColor(SUBSCRIBE_IMAGE_RED_COLOR)
                .addText(f, x, y);

        x += TextUtils.getFortWidth(f, FONT);
        combiner.setColor(SUBSCRIBE_IMAGE_BROWN_COLOR)
                .addText(r, x, y);
        // ================== 指令例子 END ==================


        // ================== 注意事项 Start ==================
        x = SUBSCRIBE_IMAGE_MARGIN + 40;
        y += FONT_SIZE + SUBSCRIBE_IMAGE_MARGIN;
        String note = "注意事项：";
        String rl = "遗物等级";
        String only = "只有在订阅";
        String f3 = "裂隙";
        String useful = "时有用";

        combiner.setColor(BLACK_COLOR)
                .addText(note, x, y);

        x += TextUtils.getFortWidth(note, FONT) + 40;
        combiner.setColor(SUBSCRIBE_IMAGE_BROWN_COLOR)
                .addText(rl, x, y);

        x += TextUtils.getFortWidth(rl, FONT) + 30;
        combiner.setColor(BLACK_COLOR)
                .addText(only, x, y);

        x += TextUtils.getFortWidth(only, FONT) + 60;
        combiner.setColor(SUBSCRIBE_IMAGE_PURPLE_COLOR)
                .addText(f3, x, y);

        x += TextUtils.getFortWidth(f3, FONT);
        combiner.setColor(BLACK_COLOR)
                .addText(useful, x, y);
        // ================== 注意事项 END ==================


        // ================== 订阅内容类型数值 Start ==================
        String subscribeTitle = "订阅内容类型数值";
        y += FONT_SIZE + SUBSCRIBE_IMAGE_MARGIN;
        combiner.setColor(SUBSCRIBE_IMAGE_PURPLE_COLOR)
                .setFont(FONT)
                .addCenteredText(subscribeTitle, y);

        y += TextUtils.getFortHeight(subscribeTitle, FONT) + SUBSCRIBE_IMAGE_MARGIN;
        int j = drawTable(combiner, subscribe, y, SUBSCRIBE_IMAGE_PURPLE_COLOR);
        y += j / 3 + subscribe.size() * 2;
        // ================== 订阅任务类型数值 END ==================
        String missionTypeTitle = "订阅任务类型数值";
        combiner.setColor(SUBSCRIBE_IMAGE_RED_COLOR)
                .setFont(FONT)
                .addCenteredText(missionTypeTitle, y);

        y += TextUtils.getFortHeight(missionTypeTitle, FONT) + SUBSCRIBE_IMAGE_MARGIN;
        j = drawTable(combiner, missionType, y, SUBSCRIBE_IMAGE_RED_COLOR);
        y += j / 3 + missionType.size() * 2 - IMAGE_FOOTER_HEIGHT;

        addFooter(combiner, y);

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

        int itemWidth = SUBSCRIBE_IMAGE_WIDTH / 4;
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