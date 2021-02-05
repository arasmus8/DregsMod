package dregsmod.cards.curses;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCleansingCurse;

public class Death extends AbstractCleansingCurse {
    public static final String ID = DregsMod.makeID(Death.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int DAMAGE = 100;
    private static final int COST = 0;
    private static final int CLEANSE_AMOUNT = 20;

    public Death() {
        super(ID, COST, TARGET, CLEANSE_AMOUNT);
        damage = DAMAGE;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (c.costForTurn < 0) {
            if (c.costForTurn == -1) {
                this.cleanseBy(EnergyPanel.getCurrentEnergy());
            }
        } else {
            this.cleanseBy(c.costForTurn);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.dontTriggerOnUseCard) {
            this.addToTop(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, DAMAGE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SMASH));
        } else {
            modifyCostForCombat(1);
        }
    }

    public void triggerOnEndOfTurnForPlayingCard() {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
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
