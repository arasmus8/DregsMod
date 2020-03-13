package dregsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import dregsmod.DregsMod;
import dregsmod.util.TextureLoader;

import static dregsmod.DregsMod.makeRelicOutlinePath;
import static dregsmod.DregsMod.makeRelicPath;

public class DeathBrand extends CustomRelic {

    public static final String ID = DregsMod.makeID(DeathBrand.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("DeathBrand.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("DeathBrand.png"));

    public DeathBrand() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if (drawnCard.type == AbstractCard.CardType.CURSE) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(
                    new DamageAllEnemiesAction(
                            null,
                            DamageInfo.createDamageMatrix(5, true),
                            DamageInfo.DamageType.THORNS,
                            AbstractGameAction.AttackEffect.BLUNT_LIGHT
                    )
            );
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(CurseBrand.ID);
    }

    @Override
    public void obtain() {
        for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
            if (AbstractDungeon.player.relics.get(i).relicId.equals(CurseBrand.ID)) {
                instantObtain(AbstractDungeon.player, i, true);
                return;
            }
        }
        super.obtain();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
