package tfar.enchantedbookredesign.platform;

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
        return () -> Set.of(Items.ENCHANTED_BOOK);
    }
}
