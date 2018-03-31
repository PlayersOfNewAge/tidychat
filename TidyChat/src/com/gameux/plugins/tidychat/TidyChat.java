package com.gameux.plugins.tidychat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class TidyChat extends JavaPlugin implements Listener {

	public LogWriter lw;
	
	// Listeler:
	public List<String> banned_words = getConfig().getStringList("bannedWords");
	public List<String> authorized_players = getConfig().getStringList("authorizedPlayers");
	public List<String> bypass_players = getConfig().getStringList("bypassPlayers");
	
	// -------------------------------------------------------------------------
	
	// Mute Süreleri
	
	protected String kufur = new Integer(getConfig().getInt("kufur")).toString();
	protected String argo = new Integer(getConfig().getInt("argo")).toString();
	protected String aileviKufur = new Integer(getConfig().getInt("aileviKufur")).toString();
	protected String reklam = new Integer(getConfig().getInt("reklam")).toString();
	
	// -------------
	
	public boolean defaultBannedWorld = false;
	
	public File chatLog;
	public String plFolder = getDataFolder().getAbsolutePath();
	
	public void onEnable() {
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		getServer().getPluginManager().registerEvents(this, this);
		
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "\n\n[GameUx/TidyChat]: " + ChatColor.GREEN + "TidyChat aktif edildi. \n");
		getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "\nAuthor: " + ChatColor.LIGHT_PURPLE + "GameUx Studios / Poyraz Hancilar \n\n");
		
		new File(plFolder).mkdirs();
		chatLog = new File(plFolder + File.separator + "ChatLog.txt");
		try {
			if(!chatLog.exists()) {
				chatLog.createNewFile();
			}
		} catch (Exception exception) {
			
		}
		
	}
	
	public void onDisable() {
		
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "\n\n[GameUx/TidyChat]: " + ChatColor.RED + "TidyChat de-aktif edildi. \n");
		getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "\nAuthor: " + ChatColor.LIGHT_PURPLE + "GameUx Studios / Poyraz Hancilar \n\n");
		
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		
		Player p = (Player) sender;
		
		for(String yetkili : p.getName().split(" ")) {
			
			if(getConfig().getStringList("authorizedPlayers").contains(yetkili)) {
				
				if(cmd.getName().equalsIgnoreCase("tc")) {
					
					if(args.length == 0) {
						
						p.sendMessage(""
								+ ChatColor.AQUA + "\n\n-- Tidy Chat | v1.0 | By GameUx Studios / Poyraz Hancýlar --\n"
								+ ChatColor.YELLOW + "/tc " + ChatColor.GREEN + "-> Yardýmý görüntüler.\n" 
								+ ChatColor.YELLOW + "/tc kelime-ekle kelime " + ChatColor.GREEN + "-> Yasaklanan kelime listesine bir kelime ekler.\n"
								+ ChatColor.YELLOW + "/tc kelime-sil kelime " + ChatColor.GREEN + "-> Yasaklanan kelime listesinden kelimeyi siler.\n" 
								+ ChatColor.YELLOW + "/tc yetkili-ekle yetkiliAdý " + ChatColor.GREEN + "-> Komutlara eriþim saðlayacak bir yetkili ekler.\n"
								+ ChatColor.YELLOW + "/tc yetkili-sil yetkiliAdý " + ChatColor.GREEN + "-> Komutlara eriþim saðlayan bir yetkiliyi siler.\n"
								+ ChatColor.YELLOW + "/tc b-ekle oyuncuAdý " + ChatColor.GREEN + "-> Küfür Korumasý etki etmeyecek bir oyuncuyu listeye ekler.\n"
								+ ChatColor.YELLOW + "/tc b-sil oyuncuAdý " + ChatColor.GREEN + "-> Küfür Korumasý etki etmeyecek bir oyuncuyu listeden çýkarýr.\n"
								+ ChatColor.YELLOW + "/tc yenile " + ChatColor.GREEN + "-> config.yml dosyasýný yeniler.\n"
								+ ChatColor.YELLOW + "/tc -r ChatLog " + ChatColor.GREEN + "-> ChatLog.txt dosyasýný sýfýrlar.\n"
								+ ChatColor.YELLOW + "/tc -s " + ChatColor.GREEN + "-> Sohbeti Temizler.\n\n" 
								+ "\n\n");
						
					}
					
					if(args[0].equalsIgnoreCase("kelime-ekle") || args[0].equalsIgnoreCase("-ke") && p.hasPermission("tc.addword")) {
						
						args[1] = args[1].toLowerCase();
						
						banned_words.add(args[1]);
						getConfig().set("bannedWords", banned_words);
						saveConfig();
						sender.sendMessage(ChatColor.AQUA + "[TidyChat]: " + ChatColor.GREEN + "Kelime " + ChatColor.RED + args[1] + ChatColor.GREEN + " eklendi.");
					}
					
					if(args[0].equalsIgnoreCase("kelime-sil") || args[0].equalsIgnoreCase("-ks")) {
						
						args[1] = args[1].toLowerCase();
						banned_words.remove(args[1]);
						getConfig().set("bannedWords", banned_words);
						saveConfig();
						sender.sendMessage(ChatColor.AQUA + "[TidyChat]: " + ChatColor.GREEN + "Kelime " + ChatColor.RED + args[1] + ChatColor.GREEN + " silindi.");
						
					}
					
					if(args[0].equalsIgnoreCase("yenile")) {
						
						reloadConfig();
						saveConfig();
						
						sender.sendMessage(ChatColor.AQUA + "[TidyChat]: " + ChatColor.GREEN + "Eklenti Baþarýyla Yenilendi.");
						
					}
					
					// Yetkili Kiþi Ekleme/Silme
					
					if(args[0].equalsIgnoreCase("yetkili-ekle")) {
						
						authorized_players.add(args[1]);
						getConfig().set("authorizedPlayers", authorized_players);
						saveConfig();
						sender.sendMessage(ChatColor.AQUA + "[TidyChat]: " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + " Yetkili Kiþi Listesine Eklendi!");
						
					}
					
					if(args[0].equalsIgnoreCase("yetkili-sil")) {
						
						authorized_players.remove(args[1]);
						getConfig().set("authorizedPlayers", authorized_players);
						saveConfig();
						sender.sendMessage(ChatColor.AQUA + "[TidyChat]: " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + " Yetkili Kiþi Listesinden Silindi!");
						
					}
					
					// -------------------
					
					// Küfür-Engeli Olmayan Oyuncyu Ekleme/Silme
					
					if(args[0].equalsIgnoreCase("b-ekle")) {
						
						bypass_players.add(args[1]);
						getConfig().set("bypassPlayers", bypass_players);
						saveConfig();
						sender.sendMessage(ChatColor.AQUA + "[TidyChat]: " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + " Küfür Engeli Olmayanlar Listesine Eklendi!");
						
					}
					
					if(args[0].equalsIgnoreCase("b-sil")) {
						
						bypass_players.remove(args[1]);
						getConfig().set("bypassPlayers", bypass_players);
						saveConfig();
						sender.sendMessage(ChatColor.AQUA + "[TidyChat]: " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + " Küfür Engeli Olmayanlar Listesinden Silindi!");
						
					}
					
					// -----------------------------------------
					
					if(args[0].equalsIgnoreCase("-r") && args[1].equalsIgnoreCase("chatlog")) {
						
						chatLog.delete();
						new File(plFolder).mkdirs();
						chatLog = new File(plFolder + File.separator + "ChatLog.txt");
						try {
							if(!chatLog.exists()) {
								chatLog.createNewFile();
								p.sendMessage(ChatColor.AQUA + "[TidyChat]: " + ChatColor.GREEN + "ChatLog dosyasý sýfýrlandý.");
							}
						} catch (Exception exception) {
							getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[TidyChat]: " + ChatColor.RED + "ChatLog dosyasi sifirlanirken bir hata meydana geldi!");
						}
						
					}
					
					if(args[0].equalsIgnoreCase("-s")) {
						
						int count = 0;
						int count_scan = 0;
						
						while(count < 500) {
							Bukkit.broadcastMessage("");
							count++;
						}
						
						while(count_scan < 1) {
							
							if(count == 500 || count > 500 || count >= 500) {
								Bukkit.broadcastMessage(ChatColor.GREEN + "Sohbet " + ChatColor.RED + sender.getName() + ChatColor.GREEN + " Tarafýndan Temizlendi!");
								count = 0;
								count_scan = 2;
							}
							
						}
						
					}
					
					// Mute Sistemleri
					
					
					
					// ---------------
					
				}
				
			} else 
				
				p.sendMessage(ChatColor.AQUA + "[TidyChat]: " + ChatColor.RED + "Bu komut için yetkiniz bulunmamakta.");
			}
			

		return false;
	}
	
	@EventHandler
	public void oyuncuSohbet(AsyncPlayerChatEvent e) {
		
		Player p = e.getPlayer();

		
		for(String word : e.getMessage().split(" ")) {
			
			for(String player : e.getPlayer().getName().split(" ")) {
				
				if(!getConfig().getStringList("bypassPlayers").contains(player)) {
					
					if(getConfig().getStringList("bannedWords").contains(word.toLowerCase())) {
						
					
							
							p.sendMessage(ChatColor.AQUA + "[TidyChat]: " + ChatColor.RED + "Yasaklanmýþ Bir Kelime Kullandýn!");
							e.setCancelled(true);
							
							if(getConfig().getBoolean("writeLogFiles")) {
								
								String pName = e.getPlayer().getDisplayName();
								String bannedWord = e.getMessage().toLowerCase();
								Date time = Calendar.getInstance().getTime();
								
								try {
									
									FileWriter fw = new FileWriter(chatLog, true);
									BufferedWriter bw = new BufferedWriter(fw);
									bw.write("\n============================================\n"
											+ "Oyuncu Adi: " +  pName + "\n"
											+ "Yasaklanan(Soylenen) Kelime: " + "-" + bannedWord + "-" + "\n"
											+ "Zaman: " + time.toString() + "\n"
											+ "============================================");
									
									fw.flush();
									bw.close();
									
									
								} catch(Exception exception) {
									exception.printStackTrace();
								}
								
							}
							
							
							if(getConfig().getBoolean("capsLockProtect")) {
								e.setMessage(e.getMessage().toLowerCase());
							}
							
						
						if(getConfig().getBoolean("sendConsoleMessages")) {
							getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "\n\n[GameUx/TidyChat]: " + ChatColor.RED + p.getDisplayName() + ChatColor.GREEN + " Yasaklanmis Bir Kelime Kullandi! \n" + ChatColor.YELLOW + "Kelime: " + ChatColor.RED + e.getMessage().toLowerCase() + "\n\n");
						}

						
							
					}
					
				}
				
			}
			
		}
		
	}
	
	
}
