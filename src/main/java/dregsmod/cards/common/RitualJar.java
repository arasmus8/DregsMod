package dregsmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.actions.RitualJarAction;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.cards.DregsCardTags;
import dregsmod.characters.Dregs;
import dregsmod.powers.BlightedPower;

public class RitualJar extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(RitualJar.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int MAGIC = 5;
    private static final int UPGRADED_MAGIC = 5;

    public RitualJar() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR, DregsCardTags.AWAKEN_SKILL);
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new BlightedPower(m, magicNumber), magicNumber));
        addToBot(new RitualJarAction(this));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADED_MAGIC);
            initializeDescription();
        }
    }
}
