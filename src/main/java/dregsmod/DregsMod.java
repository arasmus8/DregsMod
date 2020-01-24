package dregsmod;

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
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import dregsmod.characters.Dregs;
import dregsmod.relics.CurseBrand;
import dregsmod.util.IDCheckDontTouchPls;
import dregsmod.util.TextureLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@SpireInitializer
public class DregsMod implements
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

    private static final Logger logger = Logger.getLogger(DregsMod.class.getName());
    public static SpireConfig config;

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

    private static final String ENERGY_ORB_DREGS = "dregsmodResources/images/512/card_cyan_orb.png";
    private static final String CARD_ENERGY_ORB = "dregsmodResources/images/512/card_small_orb.png";

    private static final String ATTACK_DREGS_PORTRAIT = "dregsmodResources/images/1024/bg_attack.png";
    private static final String SKILL_DREGS_PORTRAIT = "dregsmodResources/images/1024/bg_skill.png";
    private static final String POWER_DREGS_PORTRAIT = "dregsmodResources/images/1024/bg_power.png";
    private static final String ENERGY_ORB_DREGS_PORTRAIT = "dregsmodResources/images/1024/card_cyan_orb.png";

    private static final String DREGS_CHARACTER_BUTTON_PNG = "dregsmodResources/images/charSelect/PotionbrewerCharacterButton.png";
    private static final String CHARACTER_SELECT_PORTRAIT = "dregsmodResources/images/charSelect/CharacterSelect.png";
    public static final String DREGS_SHOULDER = "dregsmodResources/images/char/potionbrewer/shoulder.png";
    public static final String DREGS_SHOULDER_2 = "dregsmodResources/images/char/potionbrewer/shoulder2.png";
    public static final String DREGS_CORPSE = "dregsmodResources/images/char/potionbrewer/corpse.png";

    public static final String BADGE_IMAGE = "dregsmodResources/images/Badge.png";

    public static final String DREGS_SKELETON_ATLAS = "dregsmodResources/images/char/potionbrewer/skeleton.atlas";
    public static final String DREGS_SKELETON_JSON = "dregsmodResources/images/char/potionbrewer/skeleton.json";

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

    public static Map<String, Texture> uiTextures = new HashMap<>();

    public DregsMod() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);


        setModID("dregsmod");


        logger.info("Done subscribing");

        logger.info("Creating the color " + Dregs.Enums.COLOR_BLACK.toString());

        BaseMod.addColor(Dregs.Enums.COLOR_BLACK, DREGS_BLACK, DREGS_BLACK, DREGS_BLACK,
                DREGS_BLACK, DREGS_BLACK, DREGS_BLACK, DREGS_BLACK,
                ATTACK_BLACK_BG, SKILL_BLACK_BG, POWER_BLACK_BG, ENERGY_ORB_DREGS,
                ATTACK_DREGS_PORTRAIT, SKILL_DREGS_PORTRAIT, POWER_DREGS_PORTRAIT,
                ENERGY_ORB_DREGS_PORTRAIT, CARD_ENERGY_ORB);

        logger.info("Done creating the color");


        logger.info("Done adding mod savable fields");
    }

    public static void setModID(String ID) {
        Gson coolG = new Gson();

        InputStream in = DregsMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json");
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

        InputStream in = DregsMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json");
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);
        String packageName = DregsMod.class.getPackage().getName();
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
        DregsMod dregsMod = new DregsMod();
        logger.info("========================= /Accursed Mod Initialized. Hello World./ =========================");
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + Dregs.Enums.DREGS.toString());

        BaseMod.addCharacter(new Dregs("Dregs", Dregs.Enums.DREGS),
                DREGS_CHARACTER_BUTTON_PNG, CHARACTER_SELECT_PORTRAIT, Dregs.Enums.DREGS);

        receiveEditPotions();
        logger.info("Added " + Dregs.Enums.DREGS.toString());
    }

    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");


        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        logger.info("Adding mod settings");


        try {
            config = new SpireConfig("DregsMod", "dregsConfig");
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

        // UI / VFX preload

        Texture cardSeal = TextureLoader.getTexture("dregsmodResources/images/ui/card_seal.png");
        Texture cardSealBottom = TextureLoader.getTexture("dregsmodResources/images/ui/card_seal_bottom.png");
        Texture cardSealTop = TextureLoader.getTexture("dregsmodResources/images/ui/card_seal_top.png");

        uiTextures.put("cardSeal", cardSeal);
        uiTextures.put("cardSealBottom", cardSealBottom);
        uiTextures.put("cardSealTop", cardSealTop);

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


        BaseMod.addRelicToCustomPool(new CurseBrand(), Dregs.Enums.COLOR_BLACK);

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
                getModID() + "Resources/localization/eng/DregsMod-Card-Strings.json");


        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/DregsMod-Power-Strings.json");


        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/DregsMod-Relic-Strings.json");


        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/DregsMod-Event-Strings.json");


        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/DregsMod-Potion-Strings.json");


        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/DregsMod-Character-Strings.json");


        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/DregsMod-Orb-Strings.json");

        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/eng/DregsMod-UI-Strings.json");

        BaseMod.loadCustomStringsFile(TutorialStrings.class,
                getModID() + "Resources/localization/eng/DregsMod-Tutorial-Strings.json");

        logger.info("Done editing strings");
    }

    @Override
    public void receiveEditKeywords() {


        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/DregsMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
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
