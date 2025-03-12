package gg.norisk.heroes.spiderman.ability

import gg.norisk.heroes.client.option.HeroKeyBindings
import gg.norisk.heroes.common.HeroesManager.client
import gg.norisk.heroes.common.ability.NumberProperty
import gg.norisk.heroes.common.ability.operation.AddValueTotal
import gg.norisk.heroes.common.hero.ability.AbilityScope
import gg.norisk.heroes.common.hero.ability.implementation.PressAbility
import io.wispforest.owo.ui.component.Components
import io.wispforest.owo.ui.core.Component
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.FallingBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.entity.directionVector
import net.silkmc.silk.core.item.itemStack

val webAmount = NumberProperty(1.0, 4, "Web amount", AddValueTotal(1.0, 1.0, 2.0, 2.0)).apply {
    icon = {
        Components.item(Items.COBWEB.defaultStack)
    }
}

@OptIn(ExperimentalSilkApi::class)
object ThrowWebsAbility : PressAbility("Throw webs") {
    init {
        client {
            keyBind = HeroKeyBindings.secondKeyBind
        }
        properties = listOf(webAmount)
        cooldownProperty = buildCooldown(90.0, 5, AddValueTotal(-9.0, -9.0, -9.0, -9.0, -9.0))
    }

    override fun getIconComponent(): Component {
        return Components.item(itemStack(Items.COBWEB) {})
    }

    override fun getBackgroundTexture(): Identifier {
        return Identifier.of("textures/block/packed_mud.png")
    }

    override fun onStart(player: PlayerEntity, abilityScope: AbilityScope) {
        if (player.world is ServerWorld) {
            repeat(webAmount.getValue(player.uuid).toInt()) {
                val web = FallingBlockEntity(EntityType.FALLING_BLOCK, player.world)
                web.setPosition(player.eyePos)
                web.block = Blocks.COBWEB.defaultState
                web.velocity = player.directionVector.multiply(0.6).addRandom(player.world.random, 0.3f)
                player.world.spawnEntity(web)
            }
        }
    }
}
