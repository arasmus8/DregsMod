package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.cards.AwakenedMod;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CardAwokenAction extends AbstractGameAction {
    private AbstractCard card;

    public CardAwokenAction(AbstractCard card, int times) {
        setValues(AbstractDungeon.player, AbstractDungeon.player, times);
        this.card = card;
    }

    public CardAwokenAction(int times) {
        this(randomCard(), times);
    }

    public CardAwokenAction() {
        this(randomCard(), 0);
    }

    private static AbstractCard randomCard() {
        CardGroup cg = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        AbstractPlayer p = AbstractDungeon.player;
        Predicate<AbstractCard> eligible = card1 -> card1.type == AbstractCard.CardType.ATTACK ||
                card1.type == AbstractCard.CardType.SKILL;
        cg.group.addAll(p.hand.group.stream().filter(eligible).collect(Collectors.toList()));
        cg.group.addAll(p.discardPile.group.stream().filter(eligible).collect(Collectors.toList()));
        cg.group.addAll(p.drawPile.group.stream().filter(eligible).collect(Collectors.toList()));
        if (!cg.isEmpty()) {
            return cg.getRandomCard(AbstractDungeon.cardRng);
        } else {
            return null;
        }
    }

    @Override
    public void update() {
        isDone = true;
        if (card != null) {
            if (amount > 0) {
                AwakenedMod.awakenCard(card, amount);
            } else {
                AwakenedMod.awakenCard(card);
            }
        }
    }
}
