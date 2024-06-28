package tfar.enchantedbookredesign.platform;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import tfar.enchantedbookredesign.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Set;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public MLConfig getConfig() {
        return new MLConfig() {
            @Override
            public Set<Item> whitelistedItems() {
                return Set.of();
            }

            @Override
            public Object2IntMap<ResourceLocation> getColorMap() {
                return null;
            }
        };
    }
}
