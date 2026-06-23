package io.github.kingprimes.draw;


import tools.jackson.databind.json.JsonMapper;
import io.github.kingprimes.common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.market.MarketRiven;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;


public class TestDrawDefaultMarketRivenImage {

    @Test
    public void testDrawMarketRivenImage() throws Exception {
        MarketRiven marketRiven = new JsonMapper().readValue(
                TestDrawDefaultMarketRivenImage.class.getResourceAsStream("/marketRiven.json"), MarketRiven.class);

        // 创建绘图插件实例
        DefaultDrawImagePlugin plugin = new DefaultDrawImagePlugin();

        // 调用绘制方法
        byte[] imageBytes = plugin.drawMarketRivenImage(marketRiven);

        // 将结果保存到文件以便查看
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageBytes)), Constant.PNG,
                new File(Constant.DRAW_PATH.formatted("draw_market_riven.png")));
    }
}