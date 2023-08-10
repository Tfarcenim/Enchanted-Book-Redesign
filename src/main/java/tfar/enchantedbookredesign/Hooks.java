package tfar.enchantedbookredesign;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class Hooks {

	public static ItemStack stack = ItemStack.EMPTY;

	public static final ResourceLocation TINTED_GLINT_RL = new ResourceLocation(EnchantedBookRedesign.MODID,
					"textures/misc/enchanted_item_glint.png");


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
							else switch (ench.category) {
									case ARMOR:
									case ARMOR_FEET:
									case ARMOR_LEGS:
									case ARMOR_CHEST:
									case ARMOR_HEAD:
									case WEARABLE:
											color = 0x00FF00;
											break;
									case WEAPON:
											color = 0xFF0000;
											break;
									case DIGGER:
											color = 0x774F27;
											break;
									case FISHING_ROD:
											color = 0x0000FF;
											break;
									case TRIDENT:
											color = 0x9F7FFF;
											break;
									case BOW:
											color = 0xFF7B00;
											break;
									case CROSSBOW:
											color = 0x00FFFF;
											break;
									default:
											color = 0x6B541A;
											break;
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
}
