package dregsmod.characters;

import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dregsmod.util.TextureLoader;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DregsAnimation extends AbstractAnimation {

    private boolean flipH = false;
    private boolean flipV = false;
    private static Texture dregsImg;
    private static TextureAtlas.AtlasRegion dripImg;
    private DregsDamageParticleManager damageParticleManager;
    private DregsGemManager gemManager;

    public DregsAnimation() {
        dripImg = ImageMaster.TORCH_FIRE_1;
        dregsImg = TextureLoader.getTexture("dregsmodResources/images/char/dregs.png");

        damageParticleManager = new DregsDamageParticleManager();
        gemManager = new DregsGemManager();
    }

    public void onDamage(int amount) {
        damageParticleManager.newDamageParticle(amount);
    }

    public void analyzeDeck() {
        CardGroup deck = AbstractDungeon.player.masterDeck;
        ArrayList<CardColor> colors = deck.group.stream()
                .filter(card -> card.color != Dregs.Enums.COLOR_BLACK)
                .limit(30)
                .map(card -> card.color)
                .collect(Collectors.toCollection(ArrayList::new));
        gemManager.onAnalyze(colors);
    }

    public void update() {
        gemManager.update();
    }

    @Override
    public void setFlip(boolean horizontal, boolean vertical) {
        flipH = horizontal;
        flipV = vertical;
    }

    @Override
    public void renderSprite(SpriteBatch sb, float x, float y) {
        // sb.setColor(new Color(0.1f, 0f, 0.1f, 1.0f));
        sb.draw(dregsImg, x - 75f, y, 0f, 0f, 150f, 250f, 1f, 1f, 0f, 0, 0, 150, 250, flipH, flipV);
        damageParticleManager.render(sb, x, y, flipH);
        gemManager.render(sb, x - 75f, y, flipH);
    }

    @Override
    public Type type() {
        return Type.SPRITE;
    }
}
