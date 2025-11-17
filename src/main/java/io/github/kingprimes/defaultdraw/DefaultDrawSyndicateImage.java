package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.RewardPool;
import io.github.kingprimes.model.enums.RarityEnum;
import io.github.kingprimes.model.worldstate.Job;
import io.github.kingprimes.model.worldstate.SyndicateMission;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 集团任务图像绘制实现类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawSyndicateImage {

    private static final int CARD_WIDTH = 550;
    private static final int CARD_MIN_HEIGHT = 250;
    private static final int CARD_MARGIN_X = 40;
    private static final int CARD_MARGIN_Y = 30;
    private static final int CARDS_PER_ROW = 2;

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
        int totalHeight = IMAGE_MARGIN_TOP + IMAGE_TITLE_HEIGHT + nodeHeight + IMAGE_FOOTER_HEIGHT + 100;

        // 创建图像合成器
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, totalHeight, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        // 设置背景色
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, IMAGE_WIDTH, totalHeight)
                .drawTooRoundRect()
                .drawStandingDrawing();

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

        List<Job> jobs = sm.getJobs();
        if (jobs == null || jobs.isEmpty()) {
            return new byte[0];
        }

        // 第一步：计算每个Job卡片的高度
        List<Integer> cardHeights = new ArrayList<>();
        for (Job job : jobs) {
            int cardHeight = calculateJobCardHeight(job);
            cardHeights.add(cardHeight);
        }

        // 第二步：计算总高度（每行2个卡片）
        int totalHeight = IMAGE_MARGIN_TOP + IMAGE_TITLE_HEIGHT;
        for (int i = 0; i < cardHeights.size(); i += CARDS_PER_ROW) {
            // 每行取两个卡片中较高的那个
            int rowHeight = cardHeights.get(i);
            if (i + 1 < cardHeights.size()) {
                rowHeight = Math.max(rowHeight, cardHeights.get(i + 1));
            }
            totalHeight += rowHeight + CARD_MARGIN_Y;
        }
        totalHeight += IMAGE_FOOTER_HEIGHT + 100; // 为看板娘预留空间

        // 创建图像合成器
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, totalHeight, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        // 设置背景色
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, IMAGE_WIDTH, totalHeight)
                .drawTooRoundRect()
                .drawStandingDrawing();

        // 绘制标题
        String title = sm.getTag() != null ? sm.getTag().getName() + " - 赏金任务" : "集团任务";
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 32))
                .addCenteredText(title, IMAGE_MARGIN_TOP + 30);

        // 绘制Job卡片
        int x = IMAGE_MARGIN;
        int y = IMAGE_MARGIN_TOP + IMAGE_TITLE_HEIGHT + 25;
        int cardIndex = 0;

        for (int i = 0; i < jobs.size(); i++) {
            Job job = jobs.get(i);
            int cardHeight = cardHeights.get(i);

            // 检查是否需要换行
            if (cardIndex > 0 && cardIndex % CARDS_PER_ROW == 0) {
                x = IMAGE_MARGIN;
                // 获取上一行的最大高度
                int prevRowMaxHeight = cardHeights.get(i - 1);
                if (i - 2 >= 0) {
                    prevRowMaxHeight = Math.max(prevRowMaxHeight, cardHeights.get(i - 2));
                }
                y += prevRowMaxHeight + CARD_MARGIN_Y;
            }

            // 绘制Job卡片
            BufferedImage cardImage = drawJobCard(
                    new ImageCombiner(CARD_WIDTH, cardHeight, ImageCombiner.OutputFormat.PNG),
                    job,
                    cardHeight
            );
            combiner.drawImage(cardImage, x, y);

            // 更新坐标
            x += CARD_WIDTH + CARD_MARGIN_X;
            cardIndex++;
        }

        // 添加底部署名
        addFooter(combiner, totalHeight - IMAGE_FOOTER_HEIGHT);

        // 合成并返回图像
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
    private static BufferedImage drawJobCard(ImageCombiner combiner, Job job, int cardHeight) {
        // 获取边框颜色
        Color borderColor = getJobBorderColor(job);

        // 绘制卡片背景和边框
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, CARD_WIDTH, cardHeight)
                .setColor(CARD_BACKGROUND_COLOR)
                .fillRoundRect(0, 0, CARD_WIDTH, cardHeight, 15, 15)
                .setColor(borderColor)
                .setStroke(5)
                .drawRoundRect(0, 0, CARD_WIDTH, cardHeight, 15, 15);

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
            String[] descLines = wrapText(combiner, job.getDesc());
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
    private static int calculateJobCardHeight(Job job) {
        int height = 30; // 顶部任务类型
        height += 40;    // 敌人等级

        // 段位要求（如果存在）
        if (job.getMasteryReq() != null && job.getMasteryReq() > 0) {
            height += 30;
        }

        // 任务描述（动态计算行数）
        if (job.getDesc() != null && !job.getDesc().isEmpty()) {
            int descLines = calculateTextLines(job.getDesc());
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
    private static int calculateTextLines(String text) {
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

            if (lineWidth > 510) {
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
     * 文本自动换行
     *
     * @param combiner 图像合成器
     * @param text     文本内容
     * @return 换行后的文本数组
     */
    private static String[] wrapText(ImageCombiner combiner, String text) {
        if (text == null || text.isEmpty()) {
            return new String[]{""};
        }

        return combiner.wrapText(text, 510);
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
            case COMMON -> ACTIVE_MISSION_VOID_T2_COLOR; // #75562B
            case UNCOMMON -> ACTIVE_MISSION_VOID_T3_COLOR; // #9F9E9E
            case RARE -> ACTIVE_MISSION_VOID_T4_COLOR; // #C1BE39
            case LEGENDARY -> ACTIVE_MISSION_VOID_T5_COLOR; // #872A2C
        };
    }
}