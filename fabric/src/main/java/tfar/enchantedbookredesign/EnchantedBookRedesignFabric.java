package tfar.enchantedbookredesign;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.world.item.Items;

public class EnchantedBookRedesignFabric implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        EnchantedBookRedesign.init();
        ColorProviderRegistry.ITEM.register(EnchantedBookRedesign.itemColor, Items.ENCHANTED_BOOK);
        CoreShaderRegistrationCallback.EVENT.register(context -> {
            context.register(EnchantedBookRedesign.id("rendertype_tinted_glint_direct"), DefaultVertexFormat.POSITION_TEX_COLOR,
                    (instance) -> Hooks.rendertype_tinted_glint_direct = instance);
        });
    }
}
