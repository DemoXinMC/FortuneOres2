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
            if(!ore.enabled)
                continue;
            
            for(String oreName : ore.oreNames)
            {
                addOre(oreName, ore);
            }
        }
    }

    public void addOre(String oreName, Ore ore)
    {
        int oreID = OreDictionary.getOreID(oreName);

        int dropCount = ore.dropCount;
        if(oreName.contains("Nether"))
            dropCount *= 2;

        dropMap.put(oreID, new DropStorage(ore.meta, dropCount));
    }

    @SubscribeEvent
    public void SwapOres(HarvestDropsEvent event)
    {
        if(event.isSilkTouching)
            return;
        if(event.drops.isEmpty())
            return;

        // We have a drop, let's check if it's an Item or and ItemBlock.  We're only interested in ItemBlocks
        if(!(event.drops.get(0).getItem() instanceof ItemBlock))
            return;

        ItemStack checkItem = event.drops.get(0);

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

        event.drops.clear();

        int count = randomCount(result.count, event.fortuneLevel, event.world);
        event.dropChance = 1.0f;

        for(int i = 0; i < count; i++)
            event.drops.add(new ItemStack(FortuneOres.itemChunk, 1, result.meta));

    }

    private int randomCount(int baseCount, int fortuneLevel, World world)
    {		
        if(fortuneLevel > 0)
        {
            int j = world.rand.nextInt(fortuneLevel + 2) - 1;

            if (j < 0)
            {
                j = 0;
            }

            return baseCount * (j + 1);
        }
        else
        {
            return baseCount;
        }
    }

    protected class DropStorage
    {
        public int meta;
        public int count;

        public DropStorage(int oreMeta, int baseCount)
        {
            meta = oreMeta;
            count = baseCount;
        }
    }

}
