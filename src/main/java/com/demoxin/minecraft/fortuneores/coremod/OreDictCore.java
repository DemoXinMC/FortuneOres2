package com.demoxin.minecraft.fortuneores.coremod;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.oredict.OreDictionary;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.demoxin.minecraft.fortuneores.FortuneOres;
import com.demoxin.minecraft.fortuneores.Ore;
import com.demoxin.minecraft.fortuneores.OreDictionaryMagic;

public class OreDictCore implements IClassTransformer
{
    public static boolean reflected = false;
    private static Method registerOreImpl;
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
        if(transformedName.equals("net.minecraftforge.oredict.OreDictionary"))
        {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(classNode, 0);
            
            Iterator<MethodNode> methods = classNode.methods.iterator();
            while(methods.hasNext())
            {
                MethodNode m = methods.next();
                if(m.name.equals("registerOre"))
                {
                    Iterator<AbstractInsnNode> instructions = m.instructions.iterator();
                    while(instructions.hasNext())
                    {
                        AbstractInsnNode instruction = instructions.next();
                        if(instruction.getType() == AbstractInsnNode.METHOD_INSN)
                        {
                            MethodInsnNode insn = (MethodInsnNode)instruction;
                            if(insn.name.equals("registerOreImpl"))
                            {
                                m.instructions.clear();
                                m.instructions.add(new VarInsnNode(ALOAD, 0));
                                m.instructions.add(new VarInsnNode(ALOAD, 1));
                                m.instructions.add(new MethodInsnNode(INVOKESTATIC, "com/demoxin/minecraft/fortuneores/coremod/OreDictCore", "registerOre", "(Ljava/lang/String;Lnet/minecraft/item/ItemStack;)V", false));
                                m.instructions.add(new InsnNode(RETURN));
                            }
                        }
                    }
                }
            }
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(cw);
            return cw.toByteArray();
        }
        return basicClass;
    }
    
    // Return true to kill ore registration
    public static void registerOre(String name, ItemStack input)
    {       
        if(FortuneOres.itemChunk != null && input.getItem() == FortuneOres.itemChunk)
        {
            allow(name, input);
            return;
        }

        for(Ore ore : FortuneOres.ores)
        {
            for(String oreName : ore.oreNames)
            {
                if(name.equals(oreName))
                {
                    OreDictionaryMagic.preserve(ore, input);
                    return;
                }
            }
        }

        allow(name, input);
    }
    
    private static void allow(String name, ItemStack input)
    {
        if(!reflected)
            reflect();

        try
        {
            registerOreImpl.invoke(null, name, input);
        } 
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        } 
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        } 
        catch(InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }
    
    private static void reflect()
    {
        for(Method method : OreDictionary.class.getDeclaredMethods())
        {
            if(method.getName().equals("registerOreImpl"))
            {
                registerOreImpl = method;
                break;
            }
        }
        
        if(registerOreImpl == null)
            System.out.println("Failed to get registerOreImpl!  Incoming NPEs and whatnot!");
        
        registerOreImpl.setAccessible(true);
        reflected = true;
    }
}
