package mine_sweeper;
import java.util.*;
public class Mines
{
    private int side;
    private int dif;
    private boolean[][] mines;
    private boolean[][] flags;
    private boolean[][] choose;
    private int numFlags;
    private int numMines;

    public Mines(int newSide, int newDifficulty){
        setUpGame( newSide, newDifficulty );
    }

    public int getSide( )
    {
        return side;
    }
    
    public boolean getChoose(int x, int y){
        return choose[x][y];
    }
    
    public void setChoose(int x, int y){
        choose[x][y] = true;
    }
    
    public void setMine(int x, int y, boolean mine){
        mines[x][y] = mine;
    }
    
    public void setFlag(int x, int y, boolean flag){
        flags[x][y] = flag;
    }
    
    public boolean getFlag(int x, int y){
        return flags[x][y];
    }
    
    public void setFlagsNum(int newNum){
        numFlags = newNum;
    }
    
    public int getFlagsNum(){
        return numFlags;
    }
    
    public int getMinesNum(){
        return numMines;
    }
    
    public void setUpGame(int newSide, int newDifficulty){
        if ( side > 0 )
            side = newSide;
        else
            side = 15;
        numMines = 0;
        mines = new boolean[side][side];
        choose = new boolean[side][side];
        flags = new boolean[side][side];
        for ( int i = 0; i < side; i++ )
        {
            for ( int j = 0; j < side; j++ )
            {
                Random random = new Random();
                int num = random.nextInt(10);
                if(num == 0){
                    numMines++;
                    mines[i][j] = true;
                }
                else{
                    mines[i][j] = false;
                }
                choose[i][j] = false;
            }
        }
        numFlags = numMines;
    }
    
    public void protectFirst(int x, int y){
        setMine(x,y,false);
        boolean changed = false;
        while(!changed){
            Random random = new Random();
            int i = random.nextInt(side);
            int j = random.nextInt(side);
            if((i != x || j != y)&&!mines[i][j]){
                setMine(i,j,true);
                changed = true;
            }
        }
    }

    public boolean checkMine(int x, int y){
        boolean mine = false;
        if(mines[x][y]){
            mine = true;
        }
        return mine;
    }

    public int countMines(int x, int y){
        int count = 0;
        for ( int i = -1; i <= 1; i++ )
        {   
            if(x+i < side && x+i >= 0){
                for ( int j = -1; j <= 1; j++ )
                {
                    if(y+j < side && y+j >= 0){
                        if(mines[x+i][y+j]){
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }
    
    
    public boolean win(){
        boolean win = true;
        for ( int i = 0; i < side; i++ )
        {
            for ( int j = 0; j < side; j++ )
            {
                if(!choose[i][j] && !mines[i][j]){
                    win = false;
                }
            }
        }
        return win;
    }
    
    public void disable(){
        for ( int i = 0; i < side; i++ )
        {
            for ( int j = 0; j < side; j++ )
            {
                setChoose(i, j);
            }
        }
    }
}
