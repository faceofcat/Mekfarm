package mekfarm.client;

import mekfarm.common.FluidsRegistry;
import mekfarm.machines.LiquidXPStorageEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.ndrei.teslacorelib.render.HudInfoRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Created by CF on 2016-11-24.
 */
public class LiquidXPStorageSpecialRenderer extends HudInfoRenderer<LiquidXPStorageEntity> {
    public LiquidXPStorageSpecialRenderer() {
    }

    @Override
    public void renderTileEntityAt(LiquidXPStorageEntity entity, double x, double y, double z, float partialTicks, int destroyStage) {
        float height = Math.max(0.0f, Math.min(1.0f, entity.getFillPercent()));
        if (height > 0.0f) {
            float top = 1 + 14 * height;

            int color = FluidsRegistry.liquidXP.getColor();
            ResourceLocation still = FluidsRegistry.liquidXP.getStill();
            ResourceLocation flowing = FluidsRegistry.liquidXP.getFlowing();
            TextureAtlasSprite stillSprite = (still == null) ? null : Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(still.toString());
            if (stillSprite == null) {
                stillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
            }
            TextureAtlasSprite flowingSprite = /*(flowing == null) ? null : Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(flowing.toString());
            if (flowingSprite == null) {
                flowingSprite = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
            }
            flowingSprite =*/ stillSprite;

            GlStateManager.pushAttrib();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x + 0.0F, (float) y + 0.0F, (float) z + 0.0F);
            float magicNumber = 1.0f / 16.0f;
            GlStateManager.scale(magicNumber, magicNumber, magicNumber);

            VertexBuffer vertexbuffer = Tessellator.getInstance().getBuffer();
            vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);


            vertexbuffer.pos(1, 1, 1).tex(flowingSprite.getMaxU(), flowingSprite.getMaxV()).endVertex();
            vertexbuffer.pos(1, top, 1).tex(flowingSprite.getMaxU(), flowingSprite.getMinV()).endVertex();
            vertexbuffer.pos(15, top, 1).tex(flowingSprite.getMinU(), flowingSprite.getMinV()).endVertex();
            vertexbuffer.pos(15, 1, 1).tex(flowingSprite.getMinU(), flowingSprite.getMaxV()).endVertex();

            vertexbuffer.pos(1, 1, 15).tex(flowingSprite.getMaxU(), flowingSprite.getMaxV()).endVertex();
            vertexbuffer.pos(15, 1, 15).tex(flowingSprite.getMinU(), flowingSprite.getMaxV()).endVertex();
            vertexbuffer.pos(15, top, 15).tex(flowingSprite.getMinU(), flowingSprite.getMinV()).endVertex();
            vertexbuffer.pos(1, top, 15).tex(flowingSprite.getMaxU(), flowingSprite.getMinV()).endVertex();

            vertexbuffer.pos(1, 1, 1).tex(flowingSprite.getMinU(), flowingSprite.getMaxV()).endVertex();
            vertexbuffer.pos(1, 1, 15).tex(flowingSprite.getMaxU(), flowingSprite.getMaxV()).endVertex();
            vertexbuffer.pos(1, top, 15).tex(flowingSprite.getMaxU(), flowingSprite.getMinV()).endVertex();
            vertexbuffer.pos(1, top, 1).tex(flowingSprite.getMinU(), flowingSprite.getMinV()).endVertex();

            vertexbuffer.pos(15, 1, 1).tex(flowingSprite.getMinU(), flowingSprite.getMaxV()).endVertex();
            vertexbuffer.pos(15, top, 1).tex(flowingSprite.getMinU(), flowingSprite.getMinV()).endVertex();
            vertexbuffer.pos(15, top, 15).tex(flowingSprite.getMaxU(), flowingSprite.getMinV()).endVertex();
            vertexbuffer.pos(15, 1, 15).tex(flowingSprite.getMaxU(), flowingSprite.getMaxV()).endVertex();

            vertexbuffer.pos(1, top, 1).tex(stillSprite.getMinU(), stillSprite.getMinV()).endVertex();
            vertexbuffer.pos(1, top, 15).tex(stillSprite.getMinU(), stillSprite.getMaxV()).endVertex();
            vertexbuffer.pos(15, top, 15).tex(stillSprite.getMaxU(), stillSprite.getMaxV()).endVertex();
            vertexbuffer.pos(15, top, 1).tex(stillSprite.getMaxU(), stillSprite.getMinV()).endVertex();

            super.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.color((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableLighting();
            super.setLightmapDisabled(true);

            Tessellator.getInstance().draw();

            super.setLightmapDisabled(false);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
            GlStateManager.popAttrib();
        }

        super.renderTileEntityAt(entity, x, y, z, partialTicks, destroyStage);
    }
}
