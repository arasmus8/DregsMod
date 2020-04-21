package dregsmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCurseHoldingCard;
import dregsmod.cards.AwakenSkillTag;
import dregsmod.characters.Dregs;
import dregsmod.powers.FortitudePower;

import static dregsmod.DregsMod.makeCardPath;

public class EmbraceDarkness extends AbstractCurseHoldingCard {

// TEXT DECLARATION

    public static final String ID = DregsMod.makeID(EmbraceDarkness.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Skill.png");
// Must have an image with the same NAME as the card in your image folder!

// /TEXT DECLARATION/

// STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int MAGIC = 2;
    private static final int UPGRADED_MAGIC = 1;

// /STAT DECLARATION/

    public EmbraceDarkness() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = MAGIC;
        magicNumber = baseMagicNumber;
        tags.add(AwakenSkillTag.CANT_AWAKEN);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (holdingCurse) {
            showEvokeValue = true;
            showEvokeOrbCount = 1;
        } else {
            showEvokeValue = false;
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FortitudePower(p, magicNumber), magicNumber));
        if (holdingCurse) {
            addToBot(new ChannelAction(new Dark()));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADED_MAGIC);
            initializeDescription();
        }
    }
}
