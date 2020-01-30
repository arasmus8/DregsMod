package dregsmod.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.PowerTip;
import dregsmod.DregsMod;
import dregsmod.util.TextureLoader;

import static dregsmod.DregsMod.makeRelicOutlinePath;
import static dregsmod.DregsMod.makeRelicPath;

public class CursedLocket extends CustomRelic {

    public static final String ID = DregsMod.makeID(CursedLocket.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("CursedLocket.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("CursedLocket.png"));

    public CursedLocket() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.FLAT);
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        tips.add(new PowerTip(BaseMod.getKeywordProper("dregsmod:cursed"), BaseMod.getKeywordDescription("dregsmod:cursed")));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
