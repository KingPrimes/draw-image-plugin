package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.image.ImageIOUtils;
import io.github.kingprimes.model.MarketLichSister;
import io.github.kingprimes.model.enums.ElementEnum;

import java.awt.image.BufferedImage;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 市场Lich拍卖图像绘制实现类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawMarketLichSisterImage {

    private static final int IMAGE_WIDTH = 1350;
    private static final int IMAGE_MARGIN = 40;
    private static final int ROW_HEIGHT = 60;
    private static final int HEADER_HEIGHT = 50;
    private static final int TITLE_HEIGHT = 60;
    private static final int FOOTER_HEIGHT = 40;

    /**
     * 绘制市场Lich拍卖图像
     *
     * @param marketLichs 市场Lich拍卖数据
     * @return 图像流
     */
    public static byte[] drawMarketLichesImage(MarketLichSister marketLichs) {
        if (marketLichs == null || marketLichs.getPayload() == null || marketLichs.getPayload().getAuctions() == null) {
            return new byte[0];
        }

        List<MarketLichSister.Payload.Auctions> auctions = marketLichs.getPayload().getAuctions();

        // 计算图像高度
        int contentHeight = HEADER_HEIGHT + auctions.size() * ROW_HEIGHT;
        int totalHeight = TITLE_HEIGHT + contentHeight + FOOTER_HEIGHT + 100; // 为看板娘预留空间

        // 创建图像合成器
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, totalHeight, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        // 设置背景色
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, IMAGE_WIDTH, totalHeight)
                .drawTooRoundRect();
        BufferedImage xiaoMeiWangImage = ImageIOUtils.getRandomXiaoMeiWangImage();
        // 增大看板娘尺寸，使其更显眼
        int mascotSize = Math.min(400, Math.min(IMAGE_WIDTH, totalHeight) / 2);
        // 调整看板娘位置，确保在右下角合适位置
        int mascotX = IMAGE_WIDTH - mascotSize + 80;
        int mascotY = totalHeight - mascotSize - 40; // 底部边距40像素
        combiner.drawImageWithAspectRatio(
                xiaoMeiWangImage,
                mascotX,
                mascotY,
                mascotSize,
                mascotSize
        );
        // 绘制标题
        int currentY = TITLE_HEIGHT;
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT)
                .addCenteredText(marketLichs.getPayload().getItemName() + " 拍卖信息", currentY);

        // 绘制表头
        currentY += 30;

        combiner.setColor(TEXT_COLOR)
                .addText("元素", IMAGE_MARGIN + 10, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("伤害", IMAGE_MARGIN + 200, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("起拍价", IMAGE_MARGIN + 300, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("买断价", IMAGE_MARGIN + 400, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("卖家", IMAGE_MARGIN + 650, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("声望", IMAGE_MARGIN + 1000, currentY + HEADER_HEIGHT / 2 + 8);

        currentY += HEADER_HEIGHT + 5;

        // 绘制数据行
        for (MarketLichSister.Payload.Auctions auction : auctions) {
            // 获取物品信息
            ElementEnum element = auction.getItem().getElement();
            String damage = auction.getItem().getDamage() != null ?
                    auction.getItem().getDamage().toString() : "未知";

            // 获取价格信息
            String startingPrice = auction.getStartingPrice() != null ?
                    auction.getStartingPrice().toString() : "无";
            String buyoutPrice = auction.getBuyoutPrice() != null ?
                    auction.getBuyoutPrice().toString() : "无";

            // 获取卖家信息
            String sellerName = "未知";
            String reputation = "未知";
            if (auction.getOwner() != null) {
                sellerName = auction.getOwner().getIngameName() != null ?
                        auction.getOwner().getIngameName() : "未知";
                reputation = auction.getOwner().getReputation() != null ?
                        auction.getOwner().getReputation().toString() : "未知";
            }

            // 绘制行数据
            combiner.setColor(element.getCOLOR())
                    .setFont(FONT_ELEMENT)
                    .addText(element.getICON(), IMAGE_MARGIN, currentY + ROW_HEIGHT / 2 + 8)
                    .setFont(FONT)
                    .addText(element.getNAME(), IMAGE_MARGIN + 40, currentY + ROW_HEIGHT / 2 + 8)
                    .addText(damage, IMAGE_MARGIN + 210, currentY + ROW_HEIGHT / 2 + 8)
                    .addText(startingPrice, IMAGE_MARGIN + 320, currentY + ROW_HEIGHT / 2 + 8)
                    .addText(buyoutPrice, IMAGE_MARGIN + 420, currentY + ROW_HEIGHT / 2 + 8)
                    .addText(sellerName, IMAGE_MARGIN + 550, currentY + ROW_HEIGHT / 2 + 8)
                    .addText(reputation, IMAGE_MARGIN + 1010, currentY + ROW_HEIGHT / 2 + 8);

            currentY += ROW_HEIGHT;
        }

        // 添加底部署名
        addFooter(combiner, totalHeight - 40);

        // 合成并返回图像
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }
}