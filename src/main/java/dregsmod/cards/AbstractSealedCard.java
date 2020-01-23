package dregsmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public abstract class AbstractSealedCard extends CustomCard {
    public AbstractSealedCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    public abstract void triggerWhileSealed(AbstractPlayer player);
}
