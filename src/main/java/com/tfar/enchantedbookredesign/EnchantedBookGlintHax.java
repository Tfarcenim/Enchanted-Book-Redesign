package com.tfar.enchantedbookredesign;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnchantedBookGlintHax extends ItemStackTileEntityRenderer {

  public static final Minecraft mc = Minecraft.getInstance();

  public BookWrapper model;
  public static ItemCameraTransforms.TransformType transform;
  private ItemRenderer itemRenderer;




  @Override
  public void renderByItem(ItemStack stack) {
    GlStateManager.pushMatrix();
    if (itemRenderer == null)itemRenderer = new EnchantedBookItemRenderer(mc.textureManager,mc.getModelManager(),mc.getItemColors(),mc.getItemRenderer().getItemModelMesher());
    model.handlePerspective(transform);
    this.itemRenderer.renderItem(stack, model.internal);
    GlStateManager.popMatrix();
  }
}
