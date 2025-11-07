package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.image.ImageIOUtils;
import io.github.kingprimes.model.worldstate.KnownCalendarSeasons;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认1999日历季节图片绘制工具类
 * <p>
 * 该类负责将1999日历季节数据渲染为图像，包括季节信息、年份迭代次数和每日事件等元素
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawKnownCalendarSeasonsImage {

    // 每个月份区域的尺寸
    private static final int IMAGE_WIDTH = 1500;
    private static final int MONTH_WIDTH = 650;
    private static final int MONTH_HEADER_HEIGHT = 40;
    private static final int DAY_HEIGHT = 30; // 每天的高度
    private static final int EVENT_HEIGHT = 30; // 每个事件的高度
    private static final int MONTH_FOOTER_HEIGHT = 30;
    private static final int EVENT_MAX_WIDTH = 580; // 事件文本最大宽度

    // 每行显示的月份数量
    private static final int MONTHS_PER_ROW = 2;

    // 颜色常量定义（使用项目中已有的颜色常量）
    private static final Color SEASON_COLOR = new Color(0x2E86AB); // 季节信息颜色 - 蓝色系

    /**
     * 私有构造函数，防止实例化该工具类
     */
    private DefaultDrawKnownCalendarSeasonsImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawKnownCalendarSeasonsImage class");
    }

    /**
     * 绘制1999日历季节图像
     *
     * @param knownCalendarSeasonsList 1999日历季节数据列表
     * @return 生成的日历季节图像的 PNG 格式字节数组
     */
    public static byte[] drawKnownCalendarSeasonsImage(List<KnownCalendarSeasons> knownCalendarSeasonsList) {
        // 如果没有日历季节数据，则返回空字节数组
        if (knownCalendarSeasonsList == null || knownCalendarSeasonsList.isEmpty()) {
            return new byte[0];
        }

        // 获取第一个日历季节数据（通常只有一个）
        KnownCalendarSeasons calendar = knownCalendarSeasonsList.getFirst();

        // 根据事件数量计算图像高度
        int height = calculateImageHeight(calendar);

        // 创建图像合成器实例
        ImageCombiner combiner = new ImageCombiner(
                IMAGE_WIDTH,
                height,
                ImageCombiner.OutputFormat.PNG
        );

        // 填充背景色并绘制双层圆角矩形边框
        combiner.setFont(FONT)
                .setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, IMAGE_WIDTH, height)
                .drawTooRoundRect();

        // 绘制标题
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 36)) // 标题字体大小为36
                .addCenteredText("1999日历季节信息", 80);

        // 绘制季节基本信息
        int startY = 120;
        drawCalendarInfo(combiner, calendar, startY);

        // 更新起始Y坐标
        startY += 100;

        // 绘制每月事件列表
        if (calendar.getMonthDays() != null && !calendar.getMonthDays().isEmpty()) {
            // 按月份排序

            // 将monthDays转换为正确的类型
            ObjectMapper mapper = new ObjectMapper();
            Map<String, List<KnownCalendarSeasons.Days>> monthDays = mapper.convertValue(
                    calendar.getMonthDays(),
                    new TypeReference<>() {
                    }
            );

            Map<String, List<KnownCalendarSeasons.Days>> sortedMonthDays = new TreeMap<>(monthDays);

            // 计算行数和列数
            Object[] months = sortedMonthDays.keySet().toArray();
            int rows = (int) Math.ceil((double) months.length / MONTHS_PER_ROW);

            // 绘制月份区域
            int currentY = startY;
            for (int row = 0; row < rows; row++) {
                int rowHeight = getMaxMonthHeightForRow(sortedMonthDays, row);
                for (int col = 0; col < MONTHS_PER_ROW; col++) {
                    int monthIndex = row * MONTHS_PER_ROW + col;
                    if (monthIndex >= months.length) {
                        break;
                    }

                    String monthKey = (String) months[monthIndex];
                    List<KnownCalendarSeasons.Days> days = sortedMonthDays.get(monthKey);

                    // 计算月份区域的X坐标
                    int monthX = IMAGE_MARGIN + col * (MONTH_WIDTH + 50);

                    // 绘制月份区域
                    drawMonthSection(combiner, Integer.parseInt(monthKey), days, monthX, currentY);
                }
                // 更新下一行的Y坐标
                currentY += rowHeight + 30; // 30是行间距
            }
        }

        // 添加看板娘图片，根据动态计算的图像宽高来确定位置和大小
        BufferedImage xiaoMeiWangImage = ImageIOUtils.getRandomXiaoMeiWangImage();
        int mascotX = IMAGE_WIDTH - IMAGE_WIDTH / 3 - 120;
        int mascotY = height / 2; // 底部边距40像素
        combiner.drawImageWithAspectRatio(
                xiaoMeiWangImage,
                mascotX,
                mascotY,
                IMAGE_WIDTH / 2,
                height / 2
        );

        // 添加底部署名
        addFooter(combiner.setFont(FONT), height - IMAGE_FOOTER_HEIGHT);

        // 合并图像并获取字节数组
        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }

    /**
     * 计算图像高度
     * 根据日历事件数量动态计算图像高度，确保内容完整显示
     *
     * @param calendar 日历季节数据
     * @return 图像高度
     */
    private static int calculateImageHeight(KnownCalendarSeasons calendar) {
        int headerHeight = 120; // 标题区域高度
        int infoHeight = 100; // 基本信息区域高度
        int footerHeight = IMAGE_FOOTER_HEIGHT + 50; // 底部区域高度

        // 计算月份区域高度
        int monthContentHeight = 0;
        if (calendar.getMonthDays() != null) {
            // 将monthDays转换为正确的类型
            ObjectMapper mapper = new ObjectMapper();
            Map<String, List<KnownCalendarSeasons.Days>> monthDays = mapper.convertValue(
                    calendar.getMonthDays(),
                    new TypeReference<>() {
                    }
            );

            Map<String, List<KnownCalendarSeasons.Days>> sortedMonthDays = new TreeMap<>(monthDays);

            // 计算行数
            Object[] months = sortedMonthDays.keySet().toArray();
            int rows = (int) Math.ceil((double) months.length / MONTHS_PER_ROW);

            // 计算每行的实际高度
            for (int row = 0; row < rows; row++) {
                monthContentHeight += getMaxMonthHeightForRow(sortedMonthDays, row) + 30; // 30是行间距
            }
        }

        int tableHeight = headerHeight + infoHeight + monthContentHeight + footerHeight; // 总高度

        return Math.max(tableHeight, 800); // 最小高度800
    }

    /**
     * 获取指定行中月份区域的最大高度
     *
     * @param sortedMonthDays 排序后的月份数据
     * @param row             行号
     * @return 该行中月份区域的最大高度
     */
    private static int getMaxMonthHeightForRow(Map<String, List<KnownCalendarSeasons.Days>> sortedMonthDays, int row) {
        Object[] months = sortedMonthDays.keySet().toArray();
        int maxHeight = 0;

        for (int col = 0; col < MONTHS_PER_ROW; col++) {
            int monthIndex = row * MONTHS_PER_ROW + col;
            if (monthIndex >= months.length) {
                break;
            }

            String monthKey = (String) months[monthIndex];
            List<KnownCalendarSeasons.Days> days = sortedMonthDays.get(monthKey);

            // 计算当前月份区域的实际高度，要考虑每天的事件数量
            int maxEventsPerDay = 0;
            for (KnownCalendarSeasons.Days day : days) {
                if (day.getEvents() != null) {
                    maxEventsPerDay = Math.max(maxEventsPerDay, day.getEvents().size());
                }
            }

            // 每天的高度取决于事件数量
            int dayHeightWithEvents = Math.max(DAY_HEIGHT, maxEventsPerDay * (EVENT_HEIGHT + 10));
            int monthHeight = MONTH_HEADER_HEIGHT + (days.size() * dayHeightWithEvents) + MONTH_FOOTER_HEIGHT;
            maxHeight = Math.max(maxHeight, monthHeight);
        }

        return maxHeight;
    }

    /**
     * 绘制日历基本信息
     * 包括季节名称、年份迭代次数等
     *
     * @param combiner 图像合成器实例
     * @param calendar 日历季节数据
     * @param startY   起始Y坐标
     */
    private static void drawCalendarInfo(ImageCombiner combiner, KnownCalendarSeasons calendar, int startY) {
        // 绘制季节信息
        String seasonText = "当前季节: " + (calendar.getSeason() != null ? calendar.getSeason().getName() : "未知");
        combiner.setColor(SEASON_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 28))
                .addText(seasonText, IMAGE_MARGIN + 300, startY + 30);

        // 绘制年份迭代次数
        String yearIterationText = "年份迭代: 第" + (calendar.getYearIteration() != null ? calendar.getYearIteration() : "未知") + "次";
        combiner
                .setFont(FONT.deriveFont(28f))
                .addText(yearIterationText, IMAGE_MARGIN + 600, startY + 30);

        // 绘制版本号
        String versionText = "版本: " + (calendar.getVersion() != null ? calendar.getVersion() : "未知");
        combiner.setFont(FONT.deriveFont(28f))
                .addText(versionText, IMAGE_MARGIN + 900, startY + 30)
                .setColor(TEXT_COLOR);
    }

    /**
     * 绘制月份区域
     * 包括月份标题和该月的所有天事件
     *
     * @param combiner 图像合成器实例
     * @param month    月份
     * @param days     该月的日期列表
     * @param startX   起始X坐标
     * @param startY   起始Y坐标
     */
    private static void drawMonthSection(ImageCombiner combiner, Integer month, List<KnownCalendarSeasons.Days> days, int startX, int startY) {
        // 计算当前月份区域的实际高度
        int maxEventsPerDay = 0;
        for (KnownCalendarSeasons.Days day : days) {
            if (day.getEvents() != null) {
                maxEventsPerDay = Math.max(maxEventsPerDay, day.getEvents().size());
            }
        }

        // 每天的高度取决于事件数量
        int dayHeightWithEvents = Math.max(DAY_HEIGHT, maxEventsPerDay * (EVENT_HEIGHT + 10));
        int monthHeight = MONTH_HEADER_HEIGHT + (days.size() * dayHeightWithEvents) + MONTH_FOOTER_HEIGHT;

        // 绘制月份背景框
        combiner.setColor(CARD_BACKGROUND_COLOR)
                .fillRoundRect(startX, startY, MONTH_WIDTH, monthHeight, 15, 15)
                .setColor(TEXT_COLOR)
                .drawRoundRect(startX, startY, MONTH_WIDTH, monthHeight, 15, 15);

        // 绘制月份标题
        combiner.setColor(SEASON_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 28))
                .addText(String.format("%d月", month), startX + 20, startY + 40);

        // 绘制该月的所有天事件
        int currentDayY = startY + MONTH_HEADER_HEIGHT + 30;
        for (KnownCalendarSeasons.Days day : days) {
            // 绘制单个日期事件
            int eventsHeight = drawDayEvents(combiner, day, startX + 20, currentDayY);

            // 计算下一个日期的Y坐标
            int eventsCount = (day.getEvents() != null) ? day.getEvents().size() : 0;

            currentDayY = eventsHeight + eventsCount + DAY_HEIGHT;
        }

    }

    /**
     * 绘制单个日期的事件
     * 包括日期和该日期的所有事件
     *
     * @param combiner 图像合成器实例
     * @param day      日期事件数据
     * @param startX   起始X坐标
     * @param startY   起始Y坐标
     */
    private static int drawDayEvents(ImageCombiner combiner, KnownCalendarSeasons.Days day, int startX, int startY) {
        // 绘制日期
        String dayText = String.format("%d月%d日", day.getMonth(), day.getDay());
        combiner.setColor(TEXT_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 24))
                .addText(dayText, startX, startY);

        // 绘制该日期的事件（在日期下方纵向排列）
        if (day.getEvents() != null && !day.getEvents().isEmpty()) {
            int eventStartY = startY + 30; // 日期下方30像素开始绘制事件

            // 显示所有事件，每个事件占EVENT_HEIGHT像素高度
            for (int i = 0; i < day.getEvents().size(); i++) {
                KnownCalendarSeasons.Events event = day.getEvents().get(i);

                StringBuilder eventText = new StringBuilder();
                Color eventColor = TEXT_COLOR;

                if (event.getType() != null) {
                    switch (event.getType()) {
                        case CET_CHALLENGE:
                            eventText.append("[任务] ");
                            if (event.getChallengeInfo() != null) {
                                eventText.append(event.getChallengeInfo().challenge());
                            }
                            eventColor = new Color(0xFF6B6B); // 挑战事件颜色 - 红色系
                            break;
                        case CET_REWARD:
                            eventText.append("[奖励] ").append(event.getReward());
                            eventColor = new Color(0x4CAF50); // 奖励事件颜色 - 绿色系
                            break;
                        case CET_UPGRADE:
                            eventText.append("[加成] ");
                            if (event.getUpgradeInfo() != null) {
                                eventText.append(event.getUpgradeInfo().upgrade());
                            }
                            eventColor = new Color(0x554820); // 加成事件颜色 - 橙色系
                            break;
                    }
                }

                int y = combiner.calculateWrappedTextHeight(eventText.toString(), EVENT_MAX_WIDTH, 5);

                combiner.setColor(eventColor)
                        .setFont(FONT.deriveFont(24f))
                        .addMultilineTextWithWrap(eventText.toString(), startX + 20, eventStartY, EVENT_MAX_WIDTH, 24, 5);
                eventStartY = eventStartY + y;
                startY += y;
            }
        }
        return startY;
    }
}