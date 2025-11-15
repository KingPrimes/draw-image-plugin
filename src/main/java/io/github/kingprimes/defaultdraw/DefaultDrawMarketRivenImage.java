package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.image.ImageIOUtils;
import io.github.kingprimes.model.enums.FactionEnum;
import io.github.kingprimes.model.enums.IconEnum;
import io.github.kingprimes.model.market.MarketRiven;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 市场紫卡图像绘制实现类
 *
 * @author KingPrimes
 * @version 1.0.0
 */
final class DefaultDrawMarketRivenImage {

    private static final int CARD_WIDTH = 632;
    private static final int CARD_HEIGHT = 800;
    private static final int CARD_MARGIN = 20;
    private static final int COLUMNS = 4;
    private static final Color RIVEN_TEXT_COLOR = new Color(0x8669A7);

    // 私有构造函数防止实例化
    private DefaultDrawMarketRivenImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawMarketRivenImage class");
    }

    /**
     * 绘制市场紫卡图像
     *
     * @param marketRiven 市场紫卡数据
     * @return 图像流
     */
    public static byte[] drawMarketRivenImage(MarketRiven marketRiven) {
        if (marketRiven == null || marketRiven.getPayload() == null || marketRiven.getPayload().getAuctions() == null) {
            return new byte[0];
        }

        List<MarketRiven.Auctions> auctions = marketRiven.getPayload().getAuctions();
        if (auctions.isEmpty()) {
            return new byte[0];
        }

        // 计算图像尺寸
        int totalWidth = CARD_WIDTH * COLUMNS + CARD_MARGIN * (COLUMNS + 1);
        int rowsNeeded = (int) Math.ceil((double) auctions.size() / COLUMNS);
        int totalHeight = CARD_HEIGHT * rowsNeeded + CARD_MARGIN * (rowsNeeded + 1) + 200; // 为标题和看板娘预留空间

        // 创建图像合成器
        BufferedImage image = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        // 设置背景色
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, totalWidth, totalHeight)
                .drawTooRoundRect()
                .drawStandingDrawing();

        // 绘制标题
        int titleY = 100;
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(64f))
                .addCenteredText("Warframe Market 紫卡市场", titleY);

        // 绘制紫卡卡片
        int currentY = titleY + 40;
        for (int i = 0; i < auctions.size(); i++) {
            MarketRiven.Auctions auction = auctions.get(i);
            int row = i / COLUMNS;
            int col = i % COLUMNS;

            int cardX = CARD_MARGIN + col * (CARD_WIDTH + CARD_MARGIN);
            int cardY = currentY + CARD_MARGIN + row * (CARD_HEIGHT + CARD_MARGIN);

            combiner.drawImage(
                    drawRivenCard(new ImageCombiner(ImageIOUtils.getRivenTemplate(), ImageCombiner.OutputFormat.PNG),
                            auction,
                            marketRiven.getItemName()),
                    cardX, cardY
            );
        }

        // 添加底部署名
        addFooter(combiner, totalHeight - 40);

        // 合成并返回图像
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }

    /**
     * 绘制单个紫卡卡片
     *
     * @param combiner 图像合成器
     * @param auction  紫卡拍卖信息
     */
    private static BufferedImage drawRivenCard(ImageCombiner combiner, MarketRiven.Auctions auction, String itemName) {

        // 获取物品信息
        MarketRiven.Item item = auction.getItem();
        if (item == null) return null;

        int width = combiner.getCanvasWidth();
        int height = combiner.getCanvasHeight();

        // 绘制物品名称 (在紫卡模板图片的正上方居中绘制)
        combiner.setColor(RIVEN_TEXT_COLOR)
                .setFont(FONT.deriveFont(32f))
                .addCenteredText(itemName, 430)
                .addCenteredText(item.getName(), 465);


        // 绘制MOD等级数据 (在紫卡模板图片的最下方居中绘制)
        if (item.getModRank() != null) {
            combiner.setColor(ALL_CYCLE_COLD_COLOR)
                    .setFont(FONT.deriveFont(18f));
            int x = 222;
            for (int i = 0; i < item.getModRank(); i++) {
                combiner
                        .addText("●", x, height - 36);
                x += 25;
            }
        }

        // 绘制段位数据 (在紫卡模板图片的左下角)
        if (item.getMasteryLevel() != null) {
            combiner.setColor(RIVEN_TEXT_COLOR)
                    .setFont(FONT.deriveFont(28f))
                    .addText("段位: " + item.getMasteryLevel(), 140, height - 108);
        }

        // 绘制刷新次数图标 (在紫卡模板图片的右下角)
        if (item.getReRolls() != null) {
            combiner.setColor(RIVEN_TEXT_COLOR)
                    .setFont(FONT_WARFRAME_ICON.deriveFont(28f))
                    .addText(IconEnum.REFRESH.getIcon(), width - 185, height - 105)
                    .setFont(FONT.deriveFont(28f))
                    .addText(item.getReRolls().toString(), width - 150, height - 108);
        }

        // 绘制极性图标 (在紫卡模板图片的右上角)
        if (item.getPolarity() != null) {
            combiner.setColor(RIVEN_TEXT_COLOR)
                    .setFont(FONT_WARFRAME_ICON.deriveFont(48f))
                    .addText(item.getPolarity().getIcon(), width - 132, 120);
        }


        // 绘制紫卡词条数据 (在紫卡模板图片的正下方居中绘制)
        if (item.getAttributes() != null && !item.getAttributes().isEmpty()) {
            combiner.setColor(RIVEN_TEXT_COLOR)
                    .setFont(FONT.deriveFont(32f));
            int attrY = 515;
            for (MarketRiven.Attributes attribute : item.getAttributes()) {
                String attrText =
                        attribute.getValue() + "  " +
                                attribute.getUrlName();
                combiner.addCenteredText(attrText, attrY);
                attrY += 40;
            }
        }

        // 绘制玩家昵称和买断价格 (在紫卡模板图片的右边)
        if (auction.getOwner() != null && auction.getOwner().getIngameName() != null) {
            combiner.setColor(TITLE_COLOR)
                    .setFont(FONT_WARFRAME_ICON.deriveFont(48f))
                    .addText(FactionEnum.FC_TENNO.getIcon(), 100, 320)
                    .setFont(FONT.deriveFont(48f))
                    .addText(auction.getOwner().getIngameName(), 180, 320);
        }

        if (auction.getBuyoutPrice() != null) {
            combiner.setColor(TITLE_COLOR)
                    .setFont(FONT_WARFRAME_ICON.deriveFont(48f))
                    .addText(IconEnum.PLATINUM.getIcon(), 100, 380)
                    .setFont(FONT.deriveFont(48f))
                    .addText(auction.getBuyoutPrice().toString(), 180, 380);
        }


        combiner.combine();
        return combiner.getCombinedImage();
    }
}