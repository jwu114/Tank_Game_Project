package mine_sweeper;
import java.io.Serializable;

public class Score implements Serializable
{
    private int mines;
    private int time;
    private int score;
    private String name;
    
    public Score(int mines, int time){
        this.mines = mines;
        this.time = time;
        this.name = "";
        calScore();
    }
    
    public int getMines(){
        return mines;
    }
    
    public int getTime(){
        return time;
    }
    
    public double getScore(){
        return score;
    }
    
    public String getName(){
        return name;
    }
    
    public void setMines(int newMines){
        mines = newMines;
    }
    
    public void setTime(int newTime){
        time = newTime;
    }
    
    public void setScore(int newScore){
        score = newScore;
    }
    
    public void setName(String newName){
        name = newName;
    } 
    
    public void calScore(){
        score = 1000 * mines/time;
    }
}
