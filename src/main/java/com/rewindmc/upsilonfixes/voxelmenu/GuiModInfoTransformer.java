package com.rewindmc.upsilonfixes.voxelmenu;

import com.thevoxelbox.voxelmenu.modinfo.GuiModInfo;
import com.thevoxelbox.voxelmenu.modinfo.ModInterface;

import nilloader.api.NilMetadata;
import nilloader.api.NilModList;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("com.thevoxelbox.voxelmenu.modinfo.GuiModInfo")
public class GuiModInfoTransformer extends MiniTransformer {

	@Patch.Method("<init>(Lcom/thevoxelbox/voxelmenu/GuiMainMenuVoxelBox;)V")
	public void patchConstructor(PatchContext ctx) {
		ctx.jumpToLastReturn();
		
		ctx.add(
			ALOAD(0),
			INVOKESTATIC("com/rewindmc/upsilonfixes/voxelmenu/GuiModInfoTransformer$Hooks", "contributeMods", "(Lcom/thevoxelbox/voxelmenu/modinfo/GuiModInfo;)V")
		);
	}
	
	public static class Hooks {
		public static void contributeMods(GuiModInfo gui) {
			for (NilMetadata meta : NilModList.getAll()) {
				gui.mods.add(new ModInterface() {
					
					@Override
					public String getVersion() {
						return meta.version+" Ø";
					}
					
					@Override
					public String getModName() {
						return meta.name;
					}
					
					@Override
					public String getDescription() {
						return meta.description;
					}
					
					@Override
					public String getAuthor() {
						return meta.authors;
					}
				});
			}
		}
	}

}
