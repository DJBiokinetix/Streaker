package com.survivaldub;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Streaker extends JavaPlugin implements Listener {

	Logger log = Logger.getLogger("Minecraft.LudusTravel");
	Map<String, Integer> Streaker = new HashMap<String, Integer>();

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("kd").setExecutor(this);
		log.info("[Streaker] Ha sido activado correctamente!");
	}

	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if(command.getName().equalsIgnoreCase("kd")){
		if(sender instanceof Player){
			String name = sender.getName();
			if(Streaker.containsKey(name)){
				int kills = Streaker.get(name);
				sender.sendMessage("Your on a " + ChatColor.RED+ Integer.toString(kills)+ ChatColor.WHITE + " Killstreak.");
			}else{
				sender.sendMessage("Your on a " + ChatColor.RED+ 0+ ChatColor.WHITE + " Killstreak.");
			}
		}
		}
		return false;
	}
	@EventHandler
	public void playerdeath (PlayerDeathEvent ev){
		Player p = ev.getEntity();
		if(p.getKiller() instanceof Player){
			Player killer = p.getKiller();
			p.sendMessage("Your killstreak has been destroyed by "+ChatColor.RED+killer.getName());
			addtokillstreak(killer);
		}
		return;
		}

	public void addtokillstreak(Player killer) {
		String name = killer.getName();
		if(Streaker.containsKey(name)){
			int kills = Streaker.get(name);
			kills++;
			Streaker.put(name, kills);
			killer.sendMessage("Your on a " + ChatColor.RED+ Integer.toString(kills)+ ChatColor.WHITE + " Killstreak.");
			runcommands(name,Streaker.get(name));
		}else{
			Streaker.put(name, 1);
			killer.sendMessage("Your on a " + ChatColor.RED+ "1" + ChatColor.WHITE + " Killstreak.");
			runcommands(name,1);
		}
	}

	public void runcommands(String name, int kills){
		String numofkills = Integer.toString(kills);
		boolean rancommands = false;
		int commandnumber = 0;
		while (rancommands == false){
			commandnumber++;
			if(this.getConfig().getString(numofkills+"."+commandnumber) != null){
				String command = this.getConfig().getString(numofkills+"."+commandnumber).replaceAll("%name%", name);
				String command1 = command.replaceAll("%killstreak%", numofkills);
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command1);
			}
			if(this.getConfig().getString(numofkills+"."+commandnumber) == null){
				return;
			}
		}
	}
}
