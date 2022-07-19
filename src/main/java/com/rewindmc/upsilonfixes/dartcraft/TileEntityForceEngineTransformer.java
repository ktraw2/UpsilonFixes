package com.rewindmc.upsilonfixes.dartcraft;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("bluedart.tile.TileEntityForceEngine")
public class TileEntityForceEngineTransformer extends UpsilonMiniTransformer {

	@Patch.Method("transferEnergy()V")
	public void patchTransferEnergy(PatchContext ctx) {
		// The actual maximum energy production of the Force Engine is 16MJ/t (400MJ per cycle)
		// However, it's limited to 10MJ/t (250MJ per cycle) due to an oversight, causing it to
		// waste power if it's producing more than 10MJ/t.
		////
		// This is fixed in the 1.6 version of DartCraft by making the MJ-per-cycle dynamic, based
		// on the current generation rate. Let's just change it to the maximum in this version.
		ctx.search(
			LDC(250f)
		).jumpAfter();
		ctx.add(
			POP(),
			LDC(400f)
		);
	}
	
}
