package dregsmod.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.CurseOfTheBell;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import dregsmod.DregsMod;
import dregsmod.cards.Adaptability;
import dregsmod.cards.DregsDefend;
import dregsmod.cards.DregsStrike;
import dregsmod.cards.LashOut;
import dregsmod.cards.curses.Jealousy;
import dregsmod.relics.CurseBrand;
import dregsmod.vfx.VictoryEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static dregsmod.DregsMod.*;
import static dregsmod.characters.Dregs.Enums.COLOR_BLACK;

public class Dregs extends CustomPlayer {
    private static final Logger logger = Logger.getLogger(DregsMod.class.getName());

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass DREGS;
        @SpireEnum(name = "DREGS_BLACK_COLOR")
        public static AbstractCard.CardColor COLOR_BLACK;
        @SpireEnum(name = "DREGS_BLACK_COLOR")
        @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }
    
    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 75;
    public static final int MAX_HP = 75;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 2;

    private static final String ID = makeID("Dregs");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    private static final DregsAnimation animation = new DregsAnimation();

    public Dregs(String name, PlayerClass setClass) {
        super(name, setClass, new DregsEnergyOrb(), animation);

        initializeClass(null,
                DREGS_SHOULDER,
                DREGS_SHOULDER_2,
                DREGS_CORPSE,
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));


        loadAnimation(
                DREGS_SKELETON_ATLAS,
                DREGS_SKELETON_JSON,
                1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        
        
        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 220.0F * Settings.scale);
    }
    
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }
    
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        logger.info("Begin loading starter Deck Strings");

        retVal.add(DregsStrike.ID);
        retVal.add(DregsStrike.ID);
        retVal.add(DregsStrike.ID);
        retVal.add(DregsStrike.ID);
        retVal.add(DregsDefend.ID);
        retVal.add(DregsDefend.ID);
        retVal.add(DregsDefend.ID);
        retVal.add(DregsDefend.ID);
        retVal.add(Adaptability.ID);
        retVal.add(LashOut.ID);
        retVal.add(Jealousy.ID);
        return retVal;
    }
    
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(CurseBrand.ID);

        UnlockTracker.markRelicAsSeen(CurseBrand.ID);

        return retVal;
    }
    
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_PIERCING_WAIL", 0.65f);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false);
    }
    
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_PIERCING_WAIL";
    }
    
    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }
    
    @Override
    public AbstractCard.CardColor getCardColor() {
        return COLOR_BLACK;
    }
    
    @Override
    public Color getCardTrailColor() {
        return DregsMod.DREGS_BLACK;
    }
    
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }
    
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }
    
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Adaptability();
    }
    
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }
    
    @Override
    public AbstractPlayer newInstance() {
        return new Dregs(name, chosenClass);
    }
    
    @Override
    public Color getCardRenderColor() {
        return DregsMod.DREGS_BLACK;
    }
    
    @Override
    public Color getSlashAttackColor() {
        return DregsMod.DREGS_BLACK;
    }
    
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    @Override
    public void applyPreCombatLogic() {
        animation.analyzeDeck();
        super.applyPreCombatLogic();
    }

    /*
    @Override
    public void applyStartOfTurnPostDrawPowers() {
        discardPile.group.stream()
                .filter(card -> CardSealed.isSealed.get(card) && card instanceof AbstractSealedCard)
                .forEach(card -> ((AbstractSealedCard) card).triggerWhileSealed(this));
        super.applyStartOfTurnPostDrawPowers();
    }
    */

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel("dregsmodResources/images/heart1.png", "ATTACK_HEAVY"));
        panels.add(new CutscenePanel("dregsmodResources/images/heart2.png", "NECRONOMICON"));
        panels.add(new CutscenePanel("dregsmodResources/images/heart3.png"));
        return panels;
    }

    @Override
    public void updateVictoryVfx(ArrayList<AbstractGameEffect> effects) {
        for (AbstractGameEffect effect : effects) {
            if (effect instanceof VictoryEffect) {
                effect.update();
                // effect exists, so don't create
                return;
            }
        }
        effects.add(new VictoryEffect());
    }

    @Override
    public Texture getCutsceneBg() {
        return ImageMaster.loadImage("images/scenes/purpleBg.jpg");
    }

    @Override
    public void damage(DamageInfo info) {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output - currentBlock > 0) {
            animation.onDamage(info.output - currentBlock);
        }

        super.damage(info);
    }

    @Override
    public boolean isCursed() {
        return this.masterDeck.group.stream().anyMatch(card ->
                card.type == AbstractCard.CardType.CURSE &&
                        !card.cardID.equals(Necronomicurse.ID) &&
                        !card.cardID.equals(CurseOfTheBell.ID) &&
                        !card.cardID.equals(Jealousy.ID));
    }

    @Override
    public void combatUpdate() {
        super.combatUpdate();
        animation.update();
    }
}
