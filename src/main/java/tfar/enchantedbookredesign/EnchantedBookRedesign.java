package tfar.enchantedbookredesign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resources.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.fml.packs.ModFileResourcePack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@Mod(EnchantedBookRedesign.MODID)
public class EnchantedBookRedesign {

    public static final String MODID = "enchantedbookredesign";

    private static ResourcePack internalResourcePack;

    public EnchantedBookRedesign() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            List<ModFileInfo> modFiles = ModList.get().getModFiles();
            for (ModFileInfo modFileInfo : modFiles){
                if (modFileInfo.getMods().get(0).getModId().equals(MODID)) {
                    internalResourcePack = new ModFileResourcePack(modFileInfo.getFile());
                    break;
                }
            }

            ResourcePackList<ClientResourcePackInfo> rps = Minecraft.getInstance().getResourcePackList();
            rps.addPackFinder(new IPackFinder() {
                @Override
                @SuppressWarnings("unchecked")
                public <T extends ResourcePackInfo> void addPackInfosToMap(@Nonnull Map<String, T> nameToPackMap, @Nonnull ResourcePackInfo.IFactory<T> packInfoFactory) {
                    T pack = (T) new ClientResourcePackInfo(MODID, true, () -> internalResourcePack,
                            new StringTextComponent(internalResourcePack.getName()), new StringTextComponent(internalResourcePack.getName()),
                            PackCompatibility.COMPATIBLE, ResourcePackInfo.Priority.TOP, true, null, true);
                    nameToPackMap.put(MODID, pack);
                }
            });

            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        });
    }

    @OnlyIn(Dist.CLIENT)
    private void setupClient(final FMLClientSetupEvent event) {

        Items.ENCHANTED_BOOK.addPropertyOverride(new ResourceLocation(MODID, "level"),
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
        itemColors.register((stack, tintIndex) -> tintIndex != 1 ? -1 : getColor(stack), Items.ENCHANTED_BOOK);
    }

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
                if (ench.type == null)
                    color = 0x6B541A;
                else switch (ench.type) {
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
