package com.aranai.craftyhelper;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Prospector for Bukkit
 *
 * @author Timberjaw
 * 
 */
public class CraftyHelper extends JavaPlugin {

	private Logger log;
	private HelperServer hs;
	private Server server;
	private PerformanceMonitor pf;

	public CraftyHelper()
	{
		super();
	}

	@Override
	public void onDisable() {
	    hs.stop();
	}

	@Override
	public void onEnable() {
		log = Logger.getLogger("Minecraft");
		
		// Enable message
        PluginDescriptionFile pdfFile = this.getDescription();
		log.info("[CraftyHelper] version [" + pdfFile.getVersion() + "] loaded" );
		
		// Get Bukkit server
		server = Bukkit.getServer();
		
		// Start helper server
		hs = new HelperServer(this);
		
		// Start performance monitor
		pf = new PerformanceMonitor();
		
		Thread thread = new Thread(hs, "HelperServer");
        thread.start();
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		return true;
	}
	
	protected String cmdGetPlayerList() {
	    StringBuffer sb = new StringBuffer();
	    
	    Player[] players = Bukkit.getOnlinePlayers();
	    if(players.length > 0)
	    {
    	    sb.append(players[0].getName());
    	    
    	    for(int i = 1; i < players.length; i++)
    	    {
    	        sb.append(":");
    	        sb.append(players[i].getName());
    	    }
	    }
	    
	    return sb.toString();
	}
	
	protected String cmdGetVersion() {
	    String cbVer = "Unknown";
        String cbName = "Unknown";
        cbVer = server.getVersion();
        cbName = server.getWorlds().get(0).getName();
	    return "CraftBukkit Version: "+cbVer+" | World: "+cbName;
	}
	
	protected String cmdGetPlayerIp(String name) {
	    Player p = server.getPlayer(name);
	    
	    if(p != null)
        {
            return "Player "+name+" has IP: "+p.getAddress().toString();
        }
	    
        return "Could not find player "+name;
	}
	
	protected String cmdGetPlayerPos(String name) {
        Player p = server.getPlayer(name);
        
        if(p != null)
        {
            return p.getLocation().getWorld().getName()+","+p.getLocation().getX()+","+p.getLocation().getY()+","+p.getLocation().getZ();
        }
        
        return "?,?,?";
    }
	
	protected String cmdGetPerfStats() {
	 // Get RAM usage
        String mem = Long.toString(PerformanceMonitor.memoryUsed()/1024/1024);
        String memMax = Long.toString(PerformanceMonitor.memoryAvailable()/1024/1024);
        
        // Get thread count
        int threads = PerformanceMonitor.threadsUsed();
        
        // Get CPU usage
        String cpu = Double.toString(pf.getCpuUsage());
        
        return mem + ":" + memMax + ":" + cpu + ":" + threads;
	}
	
}
