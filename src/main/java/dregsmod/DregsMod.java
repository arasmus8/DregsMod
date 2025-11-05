package dregsmod;

import basemod.*;
import basemod.abstracts.CustomSavable;
import basemod.devcommands.ConsoleCommand;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.characters.Dregs;
import dregsmod.patches.enums.CustomRewardItem;
import dregsmod.potions.InsightPotion;
import dregsmod.relics.*;
import dregsmod.util.AssetLoader;
import dregsmod.util.TextureLoader;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpireInitializer
public class DregsMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        StartGameSubscriber,
        PostInitializeSubscriber,
        CustomSavable<Integer> {

    private static String modID;

    private static final Logger logger = Logger.getLogger(DregsMod.class.getName());

    public static SpireConfig config;
    private static boolean globalRelics = false;

    private static final String MODNAME = "TheDregs";
    private static final String AUTHOR = "NotInTheFace";
    private static final String DESCRIPTION = "A custom character with Curse synergies." +
            "\n- Cleanse special curses to collect cards from other classes" +
            "\n- Curse your enemies to add special curses to your deck" +
            "\n- try not to die";

    public static final Color DREGS_BLACK = CardHelper.getColor(0, 20, 30);

    private static final String ATTACK_BLACK_BG = "dregsmodResources/images/512/bg_attack.png";
    private static final String SKILL_BLACK_BG = "dregsmodResources/images/512/bg_skill.png";
    private static final String POWER_BLACK_BG = "dregsmodResources/images/512/bg_power.png";

    private static final String ENERGY_ORB_DREGS = "dregsmodResources/images/512/card_dregs_orb.png";
    public static final String CARD_ENERGY_ORB = "dregsmodResources/images/512/card_small_orb.png";

    private static final String ATTACK_DREGS_PORTRAIT = "dregsmodResources/images/1024/bg_attack.png";
    private static final String SKILL_DREGS_PORTRAIT = "dregsmodResources/images/1024/bg_skill.png";
    private static final String POWER_DREGS_PORTRAIT = "dregsmodResources/images/1024/bg_power.png";
    private static final String ENERGY_ORB_DREGS_PORTRAIT = "dregsmodResources/images/1024/card_dregs_orb.png";

    private static final String DREGS_CHARACTER_BUTTON_PNG = "dregsmodResources/images/charSelect/CharacterButton.png";
    private static final String CHARACTER_SELECT_PORTRAIT = "dregsmodResources/images/charSelect/CharacterSelect.png";
    public static final String DREGS_SHOULDER = "dregsmodResources/images/char/dregs/shoulder.png";
    public static final String DREGS_SHOULDER_2 = "dregsmodResources/images/char/dregs/shoulder2.png";
    public static final String DREGS_CORPSE = "dregsmodResources/images/char/dregs/corpse.png";

    public static final String BADGE_IMAGE = "dregsmodResources/images/Badge.png";

    public static final String DREGS_SKELETON_ATLAS = "dregsmodResources/images/char/dregs/skeleton.atlas";
    public static final String DREGS_SKELETON_JSON = "dregsmodResources/images/char/dregs/skeleton.json";

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    public static String assetPath(String path) {
        return "dregsmodAssets/" + path;
    }

    public static AssetLoader assets = new AssetLoader();

    public static Map<String, Texture> uiTextures = new HashMap<>();
    public static ArrayList<AbstractCard> postSealedCards = new ArrayList<>();
    public static boolean keywordsSetup = false;

    public DregsMod() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);


        modID = "dregsmod";

        logger.info("Done subscribing");

        logger.info("Creating the color " + Dregs.Enums.COLOR_BLACK.toString());

        BaseMod.addColor(Dregs.Enums.COLOR_BLACK, DREGS_BLACK, DREGS_BLACK, DREGS_BLACK,
                DREGS_BLACK, DREGS_BLACK, DREGS_BLACK, DREGS_BLACK,
                ATTACK_BLACK_BG, SKILL_BLACK_BG, POWER_BLACK_BG, ENERGY_ORB_DREGS,
                ATTACK_DREGS_PORTRAIT, SKILL_DREGS_PORTRAIT, POWER_DREGS_PORTRAIT,
                ENERGY_ORB_DREGS_PORTRAIT, CARD_ENERGY_ORB);

        logger.info("Done creating the color");


        // Save/Load fields
        BaseMod.addSaveField(makeID("cleansedCurseRewardRng"), this);

        logger.info("Done adding mod savable fields");
    }

    public static String getModID() {
        return modID;
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        DregsMod dregsMod = new DregsMod();
        try {
            config = new SpireConfig("DregsMod", "dregsModConfig");
            config.load();
            globalRelics = config.getBool("globalRelics");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading settings from config", e);
        }
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + Dregs.Enums.DREGS.toString());

        BaseMod.addCharacter(new Dregs("Dregs", Dregs.Enums.DREGS),
                DREGS_CHARACTER_BUTTON_PNG, CHARACTER_SELECT_PORTRAIT, Dregs.Enums.DREGS);

        receiveEditPotions();
        logger.info("Added " + Dregs.Enums.DREGS.toString());
    }

    private void saveSettings() {
        try {
            // And based on that boolean, set the settings and save them
            config = new SpireConfig("DregsMod", "dregsModConfig");
            config.load();
            config.setBool("globalRelics", globalRelics);
            config.save();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving settings to config", e);
        }
    }

    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");


        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        logger.info("Adding mod settings");

        try {
            config = new SpireConfig("DregsMod", "dregsModConfig");
            config.load();
            globalRelics = config.getBool("globalRelics");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading settings from config", e);
        }
        logger.info("Done adding mod settings");

        UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("Settings"));

        ModPanel panel = new ModPanel();

        ModLabeledToggleButton toggleGlobalRelicsButton = new ModLabeledToggleButton(uiStrings.TEXT[0],
                375.0f, 500.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                globalRelics,
                panel,
                (label) -> {
                },
                (button) -> {
                    globalRelics = button.enabled;
                    saveSettings();
                });
        panel.addUIElement(toggleGlobalRelicsButton);

        panel.addUIElement(
                new ModLabel("Thanks for trying this character! I hope you like it.",
                        350.0F,
                        700.0F,
                        Settings.CREAM_COLOR,
                        FontHelper.charDescFont,
                        panel,
                        (b) -> {
                        }
                )
        );

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, panel);

        logger.info("Done loading badge Image and mod options");

        // UI / VFX preload

        Texture cardSeal = TextureLoader.getTexture("dregsmodResources/images/ui/card_seal.png");
        Texture cardSealBottom = TextureLoader.getTexture("dregsmodResources/images/ui/card_seal_bottom.png");
        Texture cardSealTop = TextureLoader.getTexture("dregsmodResources/images/ui/card_seal_top.png");

        uiTextures.put("cardSeal", cardSeal);
        uiTextures.put("cardSealBottom", cardSealBottom);
        uiTextures.put("cardSealTop", cardSealTop);

        // Console Commands
        ConsoleCommand.addCommand("cleanse", CleanseCursesConsoleCommand.class);

        // Events

        // Rewards
        BaseMod.registerCustomReward(CustomRewardItem.CLEANSED_CURSE_REWARD,
                (rewardSave) -> new CleansedCurseReward(rewardSave.amount),
                (reward) -> new RewardSave(reward.type.toString(), CleansedCurseReward.ID, ((CleansedCurseReward) reward).amount, 0));
    }

    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        BaseMod.addPotion(InsightPotion.class, InsightPotion.LIQUID_COLOR, InsightPotion.HYBRID_COLOR, InsightPotion.SPOTS_COLOR, InsightPotion.POTION_ID, Dregs.Enums.DREGS);

        logger.info("Done editing potions");
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");


        BaseMod.addRelicToCustomPool(new CurseBrand(), Dregs.Enums.COLOR_BLACK);
        BaseMod.addRelicToCustomPool(new CursedEgg(), Dregs.Enums.COLOR_BLACK);
        BaseMod.addRelicToCustomPool(new CursedLocket(), Dregs.Enums.COLOR_BLACK);
        BaseMod.addRelicToCustomPool(new DarksteelHammer(), Dregs.Enums.COLOR_BLACK);
        BaseMod.addRelicToCustomPool(new DeathBrand(), Dregs.Enums.COLOR_BLACK);
        BaseMod.addRelicToCustomPool(new EvilEye(), Dregs.Enums.COLOR_BLACK);
        BaseMod.addRelicToCustomPool(new JadeSeal(), Dregs.Enums.COLOR_BLACK);
        BaseMod.addRelicToCustomPool(new LuckyClover(), Dregs.Enums.COLOR_BLACK);
        BaseMod.addRelicToCustomPool(new NeowsHatred(), Dregs.Enums.COLOR_BLACK);
        BaseMod.addRelicToCustomPool(new Padlock(), Dregs.Enums.COLOR_BLACK);

        UnlockTracker.markRelicAsSeen(CursedEgg.ID);
        UnlockTracker.markRelicAsSeen(CursedLocket.ID);
        UnlockTracker.markRelicAsSeen(DarksteelHammer.ID);
        UnlockTracker.markRelicAsSeen(DeathBrand.ID);
        UnlockTracker.markRelicAsSeen(EvilEye.ID);
        UnlockTracker.markRelicAsSeen(JadeSeal.ID);
        UnlockTracker.markRelicAsSeen(LuckyClover.ID);
        UnlockTracker.markRelicAsSeen(NeowsHatred.ID);
        UnlockTracker.markRelicAsSeen(Padlock.ID);

        if (globalRelics) {
            BaseMod.addRelic(new BananaPeel(), RelicType.SHARED);
            BaseMod.addRelic(new FracturedPrism(), RelicType.SHARED);
            BaseMod.addRelic(new StickyBarb(), RelicType.SHARED);

        } else {
            BaseMod.addRelicToCustomPool(new BananaPeel(), Dregs.Enums.COLOR_BLACK);
            BaseMod.addRelicToCustomPool(new FracturedPrism(), Dregs.Enums.COLOR_BLACK);
            BaseMod.addRelicToCustomPool(new StickyBarb(), Dregs.Enums.COLOR_BLACK);

        }
        UnlockTracker.markRelicAsSeen(BananaPeel.ID);
        UnlockTracker.markRelicAsSeen(FracturedPrism.ID);
        UnlockTracker.markRelicAsSeen(StickyBarb.ID);

        logger.info("Done adding relics!");
    }

    @SuppressWarnings("LibGDXUnsafeIterator")
    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");

        logger.info("Add variables");

        logger.info("Adding cards");


        TextureAtlas cardAtlas = ReflectionHacks.getPrivateStatic(AbstractCard.class, "cardAtlas");

        TextureAtlas myCardAtlas = assets.loadAtlas(assetPath("images/cards/cards.atlas"));
        for (TextureAtlas.AtlasRegion region : myCardAtlas.getRegions()) {
            cardAtlas.addRegion(getModID() + "/" + region.name, region);
        }

        new AutoAdd("DregsMod")
                .packageFilter(AbstractDregsCard.class)
                .setDefaultSeen(true)
                .cards();

        logger.info("Done adding cards!");
    }

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        String lang = "eng";

        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "zhs";
        }

        logger.info("Loading strings for language: " + lang);


        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/" + lang + "/DregsMod-Card-Strings.json");


        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/" + lang + "/DregsMod-Power-Strings.json");


        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/" + lang + "/DregsMod-Relic-Strings.json");


        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/" + lang + "/DregsMod-Event-Strings.json");


        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/" + lang + "/DregsMod-Potion-Strings.json");


        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/" + lang + "/DregsMod-Character-Strings.json");


        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/" + lang + "/DregsMod-Orb-Strings.json");

        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/" + lang + "/DregsMod-UI-Strings.json");

        BaseMod.loadCustomStringsFile(TutorialStrings.class,
                getModID() + "Resources/localization/" + lang + "/DregsMod-Tutorial-Strings.json");

        BaseMod.loadCustomStringsFile(StanceStrings.class,
                getModID() + "Resources/localization/" + lang + "/DregsMod-Stance-Strings.json");

        logger.info("Done editing strings");
    }

    @Override
    public void receiveEditKeywords() {

        String lang = "eng";

        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "zhs";
        }

        logger.info("Loading keywords for language: " + lang);


        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/" + lang + "/DregsMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
        keywordsSetup = true;
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    public static Random cleansedCurseRng;

    @Override
    public Integer onSave() {
        cleansedCurseRng = Optional.ofNullable(cleansedCurseRng).orElse(new Random(Settings.seed));
        return cleansedCurseRng.counter;
    }

    @Override
    public void onLoad(Integer value) {
        int randomCount = Optional.ofNullable(value).orElse(0);
        cleansedCurseRng = new Random(Settings.seed, randomCount);
    }

    // If the player somehow avoids the Neow choice, give them the relic on floor 1
    @Override
    public void receiveStartGame() {
        if (AbstractDungeon.player.chosenClass.equals(Dregs.Enums.DREGS)) {
            AbstractDungeon.actionManager.addToNextCombat(new AbstractGameAction() {
                @Override
                public void update() {
                    isDone = true;
                    if (AbstractDungeon.floorNum == 1) {
                        if (AbstractDungeon.player.relics.stream().noneMatch(r -> r.relicId.equals(NeowsHatred.ID))) {
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), new NeowsHatred().makeCopy());
                        }
                    }
                }
            });
        }
    }
}
