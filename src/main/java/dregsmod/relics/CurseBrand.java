package dregsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import dregsmod.DregsMod;
import dregsmod.actions.CardAwokenAction;
import dregsmod.util.TextureLoader;

import static dregsmod.DregsMod.makeRelicOutlinePath;
import static dregsmod.DregsMod.makeRelicPath;

public class CurseBrand extends CustomRelic {

    public static final String ID = DregsMod.makeID(CurseBrand.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("CurseBrand.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("CurseBrand.png"));

    public CurseBrand() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        tips.add(new PowerTip(DESCRIPTIONS[1], DESCRIPTIONS[2]));
        initializeTips();
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if (drawnCard.type == AbstractCard.CardType.CURSE) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(
                    new CardAwokenAction()
            );
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
