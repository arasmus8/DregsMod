package dregsmod.cards.uncommon;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.characters.Dregs;

import static dregsmod.DregsMod.makeCardPath;

public class HardLuck extends CustomCard {

// TEXT DECLARATION

    public static final String ID = DregsMod.makeID(HardLuck.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Skill.png");
// Must have an image with the same NAME as the card in your image folder!

// /TEXT DECLARATION/

// STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 6;
    private static final int UPGRADED_COST = 5;

    private static final int BLOCK = 13;

// /STAT DECLARATION/

    public HardLuck() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        configureCostsOnNewCard();
    }

    public void configureCostsOnNewCard() {
        if (CardCrawlGame.dungeon != null) {
            int curseCount = (int) AbstractDungeon.player.masterDeck.group.stream()
                    .filter(card -> card.type == CardType.CURSE)
                    .count();
            updateCost(-curseCount);
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            configureCostsOnNewCard();
            initializeDescription();
        }
    }
}
