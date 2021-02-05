package dregsmod.cards;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.Arrays;

import static dregsmod.DregsMod.getModID;
import static dregsmod.DregsMod.makeCardPath;

public abstract class AbstractDregsCard extends CustomCard {
    protected final CardStrings cardStrings;
    protected final String NAME;
    protected final String DESCRIPTION;
    protected final String UPGRADE_DESCRIPTION;
    protected final String[] EXTENDED_DESCRIPTION;

    public AbstractDregsCard(final String id,
                             final int cost,
                             final AbstractCard.CardType type,
                             final AbstractCard.CardRarity rarity,
                             final AbstractCard.CardTarget target,
                             final AbstractCard.CardColor color,
                             CardTags... tagsList) {
        super(id, "", getRegionName(id), cost, "", type, color, rarity, target);
        cardStrings = CardCrawlGame.languagePack.getCardStrings(id);
        name = NAME = cardStrings.NAME;
        originalName = NAME;
        rawDescription = DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
        initializeTitle();
        initializeDescription();
        if (tagsList != null) {
            tags.addAll(Arrays.asList(tagsList));
        }
    }

    protected static String getBaseImagePath(String id) {
        return id.replaceAll(getModID() + ":", "");
    }

    protected static CustomCard.RegionName getRegionName(String id) {
        return new CustomCard.RegionName(String.format("%s/%s", getModID(), getBaseImagePath(id)));
    }

    @Override
    public void loadCardImage(String img) {
        TextureAtlas cardAtlas = ReflectionHacks.getPrivateStatic(AbstractCard.class, "cardAtlas");
        portrait = cardAtlas.findRegion(img);
    }

    @Override
    protected Texture getPortraitImage() {
        return ImageMaster.loadImage(makeCardPath(String.format("%s_p.png", getBaseImagePath(cardID))));
    }
}
