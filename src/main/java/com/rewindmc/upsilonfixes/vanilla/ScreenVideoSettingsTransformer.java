package com.rewindmc.upsilonfixes.vanilla;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenVideoSettings;
import net.minecraft.client.gui.WidgetOptionButton;
import net.minecraft.client.gui.WidgetSlider;
import net.minecraft.client.settings.Option;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.gui.ScreenVideoSettings")
@ConfigOptions("modernFpsSlider")
public class ScreenVideoSettingsTransformer extends UpsilonMiniTransformer {

	@Patch.Method("initGui()V")
	public void patchInitGui(PatchContext ctx) {
		ctx.jumpToLastReturn();
		
		ctx.add(
			ALOAD(0),
			INVOKESTATIC(hooks(), "modifyButtons", "(Lnet/minecraft/client/gui/ScreenVideoSettings;)V")
		);
	}
	
	public static class Hooks {

		public static void modifyButtons(ScreenVideoSettings gui) {
			WidgetOptionButton perf = null;
			for (Object o : gui.controlList) {
				if (o instanceof WidgetOptionButton) {
					WidgetOptionButton gsb = (WidgetOptionButton)o;
					if (gsb.getOption() == Option.FRAMERATE_LIMIT) {
						perf = gsb;
					}
				}
			}
			if (perf != null) {
				gui.controlList.remove(perf);
				int fps = Minecraft.instance().options.limitFramerate;
				float val = (fps-10)/240f;
				if (val <= 0) {
					fps = 10;
					val = 0;
				}
				String s = Integer.toString(fps);
				if (val >= 1) {
					s = "Unlimited";
					val = 1;
				}
				WidgetSlider slider = new WidgetSlider(9001, perf.xPosition, perf.yPosition, Option.FRAMERATE_LIMIT, "Max Framerate: "+s, val) {
					@Override
					public void mouseDragged(Minecraft var1, int var2, int var3) {
						super.mouseDragged(var1, var2, var3);
						int fps = (int)((sliderValue*240)+10);
						Minecraft.instance().options.limitFramerate = fps;
						String s = Integer.toString(fps);
						if (fps >= 250) {
							s = "Unlimited";
						}
						displayString = "Max Framerate: "+s;
					}
				};
				gui.controlList.add(slider);
			}
		}
	
	}
	
}
