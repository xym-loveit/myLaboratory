package com.xym.laboratory.hashcode;

/**
 * desc
 *
 * @author xym
 */
public class People {
    private String name;
    private int age;

    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return name.hashCode() * 37 + age;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        //return this.name.equals(((People) obj).name) && this.age == ((People) obj).age;
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        People p = (People) obj;
        return this.name.equals(p.name)
                && this.age == p.age;
    }
}