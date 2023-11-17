package com.newwind.nwtweaks.registries;

import com.mrcrayfish.guns.Config;
import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.NWTweaks;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Attributes {

	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, NWTweaks.MODID);

	public static final RegistryObject<Attribute> HEADSHOT_DAMAGE = ATTRIBUTES.register(
					"headshot_damage",
					() -> new RangedAttribute(
									"attribute.name.nwtweaks.headshot_damage",
									0.0D,
									0.0D,
									Double.MAX_VALUE
					)
	);

	public static final RegistryObject<Attribute> BC_ATTACK_RANGE = ATTRIBUTES.register(
					"bc-attack_range",
					() -> new RangedAttribute(
									"attribute.name.nwtweaks.bc-attack_range",
									1.0D,
									1.0D,
									Double.MAX_VALUE
					)
									.setSyncable(true)
	);

	public static final RegistryObject<Attribute> RANGED_ARMOR_PIERCING = ATTRIBUTES.register(
					"ranged_armor_piercing",
					() -> new RangedAttribute(
									"attribute.name.nwtweaks.ranged_armor_piercing",
									0.0D,
									0.0D,
									1.0D
					)
	);


}
