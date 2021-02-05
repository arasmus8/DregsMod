package dregsmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class AbstractCurseHoldingCard extends AbstractDregsCard {
    public boolean holdingCurse;
    public int cursesInHand;

    public AbstractCurseHoldingCard(String id,
                                    int cost,
                                    CardType type,
                                    CardColor color,
                                    CardRarity rarity,
                                    CardTarget target,
                                    CardTags... tags) {
        super(id, cost, type, rarity, target, color, tags);
    }

    private boolean checkForCurse() {
        long curseCount = AbstractDungeon.player.hand.getCardsOfType(CardType.CURSE).group.stream()
                .filter(card -> card != this)
                .count();
        cursesInHand = (int) curseCount;
        return curseCount > 0;
    }

    @Override
    public void triggerOnGlowCheck() {
        if (checkForCurse()) {
            glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCurseHoldingCard copy = (AbstractCurseHoldingCard) super.makeStatEquivalentCopy();
        copy.holdingCurse = holdingCurse;
        return copy;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        holdingCurse = checkForCurse();
    }
}
