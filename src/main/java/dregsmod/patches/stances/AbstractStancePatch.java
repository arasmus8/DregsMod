package dregsmod.patches.stances;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.stances.AbstractStance;
import dregsmod.stances.AcceptanceStance;
import dregsmod.stances.DenialStance;

@SpirePatch(
        clz = AbstractStance.class,
        method = "getStanceFromName"
)
public class AbstractStancePatch {
    public static SpireReturn<AbstractStance> Prefix(String name) {
        if (name.equals(AcceptanceStance.STANCE_ID)) {
            return SpireReturn.Return(new AcceptanceStance());
        } else if (name.equals(DenialStance.STANCE_ID)) {
            return SpireReturn.Return(new DenialStance());
        } else {
            return SpireReturn.Continue();
        }
    }
}
