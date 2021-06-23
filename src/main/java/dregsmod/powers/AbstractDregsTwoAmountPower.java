package dregsmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import dregsmod.DregsMod;

public class AbstractDregsTwoAmountPower extends TwoAmountPower {
    private final TextureAtlas powerAtlas = DregsMod.assets.loadAtlas(DregsMod.assetPath("images/powers/powers.atlas"));

    @Override
    protected void loadRegion(String fileName) {
        region48 = powerAtlas.findRegion("32/" + fileName);
        region128 = powerAtlas.findRegion("128/" + fileName);

        if (region48 == null && region128 == null) {
            super.loadRegion(fileName);
        }
    }
}
