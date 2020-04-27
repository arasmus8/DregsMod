package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.cards.AwakenedMod;
import dregsmod.vfx.AwakenCardEffect;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CardAwokenAction extends AbstractGameAction {
    private AbstractCard card;
    private CardGroup fromGroup;

    public CardAwokenAction(AbstractCard card, int times) {
        setValues(AbstractDungeon.player, AbstractDungeon.player, times);
        this.card = card;
        fromGroup = null;
    }

    public CardAwokenAction(CardGroup group, int times) {
        this((AbstractCard) null, times);
        fromGroup = group;
    }

    public CardAwokenAction(int times) {
        this((AbstractCard) null, times);
    }

    public CardAwokenAction() {
        this((AbstractCard) null, 0);
    }

    private static AbstractCard randomCard(CardGroup group) {
        CardGroup cg = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        Predicate<AbstractCard> eligible = AwakenedMod.eligibleToAwaken;
        if (group != null) {
            cg.group.addAll(group.group.stream().filter(eligible).collect(Collectors.toList()));
        } else {
            AbstractPlayer p = AbstractDungeon.player;
            cg.group.addAll(p.hand.group.stream().filter(eligible).collect(Collectors.toList()));
            cg.group.addAll(p.discardPile.group.stream().filter(eligible).collect(Collectors.toList()));
            cg.group.addAll(p.drawPile.group.stream().filter(eligible).collect(Collectors.toList()));
        }
        if (!cg.isEmpty()) {
            return cg.getRandomCard(AbstractDungeon.cardRng);
        } else {
            return null;
        }
    }

    @Override
    public void update() {
        isDone = true;
        if (card == null) {
            card = randomCard(fromGroup);
        }
        if (card != null) {
            if (amount > 0) {
                AwakenedMod.awakenCard(card, amount);
            } else {
                AwakenedMod.awakenCard(card);
            }
            AbstractDungeon.effectList.add(new AwakenCardEffect(card.makeStatEquivalentCopy()));
        }
    }
}
