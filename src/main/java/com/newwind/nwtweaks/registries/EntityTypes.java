package com.newwind.nwtweaks.registries;

import com.newwind.nwtweaks.NWTweaks;
import com.newwind.nwtweaks.world.entities.LootBag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypes {

	public static final ResourceLocation LOOT_BAG_LOCATION = new ResourceLocation(NWTweaks.MODID, "loot_bag");


	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NWTweaks.MODID);

	public static final RegistryObject<EntityType<LootBag>> LOOT_BAG = ENTITY_TYPES.register(LOOT_BAG_LOCATION.getPath(),
					() -> EntityType.Builder.of(LootBag::new, MobCategory.MISC)
									.sized(0.625f, 0.4375f)
									.fireImmune()
									.build(LOOT_BAG_LOCATION.toString()));

}
