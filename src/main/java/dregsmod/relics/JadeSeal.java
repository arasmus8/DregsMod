package dregsmod.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import dregsmod.DregsMod;
import dregsmod.util.TextureLoader;

import static dregsmod.DregsMod.makeRelicOutlinePath;
import static dregsmod.DregsMod.makeRelicPath;

public class JadeSeal extends CustomRelic implements TriggerOnSealedRelic {

    public static final String ID = DregsMod.makeID(JadeSeal.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("JadeSeal.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("JadeSeal.png"));

    public JadeSeal() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.HEAVY);
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        if (BaseMod.getKeywordProper("dregsmod:seal") != null) {
            tips.add(new PowerTip(BaseMod.getKeywordTitle("dregsmod:seal"),
                    BaseMod.getKeywordDescription("dregsmod:seal")));
        }
        initializeTips();
    }

    @Override
    public void triggerOnSealed(AbstractCard card) {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new GainBlockAction(AbstractDungeon.player, 3, true));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
