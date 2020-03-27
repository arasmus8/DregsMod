package dregsmod.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.ArrayList;

public class DregsGemManager {
    private static TextureAtlas.AtlasRegion redImg;
    private static TextureAtlas.AtlasRegion greenImg;
    private static TextureAtlas.AtlasRegion blueImg;
    private static TextureAtlas.AtlasRegion purpleImg;
    private static TextureAtlas.AtlasRegion curseImg;
    private static float SCALE;
    private static float CURSE_SCALE;
    private static float WIDTH_OFFSET;
    private static float HEIGHT_OFFSET;
    private static float CURSE_WIDTH_OFFSET;
    private static float CURSE_HEIGHT_OFFSET;

    private static float highlightY;

    private static float[] xOffsets;
    private static float[] yOffsets;

    ArrayList<CardColor> colors;

    public DregsGemManager() {
        redImg = ImageMaster.CARD_RED_ORB;
        greenImg = ImageMaster.CARD_GREEN_ORB;
        blueImg = ImageMaster.CARD_BLUE_ORB;
        purpleImg = ImageMaster.CARD_PURPLE_ORB;
        curseImg = ImageMaster.STRIKE_BLUR;
        colors = new ArrayList<>();
        SCALE = 0.16f * Settings.scale;
        CURSE_SCALE = 0.6f * Settings.scale;
        WIDTH_OFFSET = redImg.packedWidth / 2f * Settings.scale;
        HEIGHT_OFFSET = redImg.packedHeight / 2f * Settings.scale;
        CURSE_WIDTH_OFFSET = curseImg.packedWidth / 2f * Settings.scale;
        CURSE_HEIGHT_OFFSET = curseImg.packedHeight / 2f * Settings.scale;
        xOffsets = new float[]{78f, 55f, 99f, 26f, 79f, 46f, 139f, 24f, 124f, 104f, 79f, 14f, 88f, 118f, 36f, 54f, 85f, 103f, 34f, 124f, 75f, 100f, 17f, 80f, 63f, 17f, 50f, 98f, 53f, 61f};
        yOffsets = new float[]{148f, 104f, 64f, 166f, 113f, 45f, 120f, 123f, 34f, 217f, 85f, 36f, 161f, 78f, 72f, 150f, 203f, 32f, 143f, 128f, 61f, 103f, 150f, 36f, 129f, 110f, 174f, 83f, 62f, 86f};
        for (int i = 0; i < xOffsets.length; ++i) {
            xOffsets[i] *= Settings.scale;
            yOffsets[i] *= Settings.scale;
        }
    }

    public void onAnalyze(ArrayList<CardColor> colors) {
        this.colors = colors;
    }

    public void update() {
        if (highlightY < 0) {
            if (MathUtils.randomBoolean(0.50f)) {
                // start new highlight pulse
                highlightY = 0f;
            }
        } else {
            highlightY += Gdx.graphics.getDeltaTime() * 50f;
            if (highlightY > 250f) {
                highlightY = -1f;
            }
        }
    }

    public void draw(SpriteBatch sb, CardColor color, float x, float y, float a) {
        sb.setColor(new Color(1f, 1f, 1f, a));
        switch (color) {
            case RED:
                sb.draw(redImg, x - WIDTH_OFFSET, y - HEIGHT_OFFSET, redImg.packedWidth / 2f, redImg.packedHeight / 2f, redImg.packedWidth, redImg.packedHeight, SCALE, SCALE, 0f);
                break;
            case GREEN:
                sb.draw(greenImg, x - WIDTH_OFFSET, y - HEIGHT_OFFSET, greenImg.packedWidth / 2f, greenImg.packedHeight / 2f, greenImg.packedWidth, greenImg.packedHeight, SCALE, SCALE, 0f);
                break;
            case BLUE:
                sb.draw(blueImg, x - WIDTH_OFFSET, y - HEIGHT_OFFSET, blueImg.packedWidth / 2f, blueImg.packedHeight / 2f, blueImg.packedWidth, blueImg.packedHeight, SCALE, SCALE, 0f);
                break;
            case PURPLE:
                sb.draw(purpleImg, x - WIDTH_OFFSET, y - HEIGHT_OFFSET, purpleImg.packedWidth / 2f, purpleImg.packedHeight / 2f, purpleImg.packedWidth, purpleImg.packedHeight, SCALE, SCALE, 0f);
                break;
            default:
                sb.setColor(new Color(0.8f, 0f, 1f, a));
                sb.draw(curseImg, x - CURSE_WIDTH_OFFSET, y - CURSE_HEIGHT_OFFSET, curseImg.packedWidth / 2f, curseImg.packedHeight / 2f, curseImg.packedWidth, curseImg.packedHeight, CURSE_SCALE, CURSE_SCALE, 0f);
                break;
        }
    }

    public void render(SpriteBatch sb, float x, float y, boolean flip) {
        Color original = sb.getColor();
        for (int i = 0; i < colors.size(); ++i) {
            float highlightFadeValue = MathUtils.clamp(Math.abs(yOffsets[i] - highlightY) / 13f, 0f, 1f);
            if (i == 0 && highlightFadeValue < 1f) {
                sb.getColor();
            }
            float a = Interpolation.exp5In.apply(original.a, original.a * 0.7f, highlightFadeValue);
            if (flip) {
                draw(sb, colors.get(i), x - xOffsets[i], y + yOffsets[i], a);
            } else {
                draw(sb, colors.get(i), x + xOffsets[i], y + yOffsets[i], a);
            }
        }
        sb.setColor(original);
    }
}
