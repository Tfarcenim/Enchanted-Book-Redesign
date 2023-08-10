package tfar.enchantedbookredesign;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderType.State;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class ModRenderType extends RenderState {
  public ModRenderType(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
    super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
  }

  public static RenderType TINTED_GLINT  = RenderType.create("tinted_glint", DefaultVertexFormats.POSITION_COLOR_TEX,
          7, 256, State.builder()
                  .setTextureState(new RenderState.TextureState(Hooks.TINTED_GLINT_RL, true, false))
                  .setWriteMaskState(COLOR_WRITE)
                  .setCullState(NO_CULL)
                  .setDepthTestState(EQUAL_DEPTH_TEST)
                  .setTransparencyState(GLINT_TRANSPARENCY)
                  .setTexturingState(GLINT_TEXTURING)
                  .createCompositeState(false));

  public static RenderType TINTED_GLINT_DIRECT = RenderType.create("tinted_glint_direct",
          DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS,
          256, RenderType.State.builder()
                  .setTextureState(new RenderState.TextureState(Hooks.TINTED_GLINT_RL, true, false))
                  .setWriteMaskState(COLOR_WRITE)
                  .setCullState(NO_CULL)
                  .setDepthTestState(EQUAL_DEPTH_TEST)
                  .setTransparencyState(GLINT_TRANSPARENCY)
                  .setTexturingState(GLINT_TEXTURING)
                  .createCompositeState(false));


  public static final RenderType TINTED_ENTITY_GLINT_DIRECT =
         RenderType.create("tinted_entity_glint_direct",
                  DefaultVertexFormats.POSITION_COLOR_TEX,
                  GL11.GL_QUADS, 256, RenderType.State.builder()
                          .setTextureState(new RenderState.TextureState(Hooks.TINTED_GLINT_RL, true, false))
                          .setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL)
                          .setDepthTestState(EQUAL_DEPTH_TEST)
                          .setTransparencyState(GLINT_TRANSPARENCY)
                          .setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));


}
