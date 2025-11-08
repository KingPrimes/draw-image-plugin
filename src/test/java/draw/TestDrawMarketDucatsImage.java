package draw;

import com.alibaba.fastjson2.JSON;
import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.Ducats;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class TestDrawMarketDucatsImage {
    @Test
    public void testDrawMarketGodDumpImage() throws IOException {

        Ducats ducats = JSON.parseObject(TestDrawMarketDucatsImage.class.getResourceAsStream("/ducats.json"), Ducats.class);
        // 绘制图像
        byte[] imageBytes = new DefaultDrawImagePlugin().drawMarketGodDumpImage(ducats.getGoldDump());
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageBytes)), Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_market_god_dump.png")));
    }

    @Test
    public void testDrawMarketSilverDumpImage() throws IOException {

        Ducats ducats = JSON.parseObject(TestDrawMarketDucatsImage.class.getResourceAsStream("/ducats.json"), Ducats.class);
        // 绘制图像
        byte[] imageBytes = new DefaultDrawImagePlugin().drawMarketSilverDumpImage(ducats.getSilverDump());
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageBytes)), Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_market_silver_dump.png")));
    }
}
