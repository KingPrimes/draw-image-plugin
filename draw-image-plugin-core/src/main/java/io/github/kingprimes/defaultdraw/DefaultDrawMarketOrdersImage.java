package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.enums.IconEnum;
import io.github.kingprimes.model.enums.MarketStatusEnum;
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
 * @version 1.0.8
 */
final class DefaultDrawMarketOrdersImage {

    private static final int IMAGE_WIDTH = 1550;
    private static final int IMAGE_MARGIN = 40;
    private static final int TITLE_HEIGHT = 60;
    private static final int HEADER_HEIGHT = 50;
    private static final int ROW_HEIGHT = 60;
    private static final int FOOTER_HEIGHT = 40;

    private static final Color PLATFORM_BG = new Color(0x6C5CE7);
    private static final Color ACTIVE_BG = new Color(0x27AE60);
    private static final Color INACTIVE_BG = CARD_BACKGROUND_COLOR;
    private static final Color PRICE_COLOR = new Color(0xE8D5A3);
    private static final Color ONLINE_COLOR = new Color(0x27AE60);
    private static final Color INGAME_COLOR = new Color(0x3498DB);
    private static final Color OFFLINE_COLOR = new Color(0xE74C3C);
    private static final Color INVISIBLE_COLOR = TEXT_MUTED_COLOR;

    private DefaultDrawMarketOrdersImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawMarketOrdersImage(Orders orders) {
        if (orders == null || orders.getOrders() == null) {
            return new byte[0];
        }

        List<OrderWithUser> orderList = orders.getOrders();

        box sz = scaleByPct(IMAGE_WIDTH, IMAGE_WIDTH, STANDING_RATIO);
        int contentHeight = HEADER_HEIGHT + orderList.size() * ROW_HEIGHT;
        int totalHeight;

        int startY = TITLE_HEIGHT + 20;

        // 预计算：标题下方 info 区域占 Y: startY ~ startY+100
        int infoEndY = startY + 100;
        int tableStartY = infoEndY + 30;
        int standingY = tableStartY + contentHeight + 50;
        totalHeight = standingY + sz.y();

        BufferedImage image = new BufferedImage(IMAGE_WIDTH, totalHeight, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, IMAGE_WIDTH, totalHeight)
                .drawTooRoundRect();

        // 绘制标题
        int currentY = startY;
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(48f))
                .addCenteredText(orders.getName(), currentY)
                .setFont(FONT);
        int lineX = 50;

        if (orders.getIcon() != null) {
            BufferedImage icon = orders.getIcon();
            int width = icon.getWidth();
            int height = icon.getHeight();
            lineX += width / 3;
            combiner.drawImage(orders.getIcon(), 40, 25, width / 3, height / 3);
        }

        currentY += 80;
        combiner.drawLine(lineX, currentY - 70, IMAGE_WIDTH - 30, currentY - 70);

        // ---- 平台标签 ----
        combiner.setColor(PLATFORM_BG)
                .fillRoundRect(lineX += 50, currentY - 40, 120, 60, 20, 20)
                .setColor(Color.WHITE)
                .addText(orders.getForm().name(), lineX + 20, currentY);

        // ---- 卖家/买家切换按钮 ----
        boolean isBuy = orders.getIsBy() != null && orders.getIsBy();
        // 卖家
        combiner.setColor(isBuy ? ACTIVE_BG : INACTIVE_BG)
                .fillRoundRect(lineX += 250, currentY - 40, 120, 60, 20, 20)
                .setColor(isBuy ? Color.WHITE : TEXT_SECONDARY_COLOR)
                .addText("卖家", lineX + 25, currentY);
        // 买家
        combiner.setColor(isBuy ? INACTIVE_BG : ACTIVE_BG)
                .fillRoundRect(lineX += 120, currentY - 40, 120, 60, 20, 20)
                .setColor(isBuy ? TEXT_SECONDARY_COLOR : Color.WHITE)
                .addText("买家", lineX + 25, currentY);

        // ---- 杜卡币 ----
        combiner.setColor(ACCENT_GOLD_COLOR).setFont(FONT)
                .addText("杜卡币", lineX += 280, currentY - 32)
                .setFont(FONT_WARFRAME_ICON)
                .addText(IconEnum.DUCATS.getIcon(), lineX, currentY + 15)
                .setFont(FONT)
                .addText(orders.getDucats().toString(), lineX += 50, currentY + 15);

        // ---- 交易税 ----
        combiner.setColor(TEXT_SECONDARY_COLOR).setFont(FONT)
                .addText("交易税", lineX += 120, currentY - 32)
                .setFont(FONT_WARFRAME_ICON)
                .addText(IconEnum.CREDITS.getIcon(), lineX, currentY + 15)
                .setFont(FONT)
                .addText(orders.getTradingTax().toString(), lineX + 50, currentY + 15);

