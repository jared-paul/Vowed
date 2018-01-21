package net.vowed.player.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * Created by JPaul on 9/3/2016.
 */
public class OnWeather implements Listener
{
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent changeEvent)
    {
        changeEvent.setCancelled(true);
        changeEvent.getWorld().setWeatherDuration(0);
    }
}
