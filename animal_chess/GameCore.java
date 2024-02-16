package animal_chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameCore extends JPanel
{
    private JButton[][] btnChesses;    // 4x4 = 16 btnChesses
    private JLabel[][] states;
    private JTextField time1 = new JTextField("");
    private JTextField time2 = new JTextField("");

    private int[][] chesses;    // two indexs of this array represent the coordinate
    /*
     *-1 -> magma(kill any chess)
     * 0 -> elephant
     * 1 -> lion
     * 2 -> tiger
     * 3 -> leopard
     * 4 -> wolf
     * 5 -> dog
     * 6 -> cat
     * 7 -> rat
     * 8 -> nothing(no chess)
     */

    private int[][] teams;      
    /*
     *-1 -> None (magma or nothing)
     * 0 -> Blue
     * 1 -> Red
     */

    private boolean[][] alive;  //whether the chess is alive
    private boolean[][] clicked;//whether the card in each coordinate has been turned over
    private int pressX, pressY; //The coordinate of chess which has been pressed  
    private JFrame frame;       //GameUI's frame (JDialog uses this in super())

    private int attacker, defender;
    private int turn;
    private int peaceTurns;     //store the number of turns that no chess has been killed
    private boolean magma;      //whether the magma begins to spread
    private boolean wait;
    private boolean end;
    private Thread timer, t3;

    public GameCore(JFrame frame)
    {              
        this.frame = frame;
        initialize();      
        start();
    }

    public void initialize(){ //initialize field variables
        // Blue is on the offensive
        attacker = 0;
        defender = 1;
        turn = 0;
        peaceTurns = 0;
        magma = false;
        wait = false;
        end = false;
        pressX = -1;
        pressY = -1;
        chesses = new int [4][4];
        teams = new int [4][4];
        alive = new boolean[2][8];
        clicked = new boolean [4][4];
        states = new JLabel[2][8];
        //initialize each array 
        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                chesses[i][j] = 9;
                clicked[i][j] = false;
                teams[i][j] = -1;
            }        
        }        
        for(int i = 0; i<2; i++){
            for(int j = 0; j<8; j++){
                alive[i][j] = true;
            }
        }
        createChesses(); /*generate the initial position of chesses*/
    }

    public void createChesses(){//Generate the initial position of each chess       
        for(int team = 0; team < 2; team++){
            for(int index = 0; index < 8; index++){
                boolean quit = false;
                while(!quit){
                    Random random = new Random();
                    int x = random.nextInt(4);  
                    int y = random.nextInt(4);
                    if(chesses[x][y] == 9){
                        teams[x][y] = team;
                        chesses[x][y] = index;
                        quit = true;
                    }
                }
            }
        }
    }//(achieve)

    public void paintMagma(){
        int x;
        int y;
        int loop = 0;
        while(true){
            x = loop-1;
            y = loop;         
            while(x+1 < chesses.length - loop){
                if(chesses[x+1][y] != -1){                    
                    teams[x+1][y] = -1;
                    chesses[x+1][y] = -1;
                    paintChess(x+1,y);
                    return;
                }
                x++;
            }
            while(y+1 < chesses.length - loop){
                if(chesses[x][y+1] != -1){
                    teams[x][y+1] = -1;
                    chesses[x][y+1] = -1;
                    paintChess(x,y+1);
                    return;
                }
                y++;
            }
            while(x-1 >= 0 + loop){
                if(chesses[x-1][y] != -1){
                    teams[x-1][y] = -1;
                    chesses[x-1][y] = -1;
                    paintChess(x-1,y);
                    return;
                }
                x--;
            }
            while(y-1 >= 0 + loop){
                if(chesses[x][y-1] != -1){
                    teams[x][y-1] = -1;
                    chesses[x][y-1] = -1;
                    paintChess(x,y-1);
                    return;
                }
                y--;
            }
            loop++;  
        }        
    }

    public void start(){ /*start the new turn*/   
        turn++;

        if(magma && turn%4 == 2){ /*magma spreads a button per 4 turns*/
            paintMagma();
            updateAlive();
            if(countAliveChesses(1) == 0 && !end){
                endGame(1);
            }
            else if(countAliveChesses(2) == 0 && !end){
                endGame(0);
            }
        }
        Thread t = new Thread(new Reminder(0));   
        timer = new Thread(new Timer());
        t.start();              
        timer.start();
    }

    public void end(){//end the turn
        wait = false;
        peaceTurns++;

        //reminder for starting magma
        if(peaceTurns >= 6 && turn >= 18 && peaceTurns < 10 && turn < 21){
            if((9-peaceTurns)<(20-turn)){
                wait = true;
                Thread t = new Thread(new Reminder(1));
                t.start();                    
            }
            else{
                wait = true;
                Thread t = new Thread(new Reminder(2));
                t.start();
            }
        }
        else if(peaceTurns >= 6 && turn < 18 && peaceTurns < 9){
            wait = true;
            Thread t = new Thread(new Reminder(1));
            t.start();
        }
        else if(turn >= 18 && turn < 21){
            wait = true;
            Thread t = new Thread(new Reminder(2));
            t.start();
        }            

        if(peaceTurns == 9 || turn == 19){
            magma = true;//start spreading magma
        }

        if(attacker == 0){//exchange attacker and defender
            attacker = 1;
            defender = 0;
        }
        else{
            attacker = 0;
            defender = 1;
        }
        start();
    }

    public void changeChessBackground(int team, int x, int y, boolean tool){//change the background color of chess
        if(!tool){
            if(team == 0){
                btnChesses[x][y].setBackground(Color.BLUE); //change background color to show the selection
            }
            else if(team == 1){
                btnChesses[x][y].setBackground(Color.RED);
            }
            else{
                btnChesses[x][y].setBackground(new Color(57,19,0)); //cancel the background
            }
        }
        else{   
            btnChesses[x][y].setBackground(Color.GREEN);
        }
        btnChesses[x][y].setContentAreaFilled(false);
        btnChesses[x][y].setOpaque(true); 
    }

    public void paintChess(int x, int y){    //paint chess when clicking card which hasn't been turned over
        int team = teams[x][y];
        int index = chesses[x][y];
        if(team != -1){
            btnChesses[x][y].setIcon(new ImageIcon("./Picture/Chess/" + team + "/" + index + ".png"));
        }
        else if(index == 8){
            btnChesses[x][y].setIcon(null);
        }
        else if(index == -1){
            btnChesses[x][y].setIcon(new ImageIcon("./Picture/Chess/Magma.png"));
        }
        clicked[x][y] = true;
    }

    public int compareChesses(int index1, int index2){
        if(index1 == 0 && index2 == 7){//two chesses are elephant and rat separately (special situation)
            return 2;
        }
        else if(index1 == 7 && index2 == 0){//two chesses are elephant and rat separately (special situation)
            return 1;
        }
        else if(index1 == index2){//two chesses are same (both of them will dead, except the final turn)
            if(countAliveChesses(0) == 2){ //final turn (only 2 chesses)
                return 1;
            }
            else{
                return 0;   //both of them perish together
            }
        }
        else{
            if(index1 > index2){    //chess2 is stronger
                return 2;
            }
            else{                   //chess1 is stronger
                return 1;
            }
        }
    }

    public int countAliveChesses(int mode){
        int count1 = 0; //count blue
        int count2 = 0; //count red
        for(int i = 0; i < alive[0].length; i++){
            if(alive[0][i]){
                count1 += 1;
            }
        }
        for(int i = 0; i < alive[1].length; i++){
            if(alive[1][i]){
                count2 += 1;
            }
        }
        switch(mode){
            case 0: return count1 + count2;
            case 1: return count1;
            case 2: return count2;
            default: return -1;
        }
    }

    public void updateAlive(){
        for(int i = 0; i<2; i++){
            for(int j = 0; j<8; j++){
                alive[i][j] = false;
            }
        }
        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                if(teams[i][j] != -1){
                    alive[teams[i][j]][chesses[i][j]] = true;
                }
            }        
        }    
        for(int i = 0; i<2; i++){
            for(int j = 0; j<8; j++){
                if(!alive[i][j]){
                    states[i][j].setIcon(new ImageIcon("./Picture/State/Team-1/" + j + ".png"));
                }
            }
        }
    }

    public void battle(int x1, int y1, int x2, int y2){  //coordindate of two chesses 
        peaceTurns = -1;
        int index1 = chesses[x1][y1];
        int index2 = chesses[x2][y2];
        int team1 = teams[x1][y1];
        int team2 = teams[x2][y2];
        teams[x1][y1] = -1;             //reset (x1,y1) position to nothing (-1)
        chesses[x1][y1] = 8;            //reset (x1,y1) position to nothing (8)
        paintChess(x1, y1);             //move chess  
        if(compareChesses(index1, index2) == 1){//first-hand player has stronger chess 
            teams[x2][y2] = team1;
            chesses[x2][y2] = index1;
            paintChess(x2, y2);
        }
        else if(compareChesses(index1, index2) == 2){//last-hand player has stronger chess
            teams[x2][y2] = team2;
            chesses[x2][y2] = index2;
            paintChess(x2, y2);
        }
        else{//same
            teams[x2][y2] = -1;
            chesses[x2][y2] = 8;
            paintChess(x2, y2);
        }   
        updateAlive();
        if(countAliveChesses(1) == 0){
            endGame(1);
        }
        else if(countAliveChesses(2) == 0){
            endGame(0);
        }
    }

    public void stopTimer(){
        timer.interrupt();
        if(attacker == 0){
            time1.setText("");
        }
        else{
            time2.setText("");
        }
    }

    public void disableChess(){//disable all chesses
        for(int i = 0; i < btnChesses.length; i++){
            for(int j = 0; j < btnChesses[0].length; j++){
                clicked[i][j] = true;
                teams[i][j] = -1;
            }
        }
    }

    public void endGame(int team){
        end = true;
        disableChess();
        String winner;
        if(team == 0){
            winner = "blue";
        }
        else{
            winner = "red";
        }
        JOptionPane.showMessageDialog(null, "winner is " + winner, "Game Result", JOptionPane.INFORMATION_MESSAGE);      
        frame.dispose();
        new MenuUI();
    }

    public JPanel paintButtons(){//paint 4x4 grid of buttons (called by GameUI)
        JPanel panel = new JPanel();
        panel.setBackground(new Color(57,19,0));
        panel.setLayout(new GridLayout(4,4));

        ButtonHandler bh = new ButtonHandler();                       

        btnChesses = new JButton[4][4];
        for(int i = 0; i < btnChesses.length; i++){
            for(int j = 0; j < btnChesses[0].length; j++){
                btnChesses[i][j] = new JButton("");
                btnChesses[i][j].setIcon(new ImageIcon("./Picture/Chess.png"));
                btnChesses[i][j].addActionListener(bh);
                panel.add(btnChesses[i][j], i, j);
            }
        }
        return panel;
    }

    public JTextField paintTimer(int index){//paint timer of P1 and P2 (called by GameUI)
        if(index == 1){            
            return time1;
        }
        else{           
            return time2;
        }        
    }

    public JPanel paintStates(int team){//paint states of P1 and P2 (called by GameUI)         
        JPanel panel = new JPanel();
        for(int i=0; i < states[0].length; i++){
            states[team][i] = new JLabel(new ImageIcon("./Picture/State/Team" + team + "/" + i + ".png"));
            states[team][i].setPreferredSize(new Dimension(45,45));   
            panel.add(states[team][i]);
        }
        panel.setBackground(new Color(57,19,0));
        return panel;
    }

    private class ButtonHandler implements ActionListener{
        public void actionPerformed(ActionEvent ae){
           try{
                for(int i = 0; i < btnChesses.length; i++){
                    for(int j = 0; j < btnChesses[0].length; j++){
                        if(ae.getSource() == btnChesses[i][j]){ 
                            if(clicked[i][j] == false){     //the chess hasn't been clicked (turned over)
                                stopTimer();
                                paintChess(i,j);              
                                if(pressX != -1){
                                    changeChessBackground(-1, pressX, pressY, false);
                                    if(Math.abs(pressX-i) + Math.abs(pressY-j) <= 1){  
                                        if(teams[i][j] != attacker){
                                            t3 = new Thread (new AnimateMove(pressX, pressY, i, j));
                                            t3.start();
                                        }
                                        pressX = -1;
                                        pressY = -1;                                    
                                    }
                                }
                                end();
                            }
                            else{   //the chess has been clicked
                                if(teams[i][j] == attacker){ 
                                    if(pressX == -1){       //no chess has been selected
                                        pressX = i; 
                                        pressY = j;
                                        changeChessBackground(attacker, i, j, false);
                                    }
                                    else{
                                        if(i == pressX && j == pressY){ //the same chess                             
                                            changeChessBackground(-1, pressX, pressY, false); //cancel the selection
                                            pressX = -1;
                                            pressY = -1;
                                        }
                                        else{
                                            changeChessBackground(-1, pressX, pressY, false); //cancel the selection
                                            pressX = i; 
                                            pressY = j;
                                            changeChessBackground(attacker, i, j, false);
                                        }
                                    }
                                }
                                else if(teams[i][j] != attacker){
                                    if(pressX != -1){
                                        if(Math.abs(pressX-i) + Math.abs(pressY-j) <= 1){
                                            stopTimer();
                                            changeChessBackground(-1, pressX, pressY, false);
                                            t3 = new Thread(new AnimateMove(pressX, pressY, i, j));
                                            t3.start();
                                            pressX = -1;
                                            pressY = -1;   
                                            end();
                                        }
                                    }
                                }
                            }    
                        }
                    }
                }
           }catch(Exception e){
               JOptionPane.showMessageDialog(null, "Unknown error G_A_M_E", "Error", JOptionPane.ERROR_MESSAGE);
           }
        }
    }

    private class AttackerReminder extends JDialog{//reminds the attacker of this turn
        private Container contents;
        private JTextField reminder;
        public AttackerReminder(){
            super(frame, "Reminder", false);
            this.setUndecorated(true);           
            contents = getContentPane(); 
            contents.setLayout(null);

            reminder = new JTextField("P" + (attacker + 1) + "'s Turn");
            if(attacker == 0){
                reminder.setForeground(Color.BLUE);
            }
            else{
                reminder.setForeground(Color.RED);
            }
            Font mf = new Font("TimesRoman", Font.BOLD, 25);
            reminder.setFont(mf);
            reminder.setBorder(null);
            reminder.setEditable(false);
            contents.add(reminder);
            reminder.setBounds(35,20,118,60);

            this.getContentPane().setBackground(Color.WHITE);
            setSize(180,100);
            this.setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);      
        }
    }

    private class NoBattleReminder extends JDialog{
        private Container contents;
        private JTextArea reminder;
        public NoBattleReminder(){
            super(frame, "Reminder", false);
            this.setUndecorated(true);           
            contents = getContentPane(); 
            contents.setLayout(null);
            if(8-peaceTurns > 0){
                reminder = new JTextArea("If no chess is killed in " + (8-peaceTurns) + "\nturns, magma will spread.");
            }
            else{
                reminder = new JTextArea("Magma will begin \nspreading next turn.");
            }

            Font mf = new Font("TimesRoman", Font.BOLD, 18);
            reminder.setFont(mf);
            reminder.setBorder(null);
            reminder.setEditable(false);
            contents.add(reminder);
            reminder.setBounds(10,30,203,80);

            this.getContentPane().setBackground(Color.WHITE);
            setSize(220,100);
            this.setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);      
        }
    }

    private class MagmaReminder extends JDialog{
        private Container contents;
        private JTextArea reminder;
        public MagmaReminder(){
            super(frame, "Reminder", false);
            this.setUndecorated(true);           
            contents = getContentPane(); 
            contents.setLayout(null);

            reminder = new JTextArea("Magma will spread in " + (22-turn) +" turns.");

            Font mf = new Font("TimesRoman", Font.BOLD, 18);
            reminder.setFont(mf);
            reminder.setBorder(null);
            reminder.setEditable(false);
            contents.add(reminder);
            reminder.setBounds(6,30,240,80);

            this.getContentPane().setBackground(Color.WHITE);
            setSize(247,80);
            this.setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);      
        }
    }

    private class Reminder implements Runnable{//A thread used to create in-game reminder and dispose it in given time
        private int task;
        /*
         * 0 -> AttackerReminder;
         * 1 -> MagmaReminder (due to no chess is killed in 10 turns);
         * 2 -> MagmaReminder (due to the number of turns is larger than 20);
         */
        public Reminder(int task){
            this.task = task;
        }

        public void run(){
            try{                    
                switch(task){
                    case 0:
                    if(wait){
                        Thread.sleep(1000);
                    }
                    AttackerReminder ar = new AttackerReminder();
                    Thread.sleep(600);
                    ar.dispose(); 
                    break;
                    case 1: NoBattleReminder nbr = new NoBattleReminder();
                    Thread.sleep(1000);
                    nbr.dispose();  
                    break;
                    case 2: MagmaReminder mr = new MagmaReminder();
                    Thread.sleep(1000);
                    mr.dispose();
                    break;
                    default:
                    break;
                }                                              
            }catch(InterruptedException e){}
        }
    }

    private class AnimateMove implements Runnable{
        private int x1, x2, y1, y2;
        public AnimateMove(int x1, int y1, int x2, int y2){
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public void run(){            
            try{                
                Thread.sleep(500);
                battle(x1, y1, x2, y2); //compare the intensity of two chesses (selected chess and the chess which is just clicked)
            }catch(InterruptedException e){}
        }
    }

    private class Timer implements Runnable{
        private int time;
        public Timer(){
            time = 30;
        }

        public void run(){
            try{            
                if(attacker == 0){
                    while(time >= 0){ 
                        time1.setText(time+"");                      
                        time--;
                        Thread.sleep(1200);                    
                    }
                }
                else{
                    while(time >= 0){        
                        time2.setText(time+"");
                        time--;
                        Thread.sleep(1200);                      
                    }
                }

                if(frame.isVisible()){//EXIT_ON_CLOSE can't stop this thread, and the timer always continuous works and send endGame result
                    endGame(defender);                    
                }
            }catch(InterruptedException e){}
        }
    }
}


