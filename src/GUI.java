import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
//import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame
{
    JButton[][] cellsGrid;
    JPanel gridPanel;

    String[] ballsImg = {
        "redBall.png",
        "blueBall.png",
    };

    JButton cell;

    private final int WIDTH = 700;
    private final int HEIGHT = 700;
    private final String ICON_PATH = "/src/img/";  

    public GUI() throws IOException
    {
        //  Windows Content
        newView();

        // Windows Specs
        setSize(WIDTH, HEIGHT);
        setTitle("Jeu de la vie - Hugo GOYARD & Robin VUITTENEZ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDisplay(true);
    }

    public void newView() throws IOException
    {
        cellsGrid = new JButton[10][10];
        gridPanel = new JPanel(new GridLayout(10, 10));
        gridPanel.setBackground(Color.WHITE);

        makeGrid();
    }

    public void makeGrid() throws IOException
    {
        for (int i = 0; i < cellsGrid.length; i++) {
            for (int j = 0; j < cellsGrid[i].length; j++) {
                cell = new JButton();
                cell.setSize(WIDTH/10, HEIGHT/10);
                cell.setBorder(null);
                cell.setBackground(Color.WHITE);
                //cell.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
                setIcon(cell);
                gridPanel.add(cell);
                cellsGrid[i][j] = cell;
            }
        }
        add(gridPanel);
    }

    public void setIcon(JButton button) throws IOException
    {
        Image photo = ImageIO.read(new File(ballsImg[0]));
        Image image = photo.getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        button.setIcon(icon);
        validate();
    }

    public void setDisplay(Boolean value)
    {
        setVisible(value);
    }


    public static void main(String[] args) throws IOException
    {
        GUI gui = new GUI();
    }
}
