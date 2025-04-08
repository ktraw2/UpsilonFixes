package com.rewindmc.upsilonfixes.vanilla;

import java.util.Arrays;
import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.settings.OptionsManager;

public class ModernHotbarBinds {

	public static final KeyBinding[] binds; static {
		binds = new KeyBinding[9];
		for (int i = 0; i < binds.length; i++) {
			int j = i+1;
			String k = "key.hotbar."+j;
			binds[i] = new KeyBinding(k, Keyboard.KEY_1+i);
			LanguageRegistry.instance().addStringLocalization(k, "Hotbar Slot "+j);
		}
		OptionsManager opt = Minecraft.instance.options;
		KeyBinding[] newBinds = Arrays.copyOf(opt.keyBindings, opt.keyBindings.length+binds.length);
		System.arraycopy(binds, 0, newBinds, opt.keyBindings.length, binds.length);
		opt.keyBindings = newBinds;
	}
	
	public static void init() {
		TickRegistry.registerTickHandler(new ITickHandler() {
			private final EnumSet<TickType> ticks = EnumSet.of(TickType.CLIENT);
			@Override
			public EnumSet<TickType> ticks() {
				return ticks;
			}
			
			@Override
			public void tickStart(EnumSet<TickType> var1, Object... var2) {}
			
			@Override
			public void tickEnd(EnumSet<TickType> var1, Object... var2) {
				Minecraft mc = Minecraft.instance;
				if (mc.player != null && (mc.currentScreen == null || mc.currentScreen.allowUserInput)) {
					for (int i = 0; i < binds.length; i++) {
						while (binds[i].isPressed()) {
							mc.player.inventory.currentItem = i;
						}
					}
				}
			}
			
			@Override
			public String getLabel() {
				return "UpsilonFixes Modern Hotbar Binds";
			}
		}, Side.CLIENT);
	}
}
