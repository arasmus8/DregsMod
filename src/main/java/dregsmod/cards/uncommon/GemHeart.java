package dregsmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.cards.DregsCardTags;
import dregsmod.cards.choices.Blue;
import dregsmod.cards.choices.Green;
import dregsmod.cards.choices.Purple;
import dregsmod.cards.choices.Red;
import dregsmod.characters.Dregs;

import java.util.ArrayList;

public class GemHeart extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(GemHeart.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;

    public GemHeart() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR, DregsCardTags.CANT_AWAKEN);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new Red());
        choices.add(new Green());
        choices.add(new Blue());
        choices.add(new Purple());
        if (upgraded) {
            choices.forEach(AbstractCard::upgrade);
        }
        addToBot(new ChooseOneAction(choices));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
