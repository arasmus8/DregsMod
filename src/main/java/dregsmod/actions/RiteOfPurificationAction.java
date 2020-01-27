package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class RiteOfPurificationAction extends AbstractGameAction {

    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractCard riteCard;

    public RiteOfPurificationAction(AbstractCard riteCard) {
        setValues(AbstractDungeon.player, AbstractDungeon.player);
        this.riteCard = riteCard;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
        } else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            boolean curseExhausted = false;
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                AbstractDungeon.player.hand.addToTop(card);
                addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
                if (card.type == AbstractCard.CardType.CURSE) {
                    curseExhausted = true;
                }
            }

            if (curseExhausted) {
                riteCard.returnToHand = true;
                addToTop(new DrawCardAction(AbstractDungeon.player, 1));
            } else {
                riteCard.returnToHand = false;
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.clear();
            isDone = true;
        }
        tickDuration();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");
        TEXT = uiStrings.TEXT;
    }
}
