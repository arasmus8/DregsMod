package dregsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import dregsmod.DregsMod;
import dregsmod.util.TextureLoader;

import static dregsmod.DregsMod.makeRelicOutlinePath;
import static dregsmod.DregsMod.makeRelicPath;

public class StickyBarb extends CustomRelic implements OnReceivePowerRelic {

    public static final String ID = DregsMod.makeID(StickyBarb.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("StickyBarb.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("StickyBarb.png"));

    public StickyBarb() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.SOLID);
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature) {
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature source, int stackAmount) {
        if (power.ID.equals(VulnerablePower.POWER_ID)) {
            boolean isSourceMonster = !source.isPlayer;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(m, source, new VulnerablePower(m, 1, isSourceMonster), 1));
                }
            }
            flash();
        }
        return stackAmount;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
