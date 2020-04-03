package dregsmod.cards.curses;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCleansingCurse;

import java.util.ArrayList;

import static dregsmod.DregsMod.makeCardPath;

public class Infection extends AbstractCleansingCurse {

// TEXT DECLARATION

    public static final String ID = DregsMod.makeID(Infection.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Curse.png");
// Must have an image with the same NAME as the card in your image folder!

// /TEXT DECLARATION/

// STAT DECLARATION

    private static final CardTarget TARGET = CardTarget.SELF;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = 1;
    private static final int CLEANSE_AMOUNT = 12;

// /STAT DECLARATION/

    public Infection() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TARGET, CLEANSE_AMOUNT);
        selfRetain = true;
        exhaust = true;
    }

    @Override
    public void onRetained() {
        if (!isCleansed) {
            cleanseBy(1);
            if (isCleansed) {
                AbstractPlayer p = AbstractDungeon.player;
                ArrayList<AbstractCard> withSameUUID = new ArrayList<>();
                for (AbstractCard card : p.drawPile.group) {
                    if (card.uuid.equals(this.uuid)) {
                        withSameUUID.add(card);
                    }
                }
                for (AbstractCard card : p.discardPile.group) {
                    if (card.uuid.equals(this.uuid)) {
                        withSameUUID.add(card);
                    }
                }
                for (AbstractCard card : withSameUUID) {
                    card.applyPowers();
                }
            } else {
                addToBot(new VFXAction(new ShowCardAndAddToDrawPileEffect(this.makeSameInstanceOf(), true, false)));
            }
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