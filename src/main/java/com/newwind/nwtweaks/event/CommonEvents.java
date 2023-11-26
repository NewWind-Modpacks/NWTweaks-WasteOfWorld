package com.newwind.nwtweaks.event;

import com.mrcrayfish.backpacked.item.BackpackItem;
import com.mrcrayfish.guns.item.GunItem;
import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.NWTweaks;
import com.newwind.nwtweaks.capability.*;
import com.newwind.nwtweaks.networking.ModMessages;
import com.newwind.nwtweaks.networking.packet.S2CDiscoveredPills;
import com.newwind.nwtweaks.networking.packet.S2CIsUnderground;
import com.newwind.nwtweaks.networking.packet.S2CRedDweller;
import com.newwind.nwtweaks.registries.Attributes;
import com.newwind.nwtweaks.registries.Blocks;
import com.newwind.nwtweaks.util.BreakChecks;
import com.newwind.nwtweaks.util.CommonUtils;
import com.newwind.nwtweaks.util.RadUtil;
import com.newwind.nwtweaks.world.blocks.ChippedBlock;
import com.newwind.nwtweaks.world.blocks.ChippedPillarBlock;
import com.newwind.nwtweaks.world.items.PillItem;
import de.cadentem.cave_dweller.entities.CaveDwellerEntity;
import dev.compactmods.machines.dimension.Dimension;
import dev.compactmods.machines.util.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.PacketDistributor;
import nuparu.tinyinv.utils.Utils;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;

import java.util.concurrent.atomic.AtomicBoolean;


@Mod.EventBusSubscriber(modid = NWTweaks.MODID)
public class CommonEvents {

	@SubscribeEvent
	public static void onPlayerTick(final PlayerTickEvent event) {
		handleRadiation(event.player);
		ServerPlayer player;
		if (event.player instanceof ServerPlayer) {
			player = (ServerPlayer) event.player;
		} else return;

		handleCompactKick(player);

		updateHeldStack(player);


	}

	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingTickEvent event) {
		Entity entity = event.getEntity();
		if (!entity.level.isClientSide) {
			entity.getCapability(IsUndergroundProvider.CAPABILITY).ifPresent(undergroundObject -> {
								undergroundObject.countDown();
								if (undergroundObject.getNextCheck() <= 0) {
									undergroundObject.resetCheck();
									boolean isUnderground = CommonUtils.checkUnderground(entity);
									if (isUnderground) {
										undergroundObject.addTime(undergroundObject.getNextCheck());
									} else {
										undergroundObject.setUnderground(0);
									}
									if (entity instanceof ServerPlayer player)
										ModMessages.sendToClient(new S2CIsUnderground(undergroundObject.isUnderground()), player);
								}
							}
			);
		}
	}

//	@SubscribeEvent
//	public static void onLivingHurt(LivingHurtEvent event) {
//		if (event.getEntity() instanceof CaveDwellerEntity caveDweller) {
//			caveDweller.getCapability(RedDwellerProvider.CAPABILITY).ifPresent(redDweller -> {
//				redDweller.setRedDweller(true);
//				ModMessages.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> caveDweller), new S2CRedDweller(caveDweller.getId(), redDweller.isRedDweller()));
//			});
//		}
//
//	}

