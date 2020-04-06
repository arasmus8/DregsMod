package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dregsmod.DregsMod;

public class ScapegoatPower extends AbstractPower implements CloneablePowerInterface, TriggerOnSealedPower {

    public static final String POWER_ID = DregsMod.makeID(ScapegoatPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int magic;

    public ScapegoatPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.magic = amount;
        this.amount = 0;

        loadRegion("corruption");
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.magic += stackAmount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            amount = 0;
        }
    }

    @Override
    public void onDrawOrDiscard() {
        super.onDrawOrDiscard();
    }

    @Override
    public void triggerOnSealed(AbstractCard card) {
        amount += magic;
        updateDescription();
    }

    @Override
    public void onExhaust(AbstractCard card) {
        amount += magic;
        updateDescription();
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * (1f + amount / 100f);
        }
        return damage;
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount * (1f + amount / 100f);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ScapegoatPower(owner, amount);
    }
}
