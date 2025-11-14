package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.Alert;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认警报图片绘制工具类
 *
 * @author KingPrimes
 * @version 1.0.0
 */
public final class DefaultDrawAlertsImage {

    private static final int ALERTS_IMAGE_WIDTH = 1200;
    private static final int ALERTS_IMAGE_MIN_HEIGHT = 600;

    private static final Color HEADER_COLOR = new Color(0x4A90E2);

    private DefaultDrawAlertsImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawAlertsImage class");
    }

    /**
     * 绘制警报图像
     *
     * @param alerts 警报数据列表
     * @return 生成的警报图像的 PNG 格式字节数组
     */
    public static byte[] drawAlertsImage(List<Alert> alerts) {
        if (alerts == null || alerts.isEmpty()) {
            return new byte[0];
        }

        // 计算图像高度
        int height = calculateImageHeight(alerts.size());

        // 创建画布
        ImageCombiner combiner = new ImageCombiner(
                ALERTS_IMAGE_WIDTH,
                height,
                ImageCombiner.OutputFormat.PNG
        );

        // 填充背景色
        combiner.setFont(FONT)
                .setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, ALERTS_IMAGE_WIDTH, height)
                // 绘制双层边框
                .drawTooRoundRect();

        // 绘制标题
        combiner.setColor(HEADER_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 40))
                .addCenteredText("警报", 80);

        // 绘制表格
        int tableY = 120;
        int tableWidth = ALERTS_IMAGE_WIDTH - 2 * IMAGE_MARGIN;
        int rowHeight = 60;

        // 绘制表头
        String[] headers = {"任务地点", "任务类型", "派系", "任务奖励", "距离结束"};
        int[] columnWidths = {200, 150, 150, 350, 150};

        // 绘制表头背景
        combiner.setColor(HEADER_COLOR)
                .fillRect(IMAGE_MARGIN, tableY, tableWidth, rowHeight);

        // 绘制表头文字
        combiner.setColor(Color.WHITE)
                .setFont(FONT.deriveFont(Font.BOLD, 16));
        int x = IMAGE_MARGIN;
        for (int i = 0; i < headers.length; i++) {
            combiner.addText(headers[i], x + columnWidths[i] / 2, tableY + rowHeight / 2 + 8);
            x += columnWidths[i];
        }
        combiner.drawStandingDrawing();
        // 绘制表格内容
        int contentY = tableY + rowHeight;
        for (int i = 0; i < alerts.size(); i++) {
            Alert alert = alerts.get(i);
            int rowY = contentY + i * rowHeight;

            // 绘制斑马纹效果
            if (i % 2 == 0) {
                combiner.setColor(new Color(0xFFFFFF, true)) // 半透明白色
                        .fillRect(IMAGE_MARGIN, rowY, tableWidth, rowHeight);
            }

            // 绘制表格行边框
            combiner.setColor(new Color(0xE0E0E0, true))
                    .setStroke(1)
                    .drawLine(IMAGE_MARGIN, rowY + rowHeight, IMAGE_MARGIN + tableWidth, rowY + rowHeight);

            // 绘制行内容
            drawAlertRow(combiner, alert, rowY, columnWidths, rowHeight);
        }

        // 绘制底部边框线
        int tableHeight = rowHeight + alerts.size() * rowHeight;
        combiner.setColor(new Color(0xE0E0E0, true))
                .setStroke(1)
                .drawLine(IMAGE_MARGIN, tableY + tableHeight, IMAGE_MARGIN + tableWidth, tableY + tableHeight);


        // 添加底部署名
        addFooter(combiner, height - IMAGE_FOOTER_HEIGHT);

        // 合并图像
        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }

    /**
     * 计算图像高度
     *
     * @param alertCount 警报数量
     * @return 图像高度
     */
    private static int calculateImageHeight(int alertCount) {
        int headerHeight = 120;
        int rowHeight = 60;
        int tableHeight = headerHeight + (rowHeight * alertCount) + IMAGE_FOOTER_HEIGHT;

        return Math.max(tableHeight, ALERTS_IMAGE_MIN_HEIGHT);
    }

    /**
     * 绘制警报行
     *
     * @param combiner     图像合成器
     * @param alert        警报数据
     * @param startY       起始Y坐标
     * @param columnWidths 列宽数组
     * @param rowHeight    行高
     */
    private static void drawAlertRow(ImageCombiner combiner, Alert alert, int startY, int[] columnWidths, int rowHeight) {
        combiner.setColor(TEXT_COLOR);

        // 任务地点
        String node = alert.getMissionInfo() != null && alert.getMissionInfo().getLocation() != null ?
                alert.getMissionInfo().getLocation() : "未知";
        combiner
                .addText(node, DrawConstants.IMAGE_MARGIN + columnWidths[0] / 2, startY + rowHeight / 2 + 5);

        // 任务类型
        String missionType = alert.getMissionInfo() != null && alert.getMissionInfo().getMissionType() != null ?
                alert.getMissionInfo().getMissionType().getName() : "未知";
        combiner.addText(missionType, DrawConstants.IMAGE_MARGIN + columnWidths[0] + columnWidths[1] / 2, startY + rowHeight / 2 + 5);

        // 派系
        String faction = alert.getMissionInfo() != null && alert.getMissionInfo().getFaction() != null ?
                alert.getMissionInfo().getFaction().getName() : "未知";
        combiner.addText(faction, DrawConstants.IMAGE_MARGIN + columnWidths[0] + columnWidths[1] + columnWidths[2] / 2, startY + rowHeight / 2 + 5);

        // 任务奖励
        int rewardX = DrawConstants.IMAGE_MARGIN + columnWidths[0] + columnWidths[1] + columnWidths[2];
        if (alert.getMissionInfo() != null && alert.getMissionInfo().getMissionReward() != null) {
            Alert.MissionInfo.Reward reward = alert.getMissionInfo().getMissionReward();
            int rewardY = startY + 15;

            // 绘制星币奖励
            if (reward.getCredits() != null && reward.getCredits() > 0) {
                combiner
                        .addText(reward.getCredits() + "星币", rewardX + 80, rewardY + 15);
            }

            // 绘制物品奖励
            if (reward.getItems() != null && !reward.getItems().isEmpty()) {
                for (String item : reward.getItems()) {
                    if (item != null && !item.isEmpty()) {
                        combiner
                                .addText(item, rewardX + 220, rewardY + 15);
                        rewardY += 20;
                    }
                }
            }
        }

        // 距离结束
        String eta = alert.getTimeLeft();
        combiner
                .addText(eta, DrawConstants.IMAGE_MARGIN + columnWidths[0] + columnWidths[1] + columnWidths[2] + columnWidths[3] + columnWidths[4] / 2, startY + rowHeight / 2 + 5);
    }
}