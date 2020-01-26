package dregsmod.patches.sealed;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import dregsmod.DregsMod;
import dregsmod.patches.variables.CardSealed;

@SpirePatch(
        clz = AbstractCard.class,
        method = "renderPortrait"
)
public class AbstractCardRender {
    public static void Postfix(AbstractCard __instance, SpriteBatch sb) {
        if (!Settings.hideCards) {
            if (CardSealed.isSealed.get(__instance)) {
                Color renderColor = Color.WHITE.cpy();
                renderColor.a = 0.8f;
                sb.setColor(renderColor);

                Texture img = DregsMod.uiTextures.get("cardSeal");
                Vector2 vec = new Vector2(-82, -15);
                vec.scl(__instance.drawScale * Settings.scale);
                vec.rotate(__instance.angle);
                float x = __instance.current_x + vec.x;
                float y = __instance.current_y + vec.y;
                float scale = __instance.drawScale * 0.55F * Settings.scale;
                sb.draw(img, x, y, 0F, 0F, 325.0F, 325.0F, scale, scale, 0.0F, 0, 0, 325, 325, false, false);
                sb.setColor(Color.WHITE.cpy());
            }
        }
    }
}
