package dregsmod.cards.common;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.actions.GemShardAction;
import dregsmod.cards.AwakenSkillTag;
import dregsmod.cards.UpgradeTextChangingCard;
import dregsmod.characters.Dregs;

import static dregsmod.DregsMod.makeCardPath;

public class AmethystShard extends CustomCard implements UpgradeTextChangingCard {

// TEXT DECLARATION

    public static final String ID = DregsMod.makeID(AmethystShard.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("AmethystShard.png");
// Must have an image with the same NAME as the card in your image folder!

// /TEXT DECLARATION/

// STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 0;

// /STAT DECLARATION/

    public AmethystShard() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
        tags.add(AwakenSkillTag.CANT_AWAKEN);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GemShardAction(CardColor.PURPLE, upgraded));
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
