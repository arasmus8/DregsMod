package dregsmod.cards.common;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.actions.CurateAction;
import dregsmod.actions.DiscardAndPerformAction;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.characters.Dregs;

public class Curate extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(Curate.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 0;

    private static final int BLOCK = 3;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    private static final int MAGIC = 1;

    public Curate() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR);
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, magicNumber));
        addToBot(new DiscardAndPerformAction(1, false, new CurateAction(p, block)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
