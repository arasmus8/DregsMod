package dregsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import dregsmod.DregsMod;
import dregsmod.util.TextureLoader;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static dregsmod.DregsMod.makeRelicOutlinePath;
import static dregsmod.DregsMod.makeRelicPath;

public class DarksteelHammer extends CustomRelic {

    public static final String ID = DregsMod.makeID(DarksteelHammer.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("DarksteelHammer.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("DarksteelHammer.png"));

    public DarksteelHammer() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK);
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        if (c.color == AbstractCard.CardColor.CURSE) {
            AbstractPlayer p = AbstractDungeon.player;
            List<AbstractCard> upgradeableCards = p.masterDeck.group.stream()
                    .filter(AbstractCard::canUpgrade)
                    .collect(Collectors.toList());
            if (upgradeableCards.size() > 0) {
                Collections.shuffle(upgradeableCards, AbstractDungeon.miscRng.random);
                AbstractCard toUpgrade = upgradeableCards.get(0);
                toUpgrade.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(toUpgrade);
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(toUpgrade.makeStatEquivalentCopy(), Settings.WIDTH * 0.75f, Settings.HEIGHT / 2f));
                AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH * 0.75F, (float) Settings.HEIGHT / 2.0F));
            }
        }
    }

    @Override
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum < 40;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
