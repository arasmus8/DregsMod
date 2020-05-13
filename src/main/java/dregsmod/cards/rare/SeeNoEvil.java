package dregsmod.cards.rare;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.actions.SealAndPerformAction;
import dregsmod.cards.AwakenSkillTag;
import dregsmod.cards.UpgradeTextChangingCard;
import dregsmod.characters.Dregs;

import static dregsmod.DregsMod.makeCardPath;

public class SeeNoEvil extends CustomCard implements UpgradeTextChangingCard {

// TEXT DECLARATION

    public static final String ID = DregsMod.makeID(SeeNoEvil.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("SeeNoEvil.png");
// Must have an image with the same NAME as the card in your image folder!

// /TEXT DECLARATION/

// STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 0;
// /STAT DECLARATION/

    public SeeNoEvil() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
        tags.add(AwakenSkillTag.CANT_AWAKEN);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SealAndPerformAction(
                -1,
                false,
                p.drawPile,
                null,
                null
        ));
    }

    @Override
    public String upgradePreviewText() {
        return diffText(CARD_STRINGS.DESCRIPTION, CARD_STRINGS.UPGRADE_DESCRIPTION);
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            exhaust = false;
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            upgradeName();
            initializeDescription();
        }
    }
}
