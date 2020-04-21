package dregsmod.cards.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.actions.KindleAction;
import dregsmod.actions.SealAndPerformAction;
import dregsmod.cards.AbstractCurseHoldingCard;
import dregsmod.cards.AwakenSkillTag;
import dregsmod.cards.UpgradeTextChangingCard;
import dregsmod.characters.Dregs;

import static dregsmod.DregsMod.makeCardPath;

public class Kindle extends AbstractCurseHoldingCard implements UpgradeTextChangingCard {

// TEXT DECLARATION

    public static final String ID = DregsMod.makeID(Kindle.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Skill.png");
// Must have an image with the same NAME as the card in your image folder!

// /TEXT DECLARATION/

// STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 0;

// /STAT DECLARATION/

    public Kindle() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        tags.add(AwakenSkillTag.CANT_AWAKEN);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded && holdingCurse) {
            addToBot(new SealAndPerformAction(1,
                    true,
                    p.hand,
                    card -> card.type == CardType.CURSE,
                    new KindleAction(this, true)));
        } else {
            addToBot(new KindleAction(this, false));
        }
    }

    @Override
    public String upgradePreviewText() {
        return diffText(CARD_STRINGS.DESCRIPTION, CARD_STRINGS.UPGRADE_DESCRIPTION);
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
