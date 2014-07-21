package com.demoxin.minecraft.fortuneores;

import java.util.HashMap;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OreSwapper {
    protected HashMap<Integer, DropStorage> dropMap;
    
    public OreSwapper()
    {
        dropMap = new HashMap<Integer, DropStorage>();
        
        for(Ore ore : FortuneOres.oreStorage)
        {
            for(String oreName : ore.oreNames)
            {
                addOre(oreName, ore);
            }
        }
    }
    
    public void addOre(String fOreDict, Ore fOre)
    {
        int oreID = OreDictionary.getOreID(fOreDict);
        
        int dropCount = fOre.dropCount;
        if(fOreDict.contains("Nether"))
            dropCount *= 2;
            
        dropMap.put(oreID, new DropStorage(fOre.meta, dropCount));
    }
    
	@SubscribeEvent
	public void SwapOres(HarvestDropsEvent fEvent)
	{
		if(fEvent.isSilkTouching)
			return;
		if(fEvent.drops.isEmpty())
			return;
		
		// We have a drop, let's check if it's an Item or and ItemBlock.  We're only interested in ItemBlocks
		if(!(fEvent.drops.get(0).getItem() instanceof ItemBlock))
			return;
		
		ItemStack checkItem = fEvent.drops.get(0);
		
		int[] oreIDs = OreDictionary.getOreIDs(checkItem);
		DropStorage result = null;
		for(int i = 0; i < oreIDs.length; i++)
		{
		    if(dropMap.containsKey(oreIDs[i]))
		    {
		        result = dropMap.get(oreIDs[i]);
		        break;
		    }
		}
		
		if(result == null)
		    return;
		
		fEvent.drops.clear();
		
		int count = RandCount(result.count, fEvent.fortuneLevel, fEvent.world);
		fEvent.dropChance = 1.0f;
		
		for(int i = 0; i < count; i++)
		    fEvent.drops.add(new ItemStack(FortuneOres.itemChunk, 1, result.meta));
		
	}
	
	private int RandCount(int fBase, int fFortune, World fWorld)
	{		
		if(fFortune > 0)
		{
			int j = fWorld.rand.nextInt(fFortune + 2) - 1;

            if (j < 0)
            {
                j = 0;
            }

            return fBase * (j + 1);
		}
		else
		{
			return fBase;
		}
	}
	
	protected class DropStorage
    {
        public int meta;
        public int count;
        
        public DropStorage(int fMeta, int fCount)
        {
            meta = fMeta;
            count = fCount;
        }
    }
	
}
