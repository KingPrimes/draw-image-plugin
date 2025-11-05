package draw;

import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TestDrawAlertsImage {

    @Test
    public void drawAlertsImage() throws IOException {
        // 使用测试常量类中加载JSON文件的常量来获取数据
        byte[] bytes = new DefaultDrawImagePlugin().drawAlertsImage(Constant.WORLD_STATE.getAlerts());
        if (bytes.length > 0) {
            ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                    Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_alerts.png")));
        }
    }
}