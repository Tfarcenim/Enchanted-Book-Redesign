package tfar.enchantedbookredesign.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.enchantedbookredesign.Hooks;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

	//capture the itemstack
	@Inject(method = "render", at = @At("HEAD"))
	private void capturestack(ItemStack pItemStack, ItemDisplayContext pDisplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay, BakedModel pModel, CallbackInfo ci) {
		Hooks.stack = pItemStack;
	}

	//stop the vanilla glint from drawing at all if our conditions are met
	@Inject(method = "getFoilBufferDirect", at = @At("HEAD"), cancellable = true)
	private static void tintedglint(MultiBufferSource bufferIn, RenderType renderTypeIn, boolean isItem, boolean glint, CallbackInfoReturnable<VertexConsumer> cir) {
		VertexConsumer consumer = Hooks.buildConsumer(bufferIn,renderTypeIn,isItem,glint);
		if (consumer != null) {
			cir.setReturnValue(consumer);
		}
	}
}