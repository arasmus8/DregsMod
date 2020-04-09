package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KindleAction extends AbstractGameAction {

    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractCard card;
    private boolean returnToHand;

    public KindleAction(AbstractCard card, boolean returnToHand) {
        setValues(AbstractDungeon.player, AbstractDungeon.player);
        this.card = card;
        this.returnToHand = returnToHand;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            CardGroup cg = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            AbstractPlayer p = AbstractDungeon.player;
            Predicate<AbstractCard> eligible = card1 -> (card1.cost > -2) &&
                    (card1.type == AbstractCard.CardType.ATTACK || card1.type == AbstractCard.CardType.SKILL);
            cg.group.addAll(p.hand.group.stream().filter(eligible).collect(Collectors.toList()));
            cg.group.addAll(p.discardPile.group.stream().filter(eligible).collect(Collectors.toList()));
            cg.group.addAll(p.drawPile.group.stream().filter(eligible).collect(Collectors.toList()));
            if (cg.size() < 1) {
                isDone = true;
            } else if (cg.size() == 1) {
                AbstractCard toAwaken = cg.getTopCard();
                addToTop(new CardAwokenAction(toAwaken, 1));

                card.returnToHand = returnToHand;
                isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(cg, 1, false, TEXT[0]);
            }
        } else if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                addToTop(new CardAwokenAction(c, 1));
            }

            card.returnToHand = returnToHand;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
            isDone = true;
        }
        tickDuration();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ArmamentsAction");
        TEXT = uiStrings.TEXT;
    }
}
