package de.plocki.bungeesys.main;

import net.md_5.bungee.config.Configuration;

import de.plocki.bungeesys.utils.FileManager;
import de.plocki.bungeesys.utils.FriendManager;
import de.plocki.bungeesys.utils.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin{
	
	private MySQL mysql;
	private FileManager filemanager;
	private FriendManager friendmanager;
	
	public void onEnable() {
		fetchClasses();
		loadMySQLFiles();
		MySQL.connect();
		
	}
	
	public void onDisable() {
		MySQL.close();
	}
	
	public void fetchClasses() {
		mysql = new MySQL(this);
		filemanager = new FileManager(this);
		friendmanager = new FriendManager(this);
	}
	
	public void loadMySQLFiles() {
		getDataFolder().mkdirs();
		
		if(!filemanager.exists("mysql.yml", "BungeeSystem")) {
			filemanager.createNewFile("mysql.yml", "BungeeSystem");
			
			Configuration cfg = filemanager.getConfiguration("mysql.yml", "BungeeSystem");
			
			cfg.set("user", "root");
			cfg.set("password", "password");
			cfg.set("host", "host");
			cfg.set("database", "database");
			cfg.set("port", "3306");
		}
		Configuration cfg = filemanager.getConfiguration("mysql.yml", "BungeeSystem");
		getMySQL().host = cfg.getString("host");
		getMySQL().username = cfg.getString("user");
		getMySQL().database = cfg.getString("database");
		getMySQL().password = cfg.getString("password");
		getMySQL().port = cfg.getString("port");
	}
	
	public MySQL getMySQL() {
		return mysql;
	}
	
	public FriendManager getFriendManager() {
		return friendmanager;
	}
	
	public FileManager getFileManager() {
		return filemanager;
	}
}
