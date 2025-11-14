package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.Arbitration;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认有价值的仲裁任务图片绘制工具类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawArbitrationsImage {

    private static final int ARBITRATIONS_ITEM_HEIGHT = 240;

    private static final Color ARBITRATION_WORTH_COLOR = new Color(0x27ae60);

    /**
     * 绘制多个有价值的仲裁任务图像
     *
     * @param arbitrations 仲裁任务数据列表
     * @return 生成的仲裁任务图像的 PNG 格式字节数组
     */
    public static byte[] drawArbitrationsImage(List<Arbitration> arbitrations) {
        if (arbitrations == null || arbitrations.isEmpty()) {
            return new byte[0];
        }

        // 过滤出值得参与的仲裁任务
        List<Arbitration> worthArbitrations = arbitrations.stream()
                .filter(Arbitration::isWorth)
                .limit(5)
                .toList();

        if (worthArbitrations.isEmpty()) {
            return new byte[0];
        }

        // 计算图像高度
        int contentHeight = worthArbitrations.size() * ARBITRATIONS_ITEM_HEIGHT;
        int totalHeight = IMAGE_MARGIN + IMAGE_MARGIN_TOP + contentHeight + IMAGE_FOOTER_HEIGHT + IMAGE_MARGIN;

        // 创建画布
        ImageCombiner combiner = new ImageCombiner(IMAGE_WIDTH, totalHeight, ImageCombiner.OutputFormat.PNG);

        // 设置背景
        combiner.setColor(Color.WHITE)
                .fillRect(0, 0, IMAGE_WIDTH, totalHeight);

        // 标题
        String title = "有价值的仲裁任务列表";
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT)
                .addCenteredText(title, IMAGE_MARGIN + IMAGE_MARGIN_TOP / 2)
                .drawTooRoundRect()
                .drawStandingDrawing();

        int textY = IMAGE_MARGIN_TOP;
        // 绘制每个仲裁任务
        combiner.setFont(FONT);
        for (Arbitration arbitration : worthArbitrations) {
            textY += IMAGE_MARGIN * 2;
            // 节点
            combiner.setColor(TEXT_COLOR)
                    .addText("节点: " + arbitration.getNode(), IMAGE_MARGIN, textY);
            // 敌人
            textY += IMAGE_MARGIN;
            combiner.setColor(TEXT_COLOR)
                    .addText("敌人: " + arbitration.getEnemy(), IMAGE_MARGIN, textY);

            // 任务类型
            textY += IMAGE_MARGIN;
            combiner.setColor(TEXT_COLOR)
                    .addText("任务类型: " + arbitration.getType(), IMAGE_MARGIN, textY);

            // 时间信息
            textY += IMAGE_MARGIN;
            if (arbitration.getExpiry() != null) {
                combiner.setColor(TEXT_COLOR)
                        .addText("结束时间: " + arbitration.getTimeLeft(), IMAGE_MARGIN, textY);
            }
            textY += IMAGE_MARGIN;
            // 价值标识
            String worthText = "值得参与";
            combiner.setColor(ARBITRATION_WORTH_COLOR)
                    .addText(worthText,
                            IMAGE_MARGIN,
                            textY);
        }

        // 添加底部署名
        addFooter(combiner, totalHeight - IMAGE_FOOTER_HEIGHT + 10);

        // 合并图像
        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }
}