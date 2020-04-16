package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import dregsmod.DregsMod;
import dregsmod.util.TextureLoader;

public class VivificationPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = DregsMod.makeID(VivificationPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Texture texture = TextureLoader.getTexture(DregsMod.makePowerPath("vivification.png"));
    private final boolean upgraded;
    private boolean usedThisCombat;

    public VivificationPower(AbstractPlayer owner, boolean upgraded) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.upgraded = upgraded;
        this.usedThisCombat = false;

        region48 = new TextureAtlas.AtlasRegion(texture, 0, 0, 32, 32);
        region128 = new TextureAtlas.AtlasRegion(texture, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (!usedThisCombat && damageAmount >= owner.currentHealth) {
            flash();
            usedThisCombat = true;
            updateDescription();
            AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(CardLibrary.getCurse(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
            int goalHealth = upgraded ? owner.maxHealth / 2 : owner.maxHealth / 3;
            if (owner.currentHealth > goalHealth) {
                return owner.currentHealth - goalHealth;
            }
            owner.heal(goalHealth - owner.currentHealth, true);
            return 0;
        }

        return damageAmount;
    }

    @Override
    public void updateDescription() {
        if (usedThisCombat) {
            description = DESCRIPTIONS[2];
        } else if (upgraded) {
            description = DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new VivificationPower((AbstractPlayer) owner, upgraded);
    }
}
