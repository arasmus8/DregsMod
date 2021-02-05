package dregsmod.cards.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.actions.KindleAction;
import dregsmod.actions.SealAndPerformAction;
import dregsmod.cards.AbstractCurseHoldingCard;
import dregsmod.cards.DregsCardTags;
import dregsmod.characters.Dregs;

public class Kindle extends AbstractCurseHoldingCard {
    public static final String ID = DregsMod.makeID(Kindle.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 0;

    public Kindle() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        tags.add(DregsCardTags.CANT_AWAKEN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded && holdingCurse) {
            addToBot(new SealAndPerformAction(1,
                    true,
                    p.hand,
                    card -> card.type == CardType.CURSE,
                    new KindleAction(this, true)));
        } else {
            addToBot(new KindleAction(this, false));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
