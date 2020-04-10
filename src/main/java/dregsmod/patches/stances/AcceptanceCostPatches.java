package dregsmod.patches.stances;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import dregsmod.actions.AcceptanceEndTurnAction;
import dregsmod.stances.AcceptanceStance;
import javassist.CtBehavior;

public class AcceptanceCostPatches {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "draw",
            paramtypez = {int.class}
    )
    public static class AbstractPlayerDrawPatch {
        @SpireInsertPatch(
                locator = AbstractPlayerOnCardDrawLocator.class,
                localvars = {"c"}
        )
        public static void Insert(AbstractPlayer _instance, int numCards, AbstractCard c) {
            if (
                    _instance.stance.ID.equals(AcceptanceStance.STANCE_ID) &&
                            c.type == AbstractCard.CardType.ATTACK
            ) {
                if (c.costForTurn >= 0) {
                    c.setCostForTurn(c.costForTurn + 1);
                }
            }
        }
    }

    private static class AbstractPlayerOnCardDrawLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }

    @SpirePatch(
            clz = ShowCardAndAddToHandEffect.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, float.class, float.class}
    )
    public static class ShowCardAndAddToHandEffectConstructorOnePatch {
        public static void Prefix(ShowCardAndAddToHandEffect _instance, AbstractCard card, float offsetX, float offsetY) {
            if (AbstractDungeon.player.stance.ID.equals(AcceptanceStance.STANCE_ID) && card.type == AbstractCard.CardType.ATTACK) {
                if (card.costForTurn >= 0) {
                    card.setCostForTurn(card.costForTurn + 1);
                }
            }
        }
    }

    @SpirePatch(
            clz = ShowCardAndAddToHandEffect.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class}
    )
    public static class ShowCardAndAddToHandEffectConstructorTwoPatch {
        public static void Prefix(ShowCardAndAddToHandEffect _instance, AbstractCard card) {
            if (AbstractDungeon.player.stance.ID.equals(AcceptanceStance.STANCE_ID) && card.type == AbstractCard.CardType.ATTACK) {
                if (card.costForTurn >= 0) {
                    card.setCostForTurn(card.costForTurn + 1);
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractRoom.class,
            method = "endTurn"
    )
    public static class AbstractRoomEndTurnPatch {
        public static void Postfix(AbstractRoom _instance) {
            if (AbstractDungeon.player.stance.ID.equals(AcceptanceStance.STANCE_ID)) {
                AbstractDungeon.actionManager.addToBottom(new AcceptanceEndTurnAction());
            }
        }
    }
}
