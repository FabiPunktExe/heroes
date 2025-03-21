package gg.norisk.heroes.spiderman.ability

import gg.norisk.heroes.common.hero.getHero
import gg.norisk.heroes.spiderman.SpidermanManager
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun handleClimbCheck(player: PlayerEntity, cir: CallbackInfoReturnable<Boolean>) {
    if (player.getHero() == SpidermanManager.Spiderman && !player.isSpectator && player.horizontalCollision) {
        cir.returnValue = true
    }
}
