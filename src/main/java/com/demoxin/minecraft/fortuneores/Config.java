package com.demoxin.minecraft.fortuneores;

import net.minecraftforge.common.config.Configuration;

public class Config
{
    public Config(Configuration config)
    {
        config.load();

        for(Ore ore : FortuneOres.ores)
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