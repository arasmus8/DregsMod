package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.patches.variables.CardSealed;

public class SetCardSealedAction extends AbstractGameAction {
    private AbstractCard card;

    public SetCardSealedAction(AbstractCard card) {
        duration = Settings.ACTION_DUR_XFAST;
        setValues(AbstractDungeon.player, AbstractDungeon.player, 1);
        this.card = card;
    }

    public SetCardSealedAction() {
        this(null);
    }

    @Override
    public void update() {
        isDone = true;
        if (card != null) {
            CardSealed.isSealed.set(card, true);
            card.triggerOnExhaust();
        } else {
            for (AbstractCard card : DiscardAndPerformAction.discardedCards) {
                CardSealed.isSealed.set(card, true);
                card.triggerOnExhaust();
            }
        }
        tickDuration();
    }
}
