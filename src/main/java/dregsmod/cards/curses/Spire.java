package dregsmod.cards.curses;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCleansingCurse;

public class Spire extends AbstractCleansingCurse {
    public static final String ID = DregsMod.makeID(Spire.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = -2;
    private static final int CLEANSE_AMOUNT = 50;

    public Spire() {
        super(ID, COST, TARGET, CLEANSE_AMOUNT);
        isInnate = true;
        selfRetain = true;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        cleanseBy(1);
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