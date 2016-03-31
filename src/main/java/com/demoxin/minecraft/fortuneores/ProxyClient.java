package com.demoxin.minecraft.fortuneores;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ProxyClient extends ProxyCommon
{
    @Override
    public void preInit()
    {
        for(Ore ore : FortuneOres.ores)
        {
            ModelLoader.setCustomModelResourceLocation(FortuneOres.itemChunk, ore.meta, new ModelResourceLocation(FortuneOres.MODID + ":" + ore.name.toLowerCase(), "inventory"));
        }
    }
}
