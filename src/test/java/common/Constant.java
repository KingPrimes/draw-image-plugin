package common;

import com.alibaba.fastjson2.JSON;
import io.github.kingprimes.model.WorldState;

public class Constant {
    /**
     * 绘制图片保存路径</br>
     * %s 为图片名称 </br>
     * 使用 DRAW_PATH.formatted("图片名称") 获取图片路径
     */
    public static final String DRAW_PATH = "./draw/%s";

    public static final String PNG = "png";

    public static final String WORLD_STATUS_PATH = "/state-2025-10-28.json";
    public static WorldState WORLD_STATE;

    static {
        WORLD_STATE = JSON.parseObject(Constant.class.getResourceAsStream(WORLD_STATUS_PATH), WorldState.class);
    }
}
