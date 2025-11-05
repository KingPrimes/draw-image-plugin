package draw;

import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.WorldState;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TestDrawDailyDealsImage {

    @Test
    public void drawDailyDealsImage() throws IOException {
        // 创建测试数据
        WorldState worldState = Constant.WORLD_STATE;

        byte[] bytes = new DefaultDrawImagePlugin().drawDailyDealsImage(worldState.getDailyDeals().getFirst());
        if (bytes.length > 0) {
            ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                    Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_daily_deals.png")));
        }
    }
}