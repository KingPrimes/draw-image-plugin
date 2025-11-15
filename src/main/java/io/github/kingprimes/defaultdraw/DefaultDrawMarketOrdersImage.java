package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.enums.IconEnum;
import io.github.kingprimes.model.market.OrderWithUser;
import io.github.kingprimes.model.market.Orders;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 市场订单图像绘制实现类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawMarketOrdersImage {

    private static final int IMAGE_WIDTH = 1550;
    private static final int IMAGE_MARGIN = 40;
    private static final int TITLE_HEIGHT = 60;
    private static final int HEADER_HEIGHT = 50;
    private static final int ROW_HEIGHT = 60;
    private static final int FOOTER_HEIGHT = 40;

    /**
     * 绘制市场订单图像
     *
     * @param orders 市场订单数据
     * @return 图像流
     */
    public static byte[] drawMarketOrdersImage(Orders orders) {
        if (orders == null || orders.getOrders() == null) {
            return new byte[0];
        }

        List<OrderWithUser> orderList = orders.getOrders();

        // 计算图像高度
        int contentHeight = HEADER_HEIGHT + orderList.size() * ROW_HEIGHT;
        int totalHeight = TITLE_HEIGHT + contentHeight + FOOTER_HEIGHT + 200; // 为看板娘预留空间

        // 创建图像合成器
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, totalHeight, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        // 设置背景色
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, IMAGE_WIDTH, totalHeight)
                .drawTooRoundRect()
                .drawStandingDrawing();

        // 绘制标题
        int currentY = TITLE_HEIGHT + 20;

        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(48f))
                .addCenteredText(orders.getName(), currentY)
                .setFont(FONT);
        int lineX = 50;
        // 绘制物品图标
        if (orders.getIcon() != null) {
            BufferedImage icon = orders.getIcon();
            int width = icon.getWidth();
            int height = icon.getHeight();
            lineX += width / 3;
            combiner.drawImage(orders.getIcon(), 40, 25, width / 3, height / 3);
        }

        // 绘制物品信息
        currentY += 80;
        // 绘制分割线
        combiner.drawLine(lineX, currentY - 70, IMAGE_WIDTH - 30, currentY - 70);

        // 绘制平台类型
        combiner
                .setColor(TEXT_COLOR)
                .fillRoundRect(lineX += 50, currentY - 40, 120, 60, 20, 20)
                .setColor(Color.WHITE)
                .addText(orders.getForm().name(), lineX += 35, currentY);
        // 绘制卖家按钮
        combiner.setColor(orders.getIsBy() ? Color.GREEN : TEXT_COLOR)
                .fillRoundRect(lineX += 300, currentY - 40, 120, 60, 20, 20)
                .setColor(Color.WHITE)
                .addText("卖家", lineX += 25, currentY);
        // 绘制买家按钮
        combiner.setColor(orders.getIsBy() ? TEXT_COLOR : Color.GREEN)
                .fillRoundRect(lineX += 120, currentY - 40, 120, 60, 20, 20)
                .setColor(Color.WHITE)
                .addText("买家", lineX += 25, currentY);
        // 绘制价值杜卡币
        combiner.setColor(new Color(0x9c8140))
                .addText("杜卡币", lineX += 350, currentY - 32)
                .setFont(FONT_WARFRAME_ICON)
                .addText(IconEnum.DUCATS.getIcon(), lineX, currentY + 15)
                .setFont(FONT)
                .addText(orders.getDucats().toString(), lineX += 50, currentY + 15);

        // 绘制交易税
        combiner.setColor(new Color(0x45778f))
                .addText("交易税", lineX += 150, currentY - 32)
                .setFont(FONT_WARFRAME_ICON)
                .addText(IconEnum.CREDITS.getIcon(), lineX, currentY + 15)
                .setFont(FONT)
                .addText(orders.getTradingTax().toString(), lineX + 50, currentY + 15);

        // 绘制表头
        currentY += 30;
        combiner.setColor(TITLE_COLOR).drawLine(30, currentY, IMAGE_WIDTH - 30, currentY);
        currentY += 20;
        combiner.setColor(TEXT_COLOR)
                .addText("价格", IMAGE_MARGIN + 10, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("数量", IMAGE_MARGIN + 150, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("等级", IMAGE_MARGIN + 350, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("卖家", IMAGE_MARGIN + 600, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("状态", IMAGE_MARGIN + 980, currentY + HEADER_HEIGHT / 2 + 8);

        currentY += HEADER_HEIGHT + 5;

        // 绘制数据行
        for (OrderWithUser order : orderList) {
            // 获取订单信息
            String platinum = order.getPlatinum() != null ? order.getPlatinum().toString() : "未知";
            String quantity = order.getQuantity() != null ? order.getQuantity().toString() : "未知";
            String rank = order.getRank() != null ? order.getRank().toString() : "-";

            // 获取用户信息
            String sellerName = "未知";
            String status = "未知";

            if (order.getUser() != null) {
                sellerName = order.getUser().getIngameName() != null ?
                        order.getUser().getIngameName() : "-";
                status = order.getUser().getStatus() != null ?
                        order.getUser().getStatus().getStatus() : "-";
            }

            // 绘制行数据
            combiner.setColor(TEXT_COLOR)
                    .setFont(FONT_WARFRAME_ICON)
                    .addText(IconEnum.PLATINUM.getIcon(), IMAGE_MARGIN, currentY + ROW_HEIGHT / 2 + 8)
                    .setFont(FONT)
                    .addText(platinum, IMAGE_MARGIN + 40, currentY + ROW_HEIGHT / 2 + 8)
                    .setFont(FONT_WARFRAME_ICON)
                    .addText(IconEnum.CUBES.getIcon(), IMAGE_MARGIN + 140, currentY + ROW_HEIGHT / 2 + 8)
                    .setFont(FONT)
                    .addText(quantity, IMAGE_MARGIN + 180, currentY + ROW_HEIGHT / 2 + 8)
                    .addText(rank, IMAGE_MARGIN + 360, currentY + ROW_HEIGHT / 2 + 8)
                    .addText(sellerName, IMAGE_MARGIN + 500, currentY + ROW_HEIGHT / 2 + 8)
                    .addText(status, IMAGE_MARGIN + 960, currentY + ROW_HEIGHT / 2 + 8);

            currentY += ROW_HEIGHT;
        }

        // 添加底部署名
        addFooter(combiner, totalHeight - 40);

        // 合成并返回图像
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }

    /**
     * 绘制可能要查询的订单图像
     *
     * @param possibleItems 可能要查询的物品列表
     * @return 图像流
     */
    public static byte[] drawMarketOrdersImage(List<String> possibleItems) {
        if (possibleItems == null || possibleItems.isEmpty()) {
            return new byte[0];
        }

        // 计算图像高度
        int contentHeight = possibleItems.size() * ROW_HEIGHT;
        int totalHeight = TITLE_HEIGHT + contentHeight + FOOTER_HEIGHT + 150; // 为看板娘预留空间

        // 创建图像合成器
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, totalHeight, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        // 设置背景色
        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, IMAGE_WIDTH, totalHeight)
                .drawTooRoundRect()
                .drawStandingDrawing();

        // 绘制标题
        int currentY = TITLE_HEIGHT;
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT)
                .addCenteredText("可能要查询的物品列表", currentY);

        // 绘制表头
        currentY += 50;
        combiner.setColor(TEXT_COLOR)
                .addText("序号", IMAGE_MARGIN + 10, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("物品名称", IMAGE_MARGIN + 150, currentY + HEADER_HEIGHT / 2 + 8);

        currentY += HEADER_HEIGHT + 5;

        // 绘制数据行
        int index = 1;
        for (String item : possibleItems) {
            combiner.setColor(TEXT_COLOR)
                    .addText(String.valueOf(index), IMAGE_MARGIN + 20, currentY + ROW_HEIGHT / 2 + 8)
                    .addText(item, IMAGE_MARGIN + 160, currentY + ROW_HEIGHT / 2 + 8);

            currentY += ROW_HEIGHT;
            index++;
        }

        // 添加底部署名
        addFooter(combiner, totalHeight - 40);

        // 合成并返回图像
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }
}