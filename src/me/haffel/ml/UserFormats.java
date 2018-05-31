package me.haffel.ml;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class UserFormats
{
	private static YamlConfiguration userFormats = new YamlConfiguration();
	
	public static void setFormat(String pluginName, String playerName, String formatKey, String formatValue)
	{
		if(!getUserFormats(pluginName).isConfigurationSection(playerName))
		{
			getUserFormats(pluginName).createSection(playerName);
		}
		
		getUserFormats(pluginName).getConfigurationSection(playerName).set(formatKey, formatValue);
	}
	
	public static String getFormat(String pluginName, String playerName, String format)
	{
		if(isFormatSet(pluginName, playerName, format))
		{
			return getUserFormats(pluginName).getConfigurationSection(playerName).getString(format);
		}
		
		return "";
	}
	
	public static boolean isFormatSet(String pluginName, String playerName, String format)
	{
		if(getUserFormats(pluginName).isConfigurationSection(playerName) &&
				getUserFormats(pluginName).getConfigurationSection(playerName).contains(format))
		{
			return true;
		}
		
		return false;
	}
	
	public static void setUserLanguage(String pluginName, String playerName, String language)
	{
		if(!getUserFormats(pluginName).isConfigurationSection(playerName))
		{
			getUserFormats(pluginName).createSection(playerName);
		}
		
		getUserFormats(pluginName).getConfigurationSection(playerName).set("language", language);
	}
	
	public static String getUserLanguage(String pluginName, String playerName)
	{
		if(getUserFormats(pluginName).contains(playerName) &&
				getUserFormats(pluginName).isConfigurationSection(playerName) &&
				getUserFormats(pluginName).getConfigurationSection(playerName).contains("language"))
		{
			return getUserFormats(pluginName).getConfigurationSection(playerName).getString("language");
		} else
		{
			return "en";
		}
	}
	
	@SuppressWarnings("static-access")
	public static YamlConfiguration getUserFormats(String pluginName)
	{
		File userFormatsFile = new File(Main.getInstance().getDataFolder() + "/" + pluginName + "/user_formats.yml");
		
		if(!userFormatsFile.exists())
		{
			userFormatsFile.getParentFile().mkdirs();
			try
			{
				userFormatsFile.createNewFile();
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return userFormats.loadConfiguration(userFormatsFile);
	}
}
