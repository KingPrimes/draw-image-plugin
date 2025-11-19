package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.LiteSorite;
import io.github.kingprimes.model.worldstate.Mission;

import java.io.ByteArrayOutputStream;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认执刑官猎杀任务图片绘制工具类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawLiteSoriteImage {

    private static final int LITE_SORITE_IMAGE_WIDTH = 900;
    private static final int LITE_SORITE_IMAGE_HEIGHT = 600;

    /**
     * 绘制执刑官猎杀任务图像
     *
     * @param liteSorite 执刑官猎杀任务数据
     * @return 生成的执刑官猎杀任务图像的 PNG 格式字节数组
     */
    public static byte[] drawLiteSoriteImage(LiteSorite liteSorite) {
        if (liteSorite == null) {
            return new byte[0];
        }

        // 计算图像高度基于任务数量
        int missionCount = liteSorite.getMissions() != null ? liteSorite.getMissions().size() : 0;
        int imageHeight = Math.max(LITE_SORITE_IMAGE_HEIGHT, 400 + missionCount * 60);

        // 创建画布
        ImageCombiner combiner = new ImageCombiner(LITE_SORITE_IMAGE_WIDTH, imageHeight, ImageCombiner.OutputFormat.PNG);

        // 设置背景
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, LITE_SORITE_IMAGE_WIDTH, imageHeight)
                .drawTooRoundRect()
                .drawStandingDrawing();

        int y = IMAGE_MARGIN;

        // 标题
        String title = "执刑官猎杀";
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT)
                .addCenteredText(title, y + IMAGE_MARGIN_TOP / 2);

        y += IMAGE_MARGIN_TOP;

        // 执刑官信息
        combiner.setFont(FONT);

        // Boss信息
        y += IMAGE_ROW_HEIGHT;
        combiner.setColor(TEXT_COLOR)
                .addText("Boss: " + liteSorite.getBoss(), IMAGE_MARGIN, y);

        // 结束时间信息
        if (liteSorite.getExpiry() != null) {
            combiner.setColor(TEXT_COLOR)
                    .addText("结束时间: " + liteSorite.getExpiry().getTime(), LITE_SORITE_IMAGE_WIDTH / 2 - 60, y);
        }

        // 任务列表标题
        y += IMAGE_ROW_HEIGHT + 10;
        combiner.setColor(TITLE_COLOR)
                .addText("任务列表:", IMAGE_MARGIN, y);

        // 任务列表
        if (liteSorite.getMissions() != null && !liteSorite.getMissions().isEmpty()) {
            y += 10;
            for (Mission mission : liteSorite.getMissions()) {
                y += IMAGE_ROW_HEIGHT;
                combiner.setColor(mission.getMissionTypeColor())
                        .addText("• " + mission.getMissionTypeName() + " - " + mission.getNode(), IMAGE_MARGIN + 20, y);
            }
        } else {
            y += IMAGE_ROW_HEIGHT;
            combiner.setColor(TEXT_COLOR)
                    .addText("暂无任务信息", IMAGE_MARGIN + 20, y);
        }

        // 添加底部署名
        addFooter(combiner, imageHeight - IMAGE_FOOTER_HEIGHT);

        // 合并图像
        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }
}