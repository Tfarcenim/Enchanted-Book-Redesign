package tfar.enchantedbookredesign.platform;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import tfar.enchantedbookredesign.EnchantedBookRedesignNeoForge;
import tfar.enchantedbookredesign.platform.services.IPlatformHelper;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public MLConfig getConfig() {
        return EnchantedBookRedesignNeoForge.CLIENT;
    }
}