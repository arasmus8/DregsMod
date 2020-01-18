package accursed;

import accursed.characters.Accursed;
import accursed.relics.PotionKit;
import accursed.util.IDCheckDontTouchPls;
import accursed.util.TextureLoader;
import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@SpireInitializer
public class AccursedMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        OnCardUseSubscriber,
        OnStartBattleSubscriber,
        PostBattleSubscriber,
        PostInitializeSubscriber,
        PostPotionUseSubscriber,
        StartActSubscriber,
        StartGameSubscriber {

    private static String modID;

    private static final Logger logger = Logger.getLogger(AccursedMod.class.getName());
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    private static final String DELIM = ", ";
    public static SpireConfig config;

    private static final String MODNAME = "TheAccursed";
    private static final String AUTHOR = "NotInTheFace";
    private static final String DESCRIPTION = "A custom character with Curse synergies." +
            "\n- Cleanse special curses to collect cards from other classes" +
            "\n- Curse your enemies to add special curses to your deck" +
            "\n- try not to die";

    public static final Color ACCURSED_GRAY = CardHelper.getColor(0, 20, 30);

    private static final String ATTACK_GRAY_BG = "accursedResources/images/512/bg_attack.png";
    private static final String SKILL_GRAY_BG = "accursedResources/images/512/bg_skill.png";
    private static final String POWER_GRAY_BG = "accursedResources/images/512/bg_power.png";

    private static final String ENERGY_ORB_ACCURSED = "accursedResources/images/512/card_cyan_orb.png";
    private static final String CARD_ENERGY_ORB = "accursedResources/images/512/card_small_orb.png";

    private static final String ATTACK_ACCURSED_PORTRAIT = "accursedResources/images/1024/bg_attack.png";
    private static final String SKILL_ACCURSED_PORTRAIT = "accursedResources/images/1024/bg_skill.png";
    private static final String POWER_ACCURSED_PORTRAIT = "accursedResources/images/1024/bg_power.png";
    private static final String ENERGY_ORB_ACCURSED_PORTRAIT = "accursedResources/images/1024/card_cyan_orb.png";

    private static final String ACCURSED_CHARACTER_BUTTON_PNG = "accursedResources/images/charSelect/PotionbrewerCharacterButton.png";
    private static final String CHARACTER_SELECT_PORTRAIT = "accursedResources/images/charSelect/CharacterSelect.png";
    public static final String ACCURSED_SHOULDER = "accursedResources/images/char/potionbrewer/shoulder.png";
    public static final String ACCURSED_SHOULDER_2 = "accursedResources/images/char/potionbrewer/shoulder2.png";
    public static final String ACCURSED_CORPSE = "accursedResources/images/char/potionbrewer/corpse.png";

    public static final String BADGE_IMAGE = "accursedResources/images/Badge.png";

    public static final String ACCURSED_SKELETON_ATLAS = "accursedResources/images/char/potionbrewer/skeleton.atlas";
    public static final String ACCURSED_SKELETON_JSON = "accursedResources/images/char/potionbrewer/skeleton.json";

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
        return getModID() + "Resources/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    public static CardGroup sealedPile;

    public AccursedMod() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);


        setModID("accursed");


        logger.info("Done subscribing");

        logger.info("Creating the color " + Accursed.Enums.COLOR_BLACK.toString());

        BaseMod.addColor(Accursed.Enums.COLOR_BLACK, ACCURSED_GRAY, ACCURSED_GRAY, ACCURSED_GRAY,
                ACCURSED_GRAY, ACCURSED_GRAY, ACCURSED_GRAY, ACCURSED_GRAY,
                ATTACK_GRAY_BG, SKILL_GRAY_BG, POWER_GRAY_BG, ENERGY_ORB_ACCURSED,
                ATTACK_ACCURSED_PORTRAIT, SKILL_ACCURSED_PORTRAIT, POWER_ACCURSED_PORTRAIT,
                ENERGY_ORB_ACCURSED_PORTRAIT, CARD_ENERGY_ORB);

        logger.info("Done creating the color");


        logger.info("Done adding mod savable fields");
    }

    public static void setModID(String ID) {
        Gson coolG = new Gson();

        InputStream in = AccursedMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json");
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);
        logger.info("You are attempting to set your mod ID as: " + ID);
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) {
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION);
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) {
            modID = EXCEPTION_STRINGS.DEFAULTID;
        } else {
            modID = ID;
        }
        logger.info("Success! ID is " + modID);
    }

    public static String getModID() {
        return modID;
    }

    private static void pathCheck() {
        Gson coolG = new Gson();

        InputStream in = AccursedMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json");
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);
        String packageName = AccursedMod.class.getPackage().getName();
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources");
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) {
            if (!packageName.equals(getModID())) {
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID());
            }
            if (!resourcePathExists.exists()) {
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources");
            }
        }
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing Accursed Mod. Hi. =========================");
        AccursedMod accursedMod = new AccursedMod();
        logger.info("========================= /Accursed Mod Initialized. Hello World./ =========================");
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + Accursed.Enums.ACCURSED.toString());

        BaseMod.addCharacter(new Accursed("Accursed", Accursed.Enums.ACCURSED),
                ACCURSED_CHARACTER_BUTTON_PNG, CHARACTER_SELECT_PORTRAIT, Accursed.Enums.ACCURSED);

        receiveEditPotions();
        logger.info("Added " + Accursed.Enums.ACCURSED.toString());
    }

    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");


        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        logger.info("Adding mod settings");


        try {
            config = new SpireConfig("AccursedMod", "accursedConfig");
            config.load();
            // PotionbrewerTipTracker.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");


        ModPanel panel = new ModPanel();
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

        // Console Commands

        // Save/Load fields

        // Events
    }

    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        // BaseMod.addPotion(AcidPotion.class, AcidPotion.LIQUID_COLOR, AcidPotion.HYBRID_COLOR, AcidPotion.SPOTS_COLOR, AcidPotion.POTION_ID, null);

        logger.info("Done editing potions");
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");


        BaseMod.addRelicToCustomPool(new PotionKit(), Accursed.Enums.COLOR_BLACK);

        // UnlockTracker.markRelicAsSeen(AlchemistKit.ID);
        // BaseMod.addRelic(new AlchemistFlask(), RelicType.SHARED);
        logger.info("Done adding relics!");
    }

    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");

        pathCheck();

        logger.info("Add variables");

        logger.info("Adding cards");


        for (AbstractCard c : CardList.allCards) {
            BaseMod.addCard(c);
            UnlockTracker.unlockCard(c.cardID);
        }

        logger.info("Done adding cards!");
    }

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());


        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/AccursedMod-Card-Strings.json");


        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/AccursedMod-Power-Strings.json");


        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/AccursedMod-Relic-Strings.json");


        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/AccursedMod-Event-Strings.json");


        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/AccursedMod-Potion-Strings.json");


        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/AccursedMod-Character-Strings.json");


        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/AccursedMod-Orb-Strings.json");

        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/eng/AccursedMod-UI-Strings.json");

        BaseMod.loadCustomStringsFile(TutorialStrings.class,
                getModID() + "Resources/localization/eng/AccursedMod-Tutorial-Strings.json");

        logger.info("Done editing strings");
    }

    @Override
    public void receiveEditKeywords() {


        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/AccursedMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveStartGame() {
    }

    @Override
    public void receiveStartAct() {
    }

    @Override
    public void receivePostPotionUse(AbstractPotion potion) {
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        sealedPile = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
    }

    @Override
    public void receiveCardUsed(AbstractCard card) {
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
