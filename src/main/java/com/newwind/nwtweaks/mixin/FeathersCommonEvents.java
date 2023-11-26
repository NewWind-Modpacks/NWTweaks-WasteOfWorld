package com.newwind.nwtweaks.mixin;

import com.elenai.feathers.capability.PlayerFeathersProvider;
import com.elenai.feathers.event.CommonEvents;
import com.elenai.feathers.networking.FeathersMessages;
import com.elenai.feathers.networking.packet.ColdSyncSTCPacket;
import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.util.CommonUtils;
import com.stereowalker.survive.world.entity.ai.attributes.SAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CommonEvents.class)
public abstract class FeathersCommonEvents {

	/**
	 * @author Kevadroz
	 * @reason Effect replaced by internal handling
	 */
	@Overwrite(remap = false)
	private static void handleFrostEffect(PlayerTickEvent event) {
		if (event.player instanceof ServerPlayer player)
			player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(feathers -> {
				if (Math.abs(CommonUtils.getEffectivePlayerTemperature(player)) > NWConfig.Common.FEATHERS_STALL_TEMP.get()) {
					if (!feathers.isCold()) {
						feathers.setCold(true);
						FeathersMessages.sendToPlayer(new ColdSyncSTCPacket(feathers.isCold()), player);
					}
				} else if (feathers.isCold()) {
					feathers.setCold(false);
					FeathersMessages.sendToPlayer(new ColdSyncSTCPacket(feathers.isCold()), player);
				}
			});
	}

	;

}
