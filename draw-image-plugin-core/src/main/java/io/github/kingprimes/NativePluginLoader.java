package io.github.kingprimes;

/**
 * Native 插件加载器接口。
 * <p>
 * core 模块定义此接口，不包含任何 JNA 依赖。
 * 具体的 native 加载实现（如基于 JNA）由 {@code draw-image-plugin-native-jna} 模块提供，
 * 宿主可通过构造器注入的方式将实现传入 {@link DrawImagePluginManager}。
 * </p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
public interface NativePluginLoader {

    /**
     * 从本地库文件加载插件
     *
     * @param libraryName 本地库名称（不包含平台前缀和后缀）
     * @param libraryPath 本地库完整路径
     * @return 加载成功的插件实例
     * @throws Exception 加载失败时抛出
     */
    DrawImagePlugin loadNativePlugin(String libraryName, String libraryPath) throws Exception;

    /**
     * 清理 Native 加载器占用的全局资源。
     * <p>
     * 在宿主重载插件目录时由 {@link DrawImagePluginManager#loadPlugins(String)} 自动调用。
     * 例如 JNA 实现可在此方法中关闭平台线程池。
     * </p>
     */
    default void cleanup() {
        // 默认无操作
    }
}
