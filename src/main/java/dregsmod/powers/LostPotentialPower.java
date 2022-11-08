package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dregsmod.DregsMod;

public class LostPotentialPower extends AbstractDregsPower implements CloneablePowerInterface, TriggerOnSealedPower {

    public static final String POWER_ID = DregsMod.makeID(LostPotentialPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public LostPotentialPower(AbstractPlayer owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        loadRegion("lostpotential");
        updateDescription();
    }

    @Override
    public void triggerOnSealed(AbstractCard card) {
        flash();
        for (int i = 0; i < amount; i++) {
            addToBot(new ChannelAction(new Lightning()));
        }
    }

    @Override
    public void updateDescription() {
        if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new LostPotentialPower((AbstractPlayer) owner, amount);
    }
}
