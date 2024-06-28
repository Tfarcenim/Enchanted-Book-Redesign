package tfar.enchantedbookredesign;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.tuple.Pair;
import tfar.enchantedbookredesign.platform.MLConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod(EnchantedBookRedesign.MOD_ID)
public class EnchantedBookRedesignForge {

    public EnchantedBookRedesignForge() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, ()->new IExtensionPoint.DisplayTest(()->"ANY", (remote, isServer)-> true));
        if (FMLEnvironment.dist.isClient()) {
            IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
            bus.addListener(this::configLoad);
            bus.addListener(this::shaders);
            bus.addListener(this::itemColors);
            EnchantedBookRedesign.init();
        } else {
            System.out.println("why is this on the server?");
        }
    }

    private void itemColors(RegisterColorHandlersEvent.Item event) {
        EnchantedBookRedesign.applyTints(event.getItemColors());
    }

    private void shaders(RegisterShadersEvent e) {
        try {
            e.registerShader(new ShaderInstance(e.getResourceProvider(),EnchantedBookRedesign.id("rendertype_tinted_glint_direct"), DefaultVertexFormat.POSITION_COLOR_TEX), (instance) -> {
                Hooks.rendertype_tinted_glint_direct = instance;
            });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static Set<Item> cache = new HashSet<>();

    public static class ClientConfig implements MLConfig {

        public static ForgeConfigSpec.ConfigValue<List<? extends String>> items;

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            builder.push("client");
            items = builder
                    .comment("Which items to override glint for")
                    .defineList("items",List.of("minecraft:enchanted_book"),String.class::isInstance);
            builder.pop();
        }

        @Override
        public Set<Item> whitelistedItems() {
            return cache;
        }
    }


    private void configLoad(ModConfigEvent e) {
        if (e.getConfig().getModId().equals(EnchantedBookRedesign.MOD_ID)) {
            cache.clear();
            for (String s : ClientConfig.items.get()) {
                Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(s));
                if (item == Items.AIR) {
                    System.out.println(s+" not found");
                } else {
                    cache.add(item);
                }
            }
        }
    }
}
