package tfar.enchantedbookredesign.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.VertexBuilderUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.enchantedbookredesign.EnchantedBookRedesign;
import tfar.enchantedbookredesign.Hooks;
import tfar.enchantedbookredesign.ModRenderType;
import tfar.enchantedbookredesign.TintedVertexConsumer;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

	//capture the itemstack
	@Inject(method = "render", at = @At("HEAD"))
	private void capturestack(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformTypeIn, boolean leftHand, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, IBakedModel modelIn, CallbackInfo ci) {
		Hooks.stack = itemStackIn;
	}

	//stop the vanilla glint from drawing at all if our conditions are met
	@Inject(method = "getFoilBufferDirect", at = @At("HEAD"), cancellable = true)
	private static void tintedglint(IRenderTypeBuffer bufferIn, RenderType renderTypeIn, boolean isItem, boolean glint, CallbackInfoReturnable<IVertexBuilder> cir) {
			if (glint && EnchantedBookRedesign.cache.contains(Hooks.stack.getItem())) {
				IVertexBuilder builder2 = VertexBuilderUtils.create(
								TintedVertexConsumer.withTint(
												bufferIn.getBuffer(isItem ? ModRenderType.TINTED_GLINT_DIRECT : ModRenderType.TINTED_ENTITY_GLINT_DIRECT)
												, Hooks.getColor(Hooks.stack)),
								bufferIn.getBuffer(renderTypeIn));
				cir.setReturnValue(builder2);
			}
	}
}