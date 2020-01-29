package dregsmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Optional;

import static dregsmod.patches.enums.CustomCardTags.CLEANSE_CURSE;

public abstract class AbstractCleansingCurse extends CustomCard {
    public int cleanseAmount;

    public AbstractCleansingCurse(String id, String name, String img, int cost, String rawDescription, CardTarget target, int cleanseAmount) {
        super(id, name, img, cost, rawDescription, CardType.CURSE, CardColor.CURSE, CardRarity.CURSE, target);
        this.cleanseAmount = cleanseAmount;
        baseMagicNumber = magicNumber = cleanseAmount - misc;
        tags.add(CLEANSE_CURSE);
    }

    public void cleanseBy(int amount) {
        if (cleanseAmount > misc + amount) {
            addToBot(new IncreaseMiscAction(uuid, misc, amount));
        } else {
            Optional<AbstractCard> original = AbstractDungeon.player.masterDeck.group.stream()
                    .filter(c -> c.uuid.equals(uuid))
                    .findFirst();
            original.ifPresent(card -> AbstractDungeon.player.masterDeck.removeCard(card));
            AbstractCard cleansedCurse = new CleansedCurse();
            AbstractDungeon.player.masterDeck.addToTop(cleansedCurse.makeSameInstanceOf());
            addToTop(new MakeTempCardInHandAction(cleansedCurse.makeSameInstanceOf()));
            addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
            AbstractDungeon.player.hand.refreshHandLayout();
        }
    }

    @Override
    public void applyPowers() {
        baseMagicNumber = magicNumber = cleanseAmount - misc;
        super.applyPowers();
        initializeDescription();
    }
}
