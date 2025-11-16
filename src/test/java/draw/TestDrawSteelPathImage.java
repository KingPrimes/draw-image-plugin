package draw;

import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.WorldState;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class TestDrawSteelPathImage {

    @Test
    public void drawSteelPathImage() throws IOException {
        WorldState worldState = Constant.WORLD_STATE;

        byte[] bytes = new DefaultDrawImagePlugin().drawSteelPath(worldState.getSteelPath());
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(bytes)),
                Constant.PNG,
                new File(Constant.DRAW_PATH.formatted("draw_steel_path.png")));
    }
}