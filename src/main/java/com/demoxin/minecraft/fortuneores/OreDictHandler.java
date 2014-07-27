package com.demoxin.minecraft.fortuneores;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OreDictHandler
{
    @SubscribeEvent
    public void Handle(OreRegisterEvent event)
    {
        if(!event.Name.contains("ore"))
            return;
        
        String oreName = event.Name.replace("ore", "");
        
        for(Ore ore : FortuneOres.oreStorage)
        {
            if(ore.name.equals(oreName))
            {
                registerOreDict(ore);
                return;
            }
        }
    }
    
    public void registerOreDict(Ore ore)
    {
        if(ore.oreDicted)
            return;
        
        ore.oreDicted = true;
        
        if(!ore.enabled)
            return;

        ItemStack chunk = new ItemStack(FortuneOres.itemChunk, 1, ore.meta);
        for(String oreName : ore.oreNames)
        {
            if(FortuneOres.allowProcessing)
            {
                if(!oreName.contains("Nether") && !oreName.contains("dense"))
                    OreDictionary.registerOre(oreName, chunk);
            }
            else
            {
                String dustName = oreName.replace("ore", "").replace("dense", "").replace("Nether", "");
                OreDictionary.registerOre("dust" + dustName, chunk);
            }
        }
    }
}