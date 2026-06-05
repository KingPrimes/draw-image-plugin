package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.enums.FactionEnum;
import io.github.kingprimes.model.enums.IconEnum;
import io.github.kingprimes.model.worldstate.AllCycle;

import java.awt.*;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 平原循环卡片渲染器 — 三列卡片网格 + 右侧看板娘
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawAllCycleImage {

    private static final int CANVAS_W = 1450;
    private static final int CONTENT_X = 50;
    private static final int COLS = 3;
    private static final int COL_GAP = 20;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 20;
    private static final int ROW_GAP = 20;

    private static final int TITLE_Y = 80;
    private static final int CONTENT_START_Y = 150;

    private static final Font ICON_FONT = FONT_WARFRAME_ICON.deriveFont(Font.PLAIN, 80f);
    private static final Font CARD_TITLE_FONT = FONT.deriveFont(Font.BOLD,26);
    private static final Font STATE_FONT = FONT.deriveFont(Font.BOLD,22);
    private static final Font TIME_FONT = FONT.deriveFont(Font.BOLD,20);

    private DefaultDrawAllCycleImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawAllCycleImage(AllCycle allCycle) {
        java.util.List<CycleCard> cards = new java.util.ArrayList<>();

        cards.add(new CycleCard("地球",
                allCycle.getEarthCycle().getState(),
                allCycle.getEarthCycle().getTimeLeft(),
                allCycle.getEarthCycle().isDay() ? IconEnum.SUN.getIcon() : IconEnum.NIGHT.getIcon(),
                allCycle.getEarthCycle().isDay() ? ALL_CYCLE_WARM_COLOR : ALL_CYCLE_COLD_COLOR));

        cards.add(new CycleCard("夜灵平野",
                allCycle.getCetusCycle().getState(),
                allCycle.getCetusCycle().getTimeLeft(),
                allCycle.getCetusCycle().getIsDay() ? IconEnum.SUN.getIcon() : IconEnum.NIGHT.getIcon(),
                allCycle.getCetusCycle().getIsDay() ? ALL_CYCLE_WARM_COLOR : ALL_CYCLE_COLD_COLOR));

        cards.add(new CycleCard("福尔图娜",
                allCycle.getVallisCycle().getState(),
                allCycle.getVallisCycle().getTimeLeft(),
                allCycle.getVallisCycle().isWarm() ? IconEnum.SUN.getIcon() : IconEnum.COLD.getIcon(),
                allCycle.getVallisCycle().isWarm() ? ALL_CYCLE_WARM_COLOR : ALL_CYCLE_COLD_COLOR));

        boolean cambionWarm = "FASS".equals(allCycle.getCambionCycle().getActive());
        cards.add(new CycleCard("魔胎之境",
                allCycle.getCambionCycle().getActive(),
                allCycle.getCambionCycle().getTimeLeft(),
                cambionWarm ? IconEnum.SUN.getIcon() : IconEnum.NIGHT.getIcon(),
                cambionWarm ? ALL_CYCLE_WARM_COLOR : ALL_CYCLE_COLD_COLOR));

        boolean zarimanCorpus = allCycle.getZarimanCycle().isCorpus();
        cards.add(new CycleCard("扎里曼",
                allCycle.getZarimanCycle().getState(),
                allCycle.getZarimanCycle().getTimeLeft(),
                zarimanCorpus ? FactionEnum.FC_CORPUS.getIcon() : FactionEnum.FC_GRINEER.getIcon(),
                zarimanCorpus ? ALL_CYCLE_WARM_COLOR : ALL_CYCLE_COLD_COLOR));

        int n = cards.size();
        int rows = (int) Math.ceil((double) n / COLS);
        int lastRowY = CONTENT_START_Y + (rows - 1) * (CARD_H() + ROW_GAP);

        box sz = scaleByPct(CANVAS_W, CANVAS_W, STANDING_RATIO);
        int cardsContentW = CANVAS_W - CONTENT_X - sz.x() - 30;
        int cardW = (cardsContentW - COL_GAP * (COLS - 1)) / COLS;
        int[] colX = new int[COLS];
        for (int c = 0; c < COLS; c++) {
            colX[c] = CONTENT_X + c * (cardW + COL_GAP);
        }

        int standingX = CANVAS_W - sz.x();
        int standingY;
        standingY = lastRowY;
        int canvasH = standingY + sz.y();

        ImageCombiner cb = new ImageCombiner(CANVAS_W, canvasH, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, canvasH);
        cb.drawTooRoundRect();

        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 44))
                .addCenteredText("平原查询结果", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 55, CONTENT_X + cardsContentW, TITLE_Y + 55);

        for (int i = 0; i < n; i++) {
            int row = i / COLS;
            int col = i % COLS;
            drawCycleCard(cb, cards.get(i), colX[col],
                    CONTENT_START_Y + row * (CARD_H() + ROW_GAP), cardW);
        }

        return getBytes(sz, standingX, standingY, canvasH, cb);
    }

    private static int CARD_H() {
        return 210;
    }

    private static void drawCycleCard(ImageCombiner cb, CycleCard card, int cardX, int cardY, int cardW) {
        int cardH = CARD_H();
        int innerX = cardX + CARD_PAD;
        int innerW = cardW - CARD_PAD * 2;

        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(cardX, cardY, cardW, cardH, CARD_RADIUS, CARD_RADIUS);

        // 顶部强调条
        cb.setColor(card.color).fillRect(cardX + CARD_RADIUS, cardY + 2, cardW - 2 * CARD_RADIUS, 4);

        // 区域名称
        cb.setColor(TITLE_COLOR).setFont(CARD_TITLE_FONT);
        cb.addText(card.name, innerX, cardY + 34);

        // 状态图标 (居中)
        cb.setColor(card.color).setFont(ICON_FONT);
        int iconW = cb.getFontMetrics(ICON_FONT).stringWidth(card.icon);
        cb.addText(card.icon, cardX + (cardW - iconW) / 2, cardY + 118);

        // 状态文字 (图标下方)
        cb.setColor(card.color).setFont(STATE_FONT);
        int stateW = cb.getFontMetrics(STATE_FONT).stringWidth(card.state);
        cb.addText(card.state, cardX + (cardW - stateW) / 2, cardY + 150);

        // 剩余时间 (右下角)
        cb.setColor(TEXT_SECONDARY_COLOR).setFont(TIME_FONT);
        int timeW = cb.getFontMetrics(TIME_FONT).stringWidth("剩余: " + card.timeLeft);
        cb.addText("剩余: " + card.timeLeft, innerX + innerW - timeW, cardY + cardH - 22);
    }

    private record CycleCard(String name, String state, String timeLeft, String icon, Color color) {
    }
}
