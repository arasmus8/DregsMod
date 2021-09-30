package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dregsmod.DregsMod;

public class ScapegoatPower extends AbstractDregsTwoAmountPower implements TriggerOnSealedPower, CloneablePowerInterface {

    public static final String POWER_ID = DregsMod.makeID(ScapegoatPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ScapegoatPower(AbstractCreature owner, int amount, int amount2) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = amount2;

        loadRegion("scapegoat");
        updateDescription();
    }

    @Override
    public void triggerOnSealed(AbstractCard card) {
        amount2 += amount;
        AbstractDungeon.player.hand.group.forEach(AbstractCard::applyPowers);
        updateDescription();
    }

    @Override
    public void onExhaust(AbstractCard card) {
        amount2 += amount;
        AbstractDungeon.player.hand.group.forEach(AbstractCard::applyPowers);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ScapegoatPower(owner, amount, amount2);
    }
}
