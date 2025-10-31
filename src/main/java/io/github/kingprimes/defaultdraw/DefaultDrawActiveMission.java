package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.image.ImageIOUtils;
import io.github.kingprimes.image.TextUtils;
import io.github.kingprimes.model.enums.VoidEnum;
import io.github.kingprimes.model.worldstate.ActiveMission;
import io.github.kingprimes.utils.Fonts;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * 默认 裂隙任务绘制工具类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawActiveMission {
    static final int WIDTH = 1500;
    static final int MARGIN = 60;
    static final int TITLE_HEIGHT = 60;
    static final int ROW_HEIGHT = 60;
    static final int ROW_MARGIN = 15;
    static final Font FONT = Fonts.FONT_TEXT;
    static final Color BACKGROUND_COLOR = new Color(0x437BBF);
    static final Color HEADER_COLOR = new Color(0x1a2c38);
    static final Color TASK_TEXT_COLOR = new Color(0xff7e5f);
    static final Color LOCATION_COLOR = new Color(0xdfe6e9);
    static final Color TIME_LOW_COLOR = new Color(0xff6b6b);
    static final Color TIME_MEDIUM_COLOR = new Color(0xfeca57);
    static final Color TIME_HIGH_COLOR = new Color(0x1dd1a1);

    // 遗物等级颜色
    static final Color VOID_T1_COLOR = new Color(0x514234);
    static final Color VOID_T2_COLOR = new Color(0x75562B);
    static final Color VOID_T3_COLOR = new Color(0xB3B3B3);
    static final Color VOID_T4_COLOR = new Color(0xE0DC46);
    static final Color VOID_T5_COLOR = new Color(0x872A2C);
    static final Color VOID_T6_COLOR = new Color(0x7f8c8d);

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
        int contentHeight = contentRows * (ROW_HEIGHT + ROW_MARGIN) - ROW_MARGIN; // 减去最后一个项目的边距
        int height = TITLE_HEIGHT + 20 + contentHeight + 80; // 上边距 + 标题高度 + 标题与内容间距 + 内容高度 + 下边距

        // 创建画布
        ImageCombiner combiner = new ImageCombiner(WIDTH, height, ImageCombiner.OutputFormat.PNG);

        // 填充背景色
        combiner.setColor(BACKGROUND_COLOR)
                .fillRect(0, 0, WIDTH, height)
                .drawTooRoundRect();

        combiner.setColor(HEADER_COLOR)
                .setFont(FONT)
                .addCenteredText(activeMission.getFirst().getHard() ? "钢铁裂隙" : "虚空裂隙", 80);
        BufferedImage backgroundImage = ImageIOUtils.getRandomXiaoMeiWangImage();
        int maxImageWidth = WIDTH / 2;
        int maxImageHeight = height - MARGIN;
        combiner.drawImageWithAspectRatio(backgroundImage, WIDTH / 2 + 78, MARGIN, maxImageWidth, maxImageHeight);
        // 绘制内容区域
        int y = MARGIN + ROW_HEIGHT + 20;

        for (ActiveMission mission : activeMission) {
            int x = 60;
            // 裂隙类型
            String modifier = mission.getModifier();
            combiner.setColor(getModifierColor(mission.getVoidEnum()))
                    .addText(modifier, x, y);

            x += TextUtils.getFortWidth(modifier, FONT) + MARGIN;
            // 任务类型和派系
            String missionInfo = mission.getMissionType() + "-" + mission.getFaction().getName();
            combiner.setColor(TASK_TEXT_COLOR)
                    .addText(missionInfo, x, y);

            // 节点位置
            x += TextUtils.getFortWidth(missionInfo, FONT) + MARGIN + 20;
            String node = mission.getNode();
            combiner.setColor(LOCATION_COLOR)
                    .addText(node, x, y);

            // 剩余时间
            x += TextUtils.getFortWidth(node, FONT) + MARGIN + 40;
            Color timeColor = getTimeColor(mission);
            combiner.setColor(timeColor)
                    .addText(mission.getTimeLeft(), x, y);

            y += ROW_HEIGHT;
        }

        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }

    private static Color getModifierColor(VoidEnum ve) {
        return switch (ve) {
            case VoidT1 -> VOID_T1_COLOR;
            case VoidT2 -> VOID_T2_COLOR;
            case VoidT3 -> VOID_T3_COLOR;
            case VoidT4 -> VOID_T4_COLOR;
            case VoidT5 -> VOID_T5_COLOR;
            case VoidT6 -> VOID_T6_COLOR;
        };
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
            return TIME_HIGH_COLOR; // 时间充足 - 绿色
        } else if (timeLeft.contains("m")) {
            int minutes = extractMinutes(timeLeft);
            if (minutes < 10) {
                return TIME_LOW_COLOR; // 时间紧急 - 红色
            } else {
                return TIME_MEDIUM_COLOR; // 时间适中 - 黄色
            }
        } else {
            return TIME_LOW_COLOR; // 时间紧急 - 红色
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
