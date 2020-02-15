package dregsmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SharpShadowsEffect extends AbstractGameEffect {
    private static TextureAtlas.AtlasRegion img;
    private float x;
    private float y;
    private float t;
    private float targetScale;
    private boolean playedSound;

    public SharpShadowsEffect(float x, float y) {
        if (img == null) {
            img = ImageMaster.CONE_4;
        }

        duration = 0.2f;
        t=0f;
        targetScale = MathUtils.random(0.2f, 0.4f);
        rotation = MathUtils.random(265f, 275f);
        this.color = new Color(MathUtils.random(0.2F, 0.3F), 0f, MathUtils.random(0.2F, 0.3F), 0.8F);
        this.x = x + MathUtils.random(-60, 60) * Settings.scale - (float) (img.packedWidth);
        this.y = y + MathUtils.random(-5, 5) * Settings.scale - (float) (img.packedHeight / 2);
        playedSound = false;
    }

    @Override
    public void update() {
        if (t < duration) {
            scale = Interpolation.exp10Out.apply(0.1f, targetScale, t / duration) * Settings.scale;
        } else {
            scale = targetScale;
        }
        t += Gdx.graphics.getDeltaTime();
        if(t >= duration * 2) {
            isDone = true;
        } else if (t > duration && !playedSound) {
            playedSound = true;
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, MathUtils.randomBoolean());
            CardCrawlGame.sound.play("ATTACK_FAST", 0.2F);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(img, this.x, this.y, (float) img.packedWidth, (float) img.packedHeight / 2.0F, (float) img.packedWidth, (float) img.packedHeight, this.scale, this.scale, this.rotation);
        sb.setBlendFunction(770, 1);
        sb.draw(img, this.x, this.y, (float) img.packedWidth, (float) img.packedHeight / 2.0F, (float) img.packedWidth, (float) img.packedHeight, this.scale * 1.2f, this.scale * 1.02f, this.rotation);
        sb.setBlendFunction(770, 771);
        // sb.draw(img, this.x, this.y, (float) img.packedWidth / 2.0F, (float) img.packedHeight / 2.0F, (float) img.packedWidth, (float) img.packedHeight, this.scale * MathUtils.random(1.0F, 1.2F), this.scale * MathUtils.random(1.0F, 1.2F), this.rotation);
        // sb.draw(img, this.x, this.y, (float) img.packedWidth / 2.0F, (float) img.packedHeight / 2.0F, (float) img.packedWidth, (float) img.packedHeight, this.scale * MathUtils.random(0.9F, 1.1F), this.scale * MathUtils.random(0.9F, 1.1F), this.rotation);
    }

    @Override
    public void dispose() {

    }
}
