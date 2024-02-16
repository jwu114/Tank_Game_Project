package mine_sweeper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
public class Game extends JPanel
{
    private JButton [][] squares;
    private Mines game;
    private boolean loopMode;
    private boolean first;
    private boolean press = false;
    private boolean end;
    private int time;

    private JLabel timeL;
    private JLabel remain;

    private Thread t1;

    public Game(int newSide, int newDifficulty){
        game = new Mines( newSide, newDifficulty );   
        first = true;
        loopMode = false;
        setUpGameGUI( );
    }

    public void setUpGame( int newSide, int newDifficulty )
    {
        game.setUpGame( newSide, newDifficulty );
        first = true;
        loopMode = false;
        setUpGameGUI( );
    }

    public String getRemain(){
        int remain = game.getFlagsNum();
        return remain + "";
    }

    public void end(){
        t1.stop();
        end = true;
    }

    public void setUpGameGUI( )
    {
        removeAll( ); // remove all components        
        setLayout( new GridLayout( game.getSide( ),
                game.getSide( ) ) );

        squares = new JButton[game.getSide( )][game.getSide( )];
        KeyHandler kh = new KeyHandler( );
        ButtonHandler bh = new ButtonHandler( );
        
        // for each button: generate button label,
        // instantiate button, add to container,
        // and register listener
        for ( int i = 0; i < game.getSide( ); i++ )
        {
            for ( int j = 0; j < game.getSide( ); j++ )
            {
                squares[i][j] = new JButton( "" );
                squares[i][j].setIcon(new ImageIcon(getClass().getResource("blank.png")));
                add( squares[i][j] );
                squares[i][j].addKeyListener( kh );                
                squares[i][j].addActionListener( bh );                               
            }
        }        

        setSize( 650, 650 );
        setVisible( true );
    }

    public JPanel getPanel(){
        JPanel p = new JPanel();                  

        timeL = new JLabel("Time: 0");   
        remain = new JLabel("Remained flags: " + getRemain());

        p.add(remain);
        p.add(timeL);

        return p;
    }

    public void restartTimeL(){
        timeL.setText("Time: 0");
    }

    public void restartRemain(){
        remain.setText("Remained flags: " + getRemain());
    }

    private void update( int i, int j )
    {   
        int num = game.countMines( i, j );
        game.setChoose( i, j );
        switch(num){
            case 0:squares[i][j].setIcon(new ImageIcon(getClass().getResource("null.png")));
            loopMode( i, j );
            break;
            case 1:squares[i][j].setIcon(new ImageIcon(getClass().getResource("1.png")));
            break;
            case 2:squares[i][j].setIcon(new ImageIcon(getClass().getResource("2.png")));
            break;
            case 3:squares[i][j].setIcon(new ImageIcon(getClass().getResource("3.png")));
            break;
            case 4:squares[i][j].setIcon(new ImageIcon(getClass().getResource("4.png")));
            break;
            case 5:squares[i][j].setIcon(new ImageIcon(getClass().getResource("5.png")));
            break; 
            case 6:squares[i][j].setIcon(new ImageIcon(getClass().getResource("6.png")));
            break;
            case 7:squares[i][j].setIcon(new ImageIcon(getClass().getResource("7.png")));
            break;
            case 8:squares[i][j].setIcon(new ImageIcon(getClass().getResource("8.png")));
            break;
        }  
        if(game.win()){
            win();
        }
    }

    public void loopMode( int x, int y ){  
        loopMode = true;
        while(loopMode){
            loopMode = false;
            for(int i = 0; i < game.getSide( ); i++){
                for(int j = 0; j < game.getSide( ); j++){
                    if(game.countMines( i, j ) == 0 && game.getChoose( i, j )){
                        for(int n = -1; n <= 1; n++){
                            if(x+n < game.getSide( ) && x+n >= 0){
                                for(int m = -1; m <= 1; m++){
                                    if(m+y < game.getSide( ) && m+y >= 0 )
                                        if(!game.getChoose( n+x, m+y )){
                                            loopMode = true;
                                            update( n+x, m+y );
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void lose( int targetI, int targetJ ){
        t1.stop();
        end = true;
        JOptionPane.showMessageDialog( Game.this,
            "You lose" );      
        game.disable();
        squares[targetI][targetJ].setIcon(new ImageIcon(getClass().getResource("explode.png")));
        for( int i = 0; i < game.getSide( ); i++ )
        {
            for( int j = 0; j < game.getSide( ); j++ )
            {                         
                if ( !(i == targetI && j == targetJ) && game.checkMine( i , j ) && !game.getFlag( i, j )){
                    squares[i][j].setIcon(new ImageIcon(getClass().getResource("mine.png")));
                }
                else if ( !game.checkMine( i , j ) && game.getFlag( i, j )){
                    squares[i][j].setIcon(new ImageIcon(getClass().getResource("wrong mine.png")));
                }
            } 
        } 
        
    } 

    public void win(){
        t1.stop();
        end = true;
        JOptionPane.showMessageDialog( Game.this,
            "You win!!!" );        
        game.disable();
    }

    
    private class ButtonHandler implements ActionListener
    {       
        public void actionPerformed( ActionEvent ae )
        {
            for( int i = 0; i < game.getSide( ); i++ )
            {
                for( int j = 0; j < game.getSide( ); j++ )
                {
                    if ( ae.getSource( ) == squares[i][j] && !press && !game.getChoose( i, j ))
                    {       
                        if (first){   
                            t1 = new Thread(new Time());
                            t1.start(); 
                            end = false;
                            first = false;
                            if(game.checkMine(i,j)){
                                game.protectFirst(i, j);
                            }
                        }
                        if ( !game.checkMine( i , j ) ){
                            update( i, j );
                        }
                        else
                            lose( i, j );                                                
                    } // end if 
                    else if ( ae.getSource( ) == squares[i][j] && press && !game.getChoose( i, j )){
                        if(game.getFlag(i,j)){
                            squares[i][j].setIcon(new ImageIcon(getClass().getResource("blank.png")));
                            game.setFlag(i,j,false);
                            game.setFlagsNum(game.getFlagsNum()+1);
                            remain.setText("Remained flags: " + getRemain());
                        }
                        else if(game.getFlagsNum()>0){
                            squares[i][j].setIcon(new ImageIcon(getClass().getResource("flag.png")));
                            game.setFlag(i,j,true);
                            game.setFlagsNum(game.getFlagsNum()-1);
                            remain.setText("Remained flags: " + getRemain());
                        }
                    }
                } // end inner for loop
            } // outer for loop
        } // end actionPerformed method                          
    } // en Game class

    private class KeyHandler implements KeyListener{
        public void keyReleased( KeyEvent ke ){
            if (ke.getKeyCode() == 17){
                press = false;
            }
        }

        public void keyPressed( KeyEvent ke ){
            if (ke.getKeyCode() == 17){
                press = true;
            }
        }

        public void keyTyped( KeyEvent ke ){

        }
    }    

    private class Time implements Runnable{
        public Time(){
            time = 0;
        }

        public void run(){
            while(!end){
                time++;
                timeL.setText("Time: " + time);
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
