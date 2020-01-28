package dregsmod.patches.sealed;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.DregsMod;
import dregsmod.patches.variables.CardSealed;

import java.util.ArrayList;

@SpirePatch(
        clz = EmptyDeckShuffleAction.class,
        method = "update"
)
public class EmptyDeckShuffleActionPatches {
    public static void Postfix(EmptyDeckShuffleAction __instance) {
        boolean vfxDone = (Boolean) ReflectionHacks.getPrivate(__instance, EmptyDeckShuffleAction.class, "vfxDone");
        if (vfxDone) {
            ArrayList<AbstractCard> sealedCards = new ArrayList<>();
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (CardSealed.isSealed.get(card)) {
                    sealedCards.add(card);
                }
            }
            DregsMod.postSealedCards.clear();
            DregsMod.postSealedCards.addAll(sealedCards);
            for (AbstractCard card : sealedCards) {
                CardSealed.isSealed.set(card, false);
                AbstractDungeon.player.drawPile.moveToDiscardPile(card);
            }
        }
    }
}
