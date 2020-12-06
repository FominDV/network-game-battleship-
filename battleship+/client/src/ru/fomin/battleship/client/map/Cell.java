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
     * 6-known cell with ship
     * 0-special status for to processing the code from opponent at the end of the game*/
    static String delimiter = MapBuilder.delimiter;
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
            onlineGameWindow.setPastUsingCellForActionCoordinates(new int[]{X, Y});
            /*Last symbol in codeOfGameTurn means:
             * 0-shooting
             * 1-exploration*/
            /*mode:
             *0-simple shoot
             * 1-volley shoot
             * 2-exploration of the map
             * 3-shooting on straight*/
            if (onlineGameWindow.getModeStatus() == 2) onlineGameWindow.createCodeCellsOfAction(X, Y, 1);
            else onlineGameWindow.createCodeCellsOfAction(X, Y, 0);
        }
    }

    public int[] getCoordinates() {
        int[] coordinates = new int[2];
        coordinates[0] = X;
        coordinates[1] = Y;
        return coordinates;
    }

    public void setImage(int status) {
        switch (status) {
            case 0:
                this.status = 0;
                break;
            case 1:
                this.status = 1;
                setIcon(new ImageIcon(getClass().getResource("../img/seaUnknown.png")));
                break;
            case 2:
                this.status = 2;
                setIcon(new ImageIcon(getClass().getResource("../img/damage.png")));
                break;
            case 3:
                this.status = 3;
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
                //Image about miss
            case 12:
                setIcon(new ImageIcon(getClass().getResource("../img/miss.png")));
                break;
                //Images for ships which are alive
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
            //Images for destroyed ships
            case 110:
                setIcon(new ImageIcon(getClass().getResource("../img/damage1.png")));
                break;
            case 2110:
                setIcon(new ImageIcon(getClass().getResource("../img/damage211.png")));
                break;
            case 2120:
                setIcon(new ImageIcon(getClass().getResource("../img/damage212.png")));
                break;
            case 2210:
                setIcon(new ImageIcon(getClass().getResource("../img/damage221.png")));
                break;
            case 2220:
                setIcon(new ImageIcon(getClass().getResource("../img/damage222.png")));
                break;
            case 3110:
                setIcon(new ImageIcon(getClass().getResource("../img/damage311.png")));
                break;
            case 3120:
                setIcon(new ImageIcon(getClass().getResource("../img/damage312.png")));
                break;
            case 3130:
                setIcon(new ImageIcon(getClass().getResource("../img/damage313.png")));
                break;
            case 3210:
                setIcon(new ImageIcon(getClass().getResource("../img/damage321.png")));
                break;
            case 3220:
                setIcon(new ImageIcon(getClass().getResource("../img/damage322.png")));
                break;
            case 3230:
                setIcon(new ImageIcon(getClass().getResource("../img/damage323.png")));
                break;
            case 4110:
                setIcon(new ImageIcon(getClass().getResource("../img/damage411.png")));
                break;
            case 4120:
                setIcon(new ImageIcon(getClass().getResource("../img/damage412.png")));
                break;
            case 4130:
                setIcon(new ImageIcon(getClass().getResource("../img/damage413.png")));
                break;
            case 4140:
                setIcon(new ImageIcon(getClass().getResource("../img/damage414.png")));
                break;
            case 4210:
                setIcon(new ImageIcon(getClass().getResource("../img/damage421.png")));
                break;
            case 4220:
                setIcon(new ImageIcon(getClass().getResource("../img/damage422.png")));
                break;
            case 4230:
                setIcon(new ImageIcon(getClass().getResource("../img/damage423.png")));
                break;
            case 4240:
                setIcon(new ImageIcon(getClass().getResource("../img/damage424.png")));
                break;
                //cell with ship after exploration
            case 1111:
                setIcon(null);
                setBackground(Color.GREEN);
                break;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setNotActive() {
        if (status != 6) isActive = false;
    }
}
