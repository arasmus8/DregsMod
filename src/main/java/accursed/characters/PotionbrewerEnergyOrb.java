package accursed.characters;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class PotionbrewerEnergyOrb extends CustomEnergyOrb {

    private static final float ORB_SCALE = 0.85F;
    private Texture jar;
    private Texture liquid;
    private Texture liquidDark;
    private Texture bubble1;
    private Texture bubble1Dark;
    private Texture bubble2;
    private Texture bubble2Dark;
    private Texture orbVfx;

    private Bubble[] bubbles;
    private Bubble[] doubleBubbles;

    private static class Bubble {
        private float velocity;
        private float x;
        private float y;
        private float a;
        private float scale;
        private float origin;
        private float size;
        protected boolean isDone = false;

        protected Bubble(final boolean lg) {
            this.x = MathUtils.random(-55.0F, 5.0F);
            this.y = MathUtils.random(-60.0F, -30.0F);
            this.a = 0.0F;
            this.scale = MathUtils.random(0.3F, 1.0F);
            this.velocity = MathUtils.random(1.0F, 8.0F);
            if (lg) {
                origin = 24.0F;
                size = 48.0F;
            } else {
                origin = 16.0F;
                size = 32.0F;
            }
        }

        protected void update(final float dt) {
            float ANIM_STEP = 3.0F;
            if (y >= 5.0F) {
                if (a <= 0.0F) {
                    isDone = true;
                } else {
                    a -= dt * ANIM_STEP;
                    if (a < 0.0F) {
                        a = 0.0F;
                    }
                }
            } else {
                y += dt * ANIM_STEP * velocity;
                if (a < 1.0F) {
                    a += dt * ANIM_STEP * velocity;
                    if (a > 1.0F) {
                        a = 1.0F;
                    }
                }
            }
        }

        protected void render(final SpriteBatch sb, final Texture tx, final float current_x, final float current_y) {
            sb.setColor(1.0F, 1.0F, 1.0F, a);
            sb.draw(tx,
                    current_x + x,
                    current_y + y,
                    origin,
                    origin,
                    size,
                    size,
                    scale,
                    scale,
                    0,
                    0,
                    0,
                    (int) size,
                    (int) size,
                    false,
                    false);
        }
    }

    public PotionbrewerEnergyOrb() {
        super(null, null, null);
        jar = ImageMaster.loadImage("accursedResources/images/char/potionbrewer/orb/new4.png");
        liquid = ImageMaster.loadImage("accursedResources/images/char/potionbrewer/orb/new1.png");
        liquidDark = ImageMaster.loadImage("accursedResources/images/char/potionbrewer/orb/new1d.png");
        bubble1 = ImageMaster.loadImage("accursedResources/images/char/potionbrewer/orb/new2.png");
        bubble1Dark = ImageMaster.loadImage("accursedResources/images/char/potionbrewer/orb/new2d.png");
        bubble2 = ImageMaster.loadImage("accursedResources/images/char/potionbrewer/orb/new3.png");
        bubble2Dark = ImageMaster.loadImage("accursedResources/images/char/potionbrewer/orb/new3d.png");
        orbVfx = ImageMaster.loadImage("accursedResources/images/char/potionbrewer/orb/vfx.png");
        bubbles = new Bubble[4];
        for (int i = 0; i < bubbles.length; ++i) {
            bubbles[i] = new Bubble(false);
        }
        doubleBubbles = new Bubble[2];
        for (int i = 0; i < doubleBubbles.length; ++i) {
            doubleBubbles[i] = new Bubble(true);
        }
    }

    public Texture getEnergyImage() {
        return this.orbVfx;
    }

    @Override
    public void renderOrb(SpriteBatch sb, boolean enabled, float x, float y) {
        sb.setColor(Color.WHITE);
        if (enabled) {
            sb.draw(this.liquid,
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

            for (int i = 0; i < bubbles.length; ++i) {
                if (bubbles[i].isDone) {
                    bubbles[i] = new Bubble(false);
                }
                bubbles[i].render(sb, bubble2, x, y);
            }

            for (int i = 0; i < doubleBubbles.length; ++i) {
                if (doubleBubbles[i].isDone) {
                    doubleBubbles[i] = new Bubble(true);
                }
                doubleBubbles[i].render(sb, bubble1, x, y);
            }
        } else {
            sb.draw(this.liquidDark,
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

            for (int i = 0; i < bubbles.length; ++i) {
                if (bubbles[i].isDone) {
                    bubbles[i] = new Bubble(false);
                }
                bubbles[i].render(sb, bubble2Dark, x, y);
            }

            for (int i = 0; i < doubleBubbles.length; ++i) {
                if (doubleBubbles[i].isDone) {
                    doubleBubbles[i] = new Bubble(true);
                }
                doubleBubbles[i].render(sb, bubble1Dark, x, y);
            }
        }

        sb.setColor(Color.WHITE);
        sb.draw(this.jar,
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

    }

    @Override
    public void updateOrb(int energyCount) {
        float dt = Gdx.graphics.getDeltaTime();
        for (Bubble bubble : bubbles) {
            bubble.update(dt);
        }
        for (Bubble doubleBubble : doubleBubbles) {
            doubleBubble.update(dt);
        }
    }
}
