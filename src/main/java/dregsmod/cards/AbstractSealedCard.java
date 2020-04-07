package dregsmod.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import dregsmod.patches.variables.CardSealed;

public abstract class AbstractSealedCard extends CustomCard {
    public AbstractSealedCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    @Override
    public void atTurnStart() {
        if (CardSealed.isSealed.get(this)) {
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
