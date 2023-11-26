package com.newwind.nwtweaks.networking.packet;

import com.newwind.nwtweaks.capability.RedDwellerProvider;
import de.cadentem.cave_dweller.entities.CaveDwellerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CRedDweller {

	private final int dwellerId;
	private final boolean isRedDweller;

	public S2CRedDweller(int dwellerId, boolean isRedDweller) {
		this.dwellerId = dwellerId;
		this.isRedDweller = isRedDweller;
	}

	public S2CRedDweller(FriendlyByteBuf buf) {
		this.dwellerId = buf.readInt();
		this.isRedDweller = buf.readBoolean();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeInt(dwellerId);
		buf.writeBoolean(isRedDweller);
	}

	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			LocalPlayer player = Minecraft.getInstance().player;
			if (player != null) {
				Entity levelEntity = player.level.getEntity(this.dwellerId);
				if (levelEntity instanceof CaveDwellerEntity dweller) {
					dweller.getCapability(RedDwellerProvider.CAPABILITY).ifPresent(redDweller -> redDweller.setRedDweller(this.isRedDweller));
				}
			}
		});
		return true;
	}
}
