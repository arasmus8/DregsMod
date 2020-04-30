package dregsmod.cards.curses;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCleansingCurse;
import dregsmod.cards.TriggerOnGainBlockCard;

import static dregsmod.DregsMod.makeCardPath;

public class WeakPoint extends AbstractCleansingCurse implements TriggerOnGainBlockCard {

// TEXT DECLARATION

    public static final String ID = DregsMod.makeID(WeakPoint.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("WeakPoint.png");
// Must have an image with the same NAME as the card in your image folder!

// /TEXT DECLARATION/

// STAT DECLARATION

    private static final CardTarget TARGET = CardTarget.NONE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = -2;
    private static final int CLEANSE_AMOUNT = 50;

// /STAT DECLARATION/

    public WeakPoint() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TARGET, CLEANSE_AMOUNT);
        selfRetain = true;
    }

    @Override
    public void triggerOnGainBlock(int blockAmount) {
        cleanseBy(blockAmount);
    }

    @Override
    public void onRetained() {
        AbstractMonster m = AbstractDungeon.getRandomMonster();
        if (!m.hasPower(ArtifactPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(m, m, new StrengthPower(m, 2), 2));
        }
        addToBot(new ApplyPowerAction(m, m, new LoseStrengthPower(m, 2), 2));
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