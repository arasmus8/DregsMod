package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dregsmod.DregsMod;

public class IncantationPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = DregsMod.makeID(IncantationPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean usedThisTurn;

    public IncantationPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        usedThisTurn = true;

        loadRegion("hymn");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        usedThisTurn = false;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (!usedThisTurn && damageAmount > 0 && target != owner && info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            usedThisTurn = true;
            addToTop(new ApplyPowerAction(target, owner, new CursedPower(target, amount), amount));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new IncantationPower(owner, amount);
    }
}
