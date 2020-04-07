package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dregsmod.DregsMod;

public class LostPotentialPower extends AbstractPower implements CloneablePowerInterface, TriggerOnSealedPower {

    public static final String POWER_ID = DregsMod.makeID(LostPotentialPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static boolean upgraded;

    public LostPotentialPower(AbstractPlayer owner, int amount, boolean isUpgraded) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        if (owner.hasPower(POWER_ID)) {
            upgraded = upgraded || isUpgraded;
        } else {
            upgraded = isUpgraded;
        }

        loadRegion("static_discharge");
        updateDescription();
    }

    @Override
    public void triggerOnSealed(AbstractCard card) {
        if (card.type == AbstractCard.CardType.CURSE) {
            flash();
            if (upgraded) {
                addToBot(new IncreaseMaxOrbAction(1));
            }
            for (int i = 0; i < amount; i++) {
                addToBot(new ChannelAction(new Lightning()));
            }
        }
    }

    @Override
    public void updateDescription() {
        if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }
        if (upgraded) {
            description += DESCRIPTIONS[3];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new LostPotentialPower((AbstractPlayer) owner, amount, upgraded);
    }
}
