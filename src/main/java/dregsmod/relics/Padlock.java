package dregsmod.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import dregsmod.DregsMod;
import dregsmod.actions.SealAndPerformAction;
import dregsmod.util.TextureLoader;

import static dregsmod.DregsMod.makeRelicOutlinePath;
import static dregsmod.DregsMod.makeRelicPath;

public class Padlock extends CustomRelic {

    public static final String ID = DregsMod.makeID(Padlock.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Padlock.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Padlock.png"));

    public Padlock() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK);
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        if (BaseMod.getKeywordProper("dregsmod:seal") != null) {
            tips.add(new PowerTip(BaseMod.getKeywordTitle("dregsmod:seal"),
                    BaseMod.getKeywordDescription("dregsmod:seal")));
        }
        initializeTips();
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToTop(new SealAndPerformAction(5, false, AbstractDungeon.player.drawPile, null, null));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
