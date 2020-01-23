package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class CurateAction extends AbstractGameAction {
    public CurateAction(AbstractCreature source, int block) {
        setValues(source, source, block);
    }

    @Override
    public void update() {
        isDone = true;
        for (AbstractCard card : DiscardAndPerformAction.discardedCards) {
            if (card.type == AbstractCard.CardType.CURSE) {
                addToTop(new GainBlockAction(source, source, amount));
                break;
            }
        }
    }
}
