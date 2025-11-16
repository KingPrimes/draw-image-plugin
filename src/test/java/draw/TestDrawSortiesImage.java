package draw;

import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.WorldState;
import io.github.kingprimes.model.worldstate.Sortie;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestDrawSortiesImage {

    @Test
    public void drawSortiesImage() throws IOException {
        WorldState worldState = Constant.WORLD_STATE;
        List<Sortie> sorties = worldState.getSorties();

        byte[] bytes = new DefaultDrawImagePlugin().drawSortiesImage(sorties);
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(bytes)),
                Constant.PNG,
                new File(Constant.DRAW_PATH.formatted("draw_sorties.png")));
    }
}