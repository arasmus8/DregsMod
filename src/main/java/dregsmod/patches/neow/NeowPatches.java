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

@SuppressWarnings("unused")
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
        public static SpireReturn<Void> Insert(NeowEvent _instance) {
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
        public static SpireReturn<Void> Insert(NeowEvent _instance) {
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

        private static final CharacterStrings characterStrings;
        public static final String[] TEXT;
        private static final String[] UNIQUE_REWARDS;

        public DregsReward(boolean isFirst) {
            super(isFirst);
            drawback = DREGS_DRAWBACK;
            Logger logger = Logger.getLogger(DregsReward.class.getName());
            NeowReward.NeowRewardDef reward;
            if (isFirst) {
                type = NeowRewardType.HUNDRED_GOLD;
                reward = new NeowReward.NeowRewardDef(NeowRewardType.HUNDRED_GOLD, TEXT[8] + 100 + TEXT[9]);
            } else {
                reward = new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.TEN_PERCENT_HP_BONUS, TEXT[7] +
                        (int) ((float) AbstractDungeon.player.maxHealth * 0.1F) + " ]");
            }

            this.type = reward.type;
            optionLabel = "" + DREGS_NEOW_TEXT[0] + reward.desc;
        }

        public DregsReward(int category) {
            super(category);
            drawback = DREGS_DRAWBACK;
            Logger logger = Logger.getLogger(DregsReward.class.getName());
            ArrayList<NeowReward.NeowRewardDef> rewardOptions = new ArrayList<>();
            switch (category) {
                case 0:
                    rewardOptions.add(new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.THREE_CARDS, TEXT[0]));
                    rewardOptions.add(new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.ONE_RANDOM_RARE_CARD, TEXT[1]));
                    rewardOptions.add(new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.UPGRADE_CARD, TEXT[3]));
                    rewardOptions.add(new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.RANDOM_COLORLESS, TEXT[30]));
                    break;
                case 1:
                    rewardOptions.add(new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.THREE_SMALL_POTIONS, TEXT[5]));
                    rewardOptions.add(new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.RANDOM_COMMON_RELIC, TEXT[6]));
                    rewardOptions.add(new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.TEN_PERCENT_HP_BONUS, TEXT[7] +
                            (int) ((float) AbstractDungeon.player.maxHealth * 0.1F) +
                            " ]"));
                    rewardOptions.add(new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.HUNDRED_GOLD, TEXT[8] + 100 + TEXT[9]));
                    break;
                case 2:
                    rewardOptions.add(new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.REMOVE_TWO, " [ " + TEXT[10]));
                    rewardOptions.add(new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.ONE_RARE_RELIC, " [ " + TEXT[11]));
                    rewardOptions.add(new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.THREE_RARE_CARDS, " [ " + TEXT[12]));
                    rewardOptions.add(new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.TRANSFORM_TWO_CARDS, " [ " + TEXT[15]));
                    break;
                case 3:
                    rewardOptions.add(new NeowReward.NeowRewardDef(NeowReward.NeowRewardType.BOSS_RELIC, UNIQUE_REWARDS[0]));
                    break;
                default:
                    logger.warning("Invalid Reward Category " + category);
            }
            NeowReward.NeowRewardDef reward = rewardOptions.get(NeowEvent.rng.random(0, rewardOptions.size() - 1));
            type = reward.type;
            optionLabel = "" + DREGS_NEOW_TEXT[0] + reward.desc;
        }

        static {
            characterStrings = CardCrawlGame.languagePack.getCharacterString("Neow Reward");
            TEXT = characterStrings.TEXT;
            UNIQUE_REWARDS = characterStrings.UNIQUE_REWARDS;
        }
    }
}
