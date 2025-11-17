package io.github.kingprimes.model.enums;

import lombok.Getter;

/**
 * 集团枚举
 * <p>使用 Warframe_Font_Icon 字体图标</p>
 *
 * @author KingPrimes
 * @version 1.0.3
 */
@Getter
public enum SyndicateEnum {
    /**
     * 均衡仲裁者
     */
    ArbitersSyndicate("均衡仲裁者", "\ue500"),

    /**
     * 中枢苏达
     */
    CephalonSudaSyndicate("中枢苏达", "\ue501"),

    /**
     * 新世间
     */
    NewLokaSyndicate("新世间", "\ue502"),

    /**
     * 佩兰数列
     */
    PerrinSyndicate("佩兰数列", "\ue505"),

    /**
     * 钢铁防线
     */
    SteelMeridianSyndicate("钢铁防线", "\ue504"),

    /**
     * 血色面纱
     */
    RedVeilSyndicate("血色面纱", "\ue503"),

    /**
     * Ostron
     */
    CetusSyndicate("Ostron", "\ue508"),

    /**
     * 夜羽
     */
    QuillsSyndicate("夜羽", "\ue509"),

    /**
     * 刺客
     */
    AssassinsSyndicate("刺客", ""),

    /**
     * 集团
     */
    EventSyndicate("集团", "\ue407"),

    /**
     * 索拉里斯联盟
     */
    SolarisSyndicate("索拉里斯联盟", "\ue510"),

    /**
     * 索拉里斯之声
     */
    VoxSyndicate("索拉里斯之声", "\ue512"),

    /**
     * 通风小子
     */
    VentKidsSyndicate("通风小子", "\ue511"),
    /**
     * 夜帽
     */
    NightcapJournalSyndicate("夜帽", "\ue513"),
    /**
     * 英择谛
     */
    EntratiSyndicate("英择谛", "\ue514"),

    /**
     * 科维兽
     */
    EntratiLabSyndicate("科维兽", "\ue517"),

    /**
     * The Hex
     */
    HexSyndicate("六人组", "\ue519"),

    /**
     * 殁世械灵
     */
    NecraloidSyndicate("殁世械灵", "\ue515"),

    /**
     * 卡尔驻军
     */
    KahlSyndicate("卡尔驻军", "\ue518"),

    /**
     * 坚守者
     */
    ZarimanSyndicate("坚守者", "\ue516"),

    /**
     * 土星六号之狼
     */
    RadioLegionSyndicate("土星六号之狼", "\ue520"),

    /**
     * 使徒
     */
    RadioLegion2Syndicate("使徒", "\ue520"),

    /**
     * 玻璃匠
     */
    RadioLegion3Syndicate("玻璃匠", "\ue520"),
    RadioLegionIntermissionSyndicate("Intermission", "\ue520"),
    RadioLegionIntermission2Syndicate("Intermission II", "\ue520"),
    RadioLegionIntermission3Syndicate("Intermission III", "\ue520"),
    RadioLegionIntermission4Syndicate("Nora 的精选", "\ue520"),
    RadioLegionIntermission5Syndicate("Nora 的混选 Vol. 1", "\ue520"),
    RadioLegionIntermission6Syndicate("Nora 的混选 Vol. 2", "\ue520"),
    RadioLegionIntermission7Syndicate("RadioLegionIntermission7Syndicate", "\ue520"),
    RadioLegionIntermission8Syndicate("RadioLegionIntermission8Syndicate", "\ue520"),
    RadioLegionIntermission9Syndicate("RadioLegionIntermission9Syndicate", "\ue520"),
    RadioLegionIntermission10Syndicate("RadioLegionIntermission10Syndicate", "\ue520"),
    RadioLegionIntermission11Syndicate("RadioLegionIntermission11Syndicate", "\ue520"),
    RadioLegionIntermission12Syndicate("RadioLegionIntermission12Syndicate", "\ue520"),
    RadioLegionIntermission13Syndicate("RadioLegionIntermission13Syndicate", "\ue520"),

    ;

    private final String name;
    private final String icon;

    SyndicateEnum(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }
}
