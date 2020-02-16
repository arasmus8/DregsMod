package dregsmod.characters;

import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dregsmod.util.TextureLoader;

import java.util.Arrays;

public class DregsAnimation extends AbstractAnimation {

    private boolean flipH = false;
    private boolean flipV = false;
    private static Texture dregsImg;
    private static TextureAtlas.AtlasRegion circleImg;
    private static TextureAtlas.AtlasRegion dripImg;
    private static ParticleEffect damageEffect;
    private static ParticleEffect damageEffectSm;
    private static ParticleEffect damageEffectLg;
    private static float damageDuration = 0f;
    private static DamageSize damageSize;

    private static CardColor[] card_colors;

    public DregsAnimation() {
        circleImg = ImageMaster.WHITE_RING;
        dripImg = ImageMaster.TORCH_FIRE_1;
        dregsImg = TextureLoader.getTexture("dregsmodResources/images/char/dregs.png");

        damageEffect = new ParticleEffect();
        damageEffect.load(Gdx.files.internal("particleSettings/DamageSpray"), Gdx.files.internal("particleSettings"));
        damageEffectSm = new ParticleEffect();
        damageEffectSm.load(Gdx.files.internal("particleSettings/DamageSpraySm"), Gdx.files.internal("particleSettings"));
        damageEffectLg = new ParticleEffect();
        damageEffectLg.load(Gdx.files.internal("particleSettings/DamageSprayLg"), Gdx.files.internal("particleSettings"));

        card_colors = new CardColor[30];
        Arrays.fill(card_colors, Dregs.Enums.COLOR_BLACK);
    }

    public void onDamage(int amount) {
        if (damageDuration <= 0f) {
            damageDuration = 2.0f;
            int maxHp = AbstractDungeon.player.maxHealth;
            if (amount < maxHp / 10) {
                damageSize = DamageSize.SMALL;
            } else if (amount > maxHp / 4) {
                damageSize = DamageSize.LARGE;
            } else {
                damageSize = DamageSize.NORMAL;
            }
        }
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
        if (damageDuration > 0f) {
            if (damageDuration == 2.0f) {
                switch (damageSize) {
                    case LARGE:
                        damageEffectLg.setPosition(x - 40f * Settings.scale, MathUtils.random(80, 180) * Settings.scale + y);
                        damageEffectLg.start();
                        break;
                    case NORMAL:
                        damageEffect.setPosition(x - 40f * Settings.scale, MathUtils.random(80, 180) * Settings.scale + y);
                        damageEffect.start();
                        break;
                    case SMALL:
                        damageEffectSm.setPosition(x - 40f * Settings.scale, MathUtils.random(80, 180) * Settings.scale + y);
                        damageEffectSm.start();
                        break;
                }
            }
            damageDuration -= Gdx.graphics.getDeltaTime();
            switch (damageSize) {
                case LARGE:
                    damageEffectLg.draw(sb, Gdx.graphics.getDeltaTime());
                    break;
                case NORMAL:
                    damageEffect.draw(sb, Gdx.graphics.getDeltaTime());
                    break;
                case SMALL:
                    damageEffectSm.draw(sb, Gdx.graphics.getDeltaTime());
                    break;
            }
        }
    }

    @Override
    public Type type() {
        return Type.SPRITE;
    }

    private enum DamageSize {
        SMALL,
        NORMAL,
        LARGE;

        DamageSize() {
        }
    }
}
