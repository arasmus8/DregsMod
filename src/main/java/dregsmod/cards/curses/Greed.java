package dregsmod.cards.curses;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCleansingCurse;

import static dregsmod.DregsMod.makeCardPath;

public class Greed extends AbstractCleansingCurse {

// TEXT DECLARATION

    public static final String ID = DregsMod.makeID(Greed.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Curse.png");
// Must have an image with the same NAME as the card in your image folder!

// /TEXT DECLARATION/

// STAT DECLARATION

    private static final CardTarget TARGET = CardTarget.NONE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = -2;
    private static final int CLEANSE_AMOUNT = 10;

// /STAT DECLARATION/

    public Greed() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TARGET, CLEANSE_AMOUNT);
    }

    @Override
    public void triggerWhenDrawn() {
        AbstractDungeon.player.loseGold(5);
        cleanseBy(1);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // actions when card is used.
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
    }
}