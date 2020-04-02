package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.*;

public class AsylumChannelAction extends AbstractGameAction {
    public AsylumChannelAction() {
        setValues(AbstractDungeon.player, AbstractDungeon.player);
        duration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        int healAmount = 0;
        for (AbstractCard card : SealAndPerformAction.sealedCards) {
            AbstractOrb orbToChannel;
            switch (card.rarity) {
                case BASIC: //fallthrough
                    if (card.type == AbstractCard.CardType.CURSE) {
                        orbToChannel = new Dark();
                        break;
                    }
                case SPECIAL: //fallthrough
                    if (card.type == AbstractCard.CardType.CURSE) {
                        orbToChannel = new Dark();
                        break;
                    }
                case COMMON:
                    orbToChannel = new Lightning();
                    break;
                case UNCOMMON:
                    orbToChannel = new Frost();
                    break;
                case RARE:
                    orbToChannel = new Plasma();
                    break;
                case CURSE:
                    orbToChannel = new Dark();
                    break;
                default:
                    orbToChannel = new Lightning();
                    break;
            }
            addToBot(new ChannelAction(orbToChannel));
        }
        isDone = true;
    }
}
