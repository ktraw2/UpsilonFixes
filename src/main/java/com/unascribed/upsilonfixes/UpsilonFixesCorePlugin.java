package com.unascribed.upsilonfixes;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.TransformerExclusions("com.unascribed.upsilonfixes")
public class UpsilonFixesCorePlugin implements IFMLLoadingPlugin {

	@Override
	public String[] getLibraryRequestClass() {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		String pkg = "com.unascribed.upsilonfixes.";
		return new String[] {
				pkg+"ic2.PlatformClientTransformer",
				pkg+"vanilla.BlockFlowingTransformer"
		};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> map) {
		
	}
	
}
