package com.tfar.enchantedbookredesign;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import static net.minecraft.client.renderer.RenderType.*;

public class ModRenderType extends RenderState {
  public ModRenderType(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
    super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
  }

  public static RenderType TINTED_GLINT  = get("tinted_glint", DefaultVertexFormats.POSITION_COLOR_TEX,
            7, 256, State.builder()
            .texture(new RenderState.TextureState(Hooks.TINTED_GLINT_RL, true, false))
          .writeMask(COLOR_WRITE)
            .cull(CULL_DISABLED)
            .depthTest(DEPTH_EQUAL)
            .transparency(GLINT_TRANSPARENCY)
            .texturing(GLINT_TEXTURING)
            .build(false));

  public static final RenderType TINTED_ENTITY_GLINT =
          get("tinted_entity_glint",
                  DefaultVertexFormats.POSITION_COLOR_TEX,
                  7, 256, RenderType.State.builder()
                          .texture(new RenderState.TextureState(Hooks.TINTED_GLINT_RL, true, false))
                          .writeMask(COLOR_WRITE).cull(CULL_DISABLED)
                          .depthTest(DEPTH_EQUAL)
                          .transparency(GLINT_TRANSPARENCY)
                          .texturing(ENTITY_GLINT_TEXTURING).build(false));


}
