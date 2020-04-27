package dregsmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class AwakenCardEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 1.0F;
    private static final float ANIM_DUR = 0.5F;
    private static final float ANIM_START_AT = 0.75F;
    private static final float SOUND_AT = 0.25F;
    private final AbstractCard c;
    private static final float PADDING;
    private boolean soundPlayed;
    private TextureAtlas.AtlasRegion img;

    private static int count = 0;

    public AwakenCardEffect(AbstractCard card) {
        c = card;
        startingDuration = duration = EFFECT_DUR;
        c.drawScale = 0.01f;
        c.targetDrawScale = 1.0f;
        c.current_x = Settings.WIDTH / 2.0f - (PADDING * count);
        c.current_y = Settings.HEIGHT / 2.0f;
        c.target_x = c.current_x;
        c.target_y = c.current_y;
        c.targetAngle = MathUtils.random(-6f, 6f);
        soundPlayed = false;
        if (count > 5) {
            isDone = true;
        } else {
            ++count;
        }
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.6f) {
            c.fadingOut = true;
        }

        if (duration < SOUND_AT && !soundPlayed) {
            soundPlayed = true;
            CardCrawlGame.sound.play("NECRONOMICON", 0.8F);
        }

        if (duration > startingDuration * 0.65F) {
            img = ImageMaster.EYE_ANIM_0;
        } else if (duration > startingDuration * 0.55F) {
            img = ImageMaster.EYE_ANIM_1;
        } else if (duration > startingDuration * 0.5F) {
            img = ImageMaster.EYE_ANIM_2;
        } else if (duration > startingDuration * 0.45F) {
            img = ImageMaster.EYE_ANIM_3;
        } else if (duration > startingDuration * 0.4F) {
            img = ImageMaster.EYE_ANIM_4;
        } else if (duration > startingDuration * 0.35F) {
            img = ImageMaster.EYE_ANIM_5;
        } else {
            img = ImageMaster.EYE_ANIM_6;
        }

        c.update();

        if (duration < 0.0f) {
            isDone = true;
            --count;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!isDone) {
            c.render(sb);

            float scale = 3.5f * Settings.scale;
            float dT = MathUtils.clamp(duration / EFFECT_DUR, 0f, 1f);

            Color drawColor = new Color(
                    Interpolation.circleIn.apply(dT),
                    0f,
                    Interpolation.exp5In.apply(dT),
                    c.transparency
            );
            sb.setColor(drawColor);
            Vector2 vec = new Vector2(0, 155f);
            vec.scl(c.drawScale * Settings.scale);
            vec.rotate(c.angle);

            sb.draw(img,
                    c.hb.cX + vec.x - img.packedWidth / 2f,
                    c.hb.cY + vec.y - img.packedHeight / 2f,
                    (float) img.packedWidth / 2.0F,
                    (float) img.packedHeight / 2.0F,
                    (float) img.packedWidth,
                    (float) img.packedHeight,
                    c.drawScale * scale,
                    c.drawScale * scale,
                    c.angle);
        }
    }

    @Override
    public void dispose() {
    }

    static {
        PADDING = 50.0f * Settings.scale;
    }
}
