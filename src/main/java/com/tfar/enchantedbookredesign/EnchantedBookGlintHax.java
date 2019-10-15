package com.tfar.enchantedbookredesign;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;

import static net.minecraft.client.renderer.ItemRenderer.RES_ITEM_GLINT;

public class EnchantedBookGlintHax extends ItemStackTileEntityRenderer {

  public static final Minecraft mc = Minecraft.getInstance();

  public static BookWrapper model;
  public static ItemCameraTransforms.TransformType transform;
  private static ItemRenderer itemRenderer;

  @Override
  public void renderByItem(ItemStack stack) {
    if (!(stack.getItem() instanceof EnchantedBookItem)) {
      super.renderByItem(stack);
      return;
    }
  //  GlStateManager.pushMatrix();
    if (itemRenderer == null)itemRenderer = new EnchantedBookItemRenderer(mc.textureManager,mc.getModelManager(),mc.getItemColors(),mc.getItemRenderer().getItemModelMesher());
    model.internal.handlePerspective(transform);
    IBakedModel iBakedModel = itemRenderer.getModelWithOverrides(stack,mc.world,mc.player);
    this.itemRenderer.renderItem(stack, ((BookWrapper)iBakedModel).internal);
 //   GlStateManager.popMatrix();
  }

  public static void renderEffect(TextureManager textureManagerIn, Runnable renderModelFunction, int scale) {
    scale = 10;
    GlStateManager.depthMask(false);
    GlStateManager.depthFunc(514);
    GlStateManager.disableLighting();
    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
    textureManagerIn.bindTexture(RES_ITEM_GLINT);
    GlStateManager.matrixMode(5890);
    GlStateManager.pushMatrix();
    GlStateManager.scalef((float)scale, (float)scale, (float)scale);
    float f = (float)(Util.milliTime() % 3000L) / 3000.0F / (float)scale;
    GlStateManager.translatef(f, 0.0F, 0.0F);
    GlStateManager.rotatef(-50.0F, 0.0F, 0.0F, 1.0F);
    renderModelFunction.run();
    GlStateManager.popMatrix();
    GlStateManager.pushMatrix();
    GlStateManager.scalef((float)scale, (float)scale, (float)scale);
    float f1 = (float)(Util.milliTime() % 4873L) / 4873.0F / (float)scale;
    GlStateManager.translatef(-f1, 0.0F, 0.0F);
    GlStateManager.rotatef(10.0F, 0.0F, 0.0F, 1.0F);
    GlStateManager.popMatrix();
    GlStateManager.matrixMode(5888);
    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    GlStateManager.enableLighting();
    GlStateManager.depthFunc(515);
    GlStateManager.depthMask(true);
    textureManagerIn.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
  }
}
