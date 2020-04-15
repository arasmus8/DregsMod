package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import dregsmod.DregsMod;
import dregsmod.util.TextureLoader;

public class SturdinessPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = DregsMod.makeID(SturdinessPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Texture texture = TextureLoader.getTexture(DregsMod.makePowerPath("sturdiness.png"));

    public SturdinessPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        isTurnBased = true;

        region48 = new TextureAtlas.AtlasRegion(texture, 0, 0, 32, 32);
        region128 = new TextureAtlas.AtlasRegion(texture, 0, 0, 32, 32);
        updateDescription();

        if (owner.hasPower(FrailPower.POWER_ID)) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, FrailPower.POWER_ID));
        }
    }

    @Override
    public void atStartOfTurn() {
        if (amount <= 1) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (target == owner && power.ID.equals(FrailPower.POWER_ID)) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount * 1.25f;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + (amount > 1 ? DESCRIPTIONS[2] : DESCRIPTIONS[1]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new SturdinessPower(owner, amount);
    }
}
