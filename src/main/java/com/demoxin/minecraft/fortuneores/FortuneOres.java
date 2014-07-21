package com.demoxin.minecraft.fortuneores;

import java.util.ArrayList;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = FortuneOres.MODID, name = FortuneOres.NAME, version = FortuneOres.VERSION)
public class FortuneOres {
    // Mod Info
    public static final String MODID = "FortuneOres";
    public static final String NAME = "FortuneOres";
    public static final String VERSION = "2.0";
    // Mod Info End

    // Singleton
    @Instance("FortuneOres")
    public static FortuneOres instance;
    public static Config config;
    public static ArrayList<Ore> oreStorage;

    public static CreativeTabs creativeTab;
    public static Item itemChunk;

    public static int nextMeta;

    public FortuneOres()
    {
        nextMeta = 0;
        oreStorage = new ArrayList<Ore>();

        setupOres();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        config = new Config(new Configuration(event.getSuggestedConfigurationFile()));
        creativeTab = new CTabChunks(CreativeTabs.getNextID(), "OreChunks");
        itemChunk = new ItemChunk();
        GameRegistry.registerItem(itemChunk, "oreChunk");
    }

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new OreSwapper());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        addOreDicting();
        addSmelting();
    }

    public void addOre(String oreName)
    {
        Ore newOre = new Ore(oreName, nextMeta);
        newOre.addOreName(oreName);
        newOre.addIngotName(oreName);
        oreStorage.add(newOre);
        nextMeta++;
    }

    private void setupOres()
    {
        addOre("Iron");
        addOre("Gold");

        addOre("Copper");
        addOre("Tin");
        addOre("Lead");
        addOre("Silver");

        addOre("Nickel");
        addOre("Platinum");
        addOre("Aluminum");
        oreStorage.get(nextMeta-1).addOreName("Aluminium");
        oreStorage.get(nextMeta-1).addOreName("NaturalAluminum");
        oreStorage.get(nextMeta-1).addIngotName("Aluminium");
        oreStorage.get(nextMeta-1).addIngotName("NaturalAluminum");

        addOre("Cobalt");
        addOre("Ardite");

        addOre("Manganese");
        addOre("Zinc");

        addOre("Eximite");
        addOre("Meutoite");

        addOre("DarkIron");
    }

    public void addOreDicting()
    {
        for(Ore ore : oreStorage)
        {
            boolean doOreDict = true;
            if(!ore.enabled)
                doOreDict = false;

            boolean matched = false;
            for(String oreDict : ore.oreNames)
            {
                if(!(OreDictionary.getOres(oreDict).isEmpty()))
                    matched = true;
            }

            if(!matched)
                doOreDict = false;

            if(doOreDict)
            {
                ItemStack chunk = new ItemStack(itemChunk, 1, ore.meta);
                for(String oreName : ore.oreNames)
                {
                    if(!oreName.contains("Nether"))
                        OreDictionary.registerOre(oreName, chunk);
                }
            }
        }
    }

    private void addSmelting()
    {
        for(Ore ore : oreStorage)
        {
            ItemStack ingot = null;

            for(String ingotName : ore.ingotNames)
            {
                ArrayList<ItemStack> ingots = OreDictionary.getOres(ingotName);
                if(!ingots.isEmpty())
                    ingot = ingots.get(0);
            }

            if(ingot != null)
            {
                ingot.stackSize = 1;
                ItemStack chunk = new ItemStack(itemChunk, 1, ore.meta);
                GameRegistry.addSmelting(chunk, ingot, ore.xpSmelt);
            }
        }
    }
}
