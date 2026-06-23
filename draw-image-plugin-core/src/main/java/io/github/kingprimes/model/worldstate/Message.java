package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消息
 * <p>表示不同语言版本的消息内容</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class Message {
    /**
     * 语言代码
     * <p>消息内容对应的语言代码</p>
     */
    @JsonProperty("LanguageCode")
    private LanguageCode languageCode;

    /**
     * 消息内容
     * <p>本地化消息内容的路径或标识符</p>
     */
    @JsonProperty("Message")
    private String message;

    /**
     * 语言代码枚举
     * <p>支持的各种语言代码</p>
     */
    public enum LanguageCode {
        /**
         * 英语
         */
        en,

        /**
         * 中文(简体)
         */
        zh,

        /**
         * 韩语
         */
        ko,

        /**
         * 俄语
         */
        ru,

        /**
         * 土耳其语
         */
        tr,

        /**
         * 西班牙语
         */
        es,

        /**
         * 法语
         */
        fr,

        /**
         * 德语
         */
        de,

        /**
         * 意大利语
         */
        it,

        /**
         * 日语
         */
        jp,

        /**
         * 波兰语
         */
        pl,

        /**
         * 葡萄牙语
         */
        pt,

        /**
         * 越南语
         */
        vi,

        /**
         * 印度尼西亚语
         */
        id,

        /**
         * 泰语
         */
        th,

        /**
         * 阿拉伯语
         */
        ar,

        /**
         * 波斯语
         */
        fa,

        /**
         * 希伯来语
         */
        he,

        /**
         * 亚美尼亚语
         */
        hy,

        /**
         * 格鲁吉亚语
         */
        ka,

        /**
         * 高棉语
         */
        kr,

        /**
         * 马来语
         */
        ms,

        /**
         * 挪威语
         */
        no,

        /**
         * 阿尔巴尼亚语
         */
        sq,

        /**
         * 斯瓦希里语
         */
        sw,

        /**
         * 塔加路语
         */
        tl,

        /**
         * 乌克兰语
         */
        uk,

        /**
         * 乌兹别克语
         */
        uz,

        /**
         * 中文(繁体)
         */
        zh_tw,

        /**
         * 中文(繁体) - 另一种表示方式
         */
        tc,

        /**
         * 日语 - 另一种表示方式
         */
        ja
    }
}
