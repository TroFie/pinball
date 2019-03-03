package sample;

import java.io.Serializable;

public class User implements Serializable{
    String name;
    Double score;

    User(){
    
    }
    
    User(String name, Double score){
        setName(name);
        setScore(score);
        
    }
    
    public double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    

    @Override
    public String toString() {
        return "Student{" + ", name=" + name + ", score=" + score + '}';
    }
    
    
}
