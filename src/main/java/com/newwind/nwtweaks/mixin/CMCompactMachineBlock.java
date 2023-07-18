package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.util.CommonUtils;
import dev.compactmods.machines.machine.CompactMachineBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CompactMachineBlock.class)
public abstract class CMCompactMachineBlock {

	private final Component messageTimeout = Component.translatable("message.nwtweaks.machine_timeout").withStyle(Style.EMPTY.withColor(ChatFormatting.RED));

	/**
	 * @author Kevadroz
	 */
	@Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
	private void canEnter(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
		if (level instanceof ServerLevel) {
			if (CommonUtils.areCMUnavailable(level.getServer(), (ServerPlayer) player)) {
				player.displayClientMessage(messageTimeout, true);
				cir.setReturnValue(InteractionResult.SUCCESS);
				cir.cancel();}
		}
	}

}
