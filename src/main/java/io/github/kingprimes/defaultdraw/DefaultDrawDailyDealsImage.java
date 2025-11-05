package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.image.ImageIOUtils;
import io.github.kingprimes.model.worldstate.DailyDeals;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认每日特惠图片绘制工具类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawDailyDealsImage {

    private static final int DAILY_DEALS_IMAGE_WIDTH = 1000;
    private static final int DAILY_DEALS_IMAGE_HEIGHT = 600;

    // 原价颜色 - 红色系，用于显示划掉的原价
    private static final Color ORIGINAL_PRICE_COLOR = new Color(0xFF6B6B);
    // 现价颜色 - 绿色系，用于显示当前优惠价格
    private static final Color SALE_PRICE_COLOR = new Color(0x4CAF50);
    // 折扣百分比颜色 - 红色系，用于突出显示折扣力度
    private static final Color DISCOUNT_COLOR = new Color(0xFF6B6B);
    // 剩余数量颜色 - 绿色系，用于显示剩余可购买数量
    private static final Color REMAINING_COLOR = new Color(0x4CAF50);

    private DefaultDrawDailyDealsImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawDailyDealsImage class");
    }

    /**
     * 绘制每日特惠图像
     *
     * @param dailyDeal 每日特惠数据
     * @return 生成的每日特惠图像的 PNG 格式字节数组
     */
    public static byte[] drawDailyDealsImage(DailyDeals dailyDeal) {
        if (dailyDeal == null) {
            return new byte[0];
        }

        // 创建画布
        ImageCombiner combiner = new ImageCombiner(DAILY_DEALS_IMAGE_WIDTH, DAILY_DEALS_IMAGE_HEIGHT, ImageCombiner.OutputFormat.PNG);

        // 填充背景色
        combiner.setFont(FONT).setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, DAILY_DEALS_IMAGE_WIDTH, DAILY_DEALS_IMAGE_HEIGHT)
                // 绘制双层边框
                .drawTooRoundRect();

        BufferedImage backgroundImage = ImageIOUtils.getRandomXiaoMeiWangImage();
        int maxImageWidth = DAILY_DEALS_IMAGE_WIDTH / 3;
        int maxImageHeight = DAILY_DEALS_IMAGE_HEIGHT - IMAGE_MARGIN;
        combiner.drawImageWithAspectRatio(backgroundImage, DAILY_DEALS_IMAGE_WIDTH / 2 + 160, 25, maxImageWidth, maxImageHeight);

        // 绘制标题
        combiner.setColor(TITLE_COLOR).addCenteredText("每日特惠", 80);

        int startY = 120;

        // 绘制物品名称
        combiner.setColor(TEXT_COLOR).addText("物品名称:", IMAGE_MARGIN, startY);

        combiner.addText(dailyDeal.getItem(), 260, startY);

        startY += IMAGE_ROW_HEIGHT;

        // 绘制价格信息
        combiner.addText("原价/现价:", IMAGE_MARGIN, startY);

        // 原价
        combiner.setColor(ORIGINAL_PRICE_COLOR).addText(String.valueOf(dailyDeal.getOriginalPrice()), 260, startY);

        // 绘制删除线
        int originalPriceWidth = combiner.getStringWidth(String.valueOf(dailyDeal.getOriginalPrice()));
        combiner.drawLine(260, startY - 13, 260 + originalPriceWidth, startY - 13);

        // 斜杠分隔符
        combiner.setColor(TEXT_COLOR).addText(" / ", 260 + originalPriceWidth, startY);

        // 现价
        combiner.setColor(SALE_PRICE_COLOR).addText(String.valueOf(dailyDeal.getSalePrice()), IMAGE_MARGIN + 260 + originalPriceWidth, startY);

        startY += IMAGE_ROW_HEIGHT;

        // 绘制折扣比
        combiner.setColor(TEXT_COLOR).addText("折扣比:", IMAGE_MARGIN, startY);

        combiner.setColor(DISCOUNT_COLOR).addText(dailyDeal.getCount() + " %", 260, startY);

        startY += IMAGE_ROW_HEIGHT;

        // 绘制总数/已售信息
        combiner.setColor(TEXT_COLOR).addText("总/余:", IMAGE_MARGIN, startY);

        combiner.addText(String.valueOf(dailyDeal.getTotal()), 260, startY);

        combiner.addText(" / ", 260 + combiner.getStringWidth(String.valueOf(dailyDeal.getTotal())), startY);

        int remaining = dailyDeal.getTotal() - dailyDeal.getSold();
        combiner.setColor(REMAINING_COLOR).addText(String.valueOf(remaining), IMAGE_MARGIN + 260 + combiner.getStringWidth(String.valueOf(dailyDeal.getTotal())), startY);

        startY += IMAGE_ROW_HEIGHT;

        // 绘制剩余时间
        combiner.setColor(TEXT_COLOR).addText("剩余时间:", IMAGE_MARGIN, startY);

        combiner.addText(dailyDeal.getTimeLeft(), 260, startY);

        // 添加底部署名
        addFooter(combiner, DAILY_DEALS_IMAGE_HEIGHT - IMAGE_FOOTER_HEIGHT);

        // 合并图像
        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }
}