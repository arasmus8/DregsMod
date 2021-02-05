package dregsmod.cards.curses;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCleansingCurse;

import java.util.function.Function;

public class Greed extends AbstractCleansingCurse {
    public static final String ID = DregsMod.makeID(Greed.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = -2;
    private static final int CLEANSE_AMOUNT = 8;

    public Greed() {
        super(ID, COST, TARGET, CLEANSE_AMOUNT);
    }

    @Override
    public void triggerWhenDrawn() {
        Function<AbstractCleansingCurse, AbstractGameAction> cleanse = card -> new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                AbstractDungeon.player.loseGold(5);
                card.cleanseBy(1);
            }
        };
        addToBot(cleanse.apply(this));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // actions when card is used.
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
    }
}