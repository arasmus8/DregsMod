package dregsmod.characters;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.Arrays;

public class DregsEnergyOrb extends CustomEnergyOrb {

    private static final float ORB_SCALE = 0.85F;
    private Texture background;
    private Texture orbVfx;
    private static Color whiteAlpha80 = new Color(1f, 1f, 1f, 0.8f);
    private ParticleEffect[] dripPool;
    private float[] xOffsets;
    private float[] yOffsets;
    private float[] delays;

    public DregsEnergyOrb() {
        super(null, null, null);
        background = ImageMaster.loadImage("dregsmodResources/images/char/dregs/orbmain.png");
        orbVfx = ImageMaster.loadImage("dregsmodResources/images/char/dregs/orbvfx.png");
        dripPool = new ParticleEffect[13];
        xOffsets = new float[]{14f, 112f, 60f, 14f, 83f, 124f, 107f, 72f, 97f, 54f, 26f, 12f, 65f};
        yOffsets = new float[]{20f, 19f, 16f, 65f, 79f, 74f, 29f, 53f, 18f, 72f, 76f, 26f, 18f};
        for (int i = 0; i < xOffsets.length; ++i) {
            xOffsets[i] *= Settings.scale;
            yOffsets[i] *= Settings.scale;
        }
        delays = new float[13];
        Arrays.fill(delays, 0f);
    }

    public Texture getEnergyImage() {
        return this.orbVfx;
    }

    @Override
    public void renderOrb(SpriteBatch sb, boolean enabled, float x, float y) {
        if (enabled) {
            sb.setColor(Color.WHITE);
        } else {
            sb.setColor(whiteAlpha80);
        }
        sb.draw(this.background,
                x - 64.0F,
                y - 64.0F,
                64.0F,
                64.0F,
                128.0F,
                128.0F,
                ORB_SCALE,
                ORB_SCALE,
                0,
                0,
                0,
                128,
                128,
                false,
                false);
        for (int i = 0; i < dripPool.length; ++i) {
            if (dripPool[i] != null && !dripPool[i].isComplete()) {
                dripPool[i].setPosition(x - 64f + xOffsets[i], y - 64f + yOffsets[i]);
                dripPool[i].draw(sb);
            }
        }
    }

    @Override
    public void updateOrb(int energyCount) {
        float dt = Gdx.graphics.getDeltaTime();
        int desiredDrips = MathUtils.clamp(3 + 2 * energyCount, 3, 13);
        for (int i = 0; i < dripPool.length; ++i) {
            if (dripPool[i] == null && desiredDrips > i) {
                dripPool[i] = new ParticleEffect();
                dripPool[i].load(Gdx.files.internal("particleSettings/DripParticle"), Gdx.files.internal("particleSettings"));
                dripPool[i].start();
            }
            if (dripPool[i] != null) {
                dripPool[i].update(dt);
            }
            delays[i] -= dt;
            if (dripPool[i] != null && desiredDrips > i && delays[i] <= 0f) {
                dripPool[i].start();
                delays[i] = MathUtils.random(1.5f, 6f);
            }
        }
    }
}