//	@SubscribeEvent
//	public static void onEntitySpawn(LivingSpawnEvent event) {
//		if (!event.getLevel().isClientSide())
//			if (event.getEntity() instanceof CaveDwellerEntity dweller) {
//				dweller.getCapability(RedDwellerProvider.CAPABILITY).ifPresent(redDweller -> {
//					redDweller.setRedDweller(false);
//					ModMessages.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> dweller), new S2CRedDweller(dweller.getId(), redDweller.isRedDweller()));
//				});
//			}
//	}

	@SubscribeEvent
	public static void onPlayerTrack(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof CaveDwellerEntity dweller) {
			dweller.getCapability(RedDwellerProvider.CAPABILITY).ifPresent(redDweller -> {
				ModMessages.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> dweller), new S2CRedDweller(dweller.getId(), redDweller.isRedDweller()));
			});
		}
	}

	private static void handleCompactKick(ServerPlayer player) {
		ServerLevel level = player.getLevel();

		if (level.dimension().equals(Dimension.COMPACT_DIMENSION) && CommonUtils.areCMUnavailable(level.getServer(), player))
			PlayerUtil.teleportPlayerOutOfMachine(level, player);
	}

	private static void handleRadiation(Player player) {
		if (NWConfig.Common.ENABLE_RADIATION.get() && player.getFeetBlockState().getBlock() == net.minecraft.world.level.block.Blocks.SNOW)
			RadUtil.modifyRadiation(player, 0.005);
		else
			RadUtil.modifyRadiation(player, -0.001);
	}

	//@SubscribeEvent
	//public static void onEntityDamage(final LivingAttackEvent event) {
	//	if ((event.getSource().getEntity() instanceof ServerPlayer player)
	//					&& (event.getSource() instanceof EntityDamageSource) && !(event.getSource() instanceof IndirectEntityDamageSource)
	//					&& ProfileConfig.shouldStop(player, AoSAction.ATTACKING))
	//		event.setCanceled(true);
	//}

	private static void updateHeldStack(Player player) {
		String tag = NWConfig.Common.RELOAD_SPEED_TAG.get();

		updateTags(player.getMainHandItem(), tag, player);
		updateTags(player.getOffhandItem(), tag, player);

	}

	private static void updateTags(ItemStack stack, String tag, Player player) {
		boolean hasTag = player.getTags().contains(tag);
		CompoundTag stackTag = stack.getTag();
		Item item = stack.getItem();
		if (stackTag == null) return;
		if (item instanceof BowItem
						|| item instanceof CrossbowItem
						|| item instanceof GunItem
						|| item instanceof ModularBowItem
						|| item instanceof ModularCrossbowItem

						&& (!stackTag.contains(tag) || stackTag.getBoolean(tag) != hasTag))
			stackTag.putBoolean(tag, hasTag);
	}

	@SubscribeEvent
	public static void onCurioEquip(final CurioEquipEvent event) {
		if (event.getEntity() instanceof Player player) {
			if (event.getStack().getItem() instanceof BackpackItem)
				Utils.fixContainer(player.containerMenu, player);
		}
	}

	@SubscribeEvent
	public static void onCurioUnequip(final CurioUnequipEvent event) {
		if (event.getEntity() instanceof Player player)
			if (event.getStack().getItem() instanceof BackpackItem) {
				if (isBackpackUnused(player))
					Utils.fixContainer(player.containerMenu, player);
				else
					event.setCanceled(true);
			}
	}

	private static boolean isBackpackUnused(Player player) {
		AbstractContainerMenu container = player.containerMenu;
		for (int i = 0; i < container.slots.size(); ++i) {
			Slot slot = (Slot) container.slots.get(i);
			if (CommonUtils.tinyInvShouldBeRemoved(slot, player, container, false)
							&& !slot.getItem().isEmpty())
				return false;
		}
		return true;
	}


	@SubscribeEvent
	public static void onEnchantmentlevel(final EnchantmentLevelSetEvent event) {
		if (event.getItem().getItem() instanceof BackpackItem && event.getEnchantLevel() < NWConfig.Common.BACKPACK_MIN_POWER.get()) {
			if (event.getEnchantRow() == 2)
				event.setEnchantLevel(1);
			else
				event.setEnchantLevel(0);
		}
	}

	@SubscribeEvent
	public static void onBlockBreak(final BlockEvent.BreakEvent event) {
		BlockState state = event.getState();
		Block block = state.getBlock();
		Player player = event.getPlayer();
		LevelAccessor level = event.getLevel();
		BlockPos pos = event.getPos();
		BlockState setBlock = null;
		if (!player.isCreative())
			if (block == net.minecraft.world.level.block.Blocks.STONE) {
				setBlock = Blocks.CHIPPED_STONE.get().defaultBlockState();
			} else if (block == net.minecraft.world.level.block.Blocks.DEEPSLATE) {
				setBlock = Blocks.CHIPPED_DEEPSLATE.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
			} else if (block instanceof ChippedBlock && state.getValue(ChippedBlock.STAGE) < ChippedBlock.MAX_STAGE) {
				setBlock = state.setValue(ChippedBlock.STAGE, state.getValue(ChippedBlock.STAGE) + 1);
				level.levelEvent(player, 2001, pos, Block.getId(state));
			}
		if (setBlock != null) {
			event.setCanceled(true);
			level.setBlock(pos, setBlock, level.isClientSide() ? 11 : 3);
			Block.dropResources(state, level, pos, null);
		}
	}

	@SubscribeEvent
	public static void onBreakSpeed(final PlayerEvent.BreakSpeed event) {
		if (NWConfig.Common.ENABLE_BLOCK_BREAK_CHECK.get() && !BreakChecks.check(event.getEntity(), event.getState()))
			event.setCanceled(true);

		float speed = event.getNewSpeed();
		BlockState state = event.getState();
		Block block = state.getBlock();
		if (block instanceof ChippedBlock) {
			int stage = state.getValue(ChippedBlock.STAGE);
			speed = ((float) (speed / (Math.pow(2, stage))));
		}
		if (block instanceof ChippedBlock || block == net.minecraft.world.level.block.Blocks.STONE || block == net.minecraft.world.level.block.Blocks.DEEPSLATE) {
			speed /= NWConfig.Common.STONE_MINE_SPEED_MULTIPLIER.get();
		}

		event.setNewSpeed(speed);
	}

	@SubscribeEvent
	public static void onArrowLooseEvent(ArrowLooseEvent event) {
		if (event.getBow().getItem() instanceof BowItem)
			if (event.getEntity().getTags().contains(NWConfig.Common.RELOAD_SPEED_TAG.get()))
				event.setCharge((event.getCharge() + (int) (20 * NWConfig.Common.RELOAD_SPEED_MULTIPLIER.get())));
	}

	@SubscribeEvent
	public static void onAttachCapabilitiesLiving(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity livingEntity) {
			if (!livingEntity.getCapability(IsUndergroundProvider.CAPABILITY).isPresent()) {
				event.addCapability(new ResourceLocation(NWTweaks.MODID, "is_underground"), new IsUndergroundProvider());
			}
			if (!livingEntity.level.isClientSide() && livingEntity instanceof ServerPlayer player) {
				if (!player.getCapability(ServerPlayerDataProvider.CAPABILITY).isPresent()) {
					event.addCapability(new ResourceLocation(NWTweaks.MODID, "server_player_data"), new ServerPlayerDataProvider());
				}
			} else if (livingEntity instanceof CaveDwellerEntity caveDweller) {
				if (!livingEntity.getCapability(RedDwellerProvider.CAPABILITY).isPresent()) {
					event.addCapability(new ResourceLocation(NWTweaks.MODID, "red_dweller"), new RedDwellerProvider());
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayer player = (ServerPlayer) event.getEntity();
		player.getCapability(ServerPlayerDataProvider.CAPABILITY).ifPresent(serverPlayerData -> {
			if (!serverPlayerData.isInitialized())
				serverPlayerData.initialize(PillItem.getSlotSize());
		});
		ModMessages.sendToClient(new S2CDiscoveredPills(PillItem.getIntSlots(player)), player);
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			event.getOriginal().reviveCaps();
			event.getOriginal().getCapability(ServerPlayerDataProvider.CAPABILITY).ifPresent(serverPlayerData ->
							event.getEntity().getCapability(ServerPlayerDataProvider.CAPABILITY).ifPresent(serverPlayerData1 ->
											serverPlayerData1.setDiscoveredPills(serverPlayerData.getDiscoveredPills())));
			event.getOriginal().invalidateCaps();
		}
	}

	@SubscribeEvent
	public static void onShieldHit(ShieldBlockEvent event) {
		if (event.getEntity() instanceof Player player && event.getDamageSource().getEntity() instanceof CaveDwellerEntity dweller) {
			AtomicBoolean isRedDweller = new AtomicBoolean(false);
			dweller.getCapability(RedDwellerProvider.CAPABILITY).ifPresent(redDweller -> isRedDweller.set(redDweller.isRedDweller()));
			if (isRedDweller.get()) {
				event.setCanceled(true);
			} else {
				ItemStack stack = player.getUseItem();
				Level level = player.getLevel();
				player.getCooldowns().addCooldown(stack.getItem(), 100);
				player.stopUsingItem();
				if (!level.isClientSide())
					level.broadcastEntityEvent(player, (byte) 30);
			}
		}
	}

//	@SubscribeEvent
//	public static void onAttachCapabilitiesItemStack(AttachCapabilitiesEvent<ItemStack> event) {
//		ItemStack stack = event.getObject();
//		if (stack.is(ModRegistry.AIR_BLADDER_ITEM.get()))
//			event.addCapability(new ResourceLocation(NWTweaks.MODID, "air"), new BladderAirProvider());
//	}

	@SubscribeEvent
	public static void onServerStart(ServerStartingEvent event) {
		PillItem.randomizePills(event.getServer().overworld().getSeed());
	}

	@Mod.EventBusSubscriber(modid = NWTweaks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModBus {


		@SubscribeEvent
		public static void onEntityAttributeModificationEvent(final EntityAttributeModificationEvent event) {
			event.add(EntityType.PLAYER, Attributes.HEADSHOT_DAMAGE.get());
			event.add(EntityType.PLAYER, Attributes.BC_ATTACK_RANGE.get());
			event.add(EntityType.PLAYER, Attributes.RANGED_ARMOR_PIERCING.get());
		}

		@SubscribeEvent
		public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
			event.register(IsUnderground.class);
			event.register(BladderAir.class);
			event.register(RedDweller.class);
		}

		@SubscribeEvent
		public static void onModInit(FMLCommonSetupEvent event) {
			//SanityProcessor.PASSIVE_SANITY_SOURCES.add(new Temperature());
		}

	}

}
