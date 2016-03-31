package com.demoxin.minecraft.fortuneores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryMagic
{
    public static Map<ItemStack, Ore> preservedOreDicting;
    
    static
    {
        preservedOreDicting = new HashMap<ItemStack, Ore>();
    }
    
    @SubscribeEvent
    public void OreDictTooltip(ItemTooltipEvent event)
    {
        if(!event.isShowAdvancedItemTooltips())
            return;
        
        int[] oreIDs = OreDictionary.getOreIDs(event.getItemStack());
        
        if(oreIDs.length <= 0)
            return;
        
        for(int i = 0; i < oreIDs.length; ++i)
            event.getToolTip().add(OreDictionary.getOreName(oreIDs[i]));
    }
    
    public static void addOreDicting()
    {
        for(Ore ore : OreDictionaryMagic.preservedOreDicting.values())
        {
            for(String oreName : ore.oreNames)
                OreDictionary.registerOre(oreName, new ItemStack(FortuneOres.itemChunk, 1, ore.meta));
        }
    }

    public static void addSmelting()
    {
        for(Ore ore : FortuneOres.ores)
        {
            ItemStack ingot = null;

            for(String ingotName : ore.ingotNames)
            {
                List<ItemStack> ingots = OreDictionary.getOres(ingotName);
                if(!ingots.isEmpty())
                    ingot = ingots.get(0);
            }

            if(ingot != null)
            {
                ingot = ingot.copy();
                ingot.stackSize = 1;
                ItemStack chunk = new ItemStack(FortuneOres.itemChunk, 1, ore.meta);
                GameRegistry.addSmelting(chunk, ingot, ore.xpSmelt);
            }
        }
    }

    public static void preserve(Ore ore, ItemStack input)
    {
        preservedOreDicting.put(input, ore);
    }
}
