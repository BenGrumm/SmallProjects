import ColorPicker
import tkinter as gui
import pyHook

class UI(gui.Tk):

    def __init__(self, width, height):
        gui.Tk.__init__(self)
        print("Creating Window")
        # Set height and width of the window
        self.geometry("{}x{}".format(width, height))
        self.title("Title")
        # Don't allow the window to be resize in the x or y
        self.resizable(False, False)
        # Create a new canvas with no border and the height of the window
        self.canvas = gui.Canvas(self, borderwidth=0, bg=None, height=height, width=None)
        # Create a new label with width of 15 characters
        self.label = gui.Label(self, text="Label 1 Extending I will continue with as many chars as necessary", width=15)
        # Place the label on the third row in the first column and center it
        self.label.grid(row=2, column=0, sticky=gui.N+gui.S+gui.E)
        # Place the canvas on the first row in the second column and center it
        self.canvas.grid(row=0, column=1, rowspan=5, sticky=gui.N+gui.E+gui.S+gui.W)
        # Create a listener for keyboard events even for key events when the app is out of focus
        self.hm = pyHook.HookManager()
        self.hm.KeyDown = self.OnKeyboardEvent
        self.hm.HookKeyboard()
        # Initiate variables
        self.rectangles = []
        self.lastEdited = -1

    def createCircle(self, y, r):
        # Add a rectangle to the canvas at the give y with radius r and in the center of the canvas
        self.rectangles.append(self.canvas.create_oval((200 - self.label.winfo_width())/2 - r, y - r, (200 - self.label.winfo_width())/2 + r, y + r, fill=None, outline="black", width=2))

    def editCircle(self, colour):
        # Copy the colour to the clipboard
        self.clipboard_clear()
        self.clipboard_append(colour)
        self.update()
        # Edit the next circle in the rows
        if self.lastEdited < 4:
            self.lastEdited += 1
            self.canvas.itemconfigure(self.rectangles[self.lastEdited], fill=colour)
        else:
            self.lastEdited = 0
            self.canvas.itemconfigure(self.rectangles[self.lastEdited], fill=colour)

    def OnKeyboardEvent(self, event):
        print("Event Detection Test")
        print("{} detected".format(pyHook.HookConstants.IDToName(event.KeyID)))
        if pyHook.GetKeyState(pyHook.HookConstants.VKeyToID('VK_LSHIFT')) > 0 and (pyHook.HookConstants.IDToName(event.KeyID)).upper() == 'F12':
            print("Get Color")
            self.editCircle(ColorPicker.getColourAtMousePos())
        return True

if __name__ == "__main__":
    myUI = UI(200, 300)
    # Update needed to get the label sizes
    myUI.update()
    # Create 5 circles
    for i in range(1, 6):
        myUI.createCircle((i * 50), 20)

    myUI.mainloop()
