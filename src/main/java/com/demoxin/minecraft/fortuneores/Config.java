package com.demoxin.minecraft.fortuneores;

import net.minecraftforge.common.config.Configuration;

public class Config
{
    public Config(Configuration config)
    {
        config.load();
        
        FortuneOres.allowProcessing = config.get("AAAGeneral", "AllowProcessing", true).getBoolean(true);

        for(Ore ore : FortuneOres.oreStorage)
        {
            ore.enabled = config.get(ore.name, "Enable", true).getBoolean(true);
            ore.dropCount = config.get(ore.name, "BaseDrop", 1).getInt();
        }

        if(config.hasChanged())
        {
            config.save();
        }
    }
}