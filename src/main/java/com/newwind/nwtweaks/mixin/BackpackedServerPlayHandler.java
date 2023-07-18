package com.newwind.nwtweaks.mixin;

import com.mrcrayfish.backpacked.network.message.MessageOpenBackpack;
import com.mrcrayfish.backpacked.network.play.ServerPlayHandler;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ServerPlayHandler.class)
public class BackpackedServerPlayHandler {

	/**
	 * @author Kevadroz
	 * @reason Backpacks own inventories are disabled, so it shouldn't be possible to open them
	 */
	@Overwrite(
					remap = false
	)
	public static void handleOpenBackpack(MessageOpenBackpack message, ServerPlayer player) {
	}

}
