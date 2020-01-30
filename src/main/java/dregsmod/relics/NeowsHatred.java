package dregsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import dregsmod.CardList;
import dregsmod.DregsMod;
import dregsmod.util.TextureLoader;

import static dregsmod.DregsMod.makeRelicOutlinePath;
import static dregsmod.DregsMod.makeRelicPath;

public class NeowsHatred extends CustomRelic {

    public static final String ID = DregsMod.makeID(NeowsHatred.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("NeowsHatred.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("NeowsHatred.png"));

    public NeowsHatred() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        if(room instanceof RestRoom) {
            flash();
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(CardList.getRandomCleanseCurse(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
