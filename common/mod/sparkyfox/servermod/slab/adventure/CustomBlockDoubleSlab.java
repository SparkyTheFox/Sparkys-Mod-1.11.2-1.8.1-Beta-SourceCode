package mod.sparkyfox.servermod.slab.adventure;

public class CustomBlockDoubleSlab extends CustomBlockSlab
{
	public CustomBlockDoubleSlab(String name, float hardness, float resistance) 
	{
		super(name, hardness, resistance);
	}
	
	@Override
	public boolean isDouble() 
	{
		return true;
	}
	
}
