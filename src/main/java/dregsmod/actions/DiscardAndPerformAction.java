package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class DiscardAndPerformAction extends AbstractGameAction {
    public static ArrayList<AbstractCard> discardedCards = new ArrayList<>();
    private AbstractGameAction followUpAction;
    private boolean clearDiscardHistory;
    private boolean isRandom;
    private static final AbstractPlayer p = AbstractDungeon.player;
    private static final float DURATION;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public DiscardAndPerformAction(
            int amount,
            boolean random,
            AbstractGameAction action
    ) {
        setValues(p, p, amount);
        actionType = ActionType.DISCARD;
        duration = DURATION;
        followUpAction = action;
        isRandom = random;
        clearDiscardHistory = true;
    }

    public DiscardAndPerformAction(int amount, AbstractGameAction action) {
        this(amount, false, action);
    }

    @Override
    public void update() {
        if (duration == DURATION) {
            if (clearDiscardHistory) {
                clearDiscardHistory = false;
                discardedCards.clear();
            }

            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                isDone = true;
                return;
            }

            if (p.hand.size() <= amount) {
                discardedCards.addAll(p.hand.group);
                while (p.hand.size() > 0) {
                    AbstractCard card = p.hand.getTopCard();
                    p.hand.moveToDiscardPile(card);
                    card.triggerOnManualDiscard();
                    GameActionManager.incrementDiscard(false);
                }

                p.hand.applyPowers();
                endActionWithFollowUp();
                return;
            }

            if (!isRandom) {
                if (amount < 0) {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], 99, true, true);
                    p.hand.applyPowers();
                    tickDuration();
                    return;
                }

                if (p.hand.size() > amount) {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], amount, false);
                    p.hand.applyPowers();
                    tickDuration();
                    return;
                }
            } else {

                AbstractCard c;
                for (int i = 0; i < amount; ++i) {
                    c = p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                    discardedCards.add(c);
                    p.hand.moveToDiscardPile(c);
                    c.triggerOnManualDiscard();
                    GameActionManager.incrementDiscard(false);
                    endActionWithFollowUp();
                }
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                discardedCards.add(card);
                p.hand.moveToDiscardPile(card);
                card.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            endActionWithFollowUp();
        }

        tickDuration();
    }

    private void endActionWithFollowUp() {
        isDone = true;
        if (followUpAction != null) {
            addToTop(followUpAction);
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
        TEXT = uiStrings.TEXT;
        DURATION = Settings.ACTION_DUR_XFAST;
    }
}
