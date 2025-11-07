package draw;

import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TestDrawInvasionImage {

    @Test
    public void drawInvasionImage() throws IOException {
        // 使用测试常量类中加载JSON文件的常量来获取数据
        byte[] bytes = new DefaultDrawImagePlugin().drawInvasionImage(Constant.WORLD_STATE.getInvasions());
        if (bytes.length > 0) {
            ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                    Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_invasion.png")));
        }
    }
}