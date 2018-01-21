package net.vowed.core.util.strings;

/**
 * Created by JPaul on 1/31/2016.
 */
public class HandleErrorException extends Exception
{

    public HandleErrorException()
    {
        super("Incorrectly handled error message");
    }

    public HandleErrorException(String message)
    {
        super(message);
    }

    @Override
    public String getMessage()
    {
        return super.getMessage();
    }
}
