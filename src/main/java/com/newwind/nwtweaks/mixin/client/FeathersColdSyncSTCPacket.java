package com.newwind.nwtweaks.mixin.client;

import com.elenai.feathers.client.ClientFeathersData;
import com.elenai.feathers.config.FeathersClientConfig;
import com.elenai.feathers.networking.packet.ColdSyncSTCPacket;
import com.newwind.nwtweaks.util.CommonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import org.spongepowered.asm.mixin.*;

import java.util.function.Supplier;

@Mixin(ColdSyncSTCPacket.class)
public abstract class FeathersColdSyncSTCPacket {

	@Mutable
	@Final
	@Shadow(
					remap = false
	)
	private final boolean cold;

	protected FeathersColdSyncSTCPacket(boolean cold) {
		this.cold = cold;
	}

	/**
	 * @author Kevadroz
	 * @reason Implement overheated sound
	 */
	@Overwrite(
					remap = false
	)
	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = (NetworkEvent.Context) supplier.get();
		context.enqueueWork(() -> {
			ClientFeathersData.setCold(this.cold);
			if (ClientFeathersData.isCold() && (Boolean) FeathersClientConfig.FROST_SOUND.get()) {
				Minecraft instance = Minecraft.getInstance();
				SoundEvent soundEvent = new SoundEvent(new ResourceLocation(
								CommonUtils.getEffectivePlayerTemperature(instance.player) < 0D ?
												"entity.player.hurt_freeze" :
												"entity.player.hurt_on_fire"));
				instance.level.playLocalSound(instance.player.blockPosition(), soundEvent, SoundSource.PLAYERS, 1.0F, instance.level.random.nextFloat(), false);
			}

		});
		return true;
	}

}
