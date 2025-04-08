package com.rewindmc.upsilonfixes.dartcraft;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("bluedart.core.network.PacketHandler")
@ConfigOptions("fixDartCraftForceInfuserDimension")
public class PacketHandlerTransformer extends UpsilonMiniTransformer {

	@Patch.Method("openTileGui(Lbluedart/core/network/PacketDimCoords;Lnet/minecraft/entity/player/EntityPlayer;)V")
	public void patchOpenTileGui(PatchContext ctx) {
		// misleading method name --- actually handles clicking the "Go" button in the Force Infuser
		ctx.search(
			INVOKEVIRTUAL("net/minecraft/server/MinecraftServer", "worldServerForDimension", "(I)Lnet/minecraft/world/WorldServer;")
		).jumpAfter();
		
		ctx.add(
			POP(),
			ALOAD(2),
			GETFIELD("net/minecraft/entity/player/EntityPlayer", "worldObj", "Lnet/minecraft/world/World;"),
			CHECKCAST("net/minecraft/world/WorldServer")
		);
	}
	
}
