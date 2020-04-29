package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.cards.common.RitualJar;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class RitualJarAction extends AbstractGameAction {
    public RitualJarAction() {
        setValues(AbstractDungeon.player, AbstractDungeon.player);
        amount = 3;
    }

    @Override
    public void update() {
        isDone = true;
        AbstractPlayer p = AbstractDungeon.player;
        Predicate<AbstractCard> f = c -> c.cardID.equals(RitualJar.ID);
        Consumer<AbstractCard> incrementMagic = c -> {
            c.baseMagicNumber += amount;
            c.magicNumber += amount;
        };

        p.hand.group.stream().filter(f).forEach(incrementMagic);
        p.discardPile.group.stream().filter(f).forEach(incrementMagic);
        p.drawPile.group.stream().filter(f).forEach(incrementMagic);
    }
}
