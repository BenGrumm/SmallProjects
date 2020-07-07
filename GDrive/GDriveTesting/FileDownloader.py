from pydrive.drive import GoogleDrive
from pydrive.auth import GoogleAuth
import shutil
import sys
import os
import re

drive = None
cwd = os.getcwd()
driveFolderName = '/DriveDocs'
drivePath = cwd + driveFolderName

def authenticate():
	gauth = GoogleAuth()
	# Create local webserver and auto handles authentication.
	gauth.LocalWebserverAuth()

	global drive

	drive = GoogleDrive(gauth)


def findInitFolder(folderName):
	return 'root'

def searchFolder(stringToFind):

	foundString = False
	filesWithString = []

	regexSearch = r"\b%s\b"%(stringToFind)

	for fileName in os.listdir(drivePath):

		# Files downloaded without extensions may cause problems with file types / dirs?
		# if(fileName.endswith('.txt')):
		f = open(drivePath + '/' + fileName, 'r')

		# May produce errors with special regex characters in string
		if re.search(regexSearch, f.read()):
			foundString = True
			filesWithString.append(fileName);

	return foundString, filesWithString

def downloadFIlesInFolder(folder, recursive=True):
	# Auto-iterate through all files that matches this query
	file_list = drive.ListFile({'q': "'%s' in parents and trashed=false" %(folder)}).GetList()

	for file in file_list:

		if file['mimeType'] == 'application/vnd.google-apps.folder' and recursive:
			downloadFIlesInFolder(file['id'])
		elif file['mimeType'] != 'application/vnd.google-apps.folder':

			#TODO Fix downloading files with / in them

			file.GetContentFile(file['title'], mimetype='text/plain')
			# Move the downloaded file into the correct folder
			shutil.move(cwd + '/' + file['title'], drivePath + '/' + file['title'])

def main(folderToSearch, searchString):
	authenticate()

	if not os.path.isdir(drivePath):
		os.mkdir(drivePath)

	downloadFIlesInFolder(findInitFolder(folderToSearch), False)

	succesDown, filePaths = searchFolder(searchString)

	if succesDown:
		print("Found %d files with occurences of the string %s" %(len(filePaths), searchString))
	else:
		print("String Not Found")

	if os.path.isdir(drivePath):
		try:
			shutil.rmtree(drivePath)
		except OSError as e:
			print("Error deleting folder Error: %s" % (e))

if __name__ == '__main__':

	if(len(sys.argv) == 3):
		main(sys.argv[1], sys.argv[2])
	else:
		print('Please provide 2 arguments in the form: python FileDownloader.py [Folder Name To Search] [String To Search]')