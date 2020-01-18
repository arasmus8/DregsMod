package accursed.actions

import accursed.AccursedMod
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect

class SealCardAction(val card: AbstractCard, private val group: CardGroup) : AbstractGameAction() {

    init {
        duration = Settings.ACTION_DUR_FAST
        setValues(AbstractDungeon.player, AbstractDungeon.player, 1)
    }

    override fun update() {
        if (duration == Settings.ACTION_DUR_FAST && group.contains(card)) {
            card.triggerOnExhaust()
            card.triggerOnManualDiscard()

            // TODO make a card sealing vfx -- see SoundMaster "KEY_OBTAIN"
            AbstractDungeon.effectList.add(ExhaustCardEffect(card))
            card.exhaustOnUseOnce = false
            card.freeToPlayOnce = false
            group.removeCard(card)
            AccursedMod.sealedPile.addToBottom(card)
            AbstractDungeon.player.onCardDrawOrDiscard()
        }
    }
}