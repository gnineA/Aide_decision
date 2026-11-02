package modelling;
import java.util.*;

public class Variable {
    protected String name;
    protected Set<Object> domain;

    public Variable(String name , Set<Object> domain){
        this.name = name;
        this.domain = domain;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Variable)) return false;
        Variable autre = (Variable) o ;
        return name.equals(autre.name);
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    public String getName(){
        return this.name;
    }

    public Set<Object> getDomain(){
        return this.domain;
    }
    @Override
    public String toString(){
        return name;
    }

}

