package dregsmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import dregsmod.DregsMod;

public class SealCardEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 2.5F;
    private static final float ANIM_DUR = 1.0F;
    private static final float ANIM_START_AT = 1.75F;
    private static final float SOUND_AT = 1.0F;
    private AbstractCard c;
    private static final float PADDING;
    private Color drawColor = Color.WHITE.cpy();
    private boolean soundPlayed;

    private static int count = 0;

    public SealCardEffect(AbstractCard card) {
        c = card;
        startingDuration = duration = EFFECT_DUR;
        c.drawScale = 0.01f;
        c.targetDrawScale = 1.0f;
        c.current_x = Settings.WIDTH / 2.0f - (PADDING * count);
        c.current_y = Settings.HEIGHT / 2.0f;
        c.target_x = c.current_x;
        c.target_y = c.current_y;
        soundPlayed = false;
        if (count > 5) {
            isDone = true;
        } else {
            ++count;
        }
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.6f) {
            c.fadingOut = true;
        }

        if (duration < SOUND_AT && !soundPlayed) {
            soundPlayed = true;
            CardCrawlGame.sound.play("BLOCK_GAIN_2", 0.2F);// 23
        }

        c.update();

        if (duration < 0.0f) {
            isDone = true;
            --count;
        }
    }

    static int topY = 216;
    static int botY = 220;

    @Override
    public void render(SpriteBatch sb) {
        if (!isDone) {
            c.render(sb);
            Texture top = DregsMod.uiTextures.get("cardSealTop");
            Texture bottom = DregsMod.uiTextures.get("cardSealBottom");

            float scale = c.drawScale * Settings.scale;
            float dT = MathUtils.clamp((ANIM_START_AT - duration) / ANIM_DUR, 0f, 1f);

            float y = Interpolation.bounceOut.apply(topY - 2, -2, dT);
            float a = 1.0f;
            if (duration > ANIM_START_AT) {
                a = Interpolation.fade.apply(0f, 1f, duration - ANIM_START_AT);
            }
            drawColor.a = c.transparency * a;
            sb.setColor(drawColor);
            Vector2 topVec = new Vector2(-150, y);
            topVec.scl(c.drawScale * Settings.scale);
            topVec.rotate(c.angle);
            sb.draw(top, c.current_x + topVec.x, c.current_y + topVec.y, 0F, 0F, 300F, 216F, scale, scale, c.angle, 0, 0, 300, 216, false, false);

            y = Interpolation.bounceOut.apply(-2*botY + 4, -botY + 4, dT);
            Vector2 botVec = new Vector2(-150, y);
            botVec.scl(c.drawScale * Settings.scale);
            botVec.rotate(c.angle);
            sb.draw(bottom, c.current_x + botVec.x, c.current_y + botVec.y, 0F, 0F, 300F, 220F, scale, scale, c.angle, 0, 0, 300, 220, false, false);
        }
    }

    @Override
    public void dispose() {
    }

    static {
        PADDING = 30.0f * Settings.scale;
    }
}
