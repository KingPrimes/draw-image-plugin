package io.github.kingprimes.draw;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import io.github.kingprimes.common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.Relics;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 遗物图像绘制测试类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
public class TestDrawRelicsImage {
    @Test
    public void testDrawRelicsImage() throws IOException {
        InputStream resourceAsStream = TestDrawRelicsImage.class.getResourceAsStream("/relics.json");
        // 读取测试数据
        List<Relics> relics = new JsonMapper().readValue(resourceAsStream,
                new TypeReference<List<Relics>>() {});

        // 绘制图像
        byte[] imageBytes = new DefaultDrawImagePlugin().drawRelicsImage(relics.stream().limit(9).toList());

        // 保存图像
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageBytes)), Constant.PNG,
                new File(Constant.DRAW_PATH.formatted("draw_relics.png")));
    }
}