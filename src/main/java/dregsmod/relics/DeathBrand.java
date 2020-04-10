package dregsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import dregsmod.DregsMod;
import dregsmod.actions.CardAwokenAction;
import dregsmod.util.TextureLoader;

import java.util.function.Predicate;
import java.util.stream.Collectors;

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
            CardGroup cg = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            AbstractPlayer p = AbstractDungeon.player;
            Predicate<AbstractCard> eligible = card1 -> card1.type == AbstractCard.CardType.ATTACK ||
                    card1.type == AbstractCard.CardType.SKILL;
            cg.group.addAll(p.hand.group.stream().filter(eligible).collect(Collectors.toList()));
            if (cg.size() > 0) {
                this.flash();
                this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                this.addToBot(
                        new CardAwokenAction(cg.getRandomCard(AbstractDungeon.cardRng), 2)
                );
            }
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
