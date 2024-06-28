package tfar.enchantedbookredesign;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import tfar.enchantedbookredesign.platform.Services;

import java.util.Map;

public class Hooks {

	public static ItemStack stack = ItemStack.EMPTY;

	public static final ResourceLocation TINTED_GLINT_RL = EnchantedBookRedesign.id(
					"textures/misc/enchanted_item_glint.png");
	public static ShaderInstance rendertype_tinted_glint_direct;

	public static final int FALLBACK = 0x6B541A;
	public static final int ARMOR = 0x00FF00;
	public static final int SWORD = 0xFF0000;
	public static final int FISHING = 0x0000FF;
	public static final int TRIDENT = 0x9F7FFF;
	public static final int BOW = 0xFF7B00;
	public static final int  CROSSBOW = 0x00FFFF;
	public static final int  DIGGER = 0x774F27;

	public static int getColor(ItemStack stack) {
			ItemEnchantments enchs = stack.get(DataComponents.STORED_ENCHANTMENTS);
			if (enchs == null || enchs.isEmpty())
					return 0xFFFFFF;

			double r = 0;
			double g = 0;
			double b = 0;
			double buckets = 0;

			int potency = 0;
			int potentialPotency = 0;

			for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchs.entrySet()) {
					Holder<Enchantment> enchantmentHolder = entry.getKey();
					Enchantment enchantment = enchantmentHolder.value();
					int power = entry.getIntValue() + 1;
					int maxPower = enchantment.getMaxLevel() + 1;
					potency += power;
					potentialPotency += maxPower;

					int color = 0;

					if (!enchantmentHolder.is(EnchantmentTags.CURSE)) {

						Enchantment.EnchantmentDefinition enchantmentDefinition = enchantment.definition();
						HolderSet<Item> allowedItems;
						if (enchantmentDefinition.primaryItems().isEmpty()) {
							allowedItems = enchantment.definition().supportedItems();
						} else {
							allowedItems = enchantment.definition().primaryItems().get();
						}

						TagKey<Item> itemTag = allowedItems.unwrapKey().orElse(null);
						if (itemTag != null) {
							ResourceLocation key = itemTag.location();
							color = Services.PLATFORM.getConfig().getColorMap().getOrDefault(key,FALLBACK);
						} else {
							color = FALLBACK;
						}
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
