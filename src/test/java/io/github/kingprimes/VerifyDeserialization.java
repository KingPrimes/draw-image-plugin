package io.github.kingprimes;

import io.github.kingprimes.model.WorldState;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;

/**
 * now.json 反序列化验证工具
 * <p>验证所有 WorldState 字段是否能正确从 API JSON 数据中反序列化</p>
 */
public class VerifyDeserialization {
    public static void main(String[] args) throws Exception {
        var mapper = JsonMapper.builder()
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .build();

        File file = new File("now1.json");
        if (!file.exists()) {
            System.err.println("now1.json not found!");
            System.exit(1);
        }

        System.out.println("开始反序列化 now.json ...");
        long start = System.currentTimeMillis();
        WorldState ws = mapper.readValue(file, WorldState.class);
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("反序列化成功! 耗时: " + elapsed + "ms\n");

        int passed = 0;
        int total = 0;
        StringBuilder details = new StringBuilder();

        // === 基础字段 ===
        total++; if (ws.getWorldSeed() != null) { passed++; } else { details.append("FAIL: WorldSeed\n"); }
        total++; if (ws.getVersion() != null) { passed++; } else { details.append("FAIL: Version\n"); }
        total++; if (ws.getBuildLabel() != null) { passed++; } else { details.append("FAIL: BuildLabel\n"); }
        total++; if (ws.getMobileVersion() != null) { passed++; } else { details.append("FAIL: MobileVersion\n"); }
        total++; if (ws.getTime() != null) { passed++; } else { details.append("FAIL: Time\n"); }

        // === Events (P1: EventLiveUrl, Links) ===
        total++; if (ws.getEvents() != null && ws.getEvents().size() == 33) {
            passed++;
            long urlCount = ws.getEvents().stream().filter(e -> e.getEventLiveUrl() != null).count();
            long linkCount = ws.getEvents().stream().filter(e -> e.getLinks() != null && !e.getLinks().isEmpty()).count();
            details.append(String.format("Events: %d 条 (EventLiveUrl=%d, Links=%d)\n", ws.getEvents().size(), urlCount, linkCount));
        } else { details.append(String.format("FAIL: Events (expected 33, got %d)\n", ws.getEvents() != null ? ws.getEvents().size() : 0)); }

        // === Goals (P0: ToolTip, GracePeriod, ItemType) ===
        total++; if (ws.getGoals() != null && !ws.getGoals().isEmpty()) {
            passed++;
            var g = ws.getGoals().getFirst();
            details.append(String.format("Goals: %d 条 | ToolTip=%s | GracePeriod=%s | ItemType=%s\n",
                    ws.getGoals().size(), g.getToolTip(),
                    g.getGracePeriod() != null ? "OK" : "null",
                    g.getItemType() != null ? "OK" : "null"));
        } else { details.append("FAIL: Goals\n"); }

        // === Alerts (P1: Icon + seed/questReq/leadersAlwaysAllowed/extraEnemySpec) ===
        total++; if (ws.getAlerts() != null && ws.getAlerts().size() == 3) {
            passed++;
            var a = ws.getAlerts().getFirst();
            var ami = a.getMissionInfo();
            details.append(String.format("Alerts: %d 条 | Icon=%s | seed=%s | questReq=%s | leadersAlwaysAllowed=%s\n",
                    ws.getAlerts().size(), a.getIcon() != null ? "OK" : "null",
                    ami != null ? ami.getSeed() : "N/A",
                    ami != null ? ami.getQuestReq() : "N/A",
                    ami != null ? ami.getLeadersAlwaysAllowed() : "N/A"));
        } else { details.append(String.format("FAIL: Alerts (expected 3, got %d)\n", ws.getAlerts() != null ? ws.getAlerts().size() : 0)); }

        // === Sorties ===
        total++; if (ws.getSorties() != null && !ws.getSorties().isEmpty()) { passed++; details.append(String.format("Sorties: %d 条\n", ws.getSorties().size())); } else { details.append("FAIL: Sorties\n"); }
        total++; if (ws.getLiteSorties() != null && !ws.getLiteSorties().isEmpty()) { passed++; details.append(String.format("LiteSorties: %d 条\n", ws.getLiteSorties().size())); } else { details.append("FAIL: LiteSorties\n"); }

        // === Invasions (P0: List+ACCEPT_SINGLE) ===
        total++; if (ws.getInvasions() != null && ws.getInvasions().size() == 7) {
            passed++;
            long withReward = ws.getInvasions().stream().filter(i -> i.getAttackerReward() != null && !i.getAttackerReward().isEmpty()).count();
            details.append(String.format("Invasions: %d 条 (有奖励=%d)\n", ws.getInvasions().size(), withReward));
        } else { details.append(String.format("FAIL: Invasions (expected 7, got %d)\n", ws.getInvasions() != null ? ws.getInvasions().size() : 0)); }

        // === ActiveMissions ===
        total++; if (ws.getActiveMissions() != null && ws.getActiveMissions().size() == 23) { passed++; details.append(String.format("ActiveMissions: %d 条\n", ws.getActiveMissions().size())); } else { details.append(String.format("FAIL: ActiveMissions (expected 23, got %d)\n", ws.getActiveMissions() != null ? ws.getActiveMissions().size() : 0)); }

        // === SyndicateMissions (枚举修复: Intermission14/15) ===
        total++; if (ws.getSyndicateMissions() != null && ws.getSyndicateMissions().size() == 37) {
            passed++;
            details.append(String.format("SyndicateMissions: %d 条\n", ws.getSyndicateMissions().size()));
        } else { details.append(String.format("FAIL: SyndicateMissions (expected 37, got %d)\n", ws.getSyndicateMissions() != null ? ws.getSyndicateMissions().size() : 0)); }

        // === NodeOverrides (P1: @JsonProperty) ===
        total++; if (ws.getNodeOverrides() != null && !ws.getNodeOverrides().isEmpty()) { passed++; details.append(String.format("NodeOverrides: %d 条\n", ws.getNodeOverrides().size())); } else { details.append("FAIL: NodeOverrides\n"); }

        // === Conquests (P0: 完整重写) ===
        total++; if (ws.getConquests() != null && ws.getConquests().size() == 2) {
            passed++;
            var c = ws.getConquests().getFirst();
            details.append(String.format("Conquests: %d 条 | Type=%s | Missions=%d | Variables=%d | RandomSeed=%s\n",
                    ws.getConquests().size(), c.getType(),
                    c.getMissions() != null ? c.getMissions().size() : 0,
                    c.getVariables() != null ? c.getVariables().size() : 0,
                    c.getRandomSeed()));
        } else { details.append(String.format("FAIL: Conquests (expected 2, got %d)\n", ws.getConquests() != null ? ws.getConquests().size() : 0)); }

        // === Descents (P0: 完整重写 + Long溢出修复) ===
        total++; if (ws.getDescents() != null && ws.getDescents().size() == 5) {
            passed++;
            var d = ws.getDescents().getFirst();
            details.append(String.format("Descents: %d 条 | RandSeed=%s | Challenges=%d\n",
                    ws.getDescents().size(), d.getRandSeed(),
                    d.getChallenges() != null ? d.getChallenges().size() : 0));
        } else { details.append(String.format("FAIL: Descents (expected 5, got %d)\n", ws.getDescents() != null ? ws.getDescents().size() : 0)); }

        // === SeasonInfo (P2: Activation/Expiry in ActiveChallenges) ===
        total++; if (ws.getSeasonInfo() != null && ws.getSeasonInfo().getActiveChallenges() != null
                && !ws.getSeasonInfo().getActiveChallenges().isEmpty()) {
            passed++;
            var ac = ws.getSeasonInfo().getActiveChallenges().getFirst();
            details.append(String.format("SeasonInfo: %d ActiveChallenges | Activation=%s | Expiry=%s\n",
                    ws.getSeasonInfo().getActiveChallenges().size(),
                    ac.getActivation() != null ? "OK" : "null",
                    ac.getExpiry() != null ? "OK" : "null"));
        } else { details.append("FAIL: SeasonInfo\n"); }

        // === P1 新增顶层字段 ===
        total++; if (ws.getSkuSales() != null) { passed++; } else { details.append("FAIL: SkuSales\n"); }
        total++; if (ws.getPrimeAccessAvailability() != null && "PRIME2".equals(ws.getPrimeAccessAvailability().getState())) { passed++; details.append("PrimeAccessAvailability: PRIME2\n"); } else { details.append("FAIL: PrimeAccessAvailability\n"); }
        total++; if (ws.getPrimeVaultAvailabilities() != null && ws.getPrimeVaultAvailabilities().size() == 5) { passed++; details.append(String.format("PrimeVaultAvailabilities: %d 条\n", ws.getPrimeVaultAvailabilities().size())); } else { details.append("FAIL: PrimeVaultAvailabilities\n"); }
        total++; if (ws.getPrimeTokenAvailability() != null && ws.getPrimeTokenAvailability()) { passed++; details.append("PrimeTokenAvailability: true\n"); } else { details.append("FAIL: PrimeTokenAvailability\n"); }
        total++; if (ws.getPvpChallengeInstances() != null) { passed++; details.append(String.format("PVPChallengeInstances: %d 条\n", ws.getPvpChallengeInstances().size())); } else { details.append("FAIL: PVPChallengeInstances\n"); }
        total++; if (ws.getPersistentEnemies() != null) { passed++; } else { details.append("FAIL: PersistentEnemies\n"); }
        total++; if (ws.getPvpAlternativeModes() != null) { passed++; } else { details.append("FAIL: PVPAlternativeModes\n"); }
        total++; if (ws.getPvpActiveTournaments() != null) { passed++; } else { details.append("FAIL: PVPActiveTournaments\n"); }
        total++; if (ws.getTwitchPromos() != null) { passed++; } else { details.append("FAIL: TwitchPromos\n"); }
        total++; if (ws.getEndlessXpSchedule() != null && ws.getEndlessXpSchedule().size() == 1) { passed++; details.append("EndlessXpSchedule: 1 条\n"); } else { details.append("FAIL: EndlessXpSchedule\n"); }

        // === 其他 ===
        total++; if (ws.getDailyDeals() != null) { passed++; details.append(String.format("DailyDeals: %d 条\n", ws.getDailyDeals().size())); } else { details.append("FAIL: DailyDeals\n"); }
        total++; if (ws.getInGameMarket() != null) { passed++; details.append("InGameMarket: OK\n"); } else { details.append("FAIL: InGameMarket\n"); }
        total++; if (ws.getLibraryInfo() != null) { passed++; details.append("LibraryInfo: OK\n"); } else { details.append("FAIL: LibraryInfo\n"); }
        total++; if (ws.getVoidTraders() != null) { passed++; details.append(String.format("VoidTraders: %d 条\n", ws.getVoidTraders().size())); } else { details.append("FAIL: VoidTraders\n"); }
        total++; if (ws.getVoidStorms() != null) { passed++; details.append(String.format("VoidStorms: %d 条\n", ws.getVoidStorms().size())); } else { details.append("FAIL: VoidStorms\n"); }

        System.out.print(details.toString());
        System.out.println("========================================");
        System.out.println("验证结果: " + passed + "/" + total + " 通过");
        if (passed == total) {
            System.out.println("所有字段全部正确反序列化!");
        } else {
            System.out.println("有 " + (total - passed) + " 项失败!");
            System.exit(1);
        }
    }
}
