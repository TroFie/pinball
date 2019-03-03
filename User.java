package sample;

import java.io.Serializable;
/**
 * Creates and serializes the user object
 * @param null
 * @return null
 * @author Andreas
*/
public class User implements Serializable{
    String name;
    Double score;

    User(){}
    /**
     * Initializing the user object
     * @param name for the user
     * @return null
     * @author Andreas
    */
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
