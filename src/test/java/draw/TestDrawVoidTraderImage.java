package draw;

import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * 虚空商人图像绘制测试类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
public class TestDrawVoidTraderImage {

    @Test
    public void drawVoidTraderImage() throws IOException {
        // 使用测试常量类中加载JSON文件的常量来获取数据
        byte[] bytes = new DefaultDrawImagePlugin().drawVoidTraderImage(Constant.WORLD_STATE.getVoidTraders());
        if (bytes.length > 0) {
            ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                    Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_void_trader.png")));
        }
    }
}