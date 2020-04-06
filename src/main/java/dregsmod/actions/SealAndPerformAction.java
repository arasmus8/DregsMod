package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import dregsmod.DregsMod;
import dregsmod.cards.uncommon.Guardian;
import dregsmod.patches.variables.CardSealed;
import dregsmod.powers.TriggerOnSealedPower;
import dregsmod.vfx.SealCardEffect;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SealAndPerformAction extends AbstractGameAction {
    public static ArrayList<AbstractCard> sealedCards = new ArrayList<>();
    private Predicate<AbstractCard> filterCriteria = null;
    private CardGroup group = null;
    private AbstractGameAction followUpAction;
    private boolean clearSealHistory;
    private boolean isRandom;
    private static AbstractPlayer p;
    private static final float DURATION;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public static final String ID = DregsMod.makeID(SealAndPerformAction.class.getSimpleName());

    public SealAndPerformAction(
            int amount,
            boolean random,
            AbstractGameAction action
    ) {
        p = AbstractDungeon.player;
        setValues(p, p, amount);
        actionType = ActionType.DISCARD;
        duration = DURATION;
        followUpAction = action;
        isRandom = random;
        clearSealHistory = true;
    }

    public SealAndPerformAction(int amount, AbstractGameAction action) {
        this(amount, false, action);
    }

    public SealAndPerformAction(
            int amount,
            boolean random,
            CardGroup group,
            Predicate<AbstractCard> filterCriteria,
            AbstractGameAction action
    ) {
       this(amount, random, action);
       this.group = group;
       this.filterCriteria = filterCriteria;
    }

    @Override
    public void update() {
        if (duration == DURATION) {
            if (clearSealHistory) {
                clearSealHistory = false;
                sealedCards.clear();
            }

            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                isDone = true;
                return;
            }

            if (group == null) {
                group = p.hand;
            }

            if(filterCriteria == null) {
                filterCriteria = abstractCard -> true;
            }
            ArrayList<AbstractCard> filteredList = group.group.stream().filter(filterCriteria).collect(Collectors.toCollection(ArrayList::new));

            if (filteredList.size() <= amount) {
                sealedCards.addAll(filteredList);
                filteredList.forEach(card -> {
                    CardSealed.isSealed.set(card, true);
                    AbstractDungeon.effectList.add(new SealCardEffect(card.makeStatEquivalentCopy()));
                    group.moveToDiscardPile(card);
                    card.triggerOnManualDiscard();
                    card.triggerOnExhaust();
                    if(card.cardID.equals(Guardian.ID)) {
                        ((Guardian)card).triggerOnSealed();
                    }
                    GameActionManager.incrementDiscard(false);
                });

                p.hand.applyPowers();
                endActionWithFollowUp();
                return;
            }

            if (!isRandom) {
                if (group == p.hand) {
                    if (amount < 0) {
                        AbstractDungeon.handCardSelectScreen.open(TEXT[0], 99, true, true);
                        p.hand.applyPowers();
                        tickDuration();
                        return;
                    }

                    if (p.hand.size() > amount) {
                        AbstractDungeon.handCardSelectScreen.open(TEXT[0], amount, false);
                        p.hand.applyPowers();
                        tickDuration();
                        return;
                    }
                } else {
                    CardGroup filtered = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    filtered.group.addAll(filteredList);
                    if (amount < 0) {
                        AbstractDungeon.gridSelectScreen.open(filtered, 99, true, TEXT[0]);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(filtered, amount, TEXT[0], false, false, false, false);
                    }
                    tickDuration();
                    return;
                }
            } else {

                AbstractCard card;
                CardGroup filtered = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                filtered.group.addAll(filteredList);
                for (int i = 0; i < amount; ++i) {
                    card = filtered.getRandomCard(AbstractDungeon.cardRandomRng);
                    filtered.removeCard(card);
                    AbstractDungeon.effectList.add(new SealCardEffect(card.makeStatEquivalentCopy()));
                    sealedCards.add(card);
                    CardSealed.isSealed.set(card, true);
                    group.moveToDiscardPile(card);
                    card.triggerOnManualDiscard();
                    card.triggerOnExhaust();
                    if(card.cardID.equals(Guardian.ID)) {
                        ((Guardian)card).triggerOnSealed();
                    }
                    GameActionManager.incrementDiscard(false);
                }
                endActionWithFollowUp();
            }
        }

        if (group == p.hand) {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    AbstractDungeon.effectList.add(new SealCardEffect(card.makeStatEquivalentCopy()));
                    sealedCards.add(card);
                    CardSealed.isSealed.set(card, true);
                    p.hand.moveToDiscardPile(card);
                    card.triggerOnManualDiscard();
                    card.triggerOnExhaust();
                    if(card.cardID.equals(Guardian.ID)) {
                        ((Guardian)card).triggerOnSealed();
                    }
                    GameActionManager.incrementDiscard(false);
                }
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                p.hand.applyPowers();
                endActionWithFollowUp();
            }
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                    AbstractDungeon.effectList.add(new SealCardEffect(card.makeStatEquivalentCopy()));
                    sealedCards.add(card);
                    CardSealed.isSealed.set(card, true);
                    group.moveToDiscardPile(card);
                    card.triggerOnManualDiscard();
                    card.triggerOnExhaust();
                    if(card.cardID.equals(Guardian.ID)) {
                        ((Guardian)card).triggerOnSealed();
                    }
                    GameActionManager.incrementDiscard(false);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                p.hand.refreshHandLayout();
                p.hand.applyPowers();
                endActionWithFollowUp();
            }
        }

        tickDuration();
    }

    private void endActionWithFollowUp() {
        isDone = true;
        sealedCards.forEach(card -> AbstractDungeon.player.powers.forEach(power -> {
            if (power instanceof TriggerOnSealedPower) {
                ((TriggerOnSealedPower) power).triggerOnSealed(card);
            }
        }));
        if (followUpAction != null) {
            addToTop(followUpAction);
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
        DURATION = Settings.ACTION_DUR_XFAST;
    }
}
