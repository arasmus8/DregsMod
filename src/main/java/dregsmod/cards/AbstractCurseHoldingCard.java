package dregsmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class AbstractCurseHoldingCard extends CustomCard {
    public boolean holdingCurse;

    public AbstractCurseHoldingCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    private boolean checkForCurse() {
        long curseCount = AbstractDungeon.player.hand.group.stream()
                .filter(card -> card != this && card.type == CardType.CURSE)
                .count();
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
    public void applyPowers() {
        super.applyPowers();
        holdingCurse = checkForCurse();
    }
}
