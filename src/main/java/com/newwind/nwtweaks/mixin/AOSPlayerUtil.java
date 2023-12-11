package com.newwind.nwtweaks.mixin;

import com.alrex.parcool.common.action.impl.Crawl;
import com.alrex.parcool.common.capability.Parkourability;
import com.ccr4ft3r.actionsofstamina.util.PlayerUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PlayerUtil.class)
public class AOSPlayerUtil {

	/**
	 * @author Kevadroz
	 * @reason Crashes with last version of ParCool due to class path changes, method is identical with fixed imports.
	 */
	@Overwrite(remap = false)
	private static boolean hasParcoolCrawlPos(Player player) {
		if (!ModList.get().isLoaded("parcool")) {
			return false;
		} else {
			Parkourability parkourability = Parkourability.get(player);
			if (parkourability == null) {
				return false;
			} else {
				Crawl crawl = parkourability.get(Crawl.class);
				return crawl != null && crawl.isDoing();
			}
		}
	}

}
