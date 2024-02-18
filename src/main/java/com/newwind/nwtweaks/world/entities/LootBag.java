package com.newwind.nwtweaks.world.entities;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.access.IRestrictedContainer;
import com.newwind.nwtweaks.registries.EntityTypes;
import com.newwind.nwtweaks.util.CommonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class LootBag extends Entity implements ContainerEntity, IRestrictedContainer {

	private static final String TAG_BAG_TYPE = "Type";
	private static final String TAG_BAG_SPAWN_TIME = "SpawnTime";
	private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(LootBag.class, EntityDataSerializers.INT);
	private static final int CONTAINER_SIZE = 27;


	private ResourceLocation lootTable;
	private long lootTableSeed;
	private NonNullList<ItemStack> itemStacks = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
	private long spawnTime;

	public LootBag(EntityType<?> entityType, Level level) {
		super(entityType, level);
		this.spawnTime = 0;
	}

	public static void createFromDropEvent(LivingDropsEvent event) {
		Collection<ItemEntity> drops = event.getDrops();
		if (drops.isEmpty())
			return;

		Entity entity = event.getEntity();
		Level level = entity.getLevel();
		if (entity instanceof ServerPlayer player) {
			LootBag lootBag = EntityTypes.LOOT_BAG.get().create(level);
			if (lootBag != null) {
				lootBag.setBagType(LootBag.TYPE.PLAYER);
				lootBag.setNameFromPlayer(player);
				lootBag.setSpawnTime(level.getGameTime());
				BlockPos spawnPos = new BlockPos(player.position());

				if (CommonUtils.isUnderground(player)) {
					spawnPos = player.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawnPos);
				}
				while (!level.getBlockState(spawnPos).isAir())
					spawnPos = spawnPos.above();
				while (level.getBlockState(spawnPos.below()).isAir())
					spawnPos = spawnPos.below();

				lootBag.setPos(new Vec3(player.position().x(), spawnPos.getY(), player.position().z()));


				NonNullList<ItemStack> itemStacks = lootBag.getItemStacks();
				for (ItemEntity itemEntity : drops) {
					ItemStack itemStack = itemEntity.getItem();
					if (!itemStack.isEmpty()) {
						boolean hasBeenPlaced = false;
						for (int slotNum = 0; slotNum < LootBag.CONTAINER_SIZE && !hasBeenPlaced; slotNum++) {
							ItemStack slotStack = itemStacks.get(slotNum);
							if (slotStack.isEmpty()) {
								itemStacks.set(slotNum, itemStack);
								itemEntity.discard();
								hasBeenPlaced = true;
							} else {
								int maxStackSize = slotStack.getMaxStackSize();
								if (slotStack.sameItem(itemStack) && slotStack.getCount() < maxStackSize) {
									int splitCount = Math.min(maxStackSize - slotStack.getCount(), itemStack.getCount());
									int toAdd = itemStack.split(splitCount).getCount();
									slotStack.setCount(slotStack.getCount() + toAdd);

									if (itemStack.isEmpty()) {
										itemEntity.discard();
										hasBeenPlaced = true;
									}
								}
							}
						}
					}
				}
				level.addFreshEntity(lootBag);
			}
		}
	}

	public void setNameFromPlayer(Player player) {
		this.setCustomName(Component.translatable("entity.nwtweaks.loot_bag.owned_name", player.getName()));
	}

	public TYPE getBagType() {
		return TYPE.byOrdinal(entityData.get(DATA_ID_TYPE));
	}

	public void setBagType(TYPE bagType) {
		this.entityData.set(DATA_ID_TYPE, bagType.ordinal());
	}

	public long getSpawnTime() {
		return spawnTime;
	}

	public void setSpawnTime(long spawnTime) {
		this.spawnTime = spawnTime;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public boolean isAttackable() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
		this.interactWithChestVehicle(this::gameEvent, player);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.spawnTime > 0 && this.spawnTime + NWConfig.Common.PLAYER_LOOT_BAG_DESPAWN_TIME.get() <= level.getGameTime())
			this.discard();
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_ID_TYPE, TYPE.PLAYER.ordinal());
	}

	@Override
	protected void readAdditionalSaveData(@NotNull CompoundTag nbt) {
		this.setBagType(TYPE.byName(nbt.getString(TAG_BAG_TYPE)));
		this.setSpawnTime(nbt.getLong(TAG_BAG_SPAWN_TIME));
		this.readChestVehicleSaveData(nbt);
	}

	@Override
	protected void addAdditionalSaveData(@NotNull CompoundTag nbt) {
		nbt.putString(TAG_BAG_TYPE, this.getBagType().getName());
		nbt.putLong(TAG_BAG_SPAWN_TIME, this.getSpawnTime());
		this.addChestVehicleSaveData(nbt);
	}

	@Override
	public @NotNull Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}

	@Nullable
	@Override
	public ResourceLocation getLootTable() {
		return this.lootTable;
	}

	@Override
	public void setLootTable(@Nullable ResourceLocation lootTable) {
		this.lootTable = lootTable;
	}

	@Override
	public long getLootTableSeed() {
		return this.lootTableSeed;
	}

	@Override
	public void setLootTableSeed(long lootTableSeed) {
		this.lootTableSeed = lootTableSeed;
	}

	@Override
	public @NotNull NonNullList<ItemStack> getItemStacks() {
		return this.itemStacks;
	}

	@Override
	public void clearItemStacks() {
		this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
	}

	@Override
	public int getContainerSize() {
		return CONTAINER_SIZE;
	}

	@Override
	public @NotNull ItemStack getItem(int index) {
		return this.getChestVehicleItem(index);
	}

	@Override
	public @NotNull ItemStack removeItem(int index, int amount) {
		return this.removeChestVehicleItem(index, amount);
	}

	@Override
	public @NotNull ItemStack removeItemNoUpdate(int index) {
		return this.removeChestVehicleItemNoUpdate(index);
	}

	@Override
	public void setItem(int index, @NotNull ItemStack itemStack) {
		this.setChestVehicleItem(index, itemStack);
	}

	@Override
	public void setChanged() {
		if (this.isEmpty())
			this.discard();
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return this.isChestVehicleStillValid(player);
	}

	@Override
	public void clearContent() {
		this.clearChestVehicleContent();
	}

	@Override
	public boolean canPlaceItem(int index, @NotNull ItemStack itemStack) {
		return false;
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
		if (this.lootTable != null && player.isSpectator()) {
			return null;
		} else {
			this.unpackChestVehicleLootTable(inventory.player);
			return ChestMenu.threeRows(containerId, inventory, this);
		}
	}

	@Override
	public boolean nWTweaks$canPlaceItemsInside() {
		return false;
	}

	public enum TYPE {
		PLAYER("player"),
		BOSS("boss"),
		EVENT("event");

		private static final TYPE[] values = values();

		private final String name;

		TYPE(String name) {
			this.name = name;
		}

		private static TYPE byName(String typeName) {
			return switch (typeName) {
				default -> PLAYER;
				case "boss" -> BOSS;
				case "event" -> EVENT;
			};
		}

		public static TYPE byOrdinal(Integer ordinal) {
			if (ordinal < 0 || ordinal >= values.length)
				ordinal = 0;

			return values[ordinal];
		}

		private String getName() {
			return this.name;
		}
	}
}
