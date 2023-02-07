package com.rewindmc.upsilonfixes.vanilla;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.settings.Option;
import net.minecraft.util.Translate;
import net.minecraft.client.gui.ScreenOptions;
import net.minecraft.client.gui.WidgetButton;
import net.minecraft.client.gui.WidgetOptionButton;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.gui.ScreenOptions")
@ConfigOptions({"smearing", "removeSnooper"})
public class ScreenOptionsTransformer extends UpsilonMiniTransformer {

	@Patch.Method("initGui()V")
	public void patchInitGui(PatchContext ctx) {
		ctx.jumpToLastReturn();
		
		ctx.add(
			ALOAD(0),
			INVOKESTATIC(hooks(), "modifyButtons", "(Lnet/minecraft/client/gui/ScreenOptions;)V")
		);
	}
	
	public static class Hooks {

		public static void modifyButtons(ScreenOptions gui) {
			for (Object o : gui.controlList) {
				if (UpsilonFixesConfig.smearing && o instanceof WidgetOptionButton) {
					WidgetOptionButton gsb = (WidgetOptionButton)o;
					if (gsb.getOption() == Option.TOUCHSCREEN) {
						gsb.enabled = false;
					}
				} else if (UpsilonFixesConfig.removeSnooper && o instanceof WidgetButton
						&& ((WidgetButton)o).displayString.equals(Translate.format("options.snooper.view"))) {
					WidgetButton gb = ((WidgetButton)o);
					gb.drawButton = false;
					gb.enabled = false;
				}
			}
		}
	
	}
	
}
