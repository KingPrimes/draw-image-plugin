package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.ActiveMission;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认 裂隙任务绘制工具类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawActiveMission {

    final static Font FONT_FACTION = FONT_WARFRAME_ICON.deriveFont(Font.PLAIN, 48f);

    /**
     * 绘制裂隙任务图像
     *
     * @param activeMission 裂隙任务数据
     * @return 生成的裂隙任务图像的 PNG 格式字节数组
     */
    public static byte[] drawActiveMissionImage(List<ActiveMission> activeMission) {
        if (activeMission == null || activeMission.isEmpty()) {
            return new byte[0];
        }

        // 动态计算高度
        int contentRows = activeMission.size();
        int contentHeight = contentRows * (IMAGE_ROW_HEIGHT + IMAGE_ROW_MARGIN) - IMAGE_ROW_MARGIN; // 减去最后一个项目的边距
        int height = IMAGE_MARGIN_TOP + IMAGE_TITLE_HEIGHT + contentHeight + IMAGE_FOOTER_HEIGHT; // 上边距 + 标题高度 + 标题与内容间距 + 内容高度 + 下边距

        // 创建画布
        ImageCombiner combiner = new ImageCombiner(ACTIVE_MISSION_WIDTH, height, ImageCombiner.OutputFormat.PNG);

        // 填充背景色
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, ACTIVE_MISSION_WIDTH, height)
                .drawTooRoundRect();

        combiner.setColor(ACTIVE_MISSION_HEADER_COLOR)
                .setFont(FONT)
                .addCenteredText(activeMission.getFirst().getVoidStorms() ? "虚空风暴" : activeMission.getFirst().getHard() ? "钢铁裂隙" : "虚空裂隙", IMAGE_MARGIN_TOP)
                .drawStandingDrawing();
        // 绘制内容区域
        int y = IMAGE_MARGIN + IMAGE_ROW_HEIGHT + IMAGE_ROW_HEIGHT;

        for (ActiveMission mission : activeMission) {
            int x = IMAGE_MARGIN;
            // 裂隙类型
            combiner.setColor(mission.getModifierColor())
                    .addText(mission.getModifierName(), x, y);

            x += IMAGE_MARGIN + 60;
            // 任务类型和派系
            String missionInfo = mission.getMissionTypeName();
            combiner.setColor(mission.getMissionTypeColor())
                    .addText(missionInfo, x, y);
            combiner.setFont(FONT_FACTION)
                    .setColor(mission.getFactionColor())
                    .addText(mission.getFactionIcon(), x + 140, y + 8)
                    .setFont(FONT)
                    .addText(mission.getFactionName(), x + 190, y);
            x += IMAGE_MARGIN + 60;
            // 节点位置
            x += IMAGE_MARGIN + 260;
            String node = mission.getNode();
            combiner.setColor(ACTIVE_MISSION_LOCATION_COLOR)
                    .addText(node, x, y);

            // 剩余时间
            x += IMAGE_MARGIN + combiner.getStringWidth(node) + 60;
            Color timeColor = getTimeColor(mission);
            combiner.setColor(timeColor)
                    .addText(mission.getTimeLeft(), x, y);

            y += IMAGE_ROW_HEIGHT;
        }

        // 底部署名
        addFooter(combiner, height - 40);

        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }

    /**
     * 根据剩余时间获取对应的颜色
     *
     * @param mission 裂隙任务
     * @return 时间颜色
     */
    private static Color getTimeColor(ActiveMission mission) {
        // 简化处理：根据时间字符串中是否包含小时来判断紧急程度
        String timeLeft = mission.getTimeLeft();
        if (timeLeft.contains("h")) {
            return ACTIVE_MISSION_TIME_HIGH_COLOR; // 时间充足 - 绿色
        } else if (timeLeft.contains("m")) {
            int minutes = extractMinutes(timeLeft);
            if (minutes < 10) {
                return ACTIVE_MISSION_TIME_LOW_COLOR; // 时间紧急 - 红色
            } else {
                return ACTIVE_MISSION_TIME_MEDIUM_COLOR; // 时间适中 - 黄色
            }
        } else {
            return ACTIVE_MISSION_TIME_LOW_COLOR; // 时间紧急 - 红色
        }
    }

    /**
     * 从时间字符串中提取分钟数
     *
     * @param timeString 时间字符串
     * @return 分钟数
     */
    private static int extractMinutes(String timeString) {
        try {
            if (timeString.contains("m")) {
                int index = timeString.indexOf("m");
                String minuteStr = timeString.substring(0, index).replaceAll("\\D+", "");
                return Integer.parseInt(minuteStr);
            }
        } catch (Exception e) {
            // 解析失败时返回默认值
        }
        return 60; // 默认返回60分钟
    }
}
