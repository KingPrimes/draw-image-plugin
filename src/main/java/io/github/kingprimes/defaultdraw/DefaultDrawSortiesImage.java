package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.Sortie;
import io.github.kingprimes.model.worldstate.Variant;

import java.awt.*;
import java.io.ByteArrayOutputStream;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 突击卡片渲染器 — 对应 Python card_sortie.py，格式同 LiteSortie
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawSortiesImage {

    private static final int SORTIE_IMAGE_WIDTH = 900;
    private static final int SORTIE_IMAGE_MIN_HEIGHT = 600;
    private static final int ROW_HEIGHT = 50;

    private DefaultDrawSortiesImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawSortiesImage class");
    }

    public static byte[] drawSortiesImage(Sortie sorties) {
        if (sorties == null) return new byte[0];

        int variantCount = sorties.getVariants() != null ? sorties.getVariants().size() : 0;
        long modifierCount = sorties.getVariants() != null
                ? sorties.getVariants().stream().filter(v -> v.getModifierType() != null).count() : 0;
        int imageHeight = Math.max(SORTIE_IMAGE_MIN_HEIGHT,
                420 + variantCount * ROW_HEIGHT + (int) modifierCount * 28);

        ImageCombiner combiner = new ImageCombiner(SORTIE_IMAGE_WIDTH, imageHeight, ImageCombiner.OutputFormat.PNG);

        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, SORTIE_IMAGE_WIDTH, imageHeight)
                .drawTooRoundRect();

        // 标题
        combiner.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 44))
                .addCenteredText("突击任务", 70);
        // 分割线
        int contentX = IMAGE_MARGIN + 30;
        int contentW = SORTIE_IMAGE_WIDTH - 2 * (IMAGE_MARGIN + 30);
        combiner.setColor(DIVIDER_COLOR).drawLine(contentX, 138, contentX + contentW, 138);

        int y = 160;

        // Boss + 结束时间
        y += IMAGE_ROW_HEIGHT;
        combiner.setFont(FONT);
        combiner.setColor(TEXT_COLOR).addText("Boss: " + sorties.getBoss(), IMAGE_MARGIN, y);

        // 任务列表标题
        y += IMAGE_ROW_HEIGHT + 10;
        combiner.setColor(TITLE_COLOR).addText("任务列表:", IMAGE_MARGIN, y);

        if (sorties.getVariants() != null && !sorties.getVariants().isEmpty()) {
            y += 10;
            for (Variant variant : sorties.getVariants()) {
                y += ROW_HEIGHT;
                combiner.setColor(variant.getMissionTypeColor())
                        .addText("• " + variant.getMissionTypeName() + " - " + variant.getNode(),
                                IMAGE_MARGIN + 20, y);
                String modifier = variant.getModifierType() != null ? variant.getModifierTypeStr() : "";
                if (!modifier.isEmpty()) {
                    y += 40;
                    combiner.setColor(TEXT_SECONDARY_COLOR)
                            .addText("    • " + modifier, IMAGE_MARGIN + 40, y);
                }
            }
        } else {
            y += IMAGE_ROW_HEIGHT;
            combiner.setColor(TEXT_COLOR).addText("暂无任务信息", IMAGE_MARGIN + 20, y);
        }

        addFooter(combiner, imageHeight - IMAGE_FOOTER_HEIGHT);
        combiner.drawStandingAt(SORTIE_IMAGE_WIDTH, imageHeight, STANDING_RATIO)
                .combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }
}
