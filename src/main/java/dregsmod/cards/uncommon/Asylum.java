package dregsmod.cards.uncommon;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.characters.Dregs;
import dregsmod.powers.AsylumPower;

import java.util.ArrayList;
import java.util.List;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static dregsmod.DregsMod.makeCardPath;

public class Asylum extends CustomCard {

    public static final String ID = DregsMod.makeID(Asylum.class.getSimpleName());
    public static final String IMG = makeCardPath("Asylum.png");
    public static CardStrings CARD_STRINGS = languagePack.getCardStrings(ID);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int MAGIC = 1;
    private static final int UPGRADED_COST = 0;

    private final List<TooltipInfo> customTooltips;

    public Asylum() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        customTooltips = new ArrayList<>();
        if (BaseMod.getKeywordProper("dregsmod:asylumorbs") != null) {
            customTooltips.add(new TooltipInfo(BaseMod.getKeywordTitle("dregsmod:asylumorbs"),
                    BaseMod.getKeywordDescription("dregsmod:asylumorbs")));
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return customTooltips;
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
