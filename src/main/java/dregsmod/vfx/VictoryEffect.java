package dregsmod.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class VictoryEffect extends AbstractGameEffect {
    private final int DESIRED_EYES = 40;
    private final ArrayList<AbstractGameEffect> eyeEffects;

    public VictoryEffect() {
        this.eyeEffects = new ArrayList<>();
        while (eyeEffects.size() < DESIRED_EYES) {
            eyeEffects.add(new VictoryParticleEffect());
        }
    }

    @Override
    public void update() {
        for (AbstractGameEffect effect : eyeEffects) {
            effect.update();
        }
        eyeEffects.removeIf(e -> e.isDone);

        while (eyeEffects.size() < DESIRED_EYES) {
            eyeEffects.add(new VictoryParticleEffect());
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        for (AbstractGameEffect effect : eyeEffects) {
            effect.render(spriteBatch);
        }
    }

    @Override
    public void dispose() {
    }
}
