# TODO research https://github.com/boppreh/keyboard#keyboard.add_hotkey

import wx
from keyboard import add_hotkey

class MyApp(wx.App):

    def __init__(self):
        wx.App.__init__(self, clearSigInt=True)

        # Initialise Variables
        self.myFrame = None

        # Initialise the frame in the app
        self.InitFrame()

        # Initialise keyboard hot keys
        self.InitHotKeys()

    def InitFrame(self):
        # Have the frames style not allow resizing or maximising
        self.myFrame = MyFrame(parent=None, title="Title", pos=(100, 100), style=wx.DEFAULT_FRAME_STYLE ^ wx.RESIZE_BORDER ^ wx.MAXIMIZE_BOX)
        # Resize the frame
        self.myFrame.SetSize(width=200, height=300)
        self.myFrame.Show()

    def InitHotKeys(self):
        # When the keys Control and Shift And F12 are pressed at once the function GetColor will be called
        add_hotkey("ctrl+shift+f12", self.GetColor)

    @staticmethod
    def GetColor():
        print("Get Colour")

class MyFrame(wx.Frame):

    def __init__(self, parent, title, pos, style):
        wx.Frame.__init__(self, parent=parent, title=title, pos=pos, style=style)

        # Initialise Variables
        self.panel = None

        # Called On Init
        self.OnInit()

    def OnInit(self):
        # Create a new panel with self as parent
        self.panel = MyPanel(parent=self)

class MyPanel(wx.Panel):

    def __init__(self, parent):
        wx.Panel.__init__(self, parent=parent)

        # Add welcome text with label hello, world at 20,20
        self.welcomeText = wx.StaticText(self, id=wx.ID_ANY, label="Hello, World", pos=(0, 0))

if __name__ == "__main__":
    app = MyApp()
    app.MainLoop()
