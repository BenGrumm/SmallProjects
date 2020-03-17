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
  print('------------------------------------------------------')
  if file1['mimeType'] == 'application/vnd.google-apps.folder':
  	subfolder_list = drive.ListFile({'q': "'%s' in parents and trashed=false" % (file1['id'])}).GetList()
  	for file2 in subfolder_list:
  		print('title: %s, id: %s, MimeType: %s' % (file2['title'], file2['id'], file2['mimeType']))
  if file1['mimeType'] == 'application/vnd.google-apps.document':
  	file1.GetContentFile(file1['title'], mimetype='text/plain')