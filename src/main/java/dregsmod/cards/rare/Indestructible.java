package dregsmod.cards.rare;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.cards.DregsCardTags;
import dregsmod.characters.Dregs;

public class Indestructible extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(Indestructible.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 2;

    private static final int MAGIC = 12;
    private static final int UPGRADED_MAGIC = 6;

    public Indestructible() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR, DregsCardTags.AWAKEN_SKILL);
        magicNumber = baseMagicNumber = MAGIC;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AddTemporaryHPAction(p, p, magicNumber));
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
