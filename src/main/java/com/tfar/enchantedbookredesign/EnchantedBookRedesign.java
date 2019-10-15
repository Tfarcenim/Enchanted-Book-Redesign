package com.tfar.enchantedbookredesign;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resources.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.tfar.enchantedbookredesign.EnchantedBookGlintHax.model;

// The value here should match an entry in the META-INF/mods.toml file
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
@Mod(EnchantedBookRedesign.MODID)
public class EnchantedBookRedesign {
  // Directly reference a log4j logger.

  public static final String MODID = "enchantedbookredesign";

  private static final Logger LOGGER = LogManager.getLogger();

  private static final ItemStack trident = new ItemStack(Items.TRIDENT);
  private static final ItemStack fishing_rod = new ItemStack(Items.FISHING_ROD);
  private static final ItemStack bow = new ItemStack(Items.BOW);
  private static final ItemStack crossbow = new ItemStack(Items.CROSSBOW);
  private static final ItemStack pickaxe = new ItemStack(Items.DIAMOND_PICKAXE);
  private static final ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);

  private static final ItemStack helmet = new ItemStack(Items.DIAMOND_HELMET);
  private static final ItemStack chestplate = new ItemStack(Items.DIAMOND_CHESTPLATE);
  private static final ItemStack leggings = new ItemStack(Items.DIAMOND_LEGGINGS);
  private static final ItemStack boots = new ItemStack(Items.DIAMOND_BOOTS);



  private InternalResourcePack internalResourcePack;

  public EnchantedBookRedesign() {
    List<ModFileInfo> modFiles = ModList.get().getModFiles();
    for (ModFileInfo modFileInfo : modFiles){
      if (modFileInfo.getMods().get(0).getModId().equals(MODID)) {
        internalResourcePack = new InternalResourcePack(modFileInfo.getFile());
        break;
      }
    }
    ResourcePackList<ClientResourcePackInfo> rps = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getInstance(), "field_110448_aq");
    rps.addPackFinder(new IPackFinder() {

      @Override
      public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {
        T var3 = (T) new ClientResourcePackInfo(MODID, true, () -> internalResourcePack, new StringTextComponent(internalResourcePack.getName()), new StringTextComponent(internalResourcePack.getName()),
                PackCompatibility.COMPATIBLE, ResourcePackInfo.Priority.TOP, true, null,true);
        nameToPackMap.put(MODID, var3);
      }
    });

    //instance = this;
    // Register the doClientStuff method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
  }

  private void doClientStuff(final FMLClientSetupEvent event) {
   // ((SimpleReloadableResourceManager)Minecraft.getInstance().getResourceManager()).addReloadListener(internalResourcePack);

    Supplier<ItemStackTileEntityRenderer> renderer = EnchantedBookGlintHax::new;
    ObfuscationReflectionHelper.setPrivateValue(Item.class,Items.ENCHANTED_BOOK,renderer,"teisr");

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
    if (first.canApply(pickaxe))return 0x654321;
    if (first.canApply(sword))return 0x8F0000;
    if (first.canApply(bow))return 0xDD7000;
    if (first.canApply(helmet)
            ||first.canApply(chestplate)
            || first.canApply(leggings)
            || first.canApply(boots))return 0x008F00;

    if (first.canApply(trident))return 0x5E4B96;
    if (first.canApply(crossbow))return 0x008F8F;
    if (first.canApply(fishing_rod))return 0x00008F;


    return 0xFFFFFF;
  }
  public static final ModelResourceLocation modelLocation =
          new ModelResourceLocation("enchanted_book","inventory");
  @SubscribeEvent
  public static void bake(ModelBakeEvent e){
    Map<ResourceLocation, IBakedModel> models = e.getModelRegistry();
      ResourceLocation rl = modelLocation;
        IBakedModel oldmodel = models.get(rl);
        if (oldmodel instanceof SimpleBakedModel) {
          ItemOverrideList overrideList = ObfuscationReflectionHelper.getPrivateValue(SimpleBakedModel.class,
                  (SimpleBakedModel)oldmodel, "field_188620_g");
          List<IBakedModel> overridemodellist =
                  ObfuscationReflectionHelper.getPrivateValue(ItemOverrideList.class,overrideList,"field_209582_c");
          List<IBakedModel> newoverridelist = new ArrayList<>();
          for (IBakedModel iBakedModel : overridemodellist){
            newoverridelist.add(new BookWrapper(iBakedModel));
          }
          ObfuscationReflectionHelper.setPrivateValue(ItemOverrideList.class,overrideList,newoverridelist,"field_209582_c");
          BookWrapper model = new BookWrapper(oldmodel);
          EnchantedBookGlintHax.model = model;
           models.put(rl, model);
    }
  }
}
