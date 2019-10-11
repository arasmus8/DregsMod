package potionbrewer.cards.option;

import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import potionbrewer.PotionbrewerMod;
import potionbrewer.orbs.Lightning;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class ChooseLightning extends AbstractCard {
    public static String ID = PotionbrewerMod.makeID(ChooseLightning.class.getSimpleName());
    public static CardStrings CARD_STRINGS = languagePack.getCardStrings(ID);

    public ChooseLightning() {
        super(ID, CARD_STRINGS.NAME, "blue/skill/zap", -2, CARD_STRINGS.DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.NONE);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.onChoseThisOption();
    }

    @Override
    public void onChoseThisOption() {
        this.addToBot(new ChannelAction(new Lightning()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ChooseLightning();
    }
}
