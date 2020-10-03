package tfar.enchantedbookredesign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

@Mod(EnchantedBookRedesign.MODID)
public class EnchantedBookRedesign {

	public static final String MODID = "enchantedbookredesign";

	public EnchantedBookRedesign() {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
	}

	private void setupClient(final FMLClientSetupEvent event) {
		ItemModelsProperties.registerProperty(Items.ENCHANTED_BOOK, new ResourceLocation(MODID, "level"),
						(stack, world, entity) -> {
							Map<Enchantment, Integer> enchs = EnchantmentHelper.getEnchantments(stack);
							if (enchs.isEmpty())
								return 1;

							int level = 1;
							for (Map.Entry<Enchantment, Integer> entry : enchs.entrySet()) {
								if (entry.getKey().isCurse())
									return 0;

								level = Math.max(level, entry.getValue());
							}
							return level;
						});

		Minecraft mc = Minecraft.getInstance();
		ItemColors itemColors = mc.getItemColors();
		itemColors.register((stack, tintIndex) -> tintIndex != 1 ? -1 : Hooks.getColor(stack), Items.ENCHANTED_BOOK);
	}
}
