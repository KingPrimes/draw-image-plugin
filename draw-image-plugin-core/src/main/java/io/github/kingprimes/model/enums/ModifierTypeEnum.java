package io.github.kingprimes.model.enums;

import lombok.Getter;

/**
 * 突击任务限定
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Getter
public enum ModifierTypeEnum {

    /**
     * 辐射灾害
     */
    SORTIE_MODIFIER_HAZARD_RADIATION("辐射灾害"),

    /**
     * 敌人物理强化(切割)
     */
    SORTIE_MODIFIER_SLASH("敌人物理强化(切割)"),

    /**
     * 仅限弓箭/弩
     */
    SORTIE_MODIFIER_BOW_ONLY("仅限弓箭/弩"),

    /**
     * 敌人元素强化(磁力)
     */
    SORTIE_MODIFIER_MAGNETIC("敌人元素强化(磁力)"),

    /**
     * 卓越者大本营
     */
    SORTIE_MODIFIER_EXIMUS("卓越者大本营"),

    /**
     * 敌人元素强化(爆炸)
     */
    SORTIE_MODIFIER_EXPLOSION("敌人元素强化(爆炸)"),

    /**
     * 能量上限减少
     */
    SORTIE_MODIFIER_LOW_ENERGY("能量上限减少"),

    /**
     * 仅限霰弹枪
     */
    SORTIE_MODIFIER_SHOTGUN_ONLY("仅限霰弹枪"),
    /**
     * 仅限突击步枪
     */
    SORTIE_MODIFIER_RIFLE_ONLY("仅限突击步枪"),

    /**
     * 敌人护盾强化
     */
    SORTIE_MODIFIER_SHIELDS("敌人护盾强化"),

    /**
     * 敌人元素强化(冲击)
     */
    SORTIE_MODIFIER_IMPACT("敌人物理强化(冲击)"),

    /**
     * 敌人元素强化(穿刺)
     */
    SORTIE_MODIFIER_PUNCTURE("敌人物理强化(穿刺)"),

    /**
     * 敌人元素强化(腐蚀)
     */
    SORTIE_MODIFIER_CORROSIVE("敌人元素强化(腐蚀)"),

    /**
     * 敌人元素强化(病毒)
     */
    SORTIE_MODIFIER_VIRAL("敌人元素强化(病毒)"),

    /**
     * 敌人元素强化(电击)
     */
    SORTIE_MODIFIER_ELECTRICITY("敌人元素强化(电击)"),

    /**
     * 敌人元素强化(辐射)
     */
    SORTIE_MODIFIER_RADIATION("敌人元素强化(辐射)"),

    /**
     * 敌人元素强化(毒气)
     */
    SORTIE_MODIFIER_GAS("敌人元素强化(毒气)"),

    /**
     * 敌人元素强化(火焰)
     */
    SORTIE_MODIFIER_FIRE("敌人元素强化(火焰)"),

    /**
     * 敌人元素强化(冰冻)
     */
    SORTIE_MODIFIER_FREEZE("敌人元素强化(冰冻)"),

    /**
     * 敌人元素强化(毒素)
     */
    SORTIE_MODIFIER_TOXIN("敌人元素强化(毒素)"),

    /**
     * 敌人元素强化(毒素)
     */
    SORTIE_MODIFIER_POISON("敌人元素强化(毒素)"),

    /**
     * 电磁异常
     */
    SORTIE_MODIFIER_HAZARD_MAGNETIC("电磁异常"),

    /**
     * 浓雾
     */
    SORTIE_MODIFIER_HAZARD_FOG("浓雾"),

    /**
     * 环境危害：火災
     */
    SORTIE_MODIFIER_HAZARD_FIRE("环境危害︰火災"),

    /**
     * 低温外泄
     */
    SORTIE_MODIFIER_HAZARD_ICE("低温外泄"),

    /**
     * 极度寒冷
     */
    SORTIE_MODIFIER_HAZARD_COLD("极度寒冷"),

    /**
     * 敌人护甲强化
     */
    SORTIE_MODIFIER_ARMOR("敌人护甲强化"),

    /**
     * 武器限定：次要武器
     */
    SORTIE_MODIFIER_SECONDARY_ONLY("武器限定：次要武器"),

    /**
     * 武器限定：狙击枪
     */
    SORTIE_MODIFIER_SNIPER_ONLY("武器限定：狙击枪"),

    /**
     * 武器限定：近战
     */
    SORTIE_MODIFIER_MELEE_ONLY("武器限定：近战"),

    ;
    final String str;

    ModifierTypeEnum(String str) {
        this.str = str;
    }
}