package tfar.enchantedbookredesign;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.enchantedbookredesign.platform.MLConfig;

import java.io.IOException;
import java.util.*;

@Mod(value = EnchantedBookRedesign.MOD_ID, dist = Dist.CLIENT)
public class EnchantedBookRedesignNeoForge {

    public EnchantedBookRedesignNeoForge(IEventBus bus, ModContainer modContainer, Dist dist) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
        bus.addListener(this::configLoad);
        bus.addListener(this::shaders);
        bus.addListener(this::itemColors);
        // NeoForge.EVENT_BUS.addListener(this::onLogin);
        EnchantedBookRedesign.init();
    }

    private void itemColors(RegisterColorHandlersEvent.Item event) {
        EnchantedBookRedesign.applyTints(event.getItemColors());
    }

    private void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        MinecraftServer server = event.getEntity().getServer();
        Registry<Enchantment> enchantments = server.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        Set<ResourceLocation> possible = new HashSet<>();
        for (Enchantment enchantment : enchantments) {
            Enchantment.EnchantmentDefinition enchantmentDefinition = enchantment.definition();
            TagKey<Item> tagKey;
            if (enchantmentDefinition.primaryItems().isEmpty()) {
                tagKey = enchantment.definition().supportedItems().unwrapKey().get();
                System.out.println(enchantments.getKey(enchantment) + " : " + tagKey.location());
            } else {
                tagKey = enchantmentDefinition.primaryItems().get().unwrapKey().get();
                System.out.println(enchantments.getKey(enchantment) + " : " + tagKey.location());
            }
            possible.add(tagKey.location());
        }
        System.out.println(possible);

    }

    private void shaders(RegisterShadersEvent e) {
        try {
            e.registerShader(new ShaderInstance(e.getResourceProvider(), EnchantedBookRedesign.id("rendertype_tinted_glint_direct"), DefaultVertexFormat.POSITION_TEX_COLOR), (instance) -> {
                Hooks.rendertype_tinted_glint_direct = instance;
            });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static final ClientConfig CLIENT;
    public static final ModConfigSpec CLIENT_SPEC;

    static {
        final Pair<ClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static Set<Item> cache = new HashSet<>();
    public static Object2IntMap<ResourceLocation> color_cache = new Object2IntOpenHashMap<>();

    public static class ClientConfig implements MLConfig {

        public static ModConfigSpec.ConfigValue<List<? extends String>> items;

        public static ModConfigSpec.ConfigValue<List<? extends String>> colors;

        public static String[] defaults = new String[]{"minecraft:enchantable/mace|" + Hooks.FALLBACK,
                "minecraft:enchantable/bow|" + Hooks.BOW,
                "minecraft:enchantable/mining|" + Hooks.DIGGER,
                "minecraft:enchantable/leg_armor|" + Hooks.ARMOR,
                "minecraft:enchantable/crossbow|" + Hooks.CROSSBOW,
                "minecraft:enchantable/equippable|" + Hooks.ARMOR,
                "minecraft:enchantable/trident|" + Hooks.TRIDENT,
                "minecraft:enchantable/durability|" + Hooks.DIGGER,
                "minecraft:enchantable/head_armor|" + Hooks.ARMOR,
                "minecraft:enchantable/foot_armor|" + Hooks.ARMOR,
                "minecraft:enchantable/mining_loot|" + Hooks.DIGGER,
                "minecraft:enchantable/fishing|" + Hooks.FISHING,
                "minecraft:enchantable/vanishing|" + Hooks.FALLBACK,
                "minecraft:enchantable/sword|" + Hooks.SWORD,
                "minecraft:enchantable/armor|" + Hooks.ARMOR,
                "minecraft:enchantable/chest_armor|" + Hooks.ARMOR};

        public ClientConfig(ModConfigSpec.Builder builder) {
            builder.push("client");
            items = builder
                    .comment("Which items to override glint for")
                    .defineList("items", List.of("minecraft:enchanted_book"), String.class::isInstance);

            colors = builder
                    .comment("Enchantment tag to color map")
                    .defineList("colors", Arrays.stream(defaults).toList(), String.class::isInstance);
            builder.pop();
        }

        @Override
        public Set<Item> whitelistedItems() {
            return cache;
        }

        @Override
        public Object2IntMap<ResourceLocation> getColorMap() {
            return color_cache;
        }
    }


    private void configLoad(ModConfigEvent e) {
        if (e.getConfig().getModId().equals(EnchantedBookRedesign.MOD_ID)) {
            cache.clear();
            for (String s : ClientConfig.items.get()) {
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(s));
                if (item == Items.AIR) {
                    System.out.println(s + " not found");
                } else {
                    cache.add(item);
                }
            }
            color_cache.clear();

            for (String s : ClientConfig.colors.get()) {
                String[] split = s.split("\\|");
                try {
                    color_cache.put(ResourceLocation.parse(split[0]), Integer.parseInt(split[1]));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}
