package dregsmod.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityStanceChangeParticle;
import dregsmod.stances.AcceptanceStance;
import dregsmod.stances.DenialStance;

public class CustomStanceChangeParticleGenerator extends AbstractGameEffect {
    private float x;
    private float y;
    private String stanceId;

    public CustomStanceChangeParticleGenerator(float x, float y, String stanceId) {
        this.x = x;
        this.y = y;
        this.stanceId = stanceId;
    }

    public void update() {
        if (stanceId.equals(AcceptanceStance.STANCE_ID)) {
            for (int i = 0; i < 20; ++i) {
                AbstractDungeon.effectsQueue.add(new DivinityStanceChangeParticle(Color.GOLDENROD, x, y));
            }
        } else if (stanceId.equals(DenialStance.STANCE_ID)) {
            for (int i = 0; i < 20; ++i) {
                AbstractDungeon.effectsQueue.add(new DivinityStanceChangeParticle(Color.DARK_GRAY, x, y));
            }
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
