package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.patches.variables.CardSealed;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SealRandomCurseAction extends AbstractGameAction {
    public SealRandomCurseAction() {
        setValues(AbstractDungeon.player, AbstractDungeon.player, 1);
    }

    @Override
    public void update() {
        isDone = true;
        AbstractPlayer p = AbstractDungeon.player;
        ArrayList<AbstractCard> unsealedCurses = new ArrayList<>();
        Predicate<AbstractCard> unsealedCurseFilter = card -> card.type == AbstractCard.CardType.CURSE && !CardSealed.isSealed.get(card);
        unsealedCurses.addAll(p.hand.group.stream().filter(unsealedCurseFilter).collect(Collectors.toCollection(ArrayList::new)));
        unsealedCurses.addAll(p.drawPile.group.stream().filter(unsealedCurseFilter).collect(Collectors.toCollection(ArrayList::new)));
        unsealedCurses.addAll(p.discardPile.group.stream().filter(unsealedCurseFilter).collect(Collectors.toCollection(ArrayList::new)));

        if (unsealedCurses.size() > 0) {
            AbstractCard curseToSeal = unsealedCurses.get(AbstractDungeon.cardRng.random(unsealedCurses.size() - 1));
            addToBot(new SealAndPerformAction(curseToSeal, null));
        }
    }
}
