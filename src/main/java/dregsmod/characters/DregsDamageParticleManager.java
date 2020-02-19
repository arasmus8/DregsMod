package dregsmod.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DregsDamageParticleManager {
    private static ParticleEffect damageEffect;
    private static ParticleEffect damageEffectSm;
    private static ParticleEffect damageEffectLg;
    private static DregsDamageParticleManager.DamageSize damageSize;
    private boolean active;
    private boolean initialized;

    public DregsDamageParticleManager() {
        damageEffect = new ParticleEffect();
        damageEffect.load(Gdx.files.internal("particleSettings/DamageSpray"), Gdx.files.internal("particleSettings"));
        damageEffectSm = new ParticleEffect();
        damageEffectSm.load(Gdx.files.internal("particleSettings/DamageSpraySm"), Gdx.files.internal("particleSettings"));
        damageEffectLg = new ParticleEffect();
        damageEffectLg.load(Gdx.files.internal("particleSettings/DamageSprayLg"), Gdx.files.internal("particleSettings"));
        active = false;

    }

    public void newDamageParticle(int damageAmount) {
        if (!active) {
            int maxHp = AbstractDungeon.player.maxHealth;
            if (damageAmount < maxHp / 10) {
                damageSize = DregsDamageParticleManager.DamageSize.SMALL;
            } else if (damageAmount > maxHp / 4) {
                damageSize = DregsDamageParticleManager.DamageSize.LARGE;
            } else {
                damageSize = DregsDamageParticleManager.DamageSize.NORMAL;
            }
            active = true;
            initialized = false;
        }
    }

    public void render(SpriteBatch sb, float x, float y, boolean flipped) {
        if (active) {
            if (!initialized) {
                float xPos = flipped ? x + 40f * Settings.scale : x - 40f * Settings.scale;
                switch (damageSize) {
                    case LARGE:
                        damageEffectLg.setPosition(xPos, MathUtils.random(80, 180) * Settings.scale + y);
                        damageEffectLg.start();
                        break;
                    case NORMAL:
                        damageEffect.setPosition(xPos, MathUtils.random(80, 180) * Settings.scale + y);
                        damageEffect.start();
                        break;
                    case SMALL:
                        damageEffectSm.setPosition(xPos, MathUtils.random(80, 180) * Settings.scale + y);
                        damageEffectSm.start();
                        break;
                }
                initialized = true;
            }
            switch (damageSize) {
                case LARGE:
                    damageEffectLg.draw(sb, Gdx.graphics.getDeltaTime());
                    if (damageEffectLg.isComplete()) {
                        active = false;
                    }
                    break;
                case NORMAL:
                    damageEffect.draw(sb, Gdx.graphics.getDeltaTime());
                    if (damageEffect.isComplete()) {
                        active = false;
                    }
                    break;
                case SMALL:
                    damageEffectSm.draw(sb, Gdx.graphics.getDeltaTime());
                    if (damageEffectSm.isComplete()) {
                        active = false;
                    }
                    break;
            }
        }

    }

    private enum DamageSize {
        SMALL,
        NORMAL,
        LARGE;

        DamageSize() {
        }
    }
}
