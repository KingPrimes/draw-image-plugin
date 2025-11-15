package draw;


import com.alibaba.fastjson2.JSON;
import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.market.MarketRiven;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;


public class TestDefaultDrawMarketRivenImage {

    @Test
    public void testDrawMarketRivenImage() throws Exception {
        MarketRiven marketRiven = JSON.parseObject(TestDefaultDrawMarketRivenImage.class.getResourceAsStream("/marketRiven.json"), MarketRiven.class);

        // 创建绘图插件实例
        DefaultDrawImagePlugin plugin = new DefaultDrawImagePlugin();

        // 调用绘制方法
        byte[] imageBytes = plugin.drawMarketRivenImage(marketRiven);

        // 将结果保存到文件以便查看
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageBytes)), Constant.PNG,
                new File(Constant.DRAW_PATH.formatted("draw_market_riven.png")));
    }
}