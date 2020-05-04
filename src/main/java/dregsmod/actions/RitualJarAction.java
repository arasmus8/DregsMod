package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.cards.common.RitualJar;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class RitualJarAction extends AbstractGameAction {
    private final RitualJar card;

    public RitualJarAction(RitualJar card) {
        setValues(AbstractDungeon.player, AbstractDungeon.player);
        this.card = card;
        amount = 3;
        duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_MED) {
            isDone = true;
            AbstractPlayer p = AbstractDungeon.player;
            Predicate<AbstractCard> f = c -> c.cardID.equals(RitualJar.ID);
            Consumer<AbstractCard> incrementMagic = c -> {
                c.baseMagicNumber += amount;
                c.magicNumber += amount;
            };

            incrementMagic.accept(card);
            p.hand.group.stream().filter(f).forEach(incrementMagic);
            p.discardPile.group.stream().filter(f).forEach(incrementMagic);
            p.drawPile.group.stream().filter(f).forEach(incrementMagic);
        }
    }
}
