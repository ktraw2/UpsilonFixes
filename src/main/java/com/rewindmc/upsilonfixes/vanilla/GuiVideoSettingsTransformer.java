package com.rewindmc.upsilonfixes.vanilla;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EnumOptions;
import net.minecraft.src.GuiSlider;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.GuiVideoSettings;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.GuiVideoSettings")
public class GuiVideoSettingsTransformer extends UpsilonMiniTransformer {

	@Patch.Method("initGui()V")
	public void patchInitGui(PatchContext ctx) {
		ctx.jumpToLastReturn();
		
		ctx.add(
			ALOAD(0),
			INVOKESTATIC(hooks(), "modifyButtons", "(Lnet/minecraft/src/GuiVideoSettings;)V")
		);
	}
	
	public static class Hooks {

		public static void modifyButtons(GuiVideoSettings gui) {
			GuiSmallButton perf = null;
			for (Object o : gui.controlList) {
				if (o instanceof GuiSmallButton) {
					GuiSmallButton gsb = (GuiSmallButton)o;
					if (gsb.returnEnumOptions() == EnumOptions.FRAMERATE_LIMIT) {
						perf = gsb;
					}
				}
			}
			if (perf != null) {
				gui.controlList.remove(perf);
				int fps = Minecraft.getMinecraft().gameSettings.limitFramerate;
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
				GuiSlider slider = new GuiSlider(9001, perf.xPosition, perf.yPosition, EnumOptions.FRAMERATE_LIMIT, "Max Framerate: "+s, val) {
					@Override
					public void mouseDragged(Minecraft var1, int var2, int var3) {
						super.mouseDragged(var1, var2, var3);
						int fps = (int)((sliderValue*240)+10);
						Minecraft.getMinecraft().gameSettings.limitFramerate = fps;
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
