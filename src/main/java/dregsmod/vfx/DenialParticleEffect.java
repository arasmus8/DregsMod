package dregsmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class DenialParticleEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vX;
    private float vY;
    private float dur_div2;
    private TextureAtlas.AtlasRegion img;

    public DenialParticleEffect() {
        img = ImageMaster.WOBBLY_LINE;
        duration = MathUtils.random(1.3F, 1.8F);
        scale = MathUtils.random(0.2F, 0.3F) * Settings.scale;
        dur_div2 = duration / 2.0F;
        color = new Color(MathUtils.random(0.0F, 0.55F), 0.9F, MathUtils.random(0.4F, 0.6F), 0.0F);
        x = AbstractDungeon.player.hb.cX + MathUtils.random(-AbstractDungeon.player.hb.width / 2.0F - 30.0F * Settings.scale, AbstractDungeon.player.hb.width / 2.0F + 30.0F * Settings.scale);
        y = AbstractDungeon.player.hb.cY + MathUtils.random(-AbstractDungeon.player.hb.height / 2.0F - -10.0F * Settings.scale, AbstractDungeon.player.hb.height / 2.0F - 10.0F * Settings.scale);
        vX = MathUtils.random(10.0F, 30.0F);
        vY = MathUtils.random(-50.0F, -70.0F);
        if (x < AbstractDungeon.player.hb.cX) {
            vX *= -1;
        }
        x -= (float) img.packedWidth / 2.0F;
        y -= (float) img.packedHeight / 2.0F;
        renderBehind = MathUtils.randomBoolean(0.5F);
        rotation = MathUtils.random(-20F, 20F);
    }

    public void update() {
        if (duration > dur_div2) {
            color.a = Interpolation.fade.apply(1.0F, 0.0F, (duration - dur_div2) / dur_div2);
        } else {
            color.a = Interpolation.fade.apply(0.0F, 1.0F, duration / dur_div2);
        }

        x += Gdx.graphics.getDeltaTime() * vX * Settings.scale;
        y += Gdx.graphics.getDeltaTime() * vY * Settings.scale;
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.0F) {
            isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.setBlendFunction(770, 1);
        sb.draw(img, x, y, (float) img.packedWidth / 2.0F, (float) img.packedHeight / 2.0F, (float) img.packedWidth, (float) img.packedHeight, scale * 0.8F, scale * 0.8F, rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
