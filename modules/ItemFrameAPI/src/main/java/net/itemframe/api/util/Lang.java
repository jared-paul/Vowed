package net.itemframe.api.util;

public enum Lang {
    PLUGIN_ENABLED("Plugin enabled!"),
    PLUGIN_DISABLED("Plugin disabled!"),
    NO_PLAYER("&4You are not a player!"),
    NO_PERMISSION("&4No permission!"),
    ALREADY_SELECTION("&4You have already a selection!"),
    CLICK_FRAME("&3Please click on the frame!"),
    CLICK_MULTIFRAME("&3Please click on the top left Item net.itemframe.api.utilitemframe.api.Frame!"),
    GETTING_MODE_ENABLED("&2Getting-Mode enabled!"),
    GETTING_MODE_DISABLED("&2Getting-Mode disabled!"),
    CREATING_CANCELLED("&3Creating cancelled!"),
    PLUGIN_RELOAD("&2Plugin reloaded!"),
    FRAME_SET("&2Frame with ID &7#%id&2 created!"),
    MULTIFRAME_SET("&2%amount frames created!"),
    NO_FRAMEPICTURE("&4This isn't a frame picture!"),
    GET_URL("&3Frame infos:\n&fID: &7#%id\n&fCurrent entity id: &7%entity\n&fURL: &7%url"),
    NO_PICTURE("&4Unknown picture format: %url"),
    PREFIX("&7[&6FramePicture&7] &f"),
    NOT_ENOUGH_MONEY("&4You haven't enough Money!"),
    PLEASE_WAIT("&7Please wait ..."),
    FRAME_REMOVED("&3Frame removed!"),
    ALREADY_FRAME("&4This is already a frame picture!"),
    DOWNLOAD_ERROR("&4An error occurred while downloading the picture!");

    private final String value;
    public static YamlConfiguration config = null;
    public static File configFile = new File("plugins/FramePicture/messages.yml");

    private Lang(final String value) {
        this.value = value;
    }

    public String getText() {
        String value = this.getValue();
        if (config != null && config.contains(this.name()))
        {
            value = config.getString(this.name());
        }
        value = ChatColor.translateAlternateColorCodes('&', value);
        return value;
    }

    public String getValue() {
        return this.value;
    }

    public static void load() {
        if (!configFile.exists()) createConfig();
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void createConfig() {
        YamlConfiguration newConfig = new YamlConfiguration();
        newConfig.options().header("The Messages from FramePicture.");
        newConfig.options().copyHeader(true);
        for (Lang lang : Lang.values())
        {
            String name = lang.name();
            String value = lang.getValue();
            newConfig.set(name, value);
        }
        try {
            newConfig.save(configFile);
        } catch (Exception e) {
            FramePicturePlugin.log.log(Level.WARNING, "Error while save the messages.yml: " + e.getMessage());
        }
    }

}
