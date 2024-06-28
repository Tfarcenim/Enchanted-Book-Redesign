package tfar.enchantedbookredesign.platform;

import it.unimi.dsi.fastutil.ints.Int2CharMaps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Set;

public interface MLConfig {

    Set<Item> whitelistedItems();
    Object2IntMap<ResourceLocation> getColorMap();

}
