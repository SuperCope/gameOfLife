import javax.swing.*;
import java.awt.*;
//import java.awt.event.*;

public class GUI extends JFrame
{
    JButton[][] cellsGrid;
    JPanel gridPanel;

    String balls[] = {
        "redBall.png",
        "blueBall.png",
        "redBall.png"
    }
    
    JButton cell;

    private int width = 700;
    private int height = width;

    public GUI() 
    {
        //  Windows Content
        newView();

        // Windows Specs
        setSize(width, height);
        setTitle("Jeu de la vie - Hugo GOYARD & Robin VUITTENEZ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
        setDisplay(true);   
    }

    public void newView()
    {
        cellsGrid = new JButton[100][100];
        gridPanel = new JPanel(new GridLayout(100,100));
        gridPanel.setBackground(Color.WHITE);
        
        makeGrid();
    }

    public void makeGrid()
    {
        for (int i = 0; i < cellsGrid.length; i++) {
            for (int j = 0; j < cellsGrid[i].length; j++) {
                cell = new JButton(new ImageIcon(balls[0]));
                cell.setEnabled(false);
                cell.setBorder(null);
                cell.setBackground(Color.WHITE);
                cell.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
                cell.setPreferredSize(new Dimension(width/100, height/100));
                gridPanel.add(cell);
                cellsGrid[i][j] = cell;
            }
        }
        add(gridPanel);
    }

    public void setDisplay(Boolean value)
    {
        setVisible(value);
    }


    public static void main(String[] args)
    {
        GUI gui = new GUI();
    }
}
