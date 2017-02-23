package mod.sparkyfox.servermod.init;

import mod.sparkyfox.servermod.ServerMod;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSoundEvent {

	public static SoundEvent smg;
	public static SoundEvent impact;
	
	public ModSoundEvent() {
		init();
		register();
	}

	private void init() {
		smg = new SoundEvent(new ResourceLocation(ServerMod.MOD_ID, "smg")).setRegistryName(new ResourceLocation(ServerMod.MOD_ID, "smg"));
		impact = new SoundEvent(new ResourceLocation(ServerMod.MOD_ID, "impact")).setRegistryName(new ResourceLocation(ServerMod.MOD_ID, "impact"));
	}

	private void register() {
		registerSound(smg);
		registerSound(impact);
	}

	private void registerSound(SoundEvent sound) {
		GameRegistry.register(sound);
	}

	public static void registerSounds() {

		
	}

}
