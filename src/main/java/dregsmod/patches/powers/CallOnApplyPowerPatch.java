package dregsmod.patches.powers;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = "update"
)
public class CallOnApplyPowerPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(ApplyPowerAction __instance) {
        if (__instance.target != null && (__instance.source == null || !__instance.source.isPlayer)) {
            AbstractPower powerToApply = (AbstractPower) ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
            for (AbstractPower pow : __instance.target.powers) {
                pow.onApplyPower(powerToApply, __instance.target, __instance.source);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "player");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
