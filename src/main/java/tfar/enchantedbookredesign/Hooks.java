package tfar.enchantedbookredesign;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

public class Hooks {

	public static ItemStack stack = ItemStack.EMPTY;
	public static boolean renderTint;

	public static final ResourceLocation TINTED_GLINT_RL = new ResourceLocation(EnchantedBookRedesign.MODID,
					"textures/misc/enchanted_item_glint.png");


	public static void renderGlint(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformTypeIn, boolean leftHand
					, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, IBakedModel modelIn,
																 CallbackInfo ci, boolean flag, boolean flag1, RenderType rendertype, RenderType rendertype1,
																 IVertexBuilder ivertexbuilder){
		if (Hooks.renderTint) {

			Random random = new Random(42);

			IVertexBuilder builder2 = bufferIn.getBuffer(ModRenderType.TINTED_GLINT);
			int color = EnchantedBookRedesign.getColor(itemStackIn);

			for (Direction direction : Direction.values()) {
				random.setSeed(42L);
				renderGlintQuads(matrixStackIn, builder2, modelIn.getQuads(null, direction, random), itemStackIn,
								combinedLightIn, combinedOverlayIn, color);
			}

			renderGlintQuads(matrixStackIn, builder2, modelIn.getQuads(null, null, random)
							, itemStackIn, combinedLightIn, combinedOverlayIn, color);
			Hooks.renderTint = false;
		}
	}

	public static void renderGlintQuads(MatrixStack matrixStackIn, IVertexBuilder bufferIn, List<BakedQuad> quadsIn, ItemStack itemStackIn, int combinedLightIn, int combinedOverlayIn, int color) {
		MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();

		for (BakedQuad bakedquad : quadsIn) {
			float f = (color >> 16 & 255) / 255.0F;
			float f1 = (color >> 8 & 255) / 255.0F;
			float f2 = (color & 255) / 255.0F;

			bufferIn.addVertexData(matrixstack$entry, bakedquad, f, f1, f2, combinedLightIn, combinedOverlayIn, true);
		}
	}
}
