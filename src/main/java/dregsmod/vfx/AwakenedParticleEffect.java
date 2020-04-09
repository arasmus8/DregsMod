package dregsmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class AwakenedParticleEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vY;
    private float halfDuration;
    private TextureAtlas.AtlasRegion img;

    public AwakenedParticleEffect(AbstractCard card) {
        img = ImageMaster.EYE_ANIM_0;
        scale = MathUtils.random(0.8F, 1.2F);
        startingDuration = scale + 0.2F;
        duration = startingDuration;
        scale *= Settings.scale;
        halfDuration = duration / 2.0F;
        color = new Color(MathUtils.random(0.02F, 0.086F), MathUtils.random(0.0F, 0.008F), MathUtils.random(0.047F, 0.259F), 0.0F);
        float xOff = MathUtils.random(-20f, 20f) * Settings.scale;
        if (MathUtils.randomBoolean()) {
            x = card.hb.cX - card.hb.width / 2.0F - xOff;
        } else {
            x = card.hb.cX + card.hb.width / 2.0F + xOff;
        }
        y = card.hb.cY + MathUtils.random(-card.hb.height / 2.0F + 10.0F * Settings.scale, card.hb.height / 2.0F - 20.0F * Settings.scale);
        renderBehind = MathUtils.randomBoolean();
        rotation = MathUtils.random(12.0F, 6.0F);
        if (x > card.hb.cX) {
            rotation = -rotation;
        }

        x -= (float) img.packedWidth / 2.0F;
        y -= (float) img.packedHeight / 2.0F;
    }

    public void update() {
        if (duration > halfDuration) {
            color.a = Interpolation.fade.apply(1.0F, 0.0F, (duration - halfDuration) / halfDuration);
        } else {
            color.a = Interpolation.fade.apply(0.0F, 1.0F, duration / halfDuration);
        }

        if (duration > startingDuration * 0.85F) {
            vY = 12.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_0;
        } else if (duration > startingDuration * 0.8F) {
            vY = 8.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_1;
        } else if (duration > startingDuration * 0.75F) {
            vY = 4.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_2;
        } else if (duration > startingDuration * 0.7F) {
            vY = 3.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_3;
        } else if (duration > startingDuration * 0.65F) {
            img = ImageMaster.EYE_ANIM_4;
        } else if (duration > startingDuration * 0.6F) {
            img = ImageMaster.EYE_ANIM_5;
        } else if (duration > startingDuration * 0.55F) {
            img = ImageMaster.EYE_ANIM_6;
        } else if (duration > startingDuration * 0.38F) {
            img = ImageMaster.EYE_ANIM_5;
        } else if (duration > startingDuration * 0.3F) {
            img = ImageMaster.EYE_ANIM_4;
        } else if (duration > startingDuration * 0.25F) {
            vY = 3.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_3;
        } else if (duration > startingDuration * 0.2F) {
            vY = 4.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_2;
        } else if (duration > startingDuration * 0.15F) {
            vY = 8.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_1;
        } else {
            vY = 12.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_0;
        }

        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.0F) {
            isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(color);
        //sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.draw(img,
                x,
                y + vY,
                (float) img.packedWidth / 2.0F,
                (float) img.packedHeight / 2.0F,
                (float) img.packedWidth,
                (float) img.packedHeight,
                scale * 0.6f,
                scale * 0.6f,
                rotation);
        //sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void dispose() {
    }
}
