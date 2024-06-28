package tfar.enchantedbookredesign;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

public class ModRenderType extends RenderType {

    public ModRenderType(String $$0, VertexFormat $$1, VertexFormat.Mode $$2, int $$3, boolean $$4, boolean $$5, Runnable $$6, Runnable $$7) {
        super($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7);
    }

    static ShaderStateShard tinted_glint_direct = new RenderStateShard.ShaderStateShard(Hooks::getRendertype_tinted_glint_direct);

 /*   public static RenderType TINTED_GLINT = RenderType.create("tinted_glint", DefaultVertexFormat.POSITION_COLOR_TEX,
            VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(Hooks.TINTED_GLINT_RL, true, false))
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setTexturingState(GLINT_TEXTURING)
                    .createCompositeState(false));*/

    public static RenderType TINTED_GLINT_DIRECT = RenderType.create("tinted_glint_direct",
            DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS,
            256, false, false, RenderType.CompositeState.builder()
                    .setShaderState(tinted_glint_direct)
                    .setTextureState(new RenderStateShard.TextureStateShard(Hooks.TINTED_GLINT_RL, true, false))
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setTexturingState(GLINT_TEXTURING)
                    .createCompositeState(false));



  /*  public static final RenderType TINTED_ENTITY_GLINT_DIRECT =
            RenderType.create("tinted_entity_glint_direct",
                    DefaultVertexFormat.POSITION_COLOR_TEX,
                    VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
                            .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
                            .setTextureState(new RenderStateShard.TextureStateShard(Hooks.TINTED_GLINT_RL, true, false))
                            .setWriteMaskState(COLOR_WRITE)
                            .setCullState(NO_CULL)
                            .setDepthTestState(EQUAL_DEPTH_TEST)
                            .setTransparencyState(GLINT_TRANSPARENCY)
                            .setTexturingState(ENTITY_GLINT_TEXTURING)
                            .createCompositeState(false));*/


}
