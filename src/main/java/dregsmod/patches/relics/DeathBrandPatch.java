package dregsmod.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dregsmod.relics.CurseBrand;
import dregsmod.relics.DeathBrand;

@SpirePatch(clz = AbstractRelic.class, method = "obtain")
public class DeathBrandPatch {

    @SpirePrefixPatch
    public static SpireReturn ObtainDeathBrand(AbstractRelic __instance) {
        if (__instance.relicId.equals(DeathBrand.ID)) {
            int slot = -1;
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic.relicId.equals(CurseBrand.ID)) {
                    slot = AbstractDungeon.player.relics.indexOf(relic);
                }
            }
            if (slot >= 0) {
                __instance.instantObtain(AbstractDungeon.player, slot, true);// 248
                AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;// 249
                return SpireReturn.Return(null);
            }
        }
        return SpireReturn.Continue();
    }

}
