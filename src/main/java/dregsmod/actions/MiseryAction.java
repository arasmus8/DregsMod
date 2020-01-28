package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class MiseryAction extends AbstractGameAction {

    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;

    public MiseryAction(AbstractPlayer player) {
        this.p = player;
        setValues(player, player);
        duration = startDuration = Settings.ACTION_DUR_FASTER;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            if (p.hand.size() == 1) {
                isDone = true;
                AbstractCard card = p.hand.getTopCard();
                card.exhaustOnUseOnce = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(
                        card,
                        true,
                        EnergyPanel.getCurrentEnergy(),
                        false,
                        true
                ));
            } else if (p.hand.size() < 1) {
                isDone = true;
            } else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
            }
        } else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                p.hand.addToTop(card);
                card.exhaustOnUseOnce = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(
                        card,
                        true,
                        EnergyPanel.getCurrentEnergy(),
                        false,
                        true
                ));
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
