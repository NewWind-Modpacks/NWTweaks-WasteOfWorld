package com.newwind.nwtweaks;

import com.mojang.logging.LogUtils;
import com.newwind.nwtweaks.registries.Attributes;
import com.newwind.nwtweaks.registries.Blocks;
import com.newwind.nwtweaks.registries.Enchantments;
import com.newwind.nwtweaks.registries.Items;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod(NWTweaks.MODID)
public class NWTweaks {

    @SuppressWarnings("SpellCheckingInspection")
    public static final String MODID = "nwtweaks";
    public static final Logger LOGGER = LogUtils.getLogger();


    public NWTweaks() {

        ModLoadingContext.get().registerConfig(Type.COMMON, NWConfig.Common.SPEC, "NWTweaks-Common.toml");
        ModLoadingContext.get().registerConfig(Type.CLIENT, NWConfig.Client.SPEC, "NWTweaks-Client.toml");

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        Items.ITEMS.register(bus);
//        Blocks.BLOCKS.register(bus);
        Attributes.ATTRIBUTES.register(bus);
        Enchantments.ENCHANTMENTS.register(bus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imc);

    }

    @SuppressWarnings({"removal"})
    private void clientSetup (final FMLClientSetupEvent event) {
        if (NWConfig.Client.SNOW_CUTOUT.get())
            ItemBlockRenderTypes.setRenderLayer(net.minecraft.world.level.block.Blocks.SNOW, RenderType.cutout());
    }

    private void imc(InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("head").build());
    }

}
