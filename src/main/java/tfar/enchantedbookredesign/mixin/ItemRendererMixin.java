package tfar.enchantedbookredesign.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
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
	private void capturestack(ItemStack itemStackIn, ItemTransforms.TransformType transformTypeIn, boolean leftHand, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, BakedModel modelIn, CallbackInfo ci) {
		Hooks.stack = itemStackIn;
	}

	//stop the vanilla glint from drawing at all if our conditions are met
	@Inject(method = "getFoilBufferDirect", at = @At("HEAD"), cancellable = true)
	private static void tintedglint(MultiBufferSource bufferIn, RenderType renderTypeIn, boolean isItem, boolean glint, CallbackInfoReturnable<VertexConsumer> cir) {
			if (glint && EnchantedBookRedesign.cache.contains(Hooks.stack.getItem())) {
				VertexConsumer builder2 = VertexMultiConsumer.create(
								TintedVertexConsumer.withTint(
												bufferIn.getBuffer(isItem ? ModRenderType.TINTED_GLINT_DIRECT : ModRenderType.TINTED_ENTITY_GLINT_DIRECT)
												, Hooks.getColor(Hooks.stack)),
								bufferIn.getBuffer(renderTypeIn));
				cir.setReturnValue(builder2);
			}
	}
}