package dregsmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.CollectorStakeEffect;

public class BadOmenEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private int count;
    private float stakeTimer = 0.0F;
    private static final int TOTAL_COUNT = 7;

    public BadOmenEffect(float x, float y) {
        this.x = x;
        this.y = y;
        count = TOTAL_COUNT;
    }

    public void update() {
        stakeTimer -= Gdx.graphics.getDeltaTime();
        if (stakeTimer < 0.0F) {
            AbstractDungeon.effectsQueue.add(new CollectorStakeEffect(x + MathUtils.random(-30.0F, 30.0F) * Settings.scale, y + MathUtils.random(-40.0F, 20.0F) * Settings.scale));
            stakeTimer = 0.04F;
            --count;
            if (count == 0) {
                isDone = true;
            }
        }

    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
