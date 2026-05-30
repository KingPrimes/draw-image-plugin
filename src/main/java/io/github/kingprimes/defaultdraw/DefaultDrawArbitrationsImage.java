package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.Arbitration;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 仲裁列表卡片渲染器 — 对应 Python card_arbitration.py 列表模式
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawArbitrationsImage {

    private static final Color WORTH_COLOR = new Color(0x27ae60);

    private DefaultDrawArbitrationsImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawArbitrationsImage(List<Arbitration> arbitrations) {
        if (arbitrations == null || arbitrations.isEmpty()) return new byte[0];

        List<Arbitration> worthList = arbitrations.stream()
                .filter(Arbitration::isWorth).limit(5).toList();
        if (worthList.isEmpty()) return new byte[0];

        int contentH = worthList.size() * 120;
        int canvasH = IMAGE_MARGIN + IMAGE_MARGIN_TOP + contentH + IMAGE_FOOTER_HEIGHT + IMAGE_MARGIN + 200;

        ImageCombiner cb = new ImageCombiner(IMAGE_WIDTH, canvasH, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, IMAGE_WIDTH, canvasH);
        cb.drawTooRoundRect();

        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 40));
        cb.addCenteredText("有价值的仲裁任务列表", IMAGE_MARGIN + IMAGE_MARGIN_TOP / 2);
        cb.setColor(DIVIDER_COLOR)
                .drawLine(IMAGE_MARGIN + 20, IMAGE_MARGIN + IMAGE_MARGIN_TOP - 5,
                        IMAGE_WIDTH - IMAGE_MARGIN - 20, IMAGE_MARGIN + IMAGE_MARGIN_TOP - 5);

        int textY = IMAGE_MARGIN_TOP + 10;
        Font bodyFont = FONT.deriveFont(FONT_SIZE);

        for (Arbitration a : worthList) {
            textY += IMAGE_MARGIN * 2;
            cb.setColor(TEXT_COLOR).setFont(bodyFont);
            cb.addText("节点: " + (a.getNode() != null ? a.getNode() : "未知"), IMAGE_MARGIN, textY);
            textY += IMAGE_MARGIN;
            cb.setColor(TEXT_COLOR).setFont(bodyFont);
            cb.addText("敌人: " + a.getEnemyName(), IMAGE_MARGIN, textY);
            textY += IMAGE_MARGIN;
            cb.setColor(TEXT_COLOR).setFont(bodyFont);
            cb.addText("任务类型: " + (a.getType() != null ? a.getType() : "未知"), IMAGE_MARGIN, textY);
            textY += IMAGE_MARGIN;
            cb.setColor(TEXT_COLOR).setFont(bodyFont);
            cb.addText("开始时间: " + a.getActivationFormat(), IMAGE_MARGIN, textY);
            textY += IMAGE_MARGIN;
            cb.setColor(WORTH_COLOR).setFont(bodyFont);
            cb.addText("值得参与", IMAGE_MARGIN, textY);
        }

        cb.drawStandingAt(IMAGE_WIDTH, canvasH, STANDING_RATIO);
        addFooter(cb, canvasH - IMAGE_FOOTER_HEIGHT + 10);
        cb.combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }
}
