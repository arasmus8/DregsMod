package dregsmod.cards.curses;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.actions.SealCardAction;
import dregsmod.cards.AbstractCurseHoldingCard;
import dregsmod.characters.Dregs;

import java.util.Optional;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static dregsmod.DregsMod.makeCardPath;

public class Jealousy extends AbstractCurseHoldingCard {

// TEXT DECLARATION

    public static final String ID = DregsMod.makeID(Jealousy.class.getSimpleName());
    public static final String IMG = makeCardPath("Curse.png");
    public static CardStrings CARD_STRINGS = languagePack.getCardStrings(ID);
// Must have an image with the same NAME as the card in your image folder!

// /TEXT DECLARATION/

// STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;

// /STAT DECLARATION/


    public Jealousy() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (holdingCurse) {
            Optional<AbstractCard> curse = p.hand.group.stream()
                    .filter(card -> card.type == CardType.CURSE)
                    .filter(card -> card != this)
                    .limit(1)
                    .findFirst();
            curse.ifPresent(card -> addToBot(new SealCardAction(card)));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return holdingCurse;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
    }
}
