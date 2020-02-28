package sample;

import java.util.UUID;

public class Employee implements Worker
{
    public String firstName;
    public String lastName;
    public boolean isActive;
    public UUID id;

    @Override
    public String toString(){
        return (this.firstName + " " + this.lastName);
    }

    @Override
    public void hire()
    {
        this.isActive = true;
    }
}
