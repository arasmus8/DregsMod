package dregsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import dregsmod.DregsMod;
import dregsmod.util.TextureLoader;

import java.util.ArrayList;

import static dregsmod.DregsMod.makeRelicOutlinePath;
import static dregsmod.DregsMod.makeRelicPath;

public class EvilEye extends CustomRelic {

    public static final String ID = DregsMod.makeID(EvilEye.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("EvilEye.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("EvilEye.png"));

    public EvilEye() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.FLAT);
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public void onVictory() {
        flash();
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new RelicAboveCreatureAction(p, this));
        ArrayList<AbstractCard> remainingCurses = new ArrayList<>();
        remainingCurses.addAll(p.hand.getCardsOfType(AbstractCard.CardType.CURSE).group);
        remainingCurses.addAll(p.drawPile.getCardsOfType(AbstractCard.CardType.CURSE).group);
        remainingCurses.addAll(p.discardPile.getCardsOfType(AbstractCard.CardType.CURSE).group);
        if (p.currentHealth > 0) {
            p.heal(remainingCurses.size());
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
