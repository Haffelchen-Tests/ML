package me.haffel.ml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class LanguageFile
{
	private static YamlConfiguration languageConfig;
	private static File languageFile;

	@SuppressWarnings("static-access")
	public static FileConfiguration getLanguageFile(String pluginName, String language)
	{
		languageFile = new File(Main.getInstance().getDataFolder() + "/" + pluginName + "/language_" + language + ".yml");

		if (isLanguageFile(pluginName, language))
		{
			return languageConfig = new YamlConfiguration().loadConfiguration(languageFile);
		}
		
		System.err.println("[ML] Language file '" + language + "' of plugin '" + pluginName + "' doesn't exist");
		return null;
	}

	@SuppressWarnings("static-access")
	public static FileConfiguration getLanguageFile(String pluginName)
	{
		if (getConfig().getConfigurationSection("PluginsToTranslate").isConfigurationSection(pluginName) && getConfig().getConfigurationSection("PluginsToTranslate").getConfigurationSection(pluginName).isString("default"))
		{
			languageFile = new File(Main.getInstance().getDataFolder() + "/" + pluginName + "/language_" + getConfig().getConfigurationSection("PluginsToTranslate").getConfigurationSection(pluginName).getString("default") + ".yml");

			if (hasDefaultLanguageFile(pluginName))
			{
				return languageConfig = new YamlConfiguration().loadConfiguration(languageFile);
			}
		} else
		{
			System.err.println("[ML] Default language of plugin '" + pluginName + "' not selected in Config");
			return null;
		}
		
		System.err.println("[ML] Language file '" + getConfig().getString("Language") + "' of plugin '" + pluginName + "' doesn't exist");
		return null;
	}

	public static boolean isLanguageFile(String pluginName, String language)
	{
		languageFile = new File(Main.getInstance().getDataFolder() + "/" + pluginName + "/language_" + language + ".yml");

		if (languageFile.exists())
		{
			return true;
		}
		return false;
	}
	
	public static boolean hasDefaultLanguageFile(String pluginName)
	{
		if (getConfig().getConfigurationSection("PluginsToTranslate").isConfigurationSection(pluginName) && getConfig().getConfigurationSection("PluginsToTranslate").getConfigurationSection(pluginName).isString("default"))
		{
			languageFile = new File(Main.getInstance().getDataFolder() + "/" + pluginName + "/language_" + getConfig().getConfigurationSection("PluginsToTranslate").getConfigurationSection(pluginName).getString("default") + ".yml");

			if (languageFile.exists())
			{
				return true;
			}
		}
		
		return false;
	}
	
	@SuppressWarnings("static-access")
	public static void createAndLoadLanguageFile(String pluginName)
	{
		if (getConfig().getConfigurationSection("PluginsToTranslate").isConfigurationSection(pluginName) &&
				getConfig().getConfigurationSection("PluginsToTranslate").getConfigurationSection(pluginName).isString("default"))
		{
			languageFile = new File(
					Main.getInstance().getDataFolder() + "/" + pluginName + "/language_" + getConfig().getConfigurationSection("PluginsToTranslate").getConfigurationSection(pluginName).getString("default") + ".yml");

			if (!languageFile.exists())
			{
				try
					{
						Files.copy(new File(Main.getInstance().getDataFolder() + "/" + pluginName + "/language_TEMPLATE.yml").toPath(), new File(Main.getInstance().getDataFolder() + "/" + pluginName + "/language_" + getConfig().getConfigurationSection("PluginsToTranslate").getConfigurationSection(pluginName).getString("default") + ".yml").toPath());
					
					} catch (IOException e)
					{
						e.printStackTrace();
					}
			} else
			{
				languageConfig = new YamlConfiguration().loadConfiguration(languageFile);
			}
		}
	}

	public static String getTranslatedString(String pluginName, Player p, String toTranslate)
	{
		if (getLanguageFile(pluginName).contains(toTranslate))
		{
			if (getLanguageFile(pluginName).isConfigurationSection(toTranslate))
			{
				if (getLanguageFile(pluginName).getConfigurationSection(toTranslate).contains("permissions"))
				{
					for (String perms : perms(pluginName, toTranslate).getKeys(false))
					{
						if (perms.equalsIgnoreCase("default"))
						{
							return perms(pluginName, toTranslate).getString(perms);
						}

						if (p != null)
						{
							if (perms(pluginName, toTranslate).isList(perms))
							{
								if (perms(pluginName, toTranslate).contains(perms + "Message"))
								{
									List<String> permGroup = perms(pluginName, toTranslate).getStringList(perms);

									for (int i = 0; i < permGroup.size(); i++)
									{
										if (p.hasPermission(perms(pluginName, toTranslate).getStringList(perms).get(i)))
										{
											i = permGroup.size();
											return perms(pluginName, toTranslate).getString(perms + "Message");
										}
									}
								} else
								{
									return ChatColor.RED
											+ "Message can't be found.\n\nPls report this error. Missing message for permission group '"
											+ perms + "' of translation '" + toTranslate + "' in Language '"
											+ getConfig().getConfigurationSection("PluginsToTranslate").getConfigurationSection(pluginName).getString("default") + "'";
								}
							} else
							{
								if (p.hasPermission(perms))
								{
									return perms(pluginName, toTranslate).getString(perms);
								}
							}
						} else
						{
							return ChatColor.RED
									+ "No player specified in translation '"
									+ toTranslate + "'. You can't use permissions here";
						}
					}

					return ChatColor.RED + "Default value for translation \"" + toTranslate + "\" can't be found";
				}
			} else
			{
				if (p != null)
				{
					return getLanguageFile(pluginName).getString(toTranslate).replaceAll("%Name%", p.getName())
							.replaceAll("%Time%", "%d").replaceAll("%break%", "\n");

				} else
				{
					return getLanguageFile(pluginName).getString(toTranslate).replaceAll("%Time%", "%d").replaceAll("%break%",
							System.lineSeparator());
				}
			}
		}

		return ChatColor.RED + "String '" + toTranslate
				+ "' cant be found. Pls report this error. Missing String '" + toTranslate
				+ "' in Language '" + getConfig().getConfigurationSection("PluginsToTranslate").getConfigurationSection(pluginName).getString("default") + "'";
	}

	@SuppressWarnings("static-access")
	public static void saveLanguageFile(String pluginName, String language)
	{
		languageFile = new File(Main.getInstance().getDataFolder() + "/language_" + language + ".yml");

		try
		{
			languageConfig = new YamlConfiguration().loadConfiguration(languageFile);
			languageConfig.save(languageFile);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public static void saveLanguageFile(String pluginName)
	{
		languageFile = new File(
				Main.getInstance().getDataFolder() + "/language_" + getConfig().getString("Language") + ".yml");

		try
		{
			languageConfig = new YamlConfiguration().loadConfiguration(languageFile);
			languageConfig.save(languageFile);

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static FileConfiguration getConfig()
	{
		return Main.getInstance().getConfig();
	}

	public static void saveConfig()
	{
		Main.getInstance().saveConfig();
	}
	
	private static ConfigurationSection perms(String pluginName, String toTranslate)
	{
		return getLanguageFile(pluginName).getConfigurationSection(toTranslate).getConfigurationSection("permissions");
	}
	
	public static void setDefaultLanguage(String pluginName, String language)
	{
		if(!getConfig().isConfigurationSection("PluginsToTranslate"))
		{
			getConfig().createSection("PluginsToTranslate");
		}
		
		if(!getConfig().getConfigurationSection("PluginsToTranslate").isConfigurationSection(pluginName))
		{
			getConfig().getConfigurationSection("PluginsToTranslate").createSection(pluginName);
		}
		
		getConfig().getConfigurationSection("PluginsToTranslate").getConfigurationSection(pluginName).set("default", language);
		saveConfig();
	}
	
	public static String getDefaultLanguage(String pluginName)
	{
		if(getConfig().isConfigurationSection("PluginsToTranslate") && getConfig().getConfigurationSection("PluginsToTranslate").isConfigurationSection(pluginName))
		{
			return getConfig().getConfigurationSection("PluginsToTranslate").getConfigurationSection(pluginName).getString("default");
		} else
			System.err.println("[ML] Default language of plugin '" + pluginName + "' not selected in Config");
			return "en";
	}
	
	public static void copyTemplate(String pluginName)
	{
		if(!new File(Main.getInstance().getDataFolder() + "/" + pluginName + "/language_TEMPLATE.yml").exists())
		{
			try
			{
				File to = new File(Main.getInstance().getDataFolder() + "/" + pluginName + "/language_TEMPLATE.yml");
				to.getParentFile().mkdirs();
				Files.copy(new File("plugins/" + pluginName + "/language_TEMPLATE.yml").toPath(), to.toPath());
			
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
