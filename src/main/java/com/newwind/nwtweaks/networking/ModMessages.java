package com.newwind.nwtweaks.networking;

import com.newwind.nwtweaks.NWTweaks;
import com.newwind.nwtweaks.networking.packet.S2CDiscoveredPills;
import com.newwind.nwtweaks.networking.packet.S2CIsUnderground;
import com.newwind.nwtweaks.networking.packet.S2CRedDweller;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {

	public static SimpleChannel INSTANCE;

	private static int packetId = 0;
	private static int id() {
		return packetId++;
	}

	public static void register() {
		SimpleChannel net = NetworkRegistry.ChannelBuilder
						.named(new ResourceLocation(NWTweaks.MODID, "messages"))
						.networkProtocolVersion(() -> "1.0")
						.clientAcceptedVersions(s -> true)
						.serverAcceptedVersions(s -> true)
						.simpleChannel();

		INSTANCE = net;

		net.messageBuilder(S2CIsUnderground.class, id(), NetworkDirection.PLAY_TO_CLIENT)
						.decoder(S2CIsUnderground::new)
						.encoder(S2CIsUnderground::toBytes)
						.consumerMainThread(S2CIsUnderground::handle)
						.add();

		net.messageBuilder(S2CRedDweller.class, id(), NetworkDirection.PLAY_TO_CLIENT)
						.decoder(S2CRedDweller::new)
						.encoder(S2CRedDweller::toBytes)
						.consumerMainThread(S2CRedDweller::handle)
						.add();

		net.messageBuilder(S2CDiscoveredPills.class, id(), NetworkDirection.PLAY_TO_CLIENT)
						.decoder(S2CDiscoveredPills::new)
						.encoder(S2CDiscoveredPills::toBytes)
						.consumerMainThread(S2CDiscoveredPills::handle)
						.add();
	}

	public static <MSG> void sendToServer(MSG message) {
		INSTANCE.sendToServer(message);
	}

	public static <MSG> void sendToClient(MSG message, ServerPlayer player) {
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
	}

}
