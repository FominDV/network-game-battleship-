package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.PreparingForGameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Cell extends JButton {
    /*Status:
     * 0-unknown cell/sea
     * 1-empty cell after shoot
     * 2-wounded
     * 3-dead
     * 4-preparing for posting ships
     * 5-known cell free
     * 6-known cell with ship*/
    private int status;
    private final int X;
    private final int Y;
    private PreparingForGameFrame preparingForGameFrame;

    public Cell(int status, PreparingForGameFrame preparingForGameFrame, int x, int y) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setMargin(new Insets(0, 0, 0, 0));
        this.status = status;
        this.preparingForGameFrame = preparingForGameFrame;
        X=x;
        Y=y;
        setImage(status);
        addActionListener(e -> {
            actionClick();
        });
    }

    private void actionClick() {
        if (preparingForGameFrame.validCellForPreparingPost(X,Y)) {
            setImage(4);
        } else {

        }
    }

    public void setImage(int status) {
        this.status = status;
        switch (status) {
            case 1:
                break;
            case 2:
                break;
            case 3:
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
                this.status=6;
                setBackground(Color.RED);
                break;
        }
    }

    public int getStatus() {
        return status;
    }
}
