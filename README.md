- Basic Hierachy

- PROJECT DIRECTORY
  > Client Project [Android]
    > Android Project  
      > .settings  
      > assets  
      > bin  
      > gen  
      > libs  
      > res  
      > src  
      - .classpath  
      - .project  
      - AndroidManifest.xml  
      - proguard-project.txt  
      - project.properties  
  > Backend Project [Flask with GoogleAppEngine]  
    > application           = Flask App  
      > [controllers]       = receive POST request only  
      > [models]            = Uses GoogleCloudSQL  
    > lib                   = 3rd parth libs for GAE  
      > [library-dirs]  
    - app.yaml              = AppContext for GAE  
    - appengine_config.py   = AppConfig  for GAE  
    - deploy.py             = deploy to GoogleAppEngine   
    - manager.py            = db migration  
