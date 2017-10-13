package mod.sparkyfox.servermod.lib;

import mod.sparkyfox.servermod.init.ModBlocks;
import mod.sparkyfox.servermod.init.ModItems;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryHandler {
	
	public static void registerOreDictionary() {
		
//===========================================================================================================================================================================================\\
																								//Material\\
		
		/**Titanium*/
		OreDictionary.registerOre("Titanium", ModItems.TitaniumIngot);
		OreDictionary.registerOre("Titanium", ModItems.TitaniumNugget);
		OreDictionary.registerOre("Titanium", ModBlocks.TitaniumBlock);
		OreDictionary.registerOre("Titanium", ModBlocks.TitaniumOre);


		

		
		/**Cobalt*/
		OreDictionary.registerOre("Cobalt", ModItems.CobaltIngot);
		OreDictionary.registerOre("Cobalt", ModBlocks.CobaltBlock);
		OreDictionary.registerOre("Cobalt", ModBlocks.CobaltOre);
		
		
		

		/**Silicon*/
		OreDictionary.registerOre("Silicon", ModItems.SiliconIngot);
		OreDictionary.registerOre("Silicon", ModBlocks.SiliconBlock);
		OreDictionary.registerOre("Silicon", ModBlocks.SiliconOre);
		OreDictionary.registerOre("itemSilicon", ModItems.SiliconIngot);
		
		
		
		
		/**Nickel*/
		OreDictionary.registerOre("Nickel", ModItems.NickelIngot);
		OreDictionary.registerOre("Nickel", ModBlocks.NickelBlock);
		OreDictionary.registerOre("Nickel", ModBlocks.NickelOre);
		OreDictionary.registerOre("itemNickel", ModItems.NickelIngot);
		
		
		
		
		/**Brass*/
		OreDictionary.registerOre("Alubrass", ModItems.BrassIngot);
		OreDictionary.registerOre("Alubrass", ModBlocks.BrassBlock);
		OreDictionary.registerOre("Alubrass", ModBlocks.BrassOre);
		OreDictionary.registerOre("itemBrass", ModItems.BrassIngot);
		
//===========================================================================================================================================================================================\\
																								//Ingots\\
		
		OreDictionary.registerOre("ingotTitanium", ModItems.TitaniumIngot);
		OreDictionary.registerOre("ingotCobalt", ModItems.CobaltIngot);
		OreDictionary.registerOre("ingotSilicon", ModItems.SiliconIngot);
		OreDictionary.registerOre("ingotNickel", ModItems.NickelIngot);
		OreDictionary.registerOre("ingotAlubrass", ModItems.BrassIngot);
		
//===========================================================================================================================================================================================\\
																								//Blocks\\
		
		OreDictionary.registerOre("blockTitanium", ModBlocks.TitaniumBlock);
		OreDictionary.registerOre("blockCobalt", ModBlocks.CobaltBlock);
		OreDictionary.registerOre("blockSilicon", ModBlocks.SiliconBlock);
		OreDictionary.registerOre("blockNickel", ModBlocks.NickelBlock);
		OreDictionary.registerOre("blockAlubrass", ModBlocks.BrassBlock);
		
//===========================================================================================================================================================================================\\
																								//Ores\\
		
		OreDictionary.registerOre("oreTitanium", ModBlocks.TitaniumOre);
		OreDictionary.registerOre("oreCobalt", ModBlocks.CobaltOre);
		OreDictionary.registerOre("oreSilicon", ModBlocks.SiliconOre);
		OreDictionary.registerOre("oreNickel", ModBlocks.NickelOre);
		OreDictionary.registerOre("oreAlubrass", ModBlocks.BrassOre);
		
		
//===========================================================================================================================================================================================\\
																								//Nuggets\\
		
		OreDictionary.registerOre("nuggetTitanium", ModItems.TitaniumNugget);
		
//===========================================================================================================================================================================================\\
																								//Fluids\\

		
		
		
	}

}
