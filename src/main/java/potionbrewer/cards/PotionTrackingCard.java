package potionbrewer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public abstract class PotionTrackingCard extends CustomCard {
    public PotionTrackingCard(final String id,
                              final String name,
                              final String img,
                              final int cost,
                              final String rawDescription,
                              final CardType type,
                              final CardColor color,
                              final CardRarity rarity,
                              final CardTarget target) {

        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
    }

    public abstract void onUsePotion(AbstractPotion p);
}
