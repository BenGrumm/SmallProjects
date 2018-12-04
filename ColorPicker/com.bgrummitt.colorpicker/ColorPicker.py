from desktopmagic.screengrab_win32 import getRectAsImage
import win32gui

def run():
    x, y = win32gui.GetCursorPos()
    print(getColorAtPos(x, y))

def getColorAtPos(x, y):
    rect = getRectAsImage((x, y, x + 1, y + 1))
    return rect.load()[0, 0]

def getColourAtMousePos():
    x, y = win32gui.GetCursorPos()
    tempColour = '#%02x%02x%02x' % getColorAtPos(x, y)
    print(tempColour)
    return tempColour

if __name__ == "__main__":
    run()
