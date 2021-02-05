package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dregsmod.DregsMod;
import dregsmod.orbs.Sludge;

public class ExcessChannelPower extends AbstractDregsPower implements TriggerOnSealedPower, CloneablePowerInterface {

    public static final String POWER_ID = DregsMod.makeID(ExcessChannelPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ExcessChannelPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        loadRegion("excess");
        updateDescription();
    }

    @Override
    public void triggerOnSealed(AbstractCard card) {
        if (card.type == AbstractCard.CardType.CURSE) {
            flash();
            for (int i = 0; i < amount; i++) {
                addToBot(new ChannelAction(new Sludge()));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + (amount > 1 ? DESCRIPTIONS[2] : DESCRIPTIONS[1]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ExcessChannelPower(owner, amount);
    }
}
