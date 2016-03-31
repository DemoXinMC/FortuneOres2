package com.demoxin.minecraft.fortuneores;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemChunk extends Item
{
    private ArrayList<String> nameStorage;

    public ItemChunk()
    {
        super();
        setMaxStackSize(64);
        setCreativeTab(FortuneOres.creativeTab);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        
        createNames();
    }
    
    public void createNames()
    {
        nameStorage = new ArrayList<String>();
        for (int i = 0; i < FortuneOres.ores.size(); ++i)
        {
            nameStorage.add(i, "item.orechunks." + FortuneOres.ores.get(i).name.toLowerCase());

            if(!FortuneOres.ores.get(i).enabled)
            {
                nameStorage.remove(i);
                nameStorage.add(i, "item.orechunks.mysterious");
            }
                
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        if(nameStorage == null)
            return "item.orechunks.mysterious";
        
        int meta = itemStack.getItemDamage();

        if(meta > nameStorage.size() || nameStorage.get(meta) == null)
            return "item.orechunks.mysterious";

        return nameStorage.get(meta);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list)
    {
        for(int i = 0; i < nameStorage.size(); ++i)
        {
            ItemStack oreChunk = new ItemStack(this, 1, i);
            list.add(oreChunk);
        }
    }
}
