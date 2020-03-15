from pydrive.drive import GoogleDrive
from pydrive.auth import GoogleAuth

gauth = GoogleAuth()
# Create local webserver and auto handles authentication.
#gauth.LocalWebserverAuth()

drive = GoogleDrive(gauth)

# Auto-iterate through all files that matches this query
file_list = drive.ListFile({'q': "'root' in parents and trashed=false"}).GetList()
for file1 in file_list:
  print('title: %s, id: %s' % (file1['title'], file1['id']))
