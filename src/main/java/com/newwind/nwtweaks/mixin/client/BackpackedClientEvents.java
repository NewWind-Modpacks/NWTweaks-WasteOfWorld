package com.newwind.nwtweaks.mixin.client;

import com.mrcrayfish.backpacked.client.ClientEvents;
import com.mrcrayfish.backpacked.network.message.MessageOpenBackpack;
import com.mrcrayfish.backpacked.network.message.MessageRequestCustomisation;
import net.minecraftforge.network.simple.SimpleChannel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientEvents.class)
public class BackpackedClientEvents {

	@Redirect(
					method = "onKeyInput",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraftforge/network/simple/SimpleChannel;sendToServer(Ljava/lang/Object;)V"
					),
					remap = false
	)
	private <MSG> void openCustomization (SimpleChannel instance, MSG message) {
		instance.sendToServer(new MessageRequestCustomisation());
	}

}
