package dregsmod.cards.uncommon;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import dregsmod.DregsMod;
import dregsmod.characters.Dregs;
import dregsmod.powers.AsylumPower;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static dregsmod.DregsMod.makeCardPath;

public class Asylum extends CustomCard {

    public static final String ID = DregsMod.makeID(Asylum.class.getSimpleName());
    public static final String IMG = makeCardPath("Power.png");
    public static CardStrings CARD_STRINGS = languagePack.getCardStrings(ID);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int MAGIC = 1;
    private static final int UPGRADED_COST = 0;

    public Asylum() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new AsylumPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}