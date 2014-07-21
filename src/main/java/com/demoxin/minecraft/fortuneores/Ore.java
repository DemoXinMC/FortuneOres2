package com.demoxin.minecraft.fortuneores;

import java.util.ArrayList;

public class Ore
{
    public String name;
    public int meta;
    public boolean enabled;
    public int dropCount;
    
    public float xpSmelt;
    public float xpDrop;
    
    public ArrayList<String> oreNames;
    public ArrayList<String> ingotNames;
    
    public Ore(String fName, int fMeta)
    {
        name = fName;
        meta = fMeta;
        
        xpSmelt = 0;
        xpDrop = 0;
        
        oreNames = new ArrayList<String>();
        ingotNames = new ArrayList<String>();
    }
    
    public void addOreName(String fName)
    {
        oreNames.add("ore" + fName);
        oreNames.add("oreNether" + fName);
    }
    
    public void addIngotName(String fName)
    {
        ingotNames.add("ingot" + fName);
    }
}
