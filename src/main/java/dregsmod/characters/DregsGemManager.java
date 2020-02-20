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
    private static TextureAtlas.AtlasRegion ringImg;
    private static float SCALE;
    private static float RING_SCALE;
    private static float WIDTH_OFFSET;
    private static float HEIGHT_OFFSET;
    private static float RING_WIDTH_OFFSET;
    private static float RING_HEIGHT_OFFSET;

    private static float highlightY;

    private static float[] xOffsets;
    private static float[] yOffsets;

    ArrayList<CardColor> colors;

    public DregsGemManager() {
        redImg = ImageMaster.CARD_RED_ORB;
        greenImg = ImageMaster.CARD_GREEN_ORB;
        blueImg = ImageMaster.CARD_BLUE_ORB;
        purpleImg = ImageMaster.CARD_PURPLE_ORB;
        ringImg = ImageMaster.WHITE_RING;
        colors = new ArrayList<>();
        SCALE = 0.2f * Settings.scale;
        RING_SCALE = 0.08f * Settings.scale;
        WIDTH_OFFSET = redImg.packedWidth / 2f * Settings.scale;
        HEIGHT_OFFSET = redImg.packedHeight / 2f * Settings.scale;
        RING_WIDTH_OFFSET = ringImg.packedWidth / 2f * Settings.scale;
        RING_HEIGHT_OFFSET = ringImg.packedHeight / 2f * Settings.scale;
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
                highlightY = 0;
            }
        } else {
            highlightY += Gdx.graphics.getDeltaTime() * 50f;
            if (highlightY > 250f) {
                highlightY = -1;
            }
        }
    }

    public void draw(SpriteBatch sb, CardColor color, float x, float y) {
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
            case COLORLESS:
            case CURSE:
                sb.draw(ringImg, x - RING_WIDTH_OFFSET, y - RING_HEIGHT_OFFSET, ringImg.packedWidth / 2f, ringImg.packedHeight / 2f, ringImg.packedWidth, ringImg.packedHeight, RING_SCALE, RING_SCALE, 0f);
                break;
        }
    }

    public void render(SpriteBatch sb, float x, float y, boolean flip) {
        Color original = sb.getColor();
        for (int i = 0; i < colors.size(); ++i) {
            float a = Interpolation.fade.apply(original.a, original.a * 0.7f, Math.abs(yOffsets[i] - highlightY));
            sb.setColor(new Color(1f, 1f, 1f, a));
            if (flip) {
                draw(sb, colors.get(i), x - xOffsets[i], y - yOffsets[i]);
            } else {
                draw(sb, colors.get(i), x + xOffsets[i], y + yOffsets[i]);
            }
        }
        sb.setColor(original);
    }
}
