from pydrive.auth import GoogleAuth

gauth = GoogleAuth()
# Create local webserver and auto handles authentication.
gauth.LocalWebserverAuth()

from pydrive.drive import GoogleDrive

drive = GoogleDrive(gauth)

# Auto-iterate through all files that matches this query
file_list = drive.ListFile({'q': "'root' in parents and trashed=false"}).GetList()
for file1 in file_list:
  print('title: %s, id: %s, MimeType: %s' % (file1['title'], file1['id'], file1['mimeType']))
  if file1['mimeType'] == 'application/vnd.google-apps.document':
  	file1.GetContentFile(file1['title'], mimetype='text/plain')