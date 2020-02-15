package dregsmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.AdditiveSlashImpactEffect;

public class CursedNeedleEffect extends AbstractGameEffect {
    private static TextureAtlas.AtlasRegion img;
    private float x;
    private float y;
    private float sX;
    private float sY;
    private float tX;
    private float tY;
    private float targetAngle;
    private float startingAngle;
    private float targetScale;
    private boolean shownSlash = false;

    public CursedNeedleEffect(float x, float y) {
        if (img == null) {
            img = ImageMaster.vfxAtlas.findRegion("combat/stake");
        }

        float randomAngle = MathUtils.random(1.374447f, 1.76715f);
        this.x = MathUtils.cos(randomAngle) * MathUtils.random(250.0F, 350.0F) * Settings.scale + x;
        this.y = MathUtils.sin(randomAngle) * MathUtils.random(250.0F, 300.0F) * Settings.scale + y;
        this.duration = 1.0F;
        this.scale = 0.01F;
        this.targetScale = MathUtils.random(1.0F, 1.9F);
        this.targetAngle = MathUtils.atan2(y - this.y, x - this.x) * 57.295776F + 90.0F;
        this.startingAngle = this.targetAngle + MathUtils.random(-15.0F, 15.0F);
        this.rotation = this.startingAngle;
        this.x -= (float) (img.packedWidth / 2);
        this.y -= (float) (img.packedHeight / 2);
        this.sX = this.x;
        this.sY = this.y;
        this.tX = x - (float) (img.packedWidth / 2);
        this.tY = y - (float) (img.packedHeight / 2);
        this.color = new Color(MathUtils.random(0.5F, 1.0F), MathUtils.random(0.0F, 0.4F), MathUtils.random(0.5F, 1.0F), 0.0F);
    }

    public void update() {
        this.rotation = Interpolation.elasticIn.apply(this.targetAngle, this.startingAngle, this.duration);
        if (this.duration > 0.5F) {
            this.scale = Interpolation.elasticIn.apply(this.targetScale, this.targetScale * 10.0F, (this.duration - 0.5F) * 2.0F) * Settings.scale;
            this.color.a = Interpolation.fade.apply(0.6F, 0.0F, (this.duration - 0.5F) * 2.0F);
        } else {
            this.x = Interpolation.exp10Out.apply(this.tX, this.sX, this.duration * 2.0F);
            this.y = Interpolation.exp10Out.apply(this.tY, this.sY, this.duration * 2.0F);
        }

        if (this.duration < 0.05F && !this.shownSlash) {
            AbstractDungeon.effectsQueue.add(new AdditiveSlashImpactEffect(this.tX + (float) img.packedWidth / 2.0F, this.tY + (float) img.packedHeight / 2.0F, this.color.cpy()));
            this.shownSlash = true;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, MathUtils.randomBoolean());
            CardCrawlGame.sound.play("ATTACK_FAST", 0.2F);
        }

    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(img, this.x, this.y, (float) img.packedWidth / 2.0F, (float) img.packedHeight / 2.0F, (float) img.packedWidth, (float) img.packedHeight, this.scale * MathUtils.random(1.0F, 1.2F), this.scale * MathUtils.random(1.0F, 1.2F), this.rotation);
        sb.draw(img, this.x, this.y, (float) img.packedWidth / 2.0F, (float) img.packedHeight / 2.0F, (float) img.packedWidth, (float) img.packedHeight, this.scale * MathUtils.random(0.9F, 1.1F), this.scale * MathUtils.random(0.9F, 1.1F), this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
