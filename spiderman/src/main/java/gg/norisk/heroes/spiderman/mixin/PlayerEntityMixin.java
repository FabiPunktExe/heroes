package gg.norisk.heroes.spiderman.mixin;

import gg.norisk.heroes.spiderman.ability.WallClimbAbilityKt;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "isClimbing", at = @At("RETURN"), cancellable = true)
    private void isClimbing(CallbackInfoReturnable<Boolean> cir) {
        WallClimbAbilityKt.handleClimbCheck((PlayerEntity) (Object) this, cir);
    }
}
