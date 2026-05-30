package io.github.kingprimes.draw;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import io.github.kingprimes.common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.Arbitration;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestDrawArbitrationImage {
    private static final JsonMapper MAPPER = new JsonMapper();

    @Test
    public void drawArbitrationImage() throws IOException {
        List<Arbitration> javaList = MAPPER.readValue(Constant.class.getResourceAsStream("/arbitrations.json"),
                new TypeReference<List<Arbitration>>() {});
        Arbitration first = javaList.getFirst();
        byte[] bytes = new DefaultDrawImagePlugin().drawArbitrationImage(first);
        ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_arbitration.png")));
    }

    @Test
    public void drawArbitrationsImage() throws IOException {
        List<Arbitration> javaList = MAPPER.readValue(Constant.class.getResourceAsStream("/arbitrations.json"),
                new TypeReference<List<Arbitration>>() {});
        byte[] bytes = new DefaultDrawImagePlugin().drawArbitrationsImage(javaList);
        ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_arbitrations.png")));
    }
}
