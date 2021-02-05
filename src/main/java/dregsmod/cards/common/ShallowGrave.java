package dregsmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.characters.Dregs;
import dregsmod.powers.SturdinessPower;

public class ShallowGrave extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(ShallowGrave.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int BLOCK = 4;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    private static final int MAGIC = 2;
    private static final int UPGRADED_MAGIC = 1;

    public ShallowGrave() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR);
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SturdinessPower(p, magicNumber), magicNumber));
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADED_MAGIC);
            initializeDescription();
        }
    }
}
