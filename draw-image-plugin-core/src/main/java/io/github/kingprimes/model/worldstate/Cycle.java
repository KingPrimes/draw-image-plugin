package io.github.kingprimes.model.worldstate;

import java.time.Instant;

/**
 * 世界状态周期类的公共接口。
 * 统一暴露 {@link #getExpiry()} 方法，使各周期类可在 {@code Stream.of()} 中统一类型推断。
 */
public interface Cycle {
    Instant getExpiry();
}
