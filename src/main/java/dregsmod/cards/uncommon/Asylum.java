package dregsmod.cards.uncommon;

import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.characters.Dregs;
import dregsmod.powers.AsylumPower;

import java.util.ArrayList;
import java.util.List;

public class Asylum extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(Asylum.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int MAGIC = 1;
    private static final int UPGRADED_COST = 0;

    private final List<TooltipInfo> customTooltips;

    public Asylum() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR);
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
