package tfar.enchantedbookredesign.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import tfar.enchantedbookredesign.Hooks;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

	//capture the itemstack
	@Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IILnet/minecraft/client/renderer/model/IBakedModel;)V", at = @At("HEAD"))
	private void capturestack(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformTypeIn, boolean leftHand, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, IBakedModel modelIn, CallbackInfo ci) {
		Hooks.stack = itemStackIn;
	}

	//stop the vanilla glint from drawing at all if our conditions are met
	@Inject(method = "func_239391_c_", at = @At("HEAD"), cancellable = true)
	private static void tintedglint(IRenderTypeBuffer bufferIn, RenderType renderTypeIn, boolean isItemIn, boolean glint, CallbackInfoReturnable<IVertexBuilder> cir) {
		if (glint && Hooks.stack.getItem() == Items.ENCHANTED_BOOK) {
			IVertexBuilder builder2 = bufferIn.getBuffer(renderTypeIn);
			cir.setReturnValue(builder2);
			Hooks.renderTint = true;
		}
	}

	//render our glint
	@Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IILnet/minecraft/client/renderer/model/IBakedModel;)V",
					at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderModel(Lnet/minecraft/client/renderer/model/IBakedModel;Lnet/minecraft/item/ItemStack;IILcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;)V")
					)
	private void renderGlint(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformTypeIn, boolean leftHand, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, IBakedModel modelIn, CallbackInfo ci) {
		Hooks.renderGlintModelOverride(itemStackIn, matrixStackIn,bufferIn,combinedLightIn, combinedOverlayIn,modelIn);
	}
}
