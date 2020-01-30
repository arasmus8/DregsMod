package dregsmod.patches.neow;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import dregsmod.characters.Dregs;
import dregsmod.relics.NeowsHatred;

import java.util.ArrayList;
import java.util.logging.Logger;

public class NeowPatches {
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("dregsmod:Neow");
    private static final String[] TEXT = characterStrings.TEXT;

    @SpirePatch(
            clz = NeowEvent.class,
            method = "miniBlessing"
    )
    public static class MiniBlessingPatch {
        public static void Postfix(NeowEvent _instance) {
            if (AbstractDungeon.player.chosenClass == Dregs.Enums.DREGS) {
                ArrayList<NeowReward> rewards;
                rewards = (ArrayList<NeowReward>) ReflectionHacks.getPrivate(_instance, NeowEvent.class, "rewards");
                rewards.clear();
                rewards.add(new DregsReward(0));
                rewards.add(new DregsReward(1));
            }
        }
    }

    @SpirePatch(
            clz = NeowEvent.class,
            method = "miniBlessing"
    )
    public static class BlessingPatch {
        public static void Postfix(NeowEvent _instance) {
            if (AbstractDungeon.player.chosenClass == Dregs.Enums.DREGS) {
                ArrayList<NeowReward> rewards;
                rewards = (ArrayList<NeowReward>) ReflectionHacks.getPrivate(_instance, NeowEvent.class, "rewards");
                rewards.clear();
                rewards.add(new DregsReward(0));
                rewards.add(new DregsReward(1));
                rewards.add(new DregsReward(2));
                rewards.add(new DregsReward(3));
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

    @SpireEnum
    public static NeowReward.NeowRewardDrawback DREGS_DRAWBACK;

    public static class DregsReward extends NeowReward {

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
            optionLabel = "" + TEXT[0] + TEXT[category + 1];
        }
    }
}
