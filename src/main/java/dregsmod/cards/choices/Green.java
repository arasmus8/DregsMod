package dregsmod.cards.choices;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.actions.GemHeartAction;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class Green extends AbstractCard {
    public static final String ID = DregsMod.makeID(Green.class.getSimpleName());
    public static final String IMG = "green/skill/burst";
    public static CardStrings CARD_STRINGS = languagePack.getCardStrings(ID);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.STATUS;
    public static final CardColor COLOR = CardColor.COLORLESS;

    public Green() {
        super(ID, CARD_STRINGS.NAME, IMG, -2, CARD_STRINGS.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }

    @Override
    public void onChoseThisOption() {
        addToBot(new GemHeartAction(CardColor.GREEN, upgraded));
    }

    @Override
    public void upgrade() {
        upgraded = true;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Green();
    }
}
