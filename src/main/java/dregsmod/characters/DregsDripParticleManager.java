package dregsmod.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;

public class DregsDripParticleManager {
    private static ParticleEffect dripEffect;
    private boolean active;
    private boolean initialized;

    public DregsDripParticleManager() {
        dripEffect = new ParticleEffect();
        dripEffect.load(Gdx.files.internal("particleSettings/DripParticle"), Gdx.files.internal("particleSettings"));
        active = false;

    }

    public void newDripParticle() {
        if (!active) {
            active = true;
            initialized = false;
        }
    }

    public void update() {
        if (!active) {
            if (MathUtils.randomBoolean(0.9f)) {
                newDripParticle();
            }
        }
    }

    private float xCoordinate(int coordinateIndex) {
        switch (coordinateIndex) {
            case 0:
                return 145f;
            case 1:
                return 120f;
            case 2:
                return 38f;
            case 3:
                return 20f;
            default:
                return 0f;
        }
    }

    private float yCoordinate(int coordinateIndex) {
        switch (coordinateIndex) {
            case 0:
                return 84f;
            case 1:
                return 185f;
            case 2:
                return 83f;
            case 3:
                return 64f;
            default:
                return 0f;
        }
    }

    public void render(SpriteBatch sb, float x, float y, boolean flipped) {
        if (active) {
            if (!initialized) {
                int coordinateIndex = MathUtils.random(3);
                float xPos = flipped ? x - xCoordinate(coordinateIndex) * Settings.scale : x + xCoordinate(coordinateIndex) * Settings.scale;
                float yPos = y + yCoordinate(coordinateIndex) * Settings.scale;
                dripEffect.setPosition(xPos, yPos);
                dripEffect.start();
                initialized = true;
            }
            dripEffect.draw(sb, Gdx.graphics.getDeltaTime());
            if (dripEffect.isComplete()) {
                active = false;
            }
        }

    }
}
