package com.newwind.nwtweaks.mixin.client;

import com.newwind.nwtweaks.access.IPostProcessRenderizable;
import croissantnova.sanitydim.client.PostProcessor;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(PostProcessor.class)
public class SaPatchPostGamemodes implements IPostProcessRenderizable {

	@Final
	@Shadow(
					remap = false
	)
	public final List<PostProcessor.PostPassEntry> passEntries = new ArrayList<>();
	@Shadow(
					remap = false
	)
	private float m_time;

	@Unique
	public void nWTweaks$alwaysRender(float partialTicks) {
		this.m_time += partialTicks;
		Minecraft mc = Minecraft.getInstance();
		if (mc.player != null) {
			@SuppressWarnings("all")
			Iterator var3 = this.passEntries.iterator();

			while (true) {
				PostProcessor.PostPassEntry entry;
				do {
					do {
						do {
							do {
								if (!var3.hasNext()) {
									return;
								}

								entry = (PostProcessor.PostPassEntry) var3.next();
							} while (entry.getInPass() == null);
						} while (entry.getOutPass() == null);
					} while (entry.getInProcessor() != null && !(Boolean) entry.getInProcessor().apply(entry.getInPass()));
				} while (entry.getOutProcessor() != null && !(Boolean) entry.getOutProcessor().apply(entry.getOutPass()));

				entry.getInPass().process(partialTicks);
				entry.getOutPass().process(partialTicks);
			}
		}
	}

}
