package com.rewindmc.upsilonfixes.vanilla;

import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.src.EnumOptions;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.StringTranslate;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.GuiOptions")
public class GuiOptionsTransformer extends UpsilonMiniTransformer {

	@Patch.Method("initGui()V")
	public void patchInitGui(PatchContext ctx) {
		ctx.jumpToLastReturn();
		
		ctx.add(
			ALOAD(0),
			INVOKESTATIC(hooks(), "modifyButtons", "(Lnet/minecraft/src/GuiOptions;)V")
		);
	}
	
	public static class Hooks {

		public static void modifyButtons(GuiOptions gui) {
			for (Object o : gui.controlList) {
				if (UpsilonFixesConfig.smearing && o instanceof GuiSmallButton) {
					GuiSmallButton gsb = (GuiSmallButton)o;
					if (gsb.returnEnumOptions() == EnumOptions.TOUCHSCREEN) {
						gsb.enabled = false;
					}
				} else if (UpsilonFixesConfig.removeSnooper && o instanceof GuiButton && ((GuiButton)o).displayString.equals(StringTranslate.getInstance().translateKey("options.snooper.view"))) {
					GuiButton gb = ((GuiButton)o);
					gb.drawButton = false;
					gb.enabled = false;
				}
			}
		}
	
	}
	
}
