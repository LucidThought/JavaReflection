import java.lang.reflect.Method;
import java.lang.reflect.*;
import java.lang.Class;
import java.util.Arrays;

public class Inspector
{
	public Class objectClass = null;
	
	public void inspect(Object obj, boolean recursive)
	{
		try
		{
			objectClass = obj.getClass();
			System.out.println("-= Class Name: " + objectClass.getName());

			Class superClass = objectClass.getSuperclass();
			System.out.println("-= SuperClass Name: " + superClass.getName());
			if (!superClass.getName().equals("java.lang.Object"))
			{
				System.out.println("\n--------------------SuperClass of " + objectClass.getName() + "--------------------\n"); 

				System.out.println("\n--------------------End of SuperClass --------------------\n");
			}

			Class[] ifList = objectClass.getInterfaces();
			System.out.println("-= Interfaces: " + Arrays.asList(ifList));
			
			Field[] myFields = objectClass.getDeclaredFields();
			System.out.println("-= Fields: ");
			for(int f = 0; f < myFields.length; f++)
			{
				if (!myFields[f].isAccessible())
					myFields[f].setAccessible(true);
				System.out.println("\t" + Modifier.toString(myFields[f].getModifiers()) + " " + myFields[f].getType().getSimpleName() + " " + myFields[f].getName());
				if(!myFields[f].getType().isPrimitive())
				{
					System.out.println("\t\tHash Code: " + myFields[f].hashCode());
				}
				else
				{
					System.out.println("\t\tValue: " + myFields[f].get(obj));
				}
			}

			Constructor[] constructors = objectClass.getDeclaredConstructors();
			System.out.println("-= Constructors: ");
			for (int c = 0; c < constructors.length; c++)
			{
				Class[] constructParams = constructors[c].getParameterTypes();
				System.out.println("\tConstructor["+c+"] Parameters: " + listTypes(constructParams));
				System.out.println("\tConstructor["+c+"] Modifiers: " + listModifiers(constructors[c].getModifiers()));
			}

			Method[] myMethods = objectClass.getMethods();
			System.out.println("-= Methods: ");
			Class returnType;
			for(int i=0; i<myMethods.length; i++)
			{
				System.out.println("\t" + myMethods[i].getName() + ":");
				Class[] exceptionTypes = myMethods[i].getExceptionTypes();
				System.out.println("\t\tException Types: " + listTypes(exceptionTypes));
				Class[] parameterTypes = myMethods[i].getParameterTypes();
				System.out.println("\t\tParameter Types: " + listTypes(parameterTypes));
				returnType = myMethods[i].getReturnType();
				System.out.println("\t\tReturn Type: " + returnType.getName());
				System.out.print("\t\tModifiers: ");
				System.out.println(listModifiers(myMethods[i].getModifiers()));
			}
			
		}
		catch(Exception e)
		{
			System.out.println("");
			e.printStackTrace();
		}
		
		
	}
	
	public String listModifiers(int modNum)
	{
		return Modifier.toString(modNum);
	}

	public String listTypes(Class[] types)
	{
		String list = "";
		if (types.length > 0) 
		{
			for(int i = 0; i < types.length; i++)
			{
				if (i > 0) list += ", ";
				list = list + types[i].getName();
			}
		}
		else
		{
			list = "NULL";
		}
		return list;
	}

}
