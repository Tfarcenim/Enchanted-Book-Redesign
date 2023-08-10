package tfar.enchantedbookredesign;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod(EnchantedBookRedesign.MODID)
public class EnchantedBookRedesign {

	public static final String MODID = "enchantedbookredesign";


	public EnchantedBookRedesign() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, ()->new IExtensionPoint.DisplayTest(()->"ANY", (remote, isServer)-> true));
		if (FMLEnvironment.dist.isClient()) {
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::configLoad);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::shaders);
		} else {
			System.out.println("why is this on the server?");
		}
	}

	private void shaders(RegisterShadersEvent e) {
		try {
			e.registerShader(new ShaderInstance(e.getResourceManager(), new ResourceLocation(MODID,"rendertype_tinted_glint_direct"), DefaultVertexFormat.POSITION_COLOR_TEX), (p_172803_) -> {
				Hooks.rendertype_tinted_glint_direct = p_172803_;
			});
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void setupClient(final FMLClientSetupEvent event) {
		ItemProperties.register(Items.ENCHANTED_BOOK, new ResourceLocation(MODID, "level"),
						(stack, world, entity,a) -> {
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

	public static final ClientConfig CLIENT;
	public static final ForgeConfigSpec CLIENT_SPEC;

	static {
		final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static Set<Item> cache = new HashSet<>();

	public static class ClientConfig {

		public static ForgeConfigSpec.ConfigValue<List<? extends String>> items;

		public ClientConfig(ForgeConfigSpec.Builder builder) {
			builder.push("client");
			items = builder
					.comment("Which items to override glint for")
					.defineList("items",List.of("minecraft:enchanted_book"),String.class::isInstance);
			builder.pop();
		}
	}


	private void configLoad(ModConfigEvent e) {
		if (e.getConfig().getModId().equals(MODID)) {
			cache.clear();
			for (String s : ClientConfig.items.get()) {
				Item item = Registry.ITEM.get(new ResourceLocation(s));
				if (item == Items.AIR) {
					System.out.println(s+" not found");
				} else {
					cache.add(item);
				}
			}
		}
	}
}
