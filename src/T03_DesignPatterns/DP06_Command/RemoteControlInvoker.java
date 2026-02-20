package T03_DesignPatterns.DP06_Command;

import T03_DesignPatterns.DP06_Command.Command.ICommand;

import java.util.ArrayList;
import java.util.List;

public class RemoteControlInvoker {
    private static final int numberOfButtons = 4;
    private List<ICommand> buttons;
    private List<Boolean> isPressed;

    public RemoteControlInvoker() {
        this.buttons = new ArrayList<>(numberOfButtons);
        this.isPressed = new ArrayList<>(numberOfButtons);
        for (int i = 0; i<numberOfButtons; i++) {
            buttons.add(null);
            isPressed.add(false);
        }
    }

    public void setButton(int index, ICommand command) {
        if (index >=0 && index < numberOfButtons) {
            buttons.set(index, command);
            isPressed.set(index, false);
        }
    }

    public void pressButton(int index) {
        if (index >= 0 && index < numberOfButtons && buttons.get(index) != null) {
            if (!isPressed.get(index)) {
                buttons.get(index).execute();
            } else {
                buttons.get(index).undo();
            }
            isPressed.set(index, !isPressed.get(index));
        } else {
            System.out.println("No command set at " + index + " index value!!!");
        }
    }
}
