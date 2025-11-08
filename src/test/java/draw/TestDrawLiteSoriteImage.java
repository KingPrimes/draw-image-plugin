package draw;

import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.worldstate.LiteSorite;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TestDrawLiteSoriteImage {

    @Test
    public void drawLiteSoriteImage() throws IOException {
        LiteSorite first = Constant.WORLD_STATE.getLiteSorties().getFirst();
        byte[] bytes = new DefaultDrawImagePlugin().drawLiteSoriteImage(first);
        ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_lite_sorite.png")));
    }
}