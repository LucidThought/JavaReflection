import java.lang.reflect.Method;
import java.lang.Class;

public class Inspector
{
	public Class objectClass = null;
	
	public void inspect(Object obj, boolean recursive)
	{
		try
		{
			objectClass = Class.forName(obj);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
