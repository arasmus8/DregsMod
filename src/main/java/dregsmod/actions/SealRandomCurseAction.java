package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dregsmod.patches.variables.CardSealed;
import dregsmod.powers.TriggerOnSealedPower;
import dregsmod.vfx.SealCardEffect;

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
            CardSealed.isSealed.set(curseToSeal, true);
            addToBot(new VFXAction(new SealCardEffect(curseToSeal.makeSameInstanceOf())));
            if (p.hand.contains(curseToSeal)) {
                p.hand.moveToDiscardPile(curseToSeal);
            } else if (p.drawPile.contains(curseToSeal)) {
                p.drawPile.moveToDiscardPile(curseToSeal);
            }
            for (AbstractPower power : p.powers) {
                if (power instanceof TriggerOnSealedPower) {
                    ((TriggerOnSealedPower) power).triggerOnSealed(curseToSeal);
                }
            }
        }
    }
}
