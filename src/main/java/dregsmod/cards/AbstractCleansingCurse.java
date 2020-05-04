package dregsmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import dregsmod.CleansedCurseReward;
import dregsmod.patches.enums.CustomRewardItem;

import static dregsmod.patches.enums.CustomCardTags.CLEANSE_CURSE;

public abstract class AbstractCleansingCurse extends CustomCard {
    public int cleanseAmount;
    public boolean isCleansed = false;
    private boolean exhausting = false;

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
            p.masterDeck.group.removeIf(c -> c.uuid.equals(uuid));

            boolean rewardFound = false;
            for (RewardItem reward : AbstractDungeon.getCurrRoom().rewards) {
                if (reward.type == CustomRewardItem.CLEANSED_CURSE_REWARD) {
                    ((CleansedCurseReward) reward).incrementAmount(1);
                    rewardFound = true;
                }
            }
            if (!rewardFound) {
                AbstractDungeon.getCurrRoom().rewards.add(new CleansedCurseReward(1));
            }

            this.purgeOnUse = true;
            for (AbstractCard c : p.hand.group) {
                if (c.uuid == uuid) {
                    ((AbstractCleansingCurse) c).isCleansed = true;
                }
            }

            p.discardPile.group.removeIf(c -> c.uuid.equals(uuid));
            p.drawPile.group.removeIf(c -> c.uuid.equals(uuid));
            p.exhaustPile.group.removeIf(c -> c.uuid.equals(uuid));
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
