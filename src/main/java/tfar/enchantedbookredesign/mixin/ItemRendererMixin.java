package tfar.enchantedbookredesign.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import tfar.enchantedbookredesign.Hooks;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

	@Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IILnet/minecraft/client/renderer/model/IBakedModel;)V", at = @At("HEAD"))
	private void capturestack(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformTypeIn, boolean leftHand, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, IBakedModel modelIn, CallbackInfo ci) {
		Hooks.stack = itemStackIn;
	}

	//render our glint
	@Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IILnet/minecraft/client/renderer/model/IBakedModel;)V",
					at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderModel(Lnet/minecraft/client/renderer/model/IBakedModel;Lnet/minecraft/item/ItemStack;IILcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;)V", shift = At.Shift.AFTER)
					, locals = LocalCapture.CAPTURE_FAILHARD)
	private void renderGlint(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformTypeIn, boolean leftHand, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, IBakedModel modelIn, CallbackInfo ci, boolean flag, boolean flag1, RenderType rendertype, RenderType rendertype1, IVertexBuilder ivertexbuilder) {
		Hooks.renderGlint(itemStackIn, transformTypeIn, leftHand, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn,  modelIn,ci, flag,flag1, rendertype,rendertype1,ivertexbuilder);
	}



	//cancel the vanilla glint and render our own
	@Inject(method = "getBuffer", at = @At("HEAD"), cancellable = true)
	private static void tintedglint(IRenderTypeBuffer bufferIn, RenderType renderTypeIn, boolean isItemIn, boolean glint, CallbackInfoReturnable<IVertexBuilder> cir) {
		if (glint && Hooks.stack.getItem() == Items.ENCHANTED_BOOK) {
			IVertexBuilder builder2 = bufferIn.getBuffer(renderTypeIn);
			cir.setReturnValue(builder2);
			Hooks.renderTint = true;
		}
	}
}
