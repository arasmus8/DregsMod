package dregsmod.cards;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import dregsmod.CleansedCurseReward;
import dregsmod.patches.enums.CustomRewardItem;

import static dregsmod.cards.DregsCardTags.CLEANSE_CURSE;

public abstract class AbstractCleansingCurse extends AbstractDregsCard {
    public int cleanseAmount;
    public boolean isCleansed = false;

    public AbstractCleansingCurse(String id, int cost, CardTarget target, int cleanseAmount) {
        super(id, cost, CardType.CURSE, CardRarity.SPECIAL, target, CardColor.CURSE, CLEANSE_CURSE);
        this.cleanseAmount = cleanseAmount;
        baseMagicNumber = magicNumber = cleanseAmount - misc;
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
            isCleansed = true;
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
            p.hand.group.stream().filter(c -> c.uuid.equals(uuid)).forEach(c -> addToBot(new ExhaustSpecificCardAction(c, p.hand)));

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
