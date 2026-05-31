package io.github.kingprimes.common;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;
import io.github.kingprimes.model.WorldState;

import java.io.InputStream;

public class Constant {
    /**
     * 绘制图片保存路径</br>
     * %s 为图片名称 </br>
     * 使用 DRAW_PATH.formatted("图片名称") 获取图片路径
     */
    public static final String DRAW_PATH = "./draw/%s";

    public static final String PNG = "png";

    public static final String WORLD_STATUS_PATH = "/state-test.json";
    public static WorldState WORLD_STATE;

    private static final JsonMapper MAPPER = JsonMapper.builder()
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .build();

    static {
        try (InputStream is = Constant.class.getResourceAsStream(WORLD_STATUS_PATH)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + WORLD_STATUS_PATH);
            }
            WORLD_STATE = MAPPER.readValue(is, WorldState.class);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse WorldState JSON", e);
        }
    }
}
