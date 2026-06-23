package io.github.kingprimes.model.enums;

import lombok.Getter;

/**
 * Warframe Boss 枚举集
 * <p>该枚举包含了游戏中各种Boss的信息，包括Boss名称和所属派系</p>
 *
 * @author kingprimes
 * @version 1.0.0
 * @see <a href="https://wiki.warframe.com/w/Bosses">Boss Bosses</a>
 */
@Getter
public enum BossEnum {
    /**
     * 鬣狗群
     * <p>Corpus派系的Boss</p>
     */
    SORTIE_BOSS_HYENA("鬣狗群", "Corpus"),
    /**
     * Kela De Thaym
     * <p>Grineer派系的Boss</p>
     */
    SORTIE_BOSS_KELA("Kela De Thaym", "Grineer"),
    /**
     * Vor 上尉
     * <p>Grineer派系的Boss</p>
     */
    SORTIE_BOSS_VOR("Vor 上尉", "Grineer"),
    /**
     * Sargas Ruk 将军
     * <p>Grineer派系的Boss</p>
     */
    SORTIE_BOSS_RUK("Sargas Ruk 将军", "Grineer"),
    /**
     * Vay Hek 委员
     * <p>Grineer派系的Boss</p>
     */
    SORTIE_BOSS_HEK("Vay Hek 委员", "Grineer"),
    /**
     * Lech Kril 中尉
     * <p>Grineer派系的Boss</p>
     */
    SORTIE_BOSS_KRIL("Lech Kril 中尉", "Grineer"),
    /**
     * Tyl Regor
     * <p>Grineer派系的Boss</p>
     */
    SORTIE_BOSS_TYL("Tyl Regor", "Grineer"),
    /**
     * 豺狼
     * <p>Corpus派系的Boss</p>
     */
    SORTIE_BOSS_JACKAL("豺狼", "Corpus"),
    /**
     * Alad V
     * <p>Corpus派系的Boss</p>
     */
    SORTIE_BOSS_ALAD("Alad V", "Corpus"),
    /**
     * Ambulas
     * <p>Corpus派系的Boss</p>
     */
    SORTIE_BOSS_AMBULAS("Ambulas", "Corpus"),
    /**
     * Nef Anyo
     * <p>Corpus派系的Boss</p>
     */
    SORTIE_BOSS_NEF("Nef Anyo", "Corpus"),
    /**
     * 猛禽
     * <p>Corpus派系的Boss</p>
     */
    SORTIE_BOSS_RAPTOR("猛禽", "Corpus"),
    /**
     * Phorid
     * <p>Infestation派系的Boss</p>
     */
    SORTIE_BOSS_PHORID("Phorid", "Infestation"),
    /**
     * Lephantis
     * <p>Infestation派系的Boss</p>
     */
    SORTIE_BOSS_LEPHANTIS("Lephantis", "Infestation"),
    /**
     * 异融者 Alad V
     * <p>Infestation派系的Boss</p>
     */
    SORTIE_BOSS_INFALAD("异融者 Alad V", "Infestation"),
    /**
     * 堕落 Vor
     * <p>堕落者派系的Boss</p>
     */
    SORTIE_BOSS_CORRUPTED_VOR("堕落 Vor", "堕落者"),
    /**
     * 诡文枭主
     * <p>合一众派系的Boss</p>
     */
    SORTIE_BOSS_BOREAL("诡文枭主", "合一众"),
    /**
     * 欺谋狼主
     * <p>合一众派系的Boss</p>
     */
    SORTIE_BOSS_AMAR("欺谋狼主", "合一众"),
    /**
     * 混沌蛇主
     * <p>合一众派系的Boss</p>
     */
    SORTIE_BOSS_NIRA("混沌蛇主", "合一众"),
    /**
     * 帕祖
     * <p>合一众派系的Boss</p>
     */
    SORTIE_BOSS_PAAZUL("帕祖", "合一众");

    private final String name;
    private final String faction;

    /**
     * Boss枚举构造函数
     *
     * @param name    Boss名称
     * @param faction Boss所属派系
     */
    BossEnum(String name, String faction) {
        this.name = name;
        this.faction = faction;
    }
}
