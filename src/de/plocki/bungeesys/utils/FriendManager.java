package de.plocki.bungeesys.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.plocki.bungeesys.main.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendManager {
	
	Main friends;
	
	public FriendManager(Main friends) {
		this.friends = friends;
	}
	
	public void createTable() {
		friends.getMySQL().update("CREATE TABLE IF NOT EXISTS cFriends_Users(Name VARCHAR(16), UUID VARCHAR(64), Flist TEXT(20000), FRequests TEXT(20000), TRequest VARCHAR(10), Fjump VARCHAR(10), FOnline VARCHAR(10), FSwitch VARCHAR(10), PInvite int, FConnect BIGINT, FcOnline VARCHAR(10), FServer VARCHAR(50))");
	}
	
	public void registerPlayer(ProxiedPlayer p) {
		
		if(!existPlayer(p.getUniqueId().toString())) {
			friends.getMySQL().update("INSERT INTO cFriends_Users(Name, UUID, FList, FRequests, FJump, FOnline, FSwitch, PInvite, FConnect, FConnect, FcOnline, FServer) VALUES ("+p.getName()+", "+ p.getUniqueId().toString()+", , , 'true', 'true', 'true', 'true', '0', '"+System.currentTimeMillis()+"', 'true', '"+ProxyServer.getInstance().getPlayer(p.getUniqueId()).getServer().getInfo().getName()+"');");
			
		} else {
			friends.getMySQL().update("UPDATE cFriends_Users SET Name='"+p.getName()+"' WHERE UUID='"+p.getUniqueId().toString()+"';");
		}
		
	}
	
	public boolean getSettings(String name, String type) {
		return Boolean.parseBoolean(String.valueOf(get(name, "Name", type, "cFriends_Users")));
	}
	
	public String getFriendListRAW(String name) {
		return String.valueOf(get(name, "Name", "Flist", "cFriends_Users"));
	}
	public List<String> getFriendList(String name){
		String friendlist = getFriendListRAW(name);
		List<String> toreturn = new ArrayList<>();
		if(friendlist.isEmpty()) {
			return toreturn;
		}
		String[] friends = friendlist.split(",");
		for(int i = 0; i < friends.length; i++) {
			toreturn.add(friends[i]);
		}
		return toreturn;
	}
	
	public int getFriends(String name) {
		String friendlist = getFriendListRAW(name);
		if(friendlist.isEmpty()) {
			return 0;
		}
		String[] friends = friendlist.split(",");
		return friends.length;
	}
	
	
	public HashMap<String, List<String>> getList(String name){
		
		List<String> friendlist = getFriendList(name);
		List<String> fl = new ArrayList<>();
		
		for(String uuid : friendlist) {
			fl.add(getNamebyUUID(uuid, "cFriends_Users"));
		}
		
		List<String> offline = new ArrayList<>();
		List<String> online = new ArrayList<>();
		for(String entry : fl) {
			if(ProxyServer.getInstance().getPlayer(entry) != null) {
				online.add(entry);
			} else {
				offline.add(entry);
			}
		}
		
		Collections.sort(offline);
		Collections.sort(online);
		HashMap<String, List<String>> hash = new HashMap<>();
		hash.put("offline", offline);
		hash.put("online", online);
		
		return hash;
	}
	
	

	public String getRequestListRAW(String name) {
		return String.valueOf(get(name, "Name", "Flist", "cFriends_Users"));
	}
	public List<String> getRequestList(String name){
		String friendlist = getFriendListRAW(name);
		List<String> toreturn = new ArrayList<>();
		if(friendlist.isEmpty()) {
			return toreturn;
		}
		String[] friends = friendlist.split(",");
		for(int i = 0; i < friends.length; i++) {
			toreturn.add(friends[i]);
		}
		return toreturn;
	}
	
	public int getRequests(String name) {
		String friendlist = getFriendListRAW(name);
		if(friendlist.isEmpty()) {
			return 0;
		}
		String[] friends = friendlist.split(",");
		return friends.length;
	}
	
	
	
	
	
	
	/* Methods to Check */

	public String getUUIDbyName(String playername, String database) {
		String i = "";
		try {
			ResultSet rs = friends.getMySQL()
					.getResult("SELECT * FROM " + database + " WHERE Name= '" + playername + "'");

			if ((!rs.next()) || (String.valueOf(rs.getString("UUID")) == null))
				;

			i = rs.getString("UUID");

		} catch (SQLException e) {

		}
		return i;
	}

	public String getNamebyUUID(String playername, String database) {
		String i = "";
		try {
			ResultSet rs = friends.getMySQL()
					.getResult("SELECT * FROM " + database + " WHERE UUID= '" + playername + "'");

			if ((!rs.next()) || (String.valueOf(rs.getString("Name")) == null))
				;

			i = rs.getString("Name");

		} catch (SQLException e) {

		}
		return i;
	}

	public boolean existPlayer(String uuid) {
		try {
			ResultSet rs = friends.getMySQL().getResult("SELECT * FROM cFriends_Users WHERE UUID= '" + uuid + "'");

			if (rs.next()) {
				return rs.getString("UUID") != null;
			}
			rs.close();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean existPlayerName(String name) {
		try {
			ResultSet rs = friends.getMySQL().getResult("SELECT * FROM cFriends_Users WHERE Name= '" + name + "'");

			if (rs.next()) {
				return rs.getString("Name") != null;
			}
			rs.close();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public Object get(String whereresult, String where, String select, String database) {

		ResultSet rs = friends.getMySQL()
				.getResult("SELECT " + select + " FROM " + database + " WHERE " + where + "='" + whereresult + "'");
		try {
			if(rs.next()) {
				Object v = rs.getObject(select);
				return v;
			}
		} catch (SQLException e) {
			return "ERROR";
		}

		ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§cCould not load friends.mysql-Stats Type."));
		return "ERROR";
	}
	
	public void addFriend(String name, String newFriend) {
		String friendlist = getFriendListRAW(name);
		friendlist = friendlist + newFriend + ",";
		friends.getMySQL().update("UPDATE cFriends_Users SET FList='"+friendlist+"' WHERE NAME='"+name+"'");
	}
	
	public void addRequest(String name, String request) {
		String reqlist = getRequestListRAW(name);
		reqlist = reqlist + request+",";
		friends.getMySQL().update("UPDATE cFriends_Users SET FRequests='"+reqlist+"' WHERE NAME='"+name+"'");
	}
	public void removeFriend(String name, String newFriend) {
		String friendlist = getFriendListRAW(name);
		friendlist = friendlist.replace(newFriend + ",", "");
		friends.getMySQL().update("UPDATE cFriends_Users SET FList='"+friendlist+"' WHERE NAME='"+name+"'");
	}
	
	public void removeRequest(String name, String request) {
		String reqlist = getRequestListRAW(name);
		reqlist = reqlist.replace(request+",", "");
		friends.getMySQL().update("UPDATE cFriends_Users SET FRequests='"+reqlist+"' WHERE NAME='"+name+"'");
	}
}
