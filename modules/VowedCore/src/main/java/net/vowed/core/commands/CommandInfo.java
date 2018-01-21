package net.vowed.core.commands;

/**
 * Created by JPaul on 7/11/2016.
 */
public class CommandInfo
{
    String root;
    String subRoot;
    String initializer;
    String[] args;
    Object[] methodArgs;
    boolean nested = false;

    public CommandInfo(String root, String subRoot, String initializer, String[] args, Object... methodArgs)
    {
        this.root = root;
        this.subRoot = subRoot;
        this.initializer = initializer;
        this.args = args;
        this.methodArgs = methodArgs;
    }
}
