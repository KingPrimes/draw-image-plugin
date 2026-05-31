package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.KnownCalendarSeasons;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 1999日历季节卡片渲染器 — 两列卡片网格 + 看板娘
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawKnownCalendarSeasonsImage {

    private static final int CONTENT_X = 50;
    private static final int COLS = 2;
    private static final int COL_GAP = 20;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 20;

    private static final int TITLE_Y = 80;
    private static final int CONTENT_START_Y = 220;

    // 季节主题色
    private static final Color SPRING_COLOR = new Color(0xE91E8C);
    private static final Color SUMMER_COLOR = new Color(0xFF9800);
    private static final Color FALL_COLOR = new Color(0xC0392B);
    private static final Color WINTER_COLOR = new Color(0x3498DB);

    // 事件颜色
    private static final Color CHALLENGE_COLOR = new Color(0xFF6B6B);
    private static final Color REWARD_COLOR = new Color(0x4CAF50);
    private static final Color UPGRADE_COLOR = new Color(0xB8860B);
    private static final SeasonCard[] SEASONS = {
            new SeasonCard(WINTER_COLOR, new int[]{1, 2, 3}),
            new SeasonCard(SPRING_COLOR, new int[]{4, 5, 6}),
            new SeasonCard(SUMMER_COLOR, new int[]{7, 8, 9}),
            new SeasonCard(FALL_COLOR, new int[]{10, 11, 12}),
    };

    private DefaultDrawKnownCalendarSeasonsImage() {
        throw new AssertionError("Cannot instantiate");
    }

    private static Color getSeasonColor(int month) {
        for (SeasonCard sc : SEASONS) {
            for (int m : sc.months) {
                if (m == month) return sc.color;
            }
        }
        return TEXT_COLOR;
    }

    public static byte[] drawKnownCalendarSeasonsImage(List<KnownCalendarSeasons> knownCalendarSeasonsList) {
        if (knownCalendarSeasonsList == null || knownCalendarSeasonsList.isEmpty()) return new byte[0];

        KnownCalendarSeasons calendar = knownCalendarSeasonsList.getFirst();
        if (calendar.getMonthDays() == null || calendar.getMonthDays().isEmpty()) return new byte[0];

        Map<Integer, List<KnownCalendarSeasons.Days>> sorted = new TreeMap<>(calendar.getMonthDays());

        List<MonthCard> cards = new java.util.ArrayList<>();
        for (Map.Entry<Integer, List<KnownCalendarSeasons.Days>> e : sorted.entrySet()) {
            cards.add(new MonthCard(e.getKey(), e.getValue()));
        }
        int n = cards.size();
        boolean isOdd = n % COLS != 0;

        // 卡片宽度固定 562，看板娘盒子等宽
        int cardW = 562;
        int textW = cardW - CARD_PAD * 2;
        int CANVAS_W = CONTENT_X + COLS * cardW + (COLS - 1) * COL_GAP + CONTENT_X;

        int[] colX = new int[COLS];
        for (int c = 0; c < COLS; c++) {
            colX[c] = CONTENT_X + c * (cardW + COL_GAP);
        }

        // 预计算卡片高度
        int[] cardHeights = new int[n];
        for (int i = 0; i < n; i++) {
            cardHeights[i] = calcCardHeight(cards.get(i));
        }

        // 列流式：每列独立 Y，短列不拖长列
        int[] colEndY = new int[COLS];
        java.util.Arrays.fill(colEndY, CONTENT_START_Y);
        for (int i = 0; i < n; i++) {
            int col = i % COLS;
            colEndY[col] += cardHeights[i] + COL_GAP;
        }
        for (int c = 0; c < COLS; c++) {
            if (colEndY[c] > CONTENT_START_Y) colEndY[c] -= COL_GAP;
        }

        int standingX = colX[1];
        int totalHeight;
        if (isOdd) {
            // 看板娘从右列底部开始，与左列共享垂直空间
            totalHeight = Math.max(colEndY[0], colEndY[1] + cardW);
        } else {
            int maxEnd = CONTENT_START_Y;
            for (int c = 0; c < COLS; c++) {
                maxEnd = Math.max(maxEnd, colEndY[c]);
            }
            totalHeight = maxEnd + 10 + cardW + 30;
        }

        ImageCombiner cb = new ImageCombiner(CANVAS_W, totalHeight, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, totalHeight);
        cb.drawTooRoundRect();

        // 标题
        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 44))
                .addCenteredText("1999 日历季节信息", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 50,
                CANVAS_W - CONTENT_X, TITLE_Y + 50);

        // 季节基本信息
        String seasonName = calendar.getSeason() != null ? calendar.getSeason().getName() : "未知";
        String iter = calendar.getYearIteration() != null ? "第 " + calendar.getYearIteration() + " 次" : "未知";
        String ver = calendar.getVersion() != null ? calendar.getVersion().toString() : "未知";
        String info = "季节: " + seasonName + "    |    年份迭代: " + iter + "    |    版本: " + ver;
        cb.setColor(TEXT_SECONDARY_COLOR).setFont(FONT.deriveFont(22f));
        int infoW = cb.getFontMetrics(FONT.deriveFont(22f)).stringWidth(info);
        cb.addText(info, (CANVAS_W - infoW) / 2, TITLE_Y + 95);

        // 列流式绘制
        int[] drawY = new int[COLS];
        java.util.Arrays.fill(drawY, CONTENT_START_Y);
        for (int i = 0; i < n; i++) {
            int col = i % COLS;
            drawMonthCard(cb, cards.get(i), colX[col], drawY[col], cardW, cardHeights[i], textW);
            drawY[col] += cardHeights[i] + COL_GAP;
        }

        // 看板娘：isOdd 从右列底部，否则画布底部
        int standingY = isOdd ? colEndY[1] : totalHeight - cardW;
        cb.drawStandingAt(standingX, standingY, cardW, cardW);
        addFooter(cb, totalHeight - 25);
        cb.combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }

    private static int calcCardHeight(MonthCard card) {
        int h = CARD_PAD;                    // 顶部
        h += 40;                              // 月份标题
        h += 8;                               // 分隔线
        for (KnownCalendarSeasons.Days day : card.days) {
            h += 30;                           // 日期行
            if (day.getEvents() != null) {
                h += day.getEvents().size() * 30;
            }
        }
        h += CARD_PAD;                        // 底部
        return Math.max(h, 100);
    }

    private static void drawMonthCard(ImageCombiner cb, MonthCard card,
                                      int cardX, int cardY, int cardW, int cardH, int textW) {
        int innerX = cardX + CARD_PAD;
        int rightX = innerX + textW;
        Color seasonColor = getSeasonColor(card.month);

        // 卡片背景 + 顶部季节色强调条
        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(seasonColor).fillRect(cardX + CARD_RADIUS, cardY + 2, cardW - 2 * CARD_RADIUS, 5);
        cb.setColor(DIVIDER_COLOR).setStroke(1)
                .drawRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);

        int cy = cardY + CARD_PAD;

        // 月份标题
        cb.setColor(seasonColor).setFont(FONT.deriveFont(Font.BOLD, 28f));
        cb.addText(card.month + "月", innerX, cy + 30);
        cy += 40;

        // 分隔线
        cb.setColor(DIVIDER_COLOR).drawLine(innerX, cy + 4, rightX, cy + 4);
        cy += 12;

        // 日期事件
        Font dayFont = FONT.deriveFont(Font.BOLD, 20f);
        Font eventFont = FONT.deriveFont(18f);

        for (KnownCalendarSeasons.Days day : card.days) {
            // 日期
            String dayText = String.format("%d月%d日", day.getMonth(), day.getDay());
            cb.setColor(TEXT_COLOR).setFont(dayFont);
            cb.addText(dayText, innerX, cy + 22);
            cy += 30;

            // 事件
            if (day.getEvents() != null) {
                for (KnownCalendarSeasons.Events event : day.getEvents()) {
                    String label;
                    Color eventColor;
                    if (event.getType() != null) {
                        eventColor = switch (event.getType()) {
                            case CET_CHALLENGE -> {
                                label = "[任务] ";
                                yield CHALLENGE_COLOR;
                            }
                            case CET_REWARD -> {
                                label = "[奖励] ";
                                yield REWARD_COLOR;
                            }
                            case CET_UPGRADE -> {
                                label = "[加成] ";
                                yield UPGRADE_COLOR;
                            }
                            default -> {
                                label = "";
                                yield TEXT_SECONDARY_COLOR;
                            }
                        };
                    } else {
                        label = "";
                        eventColor = TEXT_SECONDARY_COLOR;
                    }
                    String desc = getEventDesc(event, label);
                    cb.setColor(eventColor).setFont(eventFont);
                    cb.addText(desc != null ? desc : "-", innerX + 15, cy + 20);
                    cy += 30;
                }
            }
        }
    }

    private static String getEventDesc(KnownCalendarSeasons.Events event, String label) {
        String desc = switch (event.getType()) {
            case CET_CHALLENGE -> event.getChallenge();
            case CET_REWARD -> event.getReward();
            case CET_UPGRADE -> event.getUpgrade();
        };
        if (desc == null || desc.isEmpty()) return label;
        // 路径(含/)未翻译 → 取尾段；否则直接使用翻译后的显示名
        if (desc.contains("/")) {
            int lastSlash = desc.lastIndexOf('/');
            return label + desc.substring(lastSlash + 1);
        }
        return label + desc;
    }

    record MonthCard(int month, List<KnownCalendarSeasons.Days> days) {
    }

    // 季节卡片颜色数据
    record SeasonCard(Color color, int[] months) {
    }
}
