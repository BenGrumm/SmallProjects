from pydrive.auth import GoogleAuth
import shutil
import os

gauth = GoogleAuth()
# Create local webserver and auto handles authentication.
gauth.LocalWebserverAuth()

from pydrive.drive import GoogleDrive

drive = GoogleDrive(gauth)

cwd = os.getcwd()
driveFolderName = '/DriveDocs'
drivePath = cwd + driveFolderName

def DownloadFIlesInFolder(folder, recursive=True):
	# Auto-iterate through all files that matches this query
	file_list = drive.ListFile({'q': "'%s' in parents and trashed=false" %(folder)}).GetList()

	if not os.path.isdir(drivePath):
		os.mkdir(drivePath)
	
	for file in file_list:
		if file['mimeType'] == 'application/vnd.google-apps.folder' and recursive:
			DownloadFIlesInFolder(file['id'])
		elif file['mimeType'] != 'application/vnd.google-apps.folder':
			print("Downloading and moving " + file['title'])

			#TODO Fix downloading files with / in them

			file.GetContentFile(file['title'], mimetype='text/plain')
			# Move the downloaded file into the correct folder
			shutil.move(cwd + '/' + file['title'], drivePath + '/' + file['title'])

if __name__ == '__main__':
	DownloadFIlesInFolder('root', False)