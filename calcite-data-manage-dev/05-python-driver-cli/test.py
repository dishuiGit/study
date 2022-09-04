import phoenixdb.cursor

database_url = 'http://localhost:8765/'
conn = phoenixdb.connect(database_url,
                         authentication='DIGEST',
                         avatica_user='USER1',
                         avatica_password='password1')

cursor = conn.cursor()
cursor.execute("SHOW DATABASES")
print(cursor.fetchall())

cursor = conn.cursor(cursor_factory=phoenixdb.cursor.DictCursor)
cursor.execute("SELECT * FROM sys_user")
print(cursor.fetchone()['user_name'])
