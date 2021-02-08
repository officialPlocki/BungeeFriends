package de.plocki.bungeesys.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.plocki.bungeesys.main.Main;
import de.plocki.bungeesys.main.Settings;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

public class MySQL {

	public static String username;
	public static String password;
	public static String database;
	public static String host;
	public static String port;
	public static Connection con;

	Main friends;

	public MySQL(Main friends) {

		this.friends = friends;

	}

	public static void connect() {
		if (!isConnected()) {
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + host + ":"+port+"/" + database + "?autoReconnect=true",
						username, password);
				ProxyServer.getInstance().getConsole()
						.sendMessage(new TextComponent(Settings.prefix + "§aSuccessfully connected to MySQL-Database."));
			} catch (SQLException e) {
				ProxyServer.getInstance().getConsole()
				.sendMessage(new TextComponent(Settings.prefix + "§cCould not connect to MySQL-Database, please check your MySQL-Settings."));
			}
		}
	}

	public static void close() {
		if (isConnected()) {
			try {
				con.close();
				con = null;
				ProxyServer.getInstance().getConsole()
						.sendMessage(new TextComponent(Settings.prefix + "§aSuccessfully closed MySQL-Connection."));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isConnected() {
		return con != null;
	}

	public static void update(String qry) {
		if (isConnected()) {
			try {
				con.createStatement().executeUpdate(qry);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static ResultSet getResult(String qry) {
		if (isConnected()) {
			try {
				return con.createStatement().executeQuery(qry);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}