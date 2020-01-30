package dregsmod.patches.neow;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import dregsmod.characters.Dregs;
import dregsmod.relics.NeowsHatred;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.logging.Logger;

import static dregsmod.patches.enums.CustomNeowRewardDrawback.DREGS_DRAWBACK;

public class NeowPatches {
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("dregsmod:DregsNeow");
    private static final String[] DREGS_NEOW_TEXT = characterStrings.TEXT;

    @SuppressWarnings({"unchecked"})
    @SpirePatch(
            clz = NeowEvent.class,
            method = "miniBlessing"
    )
    public static class MiniBlessingPatch {
        @SpireInsertPatch(
                locator = MiniBlessingLocator.class
        )
        public static SpireReturn Insert(NeowEvent _instance) {
            if (AbstractDungeon.player.chosenClass == Dregs.Enums.DREGS) {
                ArrayList<NeowReward> rewards;
                rewards = (ArrayList<NeowReward>) ReflectionHacks.getPrivate(_instance, NeowEvent.class, "rewards");
                rewards.clear();
                rewards.add(new DregsReward(true));
                rewards.add(new DregsReward(false));
                _instance.roomEventText.clearRemainingOptions();
                _instance.roomEventText.updateDialogOption(0, rewards.get(0).optionLabel);
                _instance.roomEventText.addDialogOption(rewards.get(1).optionLabel);
                ReflectionHacks.setPrivate(_instance, NeowEvent.class, "screenNum", 3);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class MiniBlessingLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(RoomEventDialog.class, "clearRemainingOptions");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    @SpirePatch(
            clz = NeowEvent.class,
            method = "blessing"
    )
    public static class BlessingPatch {
        @SpireInsertPatch(
                locator = BlessingLocator.class
        )
        public static SpireReturn Insert(NeowEvent _instance) {
            if (AbstractDungeon.player.chosenClass == Dregs.Enums.DREGS) {
                ArrayList<NeowReward> rewards;
                rewards = (ArrayList<NeowReward>) ReflectionHacks.getPrivate(_instance, NeowEvent.class, "rewards");
                rewards.clear();
                rewards.add(new DregsReward(0));
                rewards.add(new DregsReward(1));
                rewards.add(new DregsReward(2));
                rewards.add(new DregsReward(3));
                _instance.roomEventText.clearRemainingOptions();
                _instance.roomEventText.updateDialogOption(0, rewards.get(0).optionLabel);
                _instance.roomEventText.addDialogOption(rewards.get(1).optionLabel);
                _instance.roomEventText.addDialogOption(rewards.get(2).optionLabel);
                _instance.roomEventText.addDialogOption(rewards.get(3).optionLabel);
                ReflectionHacks.setPrivate(_instance, NeowEvent.class, "screenNum", 3);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class BlessingLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(RoomEventDialog.class, "clearRemainingOptions");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = NeowReward.class,
            method = "activate"
    )
    public static class NeowRewardActivatePatch {
        public static void Prefix(NeowReward _instance) {
            if (_instance.drawback == DREGS_DRAWBACK) {
                _instance.drawback = NeowReward.NeowRewardDrawback.NONE;
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2f, Settings.HEIGHT / 2f, new NeowsHatred());
            }
        }
    }

    public static class DregsReward extends NeowReward {

        public DregsReward(boolean isFirst) {
            super(isFirst);
            drawback = DREGS_DRAWBACK;
            Logger logger = Logger.getLogger(DregsReward.class.getName());
            int category;
            if(isFirst) {
                type = NeowRewardType.THREE_CARDS;
                category = 0;
            } else {
                type = NeowRewardType.TEN_PERCENT_HP_BONUS;
                category = 3;
            }
            optionLabel = "" + DREGS_NEOW_TEXT[0] + DREGS_NEOW_TEXT[category + 1];
        }

        public DregsReward(int category) {
            super(category);
            drawback = DREGS_DRAWBACK;
            Logger logger = Logger.getLogger(DregsReward.class.getName());
            switch (category) {
                case 0:
                    type = NeowRewardType.THREE_CARDS;
                    break;
                case 1:
                    type = NeowRewardType.HUNDRED_GOLD;
                    break;
                case 2:
                    type = NeowRewardType.TRANSFORM_CARD;
                    break;
                case 3:
                    type = NeowRewardType.TEN_PERCENT_HP_BONUS;
                    break;
                default:
                    logger.warning("Invalid Reward Category " + category);
            }
            optionLabel = "" + DREGS_NEOW_TEXT[0] + DREGS_NEOW_TEXT[category + 1];
        }
    }
}
