package dregsmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.characters.Dregs;
import dregsmod.powers.ShiftingSandsPower;

public class ShiftingSands extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(ShiftingSands.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;

    public ShiftingSands() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR);
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ShiftingSandsPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MAGIC);
            initializeDescription();
        }
    }
}
