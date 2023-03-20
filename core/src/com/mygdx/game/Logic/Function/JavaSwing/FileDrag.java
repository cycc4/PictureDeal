package com.mygdx.game.Logic.Function.JavaSwing;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FileDrag extends JFrame {

    public FileDrag() {
        super("FileDrag");

        final JTextArea area = new JTextArea();
        area.setLineWrap(true);
        add(new JScrollPane(area));

        new DropTarget(area, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dropTargetDropEvent) {
                try {
//                    if (dropTargetDropEvent.isDataFlavorSupported(DataFlavor.plainTextFlavor)) {
                        dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        List<File> list = (List<File>) (dropTargetDropEvent.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                        area.setText("");
                        for (File file : list) {
                            area.append(file.getAbsolutePath());
                            area.append("\r\n");
                        }
                        dropTargetDropEvent.dropComplete(true);
//                    } else {
//                        dropTargetDropEvent.rejectDrop();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
