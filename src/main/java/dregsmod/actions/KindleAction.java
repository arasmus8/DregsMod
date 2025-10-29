package dregsmod.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MultiGroupSelectAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.sun.org.apache.xpath.internal.operations.Mult;
import dregsmod.DregsMod;
import dregsmod.cards.AwakenedMod;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KindleAction extends AbstractGameAction {

    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private final AbstractCard card;
    private final boolean returnToHand;
    public static final String ID = DregsMod.makeID(KindleAction.class.getSimpleName());

    public KindleAction(AbstractCard card, boolean returnToHand) {
        setValues(AbstractDungeon.player, AbstractDungeon.player);
        this.card = card;
        this.returnToHand = returnToHand;
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        isDone = true;
        Predicate<AbstractCard> notPlayedCard = (cardToCheck) -> cardToCheck != card;
        Predicate<AbstractCard> eligible = AwakenedMod.eligibleToAwaken.and(notPlayedCard);
        BiConsumer<List<AbstractCard>, Map<AbstractCard, CardGroup>> callback = (selectedCards, filteredSourceMap) -> {
            for (AbstractCard c : selectedCards) {
                addToTop(new CardAwokenAction(c, 1));
            }
        };
        addToTop(new MultiGroupSelectAction(TEXT[0], callback, 1, eligible, CardGroup.CardGroupType.HAND, CardGroup.CardGroupType.DISCARD_PILE, CardGroup.CardGroupType.DRAW_PILE));

        card.returnToHand = returnToHand;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
