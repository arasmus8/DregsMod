package dregsmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static dregsmod.patches.enums.CustomCardTags.CLEANSE_CURSE;

public abstract class AbstractCleansingCurse extends CustomCard {
    public int cleanseAmount;
    public boolean isCleansed = false;
    private boolean exhausting = false;
    private AbstractCard cleansedCurse;

    public AbstractCleansingCurse(String id, String name, String img, int cost, String rawDescription, CardTarget target, int cleanseAmount) {
        super(id, name, img, cost, rawDescription, CardType.CURSE, CardColor.CURSE, CardRarity.CURSE, target);
        this.cleanseAmount = cleanseAmount;
        baseMagicNumber = magicNumber = cleanseAmount - misc;
        tags.add(CLEANSE_CURSE);
    }

    @Override
    public void triggerWhenDrawn() {
        if (magicNumber <= 0) {
            cleanseBy(0);
        }
    }

    public void cleanseBy(int amount) {
        if (cleanseAmount > misc + amount) {
            addToBot(new IncreaseMiscAction(uuid, misc, amount));
        } else if (!isCleansed) {
            AbstractPlayer p = AbstractDungeon.player;
            ArrayList<AbstractCard> instances = p.masterDeck.group.stream()
                    .filter(c -> c.uuid.equals(uuid))
                    .collect(Collectors.toCollection(ArrayList::new));
            instances.forEach(card -> p.masterDeck.removeCard(card));
            cleansedCurse = new CleansedCurse();
            p.masterDeck.addToTop(cleansedCurse.makeSameInstanceOf());
            this.purgeOnUse = true;
            for (AbstractCard c : p.hand.group) {
                if (c.uuid == uuid) {
                    ((AbstractCleansingCurse) c).isCleansed = true;
                }
            }
            for (AbstractCard c : p.discardPile.group) {
                if (c.uuid == uuid) {
                    ((AbstractCleansingCurse) c).isCleansed = true;
                }
            }
            for (AbstractCard c : p.drawPile.group) {
                if (c.uuid == uuid) {
                    ((AbstractCleansingCurse) c).isCleansed = true;
                }
            }
            for (AbstractCard c : p.limbo.group) {
                if (c.uuid == uuid) {
                    ((AbstractCleansingCurse) c).isCleansed = true;
                }
            }
            for (AbstractCard c : p.exhaustPile.group) {
                if (c.uuid == uuid) {
                    ((AbstractCleansingCurse) c).misc = 0;
                }
            }
            AbstractDungeon.player.hand.refreshHandLayout();
        }
    }

    @Override
    public void applyPowers() {
        baseMagicNumber = magicNumber = Math.max(0, cleanseAmount - misc);
        super.applyPowers();
        initializeDescription();
        if (isCleansed) {
            if (!exhausting) {
                exhausting = true;
                AbstractPlayer p = AbstractDungeon.player;
                if (p.hand.contains(this)) {
                    addToBot(new ExhaustSpecificCardAction(this, p.hand));
                } else if (p.drawPile.contains(this)) {
                    p.drawPile.removeCard(this);
                } else if (p.discardPile.contains(this)) {
                    p.discardPile.removeCard(this);
                }
                if (cleansedCurse != null) {
                    addToBot(new MakeTempCardInHandAction(cleansedCurse.makeSameInstanceOf(), true, true));
                }
                cleansedCurse = null;
            }
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCleansingCurse copy = (AbstractCleansingCurse) super.makeStatEquivalentCopy();
        copy.cleanseAmount = cleanseAmount;
        copy.isCleansed = isCleansed;
        copy.baseMagicNumber = copy.magicNumber = Math.max(0, cleanseAmount - misc);
        return copy;
    }
}
