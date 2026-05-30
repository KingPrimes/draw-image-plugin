package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.DuvalierCycle;
import io.github.kingprimes.model.worldstate.EndlessXpChoices;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 双衍王境卡片渲染器 — 情绪展示卡 + 双列选择卡
 * <p>对应 Python card_duviri.py 完整重写</p>
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawDuviriCycleImage {

    private static final int CANVAS_W = 1000;
    private static final int CONTENT_X = 60;
    private static final int CONTENT_W = 880;
    private static final int CARD_RADIUS = 14;
    private static final int COL_GAP = 20;
    private static final int CARD_W = (CONTENT_W - COL_GAP) / 2;
    private static final int TITLE_Y = 70;
    private static final int FOOTER_OFFSET = 55;
    private static final int MAX_ITEMS = 7;
    private static final int ITEM_H = 32;

    private static final Color EM_SAD = EMOTION_SAD_COLOR;
    private static final Color EM_FEAR = EMOTION_FEAR_COLOR;
    private static final Color EM_JOY = EMOTION_JOY_COLOR;
    private static final Color EM_ANGER = EMOTION_ANGER_COLOR;
    private static final Color EM_ENVY = EMOTION_ENVY_COLOR;

    private DefaultDrawDuviriCycleImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawDuviriCycleImage class");
    }

    public static byte[] drawDuviriCycleImage(DuvalierCycle cycle) {
        if (cycle == null) return new byte[0];

        List<EndlessXpChoices> choices = cycle.getChoices();
        List<String> normalItems = List.of();
        List<String> hardItems = List.of();
        if (choices != null) {
            for (EndlessXpChoices c : choices) {
                if (c.getCategory() == EndlessXpChoices.Category.EXC_NORMAL) normalItems = c.getChoices();
                if (c.getCategory() == EndlessXpChoices.Category.EXC_HARD) hardItems = c.getChoices();
            }
        }

        int maxItems = Math.max(normalItems.size(), hardItems.size());
        int displayed = Math.min(maxItems, MAX_ITEMS);
        int choiceCardsH = 80 + displayed * ITEM_H + 30;
        int canvasH = 130 + 110 + 40 + choiceCardsH + 240 + FOOTER_OFFSET;

        ImageCombiner cb = new ImageCombiner(CANVAS_W, canvasH, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, canvasH);
        cb.drawTooRoundRect();

        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 48))
                .addCenteredText("双衍王境", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 68, CONTENT_X + CONTENT_W, TITLE_Y + 68);

        // ---- 情绪展示卡 ----
        String emotion = cycle.getState() != null ? cycle.getState() : "喜悦";
        Color emotionColor = getEmotionColor(emotion);
        int emotionCardW = 420;
        int emotionCardH = 110;
        int emotionCardX = (CANVAS_W - emotionCardW) / 2;
        int emotionCardY = 130;

        cb.setColor(CARD_BACKGROUND_COLOR)
                .fillRoundRect(emotionCardX, emotionCardY, emotionCardW, emotionCardH, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(emotionColor).setStroke(3)
                .drawRoundRect(emotionCardX, emotionCardY, emotionCardW, emotionCardH, CARD_RADIUS, CARD_RADIUS);

        String timeLeft = cycle.getTimeLeft() != null ? cycle.getTimeLeft() : "";
        cb.setColor(TEXT_SECONDARY_COLOR).setFont(FONT.deriveFont(18f));
        cb.addCenteredText("当前情绪", emotionCardY + 25);
        cb.setColor(emotionColor).setFont(FONT.deriveFont(Font.BOLD, 40));
        cb.addCenteredText(emotion, emotionCardY + 65);
        if (!timeLeft.isEmpty()) {
            cb.setColor(ACCENT_COLOR).setFont(FONT.deriveFont(18f));
            cb.addCenteredText("剩余: " + timeLeft, emotionCardY + 95);
        }

        // ---- 选择卡 ----
        int cardsY = emotionCardY + emotionCardH + 40;
        int leftX = CONTENT_X;
        int rightX = CONTENT_X + CARD_W + COL_GAP;

        drawChoiceCard(cb, "普通", normalItems, leftX, cardsY, ACCENT_GOLD_COLOR);
        drawChoiceCard(cb, "钢铁之路", hardItems, rightX, cardsY, ACCENT_COLOR);

        cb.drawStandingAt(CONTENT_X + CONTENT_W - 300, canvasH - 450, 300, 450);
        addFooter(cb, canvasH - FOOTER_OFFSET);

        cb.combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }

    private static void drawChoiceCard(ImageCombiner cb, String title, List<String> items,
                                        int cardX, int cardY, Color accent) {
        if (items == null) items = List.of();
        int displayed = Math.min(items.size(), MAX_ITEMS);
        int cardH = 80 + displayed * ITEM_H + 30;

        cb.setColor(CARD_BACKGROUND_COLOR)
                .fillRoundRect(cardX, cardY, CARD_W, cardH, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(accent).fillRect(cardX + CARD_RADIUS, cardY + 2, CARD_W - 2 * CARD_RADIUS, 4);

        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 24));
        cb.addCenteredText(title, cardY + 35);
        cb.setColor(DIVIDER_COLOR).drawLine(cardX + 20, cardY + 52, cardX + CARD_W - 20, cardY + 52);

        int itemY = cardY + 70;
        Font itemFont = FONT.deriveFont(20f);
        for (int i = 0; i < displayed; i++) {
            String item = items.get(i);
            if (item != null && item.length() > 22) item = item.substring(0, 20) + "..";
            cb.setColor(TEXT_COLOR).setFont(itemFont);
            cb.addText("• " + (item != null ? item : ""), cardX + 25, itemY + 8);
            itemY += ITEM_H;
        }
        if (items.isEmpty()) {
            cb.setColor(TEXT_MUTED_COLOR).setFont(FONT.deriveFont(20f));
            cb.addText("暂无", cardX + 25, cardY + 85);
        }
    }

    private static Color getEmotionColor(String emotion) {
        return switch (emotion) {
            case "悲伤" -> EM_SAD;
            case "恐惧" -> EM_FEAR;
            case "喜悦" -> EM_JOY;
            case "愤怒" -> EM_ANGER;
            case "嫉妒" -> EM_ENVY;
            default -> ACCENT_GOLD_COLOR;
        };
    }
}
