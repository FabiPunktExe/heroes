package gg.norisk.heroes.spiderman.entity

import net.minecraft.block.Blocks
import net.minecraft.entity.FallingBlockEntity
import net.minecraft.entity.player.PlayerEntity

class FallingCobwebEntity(val owner: PlayerEntity)
    : FallingBlockEntity(owner.world, owner.x, owner.eyePos.y, owner.z, Blocks.COBWEB.defaultState)
