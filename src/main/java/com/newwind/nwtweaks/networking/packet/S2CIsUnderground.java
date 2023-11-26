package com.newwind.nwtweaks.networking.packet;

import com.newwind.nwtweaks.capability.IsUndergroundProvider;
import com.newwind.nwtweaks.client.NWClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CIsUnderground {

	private final boolean isUnderground;

	public S2CIsUnderground (boolean isUnderground) {
		this.isUnderground = isUnderground;
	}

	public S2CIsUnderground(FriendlyByteBuf buf) {
		this.isUnderground = buf.readBoolean();

	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBoolean(isUnderground);
	}

	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			assert Minecraft.getInstance().player != null;
			Minecraft.getInstance().player.getCapability(IsUndergroundProvider.CAPABILITY).ifPresent(isUnderground1 -> isUnderground1.setUnderground(this.isUnderground ? 1 : 0));
		});
		return true;
	}
}
