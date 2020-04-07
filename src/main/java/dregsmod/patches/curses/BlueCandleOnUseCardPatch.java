package dregsmod.patches.curses;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.BlueCandle;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import dregsmod.cards.curses.Jealousy;
import dregsmod.cards.rare.EclipseForm;

@SpirePatch(
        clz = BlueCandle.class,
        method = "onUseCard"
)
public class BlueCandleOnUseCardPatch {
    public static SpireReturn Prefix(BlueCandle _instance, AbstractCard card, UseCardAction action) {
        if (AbstractDungeon.player.hasPower(EclipseForm.ID)) {
            return SpireReturn.Return(null);
        } else if (card.costForTurn >= 0 && card.costForTurn <= EnergyPanel.getCurrentEnergy()) {
            if (card.cardID.equals(Jealousy.ID)) {
                if (((Jealousy) card).holdingCurse) {
                    return SpireReturn.Return(null);
                } else {
                    return SpireReturn.Continue();
                }
            }
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
