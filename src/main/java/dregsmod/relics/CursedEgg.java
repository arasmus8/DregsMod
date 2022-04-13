package dregsmod.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.PowerTip;
import dregsmod.DregsMod;
import dregsmod.util.TextureLoader;

import static dregsmod.DregsMod.makeRelicOutlinePath;
import static dregsmod.DregsMod.makeRelicPath;
import static dregsmod.cards.DregsCardTags.CLEANSE_REWARD;

public class CursedEgg extends CustomRelic {

    public static final String ID = DregsMod.makeID(CursedEgg.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("CursedEgg.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("CursedEgg.png"));

    public CursedEgg() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        if (BaseMod.getKeywordProper("dregsmod:cleanse") != null) {
            tips.add(new PowerTip(BaseMod.getKeywordTitle("dregsmod:cleanse"),
                    BaseMod.getKeywordDescription("dregsmod:cleanse")));
        }
        initializeTips();
    }

    @Override
    public void onPreviewObtainCard(AbstractCard c) {
        onObtainCard(c);
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        if (c.hasTag(CLEANSE_REWARD)) {
            c.upgrade();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
