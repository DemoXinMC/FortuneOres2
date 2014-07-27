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
    public static final String VERSION = "2.0.2";
    // Mod Info End

    // Singleton
    @Instance("FortuneOres")
    public static FortuneOres instance;
    public static Config config;
    public static ArrayList<Ore> oreStorage;

    public static CreativeTabs creativeTab;
    public static Item itemChunk;

    public static int nextMeta;
    
    public static boolean allowProcessing;

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
        MinecraftForge.EVENT_BUS.register(new OreDictHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        addOreDicting();
        MinecraftForge.EVENT_BUS.register(new OreSwapper());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
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
    
    public void addOre(String oreName, int xpMin, int xpMax, float xpSmelt)
    {
        Ore newOre = new Ore(oreName, nextMeta, xpMin, xpMax, xpSmelt);
        newOre.addOreName(oreName);
        newOre.addIngotName(oreName);
        oreStorage.add(newOre);
        nextMeta++;
    }

    private void setupOres()
    {
        // Vanilla
        addOre("Iron", 1, 5, 0.4f);
        addOre("Gold", 2, 6, 1.0f);

        // Common Ores
        addOre("Copper", 1, 5, 0.4f);
        addOre("Tin", 1, 5, 0.4f);
        addOre("Lead", 1, 5, 0.4f);
        addOre("Silver", 2, 6, 1.0f);

        // Less Common Ores
        addOre("Nickel", 1, 5, 0.4f);
        addOre("Platinum", 2, 6, 1.0f);
        addOre("Aluminum", 1, 5, 0.4f);
        oreStorage.get(nextMeta-1).addOreName("Aluminium");
        oreStorage.get(nextMeta-1).addOreName("NaturalAluminum");
        oreStorage.get(nextMeta-1).addIngotName("Aluminium");
        oreStorage.get(nextMeta-1).addIngotName("NaturalAluminum");

        // TiCon Ores
        addOre("Cobalt", 4, 7, 0.8f);
        addOre("Ardite", 3, 6, 0.8f);

        // Metallurgy Base
        addOre("Manganese", 1, 5, 0.4f);
        
        // Metallurgy Precious
        addOre("Zinc", 2, 6, 1.0f);
        
        // Metallurgy Nether
        addOre("Ignatius", 3, 7, 0.8f);
        addOre("ShadowIron", 3, 7, 0.8f);
        addOre("Lemurite", 3, 7, 0.8f);
        addOre("Midasium", 3, 7, 0.8f);
        addOre("Vyroxeres", 3, 7, 0.8f);
        addOre("Ceruclase", 3, 7, 0.8f);
        addOre("Alduorite", 3, 7, 0.8f);
        addOre("Kalendrite", 3, 7, 0.8f);
        addOre("Vulcanite", 3, 7, 0.8f);
        addOre("Sanguinite", 3, 7, 0.8f);
        
        // Metallurgy Fantasy
        addOre("Prometheum", 3, 7, 0.9f);
        addOre("DeepIron", 3, 7, 0.9f);
        addOre("Infuscolium", 3, 7, 0.9f);
        addOre("Oureclase", 3, 7, 0.9f);
        addOre("AstralSilver", 3, 7, 0.9f);
        addOre("Carmot", 3, 7, 0.9f);
        addOre("Mithril", 3, 7, 0.9f);
        oreStorage.get(nextMeta-1).addOreName("Mythril");
        oreStorage.get(nextMeta-1).addIngotName("Mythril");
        addOre("Rubracium", 3, 7, 0.9f);
        addOre("Orichalcum", 3, 7, 0.9f);
        addOre("Adamantine", 3, 7, 0.9f);
        addOre("Atlarus", 3, 7, 0.9f);

        // Metallurgy End
        addOre("Eximite", 3, 7, 0.8f);
        addOre("Meutoite", 3, 7, 0.8f);

        // Factorization
        addOre("DarkIron", 1, 5, 0.4f);
    }
    
    private void addOreDicting()
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
