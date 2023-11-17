package com.newwind.nwtweaks.networking.packet;

import com.newwind.nwtweaks.client.NWClient;
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
			NWClient.isUnderground = this.isUnderground;
		});
		return true;
	}
}
