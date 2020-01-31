package dregsmod.patches.cards;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.cards.uncommon.HardLuck;
import dregsmod.cards.uncommon.Unlucky;

import java.util.ArrayList;

@SpirePatch(
        clz = AddCardToDeckAction.class,
        method = "update"
)
public class AddCardToDeckPatch {
    public static void Postfix(AddCardToDeckAction _instance) {
        if (_instance.isDone) {
            AbstractCard card = (AbstractCard) ReflectionHacks.getPrivate(_instance, AddCardToDeckAction.class, "cardToObtain");
            if (card != null && card.type == AbstractCard.CardType.CURSE) {
                ArrayList<AbstractCard> cards = new ArrayList<>();
                cards.addAll(AbstractDungeon.player.hand.group);
                cards.addAll(AbstractDungeon.player.drawPile.group);
                cards.addAll(AbstractDungeon.player.discardPile.group);
                cards.addAll(AbstractDungeon.player.exhaustPile.group);
                for (AbstractCard c : cards) {
                    if (c instanceof HardLuck) {
                        c.updateCost(-1);
                    } else if (c instanceof Unlucky) {
                        c.updateCost(-1);
                    }
                }
            }
        }
    }
}
