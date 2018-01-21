package net.vowed.wir.generation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.vowed.api.plugin.Vowed;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by JPaul on 9/28/2016.
 */
public class WidgetContainer
{
    private Map<Widget.Size, List<Widget>> widgets = Maps.newHashMap();

    public WidgetContainer()
    {
        for (Widget.Size size : Widget.Size.values())
        {
            widgets.put(size, Lists.newArrayList());
        }

        registerWidgets();
    }

    public void registerWidgets()
    {
        List<File> schematics = Lists.newArrayList();

        getFiles(Vowed.getPlugin().getDataFolder() + "\\WiR\\components", schematics);

        for (File schematic : schematics)
        {
            String path = schematic.getPath().toLowerCase();

            Widget widget = new Widget(schematic);
            Widget.Size size = null;

            if (path.contains("tiny"))
            {
                size = Widget.Size.TINY;
            }
            else if (path.contains("small"))
            {
                size = Widget.Size.SMALL;
            }
            else if (path.contains("medium"))
            {
                size = Widget.Size.MEDIUM;
            }
            else if (path.contains("big"))
            {
                size = Widget.Size.BIG;
            }
            else if (path.contains("huge"))
            {
                size = Widget.Size.HUGE;
            }

            widgets.get(size).add(widget);
        }
    }

    public void getFiles(String directoryName, List<File> files)
    {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList)
        {
            if (file.isFile())
            {
                files.add(file);
            }
            else if (file.isDirectory())
            {
                getFiles(file.getAbsolutePath(), files);
            }
        }
    }

    public List<Widget> getWidgets(Widget.Size size)
    {
        return widgets.get(size);
    }

    public List<Widget> getTinyWidgets()
    {
        return widgets.get(Widget.Size.TINY);
    }

    public List<Widget> getSmallWidgets()
    {
        return widgets.get(Widget.Size.SMALL);
    }

    public List<Widget> getMediumWidgets()
    {
        return widgets.get(Widget.Size.MEDIUM);
    }

    public List<Widget> getBigWidgets()
    {
        return widgets.get(Widget.Size.BIG);
    }

    public List<Widget> getHugeWidgets()
    {
        return widgets.get(Widget.Size.HUGE);
    }
}
