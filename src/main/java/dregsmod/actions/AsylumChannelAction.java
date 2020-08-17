package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.orbs.Plasma;
import dregsmod.orbs.DregOrb;
import dregsmod.orbs.Sludge;

public class AsylumChannelAction extends AbstractGameAction {
    public AsylumChannelAction() {
        setValues(AbstractDungeon.player, AbstractDungeon.player);
        duration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        for (AbstractCard card : SealAndPerformAction.sealedCards) {
            AbstractOrb orbToChannel;
            if (card.type == AbstractCard.CardType.CURSE) {
                orbToChannel = new DregOrb();
            } else if (card.type == AbstractCard.CardType.STATUS) {
                orbToChannel = new Sludge();
            } else {
                switch (card.rarity) {
                    case UNCOMMON:
                        orbToChannel = new Frost();
                        break;
                    case RARE:
                        orbToChannel = new Plasma();
                        break;
                    case CURSE:
                        orbToChannel = new DregOrb();
                        break;
                    case BASIC: //fallthrough
                    case SPECIAL: //fallthrough
                    case COMMON:
                    default:
                        orbToChannel = new Lightning();
                        break;
                }
            }
            addToBot(new ChannelAction(orbToChannel));
        }
        isDone = true;
    }
}
