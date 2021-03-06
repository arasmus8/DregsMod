package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dregsmod.DregsMod;
import dregsmod.actions.AsylumChannelAction;
import dregsmod.actions.SealAndPerformAction;

import java.util.logging.Logger;

public class AsylumPower extends AbstractDregsPower implements CloneablePowerInterface {
    private static final Logger logger = Logger.getLogger(AsylumPower.class.getName());

    public static final String POWER_ID = DregsMod.makeID(AsylumPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AsylumPower(AbstractPlayer owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.priority = 3;

        loadRegion("asylum");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS [3];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + DESCRIPTIONS [3];
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        logger.info("start of turn");
        logger.info("adding action");
        flash();
        addToBot(new SealAndPerformAction(amount, new AsylumChannelAction()));
    }

    @Override
    public AbstractPower makeCopy() {
        return new AsylumPower((AbstractPlayer) owner, amount);
    }
}
