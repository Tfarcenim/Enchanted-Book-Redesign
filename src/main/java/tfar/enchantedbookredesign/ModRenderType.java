package tfar.enchantedbookredesign;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderType.CompositeState;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import org.lwjgl.opengl.GL11;

public class ModRenderType extends RenderStateShard {
    public ModRenderType(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
        super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
    }

    public static RenderType TINTED_GLINT = RenderType.create("tinted_glint", DefaultVertexFormat.POSITION_COLOR_TEX,
            VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(Hooks.TINTED_GLINT_RL, true, false))
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setTexturingState(GLINT_TEXTURING)
                    .createCompositeState(false));

    public static RenderType TINTED_GLINT_DIRECT = RenderType.create("tinted_glint_direct",
            DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS,
            256, false, false, RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(Hooks.TINTED_GLINT_RL, true, false))
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setTexturingState(GLINT_TEXTURING)
                    .createCompositeState(false));


    public static final RenderType TINTED_ENTITY_GLINT_DIRECT =
            RenderType.create("tinted_entity_glint_direct",
                    DefaultVertexFormat.POSITION_COLOR_TEX,
                    VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
                            .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
                            .setTextureState(new RenderStateShard.TextureStateShard(Hooks.TINTED_GLINT_RL, true, false))
                            .setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL)
                            .setDepthTestState(EQUAL_DEPTH_TEST)
                            .setTransparencyState(GLINT_TRANSPARENCY)
                            .setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));


}
