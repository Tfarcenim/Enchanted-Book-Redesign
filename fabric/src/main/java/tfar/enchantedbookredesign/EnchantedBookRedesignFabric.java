package tfar.enchantedbookredesign;

import net.fabricmc.api.ClientModInitializer;

public class EnchantedBookRedesignFabric implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        EnchantedBookRedesign.init();
    }
}
