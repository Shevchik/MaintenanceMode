/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package mmode.bungee;

import java.util.Map.Entry;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Commands extends Command {

	private Config config;

	public Commands(Config config) {
        super("mmode", "mmode.admin");
		this.config = config;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length >= 1 && args[0].equalsIgnoreCase("on")) {
			if (args.length == 1) {
				sender.sendMessage(ColorParser.parseColor("&4Please specify a server name"));
				return;
			}
			String servername = args[1];
			if (!ProxyServer.getInstance().getServers().containsKey(servername)) {
				sender.sendMessage(ColorParser.parseColor("&4Can't find server with name "+args[1]));
				return;
			}
			String serveraddress = getServerForcedHost(servername);
			if (serveraddress == null) {
				sender.sendMessage(ColorParser.parseColor("&4Can't find forced host for server with name "+args[1]));
				return;
			}
			config.maintenanceaddressset.add(serveraddress);
			config.saveConfig();
			if (config.kickOnEnable) {
				for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
					if (p.getPendingConnection().getVirtualHost().getHostString().toLowerCase().equals(serveraddress) && !p.hasPermission("mmode.join")) {
						p.disconnect(ColorParser.parseColor(config.kickMessage));
					}
				}
			}
			sender.sendMessage(ColorParser.parseColor("&9Maintenance mode enabled on server "+servername));
		} else if (args.length >= 1 && args[0].equalsIgnoreCase("off")) {
			if (args.length == 1) {
				sender.sendMessage(ColorParser.parseColor("&4Please specify a server name"));
				return;
			}
			String servername = args[1];
			if (!ProxyServer.getInstance().getServers().containsKey(servername)) {
				sender.sendMessage(ColorParser.parseColor("&4Can't find server with name "+args[1]));
				return;
			}
			String serveraddress = getServerForcedHost(servername);
			if (serveraddress == null) {
				sender.sendMessage(ColorParser.parseColor("&4Can't find forced host for server with name "+args[1]));
				return;
			}
			if (config.maintenanceaddressset.contains(serveraddress)) {
				config.maintenanceaddressset.remove(serveraddress);
				config.saveConfig();
				sender.sendMessage(ColorParser.parseColor("&9Maintenance mode disabled on server "+servername));
			} else {
				sender.sendMessage(ColorParser.parseColor("&4Maintenance mode wasn't enabled on server "+servername));
			}
		} else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			config.loadConfig();
			sender.sendMessage(ColorParser.parseColor("&9Config reloaded"));
		}
	}

	private String getServerForcedHost(String servername) {
		for (ListenerInfo lst : BungeeCord.getInstance().config.getListeners()) {
			for (Entry<String, String> entry : lst.getForcedHosts().entrySet()) {
				if (entry.getValue().equalsIgnoreCase(servername)) {
					return entry.getKey().toLowerCase();
				}
			}
		}
		return null;
	}

}
