package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.enums.IconEnum;
import io.github.kingprimes.model.worldstate.VoidTrader;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认虚空商人图片绘制工具类
 * <p>
 * 该类负责将虚空商人数据渲染为图像，包括商人信息、出现位置、商品清单和价格等元素
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawVoidTraderImage {

    // 图像尺寸常量
    private static final int VOID_TRADER_IMAGE_WIDTH = 1400;
    private static final int VOID_TRADER_IMAGE_MIN_HEIGHT = 800;

    // 表格相关常量
    private static final int ROW_HEIGHT = 45;
    private static final int HEADER_HEIGHT = 60;

    // 颜色常量定义
    private static final Color HEADER_BG_COLOR = new Color(0x2C3E50);
    private static final Color ITEM_NAME_COLOR = new Color(0x3498DB);
    private static final Color DUCATS_COLOR = new Color(0xE67E22);
    private static final Color CREDITS_COLOR = new Color(0xF39C12);
    private static final Color LOCATION_COLOR = new Color(0x9B59B6);

    /**
     * 私有构造函数，防止实例化该工具类
     */
    private DefaultDrawVoidTraderImage() {
        throw new AssertionError("Cannot instantiate DefaultDrawVoidTraderImage class");
    }

    /**
     * 绘制虚空商人图像
     *
     * @param voidTraders 虚空商人数据列表
     * @return 生成的虚空商人图像的 PNG 格式字节数组
     */
    public static byte[] drawVoidTraderImage(List<VoidTrader> voidTraders) {
        // 如果没有虚空商人数据，则返回空字节数组
        if (voidTraders == null || voidTraders.isEmpty()) {
            return new byte[0];
        }

        // 计算所有商品数量
        int totalItems = voidTraders.stream()
                .mapToInt(vt -> vt.getManifest() != null ? vt.getManifest().size() : 0)
                .sum();

        // 根据商品数量计算图像高度
        int height = calculateImageHeight(voidTraders.size(), totalItems);

        // 创建图像合成器实例
        ImageCombiner combiner = new ImageCombiner(
                VOID_TRADER_IMAGE_WIDTH,
                height,
                ImageCombiner.OutputFormat.PNG
        );

        // 填充背景色并绘制双层圆角矩形边框
        combiner.setFont(FONT)
                .setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, VOID_TRADER_IMAGE_WIDTH, height)
                .drawTooRoundRect()
                // 绘制看板娘
                .drawStandingDrawing();

        // 绘制标题
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 40))
                .addCenteredText("虚空商人 - Baro Ki'Teer", 80);

        // 绘制虚空商人列表
        int startY = 130;
        for (VoidTrader voidTrader : voidTraders) {
            startY = drawVoidTraderSection(combiner, voidTrader, startY);
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
     * 根据虚空商人数量和商品数量动态计算图像高度
     *
     * @param traderCount 虚空商人数量
     * @param itemCount   商品总数
     * @return 图像高度
     */
    private static int calculateImageHeight(int traderCount, int itemCount) {
        int headerHeight = 130; // 标题区域高度
        int footerHeight = IMAGE_FOOTER_HEIGHT + 50; // 底部区域高度
        int traderInfoHeight = traderCount * 80; // 商人信息区域高度（每个商人）
        int tableHeaderHeight = traderCount * HEADER_HEIGHT; // 表头高度
        int itemsHeight = itemCount * ROW_HEIGHT; // 商品行高度

        int totalHeight = headerHeight + traderInfoHeight + tableHeaderHeight + itemsHeight + footerHeight;

        return Math.max(totalHeight, VOID_TRADER_IMAGE_MIN_HEIGHT);
    }

    /**
     * 绘制单个虚空商人区域
     * 包括商人信息和商品清单
     *
     * @param combiner   图像合成器实例
     * @param voidTrader 虚空商人数据
     * @param startY     起始Y坐标
     * @return 下一个区域的起始Y坐标
     */
    private static int drawVoidTraderSection(ImageCombiner combiner, VoidTrader voidTrader, int startY) {
        int currentY = startY;

        // 绘制商人基本信息
        currentY = drawTraderInfo(combiner, voidTrader, currentY);

        // 绘制商品表格
        if (voidTrader.getManifest() != null && !voidTrader.getManifest().isEmpty()) {
            currentY = drawManifestTable(combiner, voidTrader.getManifest(), currentY);
        }

        return currentY + 30; // 添加区域间距
    }

    /**
     * 绘制商人基本信息
     *
     * @param combiner   图像合成器实例
     * @param voidTrader 虚空商人数据
     * @param startY     起始Y坐标
     * @return 下一行的Y坐标
     */
    private static int drawTraderInfo(ImageCombiner combiner, VoidTrader voidTrader, int startY) {
        // 绘制商人名称
        String characterName = voidTrader.getCharacter() != null ? voidTrader.getCharacter() : "Baro Ki'Teer";

        // 绘制出现位置
        String location = voidTrader.getNode() != null ? voidTrader.getNode() : "未知位置";
        combiner.setColor(LOCATION_COLOR)
                .setFont(FONT)
                .addText("位置: " + location, IMAGE_MARGIN + 200, startY);

        // 绘制时间信息
        String timeLeft = voidTrader.getTimeLeft() != null ? voidTrader.getTimeLeft() : "未知";
        combiner.setColor(TEXT_COLOR)
                .setFont(FONT)
                .addText("剩余时间: " + timeLeft, IMAGE_MARGIN + 750, startY);

        return startY + 50;
    }

    /**
     * 绘制商品清单表格
     *
     * @param combiner 图像合成器实例
     * @param manifest 商品清单列表
     * @param startY   起始Y坐标
     * @return 下一行的Y坐标
     */
    private static int drawManifestTable(ImageCombiner combiner, List<VoidTrader.Manifest> manifest, int startY) {
        int tableX = IMAGE_MARGIN;
        int tableWidth = VOID_TRADER_IMAGE_WIDTH - 2 * IMAGE_MARGIN;

        // 定义列宽
        int[] columnWidths = {600, 200, 200};
        String[] headers = {"物品名称", "杜卡币价格", "星币价格"};

        // 绘制表头背景
        combiner.setColor(HEADER_BG_COLOR)
                .fillRect(tableX, startY, tableWidth, HEADER_HEIGHT);

        // 绘制表头文字
        combiner.setColor(Color.WHITE)
                .setFont(FONT.deriveFont(Font.BOLD, 24));

        int headerX = tableX;
        for (int i = 0; i < headers.length; i++) {
            combiner.addText(headers[i], headerX + 20, startY + HEADER_HEIGHT / 2 + 8);
            headerX += columnWidths[i];
        }

        int currentY = startY + HEADER_HEIGHT;

        // 绘制商品行
        for (VoidTrader.Manifest item : manifest) {
            // 绘制商品信息
            drawManifestRow(combiner, item, currentY, tableX, columnWidths);

            currentY += ROW_HEIGHT;
        }

        return currentY;
    }

    /**
     * 绘制单个商品行
     *
     * @param combiner     图像合成器实例
     * @param item         商品数据
     * @param rowY         行Y坐标
     * @param startX       起始X坐标
     * @param columnWidths 列宽数组
     */
    private static void drawManifestRow(ImageCombiner combiner, VoidTrader.Manifest item,
                                        int rowY, int startX, int[] columnWidths) {
        combiner.setFont(FONT.deriveFont(24f));

        // 物品名称
        String itemName = item.getItem() != null ? item.getItem() : "未知物品";
        combiner.setColor(ITEM_NAME_COLOR)
                .addText(itemName, startX + 20, rowY + ROW_HEIGHT / 2 + 6);

        // 杜卡币价格
        String ducatsPrice = item.getPrimePrice() != null ?
                item.getPrimePrice().toString() : "0";
        combiner.setColor(new Color(0x9c8140))
                .setFont(FONT_WARFRAME_ICON.deriveFont(24f))
                .addText(IconEnum.DUCATS.getIcon(), startX + columnWidths[0] + 20, rowY + ROW_HEIGHT / 2 + 6)
                .setFont(FONT.deriveFont(24f))
                .addText(ducatsPrice, startX + columnWidths[0] + 60, rowY + ROW_HEIGHT / 2 + 6);

        // 星币价格
        combiner.setColor(new Color(0x45778f))
                .setFont(FONT_WARFRAME_ICON.deriveFont(24f))
                .addText(IconEnum.CREDITS.getIcon(), startX + columnWidths[0] + columnWidths[1] + 20,
                        rowY + ROW_HEIGHT / 2 + 6)
                .setFont(FONT.deriveFont(24f))
                .addText(item.getRegularPrice() / 1000 + "K", startX + columnWidths[0] + columnWidths[1] + 60,
                        rowY + ROW_HEIGHT / 2 + 6);
    }
}