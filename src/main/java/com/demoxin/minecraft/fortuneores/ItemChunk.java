package com.demoxin.minecraft.fortuneores;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemChunk extends Item
{
    private ArrayList<IIcon> iconStorage;
    private ArrayList<String> nameStorage;
    private IIcon mysterious;

    public ItemChunk()
    {
        super();
        setMaxStackSize(64);
        setCreativeTab(FortuneOres.creativeTab);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        
        createNames();
    }

    @Override
    public IIcon getIconFromDamage(int meta)
    {
        if(meta > iconStorage.size() || iconStorage.get(meta) == null)
        {
            return mysterious;
        }
        return iconStorage.get(meta);

    }
    
    public void createNames()
    {
        nameStorage = new ArrayList<String>();
        for (int i = 0; i < FortuneOres.nextMeta; i++)
        {
            nameStorage.add(i, "item.orechunks." + FortuneOres.oreStorage.get(i).name.toLowerCase());

            if(!FortuneOres.oreStorage.get(i).enabled)
            {
                nameStorage.remove(i);
                nameStorage.add(i, "item.orechunks.mysterious");
            }
                
        }
    }

    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        mysterious = iconRegister.registerIcon(FortuneOres.MODID + ":mysteriouschunk");
        iconStorage = new ArrayList<IIcon>();
        for (int i = 0; i < FortuneOres.nextMeta; i++)
            iconStorage.add(i, iconRegister.registerIcon(FortuneOres.MODID + ":" + FortuneOres.oreStorage.get(i).name.toLowerCase()));
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
