package de.plocki.bungeesys.commands;

import de.plocki.bungeesys.main.Main;
import de.plocki.bungeesys.main.Settings;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CMD_friend extends Command{
	
	Main friends;
	
	public CMD_friend(Main friends) {
		super("friend");
		this.friends = friends;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer){
			ProxiedPlayer p = (ProxiedPlayer)sender;
			if(args.length==0) {
				sendHelp(p, 1);
			}else if(args[0].equalsIgnoreCase("add")) {
				
				if(args.length==2) {
					String target = args[1];
					ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(target);
					if(friend == null) {
						p.sendMessage(new TextComponent(Settings.prefix+"§cDer Spieler ist momentan nicht online."));
						return;
					}
					if(p.getName().equals(friend.getName())||p == friend) {
						p.sendMessage(new TextComponent(Settings.prefix+"§cDu kannst nicht mit dir selbst befreundet sein!"));
						return;
					}
					if(friends.getFriendManager().getFriends(friend.getName())>=100) {
						p.sendMessage(new TextComponent(Settings.prefix+"§cDer Spieler hat sein Freundeslimit von 100 Spielern Erreicht!"));
						return;
					}
					if(friends.getFriendManager().getRequestListRAW(friend.getName()).contains(p.getUniqueId().toString())) {
						p.sendMessage(new TextComponent(Settings.prefix+"§cDu hast diesem Spieler bereits eine Freundschaftsanfrage gesendet!"));
						return;
					}
					if(!friends.getFriendManager().getSettings(friend.getName(), "FRequest")) {
						p.sendMessage(new TextComponent(Settings.prefix+"§cDu kannst diesem Spieler keine Freunschaftsanfrage senden!"));
						return;
					}
					if(friends.getFriendManager().getFriendListRAW(friend.getName()).contains(p.getUniqueId().toString())) {
						p.sendMessage(new TextComponent(Settings.prefix+"§cDu bist bereits mit diesem Spieler befreundet!"));
					}
					if(friends.getFriendManager().getRequestListRAW(p.getName()).contains(friend.getUniqueId().toString())) {
						//add friend
					}
					
					p.sendMessage(new TextComponent(Settings.prefix+"Du hast "+ friend.getName()+" eine Freundschaftsanfrage gesendet!"));
					friend.sendMessage(new TextComponent(Settings.prefix+"§e"+p.getName()+" hat dir eine Freundschaftsanfrage gesendet!"));
					sendConfirmation(p, friend);
					friends.getFriendManager().addRequest(friend.getName(), p.getUniqueId().toString());
					
				} else {
					p.sendMessage(new TextComponent(Settings.prefix+"§e/friend add <Spieler>"));
					return;
				}
				
			}else if(args[0].equalsIgnoreCase("2")) {
				sendHelp(p, 2);
			}else{
				sendHelp(p, 1);
			}
		}
		
	}
	
	public void sendConfirmation(ProxiedPlayer p, ProxiedPlayer t) {
		TextComponent accept = new TextComponent("§aAkzeptieren");
		accept.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "friend accept "+p.getName()));
		TextComponent deny = new TextComponent("§aAkzeptieren");
		deny.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "friend deny "+p.getName()));
		
		t.sendMessage(new TextComponent(accept +"§7/"+deny));
	}
	
	public void sendHelp(ProxiedPlayer p, int page) {
		p.sendMessage(new TextComponent(Settings.prefix+"§bFreundeverwaltung §8| §6Seite§8: §e" + page));
		if(page==1||page==0||page>=2||page<=0) {
			p.sendMessage(new TextComponent("§e/friend 2 §8- §7Zeigt die 2. Hilfeseite an"));
			p.sendMessage(new TextComponent("§e/friend add <Spieler> §8- §7Füge einen Freund hinzu"));
			p.sendMessage(new TextComponent("§e/friend remove <Spieler> §8- §7Entfernt einen Freund"));
			p.sendMessage(new TextComponent("§e/friend accept <Spieler> §8- §7Akzeptiert eine Freundschaftsanfrage"));
			p.sendMessage(new TextComponent("§e/friend deny <Spieler> §8- §7Lehnt eine Freundschaftsanfrage ab"));
			p.sendMessage(new TextComponent("§e/friend acceptall §8- §7Akzeptiert alle Freundschaftsanfragen"));
			p.sendMessage(new TextComponent("§e/friend denyall §8- §7Lehnt alle Freundschaftsanfragen ab"));
			p.sendMessage(new TextComponent("§e/friend list §8- §7Listet alle Freunde auf"));
		} else {
			p.sendMessage(new TextComponent("§e/friend requests §8- §7Zeigt dir alle offenen Freundschaftsanfragen an"));
			p.sendMessage(new TextComponent("§e/friend clear §8- §7Leert deine Freundesliste"));
			p.sendMessage(new TextComponent("§e/friend jump <Spieler> §8- §7Teleportiere dich auf den Server wo dein Freund gerade ist"));
			p.sendMessage(new TextComponent("§e/friend togglerequest §8- §7Aktivieren/Deaktivieren der Freundschaftsanfragen"));
			p.sendMessage(new TextComponent("§e/friend togglejump §8- §7Aktiviert/Deaktiviert das Freunde sich auf deinen Server teleportieren können"));
			p.sendMessage(new TextComponent("§e/friend togglenotify §8- §7Aktiviert/Deaktiviert Benachrichtigungen"));
			p.sendMessage(new TextComponent("§e/friend toggleswitch §8- §7ktiviert/Deaktiviert Switch"));
		}
	}
	
}
