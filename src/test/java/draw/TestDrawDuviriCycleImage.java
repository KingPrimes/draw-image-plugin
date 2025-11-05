package draw;

import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.WorldState;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TestDrawDuviriCycleImage {

    @Test
    public void drawDuviriCycleImage() throws IOException {
        WorldState worldState = Constant.WORLD_STATE;

        byte[] bytes = new DefaultDrawImagePlugin().drawDuviriCycleImage(worldState.getDuvalierCycle());
        if (bytes.length > 0) {
            ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                    Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_duviri_cycle.png")));
        }
    }
}