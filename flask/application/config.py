from application import app

app.config.update(dict(
    DEBUG=True,
    SECRET_KEY='development key',
    SQLALCHEMY_DATABASE_URI='mysql+gaerdbms:///wecookbob_db?instance=wecookbob:wecookbobsql',
    migration_directory = "migrations",
))