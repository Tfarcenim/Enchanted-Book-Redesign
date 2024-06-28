package tfar.enchantedbookredesign;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import tfar.enchantedbookredesign.platform.Services;

import java.util.Map;

public class Hooks {

	public static ItemStack stack = ItemStack.EMPTY;

	public static final ResourceLocation TINTED_GLINT_RL = EnchantedBookRedesign.id(
					"textures/misc/enchanted_item_glint.png");
	public static ShaderInstance rendertype_tinted_glint_direct;


	public static int getColor(ItemStack stack) {
			Map<Enchantment, Integer> enchs = EnchantmentHelper.getEnchantments(stack);
			if (enchs.isEmpty())
					return 0xFFFFFF;

			double r = 0;
			double g = 0;
			double b = 0;
			double buckets = 0;

			int potency = 0;
			int potentialPotency = 0;

			for (Map.Entry<Enchantment, Integer> entry : enchs.entrySet()) {
					Enchantment ench = entry.getKey();
					int power = entry.getValue() + 1;
					int maxPower = ench.getMaxLevel() + 1;
					potency += power;
					potentialPotency += maxPower;

					int color = 0;

					if (!ench.isCurse()) {
							if (ench.category == null)
									color = 0x6B541A;
							else color = switch (ench.category) {
                                case ARMOR, ARMOR_FEET, ARMOR_LEGS, ARMOR_CHEST, ARMOR_HEAD, WEARABLE -> 0x00FF00;
                                case WEAPON -> 0xFF0000;
                                case DIGGER -> 0x774F27;
                                case FISHING_ROD -> 0x0000FF;
                                case TRIDENT -> 0x9F7FFF;
                                case BOW -> 0xFF7B00;
                                case CROSSBOW -> 0x00FFFF;
                                default -> 0x6B541A;
                            };
					}

					int cr = (color & 0xFF0000) >> 16;
					int cg = (color & 0xFF00) >> 8;
					int cb = color & 0xFF;
					double levelMul = (double) power / maxPower;
					r += cr * levelMul;
					g += cg * levelMul;
					b += cb * levelMul;
					buckets += levelMul;
			}

			double multiplier = (1 + (double) potency / potentialPotency) / (2 * buckets);

			int trueR = (int) (r * multiplier);
			int trueG = (int) (g * multiplier);
			int trueB = (int) (b * multiplier);

			return 0xFF000000 | (trueR << 16) | (trueG << 8) | trueB;
	}


	public static VertexConsumer buildConsumer(MultiBufferSource bufferIn, RenderType renderTypeIn, boolean isItem, boolean glint) {
		if (glint && Services.PLATFORM.getConfig().whitelistedItems().contains(Hooks.stack.getItem())) {
			VertexConsumer builder2 = VertexMultiConsumer.create(
					TintedVertexConsumer.withTint(
							bufferIn.getBuffer(isItem ? ModRenderType.TINTED_GLINT_DIRECT : ModRenderType.TINTED_GLINT_DIRECT)
							, Hooks.getColor(Hooks.stack)),
					bufferIn.getBuffer(renderTypeIn));
			return builder2;
		}
		return null;
	}

	public static ShaderInstance getRendertype_tinted_glint_direct() {
		return rendertype_tinted_glint_direct;
	}
}
