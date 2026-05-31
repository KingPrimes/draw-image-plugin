package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.RewardPool;
import io.github.kingprimes.model.enums.RarityEnum;
import io.github.kingprimes.model.worldstate.Job;
import io.github.kingprimes.model.worldstate.SyndicateMission;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 集团任务图像绘制实现类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawSyndicateImage {

    private static final int CARD_MIN_HEIGHT = 250;
    private static final int CARD_MARGIN_X = 35;
    private static final int CARD_MARGIN_Y = 30;

    private DefaultDrawSyndicateImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawSyndicateImage class");
    }

    /**
     * 绘制集团任务图像
     *
     * @param sm 集团任务数据
     * @return 图像字节数组
     */
    public static byte[] drawSyndicateImage(SyndicateMission sm) {
        if (sm == null) {
            return new byte[0];
        }

        // 判断渲染模式
        if (sm.getNodes() != null && !sm.getNodes().isEmpty()) {
            return drawNodesView(sm);
        } else if (sm.getJobs() != null && !sm.getJobs().isEmpty()) {
            return drawJobsView(sm);
        }

        return new byte[0];
    }

    /**
     * 绘制Nodes视图
     *
     * @param sm 集团任务数据
     * @return 图像字节数组
     */
    private static byte[] drawNodesView(SyndicateMission sm) {
        List<String> nodes = sm.getNodes();
        if (nodes == null || nodes.isEmpty()) {
            return new byte[0];
        }

        // 计算图像高度
        int nodeHeight = nodes.size() * 50;
        int totalHeight = IMAGE_MARGIN_TOP + IMAGE_TITLE_HEIGHT + nodeHeight + IMAGE_FOOTER_HEIGHT;

        // 创建图像合成器
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, totalHeight, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        // 设置背景色
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, IMAGE_WIDTH, totalHeight)
                .drawTooRoundRect()
                .drawStandingAt(IMAGE_WIDTH, totalHeight, STANDING_RATIO);

        // 绘制标题
        String title = sm.getTag() != null ? sm.getTag().getName() + " - 节点" : "集团任务 - 节点";
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 32))
                .addCenteredText(title, IMAGE_MARGIN_TOP + 30);

        // 绘制节点列表
        int y = IMAGE_MARGIN_TOP + IMAGE_TITLE_HEIGHT + 50;
        combiner.setFont(FONT.deriveFont(24f))
                .setColor(TEXT_COLOR);

        for (String node : nodes) {
            combiner.addCenteredText("• " + node, y);
            y += 50;
        }

        // 添加底部署名
        addFooter(combiner, totalHeight - IMAGE_FOOTER_HEIGHT);

        // 合成并返回图像
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }

    /**
     * 绘制Jobs视图
     *
     * @param sm 集团任务数据
     * @return 图像字节数组
     */
    private static byte[] drawJobsView(SyndicateMission sm) {
        int IMAGE_WIDTH = 1600;
        int CONTENT_X = IMAGE_MARGIN;
        int COLS = 3;

        List<Job> jobs = sm.getJobs();
        if (jobs == null || jobs.isEmpty()) {
            return new byte[0];
        }
        int n = jobs.size();
        boolean isOdd = n % COLS != 0;

        // 看板娘盒子 + 卡片列宽
        box sz = scaleByPct(IMAGE_WIDTH, IMAGE_WIDTH, STANDING_RATIO);
        int cardsContentW = IMAGE_WIDTH - CONTENT_X * 2;
        int cardW = (cardsContentW - CARD_MARGIN_X * (COLS - 1)) / COLS;
        int textMaxW = cardW - 40;

        int[] colX = new int[COLS];
        for (int c = 0; c < COLS; c++) {
            colX[c] = CONTENT_X + c * (cardW + CARD_MARGIN_X);
        }

        // 预计算卡片高度 + 列流式 Y 终点
        int[] cardHeights = new int[n];
        for (int i = 0; i < n; i++) {
            cardHeights[i] = calculateJobCardHeight(jobs.get(i), textMaxW);
        }

        int startY = IMAGE_MARGIN_TOP + IMAGE_TITLE_HEIGHT + 25;
        int[] colEndY = new int[COLS];
        Arrays.fill(colEndY, startY);
        for (int i = 0; i < n; i++) {
            int col = i % COLS;
            colEndY[col] += cardHeights[i] + CARD_MARGIN_Y;
        }
        for (int c = 0; c < COLS; c++) {
            if (colEndY[c] > startY) colEndY[c] -= CARD_MARGIN_Y;
        }

        int standingX = IMAGE_WIDTH - sz.x();
        int standingY;
        if (isOdd) {
            // 右列有空白：取左+中列的最大底部
            standingY = Math.max(colEndY[0], colEndY[1]);
        } else {
            // 三列全满：看板娘在所有卡片下方
            int maxEnd = startY;
            for (int c = 0; c < COLS; c++) {
                maxEnd = Math.max(maxEnd, colEndY[c]);
            }
            standingY = maxEnd + 10;
        }
        int totalHeight = standingY + sz.y();

        // 创建图像合成器
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, totalHeight, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, IMAGE_WIDTH, totalHeight)
                .drawTooRoundRect();

        String title = sm.getTag() != null ? sm.getTag().getName() + " - 赏金任务" : "集团任务";
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 32))
                .addCenteredText(title, IMAGE_MARGIN_TOP + 30);

        // 列流式绘制：每列独立 Y 跟踪
        int[] drawY = new int[COLS];
        Arrays.fill(drawY, startY);

        for (int i = 0; i < n; i++) {
            int col = i % COLS;
            int cardHeight = cardHeights[i];

            BufferedImage cardImage = drawJobCard(
                    new ImageCombiner(cardW, cardHeight, ImageCombiner.OutputFormat.PNG),
                    jobs.get(i), cardHeight, cardW
            );
            combiner.drawImage(cardImage, colX[col], drawY[col]);
            drawY[col] += cardHeight + CARD_MARGIN_Y;
        }

        combiner.drawStandingAt(standingX, standingY, sz.x(), sz.y());
        addFooter(combiner, totalHeight - IMAGE_FOOTER_HEIGHT);

        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }

    /**
     * 绘制单个Job卡片
     *
     * @param combiner   图像合成器
     * @param job        任务数据
     * @param cardHeight 卡片高度
     * @return 卡片图像
     */
    private static BufferedImage drawJobCard(ImageCombiner combiner, Job job, int cardHeight, int cardW) {
        // 获取边框颜色
        Color borderColor = getJobBorderColor(job);

        // 绘制卡片背景和边框
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, cardW, cardHeight)
                .setColor(CARD_BACKGROUND_COLOR)
                .fillRoundRect(0, 0, cardW, cardHeight, 15, 15)
                .setColor(borderColor)
                .setStroke(5)
                .drawRoundRect(0, 0, cardW, cardHeight, 15, 15);

        int y = 30;

        // 1. 任务类型 + 特殊标识
        String typeText = job.getType() != null ? job.getType() : "未知任务";
        if (Boolean.TRUE.equals(job.getIsVault())) {
            typeText += " [保险库]";
        } else if (Boolean.TRUE.equals(job.getEndless())) {
            typeText += " [无尽]";
        }
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 20))
                .addCenteredText(typeText, y);

        y += 40;

        // 2. 敌人等级
        if (job.getMinLevel() != null && job.getMaxLevel() != null) {
            String levelText = "敌人等级: Lv." + job.getMinLevel() + " - Lv." + job.getMaxLevel();
            combiner.setColor(TEXT_COLOR)
                    .setFont(FONT.deriveFont(18f))
                    .addText(levelText, 20, y);
            y += 30;
        }

        // 3. 段位要求（如果存在）
        if (job.getMasteryReq() != null && job.getMasteryReq() > 0) {
            String mrText = "段位要求: MR " + job.getMasteryReq();
            combiner.setColor(new Color(0xE67E22))
                    .setFont(FONT.deriveFont(18f))
                    .addText(mrText, 20, y);
            y += 30;
        }

        // 4. 任务描述（完整显示，自动换行）
        if (job.getDesc() != null && !job.getDesc().isEmpty()) {
            String[] descLines = combiner.wrapText(job.getDesc(), cardW - 40);
            combiner.setColor(TEXT_COLOR)
                    .setFont(FONT.deriveFont(18f));
            for (String line : descLines) {
                combiner.addText(line, 20, y);
                y += 25;
            }
            y += 10; // 额外间距
        }

        // 5. 奖励列表（显示所有奖励）
        if (job.getRewardPool() != null && job.getRewardPool().getRewards() != null) {
            combiner.setColor(TITLE_COLOR)
                    .setFont(FONT.deriveFont(Font.BOLD, 20f))
                    .addText("奖励:", 20, y);
            y += 25;

            List<RewardPool.Reward> rewards = job.getRewardPool().getRewards();

            // 显示所有奖励
            for (RewardPool.Reward reward : rewards) {
                Color rarityColor = getRarityColor(reward.getRarity());
                String rewardText = "  • " + reward.getItem() + " x" + reward.getItemCount();

                combiner.setColor(rarityColor)
                        .setFont(FONT.deriveFont(18f))
                        .addText(rewardText, 20, y);
                y += 25;
            }
        }

        // 6. 经验值（底部固定位置）
        if (job.getXpAmounts() != null && !job.getXpAmounts().isEmpty()) {
            int totalXP = job.getXpAmounts().stream()
                    .mapToInt(Integer::intValue).sum();
            String xpText = "声望奖励: " + totalXP;
            combiner.setColor(new Color(0x27AE60))
                    .setFont(FONT.deriveFont(Font.BOLD, 18f))
                    .addText(xpText, 20, cardHeight - 30);
        }

        return combiner.getCombinedImage();
    }

    /**
     * 计算单个Job卡片的高度
     *
     * @param job 任务数据
     * @return 卡片高度
     */
    private static int calculateJobCardHeight(Job job, int textMaxW) {
        int height = 30; // 顶部任务类型
        height += 40;    // 敌人等级

        // 段位要求（如果存在）
        if (job.getMasteryReq() != null && job.getMasteryReq() > 0) {
            height += 30;
        }

        // 任务描述（动态计算行数）
        if (job.getDesc() != null && !job.getDesc().isEmpty()) {
            int descLines = calculateTextLines(job.getDesc(), textMaxW);
            height += descLines * 25 + 10;
        }

        // 奖励标题
        height += 30;

        // 奖励列表（所有奖励）
        if (job.getRewardPool() != null && job.getRewardPool().getRewards() != null) {
            int rewardCount = job.getRewardPool().getRewards().size();
            height += rewardCount * 25;
        }

        // 经验值
        height += 40;

        // 内边距
        height += 20;

        // 确保最小高度
        return Math.max(height, CARD_MIN_HEIGHT);
    }

    /**
     * 计算文本行数
     *
     * @param text 文本内容
     * @return 行数
     */
    private static int calculateTextLines(String text, int maxWidth) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        Font font = FONT.deriveFont((float) 18);
        BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tempImage.createGraphics();
        g2.setFont(font);
        FontMetrics metrics = g2.getFontMetrics();

        String[] words = text.split(" ");
        int lines = 1;
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
            int lineWidth = metrics.stringWidth(testLine);

            if (lineWidth > maxWidth) {
                lines++;
                currentLine = new StringBuilder(word);
            } else {
                currentLine = new StringBuilder(testLine);
            }
        }

        g2.dispose();
        return lines;
    }


    /**
     * 根据任务类型获取边框颜色
     *
     * @param job 任务数据
     * @return 边框颜色
     */
    private static Color getJobBorderColor(Job job) {
        if (Boolean.TRUE.equals(job.getIsVault())) {
            return new Color(0x9B59B6); // 紫色 - 保险库
        }
        if (Boolean.TRUE.equals(job.getEndless())) {
            return new Color(0xE67E22); // 橙色 - 无尽
        }
        return new Color(0x95A5A6); // 灰色 - 普通
    }

    /**
     * 根据稀有度获取颜色
     *
     * @param rarity 稀有度枚举
     * @return 对应的颜色
     */
    private static Color getRarityColor(RarityEnum rarity) {
        if (rarity == null) {
            return TEXT_COLOR;
        }

        return switch (rarity) {
            case COMMON -> VOID_T2_COLOR; // #75562B
            case UNCOMMON -> VOID_T3_COLOR; // #9F9E9E
            case RARE -> VOID_T4_COLOR; // #C1BE39
            case LEGENDARY -> VOID_T5_COLOR; // #872A2C
        };
    }
}