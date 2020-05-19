package dregsmod;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import dregsmod.patches.enums.CustomRewardItem;
import dregsmod.util.TextureLoader;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static dregsmod.cards.DregsCardTags.CLEANSE_REWARD;

public class CleansedCurseReward extends CustomReward {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public static final String ID = DregsMod.makeID(CleansedCurseReward.class.getSimpleName());

    private static final Texture img;
    public int amount;
    public boolean cardSelected;

    private static final int RARE_CHANCE;
    private static final int UNCOMMON_CHANCE;
    private static final int POWER_CHANCE;
    private static final int SKILL_CHANCE;

    public CleansedCurseReward(int amount) {
        super(img, TEXT[4], CustomRewardItem.CLEANSED_CURSE_REWARD);
        this.amount = amount;
        this.cardSelected = false;
    }

    public void incrementAmount(int amount) {
        this.amount += amount;
        this.text = TEXT[4];
    }

    @Override
    public boolean claimReward() {
        if (cardSelected) {
            AbstractDungeon.dynamicBanner.show = true;
            return true;
        }
        CardGroup randomCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        while (randomCards.size() < 5) {
            AbstractCard randomCard = getRandomCardOfColor(CardLibrary.LibraryType.RED);
            if (!randomCards.contains(randomCard)) {
                randomCards.addToTop(randomCard);
            }
        }
        while (randomCards.size() < 10) {
            AbstractCard randomCard = getRandomCardOfColor(CardLibrary.LibraryType.GREEN);
            if (!randomCards.contains(randomCard)) {
                randomCards.addToTop(randomCard);
            }
        }
        while (randomCards.size() < 15) {
            AbstractCard randomCard = getRandomCardOfColor(CardLibrary.LibraryType.BLUE);
            if (!randomCards.contains(randomCard)) {
                randomCards.addToTop(randomCard);
            }
        }
        while (randomCards.size() < 20) {
            AbstractCard randomCard = getRandomCardOfColor(CardLibrary.LibraryType.PURPLE);
            if (!randomCards.contains(randomCard)) {
                randomCards.addToTop(randomCard);
            }
        }
        randomCards.group.forEach(c -> {
        });
        CardGroup cardSelection = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        cardSelection.group.addAll(randomCards.group.stream().map(c -> {
            AbstractCard cpy = c.makeCopy();
            cpy.tags.add(CLEANSE_REWARD);
            AbstractDungeon.player.relics.forEach(r -> r.onPreviewObtainCard(cpy));
            return cpy;
        }).collect(Collectors.toList()));
        AbstractDungeon.gridSelectScreen.open(cardSelection, amount, TEXT[0] + amount + (amount == 1 ? TEXT[1] : TEXT[2]) + TEXT[3], false, false, false, false);
        AbstractDungeon.dynamicBanner.hide();
        return false;
    }

    @Override
    public void update() {
        super.update();
        if (!isDone && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                AbstractDungeon.effectsQueue.add(new FastCardObtainEffect(card, card.current_x, card.current_y));
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            isDone = true;
            cardSelected = true;
        }
    }

    private AbstractCard.CardType rollCardType() {
        int chance = DregsMod.cleansedCurseRng.random(99);
        if (chance < POWER_CHANCE) {
            return AbstractCard.CardType.POWER;
        } else if (chance < POWER_CHANCE + SKILL_CHANCE) {
            return AbstractCard.CardType.SKILL;
        } else {
            return AbstractCard.CardType.ATTACK;
        }
    }

    private AbstractCard.CardRarity rollCardRarity() {
        int chance = DregsMod.cleansedCurseRng.random(99);
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
        return cards.get(DregsMod.cleansedCurseRng.random(cards.size() - 1));
    }

    static {
        img = TextureLoader.getTexture("dregsmodResources/images/reward.png");
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
        RARE_CHANCE = 15;
        UNCOMMON_CHANCE = 30;
        POWER_CHANCE = 15;
        SKILL_CHANCE = 50;
    }
}
