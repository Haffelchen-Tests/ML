package me.haffel.ml;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

public class Main  extends JavaPlugin
{
	@Getter private static Main instance;
	
	@Override
	public void onEnable()
	{
		instance = this;
		System.out.println("<--=-=-=-=-=-==Multi Languages Plugin==-=-=-=-=-=--->");
		System.out.println("| Version: 1.0                                      |");
		System.out.println("| Description: A simple to use translation plugin.  |");
		System.out.println("|              Each player can have its own         |");
		System.out.println("|              translation settings.                |");
		System.out.println("<--=-=-=-=-=-=-=-=-=-==Started==-=-=-=-=-=-=-=-=-=-->");
		saveConfig();
	}
	
	@Override
	public void onDisable()
	{
		System.out.println("<--=-=-=-=-=-==Multi Languages Plugin==-=-=-=-=-=--->");
		System.out.println("| Version: 1.0                                      |");
		System.out.println("| Description: A simple to use translation plugin.  |");
		System.out.println("|              Each player can have its own         |");
		System.out.println("|              translation settings.                |");
		System.out.println("<--=-=-=-=-=-=-=-=-=-==Stopped==-=-=-=-=-=-=-=-=-=-->");
	}
}
