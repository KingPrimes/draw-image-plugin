package io.github.kingprimes;

import java.io.File;

/**
 * 基于 JNA 的 NativePluginLoader 实现。
 * <p>
 * 宿主如需支持 {@code .dll/.so/.dylib} 动态库插件，
 * 将此类实例传入 {@link DrawImagePluginManager#DrawImagePluginManager(NativePluginLoader)} 即可。
 * </p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
public class JnaNativePluginLoader implements NativePluginLoader {

    @Override
    public DrawImagePlugin loadNativePlugin(String libraryName, String libraryPath) throws Exception {
        File libFile = new File(libraryPath);
        String canonicalPath = libFile.getCanonicalPath();
        return new JNADrawPluginAdapter(libraryName, canonicalPath);
    }
}
