package dregsmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import dregsmod.DregsMod;

public class SealCardEffect extends AbstractGameEffect {
    private AbstractCard c;
    private float deltaY;

    public SealCardEffect(AbstractCard card) {
        c = card;
        duration = 1.0f;
    }

    @Override
    public void update() {
        if (duration == 1.0f) {
            CardCrawlGame.sound.play("KEY_OBTAIN", 0.2F);// 23
            deltaY = c.hb.height / 2;
        }

        duration -= Gdx.graphics.getDeltaTime();
        deltaY = Interpolation.elastic.apply(c.hb.height / 2, 0, duration);

        if (duration < 0.0f) {
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Texture top = DregsMod.uiTextures.get("cardSealTop");
        Texture bottom = DregsMod.uiTextures.get("cardSealBottom");

        float scale = c.drawScale;

        Vector2 topVec = new Vector2(-150, -216 + deltaY);
        topVec.scl(c.drawScale * Settings.scale);
        topVec.rotate(c.angle);
        sb.draw(top, c.current_x + topVec.x, c.current_y + topVec.y, 0F, 0F, 300F, 216F, scale, scale, 0f, 0, 0, 300, 216, false, false);

        Vector2 botVec = new Vector2(-150, 220 - deltaY);
        botVec.scl(c.drawScale * Settings.scale);
        botVec.rotate(c.angle);
        sb.draw(bottom, c.current_x + botVec.x, c.current_y + botVec.y, 0F, 0F, 300F, 220F, scale, scale, 0f, 0, 0, 300, 220, false, false);
    }

    @Override
    public void dispose() {
    }
}
