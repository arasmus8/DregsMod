package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import dregsmod.patches.variables.CardSealed;

public class SealCardAction extends AbstractGameAction {
    private AbstractCard card;
    private CardGroup group;

    public SealCardAction(AbstractCard card, CardGroup group) {
        duration = Settings.ACTION_DUR_FASTER;
        setValues(AbstractDungeon.player, AbstractDungeon.player, 1);
        this.card = card;
        this.group = group;
    }

    public SealCardAction(AbstractCard card) {
        this(card, AbstractDungeon.player.hand);
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FASTER) {
            if (group.contains(card)) {
                // TODO make card sealing vfx -- see SoundMaster "KEY_OBTAIN"
                AbstractDungeon.effectList.add(new ExhaustCardEffect(card));

                CardSealed.isSealed.set(card, true);
                card.exhaustOnUseOnce = false;
                card.freeToPlayOnce = false;

                group.moveToDiscardPile(card);
                GameActionManager.incrementDiscard(false);

                card.triggerOnExhaust();
                card.triggerOnManualDiscard();
            }

        }
    }
}
