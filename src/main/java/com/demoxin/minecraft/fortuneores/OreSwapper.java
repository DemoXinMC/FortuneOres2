package com.demoxin.minecraft.fortuneores;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
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

        dropMap.put(oreID, new DropStorage(ore.meta, dropCount, ore.xpDropMin, ore.xpDropMax));
    }
    
    @SubscribeEvent
    public void OreDictTooltip(ItemTooltipEvent event)
    {
        if(!event.showAdvancedItemTooltips)
            return;
        
        int[] oreIDs = OreDictionary.getOreIDs(event.itemStack);
        
        if(oreIDs.length <= 0)
            return;
        
        for(int i = 0; i < oreIDs.length; ++i)
            event.toolTip.add(OreDictionary.getOreName(oreIDs[i]));
    }

    @SubscribeEvent
    public void SwapOres(HarvestDropsEvent event)
    {
        if(event.isSilkTouching)
            return;
        if(event.drops.isEmpty())
            return;

        ArrayList<ItemStack> newDrops = new ArrayList<ItemStack>();
        boolean modifiedDrops = false;
        
        // We have a drop, let's check if it's an item or and ItemBlock.  We're only interested in ItemBlocks
        for(int dropEntry = 0; dropEntry < event.drops.size(); ++dropEntry)
        {
            if(!(event.drops.get(dropEntry).getItem() instanceof ItemBlock))
            {
                if(event.world.rand.nextFloat() < event.dropChance)
                    newDrops.add(event.drops.get(dropEntry));
                break;
            }
            
            ItemStack checkItem = event.drops.get(dropEntry);
            
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
            {
                if(event.world.rand.nextFloat() < event.dropChance)
                    newDrops.add(event.drops.get(dropEntry));
                break;
            }
            
            modifiedDrops = true;
            int count = randomCount(result.count, event.fortuneLevel, event.world);
                
            for(int i = 0; i < count; i++)
                newDrops.add(new ItemStack(FortuneOres.itemChunk, 1, result.meta));
        }

        if(!modifiedDrops)
            return;
        
        event.drops.clear();
        event.dropChance = 1.0f;
        
        for(ItemStack drop : newDrops)
        {
            event.drops.add(drop);
        }
        
        int blockOreDict = OreDictionary.getOreID(new ItemStack(Item.getItemFromBlock(event.block), 1, event.blockMetadata));
        if(!dropMap.containsKey(blockOreDict))
            return;
        
        DropStorage xpDrops = dropMap.get(blockOreDict);
        
        if(xpDrops.xpMax <= 0)
            return;
        
        int countOrbs = event.world.rand.nextInt(xpDrops.xpMax-xpDrops.xpMin);
        countOrbs += xpDrops.xpMin;
        
        for(int i = 0; i < countOrbs; ++i)
        {
            event.world.spawnEntityInWorld(new EntityXPOrb(event.world, (double)event.x + 0.5D, (double)event.y + 0.5D, (double)event.z + 0.5D, 1));
        }
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
        public int xpMin;
        public int xpMax;

        public DropStorage(int oreMeta, int baseCount, int dropXPMin, int dropXPMax)
        {
            meta = oreMeta;
            count = baseCount;
            xpMin = dropXPMin;
            xpMax = dropXPMax;
        }
    }

}
