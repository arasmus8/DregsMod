package dregsmod.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.ArrayList;

public class DregsGemManager {
    private static TextureAtlas.AtlasRegion circleImg;
    private static float SCALE;
    private static final Color RED = Color.RED.cpy();
    private static final Color GREEN = Color.FOREST.cpy();
    private static final Color BLUE = Color.SKY.cpy();
    private static final Color PURPLE = Color.VIOLET.cpy();
    private static final Color LIGHT_GRAY = Color.LIGHT_GRAY.cpy();
    private static final Color DARK_GRAY = Color.YELLOW.cpy(); // Color.DARK_GRAY.cpy();

    private static float[] xOffsets;
    private static float[] yOffsets;

    ArrayList<CardColor> colors;

    public DregsGemManager() {
        // TODO: load different images for the different colors
        circleImg = ImageMaster.CARD_RED_ORB;
    }

    public void onAnalyze(ArrayList<CardColor> colors) {
        this.colors = colors;
        SCALE = 0.2f * Settings.scale;
        xOffsets = new float[]{78f, 55f, 99f, 26f, 79f, 46f, 139f, 24f, 124f, 104f, 79f, 14f, 88f, 118f, 36f, 54f, 85f, 103f, 34f, 124f, 75f, 100f, 17f, 80f, 63f, 17f, 50f, 98f, 53f, 61f};
        yOffsets = new float[]{148f, 104f, 64f, 166f, 113f, 45f, 120f, 123f, 34f, 217f, 85f, 36f, 161f, 78f, 72f, 150f, 203f, 32f, 143f, 128f, 61f, 103f, 150f, 36f, 129f, 110f, 174f, 83f, 62f, 86f};
        for (int i = 0; i < xOffsets.length; ++i) {
            xOffsets[i] *= Settings.scale;
            yOffsets[i] *= Settings.scale;
        }
    }

    private Color colorFor(CardColor cardColor) {
        switch (cardColor) {
            case RED:
                return RED;
            case GREEN:
                return GREEN;
            case BLUE:
                return BLUE;
            case PURPLE:
                return PURPLE;
            case COLORLESS:
                return LIGHT_GRAY;
            case CURSE:
            default:
                return DARK_GRAY;
        }
    }

    public void render(SpriteBatch sb, float x, float y, boolean flip) {
        for (int i = 0; i < colors.size(); ++i) {
            sb.setColor(colorFor(colors.get(i)));
            if (flip) {
                sb.draw(circleImg, x - xOffsets[i], y - yOffsets[i], circleImg.packedWidth / 2f, circleImg.packedHeight / 2f, circleImg.packedWidth, circleImg.packedHeight, SCALE, SCALE, 0f);
            } else {
                sb.draw(
                        circleImg,
                        x + xOffsets[i] - circleImg.packedWidth / 2f,
                        y + yOffsets[i] - circleImg.packedHeight / 2f,
                        circleImg.packedWidth / 2f,
                        circleImg.packedHeight / 2f,
                        circleImg.packedWidth,
                        circleImg.packedHeight,
                        SCALE,
                        SCALE,
                        0f
                );
            }
        }
        sb.setColor(Color.WHITE.cpy());
    }
}
