package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import dregsmod.DregsMod;

public class MightPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = DregsMod.makeID(MightPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean dontRemoveThisTurn = false;

    public MightPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        loadRegion("flex");
        updateDescription();

        if (owner.hasPower(WeakPower.POWER_ID)) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, WeakPower.POWER_ID));
        }
    }

    public MightPower(AbstractCreature owner, int amount, boolean dontRemoveThisTurn) {
        this(owner, amount);
        this.dontRemoveThisTurn = dontRemoveThisTurn;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (dontRemoveThisTurn) {
            dontRemoveThisTurn = false;
        } else if (isPlayer) {
            if (amount <= 1) {
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            } else {
                addToBot(new ReducePowerAction(owner, owner, this, 1));
            }
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (target == owner && power.ID.equals(WeakPower.POWER_ID)) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * 1.25f;
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + (amount > 1 ? DESCRIPTIONS[2] : DESCRIPTIONS[1]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new MightPower(owner, amount);
    }
}
