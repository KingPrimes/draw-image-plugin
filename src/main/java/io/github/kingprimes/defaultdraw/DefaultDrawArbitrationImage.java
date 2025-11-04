package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.image.ImageIOUtils;
import io.github.kingprimes.model.Arbitration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认仲裁任务图片绘制工具类
 *
 * @author KingPrimes
 * @version 1.0.0
 */
final class DefaultDrawArbitrationImage {

    private static final int ARBITRATION_IMAGE_WIDTH = 800;
    private static final int ARBITRATION_IMAGE_HEIGHT = 500;

    private static final Color ARBITRATION_WORTH_COLOR = new Color(0x27ae60);
    private static final Color ARBITRATION_NOT_WORTH_COLOR = new Color(0xe74c3c);

    /**
     * 绘制单个仲裁任务图像
     *
     * @param arbitration 仲裁任务数据
     * @return 生成的仲裁任务图像的 PNG 格式字节数组
     */
    public static byte[] drawArbitrationImage(Arbitration arbitration) {
        if (arbitration == null) {
            return new byte[0];
        }

        // 创建画布
        ImageCombiner combiner = new ImageCombiner(ARBITRATION_IMAGE_WIDTH, ARBITRATION_IMAGE_HEIGHT, ImageCombiner.OutputFormat.PNG);

        // 设置背景
        combiner.setColor(Color.WHITE)
                .fillRect(0, 0, ARBITRATION_IMAGE_WIDTH, ARBITRATION_IMAGE_HEIGHT)
                .drawTooRoundRect();

        int y = IMAGE_MARGIN;

        // 标题
        String title = "仲裁任务";
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT)
                .addCenteredText(title, y + IMAGE_MARGIN_TOP / 2);

        y += IMAGE_MARGIN_TOP;

        BufferedImage backgroundImage = ImageIOUtils.getRandomXiaoMeiWangImage();
        combiner.drawImageWithAspectRatio(backgroundImage,
                ARBITRATION_IMAGE_WIDTH - 260,
                ARBITRATION_IMAGE_HEIGHT - 360,
                280,
                320);
        // 任务信息
        combiner.setFont(FONT);

        // 节点
        y += IMAGE_ROW_HEIGHT;
        combiner.setColor(TEXT_COLOR)
                .addText("节点: " + arbitration.getNode(), IMAGE_MARGIN, y);

        // 敌人
        y += IMAGE_ROW_HEIGHT;
        combiner.setColor(TEXT_COLOR)
                .addText("敌人: " + arbitration.getEnemy(), IMAGE_MARGIN, y);

        // 任务类型
        y += IMAGE_ROW_HEIGHT;
        combiner.setColor(TEXT_COLOR)
                .addText("任务类型: " + arbitration.getType(), IMAGE_MARGIN, y);

        // 时间信息
        y += IMAGE_ROW_HEIGHT;
        if (arbitration.getActivation() != null) {
            combiner.setColor(TEXT_COLOR)
                    .addText("开始时间: " + arbitration.getActivation(), IMAGE_MARGIN, y);
        }

        y += IMAGE_ROW_HEIGHT;
        if (arbitration.getExpiry() != null) {
            combiner.setColor(TEXT_COLOR)
                    .addText("剩余时间: " + arbitration.getTimeLeft(), IMAGE_MARGIN, y);
        }

        // 价值判断
        y += IMAGE_ROW_HEIGHT + 10;
        String worthText = arbitration.isWorth() ? "值得参与" : "不值得参与";
        Color worthColor = arbitration.isWorth() ? ARBITRATION_WORTH_COLOR : ARBITRATION_NOT_WORTH_COLOR;

        combiner.setColor(worthColor)
                .addText(worthText, IMAGE_MARGIN, y);

        // 添加底部署名
        addFooter(combiner, ARBITRATION_IMAGE_HEIGHT - IMAGE_FOOTER_HEIGHT);

        // 合并图像
        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }
}