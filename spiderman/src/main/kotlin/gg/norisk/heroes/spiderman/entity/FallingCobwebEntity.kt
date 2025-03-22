package gg.norisk.heroes.spiderman.entity

import gg.norisk.heroes.common.utils.toBlockPos
import net.minecraft.block.Blocks
import net.minecraft.entity.FallingBlockEntity
import net.minecraft.entity.MovementType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.Direction

class FallingCobwebEntity(owner: PlayerEntity)
    : FallingBlockEntity(owner.world, owner.x, owner.eyePos.y, owner.z, Blocks.COBWEB.defaultState) {
    override fun tick() {
        if (blockState.isAir) {
            discard()
            return
        }
        timeFalling++
        applyGravity()
        move(MovementType.SELF, velocity)
        tickBlockCollision()
        tickPortalTeleportation()
        if (!world.isClient && isAlive) {
            if (!isOnGround) {
                Direction.entries.forEach {
                    val pos = this.pos.add(0.5, 0.5, 0.5).add(it.doubleVector).toBlockPos()
                    if (!world.getBlockState(pos).isAir) {
                        if (world.setBlockState(blockPos, blockState, 3)) {
                            discard()
                        }
                        return
                    }
                }
                val y = blockPos.y
                if ((timeFalling > 100 && (y <= world.bottomY || y > world.topYInclusive)) || timeFalling > 600) {
                    discard()
                }
            }
            velocity = velocity.multiply(0.98)
        }
    }
}