        // ---- 表头 ----
        currentY += 30;
        combiner.setColor(DIVIDER_COLOR).drawLine(30, currentY, IMAGE_WIDTH - 30, currentY);
        currentY += 20;
        combiner.setColor(TEXT_SECONDARY_COLOR).setFont(FONT.deriveFont(20f))
                .addText("价格", IMAGE_MARGIN + 10, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("数量", IMAGE_MARGIN + 180, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("等级", IMAGE_MARGIN + 380, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("卖家", IMAGE_MARGIN + 600, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("状态", IMAGE_MARGIN + 1000, currentY + HEADER_HEIGHT / 2 + 8);

        currentY += HEADER_HEIGHT + 5;

        // ---- 数据行 ----
        for (OrderWithUser order : orderList) {
            String platinum = order.getPlatinum() != null ? order.getPlatinum().toString() : "-";
            String quantity = order.getQuantity() != null ? order.getQuantity().toString() : "-";
            String rank = order.getRank() != null ? order.getRank().toString() : "-";

            String sellerName = "-";
            String status = "-";
            MarketStatusEnum statusEnum = null;

            if (order.getUser() != null) {
                sellerName = order.getUser().getIngameName() != null
                        ? order.getUser().getIngameName() : "-";
                statusEnum = order.getUser().getStatus();
                status = statusEnum != null ? statusEnum.getStatus() : "-";
            }

            // 价格 — 白金色
            combiner.setColor(PRICE_COLOR).setFont(FONT_WARFRAME_ICON)
                    .addText(IconEnum.PLATINUM.getIcon(), IMAGE_MARGIN, currentY + ROW_HEIGHT / 2 + 8)
                    .setFont(FONT)
                    .addText(platinum, IMAGE_MARGIN + 40, currentY + ROW_HEIGHT / 2 + 8);

            // 数量 — 浅灰色
            combiner.setColor(TEXT_SECONDARY_COLOR).setFont(FONT)
                    .addText(quantity, IMAGE_MARGIN + 180, currentY + ROW_HEIGHT / 2 + 8);

            // 等级
            combiner.setColor(TEXT_COLOR).setFont(FONT)
                    .addText(rank, IMAGE_MARGIN + 380, currentY + ROW_HEIGHT / 2 + 8);

            // 卖家
            combiner.setColor(TEXT_COLOR)
                    .addText(sellerName, IMAGE_MARGIN + 600, currentY + ROW_HEIGHT / 2 + 8);

            // 状态 — 颜色跟随状态
            Color statusColor = getStatusColor(statusEnum);
            combiner.setColor(statusColor)
                    .addText(status, IMAGE_MARGIN + 1000, currentY + ROW_HEIGHT / 2 + 8);

            currentY += ROW_HEIGHT;
        }

        combiner.drawStandingAt(IMAGE_WIDTH - sz.x(), totalHeight - sz.y(), sz.x(), sz.y());
        addFooter(combiner, totalHeight - 25);
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }

    private static Color getStatusColor(MarketStatusEnum status) {
        if (status == null) return TEXT_MUTED_COLOR;
        return switch (status) {
            case ONLINE -> ONLINE_COLOR;
            case INGAME -> INGAME_COLOR;
            case OFFLINE -> OFFLINE_COLOR;
            case INVISIBLE -> INVISIBLE_COLOR;
        };
    }

    /**
     * 绘制可能要查询的订单图像
     */
    public static byte[] drawMarketOrdersImage(List<String> possibleItems) {
        if (possibleItems == null || possibleItems.isEmpty()) {
            return new byte[0];
        }

        int contentHeight = possibleItems.size() * ROW_HEIGHT;
        int totalHeight = TITLE_HEIGHT + contentHeight + FOOTER_HEIGHT + 150;

        BufferedImage image = new BufferedImage(IMAGE_WIDTH, totalHeight, BufferedImage.TYPE_INT_ARGB);
        ImageCombiner combiner = new ImageCombiner(image, ImageCombiner.OutputFormat.PNG);

        combiner.setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, IMAGE_WIDTH, totalHeight)
                .drawTooRoundRect();

        int currentY = TITLE_HEIGHT;
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT)
                .addCenteredText("可能要查询的物品列表", currentY);

        currentY += 50;
        combiner.setColor(TEXT_SECONDARY_COLOR).setFont(FONT.deriveFont(20f))
                .addText("序号", IMAGE_MARGIN + 10, currentY + HEADER_HEIGHT / 2 + 8)
                .addText("物品名称", IMAGE_MARGIN + 150, currentY + HEADER_HEIGHT / 2 + 8);

        currentY += HEADER_HEIGHT + 5;

        int index = 1;
        for (String item : possibleItems) {
            combiner.setColor(TEXT_COLOR)
                    .addText(String.valueOf(index), IMAGE_MARGIN + 20, currentY + ROW_HEIGHT / 2 + 8)
                    .addText(item, IMAGE_MARGIN + 160, currentY + ROW_HEIGHT / 2 + 8);

            currentY += ROW_HEIGHT;
            index++;
        }

        addFooter(combiner, totalHeight - 40);
        combiner.combine();
        return combiner.getCombinedImageOutStream().toByteArray();
    }
}
