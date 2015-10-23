import java.lang.reflect.Method;
import java.lang.reflect.*;
import java.lang.Class;
import java.util.Arrays;
import java.util.ArrayList;

public class Inspector
{

	public ArrayList<Class> visited = new ArrayList<Class>();
	
	public void inspect(Object obj, boolean recursive)
	{
		try
		{
			Class objectClass = obj.getClass();
			if(objectClass.isArray())
			{
				objectClass = objectClass.getComponentType();
			}
			printClassName(objectClass);
			
			printSuperClass(objectClass, recursive);
			
			listInterfaces(objectClass);
			
			listFields(objectClass, obj);

			listConstructors(objectClass);

			showMethods(objectClass);
		}
		catch(Exception e)
		{
			System.out.println("");
			e.printStackTrace();
		}
	}

	public void printClassName(Class objectClass)
	{
		System.out.println("-= Class Name: " + objectClass.getName());
	}
	
	public void printSuperClass(Class objectClass, boolean recursive)
	{
		Class superClass = objectClass.getSuperclass();
		System.out.println("-= SuperClass Name: " + superClass.getName());
		if (!superClass.getName().equals("java.lang.Object"))
		{
			System.out.println("\n--------------------SuperClass of " + objectClass.getName() + "--------------------\n"); 
			if(Modifier.isAbstract(superClass.getModifiers()))
			{
				System.out.println("The SuperClass " + superClass.getName() + " is Abstract,\n It's Methods are included inside of " + objectClass.getName() + "'s Methods");
				System.out.println(superClass.getName() + "'s Interfaces: " + Arrays.asList(superClass.getInterfaces()));
				if(!superClass.getSuperclass().getName().equals("java.lang.Object"))
				{
					System.out.println("\n----- SuperClass of the Abstract SuperClass " + superClass.getName() + " -----\n");
					inspectSuperClass(superClass.getSuperclass(), recursive);
					System.out.println("\n----- End of SuperClass of the Abstract Class -----\n");
				}
			}
			else
			{
				inspectSuperClass(superClass, recursive);
			}
			System.out.println("\n--------------------End of SuperClass--------------------\n");
		}
	}
	
	public void listInterfaces(Class objectClass)
	{
		Class[] ifList = objectClass.getInterfaces();
		if(ifList.length > 0)
			System.out.println("-= Interfaces: " + Arrays.asList(ifList));
		else
			System.out.println("-= Interfaces: NONE");
	}

	public void listFields(Class objectClass, Object obj)
	{
		try
		{
			Field[] myFields = objectClass.getDeclaredFields();
			System.out.println("-= Fields: ");
			if(myFields.length == 0)
				System.out.println("\t::No Fields Found::");
			for(int f = 0; f < myFields.length ; f++)
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void listConstructors(Class objectClass)
	{
		Constructor[] constructors = objectClass.getDeclaredConstructors();
		System.out.println("-= Constructors: ");
		for (int c = 0; c < constructors.length; c++)
		{
			Class[] constructParams = constructors[c].getParameterTypes();
			System.out.println("\tConstructor["+c+"] Parameters: " + listTypes(constructParams));
			System.out.println("\tConstructor["+c+"] Modifiers: " + listModifiers(constructors[c].getModifiers()));
		}
	}

	public void showMethods(Class objectClass)
	{
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

	public void inspectSuperClass(Class superClass, boolean recursive)
	{
		try
		{
			Object superSuperClass = superClass.newInstance();
			inspect(superSuperClass,recursive);
		}
		catch (Exception e)
		{
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
