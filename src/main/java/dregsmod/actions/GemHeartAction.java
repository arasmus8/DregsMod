package dregsmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class GemHeartAction extends AbstractGameAction {
    private CardColor color;
    private boolean retrieveCard = false;
    private boolean upgraded;

    public GemHeartAction(CardColor color, boolean upgraded) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.upgraded = upgraded;
        this.color = color;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(this.generateCardChoices(), CardRewardScreen.TEXT[1], true);
        } else {
            if (!this.retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    if (this.upgraded) {
                        disCard.setCostForTurn(0);
                    }

                    disCard.current_x = -1000.0F * Settings.scale;
                    if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                    } else {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                    }

                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                }

                this.retrieveCard = true;
            }

        }
        this.tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> cards;

        AbstractCard.CardRarity cardRarity = AbstractCard.CardRarity.UNCOMMON;

        CardLibrary.LibraryType type = CardLibrary.LibraryType.COLORLESS;
        switch (color) {
            case RED:
                type = CardLibrary.LibraryType.RED;
                break;
            case GREEN:
                type = CardLibrary.LibraryType.GREEN;
                break;
            case BLUE:
                type = CardLibrary.LibraryType.BLUE;
                break;
            case PURPLE:
                type = CardLibrary.LibraryType.PURPLE;
                break;
        }
        cards = CardLibrary.getCardList(type).stream().filter(card -> card.rarity == cardRarity).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(cards, AbstractDungeon.cardRandomRng.random);
        return cards.stream().limit(3).map(AbstractCard::makeCopy).collect(Collectors.toCollection(ArrayList::new));
    }
}
