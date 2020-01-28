package dregsmod.actions;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.random.Random;
import dregsmod.DregsMod;

import java.util.ArrayList;
import java.util.Optional;

public class CleansedCurseAction extends AbstractGameAction implements CustomSavable<Integer> {

    private static Random rng;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public static final String ID = DregsMod.makeID(CleansedCurseAction.class.getSimpleName());
    private AbstractCard cardToReplace;

    private static final int RARE_CHANCE;
    private static final int UNCOMMON_CHANCE;
    private static final int POWER_CHANCE;
    private static final int SKILL_CHANCE;

    public CleansedCurseAction(AbstractCard cardToReplace) {
        this.cardToReplace = cardToReplace;
        setValues(AbstractDungeon.player, AbstractDungeon.player);
        duration = startDuration = Settings.ACTION_DUR_XFAST;
        actionType = ActionType.CARD_MANIPULATION;
        if (cardToReplace != null) {
            cardToReplace.purgeOnUse = true;
            AbstractDungeon.player.limbo.addToTop(cardToReplace);
            Optional<AbstractCard> original = AbstractDungeon.player.masterDeck.group.stream()
                    .filter(c -> c.uuid.equals(cardToReplace.uuid))
                    .findFirst();
            original.ifPresent(card -> AbstractDungeon.player.masterDeck.removeCard(card));
        }
    }

    private AbstractCard.CardType rollCardType() {
        int chance = rng.random(99);
        if (chance < POWER_CHANCE) {
            return AbstractCard.CardType.POWER;
        } else if (chance < POWER_CHANCE + SKILL_CHANCE) {
            return AbstractCard.CardType.SKILL;
        } else {
            return AbstractCard.CardType.ATTACK;
        }
    }

    private AbstractCard.CardRarity rollCardRarity() {
        int chance = rng.random(99);
        if (chance < RARE_CHANCE) {
            return AbstractCard.CardRarity.RARE;
        } else if (chance < RARE_CHANCE + UNCOMMON_CHANCE) {
            return AbstractCard.CardRarity.UNCOMMON;
        } else {
            return AbstractCard.CardRarity.COMMON;
        }
    }

    private AbstractCard getRandomCardOfColor(CardLibrary.LibraryType libraryType) {
        ArrayList<AbstractCard> cards = CardLibrary.getCardList(libraryType);
        AbstractCard.CardType cardType = rollCardType();
        if (cardType != AbstractCard.CardType.POWER) {
            AbstractCard.CardRarity rarity = rollCardRarity();
            cards.removeIf(card -> card.type != cardType || card.rarity != rarity);
        } else {
            cards.removeIf(card -> card.type != cardType);
        }
        return cards.get(rng.random(cards.size() - 1));
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            CardGroup randomCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (int i = 0; i < 5; ++i) {
                randomCards.addToTop(getRandomCardOfColor(CardLibrary.LibraryType.RED));
            }
            for (int i = 0; i < 5; ++i) {
                randomCards.addToTop(getRandomCardOfColor(CardLibrary.LibraryType.GREEN));
            }
            for (int i = 0; i < 5; ++i) {
                randomCards.addToTop(getRandomCardOfColor(CardLibrary.LibraryType.BLUE));
            }
            for (int i = 0; i < 5; ++i) {
                randomCards.addToTop(getRandomCardOfColor(CardLibrary.LibraryType.PURPLE));
            }
            AbstractDungeon.gridSelectScreen.open(randomCards, 1, TEXT[0], false, false, false, false);
        } else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractPlayer p = AbstractDungeon.player;

            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                addToBot(new MakeTempCardInHandAction(card.makeCopy()));
                p.masterDeck.addToTop(card);
                card.lighten(false);
                card.unhover();
                p.drawPile.moveToDeck(card, false);
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
        }
        tickDuration();
    }

    @Override
    public Integer onSave() {
        return rng.counter;
    }

    @Override
    public void onLoad(Integer value) {
        int randomCount = Optional.ofNullable(value).orElse(0);
        rng = new Random(Settings.seed, randomCount);
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
        RARE_CHANCE = 15;
        UNCOMMON_CHANCE = 30;
        POWER_CHANCE = 15;
        SKILL_CHANCE = 50;
    }
}
