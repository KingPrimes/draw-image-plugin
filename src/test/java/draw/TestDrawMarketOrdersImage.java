package draw;


import com.alibaba.fastjson2.JSON;
import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.image.ImageIOUtils;
import io.github.kingprimes.model.market.Orders;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;


public class TestDrawMarketOrdersImage {

    private final DefaultDrawImagePlugin plugin = new DefaultDrawImagePlugin();

    @Test
    public void testDrawMarketOrdersImage() throws Exception {
        Orders orders = JSON.parseObject(TestDrawMarketLichesImage.class.getResourceAsStream("/orders.json"), Orders.class);
        BufferedImage resourcesImage = ImageIOUtils.getResourcesImage("/image/nova.png");
        Assert.assertNotNull(orders);
        orders.setIcon(resourcesImage);
        byte[] imageBytes = plugin.drawMarketOrdersImage(orders);
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageBytes)), Constant.PNG,
                new File(Constant.DRAW_PATH.formatted("draw_market_orders.png")));
    }

    @Test
    public void testDrawMarketOrdersImageWithList() throws Exception {
        // 创建可能的物品列表
        List<String> possibleItems = Arrays.asList(
                "Ash Prime Blueprint",
                "Ash Prime Chassis Blueprint",
                "Ash Prime Neuroptics Blueprint",
                "Ash Prime Systems Blueprint",
                "Ash Prime Set",
                "Nova Prime Blueprint",
                "Nova Prime Chassis Blueprint"
        );

        // 绘制图像
        byte[] imageBytes = plugin.drawMarketOrdersImage(possibleItems);
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageBytes)), Constant.PNG,
                new File(Constant.DRAW_PATH.formatted("market_orders_list.png")));
    }
}