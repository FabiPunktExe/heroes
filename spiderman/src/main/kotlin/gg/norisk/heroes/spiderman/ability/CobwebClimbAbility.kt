package gg.norisk.heroes.spiderman.ability

import gg.norisk.heroes.common.hero.getHero
import gg.norisk.heroes.spiderman.SpidermanManager
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun handleCobwebCollision(player: PlayerEntity, ci: CallbackInfo) {
    if (player.getHero() == SpidermanManager.Spiderman && !player.isSpectator) {
        ci.cancel()
    }
}

fun handleCobwebClimbCheck(player: PlayerEntity, cir: CallbackInfoReturnable<Boolean>) {
    if (player.getHero() == SpidermanManager.Spiderman && !player.isSpectator && player.blockStateAtPos.block == Blocks.COBWEB) {
        cir.returnValue = true
    }
}
