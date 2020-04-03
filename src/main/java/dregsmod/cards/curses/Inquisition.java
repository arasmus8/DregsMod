package dregsmod.cards.curses;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCleansingCurse;

import static dregsmod.DregsMod.makeCardPath;

public class Inquisition extends AbstractCleansingCurse {

// TEXT DECLARATION

    public static final String ID = DregsMod.makeID(Inquisition.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Curse.png");
// Must have an image with the same NAME as the card in your image folder!

// /TEXT DECLARATION/

// STAT DECLARATION

    private static final CardTarget TARGET = CardTarget.NONE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = -2;
    private static final int CLEANSE_AMOUNT = 5;

// /STAT DECLARATION/

    public Inquisition() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TARGET, CLEANSE_AMOUNT);
        selfRetain = true;
    }

    @Override
    public void triggerOnExhaust() {
        cleanseBy(1);
    }

    @Override
    public void onRetained() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, 1, true), 1));
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            addToBot(new ApplyPowerAction(m, m, new VulnerablePower(m, 1, true), 1));
        }
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