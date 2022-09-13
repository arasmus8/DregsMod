package dregsmod.cards.curses;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCleansingCurse;

public class Dread extends AbstractCleansingCurse {
    public static final String ID = DregsMod.makeID(Dread.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = 1;
    private static final int CLEANSE_AMOUNT = 7;

    private boolean suspendDamage = false;

    public Dread() {
        super(ID, COST, TARGET, CLEANSE_AMOUNT);
        selfRetain = true;
    }

    @Override
    public void tookDamage() {
        if(suspendDamage) {
            suspendDamage = false;
        } else if(AbstractDungeon.player.hand.contains(this)) {
            AbstractDungeon.player.hand.group.stream()
                    .filter(c -> c instanceof Dread)
                    .map(c -> (Dread) c)
                    .forEach(c -> c.suspendDamage = true);
            addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, 2, DamageInfo.DamageType.HP_LOSS)));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // actions when card is used.
        cleanseBy(1);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
    }
}