package com.newwind.nwtweaks.networking.packet;

import com.newwind.nwtweaks.capability.IsUndergroundProvider;
import com.newwind.nwtweaks.client.NWClient;
import com.newwind.nwtweaks.world.items.PillItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CDiscoveredPills {

	private final int[] effects;

	public S2CDiscoveredPills(int[] effects) {
		this.effects = effects;
	}

	public S2CDiscoveredPills(FriendlyByteBuf buf) {
		this.effects = buf.readVarIntArray();

	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeVarIntArray(this.effects);
	}

	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			NWClient.pillEffects = new PillItem.PILL_EFFECT[this.effects.length];
			PillItem.PILL_EFFECT[] enumEffects = PillItem.PILL_EFFECT.values();
			for (int i = 0; i < this.effects.length; i++)
				if (this.effects[i] >= 0)
					NWClient.pillEffects[i] = enumEffects[this.effects[i]];
				else
					NWClient.pillEffects[i] = null;
		});
		return true;
	}
}
