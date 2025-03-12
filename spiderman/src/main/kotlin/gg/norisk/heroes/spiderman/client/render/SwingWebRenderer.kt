package gg.norisk.heroes.spiderman.client.render

import gg.norisk.heroes.spiderman.entity.SwingWebEntity
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.state.EntityRenderState
import net.minecraft.client.render.entity.state.EntityRenderState.LeashData
import net.minecraft.client.render.item.ItemRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.ModelTransformationMode
import net.minecraft.util.math.MathHelper
import org.joml.Matrix4f

private val webItemStack = ItemStack(Items.COBWEB)

class SwingWebRenderState : EntityRenderState() {
    val itemRenderState = ItemRenderState()
}

class SwingWebRenderer(context: EntityRendererFactory.Context) :
    EntityRenderer<SwingWebEntity, SwingWebRenderState>(context) {
    private val itemModelManager = context.itemModelManager

    override fun createRenderState(): SwingWebRenderState? {
        return SwingWebRenderState()
    }

    override fun updateRenderState(entity: SwingWebEntity, state: SwingWebRenderState, tickDelta: Float) {
        super.updateRenderState(entity, state, tickDelta)
        itemModelManager.updateForNonLivingEntity(
            state.itemRenderState,
            webItemStack,
            ModelTransformationMode.GROUND,
            entity)
        if (state.leashData == null) {
            state.leashData = LeashData()
        }
        state.leashData!!.startPos = entity.pos
        val owner = entity.owner
        state.leashData!!.endPos = if (owner != null) owner.pos.add(0.0, owner.height / 2.0, 0.0) else entity.pos
    }

    override fun render(state: SwingWebRenderState,
                        matrixStack: MatrixStack,
                        vertexConsumerProvider: VertexConsumerProvider,
                        light: Int) {
        matrixStack.push()
        matrixStack.multiply(dispatcher.rotation)
        matrixStack.scale(4f, 4f, 4f)
        state.itemRenderState.render(
            matrixStack,
            vertexConsumerProvider,
            light,
            OverlayTexture.DEFAULT_UV)
        matrixStack.pop()

        val leashData = state.leashData
        if (leashData != null) {
            renderLeash(matrixStack, vertexConsumerProvider, leashData)
        }
    }

    // net.minecraft.client.render.entity.EntityRenderer.renderLeash
    private fun renderLeash(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, leashData: LeashData) {
        val g = (leashData.endPos.x - leashData.startPos.x).toFloat()
        val h = (leashData.endPos.y - leashData.startPos.y).toFloat()
        val i = (leashData.endPos.z - leashData.startPos.z).toFloat()
        val j = MathHelper.inverseSqrt(g * g + i * i) * 0.025f / 2.0f
        val k = i * j
        val l = g * j
        matrices.push()
        matrices.translate(leashData.offset)
        val vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLeash())
        val matrix4f = matrices.peek().getPositionMatrix()

        for (m in 0..24) {
            renderLeashSegment(
                vertexConsumer,
                matrix4f,
                g,
                h,
                i,
                leashData.leashedEntityBlockLight,
                leashData.leashHolderBlockLight,
                leashData.leashedEntitySkyLight,
                leashData.leashHolderSkyLight,
                0.025f,
                0.025f,
                k,
                l,
                m,
                false)
        }

        for (m in 24 downTo 0) {
            renderLeashSegment(
                vertexConsumer,
                matrix4f,
                g,
                h,
                i,
                leashData.leashedEntityBlockLight,
                leashData.leashHolderBlockLight,
                leashData.leashedEntitySkyLight,
                leashData.leashHolderSkyLight,
                0.025f,
                0.0f,
                k,
                l,
                m,
                true)
        }

        matrices.pop()
    }

    // net.minecraft.client.render.entity.EntityRenderer.renderLeashSegment
    private fun renderLeashSegment(vertexConsumer: VertexConsumer,
                                   matrix: Matrix4f,
                                   leashedEntityX: Float,
                                   leashedEntityY: Float,
                                   leashedEntityZ: Float,
                                   leashedEntityBlockLight: Int,
                                   leashHolderBlockLight: Int,
                                   leashedEntitySkyLight: Int,
                                   leashHolderSkyLight: Int,
                                   f: Float,
                                   g: Float,
                                   h: Float,
                                   i: Float,
                                   segmentIndex: Int,
                                   isLeashKnot: Boolean) {
        val j = segmentIndex.toFloat() / 24.0f
        val k = MathHelper.lerp(j, leashedEntityBlockLight.toFloat(), leashHolderBlockLight.toFloat()).toInt()
        val l = MathHelper.lerp(j, leashedEntitySkyLight.toFloat(), leashHolderSkyLight.toFloat()).toInt()
        val m = LightmapTextureManager.pack(k, l)
        val n = if (segmentIndex % 2 == (if (isLeashKnot) 1 else 0)) 0.7f else 1.0f
        val o = 0.75f * n
        val p = 0.75f * n
        val q = 0.75f * n
        val r = leashedEntityX * j
        val s = if (leashedEntityY > 0.0f)
            leashedEntityY * j * j
        else
            leashedEntityY - leashedEntityY * (1.0f - j) * (1.0f - j)
        val t = leashedEntityZ * j
        vertexConsumer.vertex(matrix, r - h * 6, s + g, t + i * 6).color(o, p, q, 1.0f).light(m)
        vertexConsumer.vertex(matrix, r + h * 6, s + f - g, t - i * 6).color(o, p, q, 1.0f).light(m)
    }
}
