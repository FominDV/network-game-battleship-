package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.OnlineGameWindow;
import ru.fomin.battleship.client.gui.PreparingForGameFrame;

import javax.swing.*;
import java.awt.*;

public class Cell extends JButton {
    /*Status:
     * 1-unknown cell
     * 2-damage
     * 3-dead
     * 4-preparing for posting ships
     * 5-known cell free
     * 6-known cell with ship*/
    static String delimiter=MapBuilder.delimiter;
    private boolean isActive;
    private int status;
    private final int X;
    private final int Y;
    private PreparingForGameFrame preparingForGameFrame;
    private OnlineGameWindow onlineGameWindow;

    public Cell(int status, PreparingForGameFrame preparingForGameFrame, int x, int y) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setMargin(new Insets(0, 0, 0, 0));
        this.status = status;
        this.preparingForGameFrame = preparingForGameFrame;
        X = x;
        Y = y;
        setImage(status);
        addActionListener(e -> {
            actionClick();
        });
    }

    public Cell(int status, OnlineGameWindow onlineGameWindow, int x, int y, boolean isActive) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setMargin(new Insets(0, 0, 0, 0));
        this.status = status;
        this.onlineGameWindow = onlineGameWindow;
        X = x;
        Y = y;
        this.isActive = isActive;
        setImage(status);
        addActionListener(e -> {
            actionClick();
        });
    }

    private void actionClick() {
        if (preparingForGameFrame != null) {
            if (preparingForGameFrame.validCellForPreparingPost(X, Y)) {
                setImage(4);
            }
            if (!preparingForGameFrame.getMode()) preparingForGameFrame.remove(X, Y);
        }
        if (onlineGameWindow != null && isActive && onlineGameWindow.getTurnOfUser()) {
            onlineGameWindow.changeTurn();
            onlineGameWindow.changePastMode();
            /*Last symbol in codeOfGameTurn means:
            * 0-shooting
            * 1-exploration*/
            String codeOfGameTurn="";
            switch (onlineGameWindow.getModeStatus()){
                case 0:
                    codeOfGameTurn=X+delimiter+Y+delimiter+0;
                    onlineGameWindow.sendCodeOfGameTurn(codeOfGameTurn);
                    break;
                case 1:

                    break;
                case 3:

                    break;
            }

        }
    }

    public int[] getCoordinates(){
        int[] coordinates=new int[2];
        coordinates[0]=X;
        coordinates[1]=Y;
        return coordinates;
    }

    public void setImage(int status) {
        switch (status) {
            case 1:
                this.status=1;
                setIcon(new ImageIcon(getClass().getResource("../img/seaUnknown.png")));
                break;
            case 2:
                this.status=2;
                setIcon(null);
                setBackground(Color.magenta);
                break;
            case 3:
                this.status=3;
                setIcon(null);
                setBackground(Color.BLACK);
                break;
            case 4:
                this.status = 4;
                setIcon(null);
                setBackground(Color.DARK_GRAY);
                break;
            case 5:
                this.status = 5;
                setIcon(new ImageIcon(getClass().getResource("../img/sea.png")));
                break;
            case 6:
                this.status = 6;
                break;
            case 11:
                setIcon(new ImageIcon(getClass().getResource("../img/ship1.png")));
                break;
            case 211:
                setIcon(new ImageIcon(getClass().getResource("../img/ship211.png")));
                break;
            case 212:
                setIcon(new ImageIcon(getClass().getResource("../img/ship212.png")));
                break;
            case 221:
                setIcon(new ImageIcon(getClass().getResource("../img/ship221.png")));
                break;
            case 222:
                setIcon(new ImageIcon(getClass().getResource("../img/ship222.png")));
                break;
            case 311:
                setIcon(new ImageIcon(getClass().getResource("../img/ship311.png")));
                break;
            case 312:
                setIcon(new ImageIcon(getClass().getResource("../img/ship312.png")));
                break;
            case 313:
                setIcon(new ImageIcon(getClass().getResource("../img/ship313.png")));
                break;
            case 321:
                setIcon(new ImageIcon(getClass().getResource("../img/ship321.png")));
                break;
            case 322:
                setIcon(new ImageIcon(getClass().getResource("../img/ship322.png")));
                break;
            case 323:
                setIcon(new ImageIcon(getClass().getResource("../img/ship323.png")));
                break;
            case 411:
                setIcon(new ImageIcon(getClass().getResource("../img/ship411.png")));
                break;
            case 412:
                setIcon(new ImageIcon(getClass().getResource("../img/ship412.png")));
                break;
            case 413:
                setIcon(new ImageIcon(getClass().getResource("../img/ship413.png")));
                break;
            case 414:
                setIcon(new ImageIcon(getClass().getResource("../img/ship414.png")));
                break;
            case 421:
                setIcon(new ImageIcon(getClass().getResource("../img/ship421.png")));
                break;
            case 422:
                setIcon(new ImageIcon(getClass().getResource("../img/ship422.png")));
                break;
            case 423:
                setIcon(new ImageIcon(getClass().getResource("../img/ship423.png")));
                break;
            case 424:
                setIcon(new ImageIcon(getClass().getResource("../img/ship424.png")));
                break;

        }
    }

    public int getStatus() {
        return status;
    }
}
