package dregsmod.cards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import dregsmod.patches.variables.CardSealed;

public abstract class AbstractSealedCard extends AbstractDregsCard {
    public AbstractSealedCard(String id,
                              int cost,
                              CardType type,
                              CardColor color,
                              CardRarity rarity,
                              CardTarget target,
                              CardTags... tags) {
        super(id, cost, type, rarity, target, color, tags);
    }

    @Override
    public void atTurnStart() {
        if (CardSealed.isSealed.get(this)) {
            applyPowers();
            float offW = AbstractDungeon.player.hb.width;
            float xOff = MathUtils.random(-offW, offW);
            AbstractDungeon.effectList.add(
                    new ShowCardBrieflyEffect(this.makeSameInstanceOf(),
                            (Settings.WIDTH + xOff) / 2f,
                            (Settings.HEIGHT + xOff / 7f) / 2f)
            );
            triggerWhileSealed(AbstractDungeon.player);
        }
    }

    public abstract void triggerWhileSealed(AbstractPlayer player);
}
