from application import app

app.config.update(dict(
    DEBUG=True,
    SECRET_KEY='development key',
    SQLALCHEMY_DATABASE_URI='mysql+gaerdbms:///sns_db?instance=korea-plan:koreaplan',
    migration_directory = "migrations",
))