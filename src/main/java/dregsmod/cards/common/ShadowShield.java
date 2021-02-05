package dregsmod.cards.common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.characters.Dregs;
import dregsmod.patches.variables.CardSealed;

public class ShadowShield extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(ShadowShield.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int BLOCK = 1;
    private static final int MAGIC = 1;

    public ShadowShield() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR);
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = MAGIC;
    }

    @Override
    public void applyPowers() {
        AbstractPlayer p = AbstractDungeon.player;
        int cardCount = p.hand.size() - 1;
        if (upgraded) {
            cardCount += p.discardPile.group.stream()
                    .filter(card -> CardSealed.isSealed.get(card))
                    .count();
        }
        baseBlock = cardCount;
        super.applyPowers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
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
