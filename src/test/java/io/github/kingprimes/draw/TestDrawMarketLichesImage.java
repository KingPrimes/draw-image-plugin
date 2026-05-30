package io.github.kingprimes.draw;

import tools.jackson.databind.json.JsonMapper;
import io.github.kingprimes.common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.market.MarketLichSister;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class TestDrawMarketLichesImage {
    @Test
    public void testDrawMarketLichesImage() throws IOException {
        // 读取测试数据
        MarketLichSister marketLichs = new JsonMapper().readValue(
                TestDrawMarketLichesImage.class.getResourceAsStream("/liches.json"), MarketLichSister.class);

        // 绘制图像
        byte[] imageBytes = new DefaultDrawImagePlugin().drawMarketLichesImage(marketLichs);

        // 保存图像
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageBytes)), Constant.PNG,
                new File(Constant.DRAW_PATH.formatted("draw_market_liches.png")));
    }
}