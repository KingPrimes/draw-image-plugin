package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.worldstate.Invasion;
import io.github.kingprimes.model.worldstate.Reward;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认入侵任务图片绘制工具类
 * <p>
 * 该类负责将入侵任务数据渲染为图像，包括节点信息、阵营对抗、进度条和奖励等元素
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawInvasionImage {

    // 图像尺寸常量
    private static final int INVASION_IMAGE_MIN_HEIGHT = 800;
    private static final int INVASION_IMAGE_MAX_HEIGHT = 2000;

    // 每行入侵任务的高度
    private static final int INVASION_ROW_HEIGHT = 200;

    // 颜色常量定义
    private static final Color ATTACKER_COLOR = new Color(0xFF6B6B); // 进攻方颜色 - 红色系
    private static final Color DEFENDER_COLOR = new Color(0x4CAF50); // 防守方颜色 - 绿色系
    private static final Color PROGRESS_BAR_BG_COLOR = new Color(0xDDDDDD); // 进度条背景色
    private static final Color PROGRESS_BAR_COLOR = new Color(0x4A90E2); // 进度条前景色

    /**
     * 私有构造函数，防止实例化该工具类
     */
    private DefaultDrawInvasionImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawInvasionImage class");
    }

    /**
     * 绘制入侵任务图像
     *
     * @param invasions 入侵任务数据列表
     * @return 生成的入侵任务图像的 PNG 格式字节数组
     */
    public static byte[] drawInvasionImage(List<Invasion> invasions) {
        // 如果没有入侵任务数据，则返回空字节数组
        if (invasions == null || invasions.isEmpty()) {
            return new byte[0];
        }

        // 根据入侵任务数量计算图像高度
        int height = calculateImageHeight(invasions.size());

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
                .drawTooRoundRect()
                // 绘制看板娘
                .drawStandingDrawing();

        // 绘制标题
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 36)) // 标题字体大小为36
                .addCenteredText("入侵任务", 80);

        // 绘制入侵任务列表
        int startY = 120;
        for (int i = 0; i < invasions.size(); i++) {
            Invasion invasion = invasions.get(i);
            int rowY = startY + i * INVASION_ROW_HEIGHT;

            // 绘制斑马纹背景效果，提高可读性
            if (i % 2 == 0) {
                combiner.setColor(new Color(0xFFFFFF, true)) // 半透明白色
                        .fillRect(IMAGE_MARGIN, rowY, IMAGE_WIDTH - 2 * IMAGE_MARGIN, INVASION_ROW_HEIGHT);
            }

            // 绘制单个入侵任务行
            drawInvasionRow(combiner, invasion, rowY);
        }

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
     * 根据入侵任务数量动态计算图像高度，确保内容完整显示
     *
     * @param invasionCount 入侵任务数量
     * @return 图像高度（在最小和最大高度之间）
     */
    private static int calculateImageHeight(int invasionCount) {
        int headerHeight = 120; // 标题区域高度
        int footerHeight = IMAGE_FOOTER_HEIGHT + 50; // 底部区域高度
        int contentHeight = invasionCount * INVASION_ROW_HEIGHT; // 内容区域高度
        int tableHeight = headerHeight + contentHeight + footerHeight; // 总高度

        // 确保图像高度在指定范围内
        return Math.min(Math.max(tableHeight, INVASION_IMAGE_MIN_HEIGHT), INVASION_IMAGE_MAX_HEIGHT);
    }

    /**
     * 绘制单个入侵任务行
     * 包括节点名称、阵营信息、进度条和奖励信息
     *
     * @param combiner 图像合成器实例
     * @param invasion 入侵任务数据
     * @param startY   起始Y坐标
     */
    private static void drawInvasionRow(ImageCombiner combiner, Invasion invasion, int startY) {
        // 绘制节点名称，使用32号粗体字体
        combiner.setColor(TEXT_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 32))
                .addText(invasion.getNode() != null ? invasion.getNode() : "未知节点",
                        IMAGE_MARGIN + 20, startY + 40);

        // 绘制阵营信息
        String attackerFaction = invasion.getFaction() != null ? invasion.getFaction() : "未知阵营";
        String defenderFaction = invasion.getDefenderFaction() != null ? invasion.getDefenderFaction() : "未知阵营";

        // 绘制进攻方阵营名称
        combiner.setColor(ATTACKER_COLOR)
                .setFont(FONT.deriveFont(28f)) // 使用28号字体
                .addText(attackerFaction, IMAGE_MARGIN + 20, startY + 80);

        // 绘制对阵标识
        combiner.setColor(TEXT_COLOR)
                .setFont(FONT.deriveFont(28f))
                .addText(" vs ", IMAGE_MARGIN + 200, startY + 80);

        // 绘制防守方阵营名称
        combiner.setColor(DEFENDER_COLOR)
                .setFont(FONT.deriveFont(28f))
                .addText(defenderFaction, IMAGE_MARGIN + 300, startY + 80);

        // 绘制进度条背景
        int progressBarX = IMAGE_MARGIN + 1120 - 450; // 进度条X坐标
        int progressBarY = startY + 45; // 进度条Y坐标
        int progressBarWidth = 400; // 进度条宽度
        int progressBarHeight = 35; // 进度条高度

        combiner.setColor(PROGRESS_BAR_BG_COLOR)
                .fillRoundRect(progressBarX, progressBarY, progressBarWidth, progressBarHeight, 15, 15);

        // 绘制进度条和进度文本
        if (invasion.getGoal() != null && invasion.getGoal() != 0 && invasion.getCount() != null) {
            // 计算进度百分比
            double progress = Math.abs(invasion.getCount()) / invasion.getGoal();
            progress = Math.min(progress, 1.0); // 确保不超过100%

            // 绘制进度条前景
            int progressWidth = (int) (progressBarWidth * progress);
            combiner.setColor(PROGRESS_BAR_COLOR)
                    .fillRoundRect(progressBarX, progressBarY, progressWidth, progressBarHeight, 15, 15);

            // 绘制进度百分比文本
            String progressText = String.format("%.1f%%", progress * 100);
            combiner.setColor(TEXT_COLOR)
                    .setFont(FONT.deriveFont(24f)) // 使用24号字体
                    .addText(progressText, progressBarX + progressBarWidth / 2 - 40,
                            progressBarY + progressBarHeight / 2 + 8);
        }

        // 绘制奖励信息
        int rewardY = startY + 140; // 奖励信息Y坐标

        // 绘制进攻方奖励
        if (invasion.getAttackerReward() != null && !invasion.getAttackerReward().isEmpty()) {
            Reward attackerReward = invasion.getAttackerReward().getFirst();
            if (attackerReward.getCountedItems() != null && !attackerReward.getCountedItems().isEmpty()) {
                Reward.Item item = attackerReward.getCountedItems().getFirst();
                if (item.getCount() != null && item.getName() != null) {
                    combiner.setColor(ATTACKER_COLOR)
                            .setFont(FONT.deriveFont(24f)) // 使用24号字体
                            .addText("进攻方: " + item.getCount() + "x " + item.getName(),
                                    IMAGE_MARGIN + 20, rewardY);
                }
            }
        }

        // 绘制防守方奖励
        if (invasion.getDefenderReward() != null &&
                invasion.getDefenderReward().getCountedItems() != null &&
                !invasion.getDefenderReward().getCountedItems().isEmpty()) {
            Reward.Item item = invasion.getDefenderReward().getCountedItems().getFirst();
            if (item.getCount() != null && item.getName() != null) {
                combiner.setColor(DEFENDER_COLOR)
                        .setFont(FONT.deriveFont(24f)) // 使用24号字体
                        .addText("防守方: " + item.getCount() + "x " + item.getName(),
                                IMAGE_MARGIN + 400, rewardY);
            }
        }
    }
}