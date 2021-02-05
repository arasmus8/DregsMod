package dregsmod.cards.curses;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCleansingCurse;

public class Inquisition extends AbstractCleansingCurse {
    public static final String ID = DregsMod.makeID(Inquisition.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = -2;
    private static final int CLEANSE_AMOUNT = 5;

    public Inquisition() {
        super(ID, COST, TARGET, CLEANSE_AMOUNT);
        selfRetain = true;
    }

    @Override
    public void triggerOnExhaust() {
        cleanseBy(1);
    }

    @Override
    public void atTurnStart() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hand.contains(this)) {
            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(this.makeSameInstanceOf(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
            addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, 1, false), 1));
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                addToBot(new ApplyPowerAction(m, m, new VulnerablePower(m, 1, false), 1));
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // actions when card is used.
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
    }
}