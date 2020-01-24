package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.patches.variables.CardSealed;
import dregsmod.vfx.SealCardEffect;

import java.util.logging.Logger;

public class SealCardAction extends AbstractGameAction {
    private static final Logger logger = Logger.getLogger(SealCardAction.class.getName());
    private AbstractCard card;
    private CardGroup group;

    public SealCardAction(AbstractCard card, CardGroup group) {
        duration = Settings.ACTION_DUR_FASTER;
        setValues(AbstractDungeon.player, AbstractDungeon.player, 1);
        actionType = ActionType.DISCARD;
        this.card = card;
        this.group = group;
    }

    public SealCardAction(AbstractCard card) {
        this(card, AbstractDungeon.player.hand);
    }

    @Override
    public void update() {
        isDone = true;
        if (group.contains(card)) {
            AbstractDungeon.effectList.add(new SealCardEffect(card));

            CardSealed.isSealed.set(card, true);
            card.exhaustOnUseOnce = false;
            card.freeToPlayOnce = false;

            group.moveToDiscardPile(card);
            GameActionManager.incrementDiscard(false);

            card.triggerOnExhaust();
            card.triggerOnManualDiscard();
        }

        tickDuration();
    }
}
