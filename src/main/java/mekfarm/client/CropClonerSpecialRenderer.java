package mekfarm.client;

import mekfarm.machines.CropClonerEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.ndrei.teslacorelib.render.HudInfoRenderer;

import java.util.List;

/**
 * Created by CF on 2016-11-24.
 */
public class CropClonerSpecialRenderer extends HudInfoRenderer<CropClonerEntity> {
    public CropClonerSpecialRenderer() {
    }

    @Override
    public void renderTileEntityAt(CropClonerEntity entity, double x, double y, double z, float partialTicks, int destroyStage) {
        IBlockState state = entity.getPlantedThing();
        if (state != null) {
            GlStateManager.pushMatrix();
            VertexBuffer vertexbuffer = Tessellator.getInstance().getBuffer();
            vertexbuffer.begin(7, DefaultVertexFormats.ITEM);

            GlStateManager.translate(x + .25, y + (8.0D / 16.0D), z + .25);
            GlStateManager.scale(0.5D, 0.5D, 0.5D);

            this.renderState(vertexbuffer, state, null);

            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Tessellator.getInstance().draw();
            GlStateManager.popMatrix();
        }

        super.renderTileEntityAt(entity, x, y, z, partialTicks, destroyStage);
    }

    private void renderState(VertexBuffer vertexbuffer, IBlockState blockState, EnumFacing side) {
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(blockState);
        if (model != null)
        {
            List<BakedQuad> quads = model.getQuads(blockState, side, 0);

            for (BakedQuad quad : quads)
            {
                int color = Minecraft.getMinecraft().getBlockColors().colorMultiplier(blockState, super.getWorld(), null, quad.getTintIndex());
                vertexbuffer.addVertexData(quad.getVertexData());
                vertexbuffer.putColorRGB_F4(((color >> 16) & 0xFF) / 255.0F, ((color >> 8) & 0xFF) / 255.0F, (color & 0xFF) / 255.0F);
            }
        }
    }
}
