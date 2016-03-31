package com.demoxin.minecraft.fortuneores.coremod;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(value = "FortuneOres Black Magic")
@IFMLLoadingPlugin.MCVersion(value = "1.9")
@IFMLLoadingPlugin.SortingIndex(100)
public class FOFMLLoadingPlugin implements IFMLLoadingPlugin
{
    public static boolean runtimeDeobfEnabled = false;

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[] {"com.demoxin.minecraft.fortuneores.coremod.OreDictCore"};
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
        runtimeDeobfEnabled = (Boolean)data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
