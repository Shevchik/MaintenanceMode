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

import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

	private Config config;

	public JoinListener(Config config) {
		this.config = config;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPostLogin(ServerConnectEvent event) {
		if (config.maintenanceset.contains(event.getTarget().getName())) {
			event.setCancelled(true);
			event.getPlayer().disconnect(ColorParser.parseColor(config.kickMessage));
		}
	}

}