package draw;

import com.alibaba.fastjson2.JSON;
import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.Arbitration;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TestDrawArbitrationImage {
    @Test
    public void drawArbitrationImage() throws IOException {
        List<Arbitration> javaList = Objects.requireNonNull(JSON.parseArray(Constant.class.getResourceAsStream("/arbitrations.json"))).toJavaList(Arbitration.class);
        Arbitration first = javaList.getFirst();
        byte[] bytes = new DefaultDrawImagePlugin().drawArbitrationImage(first);
        ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_arbitration.png")));
    }

    @Test
    public void drawArbitrationsImage() throws IOException {
        List<Arbitration> javaList = Objects.requireNonNull(JSON.parseArray(Constant.class.getResourceAsStream("/arbitrations.json"))).toJavaList(Arbitration.class);
        byte[] bytes = new DefaultDrawImagePlugin().drawArbitrationsImage(javaList);
        ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_arbitrations.png")));
    }
}
