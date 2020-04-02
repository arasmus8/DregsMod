package dregsmod.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import dregsmod.cards.UpgradeTextChangingCard;

@SpirePatch(
        clz = AbstractCard.class,
        method = "displayUpgrades"
)
public class DisplayDiffUpgradeTextOnPreviewPatch {
    public static void Postfix(AbstractCard _instance) {
        if (_instance instanceof UpgradeTextChangingCard) {
            _instance.rawDescription = ((UpgradeTextChangingCard) _instance).upgradePreviewText();
            _instance.initializeDescription();
        }
    }
}
