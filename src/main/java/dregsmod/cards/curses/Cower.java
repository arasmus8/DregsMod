package dregsmod.cards.curses;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCleansingCurse;
import dregsmod.cards.TriggerOnMonsterDeathCard;

public class Cower extends AbstractCleansingCurse implements TriggerOnMonsterDeathCard {
    public static final String ID = DregsMod.makeID(Cower.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = -2;
    private static final int CLEANSE_AMOUNT = 5;

    public Cower() {
        super(ID, COST, TARGET, CLEANSE_AMOUNT);
        selfRetain = true;
    }

    @Override
    public void onRetained() {
        for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDying && !m.isEscaping && !m.halfDead) {
                addToBot(new HealAction(m, m, 5));
            }
        }
    }

    @Override
    public void triggerOnMonsterDeath(AbstractMonster monster) {
        if(cleanseAmount <= misc+1) {
            cleanseBy(1);
        } else {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.uuid.equals(uuid)) {
                    c.misc += 1;
                    c.baseMagicNumber = c.magicNumber = ((Cower) c).cleanseAmount - c.misc;
                    c.applyPowers();
                    c.initializeDescription();
                }
            }
            for (AbstractCard c : GetAllInBattleInstances.get(uuid)) {
                c.misc += 1;
                c.baseMagicNumber = c.magicNumber = ((Cower) c).cleanseAmount - c.misc;
                c.applyPowers();
                c.initializeDescription();
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

    @Override
    public void upgrade() {
    }
}