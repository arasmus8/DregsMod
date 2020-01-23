package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class DrawPileToTopOfDeckAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;

    public DrawPileToTopOfDeckAction(AbstractCreature source) {
        p = AbstractDungeon.player;
        setValues(null, source, amount);
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FASTER;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
            isDone = true;
        } else {
            if (duration == Settings.ACTION_DUR_FASTER) {
                if (p.drawPile.isEmpty()) {
                    isDone = true;
                    return;
                }

                if (p.drawPile.size() == 1) {
                    isDone = true;
                    return;
                }

                AbstractDungeon.gridSelectScreen.open(p.drawPile, 1, TEXT[0], false, false, false, false);
                tickDuration();
                return;
            }

            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                    p.drawPile.moveToDeck(card, false);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                p.hand.refreshHandLayout();
            }

            tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("DiscardPileToTopOfDeckAction");// 13
        TEXT = uiStrings.TEXT;// 14
    }
}
