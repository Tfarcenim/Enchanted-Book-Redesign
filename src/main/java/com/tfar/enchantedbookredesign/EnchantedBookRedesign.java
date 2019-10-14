package com.tfar.enchantedbookredesign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.PackCompatibility;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EnchantedBookRedesign.MODID)
public class EnchantedBookRedesign {
    // Directly reference a log4j logger.

    public static final String MODID = "enchantedbookredesign";

    private static final Logger LOGGER = LogManager.getLogger();

    private ResourcePack dummyPack = new ResourcePack();

    public EnchantedBookRedesign instance;

    public EnchantedBookRedesign() {

        ResourcePackList<ClientResourcePackInfo> rps = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getInstance(), "field_110448_aq");
        rps.addPackFinder(new IPackFinder() {

            @Override
            public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {
                NativeImage img = null;
                try {
                    img = NativeImage.read(dummyPack.getRootResourceStream("pack.png"));
                } catch (Exception e) {
                    LogManager.getLogger().error("Could not load blur's pack.png", e);
                }
                @SuppressWarnings("unchecked")
                T var3 = (T) new ClientResourcePackInfo(MODID, true, () -> dummyPack, new StringTextComponent(dummyPack.getName()), new StringTextComponent("Enchanted Book Redesign Resource pack"),
                        PackCompatibility.COMPATIBLE, ResourcePackInfo.Priority.TOP, true, img,false);
                nameToPackMap.put(MODID, var3);
            }
        });

        instance = this;
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        Items.ENCHANTED_BOOK.addPropertyOverride(new ResourceLocation("level"),
                new IItemPropertyGetter(){
            @Override
            public float call(ItemStack stack, @Nullable World p_call_2_, @Nullable LivingEntity p_call_3_) {
                Map<Enchantment,Integer> enchs = EnchantmentHelper.getEnchantments(stack);
                if (enchs.isEmpty())return 1;
                int level = 1;
                for (Map.Entry<Enchantment, Integer> entry : enchs.entrySet()) {
                    level = entry.getValue();
                    if (entry.getKey().isCurse())level = 0;
                    break;
                }
                return level;
            }
        });

        Minecraft mc = Minecraft.getInstance();
        ItemColors itemColors = mc.getItemColors();
        itemColors.register((stack, tintIndex) -> {

            return tintIndex != 1 ? -1 : getColor(stack);
        }, Items.ENCHANTED_BOOK);
    }

    public static int getColor(ItemStack stack){
        Map<Enchantment,Integer> enchs = EnchantmentHelper.getEnchantments(stack);
        if (enchs.isEmpty())return 0xFFFFFF;
        Enchantment first = null;
        int level = 1;
        for (Map.Entry<Enchantment,Integer> entry: enchs.entrySet()){
            first = entry.getKey();
            level = entry.getValue();
            break;
        }
        if (first.isCurse())return 0;
        if (first.canApply(new ItemStack(Items.DIAMOND_PICKAXE)))return 0x654321;
        if (first.canApply(new ItemStack(Items.DIAMOND_SWORD)))return 0xFF0000;
        if (first.canApply(new ItemStack(Items.BOW)))return 0xFF8000;
        if (first.canApply(new ItemStack(Items.DIAMOND_HELMET))
                ||first.canApply(new ItemStack(Items.DIAMOND_CHESTPLATE))
                || first.canApply(new ItemStack(Items.DIAMOND_LEGGINGS))
                || first.canApply(new ItemStack(Items.DIAMOND_BOOTS)))return 0x00FF00;

        if (first.canApply(new ItemStack(Items.TRIDENT)))return 0xA17FFF;
        if (first.canApply(new ItemStack(Items.CROSSBOW)))return 0x00FFFF;

        return 0xFFFFFF;
    }
}
