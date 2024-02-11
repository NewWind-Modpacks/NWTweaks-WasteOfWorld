package com.newwind.nwtweaks.client.gui;

import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterInteger;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import static se.mickelus.tetra.gui.stats.StatsHelper.barLength;

public class TetraStats {

	public static final ItemEffect HOTBAR_SLOTS = ItemEffect.get("nwtweaks.hotslot");
	private static final int MAX_HOTSLOTS = 4;

	private static final IStatGetter hotslotGetter = new StatGetterEffectLevel(HOTBAR_SLOTS, 1d);
	private static final GuiStatBar hotslot = new GuiStatBar(
					0, 0, barLength,
					"tetra.stats.toolbelt.hotslot",
					0, MAX_HOTSLOTS, true,
					hotslotGetter, LabelGetterBasic.integerLabel,
					new TooltipGetterInteger("tetra.stats.toolbelt.hotslot.tooltip", hotslotGetter));

	public static void addBar() {
		WorkbenchStatsGui.addBar(hotslot);
		HoloStatsGui.addBar(hotslot);
	}

}
