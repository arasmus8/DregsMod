package dregsmod.patches.curses;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.BlueCandle;
import dregsmod.cards.rare.EclipseForm;

@SpirePatch(
        clz = BlueCandle.class,
        method = "onUseCard"
)
public class BlueCandleOnUseCardPatch {
    public static SpireReturn Prefix(BlueCandle _instance, AbstractCard card, UseCardAction action) {
        if (AbstractDungeon.player.hasPower(EclipseForm.ID)) {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
