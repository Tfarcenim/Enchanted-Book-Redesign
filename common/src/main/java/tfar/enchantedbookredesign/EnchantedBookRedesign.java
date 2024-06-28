package tfar.enchantedbookredesign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;
import tfar.enchantedbookredesign.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class EnchantedBookRedesign {

    public static final String MOD_ID = "enchantedbookredesign";
    public static final String MOD_NAME = "EnchantedBookRedesign";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        ItemProperties.register(Items.ENCHANTED_BOOK, id("level"),
                new ClampedItemPropertyFunction() {
                    @Override
                    public float call(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                        return unclampedCall(itemStack,clientLevel,livingEntity,i);
                    }

                    @Override
                    public float unclampedCall(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                        Map<Enchantment, Integer> enchs = EnchantmentHelper.getEnchantments(itemStack);
                        if (enchs.isEmpty())
                            return 1;

                        int level = 1;
                        for (Map.Entry<Enchantment, Integer> entry : enchs.entrySet()) {
                            if (entry.getKey().isCurse())
                                return 0;

                            level = Math.max(level, entry.getValue());
                        }
                        return level;
                    }
                });

    }

    public static ItemColor itemColor = (stack, tintIndex) -> tintIndex != 1 ? -1 : Hooks.getColor(stack);

    public static void applyTints(ItemColors itemColors) {
        itemColors.register(itemColor, Items.ENCHANTED_BOOK);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID,path);
    }

}