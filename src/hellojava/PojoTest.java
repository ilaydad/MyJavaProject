package hellojava;

public class PojoTest {
private String name;
private int age;
public PojoTest(String name,int age) {
	this.name = name;
	this.age = age;
}
public String getName() {
	return name;
}
public int getAge() {
	return age;
}
public void printInfo() {
	System.out.println ("Name: " + getName() + " , Age: " + getAge() );
	}
public static void main (String [] args ) {
	PojoTest person = new PojoTest("İlayda" ,22);
	person.printInfo();
}
	}

