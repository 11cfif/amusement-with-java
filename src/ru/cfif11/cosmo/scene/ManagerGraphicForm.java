package ru.cfif11.cosmo.scene;

import com.threed.jpct.FrameBuffer;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class ManagerGraphicForm {


    private ArrayList<GraphicForm> graphForms;

    public ManagerGraphicForm(ArrayList<GraphicForm> graphForms) {
        this.graphForms = graphForms;
    }

    public void blit(FrameBuffer buffer) {
        for(GraphicForm gForm : graphForms) {
            gForm.blit(buffer);
        }
    }
}
